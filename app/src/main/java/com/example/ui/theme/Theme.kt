package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)

private val LightColorScheme =
  lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
  )

@Composable
fun MyApplicationTheme(
  appTheme: String = "SYSTEM",
  accentColorHex: String = "#FF2E7D32",
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val darkTheme = when (appTheme) {
    "DARK" -> true
    "LIGHT" -> false
    else -> isSystemInDarkTheme()
  }

  val seedColor = try {
    Color(android.graphics.Color.parseColor(accentColorHex))
  } catch (_: Exception) {
    Color(0xFF2E7D32)
  }

  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> {
      DarkColorScheme.copy(
        primary = seedColor,
        onPrimary = Color.White,
        primaryContainer = seedColor.copy(alpha = 0.2f),
        onPrimaryContainer = seedColor
      )
    }
    else -> {
      LightColorScheme.copy(
        primary = seedColor,
        onPrimary = Color.White,
        primaryContainer = seedColor.copy(alpha = 0.1f),
        onPrimaryContainer = seedColor
      )
    }
  }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
