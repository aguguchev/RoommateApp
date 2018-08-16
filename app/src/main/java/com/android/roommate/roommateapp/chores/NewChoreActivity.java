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

import com.android.roommate.roommateapp.BuildConfig;
import com.android.roommate.roommateapp.R;

import java.util.Locale;

/**
 * Created by VengefulLimaBean on 3/21/2018.
 */

//TODO: MAKE HINT RANDOMLY POPULATE A LA ANDROID REMINDER APP
public class NewChoreActivity extends AppCompatActivity {

    private EditText descriptionInput;
    private Spinner freqSpinner;
    private EditText valInput;
    private Handler editTextFocuser;

    public static final String IS_EDIT = "isEdit";
    public static final String DESC = "desc";
    public static final String FREQ = "freq";
    public static final String VALUE = "val";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////////////getSupportActionBar().hide();
        setContentView(R.layout.activity_new_chore);

        //set view fields
        descriptionInput = findViewById(R.id.new_chore_desc_input);
        freqSpinner = findViewById(R.id.new_chore_frequency_spinner);
        valInput = findViewById(R.id.new_chore_val_input);

        //set adapter and initialize values for spinner
        ArrayAdapter<CharSequence> adapter;
        if(!BuildConfig.DEBUG){
            adapter = ArrayAdapter.createFromResource(this, R.array.chores_frequencies,
                    android.R.layout.simple_spinner_item);
        }
        else{
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.debug_chores_frequencies, android.R.layout.simple_spinner_item);
        }
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
        FrameLayout fLayout = findViewById(R.id.desc_inputbox);
        fLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextFocuser.postDelayed(focuser, 150);
            }
        });

        //check if meant for edit and if so populates fields
        Intent intent = getIntent();
        if(intent.getBooleanExtra(IS_EDIT, false)){
            descriptionInput.setText(intent.getStringExtra(DESC));
            setSpinnerSelection(intent.getStringExtra(FREQ));
            valInput.setText(String.format(Locale.getDefault(), "%d",
                    intent.getIntExtra(VALUE, -1)));
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        descriptionInput.setFocusable(true);
        descriptionInput.requestFocus();
    }

    private void cancelNewChore(){
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        finish();
    }

    private void saveNewChore(){
        Intent resIntent = new Intent();
        //Toast.makeText(this, descriptionInput.getEditableText(), Toast.LENGTH_LONG);
        resIntent.putExtra(DESC, descriptionInput.getEditableText().toString());
        resIntent.putExtra(FREQ, freqSpinner.getSelectedItem().toString());
        resIntent.putExtra(VALUE, Integer.parseInt(valInput.getEditableText().toString()));
        setResult(RESULT_OK, resIntent);
        finish();
    }

    private void setSpinnerSelection(String freq){
        String[] temp;
        if(!BuildConfig.DEBUG)
            temp = getResources().getStringArray(R.array.chores_frequencies);
        else
            temp = getResources().getStringArray(R.array.debug_chores_frequencies);
        for(int i = 0; i < temp.length; i++)
            if(temp[i].equals(freq))
                freqSpinner.setSelection(i);
    }
}
