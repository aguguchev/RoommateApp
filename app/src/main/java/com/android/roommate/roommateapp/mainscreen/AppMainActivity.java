package com.android.roommate.roommateapp.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.roommate.roommateapp.R;
import com.android.roommate.roommateapp.chores.ChoresMainActivity;
import com.android.roommate.roommateapp.payments.PayMainActivity;
import com.android.roommate.roommateapp.schedule.ScheduleMainActivity;
import com.android.roommate.roommateapp.shoppinglist.ShoppingMainActivity;

public class AppMainActivity extends AppCompatActivity {

    private DrawerLayout navDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_main_screen);

        //creates OnClickListener for all 4 buttons and assigns it
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navToFunction(v);
            }
        };

        findViewById(R.id.chore_butt).setOnClickListener(clickListener);
        findViewById(R.id.pay_butt).setOnClickListener(clickListener);
        findViewById(R.id.sched_butt).setOnClickListener(clickListener);
        findViewById(R.id.shop_butt).setOnClickListener(clickListener);

        //gets navdrawer layout and view
        NavigationView navigationView = findViewById(R.id.main_nav_view);
        navDrawerLayout = findViewById(R.id.main_drawer_layout);

        //assigns listener for drawer items
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                navToFunction(item);
                return true;
            }
        });

        //sets toolbar and configures drawer button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.nav_menu_black_24dp);

    }

    //called by mainscreen buttons
    private void navToFunction(View view){
        int id = view.getId();
        Intent intent = null;
        switch (id) {
            case R.id.chore_butt:
                intent = new Intent(this, ChoresMainActivity.class);
                break;
            case R.id.pay_butt:
                intent = new Intent(this, PayMainActivity.class);
                break;
            case R.id.sched_butt:
                intent = new Intent(this, ScheduleMainActivity.class);
                break;
            case R.id.shop_butt:
                intent = new Intent(this, ShoppingMainActivity.class);
                break;
        }

        if(intent != null){
            startActivity(intent);
        }
    }

    private void navToFunction(MenuItem item){
        item.setChecked(true);
        navDrawerLayout.closeDrawers();

        int id = item.getItemId();
        Intent intent = null;
        switch (id) {
            case R.id.chores_navdrawer_item:
                intent = new Intent(this, ChoresMainActivity.class);
                break;
            case R.id.payments_navdrawer_item:
                intent = new Intent(this, PayMainActivity.class);
                break;
            case R.id.schedule_navdrawer_item:
                intent = new Intent(this, ScheduleMainActivity.class);
                break;
            case R.id.shopping_navdrawer_item:
                intent = new Intent(this, ShoppingMainActivity.class);
                break;
        }

        if(intent != null){
            startActivity(intent);
        }
    }

    //this wires the nav drawer menu button to the actual view
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
