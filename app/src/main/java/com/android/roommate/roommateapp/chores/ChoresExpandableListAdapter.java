package com.android.roommate.roommateapp.chores;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChoresExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitles;
    private HashMap<String, List<Chore>> listDetail;
    private ChoresDataHandler choresData;
    private DataSetObserver dataSetObserver = null;

    public ChoresExpandableListAdapter(Context c){
        super();
        context = c;
        listTitles = new ArrayList<String>();
        listDetail = new HashMap<String, List<Chore>>();
        choresData = new ChoresDataHandler();
    }
    public ChoresExpandableListAdapter(Context c, List<String> titles,
                                       HashMap<String, List<Chore>> detail){
        super();
        context = c;
        listTitles = titles;
        listDetail = detail;
        choresData = new ChoresDataHandler();
    }

    public void addChore(String title, String desc){
        if(!listTitles.contains(title)){
            listTitles.add(title);
            listDetail.put(title, new ArrayList<Chore>());
        }
        listDetail.get(title).add(new Chore(title, desc));
    }

    public void completeChore(int group, int item){
        if(group < 0 || item < 0 || group > listTitles.size()
                || item > listDetail.get(listTitles.get(group)).size()){
            throw new RuntimeException("Invalid chore to complete");
        }
        Chore toComplete = listDetail.get(listTitles.get(group)).get(item);
        toComplete.complete();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {        /////Done/////
        final String titleText = listTitles.get(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(titleText);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {      /////Done/////
        String mod = ((Chore)getChild(groupPosition, childPosition)).isCompleted()
                ? "(COMPLETE)" : "";
        final String expandedListText = getChild(groupPosition, childPosition).toString() + mod;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
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
