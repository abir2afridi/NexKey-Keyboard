package com.example.ime

import android.widget.Toast
import com.example.R
import com.example.data.SpeedRecordEntity
import com.example.data.TypingAnalytics
import java.util.Locale
import kotlinx.coroutines.launch

internal fun NexKeyInputMethodService.countMeteredWord() {
    if (isTypingActive) burstWordCount++
}

internal fun NexKeyInputMethodService.finalizeSpeedWindow() {
    if (!isTypingActive) return
    isTypingActive = false

    val keys = burstKeyCount
    val words = burstWordCount
    if (keys <= 0) return

    val service = this
    val windowSec = (meterIdleMsState / 1000f).coerceAtLeast(1f)
    val isMinute = meterIntervalState == "1min"
    val label = meterIntervalState
    val cps = keys / windowSec
    val speed = if (isMinute) cps * 60f else cps
    val unit = if (isMinute) service.getString(R.string.meter_unit_cpm) else service.getString(R.string.meter_unit_cps)
    val resultText = service.getString(R.string.meter_result_in, windowSec.toInt(), words)

    scope.launch {
        val dao = TypingAnalytics.getDatabase()?.speedRecordDao()
        if (dao == null) {
            Toast.makeText(service, resultText, Toast.LENGTH_SHORT).show()
            return@launch
        }

        val best = dao.bestForInterval(label)
        val isRecord = best == null || speed > best.speed
        val windowSecInt = windowSec.toInt()

        if (isRecord) {
            val streak = (streakCounter[label] ?: 0) + 1
            streakCounter[label] = streak
            dao.insert(
                SpeedRecordEntity(
                    intervalLabel = label,
                    intervalMs = windowSecInt * 1000L,
                    recordAt = System.currentTimeMillis(),
                    wordCount = words,
                    keyCount = keys,
                    speed = speed,
                    streak = streak
                )
            )
            val message = service.getString(
                R.string.meter_result_record,
                String.format(Locale.US, "%.1f %s", speed, unit),
                streak
            )
            Toast.makeText(service, "$resultText\n$message", Toast.LENGTH_SHORT).show()
        } else {
            streakCounter[label] = 0
            val bestUnit = if (best.intervalMs >= 60000)
                service.getString(R.string.meter_unit_cpm) else service.getString(R.string.meter_unit_cps)
            val message = service.getString(
                R.string.meter_result_best,
                String.format(Locale.US, "%.1f %s", best.speed, bestUnit)
            )
            Toast.makeText(service, "$resultText\n$message", Toast.LENGTH_SHORT).show()
        }
    }
}