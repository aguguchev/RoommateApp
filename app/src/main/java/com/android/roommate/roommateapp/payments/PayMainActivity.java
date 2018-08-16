package com.android.roommate.roommateapp.payments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;

public class PayMainActivity extends AppCompatActivity {

   private ListView payMainListView;
   private FloatingActionButton newPaymentButt;
   private ArrayList<Payment> unpaidPayments;
   private ArrayAdapter<Payment> adapter;

    final int NEW_PAYMENT_REQUEST_CODE = 10;
    final int SHOW_PAYMENT_REQUEST_CODE = 20;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_main_screen);

        payMainListView = findViewById(R.id.unpaid_list);
        newPaymentButt = findViewById(R.id.new_payment_butt);

        unpaidPayments =  new ArrayList<>();
        newPaymentButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPayment();
            }
        });
    }

    private void createNewPayment(){
        Intent newPayment = new Intent(this, NewPaymentActivity.class);
        startActivityForResult(newPayment, NEW_PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == NEW_PAYMENT_REQUEST_CODE && resultCode == RESULT_OK){
            //String title = data.getStringExtra(getResources().getString(R.string.payments_freq_data_id));
            //String desc = data.getStringExtra(getResources().getString(R.string.payments_desc_data_id));
            //paymentsController.addChore(title, desc);
            //paymentsController.notifyDataSetChanged();
        }
        else if(requestCode == SHOW_PAYMENT_REQUEST_CODE && resultCode == RESULT_OK){
            //paymentsController.completeChore(selectedChoreGroup, selectedChoreItem);
            //paymentsController.notifyDataSetChanged();
            //selectedChoreItem = -1;
            //selectedChoreGroup = -1;
        }
    }
    
}

