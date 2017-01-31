package com.happyq.rvn.happyq.reservation_query;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;

import java.util.Calendar;
import java.util.Date;


public class Date_Query extends AppCompatActivity {

    DatePicker datePicker;
    TextView textView,tvDate;
    Date currentDate,selectedDate;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_date_query);

            textView = (TextView)findViewById(R.id.textView1);
            tvDate = (TextView)findViewById(R.id.tvDate);
            datePicker = (DatePicker)findViewById(R.id.datePicker1);
            calendar = Calendar.getInstance();

            Button button = (Button)findViewById(R.id.btOK);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                    int currentMonth = calendar.get(Calendar.MONTH);
                    int currentYear = calendar.get(Calendar.YEAR) - 1900;
                    currentDate = new Date(currentYear, currentMonth, currentDay);

                    int selectedYear = datePicker.getYear();
                    int selectedDay = datePicker.getDayOfMonth();
                    int selectedMonth = datePicker.getMonth();
                    selectedDate = new Date(selectedYear - 1900, selectedMonth, selectedDay);

                    if(selectedDate.compareTo(currentDate)<1){
                        Toast.makeText(getApplicationContext(), "Cannot set previous or current date.", Toast.LENGTH_SHORT).show();
                        tvDate.setText("Date not Selected");
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Selected date is " + DateFormat.format("MMM/dd/yyyy", selectedDate), Toast.LENGTH_SHORT).show();
                        tvDate.setText(DateFormat.format("MMMM dd, yyyy", selectedDate)); //Output:Apr/07/2014
                    }
                }
            });
        } catch (Exception e) {
            e.toString();
        }

    }

}
