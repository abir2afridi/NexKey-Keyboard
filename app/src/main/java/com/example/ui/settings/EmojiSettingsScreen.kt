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
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
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

    SettingsSubScaffold(title = stringResource(R.string.settings_emoji), onBack = onBack) {
        // Recent emoji retention
        val expiryOptions = listOf(1, 7, 30, 90, 0)
        val expiryLabels = listOf(stringResource(R.string.emoji_expiry_1d), stringResource(R.string.emoji_expiry_7d), stringResource(R.string.emoji_expiry_30d), stringResource(R.string.emoji_expiry_90d), stringResource(R.string.emoji_expiry_forever))
        val selectedIndex = expiryOptions.indexOf(recentEmojiExpiry).let { if (it < 0) 2 else it }
        SettingDropdownItem(
            title = stringResource(R.string.emoji_retention),
            subtitle = stringResource(R.string.emoji_retention_desc),
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
            title = stringResource(R.string.emoji_horizontal),
            subtitle = if (emojiSearchHorizontal) stringResource(R.string.emoji_horizontal_desc_rows) else stringResource(R.string.emoji_horizontal_desc_vertical),
            icon = Icons.Default.ViewModule,
            checked = emojiSearchHorizontal,
            onCheckedChange = { scope.launch { prefs.setEmojiSearchHorizontal(it) } }
        )

        // Visible rows count (only when horizontal is ON)
        if (emojiSearchHorizontal) {
            Spacer(modifier = Modifier.height(16.dp))
            val rowOptions = listOf(1, 2)
            val rowLabels = rowOptions.map { pluralStringResource(R.plurals.emoji_rows, it, it) }
            val rowSelectedIndex = rowOptions.indexOf(emojiSearchVisibleRows).let { if (it < 0) 1 else it }
            SettingDropdownItem(
                title = stringResource(R.string.emoji_visible_rows),
                subtitle = stringResource(R.string.emoji_visible_rows_desc),
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
