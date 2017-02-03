package com.happyq.rvn.happyq.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.listing.DataQueue;
import com.happyq.rvn.happyq.fragment.listing.AdapterRVQueue;
import com.happyq.rvn.happyq.fragment.listing.RecyclerItemClickListener;

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
import com.happyq.rvn.happyq.reservation_query.QueueToolbar;

/**
 * Created by RVN on 1/3/2017.
 */
public class All_fragment extends Fragment {
    //MyReceiver r;
    View lyt_not_found;
    List<DataQueue> data=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    AdapterRVQueue mAdapter;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_all, container, false);

        // Setup and Handover data to recyclerview
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layout);
        lyt_not_found = v.findViewById(R.id.lyt_not_found);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Make call to AsyncTask
        new All_fragment.AsyncFetch().execute();

        mSwipeRefreshLayout.setColorSchemeResources(R.color.marker_secondary, R.color.marker_primary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new All_fragment.AsyncFetch().execute();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getActivity(), QueueToolbar.class);
                startActivity(i);
            }
        }));

        return v;
    }

    public class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(getActivity());
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading queues...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

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
            pdLoading.dismiss();
            visiblea();
            pdLoading.dismiss();

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

                    //QueueData.fishImage= json_data.getString("fish_img");
                    //QueueData.price= json_data.getInt("price");
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

}

