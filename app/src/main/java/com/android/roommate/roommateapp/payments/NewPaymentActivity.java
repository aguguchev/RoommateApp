package com.android.roommate.roommateapp.payments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.android.roommate.roommateapp.R;

public class NewPaymentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////////////getSupportActionBar().hide();
        setContentView(R.layout.activity_new_payment);


    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void cancelNewPayment(){

    }

    public void saveNewPayment(){

    }

}
