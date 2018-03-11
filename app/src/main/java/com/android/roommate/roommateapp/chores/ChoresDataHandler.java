package com.android.roommate.roommateapp.chores;

import android.content.Context;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by VengefulLimaBean on 3/10/2018.
 */

public class ChoresDataHandler {
    private ArrayList<Chore> choreList;
    private final String[] freqOptions = {"daily", "weekly", "monthly"};

    ChoresDataHandler(){
        choreList = new ArrayList<Chore>();
    }

    public void addChore(String desc, String freq){
        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(true);
        //set to reset at midnights
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date createDate = calendar.getTime();
        choreList.add(new Chore(desc, freq, createDate));
    }

    public ArrayList<Chore> getChoresList(){
        return choreList;
    }

    private class Chore {
        private String description;
        private String frequency;
        private Date lastComplete;

        Chore(String desc, String freq, Date creationDate){
            description = desc;
            frequency = freq;
            lastComplete = creationDate;
        }
        public void setDescription(String desc){
            description = desc;
        }
        public void setFrequency(String freq){
            frequency = freq;
        }
        public void setLastComplete(Date date){
            lastComplete = date;
        }
        public String getDescription(){
            return description;
        }
        public String getFrequency(){
            return frequency;
        }
        public Date getLastComplete(){
            return lastComplete;
        }
    }
}
