package com.android.roommate.roommateapp.chores;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    //TODO: SWIPES GOING OVER PIECHART MEANT TO SWITCH FRAGS ONLY ROTATES CHART; FIX
    private PieChart chart;
    private ChoresDatabase choresDB;
    private Spinner intervalSelect;

    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.chore_report_frag, container, false);

        //grab spinner from layout and modify on select behavior
        intervalSelect = (Spinner)view.findViewById(R.id.cr_interval_spinner);
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

        //grab chart from layout and fill with data
        chart = (PieChart)view.findViewById(R.id.chore_report_chart);
        choresDB = ChoresDatabase.getInstance(getContext());
        refreshData();

        return view;
    }

    private void refreshData(){
        if(choresDB != null){
            ChoresDatabase.CHCursor chc = choresDB.getChoreHistory();

            //load all history items into hashmap to count by user
            HashMap<String, Integer> cCountByUser = new HashMap<>();
            if(chc != null && chc.getCount() > 0){
                do {
                    Log.d("dbPersistence", chc.hashCode() + ": CHistory item retrieved from db");
                    //TODO:IMPLEMENT USER_ID -> USER NAME LOOKUP
                    boolean isValid = isCompleteInInterval(chc.getColCHDateCompleted());
                    if(cCountByUser.containsKey(chc.getColCHUserID() + "") && isValid)
                       cCountByUser.put(chc.getColCHUserID() + "",
                               cCountByUser.get(chc.getColCHUserID() + "") + chc.getColCHValue());
                    else if(isValid)
                        cCountByUser.put(chc.getColCHUserID() + "", chc.getColCHValue());
                } while(chc.moveToNext());
            }

            // add hashmap items to piechart
            List<PieEntry> pieEntries = new ArrayList<PieEntry>();
            for(String key : cCountByUser.keySet())
                pieEntries.add(new PieEntry(cCountByUser.get(key), key));

            //initialize piechart
            PieDataSet pds = new PieDataSet(pieEntries, "Your Chore Report");
            PieData pd = new PieData(pds);
            chart.setData(pd);
            chart.invalidate();         //refresh
            //TODO: ADD FILLER GRAPHIC WHEN NO DATA IS AVAILABLE
        }
    }

    private boolean isCompleteInInterval(long completeDate){
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
                calendar.add(calendar.MONTH, -1);
                break;
            case(3):        //Last 3 Months
                calendar.add(calendar.MONTH, -3);
                break;
            case(4):        //All Time
                calendar.add(calendar.YEAR, -2018);         //if this app is still in use by 4036 then i'll be damned
                break;
        }
        if(calendar.getTime().getTime() < completeDate)
            return true;
        else
            return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshData();
    }
}
