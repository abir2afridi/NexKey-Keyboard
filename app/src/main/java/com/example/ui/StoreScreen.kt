package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.theme.INFOBOX_FONT_OPTIONS
import com.example.theme.InfoBoxFrame
import com.example.theme.InfoBoxFramePreset
import com.example.theme.KeyboardTheme
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import com.example.theme.ThemePreset
import com.example.theme.meterFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onBack: () -> Unit = {},
    onNavigateToCustomTheme: () -> Unit = {},
    onNavigateToSpeedMeter: () -> Unit = {},
    onNavigateToInfoBox: () -> Unit = {},
    appTheme: String = "SYSTEM",
    onToggleTheme: () -> Unit = {},
    onNavigateToAppSettings: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.store_title), fontWeight = FontWeight.Bold) },
                actions = {
                    ThemeToggleButton(appTheme = appTheme, onToggleTheme = onToggleTheme)
                    IconButton(onClick = onNavigateToAppSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.app_settings_title), tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(stringResource(R.string.store_tab_shop), fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(stringResource(R.string.store_tab_keyboard), fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text(stringResource(R.string.store_tab_meter), fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text(stringResource(R.string.store_tab_infobox), fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) },
                    icon = { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp)) }
                )
            }

            when (selectedTab) {
                0 -> ShopTab()
                1 -> KeyboardThemesTab(onNavigateToCustomTheme = onNavigateToCustomTheme)
                2 -> MeterStoreTab(onNavigateToSpeedMeter = onNavigateToSpeedMeter)
                3 -> InfoBoxStoreTab(onNavigateToInfoBox = onNavigateToInfoBox)
            }
        }
    }
}

