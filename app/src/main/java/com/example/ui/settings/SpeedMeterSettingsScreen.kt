package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
import com.example.theme.MeterTheme
import com.example.theme.MeterThemePreset
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSpeedMeterScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val interval by prefs.meterInterval.collectAsState(initial = "5s")
    val meterEnabled by prefs.meterEnabled.collectAsState(initial = true)
    val meterPosition by prefs.meterPosition.collectAsState(initial = "right")
    val meterDisplayMode by prefs.meterDisplayMode.collectAsState(initial = "speed")

    var showInfo by remember { mutableStateOf(false) }

    SettingsSubScaffold(title = stringResource(R.string.settings_speed_meter), onBack = onBack) {
        Surface(
            onClick = { scope.launch { prefs.setMeterEnabled(!meterEnabled) } },
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
                    Icons.Default.Speed,
                    contentDescription = null,
                    tint = if (meterEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.settings_speed_meter_enable), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        stringResource(if (meterEnabled) R.string.settings_speed_meter_enable_desc_on else R.string.settings_speed_meter_enable_desc_off),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = meterEnabled,
                    onCheckedChange = { scope.launch { prefs.setMeterEnabled(it) } }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.meter_designs),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
        )

        val meterThemes = remember { MeterTheme.allPresets() }
        val currentMeterTheme by prefs.meterTheme.collectAsState(initial = "CALCULATOR")
        val currentMeterFont by prefs.meterFont.collectAsState(initial = "DIGITAL")

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

        val fontOptions = listOf("DIGITAL", "LCD", "SEGMENT", "MODERN")
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

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            text = stringResource(R.string.meter_display_mode),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )

        val displayModes = listOf("speed" to stringResource(R.string.meter_display_speed), "counter" to stringResource(R.string.meter_display_counter))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            displayModes.forEach { (key, label) ->
                FilterChip(
                    selected = meterDisplayMode == key,
                    onClick = { scope.launch { prefs.setMeterDisplayMode(key) } },
                    label = { Text(label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

        val meterCountMode by prefs.meterCountMode.collectAsState(initial = "keys")

        Text(
            text = stringResource(R.string.meter_count_title),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )

        val countModes = listOf("keys" to stringResource(R.string.meter_count_keys), "words" to stringResource(R.string.meter_count_words))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            countModes.forEach { (key, label) ->
                FilterChip(
                    selected = meterCountMode == key,
                    onClick = { scope.launch { prefs.setMeterCountMode(key) } },
                    label = { Text(label) }
                )
            }
        }

        Text(
            text = stringResource(R.string.meter_count_hint),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
        )

        Text(
            text = stringResource(R.string.meter_interval_label),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )

        val intervals = listOf("5s", "10s", "1min")
        val intervalLabels = mapOf(
            "5s" to stringResource(R.string.meter_interval_5s),
            "10s" to stringResource(R.string.meter_interval_10s),
            "1min" to stringResource(R.string.meter_interval_1min)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            intervals.forEach { key ->
                FilterChip(
                    selected = interval == key,
                    onClick = {
                        scope.launch {
                            prefs.setMeterInterval(key)
                            prefs.setMeterIdleMs(when (key) {
                                "5s" -> 5000
                                "10s" -> 10000
                                else -> 60000
                            })
                        }
                    },
                    label = { Text(intervalLabels[key] ?: key) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.meter_interval_hint, intervalLabels[interval] ?: interval),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            text = stringResource(R.string.meter_position_label),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
        )

        val positions = listOf("left", "middle", "right")
        val positionLabels = mapOf(
            "left" to stringResource(R.string.meter_position_left),
            "middle" to stringResource(R.string.meter_position_middle),
            "right" to stringResource(R.string.meter_position_right)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            positions.forEach { key ->
                FilterChip(
                    selected = meterPosition == key,
                    onClick = { scope.launch { prefs.setMeterPosition(key) } },
                    label = { Text(positionLabels[key] ?: key) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant)

        Surface(
            onClick = { showInfo = true },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Speed, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.meter_rules_title), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Text(stringResource(R.string.meter_rules_subtitle), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }

    if (showInfo) {
        AlertDialog(
            onDismissRequest = { showInfo = false },
            title = { Text(stringResource(R.string.meter_rules_title), fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    stringResource(R.string.meter_rules_line1).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                    stringResource(R.string.meter_rules_line2).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                    stringResource(R.string.meter_rules_line3).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                    Text(
                        stringResource(R.string.meter_rules_line4),
                        fontSize = 14.sp,
                        lineHeight = 21.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Start
                    )
                    stringResource(R.string.meter_rules_line5).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                    stringResource(R.string.meter_rules_line6).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                    stringResource(R.string.meter_rules_line7).also { Text(it, fontSize = 14.sp, lineHeight = 21.sp) }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfo = false }) {
                    Text(stringResource(R.string.home_got_it))
                }
            }
        )
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
private fun meterFontLabel(font: String): String = stringResource(
    when (font) {
        "DIGITAL" -> R.string.meter_font_digital
        "LCD" -> R.string.meter_font_lcd
        "SEGMENT" -> R.string.meter_font_segment
        else -> R.string.meter_font_modern
    }
)