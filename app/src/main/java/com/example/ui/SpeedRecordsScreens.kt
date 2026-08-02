package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SpeedRecordEntity
import com.example.data.TypingAnalytics
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val INTERVALS = listOf("5s", "10s", "1min")

@Composable
private fun recordIntervalLabel(key: String): String = stringResource(
    when (key) {
        "5s" -> R.string.meter_interval_5s
        "10s" -> R.string.meter_interval_10s
        else -> R.string.meter_interval_1min
    }
)

@Composable
private fun recordUnit(intervalMs: Long): String =
    stringResource(if (intervalMs >= 60000) R.string.meter_unit_cpm else R.string.meter_unit_cps)

private fun formatRecordSpeed(speed: Float): String = String.format(Locale.US, "%.1f", speed)

private fun formatRecordTime(ts: Long): String =
    SimpleDateFormat("MMM d, h:mm a", Locale.getDefault()).format(Date(ts))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedRecordsScreen(onBack: () -> Unit) {
    val db = remember { TypingAnalytics.getDatabase() }
    var records by remember { mutableStateOf<List<SpeedRecordEntity>>(emptyList()) }
    var selectedInterval by remember { mutableIntStateOf(0) }
    var showInfo by remember { mutableStateOf(false) }

    val intervalLabel = INTERVALS[selectedInterval]

    LaunchedEffect(db) {
        db?.speedRecordDao()?.allRecords()?.collectLatest { records = it }
    }

    val intervalRecords = records.filter { it.intervalLabel == intervalLabel }.reversed()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.speed_records_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    IconButton(onClick = { showInfo = true }) {
                        Icon(Icons.Default.Info, contentDescription = stringResource(R.string.meter_rules_title))
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                INTERVALS.forEachIndexed { index, key ->
                    FilterChip(
                        selected = selectedInterval == index,
                        onClick = { selectedInterval = index },
                        label = { Text(recordIntervalLabel(key), fontWeight = if (selectedInterval == index) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            val best = intervalRecords.maxByOrNull { it.speed }
            val maxStreak = intervalRecords.maxOfOrNull { it.streak } ?: 0
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SpeedSummaryCard(
                    value = if (best != null) formatRecordSpeed(best.speed) else "--",
                    subtitle = stringResource(
                        if (intervalLabel == "1min") R.string.meter_unit_cpm else R.string.meter_unit_cps
                    ),
                    label = stringResource(R.string.speed_records_best),
                    icon = Icons.Default.Speed,
                    modifier = Modifier.weight(1f)
                )
                SpeedSummaryCard(
                    value = "$maxStreak",
                    subtitle = stringResource(R.string.speed_records_streak),
                    label = stringResource(R.string.speed_records_best_streak),
                    icon = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionLabel(stringResource(R.string.speed_records_graph_title))
            SpeedRecordBars(intervalRecords)

            Spacer(modifier = Modifier.height(24.dp))

            SectionLabel(stringResource(R.string.speed_records_table_title))

            if (intervalRecords.isEmpty()) {
                EmptyRecordsCard()
            } else {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp)) {
                            Text(stringResource(R.string.speed_records_col_time), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.2f))
                            Text(stringResource(R.string.speed_records_col_words), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.7f), textAlign = TextAlign.Center)
                            Text(stringResource(R.string.speed_records_col_speed), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.8f), textAlign = TextAlign.Center)
                            Text(stringResource(R.string.speed_records_col_streak), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        intervalRecords.forEachIndexed { index, record ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1.2f)) {
                                    Text(formatRecordTime(record.recordAt), fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                    Text(stringResource(R.string.speed_records_record_no, index + 1), fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                                }
                                Text("${record.wordCount}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(0.7f), textAlign = TextAlign.Center)
                                Text(
                                    text = "${formatRecordSpeed(record.speed)} ${recordUnit(record.intervalMs)}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(0.8f),
                                    textAlign = TextAlign.Center
                                )
                                Text("${record.streak}", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary, modifier = Modifier.weight(0.6f), textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }

    if (showInfo) {
        MeterRulesDialog(onDismiss = { showInfo = false })
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 10.dp, start = 4.dp)
    )
}

@Composable
private fun MeterRulesDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.meter_rules_title), fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(stringResource(R.string.meter_rules_line1), fontSize = 14.sp, lineHeight = 21.sp)
                Text(stringResource(R.string.meter_rules_line2), fontSize = 14.sp, lineHeight = 21.sp)
                Text(stringResource(R.string.meter_rules_line3), fontSize = 14.sp, lineHeight = 21.sp)
                Text(stringResource(R.string.meter_rules_line4), fontSize = 14.sp, lineHeight = 21.sp, fontWeight = FontWeight.SemiBold)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.home_got_it))
            }
        }
    )
}

@Composable
private fun SpeedSummaryCard(
    value: String,
    subtitle: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 3.dp, modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(subtitle, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun SpeedRecordBars(records: List<SpeedRecordEntity>) {
    val maxVal = (records.maxOfOrNull { it.speed } ?: 0f).coerceAtLeast(1f)
    val primary = MaterialTheme.colorScheme.primary
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            if (records.isEmpty()) {
                Text(stringResource(R.string.speed_records_empty), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 20.dp))
            } else {
                Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                    val barWidth = size.width / records.size * 0.7f
                    val gap = size.width / records.size * 0.3f
                    records.forEachIndexed { i, record ->
                        val barHeight = (record.speed / maxVal) * (size.height - 30f)
                        val x = i * (barWidth + gap) + gap / 2
                        drawRect(
                            color = Color.Black.copy(alpha = 0.04f),
                            topLeft = Offset(x + 3, size.height - barHeight + 3),
                            size = Size(barWidth, barHeight)
                        )
                        drawRect(
                            brush = Brush.verticalGradient(listOf(primary, primary.copy(alpha = 0.35f))),
                            topLeft = Offset(x, size.height - barHeight),
                            size = Size(barWidth, barHeight)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.speed_records_graph_caption, records.size), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun EmptyRecordsCard() {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Leaderboard, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(stringResource(R.string.speed_records_none), fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(R.string.speed_records_none_hint), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f), textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedLeaderboardScreen(onBack: () -> Unit) {
    val db = remember { TypingAnalytics.getDatabase() }
    var records by remember { mutableStateOf<List<SpeedRecordEntity>>(emptyList()) }

    LaunchedEffect(db) {
        db?.speedRecordDao()?.allRecords()?.collectLatest { records = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.speed_leaderboard_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            if (records.isEmpty()) {
                EmptyRecordsCard()
            } else {
                INTERVALS.forEach { interval ->
                    val intervalRecords = records.filter { it.intervalLabel == interval }.sortedByDescending { it.speed }
                    if (intervalRecords.isEmpty()) return@forEach

                    Text(
                        text = recordIntervalLabel(interval),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 10.dp, start = 4.dp, top = 12.dp)
                    )

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            intervalRecords.take(8).forEachIndexed { index, record ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier.size(28.dp).clip(CircleShape).background(
                                            when (index) {
                                                0 -> Color(0xFFFFC107)
                                                1 -> Color(0xFF9E9E9E)
                                                2 -> Color(0xFFCD7F32)
                                                else -> MaterialTheme.colorScheme.surfaceVariant
                                            }
                                        ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "${index + 1}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = if (index < 3) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            stringResource(R.string.speed_records_new_best, record.wordCount),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(formatRecordTime(record.recordAt), fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            "${formatRecordSpeed(record.speed)} ${recordUnit(record.intervalMs)}",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text("${record.streak}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}