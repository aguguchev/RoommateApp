package com.android.roommate.roommateapp.chores;

import android.content.Context;
import android.widget.RemoteViews;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by VengefulLimaBean on 3/10/2018.
 */

public class ChoresDataHandler {
    private List<Chore> choreList;
    static Chore toBeAdded = null;

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

        choreList.add(new Chore(desc, freq));
    }

    public List<Chore> getChoresList(){
        return choreList;
    }
}
