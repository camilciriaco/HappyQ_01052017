package com.happyq.rvn.happyq;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.happyq.rvn.happyq.sigin.DownloadImage;
import com.happyq.rvn.happyq.sigin.UserSessionManager;
import com.happyq.rvn.happyq.fragment.MainFragment;
import com.happyq.rvn.happyq.fragment.PurchaseCredit_fragment;
import com.happyq.rvn.happyq.fragment.QueuePrices_fragment;
import com.happyq.rvn.happyq.fragment.TabFragment;
import com.happyq.rvn.happyq.data.Tools;
import com.happyq.rvn.happyq.data.DatabaseHandler;
import com.happyq.rvn.happyq.sigin.Login;


public class Activitymain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {



    UserSessionManager session;
    private NavigationView navigationView;
    ProfilePictureView fbimg;
    GoogleApiClient mGoogleApiClient;
    ImageView closedrawer;
    DrawerLayout drawer;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    FrameLayout fl, content_fl;
    Toolbar toolbar;
    RelativeLayout relativeLayout;
    Context context = this;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;
    DatabaseHandler db;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        session = new UserSessionManager(getApplicationContext());
        fbimg = (ProfilePictureView) findViewById(R.id.fbimg);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fl = (FrameLayout) findViewById(R.id.containerView);
        content_fl = (FrameLayout) findViewById(R.id.content_frame);

