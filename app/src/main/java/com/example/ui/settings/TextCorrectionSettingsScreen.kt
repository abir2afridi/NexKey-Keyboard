package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Translate
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
fun TextCorrectionSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences.getInstance(context) }
    val autoCorrection by prefs.autoCorrection.collectAsState(initial = true)
    val phoneticAutoCorrect by prefs.phoneticAutoCorrection.collectAsState(initial = true)
    val showSuggestions by prefs.showSuggestions.collectAsState(initial = true)
    val personalized by prefs.personalizedSuggestions.collectAsState(initial = true)
    val nextWord by prefs.nextWordSuggestions.collectAsState(initial = true)

    SettingsSubScaffold(title = stringResource(R.string.settings_text_correction), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.tc_auto_correct), stringResource(R.string.tc_auto_correct_desc), Icons.Default.Spellcheck, autoCorrection) { scope.launch { prefs.setAutoCorrection(it) } }
        SettingSwitchItem(stringResource(R.string.tc_phonetic), stringResource(R.string.tc_phonetic_desc), Icons.Default.Translate, phoneticAutoCorrect) { scope.launch { prefs.setPhoneticAutoCorrection(it) } }
        SettingSwitchItem(stringResource(R.string.tc_show_suggestions), stringResource(R.string.tc_show_suggestions_desc), Icons.Default.Lightbulb, showSuggestions) { scope.launch { prefs.setShowSuggestions(it) } }
        SettingSwitchItem(stringResource(R.string.tc_personalized), stringResource(R.string.tc_personalized_desc), Icons.Default.Person, personalized) { scope.launch { prefs.setPersonalizedSuggestions(it) } }
        SettingSwitchItem(stringResource(R.string.tc_next_word), stringResource(R.string.tc_next_word_desc), Icons.Default.History, nextWord) { scope.launch { prefs.setNextWordSuggestions(it) } }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
