package com.draco.dfw

import android.annotation.SuppressLint
import android.os.Build
import android.view.Display
import java.util.*

/**
 * Created by Draco on 3/15/2018.
 */

class wm {
    fun size(s: String) {
        wmPrivate.wmSize(s)
    }

    fun density(d: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            wmPrivate.wmDensityNew(d)
        } else {
            wmPrivate.wmDensity(d)
        }
    }

    fun overscan(o: String) {
        wmPrivate.wmOverscan(o)
    }
}

class wmPrivate {
    // static access
    companion object {
        @SuppressLint("PrivateApi")
        @Throws(Exception::class)
        fun getWindowManagerService(): Any {
            return Class.forName("android.view.WindowManagerGlobal")
                    .getMethod("getWindowManagerService")
                    .invoke(null)
        }

        @SuppressLint("PrivateApi")
        @Throws(Exception::class)
        fun wmSize(commandArg: String) {
            if (commandArg == "reset") {
                Class.forName("android.view.IWindowManager")
                        .getMethod("clearForcedDisplaySize", Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY)
            } else {
                val scanner = Scanner(commandArg)
                scanner.useDelimiter("x")

                val width = scanner.nextInt()
                val height = scanner.nextInt()

                scanner.close()

                Class.forName("android.view.IWindowManager")
                        .getMethod("setForcedDisplaySize", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY, width, height)
            }
        }

        @SuppressLint("PrivateApi")
        @Throws(Exception::class)
        fun wmDensity(commandArg: String) {
            if (commandArg == "reset") {
                Class.forName("android.view.IWindowManager")
                        .getMethod("clearForcedDisplayDensity", Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY)
            } else {
                val density = Integer.parseInt(commandArg)

                Class.forName("android.view.IWindowManager")
                        .getMethod("setForcedDisplayDensity", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY, density)
            }
        }

        @SuppressLint("PrivateApi")
        @Throws(Exception::class)
        fun wmOverscan(commandArg: String) {
            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            if (commandArg == "reset") {
                bottom = 0
                right = bottom
                top = right
                left = top
            } else {
                val scanner = Scanner(commandArg)
                scanner.useDelimiter(",")

                left = scanner.nextInt()
                top = scanner.nextInt()
                right = scanner.nextInt()
                bottom = scanner.nextInt()

                scanner.close()
            }

            Class.forName("android.view.IWindowManager")
                    .getMethod("setOverscan", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                    .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY, left, top, right, bottom)
        }

        @SuppressLint("PrivateApi")
        @Throws(Exception::class)
        fun wmDensityNew(commandArg: String) {
            // From android.os.UserHandle
            val USER_CURRENT_OR_SELF = -3

            if (commandArg == "reset") {
                Class.forName("android.view.IWindowManager")
                        .getMethod("clearForcedDisplayDensityForUser", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY, USER_CURRENT_OR_SELF)
            } else {
                val density = Integer.parseInt(commandArg)

                Class.forName("android.view.IWindowManager")
                        .getMethod("setForcedDisplayDensityForUser", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                        .invoke(getWindowManagerService(), Display.DEFAULT_DISPLAY, density, USER_CURRENT_OR_SELF)
            }
        }
    }
}