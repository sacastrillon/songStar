package com.example.woo.songstar.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class PermissionUtility {

    companion object {
        fun isPermissionGranted(context: Activity?, permission: Array<String>, permissionReqCode: Int): Boolean {
            val permissionsNeeded: MutableList<String> =
                ArrayList()
            for (perm in permission) {
                if (perm.startsWith("android.permission")) {
                    if (ContextCompat.checkSelfPermission(
                            context!!,
                            perm
                        )
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionsNeeded.add(perm)
                    }
                }
            }
            return if (permissionsNeeded.size > 0) {
                ActivityCompat.requestPermissions(
                    context!!, permissionsNeeded.toTypedArray(),
                    permissionReqCode
                )
                false
            } else {
                true
            }
        }

        fun checkPermissionIsGrant(context: Context?, permission: String?): Boolean {
            return (ContextCompat.checkSelfPermission(context!!, permission!!)
                    == PackageManager.PERMISSION_GRANTED)
        }
    }
}