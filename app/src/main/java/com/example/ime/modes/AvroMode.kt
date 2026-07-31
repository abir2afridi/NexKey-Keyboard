package com.example.ime

import com.example.engine.AvroPhoneticEngine

internal object AvroMode {
    fun shouldCompose(isPasswordField: Boolean, isAlphaKey: Boolean): Boolean =
        !isPasswordField && isAlphaKey

    fun parse(raw: String): String = AvroPhoneticEngine.parse(raw)
}
