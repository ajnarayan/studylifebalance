package com.example.anjjan.studylifebalance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class ShowTaskList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Task> allTasks;
        TasksDB tasksDB;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_task_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Task List");


        ListView list = (ListView) findViewById(R.id.taskList);
        tasksDB = new TasksDB(getBaseContext());

        allTasks = tasksDB.getAllTasks();


        TaskListAdapter adapter = new TaskListAdapter(getBaseContext(),allTasks);

        list.setAdapter(adapter);
        if (adapter.refresh()) {
            Intent newtask = new Intent(getBaseContext(), ShowTaskList.class);
            startActivity(newtask);
        };


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newtask = new Intent(getBaseContext(), NavMainActivity.class);
                startActivity(newtask);
            }
        });
    }

}
