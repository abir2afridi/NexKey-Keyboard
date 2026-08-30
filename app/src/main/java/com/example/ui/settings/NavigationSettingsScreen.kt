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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences.getInstance(context) }
    val moveCursorSpace by prefs.moveCursorSpace.collectAsState(initial = true)
    val volumeCursor by prefs.volumeCursor.collectAsState(initial = false)
    val smartVolumeControl by prefs.smartVolumeControl.collectAsState(initial = true)

    SettingsSubScaffold(title = stringResource(R.string.settings_navigation), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.nav_move_cursor), stringResource(R.string.nav_move_cursor_desc), Icons.Default.SwapHoriz, moveCursorSpace) { scope.launch { prefs.setMoveCursorSpace(it) } }
        SettingSwitchItem(stringResource(R.string.nav_volume_cursor), stringResource(R.string.nav_volume_cursor_desc), Icons.AutoMirrored.Filled.VolumeUp, volumeCursor) { scope.launch { prefs.setVolumeCursor(it) } }
        if (volumeCursor) {
            SettingSwitchItem(stringResource(R.string.nav_smart_volume), stringResource(R.string.nav_smart_volume_desc), Icons.Default.MusicNote, smartVolumeControl) { scope.launch { prefs.setSmartVolumeControl(it) } }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
