package com.happyq.rvn.happyq.sigin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.data.DatabaseHandler;
import com.happyq.rvn.happyq.data.Databasehandlerr;
import com.happyq.rvn.happyq.data.SessionManager;
import com.happyq.rvn.happyq.data.AppConfig;
import com.happyq.rvn.happyq.data.AppController;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RVN on 12/7/2016.
 */

public class Register extends Activity implements View.OnClickListener {
    Button btRegister;
    TextView btCancel;
    EditText etUsername, etPwd, etConPwd, etEmail, etbirthday, etname, etinvitecode;
    RadioGroup etgender;
    RelativeLayout relativeLayout;
    //DatabaseHandler db;
    Databasehandlerr db;
    final Context con = this;
    private ProgressDialog mProgressDialog;
    private static final String TAG = Register.class.getSimpleName();
    // Database Version
    private static final int DATABASE_VERSION = 1;



    Context context= this;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;


    // Database Name
    private static final String DATABASE_NAME = "apprvndb";
    public SessionManager sessions;

    //gender
    RadioButton rbMale, rbFemale, radioSexButton;
    String userGender;
    //Calendar
    ImageView ib;
    private Calendar cal;
    private int day;
    private int month;
    private int year;

    //String
    String Sname;
    String Susername;
    String Semail;
    String Sbirthday;
    String Sgender;
    String Sinvitecode;
    String Spassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btRegister = (Button) findViewById(R.id.registerSubmitBtn);
        btCancel = (TextView) findViewById(R.id.registerCancelButton);
        etUsername = (EditText) findViewById(R.id.registrationUserName);
        etname = (EditText) findViewById(R.id.registrationName);
        etPwd = (EditText) findViewById(R.id.registrationPassword);
        etEmail = (EditText) findViewById(R.id.registrationEmail);
       etbirthday = (EditText) findViewById(R.id.formBirthDate);
        etinvitecode = (EditText) findViewById(R.id.registrationInvitecode);


        etgender = (RadioGroup) findViewById(R.id.rgGender);

        etConPwd = (EditText) findViewById(R.id.registrationConfirmPassword);


        //gender
        ib = (ImageView) findViewById(R.id.imageButton1);
        rbFemale = (RadioButton) findViewById(R.id.radioButton);
        rbMale = (RadioButton) findViewById(R.id.radioButton2);

        //Calendar
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        //gender

