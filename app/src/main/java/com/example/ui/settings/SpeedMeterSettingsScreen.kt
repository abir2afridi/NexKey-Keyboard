package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
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

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

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