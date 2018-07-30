package com.android.roommate.roommateapp.chores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.roommate.roommateapp.R;

/**
 * Created by VengefulLimaBean on 4/13/2018.
 */

public class ShowChoreActivity extends Activity {

    public static final String IS_COMPLETED= "ic";
    public static final String FIELD_DESC = "fDesc";
    public static final String FIELD_FREQ = "fFreq";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_IS_COMPLETE = "isComplete";
    public static final int COMPLETE = 0;
    public static final int DELETE = 1;
    public static final int EDIT = 2;

    private final int EDIT_CHORE_REQUEST_CODE = 8;
    private String description;
    private String frequency;
    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chore);

        //wire up buttons
        findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                deleteChore();
            }
        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cancelChore();
            }
        });
        findViewById(R.id.edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editChore();
            }
        });
        Button completeButton = findViewById(R.id.complete_button);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeChore();
            }
        });
        completeButton.setEnabled(getIntent().getBooleanExtra(FIELD_IS_COMPLETE, false));

        //set textviews
        description = getIntent().getStringExtra(FIELD_DESC);
        frequency = getIntent().getStringExtra(FIELD_FREQ);
        value = getIntent().getIntExtra(FIELD_VALUE, -1);

        ((TextView)findViewById(R.id.desc_textview)).setText(description);
        ((TextView)findViewById(R.id.freq_text_view)).setText(frequency);
        ((TextView)findViewById(R.id.chore_val_text_view)).setText(value + "");
    }

    private void cancelChore(){
        Intent cancelIntent = new Intent();
        setResult(RESULT_CANCELED, cancelIntent);
        finish();
    }

    private void completeChore(){
        Intent completeIntent = new Intent();
        completeIntent.putExtra(IS_COMPLETED, 0);
        setResult(RESULT_OK, completeIntent);
        finish();
    }

    private void deleteChore(){
        Intent deleteIntent = new Intent();
        deleteIntent.putExtra(IS_COMPLETED,1);
        setResult(RESULT_OK, deleteIntent);
        finish();
    }

    private void editChore(){
        Intent i = new Intent(this, NewChoreActivity.class);
        i.putExtra(NewChoreActivity.IS_EDIT, true);
        i.putExtra(NewChoreActivity.DESC, description);
        i.putExtra(NewChoreActivity.FREQ, frequency);
        i.putExtra(NewChoreActivity.VALUE, value);
        startActivityForResult(i, EDIT_CHORE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == EDIT_CHORE_REQUEST_CODE && resultCode == RESULT_OK){
            Intent returnWithEdits = new Intent();
            returnWithEdits.putExtra(IS_COMPLETED, EDIT);
            returnWithEdits.putExtra(FIELD_DESC, data.getStringExtra(NewChoreActivity.DESC));
            returnWithEdits.putExtra(FIELD_FREQ, data.getStringExtra(NewChoreActivity.FREQ));
            returnWithEdits.putExtra(FIELD_VALUE, data.getIntExtra(NewChoreActivity.VALUE,
                    -1));
            setResult(RESULT_OK, returnWithEdits);
        }
        else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
