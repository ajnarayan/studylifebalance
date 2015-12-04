package com.example.anjjan.studylifebalance;

import android.content.Context;
/**
 * Created by jinghong on 11/30/15.
 */
public class CheckExam {
    private ExamsDB examsDB;

    public CheckExam(Context context) {
        examsDB = new ExamsDB(context);
    }

    public boolean validate(String subject, String dateTime) {
        if (subject == null || dateTime == null) {
            return false;
        } else if (subject == "") {
            return false;
        } else {
            if (examsDB.getDateTime(subject) == dateTime) return false;
        }
        //if (isExist(subject)) return false;
        return true;
    }
    /**
     *
     * @param subject
     * @param dateTime
     * @param seat
     * @param room
     * @return
     */
    public boolean createExam(String subject, String dateTime, String seat, String room) {
        return examsDB.insertExam(subject, dateTime, seat, room);
    }
    public void deleteExamBySubject(String subject) {
        examsDB.deleteExam(subject);
    }
    public boolean isExist(String subject, String dateTime) {
        return examsDB.isExamExist(subject, dateTime);
    }

}

