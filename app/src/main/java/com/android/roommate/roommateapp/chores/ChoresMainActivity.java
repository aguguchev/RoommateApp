package com.android.roommate.roommateapp.chores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VengefulLimaBean on 3/10/2018.
 */

public class ChoresMainActivity extends AppCompatActivity {

    ChoresExpandableListAdapter choresController;
    ExpandableListView expListView;
    ImageButton newChoreButton;
    int newChoreRequestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chores_main_screen);

        //assign all Views
        newChoreButton = (ImageButton)findViewById(R.id.new_chore_button);
        expListView = (ExpandableListView) findViewById(R.id.chores_exp_list);

        //attach support objects to expandable list
        choresController = new ChoresExpandableListAdapter(this);
        expListView.setAdapter(choresController);

        //attach support objects to chore button
        newChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChore();
            }
        });
        newChoreRequestCode = getResources().getInteger(R.integer.new_chore_res_code);
    }

    public void createNewChore(){
        Intent newChoreIntent = new Intent(this, NewChoreActivity.class);
        startActivityForResult(newChoreIntent, newChoreRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == newChoreRequestCode && resultCode == RESULT_OK){
            String title = data.getStringExtra(getResources().getString(R.string.chores_freq_data_id));
            String desc = data.getStringExtra(getResources().getString(R.string.chores_desc_data_id));
            choresController.addChore(title, desc);
            choresController.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

    }
}
