package com.example.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.example.R

val METER_FONT_OPTIONS = listOf("DIGITAL", "LCD", "SEGMENT", "MODERN")

val INFOBOX_FONT_OPTIONS = listOf(
    "DEFAULT", "DIGITAL", "LCD", "SEGMENT", "MODERN", "MONO", "SERIF", "CURSIVE"
)

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
