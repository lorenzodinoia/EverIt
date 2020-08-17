package it.uniba.di.sms1920.everit.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import it.uniba.di.sms1920.everit.utils.provider.FirebaseTokenProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class NotificationService extends FirebaseMessagingService {
    private static int notificationCounter = 0;

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseTokenProvider firebaseTokenProvider = Providers.getFirebaseTokenProvider();

        if (firebaseTokenProvider != null) {
            firebaseTokenProvider.setFirebaseToken(s);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        RemoteMessage.Notification remoteNotification = remoteMessage.getNotification();
        if (remoteNotification != null) {
            String title = remoteNotification.getTitle();
            String message = remoteNotification.getBody();
            String clickAction = remoteNotification.getClickAction();
            Map<String, String> data = remoteMessage.getData();
            PendingIntent pendingIntent = null;
            //TODO Trovare un modo per effettuare le inizializzazioni della LauncherActivity
            if ((clickAction != null) && (clickAction.length() > 0)) {
                Intent clickIntent = new Intent();
                clickIntent.setAction(clickAction);
                clickIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                if (data.size() > 0) {
                    Bundle bundle = NotificationService.dataToBundle(data);
                    clickIntent.putExtras(bundle);
                }
                if (clickIntent.resolveActivity(this.getPackageManager()) != null) {
                    pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_ONE_SHOT);
                }
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, this.getApplicationContext().getString(R.string.firebase_notification_channel))
                    .setSmallIcon(R.mipmap.icon_round)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            if (pendingIntent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(++NotificationService.notificationCounter, notificationBuilder.build());
        }
    }

    public static void initNotificationChannel(Context applicationContext) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            final String channelId = applicationContext.getString(R.string.firebase_notification_channel);
            final String channelName = applicationContext.getString(R.string.firebase_notification_channel_name);
            final int channelLevel = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(channelId, channelName, channelLevel);
            NotificationManager notificationManager = applicationContext.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
               notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private static Bundle dataToBundle(Map<String, String> data) {
        Bundle bundle = new Bundle();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            bundle.putString(entry.getKey(), entry.getValue());
        }

        return bundle;
    }
}