package com.example.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MeterThemePreset {
    CALCULATOR,
    NEON_CYBER,
    RETRO_LCD,
    MINIMAL_DARK,
    GHOST_WHITE,
    CYBER_LIME,
    AMBER_RETRO,
    VIOLET_GLOW
}

data class MeterTheme(
    val preset: MeterThemePreset,
    val backgroundColor: Color,
    val textColor: Color,
    val borderColor: Color,
    val labelColor: Color,
    val borderWidth: Dp = 1.dp,
    val backgroundAlpha: Float = 0.8f,
    val useMonospace: Boolean = true,
    val letterSpacing: androidx.compose.ui.unit.TextUnit = 0.sp,
    val showLcdShadow: Boolean = false,
    val glowRadius: Float = 0f
) {
    companion object {
        val Calculator = MeterTheme(
            preset = MeterThemePreset.CALCULATOR,
            backgroundColor = Color(0xFF1B1B1B),
            textColor = Color(0xFF00FF41),
            borderColor = Color(0xFF333333),
            labelColor = Color(0xFF00FF41).copy(alpha = 0.6f),
            showLcdShadow = true,
            letterSpacing = 1.sp
        )

        val CyberLime = MeterTheme(
            preset = MeterThemePreset.CYBER_LIME,
            backgroundColor = Color(0xFF000000),
            textColor = Color(0xFFCCFF00),
            borderColor = Color(0xFFCCFF00).copy(alpha = 0.3f),
            labelColor = Color(0xFFCCFF00),
            glowRadius = 8f,
            letterSpacing = 2.sp
        )

        val AmberRetro = MeterTheme(
            preset = MeterThemePreset.AMBER_RETRO,
            backgroundColor = Color(0xFF1A120B),
            textColor = Color(0xFFFFB000),
            borderColor = Color(0xFFFFB000).copy(alpha = 0.4f),
            labelColor = Color(0xFFFFB000),
            showLcdShadow = true,
            letterSpacing = 1.5.sp
        )

        val VioletGlow = MeterTheme(
            preset = MeterThemePreset.VIOLET_GLOW,
            backgroundColor = Color(0xFF0F001A),
            textColor = Color(0xFFBB86FC),
            borderColor = Color(0xFFBB86FC).copy(alpha = 0.5f),
            labelColor = Color(0xFF03DAC6),
            glowRadius = 12f
        )

        val NeonCyber = MeterTheme(
            preset = MeterThemePreset.NEON_CYBER,
            backgroundColor = Color(0xFF000000),
            textColor = Color(0xFFFF00A0),
            borderColor = Color(0xFFFF00A0).copy(alpha = 0.5f),
            labelColor = Color(0xFF00FF9F)
        )

        val RetroLcd = MeterTheme(
            preset = MeterThemePreset.RETRO_LCD,
            backgroundColor = Color(0xFF8B956D),
            textColor = Color(0xFF0F380F),
            borderColor = Color(0xFF0F380F).copy(alpha = 0.3f),
            labelColor = Color(0xFF0F380F).copy(alpha = 0.7f),
            borderWidth = 1.5.dp
        )

        val MinimalDark = MeterTheme(
            preset = MeterThemePreset.MINIMAL_DARK,
            backgroundColor = Color(0xFF121212),
            textColor = Color(0xFFE0E0E0),
            borderColor = Color(0xFFBB86FC),
            labelColor = Color(0xFFBB86FC).copy(alpha = 0.8f)
        )

        val GhostWhite = MeterTheme(
            preset = MeterThemePreset.GHOST_WHITE,
            backgroundColor = Color(0xFFF5F5F5),
            textColor = Color(0xFF1B1B1B),
            borderColor = Color(0xFFE0E0E0),
            labelColor = Color(0xFF757575),
            backgroundAlpha = 0.9f
        )

        fun fromPreset(preset: MeterThemePreset): MeterTheme {
            return when (preset) {
                MeterThemePreset.CALCULATOR -> Calculator
                MeterThemePreset.NEON_CYBER -> NeonCyber
                MeterThemePreset.RETRO_LCD -> RetroLcd
                MeterThemePreset.MINIMAL_DARK -> MinimalDark
                MeterThemePreset.GHOST_WHITE -> GhostWhite
                MeterThemePreset.CYBER_LIME -> CyberLime
                MeterThemePreset.AMBER_RETRO -> AmberRetro
                MeterThemePreset.VIOLET_GLOW -> VioletGlow
            }
        }

        fun allPresets(): List<MeterTheme> {
            return MeterThemePreset.values().map { fromPreset(it) }
        }
    }
}
