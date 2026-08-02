package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.UserPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreLanguagesScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }
    val enableBanglaJatiyo by prefs.enableBanglaJatiyo.collectAsState(initial = true)
    val enableAvro by prefs.enableAvro.collectAsState(initial = true)
    val enableArabic by prefs.enableArabic.collectAsState(initial = true)

    SettingsSubScaffold(title = stringResource(R.string.more_lang_title), onBack = onBack) {
        Text(
            text = stringResource(R.string.more_lang_desc),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
        )

        SettingSwitchItem(stringResource(R.string.more_lang_english), stringResource(R.string.more_lang_english_desc), Icons.Default.Language, true) {}
        SettingSwitchItem(stringResource(R.string.more_lang_bangla), stringResource(R.string.more_lang_bangla_desc), Icons.Default.Language, enableBanglaJatiyo) { scope.launch { prefs.setEnableBanglaJatiyo(it) } }
        SettingSwitchItem(stringResource(R.string.more_lang_avro), stringResource(R.string.more_lang_avro_desc), Icons.Default.Language, enableAvro) { scope.launch { prefs.setEnableAvro(it) } }
        SettingSwitchItem(stringResource(R.string.more_lang_arabic), stringResource(R.string.more_lang_arabic_desc), Icons.Default.Language, enableArabic) { scope.launch { prefs.setEnableArabic(it) } }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
