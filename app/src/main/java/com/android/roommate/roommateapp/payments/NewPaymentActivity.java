package com.android.roommate.roommateapp.payments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.android.roommate.roommateapp.R;

public class NewPaymentActivity extends AppCompatActivity{

    private Button saveButton;
    private Button cancelButton;
    private EditText descriptionInput;
    private EditText numInput;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ///////////////////////////////////////////////////////////////getSupportActionBar().hide();
        setContentView(R.layout.activity_new_payment);
        // Add input of data
        saveButton = findViewById(R.id.save_button_payment);
        cancelButton = findViewById(R.id.cancel_button_payment);
        descriptionInput = findViewById(R.id.new_payment_desc_input);
        numInput = findViewById(R.id.new_payment_price_input);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewPayment();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelNewPayment();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();

    }

    public void cancelNewPayment(){
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        finish();
    }

    public void saveNewPayment(){
        Intent resIntent = new Intent();
        resIntent.putExtra((String)getResources().getText(R.string.chores_desc_data_id),
               descriptionInput.getEditableText().toString());
        resIntent.putExtra((String)getResources().getText(R.string.payment_price_data_id),
                Integer.parseInt(numInput.getEditableText().toString()));
        setResult(RESULT_OK, resIntent);
        finish();
    }

}