@Composable
private fun ShopTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.store_coming_soon),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(vertical = 24.dp)
        )
        Text(
            stringResource(R.string.store_shop_desc),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun KeyboardThemesTab(onNavigateToCustomTheme: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { com.example.data.UserPreferences.getInstance(context) }
    val scope = rememberCoroutineScope()
    val savedThemePresetName by prefs.theme.collectAsState(initial = ThemePreset.DARK_NEON.name)

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

    val customTheme = remember(customBg, customKeyBg, customKeySpec, customKeyText, customKeySpecText, customAccent, customSugBg, customSugText, customPopBg, customPopText, customKeyHint) {
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
    }

    val themes = remember(customTheme) { listOf(customTheme) + KeyboardTheme.allThemes() }

    val selectedTheme = remember(savedThemePresetName) {
        try {
            KeyboardTheme.fromPreset(ThemePreset.valueOf(savedThemePresetName))
        } catch (e: Exception) {
            KeyboardTheme.DarkNeon
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomizeLink(
            title = stringResource(R.string.store_customize),
            desc = stringResource(R.string.store_custom_theme_sub),
            icon = Icons.Default.Colorize,
            onClick = onNavigateToCustomTheme
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.store_tab_keyboard),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = 2400.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(themes.size) { index ->
                val theme = themes[index]
                ThemePreviewCard(
                    theme = theme,
                    isSelected = selectedTheme.preset == theme.preset,
                    onClick = {
                        scope.launch {
                            prefs.setTheme(theme.preset)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        DownloadThemesPlaceholder()
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun MeterStoreTab(onNavigateToSpeedMeter: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomizeLink(
            title = stringResource(R.string.store_customize),
            desc = stringResource(R.string.settings_speed_meter_desc),
            icon = Icons.Default.Speed,
            onClick = onNavigateToSpeedMeter
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.meter_designs),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        val meterThemes = remember { MeterTheme.allPresets() }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = 1000.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(meterThemes.size) { index ->
                val theme = meterThemes[index]
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(theme.backgroundColor.copy(alpha = theme.backgroundAlpha))
                                .border(theme.borderWidth, theme.borderColor, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                if (theme.showLcdShadow) {
                                    Text(
                                        "88.8",
                                        color = theme.textColor.copy(alpha = 0.05f),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = com.example.theme.meterFontFamily("DIGITAL"),
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        "0.0",
                                        color = theme.textColor,
                                        fontSize = 12.sp,
                                        fontFamily = com.example.theme.meterFontFamily("DIGITAL"),
                                        style = androidx.compose.ui.text.TextStyle(
                                            shadow = if (theme.glowRadius > 0f) {
                                                androidx.compose.ui.graphics.Shadow(
                                                    color = theme.textColor.copy(alpha = 0.8f),
                                                    blurRadius = theme.glowRadius
                                                )
                                            } else null,
                                            fontFeatureSettings = "tnum"
                                        )
                                    )
                                    Text(stringResource(R.string.kb_live), color = theme.labelColor, fontSize = 6.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = meterThemeLabel(theme.preset),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        DownloadThemesPlaceholder()
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun InfoBoxStoreTab(onNavigateToInfoBox: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { com.example.data.UserPreferences.getInstance(context) }
    val scope = rememberCoroutineScope()
    val currentInfoBoxFont by prefs.infoBoxFont.collectAsState(initial = "DEFAULT")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CustomizeLink(
            title = stringResource(R.string.store_customize),
            desc = stringResource(R.string.settings_info_box_desc),
            icon = Icons.Default.Info,
            onClick = onNavigateToInfoBox
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_frames),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        val frames = remember { InfoBoxFrame.allPresets() }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = 1000.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(frames.size) { index ->
                val frame = frames[index]
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(frame.cornerRadius))
                                .background(frame.backgroundColor.copy(alpha = frame.backgroundAlpha))
                                .border(frame.borderWidth, frame.borderColor, RoundedCornerShape(frame.cornerRadius)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "A/B/C/D",
                                color = frame.defaultTextColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = com.example.theme.meterFontFamily(currentInfoBoxFont),
                                maxLines = 1,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = infoBoxFrameLabel(frame.preset),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_text_style),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        val fontOptions = INFOBOX_FONT_OPTIONS
        val fontRows = fontOptions.chunked(4)
        fontRows.forEachIndexed { rowIndex, rowFonts ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (rowIndex < fontRows.lastIndex) 8.dp else 0.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowFonts.forEach { font ->
                    val isSelected = currentInfoBoxFont == font
                    Surface(
                        onClick = { scope.launch { prefs.setInfoBoxFont(font) } },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Aa12",
                                fontSize = 14.sp,
                                fontFamily = com.example.theme.meterFontFamily(font),
                                letterSpacing = if (font == "LCD") 2.sp else if (font == "SEGMENT") 1.5.sp else 0.sp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = meterFontLabel(font),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        DownloadThemesPlaceholder()
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun CustomizeLink(
    title: String,
    desc: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(desc, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun DownloadThemesPlaceholder() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)
    Text(
        stringResource(R.string.store_download_themes),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
    )
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.store_download_themes_desc), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun meterThemeLabel(preset: MeterThemePreset): String = stringResource(
    when (preset) {
        MeterThemePreset.CALCULATOR -> R.string.meter_calculator
        MeterThemePreset.NEON_CYBER -> R.string.meter_neon_cyber
        MeterThemePreset.RETRO_LCD -> R.string.meter_retro_lcd
        MeterThemePreset.MINIMAL_DARK -> R.string.meter_minimal_dark
        MeterThemePreset.GHOST_WHITE -> R.string.meter_ghost_white
        MeterThemePreset.CYBER_LIME -> R.string.meter_cyber_lime
        MeterThemePreset.AMBER_RETRO -> R.string.meter_amber_retro
        MeterThemePreset.VIOLET_GLOW -> R.string.meter_violet_glow
    }
)

@Composable
private fun infoBoxFrameLabel(preset: InfoBoxFramePreset): String = stringResource(
    when (preset) {
        InfoBoxFramePreset.CLASSIC -> R.string.infobox_frame_classic
        InfoBoxFramePreset.NEON_CYBER -> R.string.infobox_frame_neon_cyber
        InfoBoxFramePreset.RETRO_LCD -> R.string.infobox_frame_retro_lcd
        InfoBoxFramePreset.MINIMAL_DARK -> R.string.infobox_frame_minimal_dark
        InfoBoxFramePreset.GHOST_WHITE -> R.string.infobox_frame_ghost_white
        InfoBoxFramePreset.CYBER_LIME -> R.string.infobox_frame_cyber_lime
        InfoBoxFramePreset.VIOLET_GLOW -> R.string.infobox_frame_violet_glow
    }
)

@Composable
private fun meterFontLabel(font: String): String = stringResource(
    when (font) {
        "DEFAULT" -> R.string.meter_font_default
        "DIGITAL" -> R.string.meter_font_digital
        "LCD" -> R.string.meter_font_lcd
        "SEGMENT" -> R.string.meter_font_segment
        "MODERN" -> R.string.meter_font_modern
        "MONO" -> R.string.meter_font_mono
        "SERIF" -> R.string.meter_font_serif
        "CURSIVE" -> R.string.meter_font_cursive
        else -> R.string.meter_font_default
    }
)