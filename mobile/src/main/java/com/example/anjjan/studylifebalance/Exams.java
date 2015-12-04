package com.example.anjjan.studylifebalance;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Exams extends AppCompatActivity {

    private EditText subjectText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private EditText seatText;
    private EditText roomText;
    private Button saved;
    private CheckExam savedChecker;
    private long date;
    private long hours;
    private String subject;
    private long minh;
    private long hourh;

    private TextView textText;
    private TextView textText2;
    private TextView textText3;
    private TextView textText4;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Exam Schedule");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        subjectText = (EditText) findViewById(R.id.subject);
        seatText = (EditText) findViewById(R.id.seat);
        roomText = (EditText) findViewById(R.id.room);
        saved = (Button) findViewById(R.id.saved);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        savedChecker = new CheckExam(this.getApplicationContext());
        textText = (TextView) findViewById(R.id.test);
        textText2 = (TextView) findViewById(R.id.test2);
        textText3 = (TextView) findViewById(R.id.test3);
        textText4 = (TextView) findViewById(R.id.test4);


        saved.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                subject = subjectText.getText().toString();
                String seat = seatText.getText().toString();
                 String room = roomText.getText().toString();

                date = datePicker.getCalendarView().getDate();
                Date dateT = new Date(date);
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();
                String time = String.format("%02d:%02d", hour, min);
                hours = (long) (hour * 3600000 + min * 60000);
                hourh = (long) hour * 3600000;
                minh = (long) min * 60000;
                //DateFormat timeFormat = new SimpleDateFormat("HH:mma");

                //String time = timeFormat.format(hour+min);
                String dateTime = dateFormat.format(dateT) + "     " + time;
                String test = "???";
                String test2 = "2 ???";
                if (savedChecker.isExist(subject, dateTime)) {
                    test = "This exam schedule exists.";
                    test2 = "Please enter a new one";
                    textText.setText(test);
                    textText2.setText(test2);
                    Intent newtask1 = new Intent(getBaseContext(), Exams.class);
                    startActivity(newtask1);

                } else if (savedChecker.validate(subject, dateTime)) {
                    test = "This is a new exam schedule!";
                    textText.setText(test);

                    // if saved success, go to navMain, otherwise return to enter again
                    if (savedChecker.createExam(subject, dateTime, seat, room)) {

                        if (savedChecker.isExist(subject, dateTime)) {
                            test2 = "Your exam schedule has been saved.";
                            textText2.setText(test2);

                            setNotification(seat, room);

                            Intent newtask2 = new Intent(getBaseContext(), NavMainActivity.class);
                            //Intent newtask2 = new Intent(getBaseContext(),TasksActivity.class);
                            startActivity(newtask2);
                        }
                    } else {
                        test2 = "Some error occurs, please try again.";
                        textText2.setText(test2);
                        Intent newtask3 = new Intent(getBaseContext(), Exams.class);
                        startActivity(newtask3);

                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setNotification(String seat, String room) {
        long timeNow = Calendar.getInstance().getTimeInMillis();
        long timeHour = Calendar.getInstance().getTime().getHours() * 3600000;
        long timeMin = Calendar.getInstance().getTime().getMinutes() * 60000;

        int unique = (int) timeNow;
        // test purpose one min early;
        long selectedTime = date + hours - timeHour - timeMin;
        long interValOneMinute = selectedTime - 60000;
        long interValThreeMinute = selectedTime - 180000;

        long timeInterval = interValOneMinute - timeNow;

        long intervalOneHour = selectedTime - 3600000;
        long intervalOneDay = selectedTime  -  86400000;
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        AlarmManager alarmMgr2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Exams.this, ExamNotificationReceiver.class);
        intent.putExtra("subject", subject);
        intent.putExtra("seat",seat);
        intent.putExtra("room",room);

        /*String test3 = "realtime now: "+ timeNow + "    ;target: " + selectedTime + "    ;wait: " + timeInterval;
        textText2.setText(test3);
        String test4 = "realtime hours: " + timeHour + "     ;target hour: " + hourh +
                "     ;realtime min" + timeMin + "     ;target min: " + minh;
        textText.setText(test4);*/

        /*Bundle b = new Bundle();
        b.putLong("time", timeNow+30000);
        b.putString("subject", subject);
        intent.putExtra("bundle", b);*/
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Exams.this, unique, intent, 0);
        PendingIntent alarmIntent2 = PendingIntent.getBroadcast(Exams.this, (unique + 1), intent, 0);
        // set for 1 day early;

        //alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 30000, alarmIntent);
        alarmMgr.set(AlarmManager.RTC, selectedTime - 60000, alarmIntent);
        alarmMgr2.set(AlarmManager.RTC, selectedTime - 180000, alarmIntent2);
        //alarmMgr.set(AlarmManager.RTC, intervalOneHour, alarmIntent);
        //alarmMgr2.set(AlarmManager.RTC, intervalOneDay, alarmIntent);

    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Exams Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.anjjan.studylifebalance/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Exams Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.anjjan.studylifebalance/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
