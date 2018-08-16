package com.android.roommate.roommateapp.chores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.roommate.roommateapp.BuildConfig;
import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**	
 * Created by VengefulLimaBean on 3/19/2018.	
 */

class ChoresExpandableListAdapter extends BaseExpandableListAdapter {

    final private Context CONTEXT;
    private List<String> listTitles;
    private HashMap<String, List<Chore>> listDetail;
    final private ChoresDatabase CHORES_DB;
    private final String[] TITLE_ORDER;

    ChoresExpandableListAdapter(Context c){
        super();
        CONTEXT = c;
        CHORES_DB = ChoresDatabase.getInstance(c);
        if(!BuildConfig.DEBUG)
            TITLE_ORDER = c.getResources().getStringArray(R.array.chores_frequencies);
        else
            TITLE_ORDER = c.getResources().getStringArray(R.array.debug_chores_frequencies);

        refreshData();
    }

    public void addChore(String desc, String freq, int val){
        if(!listTitles.contains(freq))
            setNewFreq(freq);
        Chore c = CHORES_DB.addChore(desc, freq, val);
        listDetail.get(freq).add(c);
    }

    private void addChore(Chore c){
        if(!listTitles.contains(c.getFrequency()))
            setNewFreq(c.getFrequency());
        listDetail.get(c.getFrequency()).add(c);
    }

    public void completeChore(int group, int item){
        checkValidChore(group, item);
        Chore toComplete = listDetail.get(listTitles.get(group)).get(item);

        float oldLC = toComplete.getLastComplete().getTime();
        toComplete.complete();
        float newLC = toComplete.getLastComplete().getTime();
        Log.v("stddebug", newLC > oldLC ? "Last Complete Updated Succesfully" : "Last Complete Not Updated");

        CHORES_DB.editChore(toComplete.getID(), toComplete.getDescription(), toComplete.getFrequency(),
                toComplete.getLastComplete().getTime(), toComplete.getValue());
        CHORES_DB.logCompletion(toComplete);
    }

    public void deleteChore(int group, int item){
        checkValidChore(group, item);
        Chore toDelete = listDetail.get(listTitles.get(group)).get(item);
        CHORES_DB.deleteChore(toDelete.getID());
        listDetail.get(listTitles.get(group)).remove(item);
    }

    public void editChore(int group, int item, String desc, String freq, int val) throws Exception{
        checkValidChore(group, item);
        Chore toEdit = listDetail.get(listTitles.get(group)).get(item);
        toEdit.setDescription(desc);
        toEdit.setFrequency((freq));
        toEdit.setValue(val);
        CHORES_DB.editChore(toEdit.getID(), desc, freq, toEdit.getLastComplete().getTime(), val);
    }

    public void refreshData(){
       //clear previous data
        listTitles = new ArrayList<>();
        listDetail = new HashMap<>();

        //extract stored chores from db
        ArrayList<Chore> toBeAdded = new ArrayList<>();
        Set<String> freqsPresent = new HashSet<>();
        ChoresDatabase.ChoresCursor cc = CHORES_DB.getChores();
        if(cc != null && cc.getCount() > 0){
            do {
                freqsPresent.add(cc.getColChoresFreq());
                toBeAdded.add(new Chore(cc.getColChoresID(), cc.getColChoresLastComplete(),
                        cc.getColChoresDesc(), cc.getColChoresFreq(), cc.getColChoresVal()));
            } while(cc.moveToNext());
        }

        //fill listTitles in order with only populated categories
        for(String freq : TITLE_ORDER)
            if(freqsPresent.contains(freq)) {
                listTitles.add(freq);
                listDetail.put(freq, new ArrayList<Chore>());
            }

        //add all chores to listDetail
        for(Chore c : toBeAdded)
            addChore(c);

        notifyDataSetChanged();
    }

    private void setNewFreq(String freq){
        listTitles.add(freq);
        listDetail.put(freq, new ArrayList<Chore>());
    }

    private void checkValidChore(int group, int item){
        if(group < 0 || item < 0 || group > listTitles.size()
                || item > listDetail.get(listTitles.get(group)).size()){
            throw new RuntimeException("Invalid chore to edit");
        }
    }

    @Override
    public int getGroupCount() {        /////Done/////
        return listTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {        /////Done/////
        return listDetail.get(listTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {     /////Done/////
        return listDetail.get(listTitles.get(groupPosition));
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {      /////Done/////
        return listDetail.get(this.listTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {     /////Done/////
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {      /////Done/////
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {     /////Done/////
        return false;
    }

    @Override
    @SuppressLint("InflateParams")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {        /////Done/////
        final String titleText = listTitles.get(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.CONTEXT
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.list_group, null);
            else
                Log.d("NULL POINTER CAUGHT", "ChoresExpandableListAdapter: layoutInflater " +
                        "resolved to null");
        }
        TextView listTitleTextView;
        if(convertView != null) {
            listTitleTextView = convertView.findViewById(R.id.listTitle);
            listTitleTextView.setText(titleText);
        }
        else
            Log.d("NULL POINTER CAUGHT", "ChoresExpandableListAdapter: convertView " +
                "resolved to null");
        return convertView;
    }

    @Override
    @SuppressLint("InflateParams")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {      /////Done/////
        String mod = ((Chore) getChild(groupPosition, childPosition)).isCompleted()
                ? "(COMPLETE)" : "";
        final String expandedListText = getChild(groupPosition, childPosition).toString() + mod;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.CONTEXT
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (layoutInflater != null)
                convertView = layoutInflater.inflate(R.layout.list_item, null);
            else
                Log.d("NULL POINTER CAUGHT", "ChoresExpandableListAdapter: layoutInflater " +
                        "resolved to null");
        }
        TextView expandedListTextView;
        if (convertView != null){
            expandedListTextView = convertView.findViewById(R.id.expandedListItem);
            expandedListTextView.setText(expandedListText);
        }
        else
            Log.d("NULL POINTER CAUGHT", "ChoresExpandableListAdapter: convertView " +
                    "resolved to null");

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {        /////Done/////
        return true;        //COULD BE PROBLEMATIC
    }

    @Override
    public boolean isEmpty() {      /////Done/////
        return listTitles.size() == 0;
    }
}
