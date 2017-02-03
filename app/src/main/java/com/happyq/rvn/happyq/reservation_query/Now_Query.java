package com.happyq.rvn.happyq.reservation_query;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;

import java.util.Calendar;
import java.util.Date;


public class Now_Query extends AppCompatActivity {

    TextView tvDate;
    Date currentDate;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_query);

        calendar = Calendar.getInstance();
        tvDate = (TextView)findViewById(R.id.tvDate);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR) - 1900;
        currentDate = new Date(currentYear, currentMonth, currentDay);

        tvDate.setText(DateFormat.format("MMMM dd, yyyy", currentDate));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Reservation</font>"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openReserve_intent1 = new Intent(Now_Query.this, QueueToolbar.class);
                startActivity(openReserve_intent1);
            }
        });


    }

}
