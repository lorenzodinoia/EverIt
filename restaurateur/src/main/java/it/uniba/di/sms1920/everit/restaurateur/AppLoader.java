package it.uniba.di.sms1920.everit.restaurateur;

import android.content.Context;
import android.util.Log;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.OnEngineInitListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class AppLoader {
    private static boolean componentsLoaded = false;

    public static void loadComponents(Context applicationContext) throws Exception {
        AndroidThreeTen.init(applicationContext);
        Providers.init(applicationContext, Constants.Variants.RESTAURATEUR);
        RestaurateurNotificationService.initNotificationChannel(applicationContext);
        MapEngine mapEngine = MapEngine.getInstance(); //HERE SDK MapEngine initialization
        ApplicationContext appContext = new ApplicationContext(applicationContext);
        mapEngine.init(appContext, error -> {
            if (error != OnEngineInitListener.Error.NONE) {
                Log.e("HERE-SDK", error.name());
            }
        });
        AppLoader.componentsLoaded = true;
    }

    public static boolean isComponentsLoaded() {
        return componentsLoaded;
    }
}
