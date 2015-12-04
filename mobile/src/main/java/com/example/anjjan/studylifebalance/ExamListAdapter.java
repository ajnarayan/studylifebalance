package com.example.anjjan.studylifebalance;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import java.util.List;

/**
 * Created by jinghong on 12/1/15.
 */
public class ExamListAdapter extends ArrayAdapter<Exam> {
    private final Context context;
    private List<Exam> allExams;
    private ExamsDB examsDB;
    boolean check = false;

    public ExamListAdapter(Context context, List<Exam> objects) {
        super(context, R.layout.exam_list_item, objects);
        this.context = context;
        this.allExams = objects;
    }





    public View getView(int position, View convertView, ViewGroup parent) {
        final Exam curExam = allExams.get(position);
        examsDB = new ExamsDB(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        @SuppressLint("ViewHolder") View view = inflater.inflate(R.layout.exam_list_item, null);

        TextView subject = (TextView) view.findViewById(R.id.subject);
        TextView dateTime = (TextView) view.findViewById(R.id.dateTime);
        TextView seat = (TextView) view.findViewById(R.id.seat);
        TextView room = (TextView) view.findViewById(R.id.room);
        subject.setText(curExam.getSubject());
        dateTime.setText(curExam.getDateTime());
        seat.setText(curExam.getSeat());
        room.setText(curExam.getRoom());
        Button deleteBtn = (Button) view.findViewById(R.id.delete);
        /*if (context instanceof NavMainActivity) {
            deleteBtn.setVisibility(View.INVISIBLE);

        } else {
            deleteBtn.setVisibility(View.VISIBLE);*/
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    examsDB.deleteExam(curExam.getSubject());
                    notifyDataSetChanged();
                    check = true;
                    //delete(curExam.getSubject());

                }
            });
        //}
        return view;
    }
    public boolean refresh() {
        return check;
    }



}

