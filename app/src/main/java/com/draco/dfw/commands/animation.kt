package com.draco.dfw.commands

import android.content.ContentResolver
import android.provider.Settings

class animation(contentResolver: ContentResolver) {

    private val cr: ContentResolver = contentResolver
    private lateinit var currentType: String

    fun type(t: String) {
        currentType = t
    }

    fun window(v: Float) {
        Settings.Global.putFloat(cr, Settings.Global.WINDOW_ANIMATION_SCALE, v)
    }

    fun transition(v: Float) {
        Settings.Global.putFloat(cr, Settings.Global.TRANSITION_ANIMATION_SCALE, v)
    }

    fun animator(v: Float) {
        Settings.Global.putFloat(cr, Settings.Global.ANIMATOR_DURATION_SCALE, v)
    }

    fun help(): String {
        return "HELP:\n" +
                "type \"<TYPE>\"\n" +
                "value \"<VALUE>\"\n" +
                "TYPE:\n" +
                "\"window\"\n" +
                "\"transition\"\n" +
                "\"animator\""
    }
}