package com.happyq.rvn.happyq.reservation_query;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;

import java.util.Calendar;
import java.util.Date;


public class Now_Query extends AppCompatActivity {

    TextView tvDate, _tvbranchname, _tvquenumberfinal, _tv_queue;
    Date currentDate;
    Calendar calendar;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_query);

        calendar = Calendar.getInstance();
        tvDate = (TextView)findViewById(R.id.tvDate);
        _tvbranchname = (TextView)findViewById(R.id.tbranchname);
        _tvquenumberfinal = (TextView)findViewById(R.id.tvnumQueue);
        _tv_queue = (TextView)findViewById(R.id.tqueue);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR) - 1900;
        currentDate = new Date(currentYear, currentMonth, currentDay);

        tvDate.setText(DateFormat.format("MMMM dd, yyyy", currentDate));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Reservation</font>"));
        displaytickets();
        doTheAutoRefresh();
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent x = new Intent(Queue_query.this, Activity_main_tablayout.class);
//                startActivity(x);
//            }
//        });



    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);

    }



    public void displaytickets() {
        String finalbranchname = getIntent().getStringExtra("fbranchname");
        _tvbranchname.setText("");
        _tvbranchname.setText(finalbranchname);
        String finalqueuename = getIntent().getStringExtra("fqueuename");
        _tv_queue.setText("");
        _tv_queue.setText(finalqueuename);
        String number = getIntent().getStringExtra("fqueuenumber");
        _tvquenumberfinal.setText("");
        _tvquenumberfinal.setText(number);
    }


    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ProgressDialog pdLoading = new ProgressDialog(context);
                // Write code for your refresh logic
                //pdLoading.dismiss();
                displaytickets();
                doTheAutoRefresh();
            }
        }, 5000);
    }

}


