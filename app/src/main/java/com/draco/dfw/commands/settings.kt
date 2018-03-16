package com.draco.dfw.commands

import android.content.ContentResolver
import android.provider.Settings
import com.draco.dfw.NAMESPACE_GLOBAL
import com.draco.dfw.NAMESPACE_SECURE
import com.draco.dfw.NAMESPACE_SYSTEM

class settings(contentResolver: ContentResolver) {

    private val cr: ContentResolver = contentResolver
    private lateinit var currentNamespace: String
    private lateinit var setValue: String

    fun namespace(c: String) {
        currentNamespace = c
    }

    fun value(v: String) {
        setValue = v
    }

    fun put(name: String, value: String) {
        when (currentNamespace) {
            NAMESPACE_GLOBAL -> Settings.Global.putString(cr, name, value)
            NAMESPACE_SYSTEM -> Settings.System.putString(cr, name, value)
            NAMESPACE_SECURE -> Settings.Secure.putString(cr, name, value)
        }
    }

    fun get(name: String): String? {
        when (currentNamespace) {
            NAMESPACE_GLOBAL -> return Settings.Global.getString(cr, name)
            NAMESPACE_SYSTEM -> return Settings.System.getString(cr, name)
            NAMESPACE_SECURE -> return Settings.Secure.getString(cr, name)
        }
        return null
    }

    fun help(): String {
        return "HELP:\n" +
            "namespace \"<NAMESPACE>\"\n" +
            "put \"<NAME>\"\n" +
            "get \"<NAME>\"\n" +
            "NAMESPACE:\n" +
            "\"global\"\n" +
            "\"system\"\n" +
            "\"secure\""
    }
}