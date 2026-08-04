package com.example.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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
