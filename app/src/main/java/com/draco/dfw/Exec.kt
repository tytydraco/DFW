package com.draco.dfw

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.draco.dfw.commands.animation
import com.draco.dfw.commands.immersive
import com.draco.dfw.commands.permission
import com.draco.dfw.commands.settings
import kotlin.concurrent.thread

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
    private lateinit var _animation: animation
    private lateinit var _permission: permission
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BroadcastReceived", intent.action)

        val result = goAsync()
        val runnable = Runnable {
            prefs = context.getSharedPreferences("perms", Context.MODE_PRIVATE)
            editor = prefs.edit()

            // set primary command classes
            _immersive = immersive(context.contentResolver)
            _wm = wm()
            _settings = settings(context.contentResolver)
            _animation = animation(context.contentResolver)
            _permission = permission(context)

            var pname = intent.`package`
            if (intent.`package` == null) pname = "Shell"
            if (!_permission.status(pname)) {
                resultCode = RESULT_ERROR
                resultData = "RUNTIME PERMISSION NOT GRANTED\n" +
                        "Please see \"permission\" action."
            }
            //_permission.grant(pname)

            // check permissions and deliver warning
            val ungrantedPerms = Permissions.check(context, context.packageManager)
            if (ungrantedPerms.isNotEmpty()) {
                Log.d("PermissionsMissing", "[DFW] Permissions missing: " + ungrantedPerms.toString())
                result.resultCode = RESULT_ERROR
                result.resultData = "MISSING PERMISSIONS: " + ungrantedPerms.toString()
                result.finish()
                return@Runnable
            }

            // show command help
            if  (intent.extras == null) {
                result.resultData = when (intent.action) {
                    "wm" -> _wm.help()
                    "immersive" -> _immersive.help()
                    "settings" -> _settings.help()
                    "animation" -> _animation.help()
                    "permission" -> _permission.help()
                    else -> ""
                }

                result.resultCode = RESULT_NEUTRAL
                result.finish()
                return@Runnable
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
            val extraStatus = intent.extras.getString(EXTRA_STATUS)
            val extraGrant = intent.extras.getString(EXTRA_GRANT)
            val extraRevoke = intent.extras.getString(EXTRA_REVOKE)

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

                if (extraValue == null) {
                    result.finish()
                }

                if (extraPut != null) {
                    _settings.put(extraPut, extraValue)
                    result.resultCode = RESULT_OK
                } else if (extraGet != null) {
                    result.resultData = _settings.get(extraGet)
                    result.resultCode = RESULT_OK
                }
            }

            if (intent.action == "animation") {
                if (extraType != null) {
                    _animation.type(extraType)
                    result.resultCode = RESULT_NEUTRAL
                }

                if (extraValue == null) {
                    result.finish()
                }

                if (extraType == ANIMATION_WINDOW) {
                    _animation.window(extraValue.toFloat())
                    result.resultCode = RESULT_OK
                }

                if (extraType == ANIMATION_TRANSITION) {
                    _animation.transition(extraValue.toFloat())
                    result.resultCode = RESULT_OK
                }

                if (extraType == ANIMATION_ANIMATOR) {
                    _animation.animator(extraValue.toFloat())
                    result.resultCode = RESULT_OK
                }
            }

            if (intent.action == "permission") {
                if (extraGrant != null) {
                    _permission.grant(extraGrant)
                    result.resultCode = RESULT_OK
                }

                if (extraRevoke != null) {
                    _permission.revoke(extraRevoke)
                    result.resultCode = RESULT_OK
                }

                if (extraStatus != null) {
                    _permission.status(extraStatus)
                    result.resultCode = RESULT_OK
                }
            }

            result.finish()
        }
        thread {
            runnable.run()
        }

    }
}