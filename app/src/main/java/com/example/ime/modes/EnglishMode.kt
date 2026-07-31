package com.example.ime

internal object EnglishMode {
    fun shouldCompose(isPasswordField: Boolean, isAlphaKey: Boolean): Boolean =
        !isPasswordField && isAlphaKey

    fun parse(raw: String): String = raw
}
