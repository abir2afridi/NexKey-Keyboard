package com.example.ime

internal object BanglaJatiyoMode {
    fun shouldCompose(isPasswordField: Boolean, isAlphaKey: Boolean): Boolean = false

    fun parse(raw: String): String = raw
}
