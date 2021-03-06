package com.happyq.rvn.happyq.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.happyq.rvn.happyq.R;
import com.happyq.rvn.happyq.fragment.listing.AdapterRegQueue;
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
 * Created by RVN on 1/3/2017.
 */

public class MainFragment extends android.support.v4.app.Fragment {
    View lyt_not_found;
    FrameLayout content_fl, fl;
    ViewPager vieww;
    List<DataQueue> data = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;
    AdapterRegQueue mAdapter;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private Parcelable recyclerViewState;
    private final Handler handler = new Handler();
    Runnable refresh;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.activity_main_swipe_refresh_layouta);
        lyt_not_found = v.findViewById(R.id.lyt_not_founda);
        vieww = (ViewPager) v.findViewById(R.id.viewpager);
        fl = (FrameLayout) v.findViewById(R.id.containerView);
        content_fl = (FrameLayout) v.findViewById(R.id.content_frameb);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_viewa);

        // Save state
        //recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        mSwipeRefreshLayout.setColorSchemeResources(R.color.marker_secondary, R.color.marker_primary, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //onRefresh();
                        new AsyncFetch().execute();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public String onItemClick(View view, int position) {
               String branchname = "RVN Technology Solutions INC.";
                Intent i = new Intent(getActivity(), Queue_query.class);
                i.putExtra("branchname", branchname);
                startActivity(i);
                return null;
            }
        }));

        new MainFragment.AsyncFetch().execute();
        doTheAutoRefresh();
        return v;
    }

    public class AsyncFetch extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(getActivity());
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

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("https://happyq.txtlinkapp.com/happyq_app/QA_query.php");

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

           // pdLoading.dismiss();
            visiblea();
            //pdLoading.dismiss();
            data.clear();

            try {

                JSONObject aray = new JSONObject(result);
                DataQueue QueueDataa = new DataQueue();

                    try{

                        JSONArray jsonArrayimg = aray.getJSONArray("banner");
                        int img = jsonArrayimg.length();
                        // Extract data from json and store into ArrayList as class objects
                        for (int a = 0; a <= jsonArrayimg.length(); a++) {
                            notvisiblea();
                            JSONObject json_dataaa = jsonArrayimg.getJSONObject(a);

                            //QueueData.queuename= json_data.getString("name");
                            QueueDataa.banners = json_dataaa.getString("directory");


                            JSONArray jsonArrayops = aray.getJSONArray("ops");
                            int ja = jsonArrayops.length();
                            // Extract data from json and store into ArrayList as class objects
                            for (int x = 0; x <= jsonArrayops.length(); x++) {
                                notvisiblea();
                                JSONObject json_dataa = jsonArrayops.getJSONObject(x);

                                QueueDataa.queuename= "RVN Technology Solutions INC.";
                                QueueDataa.rqueuestime = json_dataa.getString("t_start");
                                QueueDataa.rqueueetime = json_dataa.getString("t_end");
                                QueueDataa.rqueueoperationday = json_dataa.getString("days");


                                //JSONObject json = new JSONObject(result);
                                JSONArray jArray = aray.getJSONArray("announce");
                                int j = jArray.length();
                                // Extract data from json and store into ArrayList as class objects
                                for (int i = 0; i <= jArray.length(); i++) {
                                    notvisiblea();
                                    JSONObject json_data = jArray.getJSONObject(i);

                                    //QueueData.queuename= json_data.getString("name");
                                    QueueDataa.Atitle = json_data.getString("header");
                                    QueueDataa.Announcement = json_data.getString("message");

                                    data.add(QueueDataa);
                                    mAdapter = new AdapterRegQueue(data);
                                    recyclerView.setAdapter(mAdapter);
                                }
                            }

                        }


                    } catch (JSONException e) {
                        // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    //DataQueue QueueData = new DataQueue();
                    //QueueData.queuename= json_data.getString("name");
                    //QueueData.Atitle =json_data.getString("header");
                    //QueueData.Announcement = innerObject.getString("message");
                    //data.add(QueueData);
//                    QueueData.rqueuestime = json_data.getString("t_start");
//                    QueueData.rqueueetime = json_data.getString("t_end");
//                    QueueData.rqueueoperationday = json_data.getString("days");

                    //QueueData.fishImage= json_data.getString("fish_img");
                    //QueueData.price= json_data.getInt("price");

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
                new MainFragment.AsyncFetch().execute();
                doTheAutoRefresh();
            }
        }, 30000);
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