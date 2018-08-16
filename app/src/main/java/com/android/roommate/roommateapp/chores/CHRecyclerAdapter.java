package com.android.roommate.roommateapp.chores;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.roommate.roommateapp.R;

public class CHRecyclerAdapter extends RecyclerView.Adapter<CHRecyclerAdapter.ViewHolder>{
    final private ChoresDatabase CHISTORY_DB;
    private String[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView CH_TEXT;
        ViewHolder(TextView v) {
            super(v);
            CH_TEXT = v;
        }
    }

    CHRecyclerAdapter(Context context, long lowerBound) {
        CHISTORY_DB = ChoresDatabase.getInstance(context);
        refreshData(lowerBound);
    }

    public void refreshData(long lowerBound){
        ChoresDatabase.CHCursor c = CHISTORY_DB.getChoreHistory(lowerBound);
        mDataset = new String[c.getCount()];
        if(c.getCount() > 0){
            int i = 0;
            do{
                mDataset[i] = c.getColCHDesc();
                i++;
            }while(c.moveToNext());
            c.close();
        }
        //not ideal for updating list; adapter contains specialized notify methods while
        //notifyDataSetChanged should be used as a "last resort"
        notifyDataSetChanged();     //TODO: [OPTIMIZATION] IMPLEMENT REFRESH SYSTEM THAT CALLS SPECIALIZED NOTIFY FUNCTIONS
    }

    @Override
    @NonNull
    public CHRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                   int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.CH_TEXT.setText(mDataset[position]);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