        setSupportActionBar(toolbar);
        prepareImageLoader();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
            setSupportActionBar(toolbar);


// private BroadcastReceiver mRegistrationBroadcastReceiver;
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if(intent.getAction().equals(GCM_notification.REGISTRATION_SUCCESS)){
//                    String token = intent.getStringExtra("token");
//                    Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
//
//                }else if (intent.getAction().equals(GCM_notification.REGISTRATION_ERROR)){
//                    Toast.makeText(getApplicationContext(), "GCM Registration Error!", Toast.LENGTH_LONG).show();
//                } else{
//
//                }
//            }
//        };
//
//        int reseultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//        if(ConnectionResult.SUCCESS != reseultCode){
//
//            if(GooglePlayServicesUtil.isUserRecoverableError(reseultCode)){
//                Toast.makeText(getApplicationContext(), "Google play is not available", Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(getApplicationContext(), "this device does not support google play service :(", Toast.LENGTH_LONG).show();
//            }
//        }else
//        {
//            Intent intent = new Intent(this, GCM_notification.class);
//            startActivity(intent);
//        }

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

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        ImageView imgview = (ImageView) hView.findViewById(R.id.user_image);
        ImageView nav_back = (ImageView) hView.findViewById(R.id.drawerback);


        String name;
        String email;
        String kimageUrl;
        String gFname;
        String gLname;
        String gimgurl;

        //viewing name/username for navigation
        TextView nav_user = (TextView) hView.findViewById(R.id.user_name);


        String userneym = getIntent().getStringExtra("username");
        nav_user.setText(userneym);
        View view2 = (View) hView.findViewById(R.id.view2);
        ImageView imgView = (ImageView) hView.findViewById(R.id.user_image);
        imgView.setVisibility(View.GONE);
        view2.setVisibility(View.GONE);
        navigationView.setNavigationItemSelectedListener(this);

         name = getIntent().getStringExtra("fbname");
        email = getIntent().getStringExtra("fbsurname");
        kimageUrl = getIntent().getStringExtra("imageUrl");
        gFname = getIntent().getStringExtra("gFname");
        gLname = getIntent().getStringExtra("gLname");
        gimgurl = getIntent().getStringExtra("gimgurl");

        if (userneym == null && name == null && email==null && kimageUrl==null) {
            view2.setVisibility(View.VISIBLE);
            Intent intent = getIntent();
            Bundle Bundle = intent.getExtras();
             if (Bundle != null) {
                     gFname = Bundle.getString("gFname");
                     gLname = Bundle.getString("gLname");
                     gimgurl = Bundle.getString("gimgurl");
                 if (gimgurl == null){
                        String lala = "imgview.setImageResource(R.drawable.happyqstraight);";

                     nav_user.setText(gFname + " " + gLname);
                     Glide.with(getApplicationContext()).load(lala)
                             .thumbnail(0.5f)
                             .crossFade()
                             .diskCacheStrategy(DiskCacheStrategy.ALL)
                             .into(imgview);
                 }


                    nav_user.setText(gFname + " " + gLname);
                 imgView.setVisibility(View.VISIBLE);
                    Glide.with(getApplicationContext()).load(gimgurl)
                         .thumbnail(0.5f)
                         .crossFade()
                         .diskCacheStrategy(DiskCacheStrategy.ALL)
                         .into((ImageView) hView.findViewById(R.id.user_image));

                    navigationView.setNavigationItemSelectedListener(this);
            }
        } else if(userneym == null && gFname == null && gLname == null && gimgurl== null) {
            view2.setVisibility(View.VISIBLE);
            Bundle inBundle = getIntent().getExtras();
             name = inBundle.get("fbname").toString();
             email = inBundle.get("fbsurname").toString();
            //fbimg.setVisibility(View.VISIBLE);
            kimageUrl = inBundle.get("imageUrl").toString();

       //     profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//            profilePictureView.setProfileId(jsonObject.getString("id"));

            nav_user.setText(name + " " + email);
            //hView.findViewById(R.id.fbimg).setVisibility(View.VISIBLE);
            imgView.setVisibility(View.VISIBLE);
            new DownloadImage((ImageView) hView.findViewById(R.id.user_image)).execute(kimageUrl);

            navigationView.setNavigationItemSelectedListener(this);

        }

        nav_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), ActivitySetting.class);
            startActivity(i);

        } else if (id == R.id.action_logout){
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog_confirm_title));
            builder.setMessage(getString(R.string.message_logout));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Login a = new Login();
                    //a.signOut();

                    session.logoutUser();
                    //session.disconnectFromFacebook();
                    Intent ia = new Intent(getApplicationContext(), Login.class);
                    startActivity(ia);
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }

    public void fragmentvisilibility(){
        content_fl.setVisibility(View.VISIBLE);
        fl.setVisibility(View.GONE);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentManager fm = getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.nav_myprofile) {
                        android.support.v4.app.FragmentManager fsm = getSupportFragmentManager();
                        fsm.beginTransaction().replace(R.id.containerView, new TabFragment()).commit();
                        fl.setVisibility(View.VISIBLE);
                        content_fl.setVisibility(View.GONE);
                            }
                            else if (id == R.id.nav_all)
                                {
                                    fm.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
                                    fragmentvisilibility();
                                }
                             else if (id == R.id.nav_purchasecredit)
                                {
                                    fm.beginTransaction().replace(R.id.content_frame, new PurchaseCredit_fragment()).commit();
                                    fragmentvisilibility();
                                }
                             else if (id == R.id.nav_queueprices)
                                {
                                    fm.beginTransaction().replace(R.id.content_frame, new QueuePrices_fragment()).commit();
                                    fragmentvisilibility();
                                }
                                else if (id == R.id.nav_logout) {
                                        AlertDialog.Builder builder =new AlertDialog.Builder(Activitymain.this);
                                        builder.setTitle(getString(R.string.dialog_confirm_title));
                                        builder.setMessage(getString(R.string.message_logout));
                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                   // Login a = new Login();
                                                    //a.signOut();
                                                //Intent ia = new Intent(getApplicationContext(), Login.class);
                                                //startActivity(ia);

                                                //session.disconnectFromFacebook();
                                                session.logoutUser();
                                                LoginManager.getInstance().logOut();
                                                Intent a = new Intent(Activitymain.this, Login.class);
                                                startActivity(a);
                                                //finish();
                                                //startActivity(getIntent());
                            //                    if (mGoogleApiClient.isConnected()) {
                            //                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            //                        mGoogleApiClient.disconnect();
                            //                        mGoogleApiClient.connect();
                            //                        // updateUI(false);
                            //                        System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");
                            //
                            //                    }

                            //                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            //                            new ResultCallback<Status>() {
                            //                                @Override
                            //                                public void onResult(Status status) {
                            //                                    // ...
                            //                                    Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                            //                                    Intent i = new Intent(getApplicationContext(), Login.class);
                            //                                    startActivity(i);
                            //                                }
                            //                            });


                            //                    if (mGoogleApiClient.isConnected()) {
                            //                        Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                            //                        mGoogleApiClient.disconnect();
                            //                        mGoogleApiClient.connect();
                            //                        // updateUI(false);
                            //                        System.err.println("LOG OUT ^^^^^^^^^^^^^^^^^^^^ SUCESS");
                            //                    }


                                            }
                                        });
                                        builder.setNegativeButton("Cancel", null);
                                        builder.show();
                                        return true;
                                    }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareImageLoader(){
        Tools.initImageLoader(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            finish();
            System.exit(0);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        //registerInternetCheckReceiver();
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCM_notification.REGISTRATION_SUCCESS));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCM_notification.REGISTRATION_ERROR));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregisterReceiver(broadcastReceiver);
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());


        if (tab.getPosition() == 0) {

            LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
            Intent i = new Intent("TAG_REFRESH");
            lbm.sendBroadcast(i);

        }

    }

