package com.example.nazoorahamed.coursework2;

public class Appointment {

    private Integer id;
    private String title;
    private String desc;
    private String time;
    private String date;

    public Appointment(Integer id, String title, String desc, String date, String time) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
