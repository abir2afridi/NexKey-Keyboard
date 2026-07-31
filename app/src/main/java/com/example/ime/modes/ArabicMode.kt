package com.example.ime

internal object ArabicMode {
    fun shouldCompose(isPasswordField: Boolean, isAlphaKey: Boolean): Boolean =
        !isPasswordField && isAlphaKey

    fun parse(raw: String): String = raw
}
