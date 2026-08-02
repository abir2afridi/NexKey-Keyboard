package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
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

    SettingsSubScaffold(title = stringResource(R.string.settings_language_keys), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.langkeys_voice), null, Icons.Default.Mic, voiceInputKey) { scope.launch { prefs.setVoiceInputKey(it) } }
        SettingSwitchItem(stringResource(R.string.langkeys_emoji), stringResource(R.string.langkeys_emoji_desc), Icons.Default.EmojiEmotions, showEmojiKey) { scope.launch { prefs.setShowEmojiKey(it) } }
        SettingSwitchItem(stringResource(R.string.langkeys_globe), stringResource(R.string.langkeys_globe_desc), Icons.Default.Language, showGlobeKey) { scope.launch { prefs.setShowGlobeKey(it) } }
        SettingSwitchItem(stringResource(R.string.langkeys_other_kb), stringResource(R.string.langkeys_other_kb_desc), Icons.Default.Keyboard, allowOtherKeyboards) { scope.launch { prefs.setAllowOtherKeyboards(it) } }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
