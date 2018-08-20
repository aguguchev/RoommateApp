package com.android.roommate.roommateapp.payments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import com.android.roommate.roommateapp.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.ArrayAdapter;

public class PaymentAdapter extends ArrayAdapter<Payment>{

    private ArrayList<Payment> unpaidPayments;

    public PaymentAdapter(Context context) {
        super(context, R.layout.payment_row);
        unpaidPayments = new ArrayList<Payment>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater paymentInflater = LayoutInflater.from(getContext());
        View paymentView = paymentInflater.inflate(R.layout.payment_row, parent, false);

        Payment payment = getItem(position);
        TextView payPriceView = paymentView.findViewById(R.id.price_display_textview);
        TextView payDescView = paymentView.findViewById(R.id.item_display_textview);

        payPriceView.setText("" + payment.getPrice());
        payDescView.setText("" + payment.getitemName());

        return paymentView;


    }

    @Override
    public int getCount() {
        return unpaidPayments.size();
    }
    @Override
    public Payment getItem(int pos) {
        return unpaidPayments.get(pos);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    public void addPayment(Payment newPayment){
        unpaidPayments.add(newPayment);
    }

    public void completePayment(int position){
        unpaidPayments.remove(position);
    }

}

