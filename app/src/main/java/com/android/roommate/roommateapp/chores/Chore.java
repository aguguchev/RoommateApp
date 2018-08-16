package com.android.roommate.roommateapp.chores;
//TODO: USE LINT INSPECTOR TO CLEAN UP CODE
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class Chore {
    private String description;
    private String frequency;
    private Date lastComplete;
    private boolean completed;
    final private int ID;
    private int value;

    Chore(int id, long lC, String desc, String freq, int val){
        description = desc;
        frequency = freq;
        lastComplete = new Date(lC);
        value = val;
        completed = isCompleted(lastComplete, freq);
        ID = id;
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
        switch(freq) {
            case "Daily":
                c.add(Calendar.DAY_OF_YEAR, -1);
                break;
            case "Weekly":
                c.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case "Month":
                c.add(Calendar.MONTH, -1);
                break;
            default:
                c.add(Calendar.SECOND, -5);
                break;
        }
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
    int getID() {return ID;}
    boolean isCompleted(){
        return completed;
    }
    int getValue() {return value;}
    void setValue(int v) throws Exception{
        if(v < 0){
            throw new Exception("Chore value must be greater than or equal to zero");
        }
        value = v;
    }
    void setDescription(String d){ description = d;}
    void setFrequency(String f){ frequency = f;}

}