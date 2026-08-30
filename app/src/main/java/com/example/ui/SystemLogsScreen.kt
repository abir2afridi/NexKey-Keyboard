package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.debug.AppLogger
import com.example.debug.LogEntry
import com.example.debug.LogLevel
import com.example.debug.ProcessInfo
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private val LevelDebug = Color(0xFF607D8B)
private val LevelInfo = Color(0xFF4CAF50)
private val LevelWarn = Color(0xFFFF9800)
private val LevelError = Color(0xFFF44336)
private val LevelCrash = Color(0xFFD50000)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemLogsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        stringResource(R.string.logs_tab_all),
        stringResource(R.string.logs_tab_errors),
        stringResource(R.string.logs_tab_process)
    )

    var logs by remember { mutableStateOf(AppLogger.getAllLogs()) }
    var errors by remember { mutableStateOf(AppLogger.getErrors()) }
    var processInfo by remember { mutableStateOf<ProcessInfo?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        processInfo = AppLogger.getProcessInfo(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.logs_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    if (selectedTab < 2) {
                        IconButton(onClick = {
                            val text = if (selectedTab == 0) AppLogger.getFormattedLogs()
                            else AppLogger.getErrors().joinToString("\n") {
                                "[${it.formattedDateTime()}] [${it.level}] [${it.tag}] ${it.message}"
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("NexKey Logs", text))
                            Toast.makeText(context, context.getString(R.string.logs_copied), Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = stringResource(R.string.logs_copy))
                        }
                        IconButton(onClick = {
                            if (selectedTab == 0) AppLogger.clearLogs() else AppLogger.clearErrors()
                            logs = AppLogger.getAllLogs()
                            errors = AppLogger.getErrors()
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.logs_clear))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> LogsTab(
                    logs = logs,
                    onRefresh = {
                        logs = AppLogger.getAllLogs()
                    }
                )
                1 -> ErrorsTab(
                    errors = errors,
                    onRefresh = {
                        errors = AppLogger.getErrors()
                    }
                )
                2 -> ProcessTab(
                    processInfo = processInfo,
                    onRefresh = {
                        processInfo = AppLogger.getProcessInfo(context)
                    }
                )
            }
        }
    }
}

@Composable
private fun LogsTab(
    logs: List<LogEntry>,
    onRefresh: () -> Unit
) {
    var filterLevel by remember { mutableStateOf<LogLevel?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredLogs = remember(logs, filterLevel, searchQuery) {
        var result = logs.asReversed()
        if (filterLevel != null) {
            result = result.filter { it.level == filterLevel }
        }
        if (searchQuery.isNotBlank()) {
            val q = searchQuery.lowercase()
            result = result.filter {
                it.message.lowercase().contains(q) || it.tag.lowercase().contains(q)
            }
        }
        result
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(stringResource(R.string.logs_search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Level filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = filterLevel == null,
                onClick = { filterLevel = null },
                label = { Text(stringResource(R.string.logs_filter_all)) }
            )
            LogLevel.entries.forEach { level ->
                FilterChip(
                    selected = filterLevel == level,
                    onClick = { filterLevel = if (filterLevel == level) null else level },
                    label = { Text(level.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (level) {
                            LogLevel.DEBUG -> LevelDebug.copy(alpha = 0.15f)
                            LogLevel.INFO -> LevelInfo.copy(alpha = 0.15f)
                            LogLevel.WARN -> LevelWarn.copy(alpha = 0.15f)
                            LogLevel.ERROR -> LevelError.copy(alpha = 0.15f)
                            LogLevel.CRASH -> LevelCrash.copy(alpha = 0.15f)
                        }
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.logs_count, filteredLogs.size),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )

        if (filteredLogs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.BugReport,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        stringResource(R.string.logs_empty),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            val listState = rememberLazyListState()
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(filteredLogs, key = { "${it.timestamp}-${it.hashCode()}" }) { entry ->
                    LogEntryItem(entry)
                }
            }
        }
    }
}

@Composable
private fun LogEntryItem(entry: LogEntry) {
    var expanded by remember { mutableStateOf(false) }
    val levelColor = when (entry.level) {
        LogLevel.DEBUG -> LevelDebug
        LogLevel.INFO -> LevelInfo
        LogLevel.WARN -> LevelWarn
        LogLevel.ERROR -> LevelError
        LogLevel.CRASH -> LevelCrash
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 0.5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (entry.stackTrace != null) expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(levelColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = entry.formattedTime(),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(8.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = levelColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = entry.level.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = levelColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = entry.tag,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                if (entry.stackTrace != null) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = entry.message,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                entry.stackTrace?.let { trace ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp)
                    ) {
                        Text(
                            text = trace,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorsTab(
    errors: List<LogEntry>,
    onRefresh: () -> Unit
) {
    if (errors.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = LevelInfo.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    stringResource(R.string.logs_no_errors),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stringResource(R.string.logs_no_errors_hint),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontSize = 12.sp
                )
            }
        }
    } else {
        val reversed = errors.asReversed()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.logs_error_count, reversed.size),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            items(reversed, key = { "${it.timestamp}-${it.hashCode()}" }) { entry ->
                LogEntryItem(entry)
            }
        }
    }
}

@Composable
private fun ProcessTab(
    processInfo: ProcessInfo?,
    onRefresh: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.logs_process_info),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                TextButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.logs_refresh))
                }
            }
        }

        if (processInfo == null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            }
        } else {
            item {
                ProcessInfoCard(processInfo)
            }
            item {
                MemoryCard(processInfo)
            }
            item {
                DeviceCard(processInfo)
            }
            item {
                DebugInfoCard()
            }
        }
    }
}

