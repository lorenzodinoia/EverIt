package it.uniba.di.sms1920.everit.restaurateur;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
        Map<String, String> data = remoteMessage.getData();
        String title = "";
        String message = "";
        PendingIntent pendingIntent = null;

        if (data.size() > 0) {
            if (data.containsKey("title")) {
                title = data.get("title");
                data.remove("title");
            }
            if (data.containsKey("message")) {
                message = data.get("message");
                data.remove("message");
            }
            if (data.containsKey("click_action")) {
                String clickAction = data.get("click_action");
                data.remove("click_action");

                Intent clickIntent = new Intent();
                clickIntent.setAction(clickAction);
                if (clickIntent.resolveActivity(this.getPackageManager()) != null) {
                    Bundle bundle = NotificationServiceUtility.notificationPayloadToBundle(data); //put as arguments the remaining keys which represent the actual payload
                    clickIntent.putExtras(bundle);

                    if (AppLoader.isComponentsLoaded()) {
                        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_ONE_SHOT);
                    }
                    else { //Needs initializations by launcher activity
                        Intent launcherActivityIntent = new Intent(this.getApplicationContext(), LauncherActivity.class);
                        launcherActivityIntent.putExtra(LauncherActivity.FLAG_ONLY_INITIALIZATIONS, true); //Says to the launcher activity to handle only initializations
                        Intent baseActivityIntent = new Intent(this.getApplicationContext(), BaseActivity.class); //Base activity are put on the bottom of the stack to prevent the app closes

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.getApplicationContext());
                        stackBuilder.addNextIntent(baseActivityIntent);
                        stackBuilder.addNextIntent(clickIntent);
                        stackBuilder.addNextIntent(launcherActivityIntent);

                        pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
                    }
                }
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, this.getApplicationContext().getString(R.string.notification_channel_id))
                .setSmallIcon(it.uniba.di.sms1920.everit.utils.R.mipmap.ic_restaurateur_round)
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
