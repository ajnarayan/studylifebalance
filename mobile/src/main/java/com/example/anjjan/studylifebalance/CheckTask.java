package com.example.anjjan.studylifebalance;

import android.content.Context;

/**
 * Created by jinghong on 11/30/15.
 */
public class CheckTask {
    private TasksDB tasksDB;

    public CheckTask(Context context) {
        tasksDB = new TasksDB(context);
    }

    public boolean validate(String subject, String date) {
        if (subject == null || date == null) {
            return false;
        } else if (subject == "") {
            return false;
        } else {
            if (tasksDB.getTaskDate(subject) == date) return false;
        }
        //if (isExist(subject)) return false;
        return true;
    }
    /**
     *
     * @param subject
     * @param date
     * @param title
     * @param details
     * @return
     */
    public boolean createTask(String subject, String date, String title, String details) {
        return tasksDB.insertTask(subject, date, title, details);
    }
    public boolean isExist(String subject) {
        return tasksDB.isTaskExist(subject);
    }
}

