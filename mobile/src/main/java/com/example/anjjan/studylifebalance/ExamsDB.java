package com.example.anjjan.studylifebalance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

public class ExamsDB extends SQLiteOpenHelper{

    public ExamsDB(Context context) {
        super(context, "exams", null, 1);
    }

    // exams(subject CS1301Exam1, date 1111, time 1200)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "exams(" +
                "subject TEXT," +
                "dateTime TEXT," +
                "seat TEXT," +
                "room TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //not yet implement
    }

    /**
     * get the full exams lists
     * @return list
     */
    public List<Exam> getAllExams() {

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM 'exams'", null);
        ArrayList<Exam> list = new ArrayList<>(10);
        String subject, dateTime, seat, room;
        while(cursor.moveToNext()) {
            subject = cursor.getString(cursor.getColumnIndex("subject"));
            dateTime = cursor.getString(cursor.getColumnIndex("dateTime"));
            seat = cursor.getString(cursor.getColumnIndex("seat"));
            room = cursor.getString(cursor.getColumnIndex("room"));
            list.add(new Exam(subject, dateTime, seat, room));
        }
        return list;
    }

    /**
     * helper to get certain fields by subject
     * @param subject
     * @param fieldName
     * @return
     */
    private String getFieldByExamSubject(String subject, String fieldName) {

        SQLiteDatabase db  = getReadableDatabase();
        //String selection = "username="+username;
        Cursor cursor = db.query("exams", new String[]{fieldName}, "subject=?"
                , new String[]{subject}, null, null, null);
        if (cursor.getCount() == 0 || cursor.getColumnIndex(fieldName) < 0) {
            return null;
        }
        cursor.moveToFirst();
        String re = cursor.getString(cursor.getColumnIndex(fieldName));
        db.close();
        return re;
    }

    public String getDateTime(String subject) {

        return getFieldByExamSubject(subject, "dateTime");
    }


    public boolean isExamExist(String subject) {
        String dateTime = getDateTime(subject);
        //if (dateTime == newDateTime) return false;
        return (dateTime != null) ;
        //return true;
    }

    /**
     * insert new exams
     * @param subject
     * @param dateTime
     * @param seat
     * @param room
     * @return
     */
    public boolean insertExam(String subject, String dateTime, String seat, String room) {
        if (isExamExist(subject)) {
            return false;
        }
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("subject", subject);
        cv.put("dateTime", dateTime);
        cv.put("seat", seat);
        cv.put("room", room);
        Long re = db.insert("exams", null, cv);
        db.close();
        return re != -1;
    }



    /**
     * delete the exam from dataBase
     * @param subject
     * @return
     */
    public boolean deleteExam(String subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        int colNum = db.delete("exams", "subject=?", new String[] {subject});
        db.close();
        return colNum != 0;
    }
}
