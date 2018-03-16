package com.draco.dfw

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageInfo

// static access
class Permissions {
    companion object {
        fun check(context: Context, pm: PackageManager): List<Any> {
            val grantedPerms = getGrantedPermissions(context, pm)
            val ungrantedPerms = ArrayList<String>()
            try {
                val info = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS)
                if (info.requestedPermissions != null) {
                    for (p in info.requestedPermissions) {
                        if (!grantedPerms.contains(p)) {
                            ungrantedPerms.add(p)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ungrantedPerms
        }

        private fun getGrantedPermissions(context: Context, pm: PackageManager): List<String> {
            val granted = ArrayList<String>()
            try {
                val pi = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                pi.requestedPermissions.indices
                        .filter { pi.requestedPermissionsFlags[it] and PackageInfo.REQUESTED_PERMISSION_GRANTED != 0 }
                        .mapTo(granted) { pi.requestedPermissions[it] }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return granted
        }
    }
}