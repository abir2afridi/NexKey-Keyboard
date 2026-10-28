package com.example.theme

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.Immutable
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
    MATERIAL_YOU,
    NOTHING_DARK,
    NOTHING_LIGHT,
    AMOLED_NOTHING,
    AMOLED_DARK_PURPLE,
    ORBIT,
    SUNFLOWER,
    SNOWFALL,
    STEEL_GRAY,
    COTTON_CANDY,
    DEEP_SEA_LIGHT,
    DEEP_SEA_DARK,
    CLASSIC_MATERIAL_LIGHT,
    CLASSIC_MATERIAL_DARK,
    NOTHING_THEME,
    DYNAMIC_NOTHING,
    DYNAMIC_DARK,
    DYNAMIC_LIGHT,
    CUSTOM
}

@Immutable
data class KeyboardTheme(
    val preset: ThemePreset = ThemePreset.DARK_NEON,
    val backgroundColor: Color = Color(0xFF12131C),
    val keyBackgroundColor: Color = Color(0xFF1E2030),
    val keySpecialColor: Color = Color(0xFF2D314E),
    val keyTextColor: Color = Color(0xFFEEEEEE),
    val keySpecialTextColor: Color = Color(0xFF80D8FF),
    val accentColor: Color = Color(0xFF00E5FF),
    val suggestionBgColor: Color = Color(0xFF1A1C29),
    val suggestionTextColor: Color = Color(0xFFEEEEEE),
    val popupBackgroundColor: Color = Color(0xFF2D314E),
    val popupTextColor: Color = Color(0xFFFFFFFF),
    val keyHintColor: Color = Color(0xFFEEEEEE).copy(alpha = 0.4f),
    val showKeyBorders: Boolean = false,
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
            suggestionBgColor = Color(0xFF1A1C29),
            suggestionTextColor = Color(0xFFF1F3FB),
            popupBackgroundColor = Color(0xFF2A2E4B),
            popupTextColor = Color(0xFF00E5FF),
            keyHintColor = Color(0xFFF1F3FB).copy(alpha = 0.4f)
        )

        val LightMinimal = KeyboardTheme(
            preset = ThemePreset.LIGHT_MINIMAL,
            backgroundColor = Color(0xFFECEFF1),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFCFD8DC),
            keyTextColor = Color(0xFF263238),
            keySpecialTextColor = Color(0xFF00838F),
            accentColor = Color(0xFF00ACC1),
            suggestionBgColor = Color(0xFFE0E0E0),
            suggestionTextColor = Color(0xFF263238),
            popupBackgroundColor = Color(0xFFCFD8DC),
            popupTextColor = Color(0xFF00838F),
            keyHintColor = Color(0xFF263238).copy(alpha = 0.35f)
        )

        val AmoledBlack = KeyboardTheme(
            preset = ThemePreset.AMOLED_BLACK,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF121212),
            keySpecialColor = Color(0xFF1F1F1F),
            keyTextColor = Color(0xFFFFFFFF),
            keySpecialTextColor = Color(0xFFBB86FC),
            accentColor = Color(0xFFBB86FC),
            suggestionBgColor = Color(0xFF080808),
            suggestionTextColor = Color(0xFFFFFFFF),
            popupBackgroundColor = Color(0xFF1F1F1F),
            popupTextColor = Color(0xFFBB86FC),
            keyHintColor = Color(0xFFFFFFFF).copy(alpha = 0.4f)
        )

        val EmeraldGreen = KeyboardTheme(
            preset = ThemePreset.EMERALD_GREEN,
            backgroundColor = Color(0xFF0A1F1C),
            keyBackgroundColor = Color(0xFF123530),
            keySpecialColor = Color(0xFF1D4E47),
            keyTextColor = Color(0xFFE0F2F1),
            keySpecialTextColor = Color(0xFF64FFDA),
            accentColor = Color(0xFF00E676),
            suggestionBgColor = Color(0xFF0F2B27),
            suggestionTextColor = Color(0xFFE0F2F1),
            popupBackgroundColor = Color(0xFF1D4E47),
            popupTextColor = Color(0xFF64FFDA),
            keyHintColor = Color(0xFFE0F2F1).copy(alpha = 0.35f)
        )

        val MidnightBlue = KeyboardTheme(
            preset = ThemePreset.MIDNIGHT_BLUE,
            backgroundColor = Color(0xFF0D1117),
            keyBackgroundColor = Color(0xFF161B22),
            keySpecialColor = Color(0xFF21262D),
            keyTextColor = Color(0xFFC9D1D9),
            keySpecialTextColor = Color(0xFF58A6FF),
            accentColor = Color(0xFF58A6FF),
            suggestionBgColor = Color(0xFF010409),
            suggestionTextColor = Color(0xFFC9D1D9),
            popupBackgroundColor = Color(0xFF21262D),
            popupTextColor = Color(0xFF58A6FF),
            keyHintColor = Color(0xFFC9D1D9).copy(alpha = 0.4f)
        )

        val SunsetOrange = KeyboardTheme(
            preset = ThemePreset.SUNSET_ORANGE,
            backgroundColor = Color(0xFF2D1B0D),
            keyBackgroundColor = Color(0xFF3D2B1D),
            keySpecialColor = Color(0xFF4D3B2D),
            keyTextColor = Color(0xFFFFE0B2),
            keySpecialTextColor = Color(0xFFFF9800),
            accentColor = Color(0xFFFF9800),
            suggestionBgColor = Color(0xFF1D0B00),
            suggestionTextColor = Color(0xFFFFE0B2),
            popupBackgroundColor = Color(0xFF4D3B2D),
            popupTextColor = Color(0xFFFF9800),
            keyHintColor = Color(0xFFFFE0B2).copy(alpha = 0.4f)
        )

        val RoyalPurple = KeyboardTheme(
            preset = ThemePreset.ROYAL_PURPLE,
            backgroundColor = Color(0xFF1A0033),
            keyBackgroundColor = Color(0xFF2D0059),
            keySpecialColor = Color(0xFF3D0073),
            keyTextColor = Color(0xFFE6CCFF),
            keySpecialTextColor = Color(0xFFB366FF),
            accentColor = Color(0xFFB366FF),
            suggestionBgColor = Color(0xFF0D001A),
            suggestionTextColor = Color(0xFFE6CCFF),
            popupBackgroundColor = Color(0xFF3D0073),
            popupTextColor = Color(0xFFB366FF),
            keyHintColor = Color(0xFFE6CCFF).copy(alpha = 0.35f)
        )

        val RetroGrey = KeyboardTheme(
            preset = ThemePreset.RETRO_GREY,
            backgroundColor = Color(0xFFC0C0C0),
            keyBackgroundColor = Color(0xFFD0D0D0),
            keySpecialColor = Color(0xFFA0A0A0),
            keyTextColor = Color(0xFF000000),
            keySpecialTextColor = Color(0xFF000080),
            accentColor = Color(0xFF000080),
            suggestionBgColor = Color(0xFF808080),
            suggestionTextColor = Color(0xFF000000),
            popupBackgroundColor = Color(0xFFA0A0A0),
            popupTextColor = Color(0xFF000080),
            keyHintColor = Color(0xFF000000).copy(alpha = 0.45f)
        )

        val Cyberpunk = KeyboardTheme(
            preset = ThemePreset.CYBERPUNK,
            backgroundColor = Color(0xFF1A1A1A),
            keyBackgroundColor = Color(0xFF252525),
            keySpecialColor = Color(0xFF000000),
            keyTextColor = Color(0xFF00FF9F),
            keySpecialTextColor = Color(0xFFFF00A0),
            accentColor = Color(0xFFFF00A0),
            suggestionBgColor = Color(0xFF111111),
            suggestionTextColor = Color(0xFF00FF9F),
            popupBackgroundColor = Color(0xFF000000),
            popupTextColor = Color(0xFFFF00A0),
            keyHintColor = Color(0xFF00FF9F).copy(alpha = 0.5f)
        )

        val SakuraPink = KeyboardTheme(
            preset = ThemePreset.SAKURA_PINK,
            backgroundColor = Color(0xFFFFF0F5),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFFFE4E1),
            keyTextColor = Color(0xFFDB7093),
            keySpecialTextColor = Color(0xFFFF69B4),
            accentColor = Color(0xFFFF69B4),
            suggestionBgColor = Color(0xFFFFF5F8),
            suggestionTextColor = Color(0xFFDB7093),
            popupBackgroundColor = Color(0xFFFFE4E1),
            popupTextColor = Color(0xFFFF69B4),
            keyHintColor = Color(0xFFDB7093).copy(alpha = 0.4f)
        )

        val DeepOcean = KeyboardTheme(
            preset = ThemePreset.DEEP_OCEAN,
            backgroundColor = Color(0xFF001F3F),
            keyBackgroundColor = Color(0xFF003366),
            keySpecialColor = Color(0xFF004080),
            keyTextColor = Color(0xFFB3E5FC),
            keySpecialTextColor = Color(0xFF00BFFF),
            accentColor = Color(0xFF00BFFF),
            suggestionBgColor = Color(0xFF001326),
            suggestionTextColor = Color(0xFFB3E5FC),
            popupBackgroundColor = Color(0xFF004080),
            popupTextColor = Color(0xFF00BFFF),
            keyHintColor = Color(0xFFB3E5FC).copy(alpha = 0.35f)
        )

        val CrimsonRed = KeyboardTheme(
            preset = ThemePreset.CRIMSON_RED,
            backgroundColor = Color(0xFF1A0000),
            keyBackgroundColor = Color(0xFF330000),
            keySpecialColor = Color(0xFF4D0000),
            keyTextColor = Color(0xFFFFCCCC),
            keySpecialTextColor = Color(0xFFFF0000),
            accentColor = Color(0xFFFF0000),
            suggestionBgColor = Color(0xFF0D0000),
            suggestionTextColor = Color(0xFFFFCCCC),
            popupBackgroundColor = Color(0xFF4D0000),
            popupTextColor = Color(0xFFFF0000),
            keyHintColor = Color(0xFFFFCCCC).copy(alpha = 0.4f)
        )

        val LavenderDream = KeyboardTheme(
            preset = ThemePreset.LAVENDER_DREAM,
            backgroundColor = Color(0xFFF3E5F5),
            keyBackgroundColor = Color(0xFFFFFFFF),
            keySpecialColor = Color(0xFFE1BEE7),
            keyTextColor = Color(0xFF4A148C),
            keySpecialTextColor = Color(0xFF7B1FA2),
            accentColor = Color(0xFF7B1FA2),
            suggestionBgColor = Color(0xFFFAFAFA),
            suggestionTextColor = Color(0xFF4A148C),
            popupBackgroundColor = Color(0xFFE1BEE7),
            popupTextColor = Color(0xFF7B1FA2),
            keyHintColor = Color(0xFF4A148C).copy(alpha = 0.35f)
        )

        val ForestMoss = KeyboardTheme(
            preset = ThemePreset.FOREST_MOSS,
            backgroundColor = Color(0xFF1B2620),
            keyBackgroundColor = Color(0xFF2D3C33),
            keySpecialColor = Color(0xFF3D4D43),
            keyTextColor = Color(0xFFC8D6CA),
            keySpecialTextColor = Color(0xFF81C784),
            accentColor = Color(0xFF4CAF50),
            suggestionBgColor = Color(0xFF0E1410),
            suggestionTextColor = Color(0xFFC8D6CA),
            popupBackgroundColor = Color(0xFF3D4D43),
            popupTextColor = Color(0xFF4CAF50),
            keyHintColor = Color(0xFFC8D6CA).copy(alpha = 0.4f)
        )

        val GoldenSand = KeyboardTheme(
            preset = ThemePreset.GOLDEN_SAND,
            backgroundColor = Color(0xFF3E2723),
            keyBackgroundColor = Color(0xFF4E342E),
            keySpecialColor = Color(0xFF5D4037),
            keyTextColor = Color(0xFFD7CCC8),
            keySpecialTextColor = Color(0xFFFFD54F),
            accentColor = Color(0xFFFFA000),
            suggestionBgColor = Color(0xFF261814),
            suggestionTextColor = Color(0xFFD7CCC8),
            popupBackgroundColor = Color(0xFF5D4037),
            popupTextColor = Color(0xFFFFA000),
            keyHintColor = Color(0xFFD7CCC8).copy(alpha = 0.4f)
        )

        val MaterialYouDark = KeyboardTheme(
            preset = ThemePreset.MATERIAL_YOU,
            backgroundColor = Color(0xFF141218),
            keyBackgroundColor = Color(0xFF1D1B20),
            keySpecialColor = Color(0xFF322F35),
            keyTextColor = Color(0xFFE6E0E9),
            keySpecialTextColor = Color(0xFFD0BCFF),
            accentColor = Color(0xFFD0BCFF),
            suggestionBgColor = Color(0xFF0F0D13),
            suggestionTextColor = Color(0xFFE6E0E9),
            popupBackgroundColor = Color(0xFF49454F),
            popupTextColor = Color(0xFFD0BCFF),
            keyHintColor = Color(0xFFE6E0E9).copy(alpha = 0.4f)
        )

        val MaterialYouLight = KeyboardTheme(
            preset = ThemePreset.MATERIAL_YOU,
            backgroundColor = Color(0xFFFFF7FF),
            keyBackgroundColor = Color(0xFFF3EDF7),
            keySpecialColor = Color(0xFFE8DEF8),
            keyTextColor = Color(0xFF1D1B20),
            keySpecialTextColor = Color(0xFF6750A4),
            accentColor = Color(0xFF6750A4),
            suggestionBgColor = Color(0xFFF7F2FA),
            suggestionTextColor = Color(0xFF1D1B20),
            popupBackgroundColor = Color(0xFFE8DEF8),
            popupTextColor = Color(0xFF6750A4),
            keyHintColor = Color(0xFF1D1B20).copy(alpha = 0.35f)
        )

        val NothingDark = KeyboardTheme(
            preset = ThemePreset.NOTHING_DARK,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF0D0C11),
            keySpecialColor = Color(0xFF1B1A1F),
            keyTextColor = Color(0xFFF1F1F1),
            keySpecialTextColor = Color(0xFFD71921),
            accentColor = Color(0xFFD71921),
            suggestionBgColor = Color(0xFF0D0C11),
            suggestionTextColor = Color(0xFFE6E1E5),
            popupBackgroundColor = Color(0xFF1B1A1F),
            popupTextColor = Color(0xFFD71921),
            keyHintColor = Color(0xFFF1F1F1).copy(alpha = 0.4f)
        )

        val NothingLight = KeyboardTheme(
            preset = ThemePreset.NOTHING_LIGHT,
            backgroundColor = Color(0xFFE4E7E9),
            keyBackgroundColor = Color(0xFFECEFF1),
            keySpecialColor = Color(0xFFD5D5D5),
            keyTextColor = Color(0xFF191C1C),
            keySpecialTextColor = Color(0xFFD71921),
            accentColor = Color(0xFFD71921),
            suggestionBgColor = Color(0xFFD5D5D5),
            suggestionTextColor = Color(0xFF191C1C),
            popupBackgroundColor = Color(0xFFD5D5D5),
            popupTextColor = Color(0xFFD71921),
            keyHintColor = Color(0xFF191C1C).copy(alpha = 0.35f)
        )

        val AmoledNothing = KeyboardTheme(
            preset = ThemePreset.AMOLED_NOTHING,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF0D0C11),
            keySpecialColor = Color(0xFF1B1A1F),
            keyTextColor = Color(0xFFFFFFFF),
            keySpecialTextColor = Color(0xFFD71921),
            accentColor = Color(0xFFD71921),
            suggestionBgColor = Color(0xFF000000),
            suggestionTextColor = Color(0xFFFFFFFF),
            popupBackgroundColor = Color(0xFF1B1A1F),
            popupTextColor = Color(0xFFD71921),
            keyHintColor = Color(0xFFFFFFFF).copy(alpha = 0.4f)
        )

        val AmoledDarkPurple = KeyboardTheme(
            preset = ThemePreset.AMOLED_DARK_PURPLE,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF0D0D12),
            keySpecialColor = Color(0xFF4A4458),
            keyTextColor = Color(0xFFE6E1E5),
            keySpecialTextColor = Color(0xFFD0BCFF),
            accentColor = Color(0xFFD0BCFF),
            suggestionBgColor = Color(0xFF000000),
            suggestionTextColor = Color(0xFFE6E1E5),
            popupBackgroundColor = Color(0xFF4A4458),
            popupTextColor = Color(0xFFD0BCFF),
            keyHintColor = Color(0xFFE6E1E5).copy(alpha = 0.4f)
        )

        val Orbit = KeyboardTheme(
            preset = ThemePreset.ORBIT,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF1C1D20),
            keySpecialColor = Color(0xFF2A5288),
            keyTextColor = Color(0xFFEAE6E1),
            keySpecialTextColor = Color(0xFFEAE6E1),
            accentColor = Color(0xFFC23636),
            suggestionBgColor = Color(0xFF1C1D20),
            suggestionTextColor = Color(0xFFEAE6E1),
            popupBackgroundColor = Color(0xFF2A5288),
            popupTextColor = Color(0xFFEAE6E1),
            keyHintColor = Color(0xFFEAE6E1).copy(alpha = 0.4f)
        )

        val Sunflower = KeyboardTheme(
            preset = ThemePreset.SUNFLOWER,
            backgroundColor = Color(0xFFFFF9EE),
            keyBackgroundColor = Color(0xFFFAF3E5),
            keySpecialColor = Color(0xFFEAE2D0),
            keyTextColor = Color(0xFF1E1B13),
            keySpecialTextColor = Color(0xFF6D5E0F),
            accentColor = Color(0xFF6D5E0F),
            suggestionBgColor = Color(0xFFF4EDDF),
            suggestionTextColor = Color(0xFF1E1C13),
            popupBackgroundColor = Color(0xFFEAE2D0),
            popupTextColor = Color(0xFF6D5E0F),
            keyHintColor = Color(0xFF1E1B13).copy(alpha = 0.35f)
        )

        val Snowfall = KeyboardTheme(
            preset = ThemePreset.SNOWFALL,
            backgroundColor = Color(0xFFF8F8F8),
            keyBackgroundColor = Color(0xFFF1EDEC),
            keySpecialColor = Color(0xFFE0E3E3),
            keyTextColor = Color(0xFF1C1B1B),
            keySpecialTextColor = Color(0xFF212223),
            accentColor = Color(0xFF212223),
            suggestionBgColor = Color(0xFFFFFFFF),
            suggestionTextColor = Color(0xFF1C1B1B),
            popupBackgroundColor = Color(0xFFE5E2E1),
            popupTextColor = Color(0xFF212223),
            keyHintColor = Color(0xFF1C1B1B).copy(alpha = 0.35f)
        )

        val SteelGray = KeyboardTheme(
            preset = ThemePreset.STEEL_GRAY,
            backgroundColor = Color(0xFFA8A8A8),
            keyBackgroundColor = Color(0xFFBBBBBB),
            keySpecialColor = Color(0xFFE0E3E3),
            keyTextColor = Color(0xFF1C1B1B),
            keySpecialTextColor = Color(0xFF212223),
            accentColor = Color(0xFF212223),
            suggestionBgColor = Color(0xFFDDD9D9),
            suggestionTextColor = Color(0xFF1C1B1B),
            popupBackgroundColor = Color(0xFFE5E2E1),
            popupTextColor = Color(0xFF212223),
            keyHintColor = Color(0xFF1C1B1B).copy(alpha = 0.4f)
        )

        val CottonCandy = KeyboardTheme(
            preset = ThemePreset.COTTON_CANDY,
            backgroundColor = Color(0xFFFFECF9),
            keyBackgroundColor = Color(0xFFFFF0F8),
            keySpecialColor = Color(0xFFF2DDEB),
            keyTextColor = Color(0xFF21191F),
            keySpecialTextColor = Color(0xFFEC5EB4),
            accentColor = Color(0xFFEC5EB4),
            suggestionBgColor = Color(0xFFF9EAF2),
            suggestionTextColor = Color(0xFF21191F),
            popupBackgroundColor = Color(0xFFF2DDEB),
            popupTextColor = Color(0xFFEC5EB4),
            keyHintColor = Color(0xFF21191F).copy(alpha = 0.35f)
        )

        val DeepSeaLight = KeyboardTheme(
            preset = ThemePreset.DEEP_SEA_LIGHT,
            backgroundColor = Color(0xFFDBE8FF),
            keyBackgroundColor = Color(0xFFF5F2FD),
            keySpecialColor = Color(0xFFE1E0F7),
            keyTextColor = Color(0xFF1A1B26),
            keySpecialTextColor = Color(0xFF4062D3),
            accentColor = Color(0xFF4062D3),
            suggestionBgColor = Color(0xFFEFECF7),
            suggestionTextColor = Color(0xFF1B1B22),
            popupBackgroundColor = Color(0xFFE1E0F7),
            popupTextColor = Color(0xFF4062D3),
            keyHintColor = Color(0xFF1A1B26).copy(alpha = 0.35f)
        )

        val DeepSeaDark = KeyboardTheme(
            preset = ThemePreset.DEEP_SEA_DARK,
            backgroundColor = Color(0xFF12132D),
            keyBackgroundColor = Color(0xFF1F1F26),
            keySpecialColor = Color(0xFF292931),
            keyTextColor = Color(0xFFE3E1F0),
            keySpecialTextColor = Color(0xFFBEC2FF),
            accentColor = Color(0xFFBEC2FF),
            suggestionBgColor = Color(0xFF0D0E15),
            suggestionTextColor = Color(0xFFE3E1EC),
            popupBackgroundColor = Color(0xFF292931),
            popupTextColor = Color(0xFFBEC2FF),
            keyHintColor = Color(0xFFE3E1F0).copy(alpha = 0.4f)
        )

        val ClassicMaterialLight = KeyboardTheme(
            preset = ThemePreset.CLASSIC_MATERIAL_LIGHT,
            backgroundColor = Color(0xFFE4E7E9),
            keyBackgroundColor = Color(0xFFECEFF1),
            keySpecialColor = Color(0xFFDAE5E2),
            keyTextColor = Color(0xFF191C1C),
            keySpecialTextColor = Color(0xFF4DB6AC),
            accentColor = Color(0xFF4DB6AC),
            suggestionBgColor = Color(0xFFF0F2F4),
            suggestionTextColor = Color(0xFF191C1C),
            popupBackgroundColor = Color(0xFFDAE5E2),
            popupTextColor = Color(0xFF006A63),
            keyHintColor = Color(0xFF191C1C).copy(alpha = 0.35f)
        )

        val ClassicMaterialDark = KeyboardTheme(
            preset = ThemePreset.CLASSIC_MATERIAL_DARK,
            backgroundColor = Color(0xFF21272B),
            keyBackgroundColor = Color(0xFF263238),
            keySpecialColor = Color(0xFF3F4947),
            keyTextColor = Color(0xFFFFFFFF),
            keySpecialTextColor = Color(0xFF80CBC4),
            accentColor = Color(0xFF80CBC4),
            suggestionBgColor = Color(0xFF1A1F22),
            suggestionTextColor = Color(0xFFFFFFFF),
            popupBackgroundColor = Color(0xFF3F4947),
            popupTextColor = Color(0xFF80CBC4),
            keyHintColor = Color(0xFFFFFFFF).copy(alpha = 0.4f)
        )

        // Orbitt's default "Nothing Theme" (VoiceInputTheme) palette
        val NothingTheme = KeyboardTheme(
            preset = ThemePreset.NOTHING_THEME,
            backgroundColor = Color(0xFF000000),
            keyBackgroundColor = Color(0xFF0E0E11),
            keySpecialColor = Color(0xFF1B1B1F),
            keyTextColor = Color(0xFFFFFFFF),
            keySpecialTextColor = Color(0xFFE5252A),
            accentColor = Color(0xFFE5252A),
            suggestionBgColor = Color(0xFF0E0E11),
            suggestionTextColor = Color(0xFFFFFFFF),
            popupBackgroundColor = Color(0xFF1B1B1F),
            popupTextColor = Color(0xFFE5252A),
            keyHintColor = Color(0xFFFFFFFF).copy(alpha = 0.4f)
        )

        private fun isNightMode(context: Context): Boolean {
            return (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
        }

        @androidx.annotation.RequiresApi(Build.VERSION_CODES.S)
        private fun dynamicMaterialYouDark(context: Context): KeyboardTheme {
            fun c(resId: Int) = Color(context.getColor(resId))
            return KeyboardTheme(
                preset = ThemePreset.MATERIAL_YOU,
                backgroundColor = c(android.R.color.system_neutral1_900),
                keyBackgroundColor = c(android.R.color.system_neutral2_800),
                keySpecialColor = c(android.R.color.system_neutral2_700),
                keyTextColor = c(android.R.color.system_neutral1_50),
                keySpecialTextColor = c(android.R.color.system_accent1_200),
                accentColor = c(android.R.color.system_accent1_300),
                suggestionBgColor = c(android.R.color.system_neutral1_1000),
                suggestionTextColor = c(android.R.color.system_neutral1_50),
                popupBackgroundColor = c(android.R.color.system_neutral2_700),
                popupTextColor = c(android.R.color.system_accent1_200),
                keyHintColor = c(android.R.color.system_neutral1_50).copy(alpha = 0.4f)
            )
        }

        @androidx.annotation.RequiresApi(Build.VERSION_CODES.S)
        private fun dynamicMaterialYouLight(context: Context): KeyboardTheme {
            fun c(resId: Int) = Color(context.getColor(resId))
            return KeyboardTheme(
                preset = ThemePreset.MATERIAL_YOU,
                backgroundColor = c(android.R.color.system_neutral1_50),
                keyBackgroundColor = c(android.R.color.system_neutral2_100),
                keySpecialColor = c(android.R.color.system_neutral2_200),
                keyTextColor = c(android.R.color.system_neutral1_900),
                keySpecialTextColor = c(android.R.color.system_accent1_600),
                accentColor = c(android.R.color.system_accent1_500),
                suggestionBgColor = c(android.R.color.system_neutral1_100),
                suggestionTextColor = c(android.R.color.system_neutral1_900),
                popupBackgroundColor = c(android.R.color.system_neutral2_200),
                popupTextColor = c(android.R.color.system_accent1_600),
                keyHintColor = c(android.R.color.system_neutral1_900).copy(alpha = 0.35f)
            )
        }

        fun materialYou(context: Context): KeyboardTheme {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                return if (isNightMode(context)) MaterialYouDark else MaterialYouLight
            }
            return if (isNightMode(context)) dynamicMaterialYouDark(context) else dynamicMaterialYouLight(context)
        }

        // Orbitt "Dynamic Nothing": follows system day/night between Nothing Light and Nothing Dark
        fun nothingDynamic(context: Context): KeyboardTheme {
            val base = if (isNightMode(context)) NothingDark else NothingLight
            return base.copy(preset = ThemePreset.DYNAMIC_NOTHING)
        }

        // Orbitt "Dynamic Dark": Material You palette forced to dark
        fun materialYouDark(context: Context): KeyboardTheme {
            val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicMaterialYouDark(context) else MaterialYouDark
            return base.copy(preset = ThemePreset.DYNAMIC_DARK)
        }

        // Orbitt "Dynamic Light": Material You palette forced to light
        fun materialYouLight(context: Context): KeyboardTheme {
            val base = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) dynamicMaterialYouLight(context) else MaterialYouLight
            return base.copy(preset = ThemePreset.DYNAMIC_LIGHT)
        }

        fun fromPreset(preset: ThemePreset, context: Context? = null): KeyboardTheme {
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
                ThemePreset.MATERIAL_YOU -> context?.let { materialYou(it) } ?: MaterialYouDark
                ThemePreset.NOTHING_DARK -> NothingDark
                ThemePreset.NOTHING_LIGHT -> NothingLight
                ThemePreset.AMOLED_NOTHING -> AmoledNothing
                ThemePreset.AMOLED_DARK_PURPLE -> AmoledDarkPurple
                ThemePreset.ORBIT -> Orbit
                ThemePreset.SUNFLOWER -> Sunflower
                ThemePreset.SNOWFALL -> Snowfall
                ThemePreset.STEEL_GRAY -> SteelGray
                ThemePreset.COTTON_CANDY -> CottonCandy
                ThemePreset.DEEP_SEA_LIGHT -> DeepSeaLight
                ThemePreset.DEEP_SEA_DARK -> DeepSeaDark
                ThemePreset.CLASSIC_MATERIAL_LIGHT -> ClassicMaterialLight
                ThemePreset.CLASSIC_MATERIAL_DARK -> ClassicMaterialDark
                ThemePreset.NOTHING_THEME -> NothingTheme
                ThemePreset.DYNAMIC_NOTHING -> context?.let { nothingDynamic(it) } ?: NothingDark
                ThemePreset.DYNAMIC_DARK -> context?.let { materialYouDark(it) } ?: MaterialYouDark
                ThemePreset.DYNAMIC_LIGHT -> context?.let { materialYouLight(it) } ?: MaterialYouLight
                ThemePreset.CUSTOM -> DarkNeon // Default for custom if not set
            }
        }

        fun allThemes(context: Context? = null): List<KeyboardTheme> {
            return ThemePreset.values()
                .filter { it != ThemePreset.CUSTOM }
                .map { fromPreset(it, context) }
        }
    }
}
