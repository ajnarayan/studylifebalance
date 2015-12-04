package com.example.anjjan.studylifebalance;

/**
 * Created by jinghong on 11/24/15.
 */
public class Task {
    private String subject;
    private String date;
    private String title;
    private String details;
    /**
     *
     * @param subject
     * @param date
     * @param title
     * @param details
     */
    public Task(String subject,  String date, String title, String details) {
        this.subject = subject;
        this.date = date;
        this.title = title;
        this.details = details;
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;

        Task task = (Task) o;

        if (date != null ? !date.equals(task.date) :task.date != null) return false;
        return !(subject != null ? !subject.equals(task.subject) : task.subject != null) && task.equals(task.subject);

    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result = subject.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }


    public Task(String subject) {
        this.subject = subject;
    }



    /**
     *
     * @return
     */
    public String getTaskDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setTaskDate(String date) {
        this.date = date;
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
     * @param title
     */
    public void setTitle(String title) {

        this.title = title;
    }


    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }


    /**
     *
     * @param details
     */
    public void setDetails(String details) {

        this.details = details;
    }


    /**
     *
     * @return
     */
    public String getDetails() {
        return details;
    }
}
