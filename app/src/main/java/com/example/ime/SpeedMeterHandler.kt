package com.example.ime

import android.widget.Toast
import com.example.R
import com.example.data.SpeedRecordEntity
import com.example.data.TypingAnalytics
import java.util.Locale
import kotlinx.coroutines.launch

enum class SpeedMeterPhase { LIVE, WAITING, RESULT }

internal fun NexKeyInputMethodService.countMeteredWord() {
    if (isTypingActive) burstWordCount++
}

internal fun NexKeyInputMethodService.finalizeSpeedWindow() {
    if (!isTypingActive) return
    isTypingActive = false
    elapsedTickerJob?.cancel()
    if (!meterEnabled) {
        meterPhase = SpeedMeterPhase.WAITING
        meterResultLines = emptyList()
        return
    }

    val keys = burstKeyCount
    val trailingWord = if (burstLastChar.length == 1 && burstLastChar[0].isLetter()) 1 else 0
    val words = burstWordCount + trailingWord
    if (keys <= 0) {
        meterPhase = SpeedMeterPhase.WAITING
        meterResultLines = emptyList()
        return
    }

    val service = this
    val windowSec = (meterIdleMsState / 1000f).coerceAtLeast(1f)
    val isMinute = meterIntervalState == "1min"
    val label = meterIntervalState
    val cps = keys / windowSec
    val speed = if (isMinute) cps * 60f else cps
    val unit = if (isMinute) service.getString(R.string.meter_unit_cpm) else service.getString(R.string.meter_unit_cps)

    val lineIn = service.getString(R.string.meter_swipe_in, windowSec.toInt())
    // Key count (default) or sentence word count, chosen in Settings → Speed Meter.
    val lineCount = if (meterCountModeState == "words") {
        service.getString(R.string.meter_swipe_words, words)
    } else {
        service.getString(R.string.meter_swipe_keys, keys)
    }
    val lineSpeed = service.getString(R.string.meter_swipe_speed, String.format(Locale.US, "%.1f %s", speed, unit))

    scope.launch {
        val dao = TypingAnalytics.getDatabase()?.speedRecordDao()
        val best = dao?.bestForInterval(label)
        val isRecord = best == null || speed > best.speed

        if (isRecord) {
            val streak = (streakCounter[label] ?: 0) + 1
            streakCounter[label] = streak
            dao?.insert(
                SpeedRecordEntity(
                    intervalLabel = label,
                    intervalMs = windowSec.toInt() * 1000L,
                    recordAt = System.currentTimeMillis(),
                    wordCount = words,
                    keyCount = keys,
                    speed = speed,
                    streak = streak
                )
            )
            val bestLine = String.format(Locale.US, "%.1f %s", speed, unit)
            meterResultLines = listOf(
                lineIn,
                lineCount,
                lineSpeed,
                service.getString(R.string.meter_swipe_best, bestLine)
            )
            meterPhase = SpeedMeterPhase.RESULT
            if (dao != null) {
                Toast.makeText(
                    service,
                    service.getString(R.string.meter_result_record, bestLine, streak),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            streakCounter[label] = 0
            val bestUnit = if (best.intervalMs >= 60000)
                service.getString(R.string.meter_unit_cpm) else service.getString(R.string.meter_unit_cps)
            meterResultLines = listOf(
                lineIn,
                lineCount,
                lineSpeed,
                service.getString(
                    R.string.meter_swipe_best,
                    String.format(Locale.US, "%.1f %s", best.speed, bestUnit)
                )
            )
            meterPhase = SpeedMeterPhase.RESULT
        }
    }
}