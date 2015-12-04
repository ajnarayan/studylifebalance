package com.example.anjjan.studylifebalance;


public class Exam {
    private String subject;
    private String dateTime;
    private String seat;
    private String room;
    /**
     *
     * @param subject
     * @param dateTime
     * @param seat
     * @param room
     */
    public Exam(String subject, String dateTime, String seat, String room) {
        this.subject = subject;
        this.dateTime = dateTime;
        this.seat = seat;
        this.room = room;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exam)) return false;

        Exam exam = (Exam) o;

        if (dateTime != null ? !dateTime.equals(exam.dateTime) : exam.dateTime != null) return false;
        return !(subject != null ? !subject.equals(exam.subject) : exam.subject != null) && exam.equals(exam.subject);

    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
        return result;
    }


    public Exam(String subject) {
        this.subject = subject;
    }



    /**
     *
     * @return
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     *
     * @param dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }



    /**
     *
     * @param subject
     */
    public void setSubject(String subject) {

        this.subject = subject;
    }


    /**
     *
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     *
     * @param seat
     */
    public void setSeat(String seat) {

        this.seat = seat;
    }


    /**
     *
     * @return
     */
    public String getSeat() {
        return seat;
    }


    /**
     *
     * @param room
     */
    public void setRoom(String room) {

        this.room = room;
    }


    /**
     *
     * @return
     */
    public String getRoom() {
        return room;
    }




}

