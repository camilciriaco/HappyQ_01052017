package com.happyq.rvn.happyq.reservation_query;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.happyq.rvn.happyq.Activitymain;
import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.MainFragment;
import com.happyq.rvn.happyq.fragment.listing.AdapterRegQueue;
import com.happyq.rvn.happyq.fragment.listing.DataQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class QueueToolbar extends AppCompatActivity {

    View not_found, found;
    List<DataQueue> data = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    AdapterRegQueue mAdapter;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    TextView tv_date, tv_date2, _tvrecent, _tvcurrent, _tvcount, _tvQreserve, _tv_branchname, _tv_branchnameticket, _tv_queue_ticket, _tv_queuenumberticket;
    Date currentDate;
    Calendar calendar;
    Button btreservenow, btreservedate;
    Context context=this;

    ImageView imagee;

    //loading..




    String Qcurrent, finalbranchname, finalqueuename, finalnumber;

    private static final String BANNER = "banner";

    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queue_reservation);

        calendar = Calendar.getInstance();
        tv_date = (TextView)findViewById(R.id.tv_date);
        tv_date2 = (TextView)findViewById(R.id.tv_date2);
        imagee = (ImageView)findViewById(R.id.image);
        not_found = findViewById(R.id.not_found);
        found = findViewById(R.id.found);
        _tvrecent = (TextView)findViewById(R.id.tvrecent);
        _tvcurrent = (TextView)findViewById(R.id.tvcurrent);
        _tvcount = (TextView)findViewById(R.id.tvcount);
        _tvQreserve = (TextView)findViewById(R.id.tvQueuenumber);
        _tv_branchname = (TextView)findViewById(R.id.tvbranchname);
        _tv_branchnameticket = (TextView)findViewById(R.id.tv_branchnamea);
        _tv_queue_ticket = (TextView)findViewById(R.id.tv_selectqueue);
        _tv_queuenumberticket = (TextView)findViewById(R.id.tvQueuenumber) ;
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);


        //loading
        ProgressDialog pdLoading = new ProgressDialog(context);
        pdLoading.setMessage("\tLoading queues...");
        pdLoading.setCancelable(false);
        pdLoading.show();
        new AsyncFetch().execute();
        pdLoading.dismiss();

        //autorefreshing
        doTheAutoRefresh();

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
                openReserve_intent1.putExtra("fbranchname", finalbranchname );
                openReserve_intent1.putExtra("fqueuename", finalqueuename );
                openReserve_intent1.putExtra("fqueuenumber", finalnumber );

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

        String a = getIntent().getExtras().getString("queuename");
        String showqueue = "https://happyq.txtlinkapp.com/happyq_app/qdetails.php?name=" + a;
        collapsingToolbarLayout.setTitle(a);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        Context context = this;
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(context, R.color.coolgreen));

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);

    }

    public class AsyncFetch extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;
        String a = getIntent().getExtras().getString("queuename");
        String showqueue = "https://happyq.txtlinkapp.com/happyq_app/qdetails.php?name=" + a;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading queues...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();

            finalbranchname = getIntent().getExtras().getString("BN_queue");
             finalqueuename = getIntent().getExtras().getString("queuename");


            _tv_branchname.setText("");
            _tv_branchname.setText(finalbranchname);
            _tv_branchnameticket.setText("");
            _tv_branchnameticket.setText(finalbranchname);
            _tv_queue_ticket.setText("");
            _tv_queue_ticket.setText(finalqueuename);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(showqueue);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {
                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());
                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            //pdLoading.dismiss();
//
//            pdLoading.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(result);
                //VISIBLE_FALSE();
                try{
                JSONArray jsonArraycurrent = jsonObject.getJSONArray("current");

                for (int a = 0; a <= jsonArraycurrent.length(); a++) {

                    JSONObject json_dataaa = jsonArraycurrent.getJSONObject(a);

                    //QueueData.queuename= json_data.getString("name");


                     Qcurrent = json_dataaa.getString("id_num");
                    _tvcurrent.setText("");
                    _tvcurrent.setText(Qcurrent);

                    JSONArray jsonArrayrecent = jsonObject.getJSONArray("recent");


                    for (int b = 0; b <= jsonArraycurrent.length(); b++) {

                        JSONObject json_data = jsonArrayrecent.getJSONObject(b);

                        //QueueData.queuename= json_data.getString("name");
                        String Qrecent = json_data.getString("id_num");
                        _tvrecent.setText("");
                        _tvrecent.setText(Qrecent);

                        JSONArray jsonArraycount = jsonObject.getJSONArray("count");

                        for (int c = 0; c <= jsonArraycount.length(); c++) {
                            JSONObject json_dataa = jsonArraycount.getJSONObject(c);

                            //int q = json_dataa.getInt("count");
                            String Qcount = json_dataa.getString("count");
                            _tvcount.setText("");
                            _tvcount.setText(Qcount);

                            JSONArray jsonArraypossible = jsonObject.getJSONArray("possible");

                            for (int d = 0; d <= jsonArraycount.length(); d++) {
                                JSONObject jsondata = jsonArraypossible.getJSONObject(d);

                                //int q = json_dataa.getInt("count");
                                finalnumber = jsondata.getString("possible");
                                _tv_queuenumberticket.setText("");
                                _tv_queuenumberticket.setText(finalnumber);



                            }

                        }
                    }

//                    // load image into imageview using glide
//                    Glide.with(getApplicationContext()).load("https://happyq.txtlinkapp.com/" + bannerimg)
//                            //.skipMemoryCache(true)
//                            .placeholder(R.drawable.ic_happyq)
//                            .error(R.drawable.ic_happyq)
//                            .into(imagee);
                }

                } catch (JSONException e) {
                    // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ProgressDialog pdLoading = new ProgressDialog(context);
                // Write code for your refresh logic
                //pdLoading.dismiss();
                new AsyncFetch().execute();
                doTheAutoRefresh();
            }
        }, 5000);
    }


    public void VISIBLE_FALSE(){
        found.setVisibility(View.GONE);
        not_found.setVisibility(View.VISIBLE);
    }

    public void VISIBLE_TRUE(){
        not_found.setVisibility(View.GONE);
       found.setVisibility(View.VISIBLE);
    }


}


