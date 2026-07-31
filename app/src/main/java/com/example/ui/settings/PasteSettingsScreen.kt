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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    SettingsSubScaffold(title = "Paste & Clipboard", onBack = onBack) {
        SettingSwitchItem("Hold key to paste", "Long-press a key to paste clipboard text", Icons.Default.ContentPaste, holdPasteEnabled) { scope.launch { prefs.setHoldPasteEnabled(it) } }
        if (holdPasteEnabled) {
            SettingDropdownItem(
                title = "Trigger key",
                subtitle = "Key to hold for paste",
                icon = Icons.Default.Keyboard,
                selectedOption = holdPasteTriggerKey.uppercase(),
                options = listOf("V", "B", "N", "M", "G", "H", "Space", "Enter"),
                onOptionSelected = { scope.launch { prefs.setHoldPasteTriggerKey(it.lowercase()) } }
            )
            SettingSliderItem("Hold duration", holdPasteDuration.toFloat(), 200f..800f) { scope.launch { prefs.setHoldPasteDuration(it.toInt()) } }
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingSwitchItem("Clipboard Recent Items", "Show recent copied or cut text in clipboard", Icons.Default.ContentPaste, clipboardRecent) { scope.launch { prefs.setClipboardRecent(it) } }
        val expiryOptions = listOf(0, 1, 5, 525600)
        val expiryLabels = listOf("Never", "1 minute", "5 minutes", "365 days")
        val selectedIndex = expiryOptions.indexOf(clipboardExpiry).let { if (it < 0) 0 else it }
        SettingDropdownItem(
            title = "Auto-delete clipboard items",
            subtitle = "Remove copies after the selected time",
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
        SettingSwitchItem("Show copied images on Clipboard", "Show screenshots or copied images", Icons.Default.Image, clipboardImages) { scope.launch { prefs.setClipboardImages(it) } }
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
                Text("Delete All Clipboard Items", color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Remove all non-pinned copied texts", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
        SettingItem("View Clipboard History", "Browse all copied texts", Icons.Default.History, onClick = onNavigateToClipboardHistory)
        Spacer(modifier = Modifier.height(32.dp))
    }
}
