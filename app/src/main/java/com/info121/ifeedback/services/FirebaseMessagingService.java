package com.info121.ifeedback.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.activities.ViewFeedbackActivity;


/**
 * Created by KZHTUN on 7/28/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    private static final String EXTRA_MESSAGE = "extra.message";
    public final String text = "";


    String OLD_CH = "";
    String NEW_CH = "";

    private void showNotification(String title, String body,  String fbid) {
        OLD_CH  = App.getOldChannelId();
        NEW_CH = App.getNewChannelId();

        Intent intent = new Intent(this, ViewFeedbackActivity.class);
        intent.putExtra("ID", fbid);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = App.getNotificationSoundUri();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NEW_CH)
                .setSmallIcon(R.mipmap.app_icon)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);



        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (soundUri != null) {
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                // Creating an Audio Attribute
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build();


//                //it will delete existing channel if it exists
                if (mNotificationManager.getNotificationChannel(OLD_CH) != null) {
                    mNotificationManager.deleteNotificationChannel(OLD_CH);
                }

                // Creating Channel
                NotificationChannel notificationChannel = new NotificationChannel(NEW_CH, NEW_CH, NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.enableLights(true);
                notificationChannel.enableVibration(true);
                notificationChannel.setSound(soundUri, audioAttributes);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }
        }

        mNotificationManager.notify(0, notificationBuilder.build());
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {


                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"),  remoteMessage.getData().get("fbid"));



//            if (remoteMessage.getData().size() == 4) {
//                showDialog(remoteMessage.getData().get("jobNo"),
//                        remoteMessage.getData().get("Name"),
//                        remoteMessage.getData().get("phone"),
//                        remoteMessage.getData().get("displayMsg")
//                );
//            }
        }

        super.onMessageReceived(remoteMessage);

    }




    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }


}
