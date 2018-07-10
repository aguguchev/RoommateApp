package com.android.roommate.roommateapp.chores;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ChoresPagerAdapter extends FragmentStatePagerAdapter {
    ChoresPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        if(i == 0)
            fragment = new MainChoresFrag();
        else
            fragment = new ChoreReportFrag();
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}