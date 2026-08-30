package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedGroupSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences.getInstance(context) }
    val longPressDelay by prefs.longPressDelayMs.collectAsState(initial = 300)
    val popupDismiss by prefs.popupDismissDelay.collectAsState(initial = "Default")
    val spaceDelay by prefs.spaceCursorDelay.collectAsState(initial = 1000)
    val spaceSpeed by prefs.spaceCursorSpeed.collectAsState(initial = 150)
    val physicalKbEmoji by prefs.physicalKbEmoji.collectAsState(initial = true)
    val typedWordFirst by prefs.showTypedWordFirst.collectAsState(initial = true)
    val backspaceDelay by prefs.backspaceRepeatDelay.collectAsState(initial = 400)
    val backspaceSpeed by prefs.backspaceRepeatSpeed.collectAsState(initial = 50)

    SettingsSubScaffold(title = stringResource(R.string.settings_advanced), onBack = onBack) {
        SettingSliderItem(stringResource(R.string.adv_long_press_delay), longPressDelay.toFloat(), 100f..1000f) { scope.launch { prefs.setLongPressDelayMs(it.toInt()) } }
        val dismissKeys = listOf("Default", "Short", "Long")
        val dismissLabels = mapOf(
            "Default" to stringResource(R.string.adv_dismiss_default),
            "Short" to stringResource(R.string.adv_dismiss_short),
            "Long" to stringResource(R.string.adv_dismiss_long)
        )
        Text(text = stringResource(R.string.adv_popup_dismiss_label, dismissLabels[popupDismiss] ?: popupDismiss), color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            dismissKeys.forEach { key ->
                FilterChip(
                    selected = popupDismiss == key,
                    onClick = { scope.launch { prefs.setPopupDismissDelay(key) } },
                    label = { Text(dismissLabels[key] ?: key) }
                )
            }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem(stringResource(R.string.adv_space_delay), spaceDelay.toFloat(), 500f..2000f) { scope.launch { prefs.setSpaceCursorDelay(it.toInt()) } }
        SettingSliderItem(stringResource(R.string.adv_space_speed), spaceSpeed.toFloat(), 50f..300f) { scope.launch { prefs.setSpaceCursorSpeed(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem(stringResource(R.string.adv_physical_emoji), stringResource(R.string.adv_physical_emoji_desc), Icons.Default.Keyboard, physicalKbEmoji) { scope.launch { prefs.setPhysicalKbEmoji(it) } }
        SettingSwitchItem(stringResource(R.string.adv_typed_first), stringResource(R.string.adv_typed_first_desc), Icons.Default.Title, typedWordFirst) { scope.launch { prefs.setShowTypedWordFirst(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        // Backspace repeat: the slider's minimum IS the default (400 ms / 50 ms) — a fast,
        // comfortable start like other keyboards. Users can only slow it down from there.
        SettingSliderItem(stringResource(R.string.adv_backspace_delay), backspaceDelay.toFloat(), 400f..1000f) { scope.launch { prefs.setBackspaceRepeatDelay(it.toInt()) } }
        Text(
            text = stringResource(R.string.adv_backspace_delay_desc),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        SettingSliderItem(stringResource(R.string.adv_backspace_speed), backspaceSpeed.toFloat(), 50f..200f) { scope.launch { prefs.setBackspaceRepeatSpeed(it.toInt()) } }
        Text(
            text = stringResource(R.string.adv_backspace_speed_desc),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSettingsScreen(onBack: () -> Unit) {
    AdvancedGroupSettingsScreen(onBack = onBack)
}
