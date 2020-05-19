package it.uniba.di.sms1920.everit.utils.provider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import it.uniba.di.sms1920.everit.utils.R;

public final class LocationProvider {
    public static int REQUEST_PERMISSION_GPS = 1;
    private static final String PERMISSION_GPS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String[] REQUIRED_PERMISSIONS = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean hasPermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, PERMISSION_GPS) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissions(Activity activity, boolean ignoreExplanation) {
        if ((!ignoreExplanation) && (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_GPS))) {
            showExplanationMessage(activity);
        }
        else {
            ActivityCompat.requestPermissions(activity, REQUIRED_PERMISSIONS, REQUEST_PERMISSION_GPS);
        }
    }

    private static void showExplanationMessage(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.gps_permission_explanation))
                .setPositiveButton(activity.getString(R.string.ok_default), (dialog, which) -> requestPermissions(activity, true))
                .setNegativeButton(activity.getString(R.string.cancel_default), (dialog, which) -> {
                    dialog.dismiss();
                    activity.onRequestPermissionsResult(REQUEST_PERMISSION_GPS, new String[] {}, new int[] {PackageManager.PERMISSION_DENIED});
                })
                .create()
                .show();
    }
}
