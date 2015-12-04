package com.example.anjjan.studylifebalance;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.anjjan.studylifebalance.TasksDB;
public class Import extends AppCompatActivity {
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;
    LinearLayout activityLayout;
    CheckTask savedChecker;
    public String[] Str_Array;
    public String[] date_array;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY};
    TasksDB tasksDB;// = new TasksDB(getApplicationContext());
    List<Task> noti;
    Button saved;
    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(" Import From Google Calendar");

        activityLayout =(LinearLayout) findViewById(R.id.linearLayout);
        mProgress = ProgressDialog.show(this, "Calling Google Calendar API", "Google Calendar API");


        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newtask = new Intent(getBaseContext(),NavMainActivity.class);
                startActivity(newtask);
            }
        });
        tasksDB = new TasksDB(getApplicationContext());
        noti = new ArrayList<>();
        saved = (Button) findViewById(R.id.saved);
        saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveFinalResult();
            }
        });
    }

    public void saveFinalResult(){
        for(Task curTask: noti) {
            String subject = curTask.getSubject();
            String dateTime = curTask.getTaskDate();
            tasksDB.insertTask(subject, dateTime, "", "");
            setNotification(subject, dateTime);

            /*long timeNow = java.util.Calendar.getInstance().getTimeInMillis();
            int unique = (int) timeNow;

            /*long year2000 = 946766701 * 1000;

            int year = Integer.parseInt(dateTime.substring(6,10)) - 2000;
            int month = Integer.parseInt(dateTime.substring(3,5));
            int day = Integer.parseInt(dateTime.substring(0,2));

            int more = year * 3154 * 10000000 + month * 2628 * 1000000 + (day-1) * 86400000;
            long targetTime = more + year2000;8*/

            /*AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(Import.this, TaskNotificationReceiver.class);
            intent.putExtra("subject", subject);
            intent.putExtra("dateTime", dateTime);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(Import.this, unique, intent, 0);
            // set for 1 day early;
            //alarmMgr.set(AlarmManager.RTC, intervalOneDay, alarmIntent);
            //alarmMgr.set(AlarmManager.RTC, timeNow + 60000, alarmIntent);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,timeNow,AlarmManager.INTERVAL_DAY,alarmIntent);*/
        }
    }

    public void setNotification(String subject, String dateTime) {
        long timeNow = java.util.Calendar.getInstance().getTimeInMillis();
        int unique = (int) timeNow;
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Import.this, TaskNotificationReceiver.class);
        intent.putExtra("subject", subject);
        intent.putExtra("dateTime", dateTime);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(Import.this, unique, intent, 0);
        // set for 1 day early;
        //alarmMgr.set(AlarmManager.RTC, intervalOneDay, alarmIntent);
        //alarmMgr.set(AlarmManager.RTC, timeNow + 60000, alarmIntent);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,timeNow,AlarmManager.INTERVAL_DAY,alarmIntent);
    }

    /**
     * Called whenever this activity is pushed to the foreground, such as after
     * a call to onCreate().
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            //mOutputText.setText("Google Play Services required: " +
            //      "after installing, close and relaunch this app.");
            Toast.makeText(getApplicationContext(),"Google Play Services required: " +"after installing, close and relaunch this app.", Toast.LENGTH_LONG );
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getApplicationContext(),"Account Unspecified",Toast.LENGTH_LONG);
                    //mOutputText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempt to get a set of data from the Google Calendar API to display. If the
     * email address isn't known yet, then call chooseAccount() method so the
     * user can pick an account.
     */
    private void refreshResults() {
        if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new MakeRequestTask(mCredential).execute();
            } else {
                Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_LONG);
                //mOutputText.setText("No network connection available.");
            }
        }
    }
    /**
     * Starts an activity in Google Play Services so the user can pick an
     * account.
     */
    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date. Will
     * launch an error dialog for the user to update Google Play Services if
     * possible.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                connectionStatusCode,
                Import.this,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;
        private Exception mLastError = null;



        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         * @param params no parameters needed for this task.
         **/
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }
        /**
         * Fetch a list of the next 10 events from the primary calendar.
         * @return List of Strings describing returned events.
         * @throws IOException
         **/
        private List<String> getDataFromApi() throws IOException {
            // List the next 10 events from the primary calendar.
            // int i=0;
            //date_array = new String[10];
            DateTime now = new DateTime(System.currentTimeMillis());
            List<String> eventStrings = new ArrayList<String>();
            Events events = mService.events().list("primary")
                    .setMaxResults(10)
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    // All-day events don't have start times, so just use
                    // the start date.
                    start = event.getStart().getDate();
                }
                eventStrings.add(
                        String.format("%s (%s)", event.getSummary(), start));
                //DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                //String date = dateFormat.format(start);
                //  date_array[i]=date.toString();
                //  i++;
            }

            //testing purpose
            // format the getDateTime()
            //DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy     hh:mm");
            //String date = dateFormat.format(event.getDateTime());
            //String.format("%s (%s)", event.getSummary(), date);
            //String task1 = "hello this is exam1 (07-11-2016     13:50)";
            //String task2 = "and this is exam2 (07-11-2016     14:50)";




            //String.format("%s (%s)", event.getSummary(), event.getDate());
            //String task1 = "hello this is task1 (2015-12-04)";
            //String task2 = "and this is task2 (2015-12-05)";
            //eventStrings.add(task1);
            //eventStrings.add(task2);
            return eventStrings;
        }


        @Override
        protected void onPreExecute() {
            //mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            Str_Array = new String[output.size()];
            if (output == null || output.size() == 0) {
                Toast.makeText(getApplicationContext(), "No Results Returned :(", Toast.LENGTH_LONG);
                // mOutputText.setText("No results returned.");
            } else {
                //output.add(0, "Data retrieved using the Google Calendar API:");
                Str_Array = output.toArray(Str_Array);
                //dynamic checkbox based on events
                for (int i = 0; i < Str_Array.length; i++)
                {
                    CheckBox cb = new CheckBox(getApplicationContext());
                    cb.setText(Str_Array[i]);
                    cb.setTag(Str_Array[i]);
                    cb.setId(i);
                    activityLayout.addView(cb);
                    cb.setOnCheckedChangeListener(handlecheck(cb));
                }
                //mOutputText.setText(TextUtils.join("\n", output));
            }
        }
        /*
                private CompoundButton.OnCheckedChangeListener handlecheck (final CheckBox chk, int i) {
                    //final String dateT = date_array[i];
                    //final String subject = chk.getText().toString();
                    return new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                if (savedChecker.validate(subject, dateT)) {
                                    if (savedChecker.createTask(subject, dateT, subject, subject)) {
                                        Toast.makeText(getApplicationContext(), " Task Added " + chk.getTag(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    };
                }
        */
        private boolean onceChecked = false;
        private CompoundButton.OnCheckedChangeListener handlecheck (final CheckBox chk) {
            return new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //add event to task
                    //tasksDB = new TasksDB(getApplicationContext());
                    String event = chk.getTag().toString();
                    int length = event.length();
                    int lengthofTime = 10;
                    int timeEndIndex = length - 1;
                    int timeStartIndex = length - 1 - lengthofTime;
                    int subjectEndIndex = timeStartIndex - 2;
                    String timeT = event.substring(timeStartIndex,timeEndIndex);
                    String year = timeT.substring(0,4);
                    String month = timeT.substring(5,7);
                    String day = timeT.substring(8,10);
                    String time = day + "-" + month + "-" + year;
                    String subject = event.substring(0, subjectEndIndex);
                    Task curTask = new Task(subject, time, "", "");
                    if (isChecked) {
                        if(!tasksDB.isTaskExist(subject)) {
                            Toast.makeText(getApplicationContext(), " Task Added " + chk.getTag(),
                                    Toast.LENGTH_LONG).show();
                            onceChecked = true;

                            tasksDB.insertTask(subject, time, "", "");
                            //noti.add(curTask);

                        } else {
                            Toast.makeText(getApplicationContext(), " Task already exists",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                    if (!isChecked && onceChecked) {
                        Toast.makeText(getApplicationContext(), " Task Deleted " + chk.getTag(),
                                Toast.LENGTH_LONG).show();
                        tasksDB.deleteTask(subject);
                        //noti.remove(curTask);
                    }

                }
            };
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            Import.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getApplicationContext(),"The following error occured : "+mLastError.getMessage() ,Toast.LENGTH_LONG);
                    //mOutputText.setText("The following error occurred:\n"
                    //        + mLastError.getMessage());
                }
            } else {
                Toast.makeText(getApplicationContext(),"Request Cancelled",Toast.LENGTH_LONG);
                //mOutputText.setText("Request cancelled.");
            }
        }
    }
}