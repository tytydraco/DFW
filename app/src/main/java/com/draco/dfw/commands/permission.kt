package com.draco.dfw.commands

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.draco.dfw.Exec
import com.draco.dfw.PermissionDialog
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager



class permission(context: Context) {

    private var prefs: SharedPreferences
    private var editor: SharedPreferences.Editor

    private val cx: Context = context

    init {
        prefs = cx.getSharedPreferences("perms", Context.MODE_PRIVATE)
        editor = prefs.edit()
    }

    fun grant(i: String) {
        if (!prefs.getBoolean(i, false) && packageExists(i)) permissionDialog(i)
    }

    fun revoke(i: String) {
        editor.putBoolean(i, false)
        editor.apply()
    }

    fun status(i: String): Boolean {
        return prefs.getBoolean(i, false)
    }

    private fun packageExists(targetPackage: String): Boolean {
        val packages: List<ApplicationInfo>
        val pm = cx.packageManager
        packages = pm.getInstalledApplications(0)
        return packages.any { it.packageName == targetPackage }
    }

    private fun permissionDialog(pname: String) {
        val i = Intent(cx, PermissionDialog::class.java).apply {
            putExtra("message", "$pname has requested runtime.")
        }
        cx.startActivity(i)

        while (Exec.permissionGranted == null) { }
        if (Exec.permissionGranted == false) return
        Exec.permissionGranted = null
        editor.putBoolean(pname, true)
        editor.apply()
    }

    fun help(): String {
        return "HELP:\n" +
                "grant \"<ID>\"\n" +
                "revoke \"<ID>\""
    }
}