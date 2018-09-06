package com.android.roommate.roommateapp.payments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Button;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.content.Intent;

import com.android.roommate.roommateapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.android.roommate.roommateapp.R;

public class PayMainActivity extends AppCompatActivity {

   private ListView payMainListView;
   private FloatingActionButton newPaymentButt;
   private PaymentAdapter payAdapter;
   private int selectedPayment;
   private PaymentsDatabase persistentPayments;

    final int NEW_PAYMENT_REQUEST_CODE = 30;
    final int SHOW_PAYMENT_REQUEST_CODE = 40;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_main_screen);

        payMainListView = findViewById(R.id.unpaid_list);
        newPaymentButt = findViewById(R.id.new_payment_butt);

        payAdapter = new PaymentAdapter(this);
        persistentPayments = new PaymentsDatabase(this, null, null, 0 );

        payMainListView.setAdapter(payAdapter);
        newPaymentButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewPayment();
            }
        });

        payMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPayment(position);
            }
        });

    }

    private void showPayment(int position){
        selectedPayment = position;

        Payment toShow = (Payment)payAdapter.getItem(selectedPayment);
        Intent showChoreIntent = new Intent(this, ShowPaymentActivity.class);
        showChoreIntent.putExtra("desc_data", toShow.getitemName());
        showChoreIntent.putExtra("price_data", toShow.getPrice() + "");
        startActivityForResult(showChoreIntent, SHOW_PAYMENT_REQUEST_CODE);
    }

    private void createNewPayment(){
        Intent newPayment = new Intent(this, NewPaymentActivity.class);
        startActivityForResult(newPayment, NEW_PAYMENT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == NEW_PAYMENT_REQUEST_CODE && resultCode == RESULT_OK){
            int price = data.getIntExtra(getResources().getString(R.string.payment_price_data_id), 0);
            String desc = data.getStringExtra(getResources().getString(R.string.payment_desc_data_id));
            payAdapter.addPayment(new Payment(desc, price));
            payAdapter.notifyDataSetChanged();
        }

        else if(requestCode == SHOW_PAYMENT_REQUEST_CODE && resultCode == RESULT_OK){
            payAdapter.completePayment(selectedPayment);
            payAdapter.notifyDataSetChanged();

        }
    }
    
}