//    private void registerInternetCheckReceiver() {
//        IntentFilter internetFilter = new IntentFilter();
//        internetFilter.addAction("android.net.wifi.STATE_CHANGE");
//        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//
//        registerReceiver(broadcastReceiver, internetFilter);
//    }
//
//    /**
//     *  Runtime Broadcast receiver inner class to capture internet connectivity events
//     */
//    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String status = getConnectivityStatusString(context);
//            setSnackbarMessage(status,false);
//        }
//    };
//
//    public static int getConnectivityStatus(Context context) {
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        if (null != activeNetwork) {
//            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
//                return TYPE_WIFI;
//
//            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
//                return TYPE_MOBILE;
//        }
//        return TYPE_NOT_CONNECTED;
//    }
//
//    public static String getConnectivityStatusString(Context context) {
//        int conn = getConnectivityStatus(context);
//        String status = null;
//        if (conn == TYPE_WIFI) {
//            status = "Wifi enabled";
//        } else if (conn == TYPE_MOBILE) {
//            status = "Mobile data enabled";
//        } else if (conn == TYPE_NOT_CONNECTED) {
//            status = "Not connected to Internet";
//        }
//        return status;
//    }
//    private void setSnackbarMessage(String status,boolean showBar) {
//        String internetStatus="";
//        if(status.equalsIgnoreCase("Wifi enabled")||status.equalsIgnoreCase("Mobile data enabled")){
//            internetStatus="Internet Connected";
//        }else {
//            internetStatus="No Internet Connection";
//        }
//        snackbar = Snackbar
//                .make(relativeLayout, internetStatus, Snackbar.LENGTH_INDEFINITE)
//                .setAction("close", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snackbar.dismiss();
//                    }
//                });
//
//        View sbView = snackbar.getView();
//        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        //textView.setTextColor(Color.WHITE);
//        if(internetStatus.equalsIgnoreCase("No Internet Connection")){
//            if(internetConnected){
//                snackbar.show();
//                snackbar.setActionTextColor(Color.WHITE);
//                textView.setTextColor(Color.RED);
//                internetConnected=false;
//            }
//        }   else{
//            if(!internetConnected){
//                internetConnected=true;
//                snackbar.setActionTextColor(Color.WHITE);
//                textView.setTextColor(Color.GREEN);
//                snackbar.show();
//            }
//
//        }
//}

}
