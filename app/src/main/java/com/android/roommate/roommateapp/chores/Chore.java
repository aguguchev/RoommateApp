package com.android.roommate.roommateapp.chores;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class Chore {
    private String description;
    private String frequency;
    private Date lastComplete;
    private boolean completed;

    Chore(String freq, String desc){
        description = desc;
        frequency = freq;
        completed = true;
        Calendar calendar = new GregorianCalendar();
        lastComplete = calendar.getTime();
    }

    void updateWithDate(String acceptedFrequencies){
        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(true);
        if(completed){
            switch(frequency) {
                case "daily":
                    calendar.add(Calendar.DATE, -1);
                case "weekly":
                    calendar.add(Calendar.DATE, -7);
                case "monthly":
                    calendar.add(Calendar.MONTH, -1);
            }
            Date compareDate = calendar.getTime();
            if(lastComplete.compareTo(compareDate) >= 0)
                completed = false;
        }

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
    boolean getCompleted(){
        return completed;
    }
}