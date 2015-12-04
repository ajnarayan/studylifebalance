package com.example.anjjan.studylifebalance;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class ExamNotificationReceiver extends BroadcastReceiver {
    public ExamNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String exam = intent.getStringExtra("subject");
        String seat = intent.getStringExtra("seat");
        String room = intent.getStringExtra("room");
        //Bundle b = intent.getBundleExtra("bundle");
        //Long when = b.getLong("time");
        //String exam = b.getString("subject");
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle("Exam Reminder")
                //.setWhen(when)
                .setContentText(exam + " Exam at "+ room + " and your seat is : " +seat)
                .setTicker("You have an exam coming! All the Best ! ")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher)
                        //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                        //.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, NavMainActivity.class), 0))
                .build();
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }
}
