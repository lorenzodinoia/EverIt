package it.uniba.di.sms1920.everit.rider;

import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class AppLoader {
    private static boolean componentsLoaded = false;

    public static void loadComponents(Context applicationContext) throws Exception {
        AndroidThreeTen.init(applicationContext);
        Providers.init(applicationContext, Constants.Variants.RIDER);
        RiderNotificationService.initNotificationChannel(applicationContext);
        AppLoader.componentsLoaded = true;
    }

    public static boolean isComponentsLoaded() {
        return componentsLoaded;
    }
}
