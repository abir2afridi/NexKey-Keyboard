package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.clipboard.ClipboardManager
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasteSettingsScreen(onBack: () -> Unit, onNavigateToClipboardHistory: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val holdPasteEnabled by prefs.holdPasteEnabled.collectAsState(initial = false)
    val holdPasteDuration by prefs.holdPasteDuration.collectAsState(initial = 400)
    val holdPasteTriggerKey by prefs.holdPasteTriggerKey.collectAsState(initial = "v")
    val clipboardExpiry by prefs.clipboardExpiry.collectAsState(initial = 120)
    val clipboardRecent by prefs.clipboardRecent.collectAsState(initial = true)
    val clipboardImages by prefs.clipboardImages.collectAsState(initial = true)

    SettingsSubScaffold(title = stringResource(R.string.settings_paste), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.paste_hold_paste), stringResource(R.string.paste_hold_paste_desc), Icons.Default.ContentPaste, holdPasteEnabled) { scope.launch { prefs.setHoldPasteEnabled(it) } }
        if (holdPasteEnabled) {
            SettingDropdownItem(
                title = stringResource(R.string.paste_trigger_key),
                subtitle = stringResource(R.string.paste_trigger_key_desc),
                icon = Icons.Default.Keyboard,
                selectedOption = holdPasteTriggerKey.uppercase(),
                options = listOf("V", "B", "N", "M", "G", "H", "Space", "Enter"),
                onOptionSelected = { scope.launch { prefs.setHoldPasteTriggerKey(it.lowercase()) } }
            )
            SettingSliderItem(stringResource(R.string.paste_hold_duration), holdPasteDuration.toFloat(), 200f..800f) { scope.launch { prefs.setHoldPasteDuration(it.toInt()) } }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem(stringResource(R.string.paste_recent), stringResource(R.string.paste_recent_desc), Icons.Default.ContentPaste, clipboardRecent) { scope.launch { prefs.setClipboardRecent(it) } }
        val expiryOptions = listOf(0, 1, 5, 525600)
        val expiryLabels = listOf(stringResource(R.string.paste_expiry_never), stringResource(R.string.paste_expiry_1m), stringResource(R.string.paste_expiry_5m), stringResource(R.string.paste_expiry_365d))
        val selectedIndex = expiryOptions.indexOf(clipboardExpiry).let { if (it < 0) 0 else it }
        SettingDropdownItem(
            title = stringResource(R.string.paste_expiry),
            subtitle = stringResource(R.string.paste_expiry_desc),
            icon = Icons.Default.Timer,
            selectedOption = expiryLabels[selectedIndex],
            options = expiryLabels,
            onOptionSelected = { label ->
                val idx = expiryLabels.indexOf(label)
                val mins = expiryOptions[idx]
                scope.launch { prefs.setClipboardExpiry(mins) }
                ClipboardManager.setExpiryMinutes(mins)
            }
        )
        SettingSwitchItem(stringResource(R.string.paste_images), stringResource(R.string.paste_images_desc), Icons.Default.Image, clipboardImages) { scope.launch { prefs.setClipboardImages(it) } }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .clickable { ClipboardManager.clearAllUnpinned() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.paste_delete_all), color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text(stringResource(R.string.paste_delete_all_desc), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingItem(stringResource(R.string.paste_view_history), stringResource(R.string.paste_view_history_desc), Icons.Default.History, onClick = onNavigateToClipboardHistory)
        Spacer(modifier = Modifier.height(32.dp))
    }
}
