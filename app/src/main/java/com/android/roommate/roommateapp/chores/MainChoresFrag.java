package com.android.roommate.roommateapp.chores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.roommate.roommateapp.R;

import static android.app.Activity.RESULT_OK;


public class MainChoresFrag extends Fragment {

    ChoresExpandableListAdapter choresController;
    ExpandableListView expListView;
    ImageButton newChoreButton;
    final int NEW_CHORE_REQUEST_CODE = 10;
    final int SHOW_CHORE_REQUEST_CODE = 20;
    private int selectedChoreGroup;
    private int selectedChoreItem;
    private View myView;
    final int SWIPE_MIN_DISTANCE = 120;
    final int SWIPE_MAX_OFF_PATH = 250;
    final int SWIPE_THRESHOLD_VELOCITY = 200;

    public void onCreate(Bundle state){
        super.onCreate(state);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle b){
        View view = inflater.inflate(R.layout.chores_frag, container, false);

        //assign all Views
        newChoreButton = view.findViewById(R.id.new_chore_button);
        expListView = view.findViewById(R.id.chores_exp_list);

        //attach support objects to expandable list
        choresController = new ChoresExpandableListAdapter(view.getContext());
        expListView.setAdapter(choresController);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                showChore(i, i1, l);
                return false;
            }
        });

        //attach support objects to chore button
        newChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChore();
            }
        });

        //creates gesture detector to listen to swipes
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        super.onDown(e);
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                testToast("right to left");
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                testToast("left to left");
                            }
                        } catch (Exception e) {
                            // nothing
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });

        myView = view;
        return view;
    }
    private void testToast(String str){
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
        Log.d("debug", str);
    }

    private void showChore(int i, int i1, long l){
        selectedChoreGroup = i;
        selectedChoreItem = i1;

        Chore toShow = (Chore)choresController.getChild(i, i1);
        Intent showChoreIntent = new Intent(myView.getContext(), ShowChoreActivity.class);
        showChoreIntent.putExtra("description", toShow.getDescription());
        showChoreIntent.putExtra("frequency", toShow.getFrequency());
        startActivityForResult(showChoreIntent, SHOW_CHORE_REQUEST_CODE);
    }

    public void createNewChore(){
        Intent newChoreIntent = new Intent(myView.getContext(), NewChoreActivity.class);
        startActivityForResult(newChoreIntent, NEW_CHORE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == NEW_CHORE_REQUEST_CODE && resultCode == RESULT_OK){
            String title = data.getStringExtra(getResources().getString(R.string.chores_freq_data_id));
            String desc = data.getStringExtra(getResources().getString(R.string.chores_desc_data_id));
            choresController.addChore(title, desc);
            choresController.notifyDataSetChanged();
        }
        else if(requestCode == SHOW_CHORE_REQUEST_CODE && resultCode == RESULT_OK){
            choresController.completeChore(selectedChoreGroup, selectedChoreItem);
            choresController.notifyDataSetChanged();
            selectedChoreItem = -1;
            selectedChoreGroup = -1;
        }
    }


}
