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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypingSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences.getInstance(context) }
    val autoCap by prefs.autoCapitalize.collectAsState(initial = true)
    val doubleSpacePeriod by prefs.smartPunctuation.collectAsState(initial = true)
    val doubleSpaceTab by prefs.doubleSpaceTab.collectAsState(initial = false)

    SettingsSubScaffold(title = stringResource(R.string.settings_typing), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.typing_auto_cap), stringResource(R.string.typing_auto_cap_desc), Icons.Default.TextFormat, autoCap) { scope.launch { prefs.setAutoCapitalize(it) } }
        SettingSwitchItem(stringResource(R.string.typing_double_space_period), stringResource(R.string.typing_double_space_period_desc), Icons.Default.SpaceBar, doubleSpacePeriod) { scope.launch { prefs.setSmartPunctuation(it) } }
        SettingSwitchItem(stringResource(R.string.typing_double_space_tab), stringResource(R.string.typing_double_space_tab_desc), Icons.AutoMirrored.Filled.KeyboardTab, doubleSpaceTab) { scope.launch { prefs.setDoubleSpaceTab(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
