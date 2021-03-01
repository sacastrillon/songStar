package com.example.woo.songstar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtility {

    public static boolean isPermissionGranted(Activity context, final String[] permission, int permissionReqCode) {
        final List<String> permissionsNeeded = new ArrayList<>();
        for (final String perm : permission) {
            if (perm.startsWith("android.permission")) {
                if (ContextCompat.checkSelfPermission(context,
                        perm)
                        != PackageManager.PERMISSION_GRANTED) {
                    permissionsNeeded.add(perm);
                }
            }
        }
        if (permissionsNeeded.size() > 0) {
            ActivityCompat.requestPermissions(context, permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    permissionReqCode);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPermissionIsGrant(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

}
