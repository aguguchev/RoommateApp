package com.android.roommate.roommateapp.chores;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**	
 * Created by VengefulLimaBean on 3/19/2018.	
 */

public class ChoresExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitles;
    private HashMap<String, List<Chore>> listDetail;
    private DataSetObserver dataSetObserver = null;
    private ChoresDatabase choresDB;

    public ChoresExpandableListAdapter(Context c){
        super();
        context = c;
        listTitles = new ArrayList<String>();
        listDetail = new HashMap<String, List<Chore>>();
        choresDB = ChoresDatabase.getInstance(c);

        //fill list with stored chores
        ChoresDatabase.ChoresCursor cc = choresDB.getChores();
        Log.d("dbPersistence", cc.toString());
        Log.d("dbPersistence", cc.getCount() + " ");
        if(cc != null && cc.getCount() > 0){
            do {
                Log.d("dbPersistence", cc.hashCode() + ": Chore retrieved from db");
                addChore(new Chore(cc.getColChoresID(), cc.getColChoresLastComplete(),
                        cc.getColChoresDesc(), cc.getColChoresFreq(), cc.getColChoresVal()));
            } while(cc.moveToNext());
        }
    }
    public ChoresExpandableListAdapter(Context c, List<String> titles,
                                       HashMap<String, List<Chore>> detail){
        super();
        context = c;
        listTitles = titles;
        listDetail = detail;
    }

    public void addChore(String desc, String freq, int val){
        if(!listTitles.contains(freq))
            setNewFreq(freq);
        Chore c = choresDB.addChore(desc, freq, val);
        listDetail.get(freq).add(c);
    }

    public void addChore(Chore c){
        if(!listTitles.contains(c.getFrequency()))
            setNewFreq(c.getFrequency());
        listDetail.get(c.getFrequency()).add(c);
    }

    public void completeChore(int group, int item){
        checkValidChore(group, item);
        Chore toComplete = listDetail.get(listTitles.get(group)).get(item);
        toComplete.complete();
        choresDB.editChore(toComplete.getId(), toComplete.getDescription(), toComplete.getFrequency(),
                toComplete.getLastComplete().getTime(), toComplete.getValue());
        choresDB.logCompletion(toComplete);
    }

    public void deleteChore(int group, int item){
        checkValidChore(group, item);
        Chore toDelete = listDetail.get(listTitles.get(group)).get(item);
        choresDB.deleteChore(toDelete.getId());
        listDetail.get(listTitles.get(group)).remove(item);
    }

    public void editChore(int group, int item, String desc, String freq, int val) throws Exception{
        checkValidChore(group, item);
        Chore toEdit = listDetail.get(listTitles.get(group)).get(item);
        toEdit.setDescription(desc);
        toEdit.setFrequency((freq));
        toEdit.setValue(val);
        choresDB.editChore(toEdit.getId(), desc, freq, toEdit.getLastComplete().getTime(), val);
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
