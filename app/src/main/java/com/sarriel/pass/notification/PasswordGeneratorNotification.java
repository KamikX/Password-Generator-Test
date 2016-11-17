package com.sarriel.pass.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.sarriel.pass.MainActivity;
import com.sarriel.pass.R;

/**
 * Created by matej on 12.8.2016.
 */
public class PasswordGeneratorNotification {

    private static int NOTIFICATION_ID = 1;

    // Where should direct replies be put in the intent bundle (can be any string)
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    /**
     * Display notification with input to generate alias.
     * @param context
     */
    public static void showNotification(Context context) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID,
                generateNotification(context, context.getString(R.string.notification_title)));
    }

    /**
     * Create a notification with an inline input.
     * @param context
     * @param message
     * @return
     */
    private static Notification generateNotification(Context context, String message) {
        RemoteInput remoteInput =
                new RemoteInput.Builder(KEY_TEXT_REPLY)
                        .setLabel(context.getString(R.string.notification_input_alias))
                        .build();

        Intent resultIntent =
                new Intent(context, DirectReplyReceiver.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Action replyAction =
                new NotificationCompat.Action.Builder(
                        android.R.drawable.ic_dialog_info,
                        context.getString(R.string.notification_action), resultPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        // Build the notification and add the action.
        Notification newMessageNotification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(message)
                        .addAction(replyAction)
                        .build();

        return newMessageNotification;
    }


    /**
     * Hide notification.
     * @param context
     */
    public static void removeNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }
}
