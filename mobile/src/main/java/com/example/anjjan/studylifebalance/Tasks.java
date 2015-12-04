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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Tasks extends AppCompatActivity {
    private EditText subjectText;
    private DatePicker datePicker;
    private EditText titleText;
    private EditText detailsText;
    private Button saved;
    private CheckTask savedChecker;
    private Spinner dropdown;
    private long date;
    private String subject;


    private TextView textText;
    private TextView textText2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("New Task");


        dropdown = (Spinner) findViewById(R.id.spinner);
        String[] Sitems = new String[] { "Assignment", "Reminder", "Revision"};
        ArrayAdapter<String> Sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Sitems);
        dropdown.setAdapter(Sadapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        subjectText = (EditText) findViewById(R.id.subject);
        titleText = (EditText) findViewById(R.id.title);
        detailsText = (EditText) findViewById(R.id.details);
        saved = (Button) findViewById(R.id.saved);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        savedChecker = new CheckTask(this.getApplicationContext());
        textText = (TextView) findViewById(R.id.test);
        textText2 = (TextView) findViewById(R.id.test2);



        saved.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                subject = subjectText.getText().toString();
                String title = titleText.getText().toString();
                String pre = dropdown.getSelectedItem().toString();
                String details = pre + "     "+ detailsText.getText().toString();
                date = datePicker.getCalendarView().getDate();
                Date dateT = new Date(date);
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String dateTime = dateFormat.format(dateT);
                String test = "???";
                String test2 = "2 ???";
                // if already exists, return to enter again
                if (savedChecker.isExist(subject, dateTime)) {
                    test = "This task schedule exists.";
                    test2 = "Please enter a new one";
                    textText.setText(test);
                    textText2.setText(test2);
                    Intent newtask1 = new Intent(getBaseContext(),Tasks.class);
                    startActivity(newtask1);

                } else if (savedChecker.validate(subject, dateTime)) {
                    test = "This is a new task schedule!";
                    textText.setText(test);

                    // if saved success, go to navMain, otherwise return to enter again
                    if (savedChecker.createTask(subject, dateTime, title, details)) {


                        //after insert in db

                        if (savedChecker.isExist(subject, dateTime)) {
                            test2 = "Your task schedule has been saved.";
                            textText2.setText(test2);
                            setNotification(dateTime);
                            Intent newtask2 = new Intent(getBaseContext(),NavMainActivity.class);
                            startActivity(newtask2);
                        }
                    } else {
                        test2 = "Some error occurs, please try again.";
                        textText2.setText(test2);
                        Intent newtask3 = new Intent(getBaseContext(),Tasks.class);
                        startActivity(newtask3);
                    }
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setNotification(String dateTime) {
        long timeNow = Calendar.getInstance().getTimeInMillis();
        int unique = (int) timeNow;
        long intervalOneDay = date - 86400000;
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Tasks.this, TaskNotificationReceiver.class);
        intent.putExtra("subject", subject);
        intent.putExtra("dateTime", dateTime);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Tasks.this, unique, intent, 0);
        // set for 1 day early;
        //alarmMgr.set(AlarmManager.RTC, intervalOneDay, alarmIntent);
        //alarmMgr.set(AlarmManager.RTC, timeNow + 60000, alarmIntent);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,timeNow,AlarmManager.INTERVAL_DAY,alarmIntent);
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
