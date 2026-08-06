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
        // LIVE PREVIEW: vibrating while the user drags the slider lets them feel the exact
        // intensity they are choosing — no need to leave the screen to test it. Same formula
        // as the IME's playFeedback() so what you feel here is what the keyboard does.
        val vibrator = remember {
            context.getSystemService(android.content.Context.VIBRATOR_SERVICE) as? android.os.Vibrator
        }
        var lastPreviewVibrate by remember { mutableStateOf(0L) }
        fun previewVibration(level: Int) {
            if (!haptics || level <= 0) return
            val now = System.currentTimeMillis()
            if (now - lastPreviewVibrate < 80L) return // debounce rapid drag ticks
            lastPreviewVibrate = now
            try {
                val duration = (level * 2).toLong().coerceAtLeast(10L)
                val amplitude = ((level / 100f) * 255).toInt().coerceIn(1, 255)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator?.vibrate(android.os.VibrationEffect.createOneShot(duration, amplitude))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(duration)
                }
            } catch (_: Exception) {}
        }
        SettingSliderItem(stringResource(R.string.feedback_vibrate_intensity), hapticIntensity.toFloat(), 0f..100f) {
            val level = it.toInt()
            previewVibration(level)
            scope.launch { prefs.setHapticIntensity(level) }
        }
        SettingSliderItem(stringResource(R.string.feedback_sound_volume), soundVol.toFloat(), 0f..100f) { scope.launch { prefs.setSoundVolume(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
