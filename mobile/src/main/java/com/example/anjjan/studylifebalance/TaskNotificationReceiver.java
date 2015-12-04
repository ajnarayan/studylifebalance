package com.example.anjjan.studylifebalance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;

public class TaskNotificationReceiver extends BroadcastReceiver {
    public TaskNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String task = intent.getStringExtra("subject");
        String date = intent.getStringExtra("dateTime");
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Task")
                .setContentText(task + " is Due on "+ date)
                .setTicker("You have a task coming")
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_launcher)
                //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                //.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, NavMainActivity.class), 0))
                .build();
        notification.defaults |=Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }
}
