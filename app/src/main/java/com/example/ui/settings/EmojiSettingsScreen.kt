package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DensitySmall
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.ViewModule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val recentEmojiExpiry by prefs.recentEmojiExpiry.collectAsState(initial = 30)
    val emojiSearchVisibleRows by prefs.emojiSearchVisibleRows.collectAsState(initial = 2)
    val emojiSearchHorizontal by prefs.emojiSearchHorizontal.collectAsState(initial = true)

    SettingsSubScaffold(title = "Emoji", onBack = onBack) {
        // Recent emoji retention
        val expiryOptions = listOf(1, 7, 30, 90, 0)
        val expiryLabels = listOf("1 day", "7 days", "30 days", "90 days", "Forever")
        val selectedIndex = expiryOptions.indexOf(recentEmojiExpiry).let { if (it < 0) 2 else it }
        SettingDropdownItem(
            title = "Recent emoji retention",
            subtitle = "How long to remember recently used emojis",
            icon = Icons.Default.EmojiEmotions,
            selectedOption = expiryLabels[selectedIndex],
            options = expiryLabels,
            onOptionSelected = { label ->
                val idx = expiryLabels.indexOf(label)
                val days = expiryOptions[idx]
                scope.launch { prefs.setRecentEmojiExpiry(days) }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Search results layout
        SettingSwitchItem(
            title = "Horizontal scroll",
            subtitle = if (emojiSearchHorizontal) "Rows scroll left-right independently" else "Vertical list layout",
            icon = Icons.Default.ViewModule,
            checked = emojiSearchHorizontal,
            onCheckedChange = { scope.launch { prefs.setEmojiSearchHorizontal(it) } }
        )

        // Visible rows count (only when horizontal is ON)
        if (emojiSearchHorizontal) {
            Spacer(modifier = Modifier.height(16.dp))
            val rowOptions = listOf(1, 2)
            val rowLabels = rowOptions.map { "$it row${if (it > 1) "s" else ""}" }
            val rowSelectedIndex = rowOptions.indexOf(emojiSearchVisibleRows).let { if (it < 0) 1 else it }
            SettingDropdownItem(
                title = "Visible rows",
                subtitle = "Number of emoji rows shown in search results",
                icon = Icons.Default.DensitySmall,
                selectedOption = rowLabels[rowSelectedIndex],
                options = rowLabels,
                onOptionSelected = { label ->
                    val idx = rowLabels.indexOf(label)
                    val rows = rowOptions[idx]
                    scope.launch { prefs.setEmojiSearchVisibleRows(rows) }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
