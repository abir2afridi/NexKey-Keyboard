package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun SizeSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val heightPortrait by prefs.kbHeightPortrait.collectAsState(initial = 100)
    val heightLandscape by prefs.kbHeightLandscape.collectAsState(initial = 100)
    val widthOneHandedPortrait by prefs.oneHandedWidthPortrait.collectAsState(initial = 85)
    val widthOneHandedLandscape by prefs.oneHandedWidthLandscape.collectAsState(initial = 40)

    SettingsSubScaffold(title = stringResource(R.string.settings_size), onBack = onBack) {
        SettingSliderItem(stringResource(R.string.size_height_portrait), heightPortrait.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightPortrait(it.toInt()) } }
        SettingSliderItem(stringResource(R.string.size_height_landscape), heightLandscape.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightLandscape(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem(stringResource(R.string.size_width_portrait), widthOneHandedPortrait.toFloat(), 50f..100f) { scope.launch { prefs.setOneHandedWidthPortrait(it.toInt()) } }
        SettingSliderItem(stringResource(R.string.size_width_landscape), widthOneHandedLandscape.toFloat(), 30f..100f) { scope.launch { prefs.setOneHandedWidthLandscape(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
