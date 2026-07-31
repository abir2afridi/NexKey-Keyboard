package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.LooksOne
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.VerticalSplit
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val showNumRow by prefs.showNumberRow.collectAsState(initial = false)
    val largeNumRow by prefs.largeNumberRow.collectAsState(initial = false)
    val hideHints by prefs.hideLongPressHints.collectAsState(initial = false)
    val enableResizing by prefs.enableKbResizing.collectAsState(initial = false)
    val splitKb by prefs.splitKeyboard.collectAsState(initial = false)
    val forcedEnter by prefs.forcedEnter.collectAsState(initial = false)
    val alwaysShowSuggestions by prefs.alwaysShowSuggestions.collectAsState(initial = false)
    val autoHideToolbar by prefs.autoHideToolbar.collectAsState(initial = false)

    SettingsSubScaffold(title = "Layout", onBack = onBack) {
        SettingSwitchItem("Enable number row", "Adds an extra row", Icons.Default.LooksOne, showNumRow) { scope.launch { prefs.setShowNumberRow(it) } }
        SettingSwitchItem("Large number row", null, Icons.Default.ViewStream, largeNumRow) { scope.launch { prefs.setLargeNumberRow(it) } }
        SettingSwitchItem("Hide long press hints", "Hide small labels from key corners", Icons.Default.VisibilityOff, hideHints) { scope.launch { prefs.setHideLongPressHints(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Enable keyboard resizing", null, Icons.Default.AspectRatio, enableResizing) { scope.launch { prefs.setEnableKbResizing(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Enable split keyboard", "For foldable phones", Icons.Default.VerticalSplit, splitKb) { scope.launch { prefs.setSplitKeyboard(it) } }
        SettingSwitchItem("Forced enter button", "Do not show emoji on enter", Icons.AutoMirrored.Filled.KeyboardReturn, forcedEnter) { scope.launch { prefs.setForcedEnter(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Always show suggestions", "Suggestion bar stays visible even when empty", Icons.Default.TextFields, alwaysShowSuggestions) { scope.launch { prefs.setAlwaysShowSuggestions(it) } }
        SettingSwitchItem("Auto-hide toolbar while typing", "Only suggestions bar visible; toggle button to show toolbar", Icons.Default.SwapHoriz, autoHideToolbar) { scope.launch { prefs.setAutoHideToolbar(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
