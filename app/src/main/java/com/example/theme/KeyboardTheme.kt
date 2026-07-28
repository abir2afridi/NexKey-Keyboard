package com.example.theme

import androidx.compose.ui.graphics.Color

enum class ThemePreset {
    DARK_NEON,
    LIGHT_MINIMAL,
    AMOLED_BLACK,
    EMERALD_GREEN,
    MIDNIGHT_BLUE,
    SUNSET_ORANGE,
    ROYAL_PURPLE,
    RETRO_GREY,
    CYBERPUNK,
    SAKURA_PINK,
    DEEP_OCEAN,
    CRIMSON_RED,
    LAVENDER_DREAM,
    FOREST_MOSS,
    GOLDEN_SAND,
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

        val MidnightBlue = KeyboardTheme(
            preset = ThemePreset.MIDNIGHT_BLUE,
            backgroundColor = Color(0xFF0D1117),
            keyBackgroundColor = Color(0xFF161B22),
            keySpecialColor = Color(0xFF21262D),
            keyTextColor = Color(0xFFC9D1D9),
            keySpecialTextColor = Color(0xFF58A6FF),
            accentColor = Color(0xFF58A6FF),
            suggestionBgColor = Color(0xFF010409)
        )

        val SunsetOrange = KeyboardTheme(
            preset = ThemePreset.SUNSET_ORANGE,
            backgroundColor = Color(0xFF2D1B0D),
            keyBackgroundColor = Color(0xFF3D2B1D),
            keySpecialColor = Color(0xFF4D3B2D),
            keyTextColor = Color(0xFFFFE0B2),
            keySpecialTextColor = Color(0xFFFF9800),
            accentColor = Color(0xFFFF9800),
            suggestionBgColor = Color(0xFF1D0B00)
        )

        val RoyalPurple = KeyboardTheme(
            preset = ThemePreset.ROYAL_PURPLE,
            backgroundColor = Color(0xFF1A0033),
            keyBackgroundColor = Color(0xFF2D0059),
            keySpecialColor = Color(0xFF3D0073),
            keyTextColor = Color(0xFFE6CCFF),
            keySpecialTextColor = Color(0xFFB366FF),
            accentColor = Color(0xFFB366FF),
            suggestionBgColor = Color(0xFF0D001A)
        )

        val RetroGrey = KeyboardTheme(
            preset = ThemePreset.RETRO_GREY,
            backgroundColor = Color(0xFFC0C0C0),
            keyBackgroundColor = Color(0xFFD0D0D0),
            keySpecialColor = Color(0xFFA0A0A0),
            keyTextColor = Color(0xFF000000),
            keySpecialTextColor = Color(0xFF000080),
            accentColor = Color(0xFF000080),
            suggestionBgColor = Color(0xFF808080)
        )

        val Cyberpunk = KeyboardTheme(
            preset = ThemePreset.CYBERPUNK,
            backgroundColor = Color(0xFF1A1A1A),
            keyBackgroundColor = Color(0xFF252525),
            keySpecialColor = Color(0xFF000000),
            keyTextColor = Color(0xFF00FF9F),
            keySpecialTextColor = Color(0xFFFF00A0),
            accentColor = Color(0xFFFF00A0),
            suggestionBgColor = Color(0xFF111111)
        )

