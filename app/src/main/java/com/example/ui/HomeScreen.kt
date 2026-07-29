package com.example.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    appTheme: String,
    onToggleTheme: () -> Unit,
    onNavigateToSetup: () -> Unit,
    onNavigateToSandbox: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToClipboard: () -> Unit = {},
    onNavigateToTextCorrection: () -> Unit = {},
    onNavigateToMoreLanguages: () -> Unit = {},
    onNavigateToGifQuality: () -> Unit = {}
) {
    val context = LocalContext.current
    val isEnabled = checkIsKeyboardEnabled(context)
    val isSelected = checkIsKeyboardSelected(context)
    val isActive = isEnabled && isSelected

    val db = remember { TypingAnalytics.getDatabase() }
    var dailyStats by remember { mutableStateOf<List<DailyStatsEntity>>(emptyList()) }
    var topEmojis by remember { mutableStateOf<List<EmojiUsageEntity>>(emptyList()) }
    var topWords by remember { mutableStateOf<List<LearnedWordEntity>>(emptyList()) }
    var recentSessions by remember { mutableStateOf<List<TypingSessionEntity>>(emptyList()) }
    
    var selectedChartPeriod by remember { mutableIntStateOf(0) } // 0: Week, 1: Month
    
    LaunchedEffect(db) {
        db?.let { database ->
            database.typingSessionDao().getAllDailyStats().collectLatest { dailyStats = it }
        }
    }
    
    LaunchedEffect(db) {
        db?.let { database ->
            topEmojis = database.emojiUsageDao().getTopEmojis(10)
            topWords = database.learnedWordDao().getAllWords().take(12)
            recentSessions = database.typingSessionDao().getSessionsSinceList(System.currentTimeMillis() - 24 * 3600 * 1000)
        }
    }

    // RPM and CPS calculation
    val rpm = remember(recentSessions) {
        if (recentSessions.isEmpty()) 0
        else {
            val totalWordsSession = recentSessions.sumOf { it.wordCount }
            val totalMinutes = recentSessions.sumOf { (it.endTime - it.startTime) } / 60000f
            if (totalMinutes > 0) (totalWordsSession / totalMinutes).toInt() else 0
        }
    }
    val cps = remember(recentSessions) {
        if (recentSessions.isEmpty()) 0f
        else {
            val totalKeys = recentSessions.sumOf { it.keyCount }
            val totalSeconds = recentSessions.sumOf { (it.endTime - it.startTime) } / 1000f
            if (totalSeconds > 0) totalKeys / totalSeconds else 0f
        }
    }

    // Infinite breathing pulsing animation for Active status dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Fixed Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.icon_header),
                    contentDescription = "NexKey",
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "NexKey",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Vibrant. Fast. Original.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = when (appTheme) {
                            "DARK" -> Icons.Default.DarkMode
                            "LIGHT" -> Icons.Default.LightMode
                            else -> Icons.Default.BrightnessAuto
                        },
                        contentDescription = "Toggle theme",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isActive) MaterialTheme.colorScheme.primary else Color(0xFFFF9800),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .alpha(if (isActive) pulseAlpha else 1f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isActive) "ACTIVE" else "SETUP",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Primary Action
            Card(
                onClick = { if (isActive) onNavigateToSandbox() else onNavigateToSetup() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                        spotColor = Color.Black.copy(alpha = 0.08f)
                    ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                if (isActive) listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                                else listOf(Color(0xFFFF9800), Color(0xFFE65100))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isActive) Icons.Default.PlayArrow else Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isActive) "Typing Sandbox" else "Complete Setup",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // TODAY'S PULSE (Live Metrics)
            Text(
                text = "TODAY'S PULSE",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("$rpm", "Avg RPM", MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, Icons.Default.Speed, Modifier.weight(1f))
                StatCard(String.format(Locale.getDefault(), "%.1f", cps), "Keys/Sec", MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary, Icons.Default.Bolt, Modifier.weight(1f))
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // USAGE TRENDS (Bar Charts)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "USAGE TRENDS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Row {
                    TextButton(onClick = { selectedChartPeriod = 0 }) {
                        Text("Week", fontSize = 11.sp, color = if (selectedChartPeriod == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = { selectedChartPeriod = 1 }) {
                        Text("Month", fontSize = 11.sp, color = if (selectedChartPeriod == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            val chartData = remember(dailyStats, selectedChartPeriod) {
                val cal = Calendar.getInstance()
                val daysToLookBack = if (selectedChartPeriod == 0) 7 else 30
                val data = mutableListOf<Pair<String, Int>>()
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                for (i in (daysToLookBack - 1) downTo 0) {
                    cal.time = Date()
                    cal.add(Calendar.DAY_OF_YEAR, -i)
                    val dateStr = sdf.format(cal.time)
                    val stat = dailyStats.find { it.date == dateStr }
                    data.add(Pair(dateStr.takeLast(2), stat?.usageMinutes ?: 0))
                }
                data
            }

            DeepBarChart(data = chartData, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(24.dp))

            // YEARLY DISTRIBUTION (Pie Chart)
            Text(
                text = "YEARLY DISTRIBUTION",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            val monthlyData = remember(dailyStats) {
                val months = mutableMapOf<String, Int>()
                val cal = Calendar.getInstance()
                val sdfMonth = SimpleDateFormat("MMM", Locale.US)
                val sdfKey = SimpleDateFormat("yyyy-MM", Locale.US)
                for (i in 0 until 6) { // Show last 6 months for better pie visibility
                    cal.time = Date()
                    cal.add(Calendar.MONTH, -i)
                    months[sdfMonth.format(cal.time)] = dailyStats.filter { it.date.startsWith(sdfKey.format(cal.time)) }.sumOf { it.usageMinutes }
                }
                months.toList().reversed()
            }

            DeepPieChart(data = monthlyData)

            Spacer(modifier = Modifier.height(24.dp))

            // KEYBOARD WORD HEATMAP
            Text(
                text = "WORD HEATMAP",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            MiniKeyboardHeatmap(topWords)

            Spacer(modifier = Modifier.height(24.dp))

            // EMOJI LEADERBOARD
            Text(
                text = "EMOJI LEADERBOARD",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            EmojiUsageChart(topEmojis)

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NexKey Pro • Analytics v2.0",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, bgColor: Color, iconTint: Color, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 2.dp, modifier = modifier) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(bgColor), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DeepBarChart(data: List<Pair<String, Int>>, color: Color) {
    val maxVal = (data.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                val barWidth = size.width / data.size * 0.7f
                val gap = size.width / data.size * 0.3f
                data.forEachIndexed { i, (_, value) ->
                    val barHeight = (value.toFloat() / maxVal) * (size.height - 30f)
                    val x = i * (barWidth + gap) + gap / 2
                    
                    // Rounded bar with gradient
                    drawRect(
                        brush = Brush.verticalGradient(listOf(color, color.copy(alpha = 0.4f))),
                        topLeft = Offset(x, size.height - barHeight),
                        size = Size(barWidth, barHeight)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val labelStep = if (data.size > 7) 5 else 1
                data.forEachIndexed { i, (label, _) ->
                    if (i % labelStep == 0) {
                        Text(label, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun DeepPieChart(data: List<Pair<String, Int>>) {
    val total = data.sumOf { it.second }.toFloat().coerceAtLeast(1f)
    val colors = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary, 
        MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f), MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
    )
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(120.dp)) {
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
                // Donut hole
                drawCircle(color = Color.White, radius = size.width / 4f)
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                data.forEachIndexed { i, (label, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(colors[i % colors.size]))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$label: ${((value / total) * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniKeyboardHeatmap(words: List<LearnedWordEntity>) {
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Top words mapped to layout", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 12.dp))
            
            val rows = words.chunked(4).take(3)
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    row.forEach { word ->
                        val intensity = (word.frequency.toFloat() / (words.firstOrNull()?.frequency ?: 1)).coerceIn(0.1f, 1f)
                        Surface(
                            modifier = Modifier.weight(1f).height(45.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = intensity),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                Text(word.word, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (intensity > 0.6f) Color.White else MaterialTheme.colorScheme.onSurface, maxLines = 1)
                                Text("${word.frequency}", fontSize = 9.sp, color = if (intensity > 0.6f) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            
            // Spacebar style word (Top 1)
            words.firstOrNull()?.let { topWord ->
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                ) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(topWord.word, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${topWord.frequency}x", fontSize = 11.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmojiUsageChart(emojis: List<EmojiUsageEntity>) {
    val maxFreq = (emojis.maxOfOrNull { it.frequency } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            emojis.forEach { emoji ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji.emoji, fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f).height(24.dp).clip(RoundedCornerShape(6.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                        val fraction = emoji.frequency.toFloat() / maxFreq
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("${emoji.frequency}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}


