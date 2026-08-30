package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
import com.example.theme.KeyboardTheme
import com.example.theme.ThemePreset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyboardResizeScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences.getInstance(context) }
    val scope = rememberCoroutineScope()

    val resizingEnabled by prefs.enableKbResizing.collectAsState(initial = false)
    val heightPortrait by prefs.kbHeightPortrait.collectAsState(initial = 100)
    val heightLandscape by prefs.kbHeightLandscape.collectAsState(initial = 100)
    val widthPortrait by prefs.oneHandedWidthPortrait.collectAsState(initial = 85)
    val widthLandscape by prefs.oneHandedWidthLandscape.collectAsState(initial = 40)
    val showNumRow by prefs.showNumberRow.collectAsState(initial = true)
    val savedThemePresetName by prefs.theme.collectAsState(initial = ThemePreset.DARK_NEON.name)

    // Build the same theme the real keyboard uses (preset or custom).
    val customBg by prefs.customBgColor.collectAsState(initial = "#FF12131C")
    val customKeyBg by prefs.customKeyBgColor.collectAsState(initial = "#FF1E2136")
    val customKeySpec by prefs.customKeySpecialColor.collectAsState(initial = "#FF2A2E4B")
    val customKeyText by prefs.customKeyTextColor.collectAsState(initial = "#FFF1F3FB")
    val customKeySpecText by prefs.customKeySpecialTextColor.collectAsState(initial = "#FF80D8FF")
    val customAccent by prefs.customAccentColor.collectAsState(initial = "#FF00E5FF")
    val customSugBg by prefs.customSuggestionBgColor.collectAsState(initial = "#FF1A1C29")
    val customSugText by prefs.customSuggestionTextColor.collectAsState(initial = "#FFF1F3FB")
    val customPopBg by prefs.customPopupBgColor.collectAsState(initial = "#FF2A2E4B")
    val customPopText by prefs.customPopupTextColor.collectAsState(initial = "#FF00E5FF")
    val customKeyHint by prefs.customKeyHintColor.collectAsState(initial = "#66F1F3FB")

    val theme = remember(
        savedThemePresetName,
        customBg, customKeyBg, customKeySpec, customKeyText, customKeySpecText,
        customAccent, customSugBg, customSugText, customPopBg, customPopText, customKeyHint
    ) {
        if (savedThemePresetName == ThemePreset.CUSTOM.name) {
            KeyboardTheme(
                preset = ThemePreset.CUSTOM,
                backgroundColor = Color(android.graphics.Color.parseColor(customBg)),
                keyBackgroundColor = Color(android.graphics.Color.parseColor(customKeyBg)),
                keySpecialColor = Color(android.graphics.Color.parseColor(customKeySpec)),
                keyTextColor = Color(android.graphics.Color.parseColor(customKeyText)),
                keySpecialTextColor = Color(android.graphics.Color.parseColor(customKeySpecText)),
                accentColor = Color(android.graphics.Color.parseColor(customAccent)),
                suggestionBgColor = Color(android.graphics.Color.parseColor(customSugBg)),
                suggestionTextColor = Color(android.graphics.Color.parseColor(customSugText)),
                popupBackgroundColor = Color(android.graphics.Color.parseColor(customPopBg)),
                popupTextColor = Color(android.graphics.Color.parseColor(customPopText)),
                keyHintColor = Color(android.graphics.Color.parseColor(customKeyHint))
            )
        } else {
            try {
                KeyboardTheme.fromPreset(ThemePreset.valueOf(savedThemePresetName))
            } catch (e: Exception) {
                KeyboardTheme.DarkNeon
            }
        }
    }

    // While resizing is disabled the keyboard always uses 100%, mirroring the IME behavior.
    val effectiveHeightPortrait = if (resizingEnabled) heightPortrait else 100
    val effectiveWidthPortrait = if (resizingEnabled) widthPortrait else 100

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.resize_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Live preview: the REAL keyboard composable, bottom-aligned in a fake input screen.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(440.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(theme.backgroundColor),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = stringResource(R.string.resize_preview_hint),
                    color = theme.keyTextColor.copy(alpha = 0.45f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(vertical = 10.dp)
                )
                KeyboardComposeView(
                    mode = KeyboardMode.ENGLISH,
                    lastTextMode = KeyboardMode.BANGLA_JATIYO,
                    shiftState = ShiftState.OFF,
                    theme = theme,
                    composingText = "",
                    suggestions = emptyList(),
                    actionLabel = "Enter",
                    showNumberRow = showNumRow,
                    keyboardHeightPortrait = effectiveHeightPortrait,
                    keyboardHeightLandscape = if (resizingEnabled) heightLandscape else 100,
                    oneHandedWidth = effectiveWidthPortrait,
                    oneHandedWidthLandscape = if (resizingEnabled) widthLandscape else 40,
                    showGlobeKey = false,
                    meterEnabled = false,
                    infoBoxEnabled = false,
                    onKeyTap = {},
                    onBackspaceTap = {},
                    onSpaceTap = {},
                    onEnterTap = {},
                    onShiftTap = {},
                    onModeChange = {},
                    onSuggestionSelect = {},
                    onVoiceClick = {},
                    onThemeToggle = {},
                    onOpenSettings = {}
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            SettingSwitchItem(stringResource(R.string.layout_resizing), stringResource(R.string.resize_enable_desc), Icons.Default.AspectRatio, resizingEnabled) {
                scope.launch { prefs.setEnableKbResizing(it) }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            SettingSliderItem(stringResource(R.string.size_height_portrait), heightPortrait.toFloat(), 50f..150f, enabled = resizingEnabled) {
                scope.launch { prefs.setKbHeightPortrait(it.toInt()) }
            }
            SettingSliderItem(stringResource(R.string.size_height_landscape), heightLandscape.toFloat(), 50f..150f, enabled = resizingEnabled) {
                scope.launch { prefs.setKbHeightLandscape(it.toInt()) }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
            SettingSliderItem(stringResource(R.string.size_width_portrait), widthPortrait.toFloat(), 50f..100f, enabled = resizingEnabled) {
                scope.launch { prefs.setOneHandedWidthPortrait(it.toInt()) }
            }
            SettingSliderItem(stringResource(R.string.size_width_landscape), widthLandscape.toFloat(), 30f..100f, enabled = resizingEnabled) {
                scope.launch { prefs.setOneHandedWidthLandscape(it.toInt()) }
            }

            OutlinedButton(
                onClick = {
                    scope.launch {
                        prefs.setKbHeightPortrait(100)
                        prefs.setKbHeightLandscape(100)
                        prefs.setOneHandedWidthPortrait(85)
                        prefs.setOneHandedWidthLandscape(40)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
            ) {
                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.resize_reset))
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
