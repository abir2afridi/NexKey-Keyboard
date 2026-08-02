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
import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

enum class DashboardSection(val icon: ImageVector) {
    PULSE(Icons.Default.Speed),
    TRENDS(Icons.Default.BarChart),
    DISTRIBUTION(Icons.Default.PieChart),
    HEATMAP(Icons.Default.Keyboard),
    EMOJIS(Icons.Default.Face)
}

@Composable
fun DashboardSection.title(): String = stringResource(
    when (this) {
        DashboardSection.PULSE -> R.string.home_pulse_title
        DashboardSection.TRENDS -> R.string.home_trends_title
        DashboardSection.DISTRIBUTION -> R.string.home_distribution_title
        DashboardSection.HEATMAP -> R.string.home_heatmap_title
        DashboardSection.EMOJIS -> R.string.home_emojis_title
    }
)

@Composable
fun DashboardSection.description(): String = stringResource(
    when (this) {
        DashboardSection.PULSE -> R.string.home_pulse_desc
        DashboardSection.TRENDS -> R.string.home_trends_desc
        DashboardSection.DISTRIBUTION -> R.string.home_distribution_desc
        DashboardSection.HEATMAP -> R.string.home_heatmap_desc
        DashboardSection.EMOJIS -> R.string.home_emojis_desc
    }
)

@OptIn(ExperimentalMaterial3Api::class)
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

    val prefs = remember { UserPreferences(context) }
    val haptics by prefs.haptics.collectAsState(initial = true)
    val sound by prefs.sound.collectAsState(initial = true)
    val autoCorrection by prefs.autoCorrection.collectAsState(initial = true)
    val showNumberRow by prefs.showNumberRow.collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    
    val db = remember { TypingAnalytics.getDatabase() }
    var dailyStats by remember { mutableStateOf<List<DailyStatsEntity>>(emptyList()) }
    var topEmojis by remember { mutableStateOf<List<EmojiUsageEntity>>(emptyList()) }
    var topWords by remember { mutableStateOf<List<LearnedWordEntity>>(emptyList()) }
    var recentSessions by remember { mutableStateOf<List<TypingSessionEntity>>(emptyList()) }
    
    var selectedChartPeriod by remember { mutableIntStateOf(0) } // 0: Week, 1: Month
    
    var showInfoForSection by remember { mutableStateOf<DashboardSection?>(null) }
    val sheetState = rememberModalBottomSheetState()
    
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
    val activeMinutesToday = remember(dailyStats) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        dailyStats.find { it.date == today }?.usageMinutes ?: 0
    }

    // Greeting logic
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> stringResource(R.string.home_greeting_morning)
        in 12..16 -> stringResource(R.string.home_greeting_afternoon)
        in 17..20 -> stringResource(R.string.home_greeting_evening)
        else -> stringResource(R.string.home_greeting_night)
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
            .statusBarsPadding()
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
                        text = "$greeting!",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
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
                        contentDescription = stringResource(R.string.home_toggle_theme),
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
                            text = if (isActive) stringResource(R.string.home_active) else stringResource(R.string.home_setup_badge),
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
                    .height(64.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                if (isActive) listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f))
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
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (isActive) stringResource(R.string.home_typing_sandbox) else stringResource(R.string.home_complete_setup),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // QUICK CONTROLS
            Text(
                text = stringResource(R.string.home_quick_controls),
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ControlTile(stringResource(R.string.home_haptics), Icons.Default.Vibration, haptics, Modifier.weight(1f)) { scope.launch { prefs.setHaptics(!haptics) } }
                ControlTile(stringResource(R.string.home_sound), Icons.Default.VolumeUp, sound, Modifier.weight(1f)) { scope.launch { prefs.setSound(!sound) } }
                ControlTile(stringResource(R.string.home_autofix), Icons.Default.AutoFixHigh, autoCorrection, Modifier.weight(1f)) { scope.launch { prefs.setAutoCorrection(!autoCorrection) } }
                ControlTile(stringResource(R.string.home_numbers), Icons.Default.Numbers, showNumberRow, Modifier.weight(1f)) { scope.launch { prefs.setShowNumberRow(!showNumberRow) } }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // TODAY'S PULSE
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_today_pulse),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = { showInfoForSection = DashboardSection.PULSE },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("$rpm", stringResource(R.string.home_avg_rpm), MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.primary, Icons.Default.Speed, Modifier.weight(1f))
                StatCard(String.format(Locale.getDefault(), "%.1f", cps), stringResource(R.string.home_keys_sec), MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary, Icons.Default.Bolt, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            StatCard("$activeMinutesToday ${stringResource(R.string.home_mins)}", stringResource(R.string.home_active_time), MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.secondary, Icons.Default.HourglassBottom, Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(32.dp))

            // USAGE TRENDS
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.home_usage_trends),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { showInfoForSection = DashboardSection.TRENDS },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                    }
                }
                Row {
                    TextButton(onClick = { selectedChartPeriod = 0 }) {
                        Text(stringResource(R.string.home_week), fontSize = 11.sp, fontWeight = if (selectedChartPeriod == 0) FontWeight.Bold else FontWeight.Normal, color = if (selectedChartPeriod == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    TextButton(onClick = { selectedChartPeriod = 1 }) {
                        Text(stringResource(R.string.home_month), fontSize = 11.sp, fontWeight = if (selectedChartPeriod == 1) FontWeight.Bold else FontWeight.Normal, color = if (selectedChartPeriod == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
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

            Spacer(modifier = Modifier.height(32.dp))

            // YEARLY DISTRIBUTION
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_yearly_distribution),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = { showInfoForSection = DashboardSection.DISTRIBUTION },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                }
            }

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

            Spacer(modifier = Modifier.height(32.dp))

            // KEYBOARD WORD HEATMAP
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_word_heatmap),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = { showInfoForSection = DashboardSection.HEATMAP },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                }
            }

            MiniKeyboardHeatmap(topWords)

            Spacer(modifier = Modifier.height(32.dp))

            // EMOJI LEADERBOARD
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp, start = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.home_emoji_leaderboard),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick = { showInfoForSection = DashboardSection.EMOJIS },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                }
            }

            EmojiUsageChart(topEmojis)

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.home_footer),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Info Bottom Sheet
        if (showInfoForSection != null) {
            ModalBottomSheet(
                onDismissRequest = { showInfoForSection = null },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = showInfoForSection?.icon ?: Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = showInfoForSection?.title() ?: "",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = showInfoForSection?.description() ?: "",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = { showInfoForSection = null },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.home_got_it))
                    }
                }
            }
        }
    }
}