        sessions = new SessionManager(getApplicationContext());
        db = new Databasehandlerr(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                    //btnCapturePicture.setVisibility(View.VISIBLE);
                }
            });
            dialog.setNegativeButton(context.getString(Integer.parseInt("Cancel")), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    //btnCapturePicture.setVisibility(View.GONE);
                    //tvlat.setText("Please Turn on your Location");
                }
            });
            dialog.show();
        }


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent openRegister_intent = new Intent(Register.this, Login.class);
                startActivity(openRegister_intent);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                // get selected radio button from radioGroup
                int selectedId = etgender.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                radioSexButton = (RadioButton) findViewById(selectedId);

                Sname = etname.getText().toString().trim();
                 Susername = etUsername.getText().toString().trim();
                 Semail = etEmail.getText().toString().trim();
                 Sgender = radioSexButton.getText().toString();
                 Sbirthday = etbirthday.getText().toString().trim();
                 Sinvitecode = etinvitecode.getText().toString().trim();
                 Spassword = etPwd.getText().toString().trim();

                if (!Sname.isEmpty() && !Semail.isEmpty() && !Spassword.isEmpty() && !Susername.isEmpty() && !Spassword.isEmpty() && !Sgender.isEmpty()) {
                    registerUser(Susername, Sname, Semail, Sbirthday, Sgender, Sinvitecode, Spassword);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }


                /*
                String edusrneym = etUsername.getText().toString();

                String edemail = etEmail.getText().toString();
                String edmobile = etbirthday.getText().toString();
                String edpass = etPwd.getText().toString();
                String edConf = etConPwd.getText().toString();

                if (edusrneym.equals("") || edemail.equals("") || edmobile.equals("") || edpass.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill in the blank fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (edConf.equals(edpass)) {

                    db = new DatabaseHandler(con);
                    RegisterData reg = new RegisterData();

                    reg.setUsername(edusrneym);
                    reg.setEmailId(edemail);
                    reg.setMobNo(edmobile);
                    reg.setPassword(edpass);

                    db.addregister(reg);

                    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));

                } else {

                    Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_LONG).show();
                    etPwd.setText("");
                    etConPwd.setText("");
                }
            */



        });



    ib.setOnClickListener(this);
    }

    /*[Start]DATE PICKER*/
    protected Dialog onCreateDialog ( int id){
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            view.setMaxDate(System.currentTimeMillis());

            Calendar userAge = new GregorianCalendar(selectedYear, selectedMonth, selectedDay);
            Calendar minAdultAge = new GregorianCalendar();
            minAdultAge.add(Calendar.YEAR, -18);
            if (minAdultAge.before(userAge)) {
                Toast.makeText(getApplicationContext(), "Must be atleast 18 years old", Toast.LENGTH_LONG).show();
            } else {
                etbirthday.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
            }

        }
    };

    /*[End]DATE PICKER*/
    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String username,
                              final String name,
                              final String email,
                              final String birthday,
                              final String gender,
                              final String invitecode,
                              final String password
                              ) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Registering ....");
        mProgressDialog.setIndeterminate(true);
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();


                try {
                        db.setName(name);
                        db.setUsername(username);
                        db.setEmail(email);
                        db.setBirthday(birthday);
                        db.setGender(gender);
                        db.setInvitecode(invitecode);
                        db.setPassword(password);

                    Toast.makeText(getApplicationContext(), "User successfully registered. Try login now! Manual", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                    finish();
                    db.addUser(name, username, email, birthday, gender, invitecode, password);

                }catch (Exception e){
                    e.printStackTrace();
                }
                /*try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        //String uid = jObj.getString("uid");


                        JSONObject user = jObj.getJSONObject("user");
                        String username = jObj.getString("name");
                        String name = user.getString("username");
                        String email = user.getString("email");
                        String birthday = user.getString("gender");
                        String gender = user.getString("birthday");
                        String invitecode = user.getString("invitecode");
                        //String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(name, username, email, birthday, gender, invitecode);


                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(Register.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
            }   */

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("name", name);
                params.put("email", email);
                params.put("birthday", birthday);
                params.put("gender", gender);
                params.put("invitecode", invitecode);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }






    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent i = new Intent(this, Login.class);
            startActivity(i);

            return false;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_LONG).show();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

//    @Override
//    public void onClick(View v) {
//            showDialog(0);
//
//// Check which radio button was clicked
//            int checkRb = etgender.getCheckedRadioButtonId();
//            switch(checkRb) {
//                case R.id.radioButton:
//                    if (rbMale.isChecked()) {
//                        Sgender = "male";
//                        userGender = "male";
//                    }
//                    break;
//                case R.id.radioButton2:
//                    if (rbFemale.isChecked()) {
//                        userGender = "feMale";
//                        Sgender = "female";
//                    }
//                    break;
//            }
//
//    }

    @Override
    public void onClick(View v) {
    showDialog(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerInternetCheckReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void registerInternetCheckReceiver() {
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        registerReceiver(broadcastReceiver, internetFilter);
    }

    /**
     *  Runtime Broadcast receiver inner class to capture internet connectivity events
     */
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = getConnectivityStatusString(context);
            setSnackbarMessage(status,false);
        }
    };

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }
    private void setSnackbarMessage(String status,boolean showBar) {
        String internetStatus="";
        if(status.equalsIgnoreCase("Wifi enabled")||status.equalsIgnoreCase("Mobile data enabled")){
            internetStatus="Internet Connected";
        }else {
            internetStatus="No Internet Connection";
        }
        snackbar = Snackbar
                .make(relativeLayout, internetStatus, Snackbar.LENGTH_INDEFINITE)
                .setAction("close", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        //textView.setTextColor(Color.WHITE);
        if(internetStatus.equalsIgnoreCase("No Internet Connection")){
            if(internetConnected){
                snackbar.show();
                snackbar.setActionTextColor(Color.WHITE);
                textView.setTextColor(Color.RED);
                internetConnected=false;
                btRegister.setEnabled(false);
            }
        }   else{
            if(!internetConnected){
                internetConnected=true;
                snackbar.setActionTextColor(Color.WHITE);
                textView.setTextColor(Color.GREEN);
                snackbar.show();
                btRegister.setEnabled(true);
            }

        }
}


}

