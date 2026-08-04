package com.example.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

fun meterFontFamily(fontStyle: String): FontFamily {
    return when (fontStyle) {
        "DEFAULT" -> FontFamily.Default
        "MONO" -> FontFamily.Monospace
        "SERIF" -> FontFamily.Serif
        "CURSIVE" -> FontFamily.Cursive
        "LCD" -> FontFamily(Font(R.font.dseg7_classic_light))
        "SEGMENT" -> FontFamily(Font(R.font.dseg14_classic_bold))
        "MODERN" -> FontFamily(Font(R.font.dseg7_modern_regular))
        else -> FontFamily(Font(R.font.dseg7_classic_regular))
    }
}

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

enum class InfoBoxFramePreset {
    CLASSIC,
    NEON_CYBER,
    RETRO_LCD,
    MINIMAL_DARK,
    GHOST_WHITE,
    CYBER_LIME,
    VIOLET_GLOW
}

data class InfoBoxFrame(
    val preset: InfoBoxFramePreset,
    val backgroundColor: Color,
    val borderColor: Color,
    val defaultTextColor: Color,
    val borderWidth: Dp = 1.dp,
    val backgroundAlpha: Float = 0.85f,
    val cornerRadius: Dp = 4.dp,
    val glowRadius: Float = 0f
) {
    companion object {
        val Classic = InfoBoxFrame(
            preset = InfoBoxFramePreset.CLASSIC,
            backgroundColor = Color(0xFF1B1B1B),
            borderColor = Color(0xFF333333),
            defaultTextColor = Color(0xFF00FF41),
            backgroundAlpha = 0.9f
        )

        val NeonCyber = InfoBoxFrame(
            preset = InfoBoxFramePreset.NEON_CYBER,
            backgroundColor = Color(0xFF000000),
            borderColor = Color(0xFFFF00A0).copy(alpha = 0.5f),
            defaultTextColor = Color(0xFF00FF9F),
            glowRadius = 10f
        )

        val RetroLcd = InfoBoxFrame(
            preset = InfoBoxFramePreset.RETRO_LCD,
            backgroundColor = Color(0xFF8B956D),
            borderColor = Color(0xFF0F380F).copy(alpha = 0.3f),
            defaultTextColor = Color(0xFF0F380F),
            borderWidth = 1.5.dp,
            backgroundAlpha = 0.95f
        )

        val MinimalDark = InfoBoxFrame(
            preset = InfoBoxFramePreset.MINIMAL_DARK,
            backgroundColor = Color(0xFF121212),
            borderColor = Color(0xFFBB86FC),
            defaultTextColor = Color(0xFFE0E0E0),
            cornerRadius = 10.dp
        )

        val GhostWhite = InfoBoxFrame(
            preset = InfoBoxFramePreset.GHOST_WHITE,
            backgroundColor = Color(0xFFF5F5F5),
            borderColor = Color(0xFFE0E0E0),
            defaultTextColor = Color(0xFF1B1B1B),
            backgroundAlpha = 0.95f
        )

        val CyberLime = InfoBoxFrame(
            preset = InfoBoxFramePreset.CYBER_LIME,
            backgroundColor = Color(0xFF000000),
            borderColor = Color(0xFFCCFF00).copy(alpha = 0.3f),
            defaultTextColor = Color(0xFFCCFF00),
            glowRadius = 8f
        )

        val VioletGlow = InfoBoxFrame(
            preset = InfoBoxFramePreset.VIOLET_GLOW,
            backgroundColor = Color(0xFF0F001A),
            borderColor = Color(0xFFBB86FC).copy(alpha = 0.5f),
            defaultTextColor = Color(0xFFBB86FC),
            glowRadius = 12f
        )

        fun fromPreset(preset: InfoBoxFramePreset): InfoBoxFrame {
            return when (preset) {
                InfoBoxFramePreset.CLASSIC -> Classic
                InfoBoxFramePreset.NEON_CYBER -> NeonCyber
                InfoBoxFramePreset.RETRO_LCD -> RetroLcd
                InfoBoxFramePreset.MINIMAL_DARK -> MinimalDark
                InfoBoxFramePreset.GHOST_WHITE -> GhostWhite
                InfoBoxFramePreset.CYBER_LIME -> CyberLime
                InfoBoxFramePreset.VIOLET_GLOW -> VioletGlow
            }
        }

        fun allPresets(): List<InfoBoxFrame> {
            return InfoBoxFramePreset.values().map { fromPreset(it) }
        }
    }
}
