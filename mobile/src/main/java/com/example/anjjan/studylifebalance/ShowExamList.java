package com.example.anjjan.studylifebalance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.View;
import android.support.design.widget.FloatingActionButton;

import android.widget.ListView;
import android.content.Intent;


import java.util.List;

public class ShowExamList extends AppCompatActivity {
    List<Exam> allExams;
    ExamsDB examsDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_exam_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Exam List");


        ListView list = (ListView) findViewById(R.id.examList);
        examsDB = new ExamsDB(getBaseContext());

        allExams = examsDB.getAllExams();


        ExamListAdapter adapter = new ExamListAdapter(getBaseContext(),allExams);
        list.setAdapter(adapter);
        if (adapter.refresh()) {
            Intent newtask = new Intent(getBaseContext(), ShowExamList.class);
            startActivity(newtask);
        }
        //list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        //setContentView(list);

        //ExamListAdapter adapter = new ExamListAdapter(getBaseContext(), allExams);
        //setListAdapter(adapter);


        /*Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] Sitems = new String[] { "Assignment", "Reminder", "Revision"};
        ArrayAdapter<String> Sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Sitems);
        dropdown.setAdapter(Sadapter);*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newtask = new Intent(getBaseContext(),NavMainActivity.class);
                startActivity(newtask);
            }
        });
    }

}
