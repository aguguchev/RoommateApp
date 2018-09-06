package com.android.roommate.roommateapp.payments;



import java.util.Date;
import java.util.GregorianCalendar;

public class Payment {
    private String itemName;
    private Date datePaid;
    private boolean completed;
    private int price;
    private int _id;

    Payment(String desc, int inputPrice){
        itemName = desc;
        datePaid = null;
        completed = false;
        price = inputPrice;
    }

    void pay(){
        completed = true;
        datePaid = (new GregorianCalendar()).getTime();
    }

    @Override
    public String toString(){
        return itemName;
    }
    String getitemName(){
        return itemName;
    }
    Date paid(){
        return datePaid;
    }
    boolean isCompleted(){
        return completed;
    }
    int getPrice(){
        return price;
    }

    public void set_id(int _id) {
        this._id = _id;
    }


    public int get_id() {
        return _id;
    }

}
