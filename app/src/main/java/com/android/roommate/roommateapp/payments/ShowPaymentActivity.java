package com.android.roommate.roommateapp.payments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.roommate.roommateapp.R;

public class ShowPaymentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_payment);

        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                completePayment();
            }
        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cancelPayment();
            }
        });

        ((TextView)findViewById(R.id.desc_textview)).setText(getIntent()
                .getStringExtra("desc_data"));
        ((TextView)findViewById(R.id.price_text_view)).setText(getIntent().
                getStringExtra("price_data"));
    }

    private void cancelPayment(){
        Intent cancelIntent = new Intent();
        setResult(RESULT_CANCELED, cancelIntent);
        finish();
    }

    private void completePayment(){
        Intent completeIntent = new Intent();
        setResult(RESULT_OK, completeIntent);
        finish();
    }
}
