package com.example.ime

import com.example.engine.BanglaPhoneticEngine

internal object BanglaPhoneticMode {
    fun shouldCompose(isPasswordField: Boolean, isAlphaKey: Boolean): Boolean =
        !isPasswordField && isAlphaKey

    fun parse(raw: String): String = BanglaPhoneticEngine.parse(raw)
}
