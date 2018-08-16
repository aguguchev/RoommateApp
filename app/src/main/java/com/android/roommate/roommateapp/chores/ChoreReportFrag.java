package com.android.roommate.roommateapp.chores;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.roommate.roommateapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ChoreReportFrag extends Fragment {
    private PieChart chart;
    private ChoresDatabase choresDB;
    private Spinner intervalSelect;
    private RecyclerView chList;

    @SuppressWarnings("EmptyMethod")
    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.chore_report_frag, container, false);
        choresDB = ChoresDatabase.getInstance(getContext());

        //grab spinner from layout and modify on select behavior
        intervalSelect = view.findViewById(R.id.cr_interval_spinner);
        intervalSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                refreshData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });

        //grab and set up recyclerview
        chList = view.findViewById((R.id.chistory_list));
        chList.setHasFixedSize(true);
        chList.setLayoutManager(new LinearLayoutManager(getContext()));
        chList.setAdapter(new CHRecyclerAdapter(getContext(), getStartOfInterval()));

        //grab chart and catalog from layout and fill with data
        chart = view.findViewById(R.id.chore_report_chart);

        chart.setTouchEnabled(false);
        chart.setCenterText("Your Chore\nReport");
        refreshData();

        return view;
    }

    private void refreshData(){
        if(chList != null && chList.getAdapter() != null)
            ((CHRecyclerAdapter)chList.getAdapter()).refreshData(getStartOfInterval());
        if(choresDB != null){
            ChoresDatabase.CHCursor chc = choresDB.getChoreHistory();

            //load all history items into hashmap to count by user
            HashMap<String, Integer> cCountByUser = new HashMap<>();
            if(chc != null && chc.getCount() > 0){
                do {
                    Log.d("dbPersistence", chc.hashCode() + ": CHistory item retrieved from db");
                    //TODO:IMPLEMENT USER_ID -> USER NAME LOOKUP
                    boolean isValid = isCompleteInInterval(chc.getColCHDateCompleted());
                    if(isValid) {
                        //add to list adapter
                       // ((CHRecyclerAdapter)chList.getAdapter()).addEntry(
                            //    choresDB.getChoreByID(chc.getColCHChoreID()));

                        //add to hashmap for piechart data
                        if (cCountByUser.containsKey(chc.getColCHUserID() + ""))
                            cCountByUser.put(chc.getColCHUserID() + "",
                                    cCountByUser.get(chc.getColCHUserID() + "") + chc.getColCHValue());
                        else
                            cCountByUser.put(chc.getColCHUserID() + "", chc.getColCHValue());
                    }
                } while(chc.moveToNext());
            }

            // add hashmap items to piechart
            List<PieEntry> pieEntries = new ArrayList<>();
            for(String key : cCountByUser.keySet())
                pieEntries.add(new PieEntry(cCountByUser.get(key), key));

            //initialize piechart
            PieDataSet pds = new PieDataSet(pieEntries, "Users");
            PieData pd = new PieData(pds);
            chart.setData(pd);
            chart.invalidate();         //refresh
            //TODO: ADD FILLER GRAPHIC WHEN NO DATA IS AVAILABLE

            //fill history catalog

        }
    }

    private boolean isCompleteInInterval(long completeDate){
        return getStartOfInterval() < completeDate;
    }

    private long getStartOfInterval(){
        Calendar calendar = new GregorianCalendar();
        calendar.setLenient(true);
        switch(intervalSelect.getSelectedItemPosition()){
            case(0):        //Last 7 Days
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                break;
            case(1):        //Last 2 Weeks
                calendar.add(Calendar.WEEK_OF_YEAR, -2);
                break;
            case(2):        //Last Month
                calendar.add(Calendar.MONTH, -1);
                break;
            case(3):        //Last 3 Months
                calendar.add(Calendar.MONTH, -3);
                break;
            case(4):        //All Time
                calendar.add(Calendar.YEAR, -2018);         //if this app is still in use by 4036 then i'll be damned
                break;
        }
        return calendar.getTimeInMillis();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //refreshes data if fragment switches
        if(isVisibleToUser) {
            refreshData();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshData();
    }
}
