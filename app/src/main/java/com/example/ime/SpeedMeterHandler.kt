package com.example.ime

import android.widget.Toast
import com.example.R
import com.example.data.SpeedRecordEntity
import com.example.data.TypingAnalytics
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class SpeedMeterPhase { LIVE, WAITING, RESULT }

internal fun NexKeyInputMethodService.countMeteredWord() {
    if (speedMeter.isTypingActive) burstWordCount++
}

internal fun NexKeyInputMethodService.finalizeSpeedWindow() {
    if (!speedMeter.isTypingActive) return
    speedMeter = speedMeter.copy(isTypingActive = false)
    if (!meterEnabled && !infoBoxEnabledState) {
        meterPhase = SpeedMeterPhase.WAITING
        meterResultLines = emptyList()
        return
    }

    val keys = burstKeyCount
    val trailingWord = if (speedMeter.burstLastChar.length == 1 && speedMeter.burstLastChar[0].isLetter()) 1 else 0
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
    val lineCount = if (meterCountModeState == "words") {
        service.getString(R.string.meter_swipe_words, words)
    } else {
        service.getString(R.string.meter_swipe_keys, keys)
    }
    val lineSpeed = service.getString(R.string.meter_swipe_speed, String.format(Locale.US, "%.1f %s", speed, unit))

    scope.launch {
        try {
            var isRecord = false
            var recordStreak = 0
            var bestSpeed = 0f
            var bestIntervalMs = 0L
            var dao: com.example.data.SpeedRecordDao? = null

            withContext(Dispatchers.IO) {
                dao = TypingAnalytics.getDatabase()?.speedRecordDao()
                val best = dao?.bestForInterval(label)
                isRecord = best == null || speed > best.speed
                if (best != null) {
                    bestSpeed = best.speed
                    bestIntervalMs = best.intervalMs
                }
                if (isRecord) {
                    val streak = (streakCounter[label] ?: 0) + 1
                    streakCounter[label] = streak
                    recordStreak = streak
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
                } else {
                    streakCounter[label] = 0
                    recordStreak = 0
                }
            }

            if (isRecord) {
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
                        service.getString(R.string.meter_result_record, bestLine, recordStreak),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val bestUnit = if (bestIntervalMs >= 60000)
                    service.getString(R.string.meter_unit_cpm) else service.getString(R.string.meter_unit_cps)
                meterResultLines = listOf(
                    lineIn,
                    lineCount,
                    lineSpeed,
                    service.getString(
                        R.string.meter_swipe_best,
                        String.format(Locale.US, "%.1f %s", bestSpeed, bestUnit)
                    )
                )
                meterPhase = SpeedMeterPhase.RESULT
            }
        } catch (e: Exception) {
            android.util.Log.e("SpeedMeter", "DB operation failed", e)
            meterPhase = SpeedMeterPhase.WAITING
            meterResultLines = emptyList()
        }
    }
}
