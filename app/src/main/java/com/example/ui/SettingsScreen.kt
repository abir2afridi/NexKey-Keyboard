package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToDictionary: () -> Unit,
    onNavigateToClipboard: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToStats: () -> Unit,
    onNavigateToCustomTheme: () -> Unit,
    onNavigateToThemes: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }

    val haptics by prefs.haptics.collectAsState(initial = true)
    val sound by prefs.sound.collectAsState(initial = true)
    val autoCap by prefs.autoCapitalize.collectAsState(initial = true)
    val smartPunc by prefs.smartPunctuation.collectAsState(initial = true)
    val keyHeight by prefs.keyHeight.collectAsState(initial = 54)
    val keyRadius by prefs.keyRadius.collectAsState(initial = 10)
    
    val showNumRow by prefs.showNumberRow.collectAsState(initial = false)
    val hapticIntensity by prefs.hapticIntensity.collectAsState(initial = 50)
    val soundVolume by prefs.soundVolume.collectAsState(initial = 50)
    
    val appTheme by prefs.appTheme.collectAsState(initial = "SYSTEM")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NexKey Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F1017),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color(0xFF0F1017)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Preferences",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingSwitchItem(
                title = "Haptic Feedback",
                subtitle = "Vibrate on keypress",
                icon = Icons.Default.Vibration,
                checked = haptics,
                onCheckedChange = { scope.launch { prefs.setHaptics(it) } }
            )

            SettingSwitchItem(
                title = "Keypress Sound",
                subtitle = "Play sound when typing",
                icon = Icons.Default.VolumeUp,
                checked = sound,
                onCheckedChange = { scope.launch { prefs.setSound(it) } }
            )

            SettingSwitchItem(
                title = "Auto-Capitalization",
                subtitle = "Capitalize first word of sentences",
                icon = Icons.Default.VerticalAlignTop,
                checked = autoCap,
                onCheckedChange = { scope.launch { prefs.setAutoCapitalize(it) } }
            )

            SettingSwitchItem(
                title = "Smart Punctuation",
                subtitle = "Auto-insert period on double space",
                icon = Icons.Default.TextFormat,
                checked = smartPunc,
                onCheckedChange = { scope.launch { prefs.setSmartPunctuation(it) } }
            )

            SettingSwitchItem(
                title = "Number Row",
                subtitle = "Always show numbers on top",
                icon = Icons.Default.LooksOne,
                checked = showNumRow,
                onCheckedChange = { scope.launch { prefs.setShowNumberRow(it) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Haptics & Sound",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingSliderItem(
                title = "Vibration Intensity",
                value = hapticIntensity.toFloat(),
                range = 1f..100f,
                onValueChange = { scope.launch { prefs.setHapticIntensity(it.toInt()) } }
            )

            SettingSliderItem(
                title = "Sound Volume",
                value = soundVolume.toFloat(),
                range = 1f..100f,
                onValueChange = { scope.launch { prefs.setSoundVolume(it.toInt()) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Appearance",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "App Theme",
                subtitle = when (appTheme) {
                    "DARK" -> "Dark Mode"
                    "LIGHT" -> "Light Mode"
                    else -> "System Default"
                },
                icon = Icons.Default.Brightness4,
                onClick = {
                    val nextTheme = when (appTheme) {
                        "SYSTEM" -> "DARK"
                        "DARK" -> "LIGHT"
                        else -> "SYSTEM"
                    }
                    scope.launch { prefs.setAppTheme(nextTheme) }
                }
            )

            SettingItem(
                title = "Keyboard Themes",
                subtitle = "Choose from preset designs",
                icon = Icons.Default.ColorLens,
                onClick = onNavigateToThemes
            )

            SettingItem(
                title = "Theme Creator",
                subtitle = "Pick your own custom colors",
                icon = Icons.Default.Palette,
                onClick = onNavigateToCustomTheme
            )

            SettingSliderItem(
                title = "Key Height",
                value = keyHeight.toFloat(),
                range = 40f..70f,
                onValueChange = { scope.launch { prefs.setKeyHeight(it.toInt()) } }
            )

            SettingSliderItem(
                title = "Key Radius",
                value = keyRadius.toFloat(),
                range = 0f..24f,
                onValueChange = { scope.launch { prefs.setKeyRadius(it.toInt()) } }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Data Management",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "Personal Dictionary",
                subtitle = "Manage learned words",
                icon = Icons.Default.Book,
                onClick = onNavigateToDictionary
            )

            SettingItem(
                title = "Typing Stats",
                subtitle = "View your typing analytics",
                icon = Icons.Default.QueryStats,
                onClick = onNavigateToStats
            )

            SettingItem(
                title = "Clipboard History",
                subtitle = "Manage copied items",
                icon = Icons.Default.ContentPaste,
                onClick = onNavigateToClipboard
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "System",
                color = Color(0xFF00E5FF),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SettingItem(
                title = "About NexKey",
                subtitle = "Version, licenses, and team",
                icon = Icons.Default.Info,
                onClick = onNavigateToAbout
            )
        }
    }
}

@Composable
fun SettingSwitchItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF00E5FF), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(text = subtitle, color = Color.Gray, fontSize = 13.sp)
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = Color(0xFF00E5FF),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color(0xFF1B1C28)
            )
        )
    }
}

@Composable
fun SettingSliderItem(
    title: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = value.toInt().toString(), color = Color(0xFF00E5FF), fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF00E5FF),
                activeTrackColor = Color(0xFF00E5FF),
                inactiveTrackColor = Color(0xFF1B1C28)
            )
        )
    }
}
