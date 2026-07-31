package com.example.ime

import com.example.ui.KeyboardMode

internal fun isBanglaMode(mode: KeyboardMode): Boolean =
    mode == KeyboardMode.BANGLA_PHONETIC || mode == KeyboardMode.AVRO || mode == KeyboardMode.BANGLA_JATIYO

internal fun parseComposing(mode: KeyboardMode, raw: String): String = when (mode) {
    KeyboardMode.BANGLA_PHONETIC -> BanglaPhoneticMode.parse(raw)
    KeyboardMode.AVRO -> AvroMode.parse(raw)
    else -> raw
}

internal fun shouldCompose(mode: KeyboardMode, isPasswordField: Boolean, isAlphaKey: Boolean): Boolean = when (mode) {
    KeyboardMode.BANGLA_PHONETIC -> BanglaPhoneticMode.shouldCompose(isPasswordField, isAlphaKey)
    KeyboardMode.AVRO -> AvroMode.shouldCompose(isPasswordField, isAlphaKey)
    KeyboardMode.ENGLISH -> EnglishMode.shouldCompose(isPasswordField, isAlphaKey)
    KeyboardMode.ARABIC -> ArabicMode.shouldCompose(isPasswordField, isAlphaKey)
    KeyboardMode.BANGLA_JATIYO -> BanglaJatiyoMode.shouldCompose(isPasswordField, isAlphaKey)
    else -> false
}
