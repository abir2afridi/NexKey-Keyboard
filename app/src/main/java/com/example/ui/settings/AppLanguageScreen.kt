package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppLanguageOption(val code: String, val displayName: String, val localName: String)

private val appLanguages = listOf(
    AppLanguageOption("en", "English", "English"),
    AppLanguageOption("bn", "Bengali", "বাংলা"),
    AppLanguageOption("hi", "Hindi", "हिन्दी"),
    AppLanguageOption("ar", "Arabic", "العربية"),
    AppLanguageOption("es", "Spanish", "Español"),
    AppLanguageOption("fr", "French", "Français"),
    AppLanguageOption("de", "German", "Deutsch"),
    AppLanguageOption("pt", "Portuguese", "Português"),
    AppLanguageOption("ru", "Russian", "Русский"),
    AppLanguageOption("ja", "Japanese", "日本語"),
    AppLanguageOption("ko", "Korean", "한국어"),
    AppLanguageOption("zh", "Chinese (Simplified)", "简体中文"),
    AppLanguageOption("zh_TW", "Chinese (Traditional)", "繁體中文"),
    AppLanguageOption("ur", "Urdu", "اردو"),
    AppLanguageOption("fa", "Persian", "فارسی")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLanguageScreen(onBack: () -> Unit) {
    var selectedLanguage by remember { mutableStateOf("en") }

    SettingsSubScaffold(title = "App Language", onBack = onBack) {
        Text(
            text = "Choose the display language for the app interface",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp)
        )
        appLanguages.forEach { lang ->
            val isSelected = selectedLanguage == lang.code
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primaryContainer
                        else Color.Transparent
                    )
                    .clickable { selectedLanguage = lang.code }
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lang.localName,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = lang.displayName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}
