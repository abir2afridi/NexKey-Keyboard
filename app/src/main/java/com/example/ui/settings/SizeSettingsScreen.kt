package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    SettingsSubScaffold(title = "Size", onBack = onBack) {
        SettingSliderItem("Keyboard Height (Portrait)", heightPortrait.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightPortrait(it.toInt()) } }
        SettingSliderItem("Keyboard Height (Landscape)", heightLandscape.toFloat(), 50f..150f) { scope.launch { prefs.setKbHeightLandscape(it.toInt()) } }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSliderItem("One Handed Width (Portrait)", widthOneHandedPortrait.toFloat(), 50f..100f) { scope.launch { prefs.setOneHandedWidthPortrait(it.toInt()) } }
        SettingSliderItem("One Handed Width (Landscape)", widthOneHandedLandscape.toFloat(), 30f..100f) { scope.launch { prefs.setOneHandedWidthLandscape(it.toInt()) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
