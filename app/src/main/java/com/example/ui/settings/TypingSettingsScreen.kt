package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardTab
import androidx.compose.material.icons.filled.SpaceBar
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val autoCap by prefs.autoCapitalize.collectAsState(initial = true)
    val doubleSpacePeriod by prefs.smartPunctuation.collectAsState(initial = true)
    val doubleSpaceTab by prefs.doubleSpaceTab.collectAsState(initial = false)

    SettingsSubScaffold(title = "Typing", onBack = onBack) {
        SettingSwitchItem("Auto-capitalization", "Capitalize the first word of each sentence", Icons.Default.TextFormat, autoCap) { scope.launch { prefs.setAutoCapitalize(it) } }
        SettingSwitchItem("Double-space period", "Double tap on spacebar inserts a period followed by a space", Icons.Default.SpaceBar, doubleSpacePeriod) { scope.launch { prefs.setSmartPunctuation(it) } }
        SettingSwitchItem("Double-space tab", "Double tap on spacebar inserts a tab", Icons.AutoMirrored.Filled.KeyboardTab, doubleSpaceTab) { scope.launch { prefs.setDoubleSpaceTab(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
