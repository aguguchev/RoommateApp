package com.android.roommate.roommateapp.chores;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.android.roommate.roommateapp.R;

import static android.app.Activity.RESULT_OK;


public class MainChoresFrag extends Fragment {

    private ChoresExpandableListAdapter choresController;
    private ExpandableListView expListView;
    private ImageButton newChoreButton;
    private final int NEW_CHORE_REQUEST_CODE = 10;
    private final int SHOW_CHORE_REQUEST_CODE = 20;
    private int selectedChoreGroup;
    private int selectedChoreItem;
    private View myView;
    private final int SWIPE_MIN_DISTANCE = 120;
    private final int SWIPE_MAX_OFF_PATH = 250;
    private final int SWIPE_THRESHOLD_VELOCITY = 200;

    @SuppressWarnings("EmptyMethod")
    public void onCreate(Bundle state){
        super.onCreate(state);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle b){
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
                launchShowChoreActivity(i, i1);
                return false;
            }
        });
        for(int i = 0; i < choresController.getGroupCount(); i++)
            expListView.expandGroup(i);

        //attach support objects to chore button
        newChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCreateChoreActivity();
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
                v.performClick();
                return gesture.onTouchEvent(event);
            }
        });

        myView = view;
        return view;
    }
    private void testToast(String str){
        Log.d("debug", str);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //refreshes data if fragment switches
        if(isVisibleToUser && choresController != null) {
            choresController.refreshData();
        }
    }

    private void launchShowChoreActivity(int i, int i1){
        selectedChoreGroup = i;
        selectedChoreItem = i1;

        Chore toShow = (Chore)choresController.getChild(i, i1);
        Intent showChoreIntent = new Intent(myView.getContext(), ShowChoreActivity.class);
        showChoreIntent.putExtra(ShowChoreActivity.FIELD_DESC, toShow.getDescription());
        showChoreIntent.putExtra(ShowChoreActivity.FIELD_FREQ, toShow.getFrequency());
        showChoreIntent.putExtra(ShowChoreActivity.FIELD_VALUE, toShow.getValue());
        showChoreIntent.putExtra(ShowChoreActivity.FIELD_IS_COMPLETE, toShow.isCompleted());
        startActivityForResult(showChoreIntent, SHOW_CHORE_REQUEST_CODE);
    }

    private void launchCreateChoreActivity(){
        Intent newChoreIntent = new Intent(myView.getContext(), NewChoreActivity.class);
        startActivityForResult(newChoreIntent, NEW_CHORE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == NEW_CHORE_REQUEST_CODE && resultCode == RESULT_OK){
            String freq = data.getStringExtra(NewChoreActivity.FREQ);
            String desc = data.getStringExtra(NewChoreActivity.DESC);

            //CHORE VALUE DEFAULTS TO -1 TO THROW AN ERROR IF VALUE IS NOT FOUND IN INTENT
            //THIS IS ON PURPOSE, IF GETTING EXCEPTIONS FROM CHORE CLASS LOOK HERE

            int val = data.getIntExtra(NewChoreActivity.VALUE, -1);
            choresController.addChore(desc, freq, val);
            choresController.refreshData(); //TODO: [OPTIMIZATION] MAYBE INSERT CHORES PRECISELY INSTEAD OF CALLING BLANKET UPDATE?
        }
        else if(requestCode == SHOW_CHORE_REQUEST_CODE && resultCode == RESULT_OK){
            int res = data.getIntExtra(ShowChoreActivity.IS_COMPLETED, -1);
            Log.d("stddebug", "Returning to main chores frag with code " + res);
            switch (res) {
                case ShowChoreActivity.COMPLETE:
                    choresController.completeChore(selectedChoreGroup, selectedChoreItem);
                    break;
                case ShowChoreActivity.DELETE:
                    choresController.deleteChore(selectedChoreGroup, selectedChoreItem);
                    break;
                case ShowChoreActivity.EDIT:
                    String desc = data.getStringExtra(ShowChoreActivity.FIELD_DESC);
                    String freq = data.getStringExtra(ShowChoreActivity.FIELD_FREQ);

                    //SAME HERE WITH CHORE VALUE DEFAULT AS -1, IF GETTING EXCEPTIONS LOOK HERE

                    int val = data.getIntExtra(ShowChoreActivity.FIELD_VALUE, -1);
                    try {
                        choresController.editChore(selectedChoreGroup,
                                selectedChoreItem, desc, freq, val);
                    } catch (Exception e) {
                        Log.e("FATAL", e.toString());
                    }
                    break;
            }
            choresController.refreshData();

            //clears selection from fragment memory (?)
            selectedChoreItem = -1;
            selectedChoreGroup = -1;
        }
    }


}
