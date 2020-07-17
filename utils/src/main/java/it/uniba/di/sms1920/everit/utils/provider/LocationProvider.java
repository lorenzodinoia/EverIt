package it.uniba.di.sms1920.everit.utils.provider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import it.uniba.di.sms1920.everit.utils.R;

public final class LocationProvider {
    public static int REQUEST_PERMISSION_GPS = 1;
    private static final String PERMISSION_GPS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String PERMISSION_BACKGROUND_GPS = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    private static final String[] REQUIRED_PERMISSIONS = new String[] {Manifest.permission.ACCESS_FINE_LOCATION};
    private static final String[] REQUIRED_PERMISSIONS_BACKGROUND = new String[] {PERMISSION_GPS, PERMISSION_BACKGROUND_GPS};

    public static boolean hasPermissions(Context context) {
        return (ContextCompat.checkSelfPermission(context, PERMISSION_GPS) == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermissions(Activity activity, boolean withBackgroundPermission) {
        requestPermissions(activity, withBackgroundPermission, false);
    }

    public static void requestPermissions(Activity activity, boolean withBackgroundPermission, boolean ignoreExplanation) {
        if ((!ignoreExplanation) && (ActivityCompat.shouldShowRequestPermissionRationale(activity, PERMISSION_GPS))) {
            showExplanationMessage(activity);
        }
        else {
            String[] permissions = (withBackgroundPermission) ? REQUIRED_PERMISSIONS_BACKGROUND : REQUIRED_PERMISSIONS;
            activity.requestPermissions(permissions, REQUEST_PERMISSION_GPS);
        }
    }

    public static void requestPermissions(Fragment fragment, boolean withBackgroundPermission) {
        requestPermissions(fragment, withBackgroundPermission, false);
    }

    public static void requestPermissions(Fragment fragment, boolean withBackgroundPermission, boolean ignoreExplanation) {
        if ((!ignoreExplanation) && (fragment.shouldShowRequestPermissionRationale(PERMISSION_GPS))) {
            showExplanationMessage(fragment);
        }
        else {
            String[] permissions = (withBackgroundPermission) ? REQUIRED_PERMISSIONS_BACKGROUND : REQUIRED_PERMISSIONS;
            fragment.requestPermissions(permissions, REQUEST_PERMISSION_GPS);
        }
    }

    private static void showExplanationMessage(Activity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.gps_permission_explanation))
                .setPositiveButton(activity.getString(R.string.ok_default), (dialog, which) -> requestPermissions(activity, false, true))
                .setNegativeButton(activity.getString(R.string.cancel_default), (dialog, which) -> {
                    dialog.dismiss();
                    if (activity instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
                        activity.onRequestPermissionsResult(REQUEST_PERMISSION_GPS, new String[] {}, new int[] {PackageManager.PERMISSION_DENIED});
                    }
                })
                .create()
                .show();
    }

    private static void showExplanationMessage(Fragment fragment) {
        Context context = fragment.getContext();
        if (context != null) {
            new AlertDialog.Builder(fragment.getContext())
                    .setMessage(fragment.getString(R.string.gps_permission_explanation))
                    .setPositiveButton(fragment.getString(R.string.ok_default), (dialog, which) -> requestPermissions(fragment, true, true))
                    .setNegativeButton(fragment.getString(R.string.cancel_default), (dialog, which) -> {
                        dialog.dismiss();
                        if (fragment instanceof ActivityCompat.OnRequestPermissionsResultCallback) {
                            fragment.onRequestPermissionsResult(REQUEST_PERMISSION_GPS, new String[] {}, new int[] {PackageManager.PERMISSION_DENIED});
                        }
                    })
                    .create()
                    .show();
        }
    }
}
