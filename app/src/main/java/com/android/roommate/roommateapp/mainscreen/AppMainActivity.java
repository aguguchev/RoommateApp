package com.android.roommate.roommateapp.mainscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.android.roommate.roommateapp.payments.PayMainActivity;
import com.android.roommate.roommateapp.R;
import com.android.roommate.roommateapp.schedule.ScheduleMainActivity;
import com.android.roommate.roommateapp.shoppinglist.ShoppingMainActivity;
import com.android.roommate.roommateapp.chores.ChoresMainActivity;

public class AppMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_screen);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToFunction(v);
            }
        };

        findViewById(R.id.chore_butt).setOnClickListener(clickListener);
        findViewById(R.id.pay_butt).setOnClickListener(clickListener);
        findViewById(R.id.sched_butt).setOnClickListener(clickListener);
        findViewById(R.id.shop_butt).setOnClickListener(clickListener);
    }

    //called by mainscreen buttons
    public void navToFunction(View view){
        int id = view.getId();
        Intent intent = null;
        if(id == R.id.chore_butt)
            intent = new Intent(this, ChoresMainActivity.class);
        else if(id == R.id.pay_butt)
            intent = new Intent(this, PayMainActivity.class);
        else if(id == R.id.sched_butt)
            intent = new Intent(this, ScheduleMainActivity.class);
        else if(id == R.id.shop_butt)
            intent = new Intent(this, ShoppingMainActivity.class);

        if(intent != null){
            startActivity(intent);
        }
    }
}
