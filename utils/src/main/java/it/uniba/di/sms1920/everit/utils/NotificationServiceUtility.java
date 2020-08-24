package it.uniba.di.sms1920.everit.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import java.util.Map;

public final class NotificationServiceUtility {
    public static void initNotificationChannel(Context applicationContext, String channelId, String channelName) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final int channelLevel = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelLevel);
            NotificationManager notificationManager = applicationContext.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public static Bundle notificationPayloadToBundle(Map<String, String> payload) {
        Bundle bundle = new Bundle();

        for (Map.Entry<String, String> entry : payload.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        return bundle;
    }
}