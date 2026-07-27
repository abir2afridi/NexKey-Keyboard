package com.example.theme

import androidx.compose.ui.graphics.Color

enum class ThemePreset {
    DARK_NEON,
    LIGHT_MINIMAL,
    AMOLED_BLACK,
    EMERALD_GREEN,
    MATERIAL_YOU
}

data class KeyboardTheme(
    val preset: ThemePreset = ThemePreset.DARK_NEON,
    val backgroundColor: Color = Color(0xFF12131C),
    val keyBackgroundColor: Color = Color(0xFF1E2030),
    val keySpecialColor: Color = Color(0xFF2D314E),
    val keyTextColor: Color = Color(0xFFEEEEEE),
    val keySpecialTextColor: Color = Color(0xFF80D8FF),
    val accentColor: Color = Color(0xFF00E5FF),
    val suggestionBgColor: Color = Color(0xFF1A1C29),
    val keyRadiusDp: Int = 10,
    val keyHeightDp: Int = 54,
    val enableHaptics: Boolean = true,
    val enableSound: Boolean = true
) {
    companion object {
        val DarkNeon = KeyboardTheme(
            preset = ThemePreset.DARK_NEON,
            backgroundColor = Color(0xFF12131C),
            keyBackgroundColor = Color(0xFF1E2136),
            keySpecialColor = Color(0xFF2A2E4B),
            keyTextColor = Color(0xFFF1F3FB),
            keySpecialTextColor = Color(0xFF80D8FF),
            accentColor = Color(0xFF00E5FF),
            suggestionBgColor = Color(0xFF1A1C29)
        )

        val LightMinimal = KeyboardTheme(
            preset = ThemePreset.LIGHT_MINIMAL,
            backgroundColor = Color(0xFFECEFF1),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFCFD8DC),
            keyTextColor = Color(0xFF263238),
            keySpecialTextColor = Color(0xFF00838F),
            accentColor = Color(0xFF00ACC1),
            suggestionBgColor = Color(0xFFE0E0E0)
        )

        val AmoledBlack = KeyboardTheme(
            preset = ThemePreset.AMOLED_BLACK,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF121212),
            keySpecialColor = Color(0xFF1F1F1F),
            keyTextColor = Color(0xFFFFFFFF),
            keySpecialTextColor = Color(0xFFBB86FC),
            accentColor = Color(0xFFBB86FC),
            suggestionBgColor = Color(0xFF080808)
        )

        val EmeraldGreen = KeyboardTheme(
            preset = ThemePreset.EMERALD_GREEN,
            backgroundColor = Color(0xFF0A1F1C),
            keyBackgroundColor = Color(0xFF123530),
            keySpecialColor = Color(0xFF1D4E47),
            keyTextColor = Color(0xFFE0F2F1),
            keySpecialTextColor = Color(0xFF64FFDA),
            accentColor = Color(0xFF00E676),
            suggestionBgColor = Color(0xFF0F2B27)
        )
    }
}
