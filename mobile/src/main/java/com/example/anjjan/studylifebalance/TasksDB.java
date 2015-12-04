package com.example.anjjan.studylifebalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TasksDB extends SQLiteOpenHelper{

    public TasksDB(Context context) {
        super(context, "tasks", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "tasks(" +
                "subject TEXT," +
                "date TEXT," +
                "title TEXT," +
                "details TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not yet implement
    }


    public List<Task> getAllTasks() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 'tasks'", null);
        ArrayList<Task> list = new ArrayList<>(10);
        String subject, date, title, details;
        while(cursor.moveToNext()) {
            subject = cursor.getString(cursor.getColumnIndex("subject"));
            date = cursor.getString(cursor.getColumnIndex("date"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            details = cursor.getString(cursor.getColumnIndex("details"));
            list.add(new Task(subject, date, title, details));
        }
        return list;
    }

    /**
     * helper to get certain fields by subject
     * @param subject
     * @param fieldName
     * @return
     */
    private String getFieldByTaskSubject(String subject, String fieldName) {

        SQLiteDatabase db  = getReadableDatabase();
        //String selection = "username="+username;
        Cursor cursor = db.query("tasks", new String[]{fieldName}, "subject=?"
                , new String[]{subject}, null, null, null);
        if (cursor.getCount() == 0 || cursor.getColumnIndex(fieldName) < 0) {
            return null;
        }
        cursor.moveToFirst();
        String re = cursor.getString(cursor.getColumnIndex(fieldName));
        db.close();
        return re;
    }

    public String getTaskDate(String subject) {

        return getFieldByTaskSubject(subject, "date");
    }


    public boolean isTaskExist(String subject) {
        String date = getTaskDate(subject);
        return (date != null);
    }

    /**
     * insert new tasks
     * @param subject
     * @param date
     * @param title
     * @param details
     * @return
     */
    public boolean insertTask(String subject, String date, String title, String details) {
        if (isTaskExist(subject)) {
            return false;
        }
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("date", date);
        cv.put("title", title);
        cv.put("details", details);
        Long re = db.insert("tasks", null, cv);
        db.close();
        return re != -1;
    }




    /**
     * delete the task from dataBase
     * @param subject
     * @return
     */
    public boolean deleteTask(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        int colNum = db.delete("tasks", "subject=?", new String[] {subject});
        db.close();
        return colNum != 0;
    }
}
