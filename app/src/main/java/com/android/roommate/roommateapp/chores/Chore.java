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
        completed = false;
        Calendar calendar = new GregorianCalendar();
        lastComplete = calendar.getTime();
    }

    void updateWithDate(String acceptedFrequencies){
        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(true);
        if(completed){
            switch(frequency) {
                case "Debug":
                    calendar.add(Calendar.SECOND, -30);
                case "Daily":
                    calendar.add(Calendar.DATE, -1);
                case "Weekly":
                    calendar.add(Calendar.DATE, -7);
                case "Monthly":
                    calendar.add(Calendar.MONTH, -1);
            }
            Date compareDate = calendar.getTime();
            if(lastComplete.compareTo(compareDate) >= 0)
                completed = false;
        }

    }

    void complete(){
        completed = true;
        lastComplete = (new GregorianCalendar()).getTime();
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
    boolean isCompleted(){
        return completed;
    }
}