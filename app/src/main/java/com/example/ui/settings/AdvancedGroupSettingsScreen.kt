package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedGroupSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val longPressDelay by prefs.longPressDelayMs.collectAsState(initial = 300)
    val popupDismiss by prefs.popupDismissDelay.collectAsState(initial = "Default")
    val spaceDelay by prefs.spaceCursorDelay.collectAsState(initial = 1000)
    val spaceSpeed by prefs.spaceCursorSpeed.collectAsState(initial = 150)
    val physicalKbEmoji by prefs.physicalKbEmoji.collectAsState(initial = true)
    val typedWordFirst by prefs.showTypedWordFirst.collectAsState(initial = true)
    val backspaceDelay by prefs.backspaceRepeatDelay.collectAsState(initial = 400)
    val backspaceSpeed by prefs.backspaceRepeatSpeed.collectAsState(initial = 50)

    SettingsSubScaffold(title = "Advanced", onBack = onBack) {
        SettingSliderItem("Key long press delay (ms)", longPressDelay.toFloat(), 100f..1000f) { scope.launch { prefs.setLongPressDelayMs(it.toInt()) } }
        val dismissOptions = listOf("Default", "Short", "Long")
        Text(text = "Popup dismiss delay: $popupDismiss", color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            dismissOptions.forEach { option ->
                FilterChip(
                    selected = popupDismiss == option,
                    onClick = { scope.launch { prefs.setPopupDismissDelay(option) } },
                    label = { Text(option) }
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("Spacebar cursor move delay", spaceDelay.toFloat(), 500f..2000f) { scope.launch { prefs.setSpaceCursorDelay(it.toInt()) } }
        SettingSliderItem("Spacebar cursor move speed", spaceSpeed.toFloat(), 50f..300f) { scope.launch { prefs.setSpaceCursorSpeed(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Physical keyboard emoji key", "Enable emoji key shortcuts on hardware keyboard", Icons.Default.Keyboard, physicalKbEmoji) { scope.launch { prefs.setPhysicalKbEmoji(it) } }
        SettingSwitchItem("Show typed word first", "Exact typed word appears as first suggestion", Icons.Default.Title, typedWordFirst) { scope.launch { prefs.setShowTypedWordFirst(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("Backspace repeat delay (ms)", backspaceDelay.toFloat(), 200f..1000f) { scope.launch { prefs.setBackspaceRepeatDelay(it.toInt()) } }
        SettingSliderItem("Backspace repeat speed (ms)", backspaceSpeed.toFloat(), 20f..200f) { scope.launch { prefs.setBackspaceRepeatSpeed(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsScreen(onBack: () -> Unit) {
    AdvancedGroupSettingsScreen(onBack = onBack)
}
