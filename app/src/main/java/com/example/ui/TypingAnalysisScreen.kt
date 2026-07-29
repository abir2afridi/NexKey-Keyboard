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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DailyStatsEntity
import com.example.data.EmojiUsageEntity
import com.example.data.LearnedWordEntity
import com.example.data.TypingAnalytics
import com.example.data.UserPreferences
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingAnalysisScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val totalWords by prefs.totalWords.collectAsState(initial = 0)
    val totalChars by prefs.totalChars.collectAsState(initial = 0)
    val timeSaved = ((totalChars / 5) * 0.5).toInt()
    val avgRpm = if (timeSaved > 0) ((totalWords.toFloat() / (timeSaved / 60f))).toInt() else 0

    val db = remember { TypingAnalytics.getDatabase() }
    var dailyStats by remember { mutableStateOf<List<DailyStatsEntity>>(emptyList()) }
    var topEmojis by remember { mutableStateOf<List<EmojiUsageEntity>>(emptyList()) }
    var topWords by remember { mutableStateOf<List<LearnedWordEntity>>(emptyList()) }
    var selectedPeriod by remember { mutableIntStateOf(0) }
    val periods = listOf("Week", "Month", "Year")

    val dailyStatsFlow = remember(db) { db?.typingSessionDao()?.getAllDailyStats() }

    LaunchedEffect(Unit) {
        dailyStatsFlow?.collect { dailyStats = it }
    }

    LaunchedEffect(db) {
        db?.let {
            topEmojis = it.emojiUsageDao().getTopEmojis(15)
            topWords = it.learnedWordDao().getAllWords().take(20)
        }
    }

    // Compute chart data from daily stats
    val weekData = remember(dailyStats, selectedPeriod) {
        val cal = Calendar.getInstance()
        val now = cal.timeInMillis
        val cutoff = when (selectedPeriod) {
            0 -> now - 7 * 24 * 3600 * 1000L
            1 -> now - 30 * 24 * 3600 * 1000L
            else -> now - 365 * 24 * 3600 * 1000L
        }
        dailyStats.filter { it.date >= SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(cutoff)) }
    }

    // Monthly aggregation for pie chart (last 12 months)
    val monthlyData = remember(dailyStats) {
        val cal = Calendar.getInstance()
        val months = mutableMapOf<String, Int>()
        for (i in 0 until 12) {
            cal.timeInMillis = System.currentTimeMillis()
            cal.add(Calendar.MONTH, -i)
            val key = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)
            months[key] = 0
        }
        dailyStats.forEach { stat ->
            val month = stat.date.take(7)
            if (months.containsKey(month)) {
                months[month] = (months[month] ?: 0) + stat.usageMinutes
            }
        }
        months.entries.sortedBy { it.key }.takeLast(12)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Typing Analysis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .padding(16.dp)
        ) {
            // Summary Cards
            Text("Overview", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AnalysisStatCard("Words", totalWords.toString(), Icons.Default.TextFields, Modifier.weight(1f))
                AnalysisStatCard("Chars", totalChars.toString(), Icons.Default.Keyboard, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                AnalysisStatCard("Avg RPM", "$avgRpm", Icons.Default.Speed, Modifier.weight(1f))
                AnalysisStatCard("Time Saved", "${timeSaved}s", Icons.Default.Timer, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Period Selector
            Text("Usage Charts", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                periods.forEachIndexed { i, label ->
                    FilterChip(
                        selected = selectedPeriod == i,
                        onClick = { selectedPeriod = i },
                        label = { Text(label, fontSize = 12.sp) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Daily Usage Bar Chart
            Text("Daily Keyboard Usage", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 4.dp))
            if (weekData.isEmpty()) {
                Text("Start typing to see usage data", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
            } else {
                BarChart(
                    data = weekData.map { Pair(it.date.takeLast(5), it.usageMinutes) },
                    label = "min",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sessions per Day
            Text("Sessions Per Day", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 4.dp))
            if (weekData.isEmpty()) {
                Text("Start typing to see session data", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
            } else {
                BarChart(
                    data = weekData.map { Pair(it.date.takeLast(5), it.sessionCount) },
                    label = "sessions",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Pie Chart
            Text("Monthly Distribution (12 months)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(vertical = 4.dp))
            if (monthlyData.all { it.value == 0 }) {
                Text("Start typing to see monthly data", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
            } else {
                PieChart(data = monthlyData.map { Pair(it.key.takeLast(2), it.value) })
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Most Used Words
            Text("Most Used Words", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
            if (topWords.isEmpty()) {
                Text("Start typing to see word frequency", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
            } else {
                WordFrequencyGrid(words = topWords.take(12))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Most Used Emojis
            Text("Most Used Emojis", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
            if (topEmojis.isEmpty()) {
                Text("Start typing emojis to see emoji frequency", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(8.dp))
            } else {
                EmojiChart(emojis = topEmojis.take(12))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AnalysisStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = modifier) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun BarChart(data: List<Pair<String, Int>>, label: String, color: Color) {
    val maxVal = (data.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                val barWidth = size.width / data.size * 0.6f
                val gap = size.width / data.size * 0.4f
                data.forEachIndexed { i, (_, value) ->
                    val barHeight = (value.toFloat() / maxVal) * (size.height - 20f)
                    val x = i * (barWidth + gap) + gap / 2
                    drawRect(
                        color = color,
                        topLeft = Offset(x, size.height - barHeight),
                        size = Size(barWidth, barHeight)
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                data.takeLast(7).forEach { (day, _) ->
                    Text(day, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun PieChart(data: List<Pair<String, Int>>) {
    val total = data.sumOf { it.second }.toFloat().coerceAtLeast(1f)
    val colors = listOf(
        Color(0xFF1B5E20), Color(0xFF2E7D32), Color(0xFF388E3C), Color(0xFF43A047),
        Color(0xFF4CAF50), Color(0xFF66BB6A), Color(0xFF81C784), Color(0xFFA5D6A7),
        Color(0xFFC8E6C9), Color(0xFFE8F5E9), Color(0xFFF1F8E9), Color(0xFFF9FBE7)
    )
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp)) {
            Canvas(modifier = Modifier.size(140.dp)) {
                var startAngle = -90f
                data.forEachIndexed { i, (_, value) ->
                    val sweepAngle = (value / total) * 360f
                    drawArc(
                        color = colors[i % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        size = Size(size.width, size.height)
                    )
                    startAngle += sweepAngle
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                data.forEachIndexed { i, (month, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(colors[i % colors.size]))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("$month: ${(value / total * 100).toInt()}%", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun WordFrequencyGrid(words: List<LearnedWordEntity>) {
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Top words by frequency", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
            // Keyboard-like grid showing words with position numbers
            val rows = words.chunked(4)
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEachIndexed { _, word ->
                        val index = words.indexOf(word) + 1
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.weight(1f).heightIn(min = 48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(4.dp)) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(word.word, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
                                    Text("#$index", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("${word.frequency}x", fontSize = 9.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                    // Fill empty slots
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun EmojiChart(emojis: List<EmojiUsageEntity>) {
    val maxFreq = (emojis.maxOfOrNull { it.frequency } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(12.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            emojis.forEach { emoji ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji.emoji, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.weight(1f).height(20.dp)) {
                        val fraction = emoji.frequency.toFloat() / maxFreq
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${emoji.frequency}x", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
