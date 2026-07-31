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
import androidx.compose.ui.unit.dp
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCorrectionSettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val autoCorrection by prefs.autoCorrection.collectAsState(initial = true)
    val phoneticAutoCorrect by prefs.phoneticAutoCorrection.collectAsState(initial = true)
    val showSuggestions by prefs.showSuggestions.collectAsState(initial = true)
    val personalized by prefs.personalizedSuggestions.collectAsState(initial = true)
    val nextWord by prefs.nextWordSuggestions.collectAsState(initial = true)

    SettingsSubScaffold(title = "Text correction", onBack = onBack) {
        SettingSwitchItem("Auto-correction", "Punctuation corrects words", Icons.Default.Spellcheck, autoCorrection) { scope.launch { prefs.setAutoCorrection(it) } }
        SettingSwitchItem("Phonetic auto-correction", "Correct Bangla transliteration", Icons.Default.Translate, phoneticAutoCorrect) { scope.launch { prefs.setPhoneticAutoCorrection(it) } }
        SettingSwitchItem("Show suggestions", "Display words while typing", Icons.Default.Lightbulb, showSuggestions) { scope.launch { prefs.setShowSuggestions(it) } }
        SettingSwitchItem("Personalized suggestions", "Learn from communication", Icons.Default.Person, personalized) { scope.launch { prefs.setPersonalizedSuggestions(it) } }
        SettingSwitchItem("Next-word suggestions", "Use previous word", Icons.Default.History, nextWord) { scope.launch { prefs.setNextWordSuggestions(it) } }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
