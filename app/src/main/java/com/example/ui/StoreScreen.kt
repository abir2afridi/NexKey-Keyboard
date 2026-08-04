package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.example.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.theme.InfoBoxFrame
import com.example.theme.InfoBoxFramePreset
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import kotlinx.coroutines.launch
import org.json.JSONArray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreScreen(
    onBack: () -> Unit = {},
    onNavigateToThemes: () -> Unit = {},
    onNavigateToCustomTheme: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { com.example.data.UserPreferences(context) }
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.store_title), fontWeight = FontWeight.Bold) },
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
                    text = { Text(stringResource(R.string.store_tab_themes), fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) },
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
                1 -> ThemesTab(onNavigateToThemes = onNavigateToThemes, onNavigateToCustomTheme = onNavigateToCustomTheme)
                2 -> MeterTab(prefs = prefs, scope = scope)
                3 -> InfoBoxTab(prefs = prefs, scope = scope)
            }
        }
    }
}

@Composable
private fun MeterTab(
    prefs: com.example.data.UserPreferences,
    scope: kotlinx.coroutines.CoroutineScope
) {
    val meterThemes = remember { MeterTheme.allPresets() }
    val currentMeterTheme by prefs.meterTheme.collectAsState(initial = "CALCULATOR")
    val currentMeterFont by prefs.meterFont.collectAsState(initial = "DIGITAL")
    
    val fontOptions = listOf("DIGITAL", "LCD", "SEGMENT", "MODERN")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.meter_designs),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = 1000.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(meterThemes.size) { index ->
                val theme = meterThemes[index]
                val isSelected = currentMeterTheme == theme.preset.name
                
                Surface(
                    onClick = { scope.launch { prefs.setMeterTheme(theme.preset.name) } },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Preview
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
                                if (theme.showLcdShadow || currentMeterFont == "LCD" || currentMeterFont == "SEGMENT") {
                                    Text(
                                        "88.8", 
                                        color = theme.textColor.copy(alpha = 0.05f), 
                                        fontSize = 12.sp, 
                                        fontWeight = FontWeight.Black,
                                        fontFamily = com.example.theme.meterFontFamily(currentMeterFont),
                                        letterSpacing = if (currentMeterFont == "LCD") 2.sp else if (currentMeterFont == "SEGMENT") 1.5.sp else theme.letterSpacing,
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
                                        fontFamily = com.example.theme.meterFontFamily(currentMeterFont),
                                        letterSpacing = if (currentMeterFont == "LCD") 2.sp else if (currentMeterFont == "SEGMENT") 1.5.sp else theme.letterSpacing,
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
        
        Text(
            stringResource(R.string.meter_font_style),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fontOptions.forEach { font ->
                val isSelected = currentMeterFont == font
                Surface(
                    onClick = { scope.launch { prefs.setMeterFont(font) } },
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
                            text = "88.8",
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
        
        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
private fun InfoBoxTab(
    prefs: com.example.data.UserPreferences,
    scope: kotlinx.coroutines.CoroutineScope
) {
    val frames = remember { InfoBoxFrame.allPresets() }
    val currentFrame by prefs.infoBoxFrame.collectAsState(initial = "CLASSIC")
    val previewFrame = remember(currentFrame, frames) {
        frames.firstOrNull { it.preset.name == currentFrame } ?: frames.first()
    }
    val currentTextColor by prefs.infoBoxTextColor.collectAsState(initial = "#00FF41")
    val previewTextColor = remember(currentTextColor) {
        try { Color(android.graphics.Color.parseColor(currentTextColor)) } catch (_: Exception) { Color(0xFF00FF41) }
    }
    val currentInfoColor by prefs.infoBoxInfoColor.collectAsState(initial = "#00FF41")
    val previewInfoColor = remember(currentInfoColor) {
        try { Color(android.graphics.Color.parseColor(currentInfoColor)) } catch (_: Exception) { Color(0xFF00FF41) }
    }
    val currentCustomTextColor by prefs.infoBoxCustomTextColor.collectAsState(initial = "#FFFFFF")
    val previewCustomTextColor = remember(currentCustomTextColor) {
        try { Color(android.graphics.Color.parseColor(currentCustomTextColor)) } catch (_: Exception) { Color.White }
    }
    val currentCustomTexts by prefs.infoBoxCustomTexts.collectAsState(initial = "[]")
    val currentCustomMode by prefs.infoBoxCustomMode.collectAsState(initial = "off")
    val currentCustomSec by prefs.infoBoxCustomSec.collectAsState(initial = 5)
    val currentSwipeTimeout by prefs.infoBoxSwipeTimeoutSec.collectAsState(initial = 10)
    val infoBoxEnabled by prefs.infoBoxEnabled.collectAsState(initial = true)

    var newText by remember { mutableStateOf("") }
    var showRules by remember { mutableStateOf(false) }
    var showCustomColorPicker by remember { mutableStateOf(false) }
    var showSwipeColorPicker by remember { mutableStateOf(false) }
    var showInfoColorPicker by remember { mutableStateOf(false) }
    val customTexts = remember(currentCustomTexts) {
        try {
            JSONArray(currentCustomTexts).let { arr -> List(arr.length()) { arr.getString(it) } }
                .filter { it.isNotBlank() }
        } catch (_: Exception) {
            emptyList()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            onClick = { scope.launch { prefs.setInfoBoxEnabled(!infoBoxEnabled) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = if (infoBoxEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.store_infobox_enable), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(stringResource(R.string.store_infobox_enable_desc), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = infoBoxEnabled,
                    onCheckedChange = { scope.launch { prefs.setInfoBoxEnabled(it) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(R.string.infobox_frames),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 6.dp)
            )
            IconButton(
                onClick = { showRules = true },
                modifier = Modifier.align(Alignment.CenterEnd).size(24.dp)
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
            }
        }

        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
            modifier = Modifier.heightIn(max = 1200.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            userScrollEnabled = false
        ) {
            items(frames.size) { index ->
                val frame = frames[index]
                val isSelected = currentFrame == frame.preset.name

                Surface(
                    onClick = { scope.launch { prefs.setInfoBoxFrame(frame.preset.name) } },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
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
                                text = "12.3 CPS",
                                color = frame.defaultTextColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
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
            stringResource(R.string.infobox_key_press_color),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        val swatchColors = remember(frames) {
            listOf(Color(0xFF00FF41), Color(0xFFFFFFFF), Color(0xFF00FF9F), Color(0xFF0F380F), Color(0xFFE0E0E0), Color(0xFF1B1B1B), Color(0xFFCCFF00), Color(0xFFBB86FC)) +
                frames.map { it.defaultTextColor }
        }
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            swatchColors.distinct().forEach { color ->
                val hex = "#%06X".format(color.toArgb() and 0xFFFFFF)
                val isSelected = currentTextColor.equals(hex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                        .clickable { scope.launch { prefs.setInfoBoxTextColor(hex) } }
                )
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showSwipeColorPicker = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.custom_color),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .width(160.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(previewFrame.cornerRadius))
                .background(previewFrame.backgroundColor.copy(alpha = previewFrame.backgroundAlpha))
                .border(previewFrame.borderWidth, previewFrame.borderColor, RoundedCornerShape(previewFrame.cornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.meter_swipe_in, 5),
                color = previewTextColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        OutlinedTextField(
            value = currentTextColor,
            onValueChange = { scope.launch { prefs.setInfoBoxTextColor(it.uppercase()) } },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            label = { Text(stringResource(R.string.infobox_key_press_color)) },
            singleLine = true
        )

        if (showSwipeColorPicker) {
            ColorPickerDialog(
                title = stringResource(R.string.infobox_key_press_color),
                initialColor = previewTextColor,
                onDismiss = { showSwipeColorPicker = false },
                onConfirm = { color ->
                    scope.launch { prefs.setInfoBoxTextColor("#%06X".format(color.toArgb() and 0xFFFFFF)) }
                    showSwipeColorPicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_info_color),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            swatchColors.distinct().forEach { color ->
                val hex = "#%06X".format(color.toArgb() and 0xFFFFFF)
                val isSelected = currentInfoColor.equals(hex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                        .clickable { scope.launch { prefs.setInfoBoxInfoColor(hex) } }
                )
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showInfoColorPicker = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.custom_color),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .width(160.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(previewFrame.cornerRadius))
                .background(previewFrame.backgroundColor.copy(alpha = previewFrame.backgroundAlpha))
                .border(previewFrame.borderWidth, previewFrame.borderColor, RoundedCornerShape(previewFrame.cornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "12.3 CPS",
                color = previewInfoColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        OutlinedTextField(
            value = currentInfoColor,
            onValueChange = { scope.launch { prefs.setInfoBoxInfoColor(it.uppercase()) } },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            label = { Text(stringResource(R.string.infobox_info_color)) },
            singleLine = true
        )

        if (showInfoColorPicker) {
            ColorPickerDialog(
                title = stringResource(R.string.infobox_info_color),
                initialColor = previewInfoColor,
                onDismiss = { showInfoColorPicker = false },
                onConfirm = { color ->
                    scope.launch { prefs.setInfoBoxInfoColor("#%06X".format(color.toArgb() and 0xFFFFFF)) }
                    showInfoColorPicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_custom_text_color),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            swatchColors.distinct().forEach { color ->
                val hex = "#%06X".format(color.toArgb() and 0xFFFFFF)
                val isSelected = currentCustomTextColor.equals(hex, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        )
                        .clickable { scope.launch { prefs.setInfoBoxCustomTextColor(hex) } }
                )
            }
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { showCustomColorPicker = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.custom_color),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Box(
            modifier = Modifier
                .padding(top = 14.dp)
                .width(160.dp)
                .height(32.dp)
                .clip(RoundedCornerShape(previewFrame.cornerRadius))
                .background(previewFrame.backgroundColor.copy(alpha = previewFrame.backgroundAlpha))
                .border(previewFrame.borderWidth, previewFrame.borderColor, RoundedCornerShape(previewFrame.cornerRadius)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = customTexts.firstOrNull() ?: stringResource(R.string.infobox_custom_text_sample),
                color = previewCustomTextColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                textAlign = TextAlign.Center,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }

        OutlinedTextField(
            value = currentCustomTextColor,
            onValueChange = { scope.launch { prefs.setInfoBoxCustomTextColor(it.uppercase()) } },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
            label = { Text(stringResource(R.string.infobox_custom_text_color)) },
            singleLine = true
        )

        if (showCustomColorPicker) {
            ColorPickerDialog(
                title = stringResource(R.string.infobox_custom_text_color),
                initialColor = previewCustomTextColor,
                onDismiss = { showCustomColorPicker = false },
                onConfirm = { color ->
                    scope.launch { prefs.setInfoBoxCustomTextColor("#%06X".format(color.toArgb() and 0xFFFFFF)) }
                    showCustomColorPicker = false
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_custom_texts),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )
        Text(
            stringResource(R.string.infobox_custom_texts_desc),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        customTexts.forEach { text ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(start = 14.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = text.ifBlank { "—" },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    IconButton(onClick = {
                        scope.launch {
                            val updated = customTexts.filter { it != text }
                            prefs.setInfoBoxCustomTexts(JSONArray(updated).toString())
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.clipboard_delete), tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                modifier = Modifier.weight(1f),
                label = { Text(stringResource(R.string.infobox_text_placeholder)) },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newText.isNotBlank()) {
                        val textToAdd = newText.trim()
                        scope.launch {
                            val updated = (customTexts + textToAdd).filter { it.isNotBlank() }
                            prefs.setInfoBoxCustomTexts(JSONArray(updated).toString())
                        }
                        newText = ""
                    }
                },
                enabled = newText.isNotBlank()
            ) {
                Text(stringResource(R.string.infobox_add_text))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.infobox_custom_mode),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("off", R.string.infobox_mode_off, Color(0xFF9E9E9E)),
                Triple("timed", R.string.infobox_mode_timed, Color(0xFFFF9800)),
                Triple("always", R.string.infobox_mode_always, Color(0xFF4CAF50))
            ).forEach { (mode, labelRes, accent) ->
                val isSelected = currentCustomMode == mode
                Surface(
                    onClick = { scope.launch { prefs.setInfoBoxCustomMode(mode) } },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) accent else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(labelRes),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        StepperRow(
            label = stringResource(R.string.infobox_custom_sec),
            value = currentCustomSec,
            onDecrease = { scope.launch { prefs.setInfoBoxCustomSec((currentCustomSec - 1).coerceAtLeast(1)) } },
            onIncrease = { scope.launch { prefs.setInfoBoxCustomSec((currentCustomSec + 1).coerceAtMost(60)) } }
        )

        Spacer(modifier = Modifier.height(12.dp))

        StepperRow(
            label = stringResource(R.string.infobox_swipe_timeout),
            value = currentSwipeTimeout,
            onDecrease = { scope.launch { prefs.setInfoBoxSwipeTimeoutSec((currentSwipeTimeout - 1).coerceAtLeast(3)) } },
            onIncrease = { scope.launch { prefs.setInfoBoxSwipeTimeoutSec((currentSwipeTimeout + 1).coerceAtMost(120)) } }
        )

        Spacer(modifier = Modifier.height(120.dp))
    }

    if (showRules) {
        MeterRulesDialog(onDismiss = { showRules = false })
    }
}

@Composable
private fun StepperRow(
    label: String,
    value: Int,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDecrease) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Text(
                text = "$value s",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.width(48.dp),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = onIncrease) {
                Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

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

// Extension to avoid compilation error if capitalize() is deprecated
private fun String.capitalize() = this.replaceFirstChar { it.uppercase() }

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
private fun meterFontLabel(font: String): String = stringResource(
    when (font) {
        "DIGITAL" -> R.string.meter_font_digital
        "LCD" -> R.string.meter_font_lcd
        "SEGMENT" -> R.string.meter_font_segment
        else -> R.string.meter_font_modern
    }
)

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
private fun ThemesTab(onNavigateToThemes: () -> Unit, onNavigateToCustomTheme: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            stringResource(R.string.store_tab_themes),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp) {
            Column {
                SettingItem(stringResource(R.string.store_browse_themes), stringResource(R.string.store_browse_themes_sub), Icons.Default.Palette, onClick = onNavigateToThemes)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant)
                SettingItem(stringResource(R.string.store_custom_theme), stringResource(R.string.store_custom_theme_sub), Icons.Default.Colorize, onClick = onNavigateToCustomTheme)
            }
        }

        Spacer(modifier = Modifier.height(120.dp))
    }
}
