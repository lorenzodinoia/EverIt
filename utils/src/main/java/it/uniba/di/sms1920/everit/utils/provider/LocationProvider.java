package it.uniba.di.sms1920.everit.utils.provider;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public final class LocationProvider {
    public enum PermissionStatus {GRANTED, NOT_GRANTED, NOT_GRANTED_SHOW_EXPLANATION}

    public static int REQUEST_PERMISSION_GPS = 1;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static int REQUEST_PERMISSION_BACKGROUND_GPS = 2;
    public static final String PERMISSION_GPS = Manifest.permission.ACCESS_FINE_LOCATION;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static final String PERMISSION_BACKGROUND_GPS = Manifest.permission.ACCESS_BACKGROUND_LOCATION;

    public static PermissionStatus hasPermissions(Activity activity) {
        PermissionStatus status;

        if (activity.checkSelfPermission(PERMISSION_GPS) == PackageManager.PERMISSION_GRANTED) {
            status = PermissionStatus.GRANTED;
        }
        else if (activity.shouldShowRequestPermissionRationale(PERMISSION_GPS)) {
            status = PermissionStatus.NOT_GRANTED_SHOW_EXPLANATION;
        }
        else {
            status = PermissionStatus.NOT_GRANTED;
        }

        return status;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static PermissionStatus hasBackgroundPermissions(Activity activity) {
        PermissionStatus status;

        if (activity.checkSelfPermission(PERMISSION_BACKGROUND_GPS) == PackageManager.PERMISSION_GRANTED) {
            status = PermissionStatus.GRANTED;
        }
        else if (activity.shouldShowRequestPermissionRationale(PERMISSION_BACKGROUND_GPS)) {
            status = PermissionStatus.NOT_GRANTED_SHOW_EXPLANATION;
        }
        else {
            status = PermissionStatus.NOT_GRANTED;
        }

        return status;
    }

    public static PermissionStatus hasPermissions(Fragment fragment) {
        PermissionStatus status;

        if (fragment.getContext() == null) {
            return PermissionStatus.NOT_GRANTED;
        }

        if (fragment.getContext().checkSelfPermission(PERMISSION_GPS) == PackageManager.PERMISSION_GRANTED) {
            status = PermissionStatus.GRANTED;
        }
        else if (fragment.shouldShowRequestPermissionRationale(PERMISSION_GPS)) {
            status = PermissionStatus.NOT_GRANTED_SHOW_EXPLANATION;
        }
        else {
            status = PermissionStatus.NOT_GRANTED;
        }

        return status;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static PermissionStatus hasBackgroundPermissions(Fragment fragment) {
        PermissionStatus status;

        if (fragment.getContext() == null) {
            return PermissionStatus.NOT_GRANTED;
        }

        if (fragment.getContext().checkSelfPermission(PERMISSION_BACKGROUND_GPS) == PackageManager.PERMISSION_GRANTED) {
            status = PermissionStatus.GRANTED;
        }
        else if (fragment.shouldShowRequestPermissionRationale(PERMISSION_BACKGROUND_GPS)) {
            status = PermissionStatus.NOT_GRANTED_SHOW_EXPLANATION;
        }
        else {
            status = PermissionStatus.NOT_GRANTED;
        }

        return status;
    }

    public static void requestPermissions(Activity activity) {
        activity.requestPermissions(new String[] {PERMISSION_GPS}, REQUEST_PERMISSION_GPS);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void requestBackgroundPermissions(Activity activity) {
        activity.requestPermissions(new String[] {PERMISSION_GPS, PERMISSION_BACKGROUND_GPS}, REQUEST_PERMISSION_BACKGROUND_GPS);
    }

    public static void requestPermissions(Fragment fragment) {
        fragment.requestPermissions(new String[] {PERMISSION_GPS}, REQUEST_PERMISSION_GPS);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void requestBackgroundPermissions(Fragment fragment) {
        fragment.requestPermissions(new String[] {PERMISSION_GPS, PERMISSION_BACKGROUND_GPS}, REQUEST_PERMISSION_BACKGROUND_GPS);
    }
}
