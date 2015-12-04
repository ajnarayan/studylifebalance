package com.example.anjjan.studylifebalance;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Vibrate extends AppCompatActivity {

    Button activate,deactivate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibrate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        activate = (Button) findViewById(R.id.button2);
        deactivate = (Button) findViewById(R.id.button3);

        deactivate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Intent i = new Intent(getBaseContext(), NavMainActivity.class);
            }
        });

        activate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                //AudioManager manage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                //manage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                Toast.makeText(getApplicationContext(), "Phone Set to Silent During Exams", Toast.LENGTH_LONG);
                Intent i = new Intent(Vibrate.this, Exams.class);
            }
        });
    }

}