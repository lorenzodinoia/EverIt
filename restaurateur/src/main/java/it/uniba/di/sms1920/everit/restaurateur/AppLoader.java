package it.uniba.di.sms1920.everit.restaurateur;

import android.content.Context;
import android.util.Log;

import com.here.android.mpa.common.ApplicationContext;
import com.here.android.mpa.common.MapEngine;
import com.here.android.mpa.common.MapSettings;
import com.here.android.mpa.common.OnEngineInitListener;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.io.File;

import it.uniba.di.sms1920.everit.utils.Constants;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class AppLoader {
    private static boolean componentsLoaded = false;

    public static void loadComponents(Context context) throws Exception {
        AndroidThreeTen.init(context);
        Providers.init(context, Constants.Variants.RESTAURATEUR);
        RestaurateurNotificationService.initNotificationChannel(context);
        String hereCacheDir = (context.getCacheDir().getPath() + File.separator + ".here-cache");
        MapSettings.setIsolatedDiskCacheRootPath(hereCacheDir); //HERE SDK cache path setup
        MapEngine mapEngine = MapEngine.getInstance(); //HERE SDK MapEngine initialization
        ApplicationContext applicationContext = new ApplicationContext(context);
        mapEngine.init(applicationContext, error -> {
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
