package com.happyq.rvn.happyq.reservation_query;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
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


    }

}
