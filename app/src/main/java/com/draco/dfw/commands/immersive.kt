package com.draco.dfw.commands

import android.content.ContentResolver
import android.provider.Settings

class immersive(contentResolver: ContentResolver) {

    private val cr: ContentResolver = contentResolver

    fun set(type: String) {
        Settings.Global.putString(cr, "policy_control", type)
    }

    fun help(): String {
        return "HELP:\n" +
                "set \"<TYPE>\"\n" +
                "TYPE:\n" +
                "\"default\": resets immersive preference\n" +
                "\"none\": nav and status visible\n" +
                "\"nav\": hides nav\n" +
                "\"status\": hides status bar\n" +
                "\"full\": hides nav and status bar"
    }
}