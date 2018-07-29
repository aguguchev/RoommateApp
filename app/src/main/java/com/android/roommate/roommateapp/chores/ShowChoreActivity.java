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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_chore);

        //wire up buttons
        findViewById(R.id.complete_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                completeChore();
            }
        });
        findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                cancelChore();
            }
        });

        //set textviews
        ((TextView)findViewById(R.id.desc_textview)).setText(getIntent()
                .getStringExtra("description"));
        ((TextView)findViewById(R.id.freq_text_view)).setText(getIntent().
                getStringExtra("frequency"));
    }

    private void cancelChore(){
        Intent cancelIntent = new Intent();
        setResult(RESULT_CANCELED, cancelIntent);
        finish();
    }

    private void completeChore(){
        Intent completeIntent = new Intent();
        setResult(RESULT_OK, completeIntent);
        finish();
    }
}
