package com.happyq.rvn.happyq.reservation_query;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.happyq.rvn.happyq.R;

import java.util.Calendar;
import java.util.Date;


public class QueueNumber extends AppCompatActivity {

    TextView tv_date, tv_date2;
    Date currentDate;
    Calendar calendar;
    Button btreservenow, btreservedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_reservation);
        calendar = Calendar.getInstance();
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_date2 = (TextView)findViewById(R.id.tv_date2);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR) - 1900;
        currentDate = new Date(currentYear, currentMonth, currentDay);

        tv_date.setText(DateFormat.format("MMMM dd, yyyy", currentDate));
        tv_date2.setText(DateFormat.format("MMMM dd, yyyy", currentDate));


        btreservenow = (Button) findViewById(R.id.bt_rnow);
        btreservedate = (Button) findViewById(R.id.bt_rdate);

        btreservenow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open Reserve Now
                Intent openReserve_intent1 = new Intent(QueueNumber.this, Now_Query.class);
                startActivity(openReserve_intent1);
            }
        });

        btreservedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open Reserve Date
                Intent openReserve_intent2 = new Intent(QueueNumber.this, Date_Query.class);
                startActivity(openReserve_intent2);
            }
        });

    }}
