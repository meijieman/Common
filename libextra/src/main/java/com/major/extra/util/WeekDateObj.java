package com.major.extra.util;

public class WeekDateObj{

    private String week;
    private String date;

    public WeekDateObj(String week, String date){
        super();
        this.week = week;
        this.date = date;
    }

    public String getWeek(){
        return week;
    }

    public void setWeek(String week){
        this.week = week;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    @Override
    public String toString(){
        return "SelectSeatGalleryObj [week=" + week + ", date=" + date + "]";
    }

}
