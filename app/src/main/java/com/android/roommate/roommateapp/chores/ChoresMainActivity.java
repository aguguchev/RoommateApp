package com.android.roommate.roommateapp.chores;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.android.roommate.roommateapp.R;

public class ChoresMainActivity extends FragmentActivity {
    private ChoresPagerAdapter pagerAdapter;
    private ViewPager vPager;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_chores_main_screen);

        //initialize pager
        pagerAdapter = new ChoresPagerAdapter(getSupportFragmentManager());
        vPager = findViewById(R.id.pager);
        vPager.setAdapter(pagerAdapter);
    }


}
