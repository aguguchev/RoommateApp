package com.android.roommate.roommateapp.chores;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.android.roommate.roommateapp.R;

public class ChoresMainActivity extends FragmentActivity {
    final String MAINFRAG_TAG = "mainfrag";
    private float x1, x2;
    static final int MIN_DISTANCE = 150;
    ChoresPagerAdapter pagerAdapter;
    ViewPager vPager;

    @Override
    public void onCreate(Bundle state){
        super.onCreate(state);
        setContentView(R.layout.activity_chores_main_screen);

        //initialize pager
        pagerAdapter = new ChoresPagerAdapter(getSupportFragmentManager());
        vPager = (ViewPager) findViewById(R.id.pager);
        vPager.setAdapter(pagerAdapter);
    }


}
