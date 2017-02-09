package com.happyq.rvn.happyq.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.listing.AdapterRVQueue;
import com.happyq.rvn.happyq.fragment.listing.DataQueue;
import com.happyq.rvn.happyq.fragment.listing.RecyclerItemClickListener;
import com.happyq.rvn.happyq.reservation_query.QueueToolbar;

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
import java.util.List;

/**
 * Created by RVN on 2/7/2017.
 */

public class Queue_query extends AppCompatActivity {
    View lyt_not_found;
    Context context=this;
    List<DataQueue> data=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    AdapterRVQueue mAdapter;
    String queuename;
    Intent r;
    HttpURLConnection connec;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_query);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        lyt_not_found = findViewById(R.id.lyt_not_found);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        doTheAutoRefresh();
        new Queue_query.AsyncFetch().execute();


        mSwipeRefreshLayout.setColorSchemeResources(R.color.marker_secondary, R.color.marker_primary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        new AsyncFetch().execute();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });




        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public String onItemClick(View v, int position) {

                        String BN = getIntent().getExtras().getString("branchname");
                        String showqueue = "https://happyq.txtlinkapp.com/happyq_app/qdetails.php?name=" + data.get(position).queuename;
                        Toast.makeText(context, showqueue, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(Queue_query.this, QueueToolbar.class);
                        intent.putExtra("queuename", data.get(position).queuename);
                        intent.putExtra("BN_queue", BN);
                        startActivity(intent);
                        return showqueue;
                    }
                })
        );

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Queueing</font>"));
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

    public class AsyncFetch extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(context);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
//            pdLoading.setMessage("\tLoading queues...");
//            pdLoading.setCancelable(false);
//            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String branchname_mainfragment = getIntent().getExtras().getString("branchname");
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://happyq.txtlinkapp.com/happyq_app/queue_query.php");

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
            visiblea();
            //pdLoading.dismiss();
            data.clear();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<=jArray.length();i++){
                    notvisiblea();
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataQueue QueueData = new DataQueue();
                    QueueData.queuename= json_data.getString("name");
                    QueueData.queueactivity = json_data.getString("activity");
                    QueueData.queuestatus = json_data.getString("status");
                    QueueData.queuemaxreserve = json_data.getString("reserve");

                    queuename = QueueData.queuename= json_data.getString("name");
                    //QueueData.fishImage= json_data.getString("fish_img");
                    //QueueData.price= json_data.getInt("price");
                    String sendd = queuename;
                    data.add(QueueData);
                    mAdapter = new AdapterRVQueue(data);
                    recyclerView.setAdapter(mAdapter);

                }


            } catch (JSONException e) {
                // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void visiblea(){
        recyclerView.setVisibility(View.GONE);
        lyt_not_found.setVisibility(View.VISIBLE);
    }

    public void notvisiblea(){
        lyt_not_found.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //ProgressDialog pdLoading = new ProgressDialog(context);
                // Write code for your refresh logic
                //pdLoading.dismiss();
                new Queue_query.AsyncFetch().execute();
                doTheAutoRefresh();
            }
        }, 20000);
    }

}