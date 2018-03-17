package com.draco.dfw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.draco.dfw.commands.immersive
import com.draco.dfw.commands.settings

class Exec : BroadcastReceiver() {

    /*
    am broadcast -a <primary> --es <secondary> "<value>" ... -n com.draco.dfw/.Exec
    am broadcast -a wm --es size "1080x2220" --es density "480" --es overscan "50,50,50,50" -n com.draco.dfw/.Exec
     */

    companion object {
        var permissionGranted: Boolean? = null
    }

    private lateinit var _wm: wm
    private lateinit var _immersive: immersive
    private lateinit var _settings: settings

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BroadcastReceived", intent.action)

        val result = goAsync()
        val asyncTask = object : AsyncTask<Unit, Unit, Unit>() {
            override fun doInBackground(vararg params: Unit?) {
                // TODO: add a permission verification method
                val i = Intent(context, PermissionDialog::class.java).apply {
                    if (intent.`package` == null) {
                        putExtra("message", "Shell has requested runtime.")
                    } else {
                        putExtra("message", "${intent.`package`} has requested runtime.")
                    }
                }

                context.startActivity(i)

                // TODO fix this garbage to not lock up the main thread
                while (permissionGranted == null) { }
                if (permissionGranted == false) return
                Log.d("PERMISSION", "GRANTED")
                permissionGranted = null

                // set primary command classes
                _immersive = immersive(context.contentResolver)
                _wm = wm()
                _settings = settings(context.contentResolver)

                // check permissions and deliver warning
                val ungrantedPerms = Permissions.check(context, context.packageManager)
                if (ungrantedPerms.isNotEmpty()) {
                    Toast.makeText(context, "[DFW] Permissions missing: " + ungrantedPerms.toString(), Toast.LENGTH_LONG).show()
                    Log.d("PermissionsMissing", "[DFW] Permissions missing: " + ungrantedPerms.toString())
                    result.resultCode = RESULT_ERROR
                    result.resultData = "MISSING PERMISSIONS: " + ungrantedPerms.toString()
                    return
                }

                // show command help
                if  (intent.extras == null) {
                    result.resultData = when (intent.action) {
                        "wm" -> _wm.help()
                        "immersive" -> _immersive.help()
                        "settings" -> _settings.help()
                        else -> ""
                    }

                    result.resultCode = RESULT_NEUTRAL
                    return
                }

                // grab all possible parameters
                val extraType = intent.extras.getString(EXTRA_TYPE)
                val extraDensity = intent.extras.getString(EXTRA_DENSITY)
                val extraSize = intent.extras.getString(EXTRA_SIZE)
                val extraOverscan = intent.extras.getString(EXTRA_OVERSCAN)
                val extraNamespace = intent.extras.getString(EXTRA_NAMESPACE)
                val extraPut = intent.extras.getString(EXTRA_PUT)
                val extraGet = intent.extras.getString(EXTRA_GET)
                val extraValue = intent.extras.getString(EXTRA_VALUE)

                // check primary function
                if (intent.action == "wm") {
                    if (extraDensity != null) {
                        _wm.density(extraDensity)
                        result.resultCode = RESULT_OK
                    }

                    if (extraSize != null) {
                        _wm.size(extraSize)
                        result.resultCode = RESULT_OK
                    }

                    if (extraOverscan != null) {
                        _wm.overscan(extraOverscan)
                        result.resultCode = RESULT_OK
                    }
                }

                if (intent.action == "immersive") {
                    when (extraType) {
                        "default" -> _immersive.set(IMMERSIVE_DEFAULT)
                        "none" -> _immersive.set(IMMERSIVE_NONE)
                        "nav" -> _immersive.set(IMMERSIVE_NAV)
                        "status" -> _immersive.set(IMMERSIVE_STATUS)
                        "full" -> _immersive.set(IMMERSIVE_FULL)
                    }
                }

                if (intent.action == "settings") {
                    if (extraNamespace != null) {
                        _settings.namespace(extraNamespace)
                    }

                    if (extraPut != null && extraValue != null) {
                        _settings.put(extraPut, extraValue)
                        result.resultCode = RESULT_OK
                    } else if (extraGet != null) {
                        result.resultData = _settings.get(extraGet)
                        result.resultCode = RESULT_OK
                    }
                }

                result.finish()
            }
        }

        asyncTask.execute()
    }
}