package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageKeysSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val voiceInputKey by prefs.voiceInputKey.collectAsState(initial = true)
    val showEmojiKey by prefs.showEmojiKey.collectAsState(initial = true)
    val showGlobeKey by prefs.showGlobeKey.collectAsState(initial = true)
    val allowOtherKeyboards by prefs.allowOtherKeyboards.collectAsState(initial = true)
    val spacebarLanguageSwitch by prefs.spacebarLanguageSwitch.collectAsState(initial = false)
    val snackbarHostState = remember { SnackbarHostState() }

    // Enforce at least one language switch method remains enabled.
    // If both are somehow false (e.g., legacy data), auto-enable header.
    LaunchedEffect(showGlobeKey, spacebarLanguageSwitch) {
        if (!showGlobeKey && !spacebarLanguageSwitch) {
            prefs.setShowGlobeKey(true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_language_keys)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            SettingSwitchItem(stringResource(R.string.langkeys_voice), null, Icons.Default.Mic, voiceInputKey) { scope.launch { prefs.setVoiceInputKey(it) } }
            SettingSwitchItem(stringResource(R.string.langkeys_emoji), stringResource(R.string.langkeys_emoji_desc), Icons.Default.EmojiEmotions, showEmojiKey) { scope.launch { prefs.setShowEmojiKey(it) } }
            SettingSwitchItem(
                stringResource(R.string.langkeys_globe),
                stringResource(R.string.langkeys_globe_desc),
                Icons.Default.Language,
                showGlobeKey
            ) { enabled ->
                if (!enabled && !spacebarLanguageSwitch) {
                    scope.launch { snackbarHostState.showSnackbar("At least one language switch must stay enabled") }
                    return@SettingSwitchItem
                }
                scope.launch { prefs.setShowGlobeKey(enabled) }
            }
            SettingSwitchItem(
                stringResource(R.string.langkeys_spacebar_switch),
                stringResource(R.string.langkeys_spacebar_switch_desc),
                Icons.Default.Language,
                spacebarLanguageSwitch
            ) { enabled ->
                if (!enabled && !showGlobeKey) {
                    scope.launch { snackbarHostState.showSnackbar("At least one language switch must stay enabled") }
                    return@SettingSwitchItem
                }
                scope.launch { prefs.setSpacebarLanguageSwitch(enabled) }
            }
            SettingSwitchItem(stringResource(R.string.langkeys_other_kb), stringResource(R.string.langkeys_other_kb_desc), Icons.Default.Keyboard, allowOtherKeyboards) { scope.launch { prefs.setAllowOtherKeyboards(it) } }
            Spacer(modifier = Modifier.height(32.dp))
            // Helper text explaining the requirement
            Text(
                text = "Header badge and Spacebar hold+swipe are language switch methods. You can enable both, or just one — but at least one must stay enabled.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
