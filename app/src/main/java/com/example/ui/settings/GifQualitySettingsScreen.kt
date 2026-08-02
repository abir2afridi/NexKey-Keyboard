package com.example.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Gif
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
fun GifQualitySettingsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val prefs = remember { UserPreferences(context) }

    val highQual by prefs.highQualityGifs.collectAsState(initial = true)
    val sendHighQual by prefs.sendHighQualityGifs.collectAsState(initial = true)

    SettingsSubScaffold(title = stringResource(R.string.settings_gif_quality), onBack = onBack) {
        SettingSwitchItem(stringResource(R.string.gif_show_high), stringResource(R.string.gif_show_high_desc), Icons.Default.Gif, highQual) { scope.launch { prefs.setHighQualityGifs(it) } }
        SettingSwitchItem(stringResource(R.string.gif_send_high), stringResource(R.string.gif_send_high_desc), Icons.AutoMirrored.Filled.Send, sendHighQual) { scope.launch { prefs.setSendHighQualityGifs(it) } }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
