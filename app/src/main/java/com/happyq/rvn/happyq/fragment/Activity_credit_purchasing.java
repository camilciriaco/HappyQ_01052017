package com.happyq.rvn.happyq.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;

/**
 * Created by RVN on 2/9/2017.
 */

public class Activity_credit_purchasing extends AppCompatActivity {
    Spinner spinnerDropDown;
    String[] credit = {
            "Visa",
            "MasterCard",
            "Debit",
            "Credit",
            "7/11",

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_purchasing);


        spinnerDropDown =(Spinner)findViewById(R.id.spinner1);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,credit);

        spinnerDropDown.setAdapter(adapter);

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                int sid=spinnerDropDown.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "You have selected : " + credit[sid],
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
    }
