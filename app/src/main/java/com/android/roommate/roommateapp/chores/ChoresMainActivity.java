package com.android.roommate.roommateapp.chores;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.roommate.roommateapp.R;

/**
 * Created by VengefulLimaBean on 3/10/2018.
 */

public class ChoresMainActivity extends AppCompatActivity {

    ChoresExpandableListAdapter choresController;
    ExpandableListView expListView;
    ImageButton newChoreButton;
    final int NEW_CHORE_REQUEST_CODE = 10;
    final int SHOW_CHORE_REQUEST_CODE = 20;
    private int selectedChoreGroup;
    private int selectedChoreItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores_main_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //assign all Views
        newChoreButton = findViewById(R.id.new_chore_button);
        expListView = findViewById(R.id.chores_exp_list);

        //attach support objects to expandable list
        choresController = new ChoresExpandableListAdapter(this);
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
    }

    private void showChore(int i, int i1, long l){
        selectedChoreGroup = i;
        selectedChoreItem = i1;

        Chore toShow = (Chore)choresController.getChild(i, i1);
        Intent showChoreIntent = new Intent(this, ShowChoreActivity.class);
        showChoreIntent.putExtra("description", toShow.getDescription());
        showChoreIntent.putExtra("frequency", toShow.getFrequency());
        startActivityForResult(showChoreIntent, SHOW_CHORE_REQUEST_CODE);
    }

    public void createNewChore(){
        Intent newChoreIntent = new Intent(this, NewChoreActivity.class);
        startActivityForResult(newChoreIntent, NEW_CHORE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
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

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }
}
