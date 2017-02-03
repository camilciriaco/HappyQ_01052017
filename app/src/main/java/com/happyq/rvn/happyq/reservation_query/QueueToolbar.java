package com.happyq.rvn.happyq.reservation_query;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.happyq.rvn.happyq.Activitymain;
import com.happyq.rvn.happyq.R;

import java.util.Calendar;
import java.util.Date;


public class QueueToolbar extends AppCompatActivity {
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
                Intent openReserve_intent1 = new Intent(QueueToolbar.this, Now_Query.class);
                startActivity(openReserve_intent1);
            }
        });

        btreservedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open Reserve Date
                Intent openReserve_intent2 = new Intent(QueueToolbar.this, Date_Query.class);
                startActivity(openReserve_intent2);
            }
        });


        final Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbarLayout.setTitle("Queue Number");
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Context context = this;
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(context, R.color.coolgreen));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), Activitymain.class);
        startActivity(myIntent);
        return true;

    }


}


