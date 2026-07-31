package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val moveCursorSpace by prefs.moveCursorSpace.collectAsState(initial = true)
    val volumeCursor by prefs.volumeCursor.collectAsState(initial = false)
    val smartVolumeControl by prefs.smartVolumeControl.collectAsState(initial = true)

    SettingsSubScaffold(title = "Navigation", onBack = onBack) {
        SettingSwitchItem("Move Cursor Using Space Key", "Swipe space to move cursor", Icons.Default.SwapHoriz, moveCursorSpace) { scope.launch { prefs.setMoveCursorSpace(it) } }
        SettingSwitchItem("Volume Key Cursor Control", "Use volume buttons to move cursor", Icons.AutoMirrored.Filled.VolumeUp, volumeCursor) { scope.launch { prefs.setVolumeCursor(it) } }
        if (volumeCursor) {
            SettingSwitchItem("Smart Volume Control", "Do not move cursor when audio is playing", Icons.Default.MusicNote, smartVolumeControl) { scope.launch { prefs.setSmartVolumeControl(it) } }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
