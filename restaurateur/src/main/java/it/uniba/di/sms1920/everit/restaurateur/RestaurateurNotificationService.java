package it.uniba.di.sms1920.everit.restaurateur;

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

import it.uniba.di.sms1920.everit.utils.NotificationServiceUtility;
import it.uniba.di.sms1920.everit.utils.provider.FirebaseTokenProvider;
import it.uniba.di.sms1920.everit.utils.provider.Providers;

public class RestaurateurNotificationService extends FirebaseMessagingService {
    private static int notificationCounter = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        RemoteMessage.Notification remoteNotification = remoteMessage.getNotification();
        if (remoteNotification != null) {
            String title = remoteNotification.getTitle();
            String message = remoteNotification.getBody();
            String clickAction = remoteNotification.getClickAction();
            Map<String, String> data = remoteMessage.getData();
            PendingIntent pendingIntent = null;

            if ((clickAction != null) && (clickAction.length() > 0)) {
                Intent clickIntent = new Intent();
                clickIntent.setAction(clickAction);
                if (data.size() > 0) {
                    Bundle bundle = NotificationServiceUtility.notificationPayloadToBundle(data);
                    clickIntent.putExtras(bundle);
                }
                if (clickIntent.resolveActivity(this.getPackageManager()) != null) {
                    //TODO Vedi come cazzo risolvere
                    /*
                    Intent launcherIntent = new Intent(this.getApplicationContext(), LauncherActivity.class);
                    launcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    launcherIntent.putExtra(LauncherActivity.FLAG_ONLY_INITIALIZATIONS, true);

                    TaskStackBuilder intentSequence = TaskStackBuilder.create(getApplicationContext());
                    intentSequence.addNextIntent(new Intent(this.getApplicationContext(), BaseActivity.class));
                    intentSequence.addNextIntent(clickIntent);
                    intentSequence.addNextIntent(launcherIntent);
                    pendingIntent = intentSequence.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

                     */
                }
            }

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, this.getApplicationContext().getString(R.string.notification_channel_id))
                    .setSmallIcon(it.uniba.di.sms1920.everit.utils.R.mipmap.icon_round)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
            if (pendingIntent != null) {
                notificationBuilder.setContentIntent(pendingIntent);
            }

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(++notificationCounter, notificationBuilder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        FirebaseTokenProvider firebaseTokenProvider = Providers.getFirebaseTokenProvider();

        if (firebaseTokenProvider != null) {
            firebaseTokenProvider.setFirebaseToken(s);
        }
    }

    public static void initNotificationChannel(Context applicationContext) {
        final String channelId = applicationContext.getString(R.string.notification_channel_id);
        final String channelName = applicationContext.getString(R.string.notification_channel_name);
        NotificationServiceUtility.initNotificationChannel(applicationContext, channelId, channelName);
    }
}
