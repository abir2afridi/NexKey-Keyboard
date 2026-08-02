package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val haptics by prefs.haptics.collectAsState(initial = true)
    val sound by prefs.sound.collectAsState(initial = true)
    val popupOnKeypress by prefs.popupOnKeypress.collectAsState(initial = true)
    val hapticIntensity by prefs.hapticIntensity.collectAsState(initial = 50)
    val soundVol by prefs.soundVolume.collectAsState(initial = 50)

    SettingsSubScaffold(title = stringResource(R.string.settings_feedback), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.feedback_vibrate), null, Icons.Default.Vibration, haptics) { scope.launch { prefs.setHaptics(it) } }
        SettingSwitchItem(stringResource(R.string.feedback_sound), null, Icons.AutoMirrored.Filled.VolumeUp, sound) { scope.launch { prefs.setSound(it) } }
        SettingSwitchItem(stringResource(R.string.feedback_popup), null, Icons.AutoMirrored.Filled.Message, popupOnKeypress) { scope.launch { prefs.setPopupOnKeypress(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem(stringResource(R.string.feedback_vibrate_intensity), hapticIntensity.toFloat(), 0f..100f) { scope.launch { prefs.setHapticIntensity(it.toInt()) } }
        SettingSliderItem(stringResource(R.string.feedback_sound_volume), soundVol.toFloat(), 0f..100f) { scope.launch { prefs.setSoundVolume(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
