package com.example.anjjan.studylifebalance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jinghong on 12/1/15.
 */
public class TaskListAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final List<Task> allTasks;
    private TasksDB tasksDB;
    boolean check = false;

    public TaskListAdapter(Context context, List<Task> objects) {
        super(context, R.layout.task_list_item, objects);
        this.context = context;
        this.allTasks = objects;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        final Task curTask = allTasks.get(position);
        tasksDB = new TasksDB(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.task_list_item, null);

        TextView subject = (TextView) view.findViewById(R.id.subject);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView details = (TextView) view.findViewById(R.id.details);
        subject.setText(curTask.getSubject());
        date.setText(curTask.getTaskDate());
        title.setText(curTask.getTitle());
        details.setText(curTask.getDetails());
        Button deleteBtn = (Button) view.findViewById(R.id.delete);
        /*if (context instanceof NavMainActivity) {
            deleteBtn.setVisibility(View.INVISIBLE);

        } else {
            deleteBtn.setVisibility(View.VISIBLE);*/
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksDB.deleteTask(curTask.getSubject());
                notifyDataSetChanged();
                check = true;
                //notifyDataSetChanged();

            }
        });
        return view;
    }

    public boolean refresh() {
        return check;
    }


}

