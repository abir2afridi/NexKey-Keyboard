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
import androidx.compose.material.icons.filled.ViewStream
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutSettingsScreen(
    onBack: () -> Unit,
    onNavigateToResize: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences.getInstance(context) }
    val showNumRow by prefs.showNumberRow.collectAsState(initial = true)
    val largeNumRow by prefs.largeNumberRow.collectAsState(initial = false)
    val hideHints by prefs.hideLongPressHints.collectAsState(initial = false)
    val forcedEnter by prefs.forcedEnter.collectAsState(initial = false)
    val alwaysShowSuggestions by prefs.alwaysShowSuggestions.collectAsState(initial = false)
    val unifiedHeader by prefs.unifiedHeader.collectAsState(initial = false)
    val toolbarAutoShowDelay by prefs.toolbarAutoShowDelay.collectAsState(initial = 10)

    SettingsSubScaffold(title = stringResource(R.string.settings_layout), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.layout_number_row), stringResource(R.string.layout_number_row_desc), Icons.Default.LooksOne, showNumRow) { scope.launch { prefs.setShowNumberRow(it) } }
        SettingSwitchItem(stringResource(R.string.layout_large_number_row), null, Icons.Default.ViewStream, largeNumRow) { scope.launch { prefs.setLargeNumberRow(it) } }
        SettingSwitchItem(stringResource(R.string.layout_hide_hints), stringResource(R.string.layout_hide_hints_desc), Icons.Default.VisibilityOff, hideHints) { scope.launch { prefs.setHideLongPressHints(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingActionItem(stringResource(R.string.layout_resizing), stringResource(R.string.layout_resizing_desc), Icons.Default.AspectRatio, onNavigateToResize)
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem(stringResource(R.string.layout_forced_enter), stringResource(R.string.layout_forced_enter_desc), Icons.AutoMirrored.Filled.KeyboardReturn, forcedEnter) { scope.launch { prefs.setForcedEnter(it) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem(stringResource(R.string.layout_always_suggestions), stringResource(R.string.layout_always_suggestions_desc), Icons.Default.TextFields, alwaysShowSuggestions) { scope.launch { prefs.setAlwaysShowSuggestions(it) } }
        SettingSwitchItem(stringResource(R.string.layout_unified_header), stringResource(R.string.layout_unified_header_desc), Icons.Default.SwapHoriz, unifiedHeader) { scope.launch { prefs.setUnifiedHeader(it) } }
        if (unifiedHeader) {
            SettingSliderItem(
                title = stringResource(R.string.layout_auto_show_delay),
                value = toolbarAutoShowDelay.toFloat(),
                range = 3f..30f
            ) { scope.launch { prefs.setToolbarAutoShowDelay(it.toInt()) } }
            Text(
                text = stringResource(R.string.layout_auto_show_delay_desc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