@Composable
private fun ControlTile(label: String, icon: ImageVector, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bgColor by animateColorAsState(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), label = "bgColor")
    val contentColor by animateColorAsState(if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant, label = "contentColor")
    
    Surface(
        onClick = onClick,
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
        tonalElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp)
        ) {
            Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = contentColor, maxLines = 1)
        }
    }
}

@Composable
private fun StatCard(
    value: String, 
    label: String, 
    bgColor: Color, 
    iconTint: Color, 
    icon: ImageVector, 
    modifier: Modifier = Modifier,
    onInfoClick: (() -> Unit)? = null
) {
    Surface(
        shape = RoundedCornerShape(16.dp), 
        color = MaterialTheme.colorScheme.surface, 
        tonalElevation = 3.dp, 
        modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (onInfoClick != null) {
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(16.dp)
                ) {
                    Icon(
                        Icons.Default.Info, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
            
            Row(modifier = Modifier.padding(18.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(bgColor), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(value, fontSize = 22.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurface)
                    Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
private fun DeepBarChart(data: List<Pair<String, Int>>, color: Color) {
    val maxVal = (data.maxOfOrNull { it.second } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                val barWidth = size.width / data.size * 0.75f
                val gap = size.width / data.size * 0.25f
                data.forEachIndexed { i, (_, value) ->
                    val barHeight = (value.toFloat() / maxVal) * (size.height - 40f)
                    val x = i * (barWidth + gap) + gap / 2
                    
                    // Shadow effect
                    drawRect(
                        color = Color.Black.copy(alpha = 0.05f),
                        topLeft = Offset(x + 4, size.height - barHeight + 4),
                        size = Size(barWidth, barHeight)
                    )
                    // Gradient bar
                    drawRect(
                        brush = Brush.verticalGradient(listOf(color, color.copy(alpha = 0.3f))),
                        topLeft = Offset(x, size.height - barHeight),
                        size = Size(barWidth, barHeight)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val labelStep = if (data.size > 7) 5 else 1
                data.forEachIndexed { i, (label, _) ->
                    if (i % labelStep == 0) {
                        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
        MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.error,
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
    )
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(24.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(130.dp)) {
                Canvas(modifier = Modifier.fillMaxSize()) {
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
                    drawCircle(color = Color.White, radius = size.width / 3.2f)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${total.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    Text(stringResource(R.string.home_mins), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.width(28.dp))
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                data.forEachIndexed { i, (label, value) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(colors[i % colors.size]))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("$label: ${((value / total) * 100).toInt()}%", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun MiniKeyboardHeatmap(words: List<LearnedWordEntity>) {
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(stringResource(R.string.home_heatmap_caption), fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 16.dp))

            val rows = words.chunked(4).take(3)
            rows.forEach { row ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEach { word ->
                        val intensity = (word.frequency.toFloat() / (words.firstOrNull()?.frequency ?: 1)).coerceIn(0.1f, 1f)
                        Surface(
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = intensity),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(2.dp)) {
                                Text(word.word, fontSize = 11.sp, fontWeight = FontWeight.Black, color = if (intensity > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface, maxLines = 1)
                                Text("${word.frequency}", fontSize = 9.sp, color = if (intensity > 0.5f) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                    repeat(4 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
            
            // Spacebar style word (Top 1)
            words.firstOrNull()?.let { topWord ->
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    shadowElevation = 2.dp
                ) {
                    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(topWord.word, fontSize = 15.sp, fontWeight = FontWeight.Black, color = Color.White)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("${topWord.frequency} ${stringResource(R.string.home_uses)}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmojiUsageChart(emojis: List<EmojiUsageEntity>) {
    val maxFreq = (emojis.maxOfOrNull { it.frequency } ?: 1).coerceAtLeast(1)
    Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface, shadowElevation = 1.dp, modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            emojis.take(6).forEach { emoji -> // Top 6 for cleaner view
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji.emoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier.weight(1f).height(28.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceVariant)) {
                        val fraction = emoji.frequency.toFloat() / maxFreq
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))))
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("${emoji.frequency}", fontSize = 13.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}