@Composable
private fun ProcessInfoCard(info: ProcessInfo) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.logs_app_info),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            InfoRow(stringResource(R.string.logs_version), "${info.appVersionName} (${info.appVersionCode})")
            InfoRow(stringResource(R.string.logs_pid), info.pid.toString())
            InfoRow(stringResource(R.string.logs_uid), info.uid.toString())
            InfoRow(stringResource(R.string.logs_threads), info.threadCount.toString())
            InfoRow(stringResource(R.string.logs_java), info.javaVersion)
        }
    }
}

@Composable
private fun MemoryCard(info: ProcessInfo) {
    val usedMB = info.usedMemoryBytes / (1024.0 * 1024.0)
    val totalMB = info.totalMemoryBytes / (1024.0 * 1024.0)
    val maxMB = info.maxMemoryBytes / (1024.0 * 1024.0)
    val freeMB = info.freeMemoryBytes / (1024.0 * 1024.0)
    val usagePercent = if (info.totalMemoryBytes > 0) {
        (info.usedMemoryBytes.toFloat() / info.totalMemoryBytes * 100).toInt()
    } else 0

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.logs_memory),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when {
                        usagePercent > 80 -> LevelError.copy(alpha = 0.15f)
                        usagePercent > 60 -> LevelWarn.copy(alpha = 0.15f)
                        else -> LevelInfo.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = "$usagePercent%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            usagePercent > 80 -> LevelError
                            usagePercent > 60 -> LevelWarn
                            else -> LevelInfo
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Memory usage bar
            LinearProgressIndicator(
                progress = { usagePercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = when {
                    usagePercent > 80 -> LevelError
                    usagePercent > 60 -> LevelWarn
                    else -> LevelInfo
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            InfoRow(stringResource(R.string.logs_used), String.format("%.1f MB", usedMB))
            InfoRow(stringResource(R.string.logs_heap), String.format("%.1f MB", totalMB))
            InfoRow(stringResource(R.string.logs_max), String.format("%.1f MB", maxMB))
            InfoRow(stringResource(R.string.logs_free), String.format("%.1f MB", freeMB))
        }
    }
}

@Composable
private fun DeviceCard(info: ProcessInfo) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.logs_device),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            InfoRow(stringResource(R.string.logs_model), info.deviceModel)
            InfoRow(stringResource(R.string.logs_android), "${info.androidVersion} (API ${info.sdkInt})")
        }
    }
}

@Composable
private fun DebugInfoCard() {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.logs_debug_info),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            InfoRow(stringResource(R.string.logs_total_logs), AppLogger.getAllLogs().size.toString())
            InfoRow(stringResource(R.string.logs_total_errors), AppLogger.getErrors().size.toString())
            val debugCount = AppLogger.getLogsByLevel(LogLevel.DEBUG).size
            val infoCount = AppLogger.getLogsByLevel(LogLevel.INFO).size
            val warnCount = AppLogger.getLogsByLevel(LogLevel.WARN).size
            val errorCount = AppLogger.getLogsByLevel(LogLevel.ERROR).size
            val crashCount = AppLogger.getLogsByLevel(LogLevel.CRASH).size
            InfoRow(stringResource(R.string.logs_debug_count), debugCount.toString())
            InfoRow(stringResource(R.string.logs_info_count), infoCount.toString())
            InfoRow(stringResource(R.string.logs_warn_count), warnCount.toString())
            InfoRow(stringResource(R.string.logs_error_count_num), errorCount.toString())
            InfoRow(stringResource(R.string.logs_crash_count), crashCount.toString())
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
