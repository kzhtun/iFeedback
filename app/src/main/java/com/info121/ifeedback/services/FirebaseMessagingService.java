package com.info121.ifeedback.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.info121.ifeedback.App;
import com.info121.ifeedback.R;
import com.info121.ifeedback.activities.MainActivity;


/**
 * Created by KZHTUN on 7/28/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    private static final String EXTRA_MESSAGE = "extra.message";
    public final String text = "";


    private void showNotification(String title, String messageBody) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(App.NOTIFICATION_SOUND_URI)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(this, R.color.red))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
               // .setDefaults(Notification.DEFAULT_ALL)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(0, notificationBuilder.build());

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData() != null) {

            if (remoteMessage.getData().size() == 2) {
                showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            }

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


//    public void showDialog(String jobNo, String name, String phone, String displayMessage) {
//        Intent intent = new Intent(this, ShowDialogService.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getService(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.my_limo_launcher)
//                .setContentTitle("Tita Limo")
//                .setContentText("New job received")
//                .setAutoCancel(true)
//                .setSound(App.PROMINENT_SOUND_URI)
//                .setContentIntent(pendingIntent)
//                .setColor(ContextCompat.getColor(this, R.color.red))
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//             //   .setDefaults(Notification.DEFAULT_ALL)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//        notificationManager.cancel(0);
//
//
//        Bundle bundle = new Bundle();
//
//        bundle.putString(ShowDialogService.JOB_NO, jobNo);
//        bundle.putString(ShowDialogService.NAME, name);
//        bundle.putString(ShowDialogService.PHONE, phone);
//        bundle.putString(ShowDialogService.MESSAGE, displayMessage);
//
//        intent.putExtras(bundle);
//        startService(intent);
//    }

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