package com.android.roommate.roommateapp.chores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.roommate.roommateapp.R;

public class ChoreReportFrag extends Fragment {

    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.chore_report_frag, container, false);

        return view;
    }
}
