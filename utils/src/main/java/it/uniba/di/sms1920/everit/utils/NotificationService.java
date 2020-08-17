package it.uniba.di.sms1920.everit.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import it.uniba.di.sms1920.everit.utils.provider.FirebaseTokenProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public final class NotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseTokenProvider firebaseTokenProvider = Providers.getFirebaseTokenProvider();

        if (firebaseTokenProvider != null) {
            firebaseTokenProvider.setFirebaseToken(s);
        }
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, this.getApplicationContext().getString(R.string.firebase_notification_channel))
                .setSmallIcon(R.mipmap.icon_round)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());
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
}
