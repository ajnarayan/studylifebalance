package com.example.anjjan.studylifebalance;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ListView;
import android.widget.RadioButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
public class NavMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExamsDB examsDB;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_main);


        /*AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(NavMainActivity.this, TaskNotificationReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(NavMainActivity.this, 0, intent, 0);
        // set for 30 seconds later
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 30000, alarmIntent);*/
        //alarmMgr.cancel(alarmIntent);


        //Intent intent = new Intent(NavMainActivity.this, Notifications.class);
        //startActivity(intent);




        /*AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(NavMainActivity.this, NotificationReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(NavMainActivity.this, 0, intent, 0);
        // set for 30 seconds later
        alarmMgr.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + 30000, alarmIntent);
        TextView test = (TextView) findViewById(R.id.test);
        test.setText("alarm activity");*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        RadioButton examButton = (RadioButton) findViewById(R.id.showEBtn);
        RadioButton taskButton = (RadioButton) findViewById(R.id.showTBtn);
        examButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newtask = new Intent(getBaseContext(),ShowExamList.class);
                startActivity(newtask);
            }
        });
        taskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newtask = new Intent(getBaseContext(),ShowTaskList.class);
                startActivity(newtask);
            }
        });

        List<Task> allTasks;
        TasksDB tasksDB;
        ListView listT = (ListView) findViewById(R.id.taskList);
        tasksDB = new TasksDB(getBaseContext());
        allTasks = tasksDB.getAllTasks();
        TaskListAdapter adapterT = new TaskListAdapter(getBaseContext(), allTasks);
        listT.setAdapter(adapterT);

        List<Exam> allExams;
        ListView listE = (ListView) findViewById(R.id.examList);
        examsDB = new ExamsDB(getBaseContext());
        //testDelected(examsDB);

        allExams = examsDB.getAllExams();


        ExamListAdapter adapterE = new ExamListAdapter(getBaseContext(),allExams);
        listE.setAdapter(adapterE);
        if (adapterE.refresh()) {
            Intent newtask = new Intent(getBaseContext(), NavMainActivity.class);
            startActivity(newtask);
        }

    }

    /*public void testDelected(ExamsDB examsDB) {
        List<Exam> allExams = examsDB.getAllExams();
        Date dateT = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int timeHour = Calendar.getInstance().getTime().getHours();
        int timeMin = Calendar.getInstance().getTime().getMinutes();
        String time = String.format("%02d:%02d", timeHour, timeMin);
        String dateCheck = dateFormat.format(dateT) +  time;
        String temp = dateCheck.replaceAll(" ", "0");
        String temp1 = temp.replaceAll("-", "0");
        String temp2 = temp1.replaceAll(":", "0");
        int cur = Integer.parseInt(temp2);
        /*int size = allExams.size();
        for (int i = 0; i < size; i++) {
            Exam curExam = allExams.get(i);
            String target = curExam.getDateTime();
            int tar = Integer.parseInt(target);
            //if (tar <= cur) {
                //examsDB.deleteExam(curExam.getSubject());
            //}
        }
    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

          if (id == R.id.nav_dashboard) {

        } else if (id == R.id.nav_tasks)
        {
            Toast.makeText(NavMainActivity.this, "TASKS", Toast.LENGTH_SHORT).show();
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(
                    new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newtask = new Intent(getBaseContext(),Tasks.class);
                startActivity(newtask);
            }
        });
        }
        else if (id == R.id.nav_exams) {
              Toast.makeText(NavMainActivity.this, "EXAMS", Toast.LENGTH_SHORT).show();
              FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
              fab.setOnClickListener(
                      new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Intent newtask = new Intent(getBaseContext(),Exams.class);
                              startActivity(newtask);
                          }
                      });


          }
          else if (id == R.id.nav_import) {
              Intent i = new Intent(getBaseContext(),Import.class);
              startActivity(i);
          } else if (id == R.id.nav_vibrate) {

            Intent i = new Intent(getBaseContext(),Vibrate.class);
              startActivity(i);

          }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
