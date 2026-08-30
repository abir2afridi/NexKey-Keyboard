package com.example.debug

import android.os.Build
import android.os.Debug
import android.os.Process
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

enum class LogLevel { DEBUG, INFO, WARN, ERROR, CRASH }

data class LogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val level: LogLevel,
    val tag: String,
    val message: String,
    val stackTrace: String? = null
) {
    fun formattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
        return sdf.format(Date(timestamp))
    }

    fun formattedDateTime(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        return sdf.format(Date(timestamp))
    }
}

data class ProcessInfo(
    val pid: Int,
    val uid: Int,
    val processName: String,
    val totalMemoryBytes: Long,
    val usedMemoryBytes: Long,
    val maxMemoryBytes: Long,
    val freeMemoryBytes: Long,
    val threadCount: Int,
    val javaVersion: String,
    val androidVersion: String,
    val sdkInt: Int,
    val deviceModel: String,
    val appVersionName: String,
    val appVersionCode: Long
)

object AppLogger {

    private const val MAX_LOG_ENTRIES = 500
    private const val MAX_ERROR_ENTRIES = 200

    private val logs = CopyOnWriteArrayList<LogEntry>()
    private val errors = CopyOnWriteArrayList<LogEntry>()

    fun d(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.INFO, tag, message)
    fun w(tag: String, message: String) = log(LogLevel.WARN, tag, message)
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        val trace = throwable?.let { getStackTrace(it) }
        log(LogLevel.ERROR, tag, message, trace)
    }

    fun crash(tag: String, message: String, throwable: Throwable) {
        val trace = getStackTrace(throwable)
        log(LogLevel.CRASH, tag, message, trace)
    }

    fun captureUncaught(tag: String, thread: Thread, throwable: Throwable) {
        val trace = getStackTrace(throwable)
        val entry = LogEntry(
            level = LogLevel.CRASH,
            tag = tag,
            message = "Uncaught in ${thread.name}: ${throwable.message ?: "Unknown"}",
            stackTrace = trace
        )
        synchronized(errors) {
            errors.add(entry)
            while (errors.size > MAX_ERROR_ENTRIES) errors.removeAt(0)
        }
        synchronized(logs) {
            logs.add(entry)
            while (logs.size > MAX_LOG_ENTRIES) logs.removeAt(0)
        }
    }

    fun getAllLogs(): List<LogEntry> = logs.toList()

    fun getErrors(): List<LogEntry> = errors.toList()

    fun getLogsByLevel(level: LogLevel): List<LogEntry> = logs.filter { it.level == level }

    fun getLogsByTag(tag: String): List<LogEntry> = logs.filter { it.tag == tag }

    fun searchLogs(query: String): List<LogEntry> {
        val q = query.lowercase()
        return logs.filter {
            it.message.lowercase().contains(q) ||
                it.tag.lowercase().contains(q) ||
                (it.stackTrace?.lowercase()?.contains(q) == true)
        }
    }

    fun clearLogs() {
        logs.clear()
    }

    fun clearErrors() {
        errors.clear()
    }

    fun clearAll() {
        logs.clear()
        errors.clear()
    }

    fun getProcessInfo(androidContext: android.content.Context): ProcessInfo {
        val runtime = Runtime.getRuntime()
        val pm = androidContext.packageManager
        val pkgInfo = pm.getPackageInfo(androidContext.packageName, 0)
        val versionName = pkgInfo.versionName ?: "unknown"
        @Suppress("DEPRECATION")
        val versionCode = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            pkgInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pkgInfo.versionCode.toLong()
        }

        val memInfo = Debug.MemoryInfo()
        Debug.getMemoryInfo(memInfo)

        return ProcessInfo(
            pid = Process.myPid(),
            uid = Process.myUid(),
            processName = androidContext.packageName,
            totalMemoryBytes = runtime.totalMemory(),
            usedMemoryBytes = memInfo.totalPrivateDirty * 1024L,
            maxMemoryBytes = runtime.maxMemory(),
            freeMemoryBytes = runtime.freeMemory(),
            threadCount = Thread.activeCount(),
            javaVersion = System.getProperty("java.version") ?: "unknown",
            androidVersion = Build.VERSION.RELEASE,
            sdkInt = Build.VERSION.SDK_INT,
            deviceModel = "${Build.MANUFACTURER} ${Build.MODEL}",
            appVersionName = versionName,
            appVersionCode = versionCode
        )
    }

    fun getFormattedLogs(): String {
        val sb = StringBuilder()
        sb.appendLine("=== NexKey System Logs ===")
        sb.appendLine("Exported: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date())}")
        sb.appendLine("Total entries: ${logs.size}")
        sb.appendLine("Errors: ${errors.size}")
        sb.appendLine("=".repeat(40))
        sb.appendLine()
        for (entry in logs) {
            sb.appendLine("[${entry.formattedDateTime()}] [${entry.level}] [${entry.tag}] ${entry.message}")
            entry.stackTrace?.let { sb.appendLine("  STACKTRACE: $it") }
            sb.appendLine()
        }
        return sb.toString()
    }

    private fun log(level: LogLevel, tag: String, message: String, stackTrace: String? = null) {
        val entry = LogEntry(
            level = level,
            tag = tag,
            message = message,
            stackTrace = stackTrace
        )
        synchronized(logs) {
            logs.add(entry)
            while (logs.size > MAX_LOG_ENTRIES) logs.removeAt(0)
        }
        if (level == LogLevel.ERROR || level == LogLevel.CRASH) {
            synchronized(errors) {
                errors.add(entry)
                while (errors.size > MAX_ERROR_ENTRIES) errors.removeAt(0)
            }
        }
    }

    private fun getStackTrace(throwable: Throwable): String {
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}