        val SakuraPink = KeyboardTheme(
            preset = ThemePreset.SAKURA_PINK,
            backgroundColor = Color(0xFFFFF0F5),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFFFE4E1),
            keyTextColor = Color(0xFFDB7093),
            keySpecialTextColor = Color(0xFFFF69B4),
            accentColor = Color(0xFFFF69B4),
            suggestionBgColor = Color(0xFFFFF5F8)
        )

        val DeepOcean = KeyboardTheme(
            preset = ThemePreset.DEEP_OCEAN,
            backgroundColor = Color(0xFF001F3F),
            keyBackgroundColor = Color(0xFF003366),
            keySpecialColor = Color(0xFF004080),
            keyTextColor = Color(0xFFB3E5FC),
            keySpecialTextColor = Color(0xFF00BFFF),
            accentColor = Color(0xFF00BFFF),
            suggestionBgColor = Color(0xFF001326)
        )

        val CrimsonRed = KeyboardTheme(
            preset = ThemePreset.CRIMSON_RED,
            backgroundColor = Color(0xFF1A0000),
            keyBackgroundColor = Color(0xFF330000),
            keySpecialColor = Color(0xFF4D0000),
            keyTextColor = Color(0xFFFFCCCC),
            keySpecialTextColor = Color(0xFFFF0000),
            accentColor = Color(0xFFFF0000),
            suggestionBgColor = Color(0xFF0D0000)
        )

        val LavenderDream = KeyboardTheme(
            preset = ThemePreset.LAVENDER_DREAM,
            backgroundColor = Color(0xFFF3E5F5),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFE1BEE7),
            keyTextColor = Color(0xFF4A148C),
            keySpecialTextColor = Color(0xFF7B1FA2),
            accentColor = Color(0xFF7B1FA2),
            suggestionBgColor = Color(0xFFFAFAFA)
        )

        val ForestMoss = KeyboardTheme(
            preset = ThemePreset.FOREST_MOSS,
            backgroundColor = Color(0xFF1B2620),
            keyBackgroundColor = Color(0xFF2D3C33),
            keySpecialColor = Color(0xFF3D4D43),
            keyTextColor = Color(0xFFC8D6CA),
            keySpecialTextColor = Color(0xFF81C784),
            accentColor = Color(0xFF4CAF50),
            suggestionBgColor = Color(0xFF0E1410)
        )

        val GoldenSand = KeyboardTheme(
            preset = ThemePreset.GOLDEN_SAND,
            backgroundColor = Color(0xFF3E2723),
            keyBackgroundColor = Color(0xFF4E342E),
            keySpecialColor = Color(0xFF5D4037),
            keyTextColor = Color(0xFFD7CCC8),
            keySpecialTextColor = Color(0xFFFFD54F),
            accentColor = Color(0xFFFFA000),
            suggestionBgColor = Color(0xFF261814)
        )

        val MaterialYou = KeyboardTheme(
            preset = ThemePreset.MATERIAL_YOU,
            backgroundColor = Color(0xFFF7F9FF),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFE1E2EC),
            keyTextColor = Color(0xFF191C20),
            keySpecialTextColor = Color(0xFF44474E),
            accentColor = Color(0xFF0061A4),
            suggestionBgColor = Color(0xFFF0F0F0)
        )

        fun fromPreset(preset: ThemePreset): KeyboardTheme {
            return when (preset) {
                ThemePreset.DARK_NEON -> DarkNeon
                ThemePreset.LIGHT_MINIMAL -> LightMinimal
                ThemePreset.AMOLED_BLACK -> AmoledBlack
                ThemePreset.EMERALD_GREEN -> EmeraldGreen
                ThemePreset.MIDNIGHT_BLUE -> MidnightBlue
                ThemePreset.SUNSET_ORANGE -> SunsetOrange
                ThemePreset.ROYAL_PURPLE -> RoyalPurple
                ThemePreset.RETRO_GREY -> RetroGrey
                ThemePreset.CYBERPUNK -> Cyberpunk
                ThemePreset.SAKURA_PINK -> SakuraPink
                ThemePreset.DEEP_OCEAN -> DeepOcean
                ThemePreset.CRIMSON_RED -> CrimsonRed
                ThemePreset.LAVENDER_DREAM -> LavenderDream
                ThemePreset.FOREST_MOSS -> ForestMoss
                ThemePreset.GOLDEN_SAND -> GoldenSand
                ThemePreset.MATERIAL_YOU -> MaterialYou
            }
        }

        fun allThemes(): List<KeyboardTheme> {
            return ThemePreset.values().map { fromPreset(it) }
        }
    }
}
