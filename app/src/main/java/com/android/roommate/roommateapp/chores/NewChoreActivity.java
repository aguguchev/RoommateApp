package com.android.roommate.roommateapp.chores;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.android.roommate.roommateapp.R;

/**
 * Created by VengefulLimaBean on 3/21/2018.
 */

public class NewChoreActivity extends AppCompatActivity {

    EditText descriptionInput;
    Spinner freqSpinner;
    String spinnerSelection;
    private Handler editTextFocuser;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////////////getSupportActionBar().hide();
        setContentView(R.layout.activity_new_chore);

        //set view fields
        descriptionInput = (EditText)findViewById(R.id.new_chore_desc_input);
        freqSpinner = (Spinner)findViewById(R.id.new_chore_frequency_spinner);

        //set adapter and initialize values for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chores_frequencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        freqSpinner.setAdapter(adapter);

        //set listener for save and cancel buttons
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNewChore();
            }
        });
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewChore();
            }
        });

        //auto focus on edittext and show keyboard
        editTextFocuser = new Handler();
        final Runnable focuser = new Runnable() {
            @Override
            public void run() {
                descriptionInput.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN , 0, 0, 0));
                descriptionInput.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                        SystemClock.uptimeMillis(), MotionEvent.ACTION_UP , 0, 0, 0));
            }
        };
        editTextFocuser.postDelayed(focuser, 150);

        //set frame layout to redirect clicks to edittext
        FrameLayout fLayout = (FrameLayout)findViewById(R.id.desc_inputbox);
        fLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFocuser.postDelayed(focuser, 150);
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        descriptionInput.setFocusable(true);
        descriptionInput.requestFocus();
    }

    public void cancelNewChore(){
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        finish();
    }

    public void saveNewChore(){
        Intent resIntent = new Intent();
        //Toast.makeText(this, descriptionInput.getEditableText(), Toast.LENGTH_LONG);
        resIntent.putExtra((String)getResources().getText(R.string.chores_desc_data_id),
                descriptionInput.getEditableText().toString());
        resIntent.putExtra((String)getResources().getText(R.string.chores_freq_data_id),
                freqSpinner.getSelectedItem().toString());
        setResult(RESULT_OK, resIntent);
        finish();
    }
}
