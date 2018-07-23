package com.android.roommate.roommateapp.chores;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class Chore {
    private String description;
    private String frequency;
    private Date lastComplete;
    private boolean completed;
    private int id;

    Chore(int id, long lC, String desc, String freq){
        description = desc;
        frequency = freq;
        lastComplete = new Date(lC);
        if(isCompleted(lastComplete, freq))
            completed = true;
        else
            completed = false;
        this.id = id;
    }

    void complete(){
        completed = true;
        lastComplete = (new GregorianCalendar()).getTime();
    }

    private boolean isCompleted(Date lC, String freq){
        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(true);
        calendar = computeLCMinusInterval(freq);
        return calendar.getTime().before(lC);
    }

    public static Calendar computeLCMinusInterval(String freq){
        Calendar c = new GregorianCalendar();
        c.setLenient(true);
        if(freq.equals("Daily"))
            c.add(Calendar.DAY_OF_YEAR, -1);
        else if(freq.equals("Weekly"))
            c.add(Calendar.WEEK_OF_YEAR, -1);
        else if(freq.equals("Month"))
            c.add(Calendar.MONTH, -1);
        else
            c.add(Calendar.SECOND, -5);
        return c;
    }

    @Override
    public String toString(){
        return description;
    }

    String getDescription(){
        return description;
    }
    String getFrequency(){
        return frequency;
    }
    Date getLastComplete(){
        return lastComplete;
    }
    int getId() {return id;}
    boolean isCompleted(){
        return completed;
    }
    void setDescription(String d){ description = d;}
    void setFrequency(String f){ frequency = f;}
}