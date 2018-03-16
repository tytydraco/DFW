package com.draco.dfw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class Exec : BroadcastReceiver() {

    /*
    am broadcast -a <primary> --es <secondary> "<value>" ... -n com.draco.dfw/.Exec
    am broadcast -a wm --es size "1080x2220" --es density "480" --es overscan "50,50,50,50" -n com.draco.dfw/.Exec
     */

    private val _wm = wm()

    override fun onReceive(context: Context, intent: Intent) {

        // check permissions and deliver warning
        val ungrantedPerms = Permissions.check(context, context.packageManager)
        if (ungrantedPerms.isNotEmpty()) {
            Toast.makeText(context, "[DFW] Permissions missing: " + ungrantedPerms.toString(), Toast.LENGTH_LONG).show()
            resultCode = RESULT_ERROR
            resultData = "MISSING PERMISSIONS: " + ungrantedPerms.toString()
            return
        }

        // prevent crashes when intent is sent without command
        if  (intent.extras == null) {
            resultCode = RESULT_ERROR
            return
        }

        // grab all possible parameters
        val extraDensity = intent.extras.getString("density")
        val extraSize = intent.extras.getString("size")
        val extraOverscan = intent.extras.getString("overscan")

        Log.d("onReceive", "Intent received")

        // check primary function
        if (intent.action == "wm") {
            Log.d("WM", "WM sent")
            if (extraDensity != null) {
                _wm.density(extraDensity)
                resultCode = RESULT_OK
            }

            if (extraSize != null) {
                _wm.size(extraSize)
                resultCode = RESULT_OK
            }

            if (extraOverscan != null) {
                _wm.overscan(extraOverscan)
                resultCode = RESULT_OK
            }
        }

    }
}