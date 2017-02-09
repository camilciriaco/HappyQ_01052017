package com.happyq.rvn.happyq.sigin;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.happyq.rvn.happyq.Activity_main_tablayout;
import com.happyq.rvn.happyq.R;

import com.happyq.rvn.happyq.data.DatabaseHandler;
import com.happyq.rvn.happyq.data.Databasehandlerr;
import com.happyq.rvn.happyq.data.SessionManager;
import com.happyq.rvn.happyq.data.AppConfig;
import com.happyq.rvn.happyq.data.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RVN on 12/7/2016.
 */

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Button btlogin, btnRevokeAccess;
    TextView btregister, Etforgotpw;
    EditText EtLoginUsername, EtLoginPwd;
    RelativeLayout relativeLayout;

    private ImageView user_image;
    LoginButton loginButton;

    UserSessionManager session;

    //Google
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private SignInButton btnSignIn;

    private static final String TAGTAG = Register.class.getSimpleName();
    public SessionManager sessions;
    public Databasehandlerr dbdb;

    //internet Connection
    Context context = this;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Snackbar snackbar;
    private boolean internetConnected = true;


    //Facebook
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

//    //Facebook login button
//    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
//        @Override
//        public void onSuccess(LoginResult loginResult) {
//            Profile profile = Profile.getCurrentProfile();
//            nextActivity(profile);
//        }
//        @Override
//        public void onCancel() {        }
//
//        @Override
//        public void onError(FacebookException e) {      }
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize SDK before setContentView(Layout ID)
        FacebookSdk.sdkInitialize(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btregister = (TextView) findViewById(R.id.loginRegisterBtn1);
        btlogin = (Button) findViewById(R.id.loginSubmitBtn);
        EtLoginUsername = (EditText) findViewById(R.id.loginEmailEt);
        EtLoginPwd = (EditText) findViewById(R.id.loginPassEt);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        Etforgotpw = (TextView) findViewById(R.id.forgotpwdBtn);
        //user_image = (ImageView) findViewById(R.id.user_image);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnRevokeAccess = (Button) findViewById(R.id.disconnect_button);

        //google
        btnSignIn = (SignInButton) findViewById(R.id.sign_in_button);

        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        dbdb = new Databasehandlerr(this);
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
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        //callbackManager = CallbackManager.Factory.create();


        //        String signinwithgoogle = "Sign In with Google";
        //        setGooglePlusButtonText(btnSignIn, signinwithgoogle);
        btnSignIn.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                //                .addApi( Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        sessions = new SessionManager(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());


        btlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String email = EtLoginUsername.getText().toString();
                String password = EtLoginPwd.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    checkLogin(email, password);

                }

            }});

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open CameraPage
                Intent openRegister_intent = new Intent(Login.this, Register.class);
                startActivity(openRegister_intent);
                finish();
            }
        });



        Etforgotpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Activity To Open CameraPage
                Intent openRegister_intent = new Intent(Login.this, Forgotpassword.class);
                startActivity(openRegister_intent);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                // accessToken.refreshCurrentAccessTokenAsync();
                //nextActivity(newProfile);
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);   }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                finish();
                startActivity(getIntent());
                //                AccessToken accessToken = loginResult.getAccessToken();
                //                //accessToken.getCurrentAccessToken();
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);



            }

            @Override
            public void onCancel() {    }

            @Override
            public void onError(FacebookException e) {      }
        };
        loginButton.setReadPermissions("public_profile", "email", "user_friends");
        loginButton.registerCallback(callbackManager, callback);

    }

//        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
//        callback = new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                Log.v("Main", response.toString());
//                                setProfileToView(object);
//                            }
//                        });
//                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,email,gender, birthday");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
////                AccessToken accessToken = loginResult.getAccessToken();
////                //accessToken.getCurrentAccessToken();
////                Profile profile = Profile.getCurrentProfile();
////                nextActivity(profile);
////                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();    }
//
//            @Override
//            public void onCancel() {    }
//
//            @Override
//            public void onError(FacebookException e) {      }
//        };
//        loginButton.setReadPermissions("public_profile", "email","user_friends");
//        //loginButton.setReadPermissions("public_profile email");
//        //loginButton.registerCallback(callbackManager, callback);
//
//    }


//MANUAL LOGIN

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("logging in ....");
        mProgressDialog.setIndeterminate(true);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        sessions.setLogin(true);

                        try {

                            dbdb.setEmail(email);
                            dbdb.setPassword(password);

                            // Launch main activity
                            Toast.makeText(Login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                            Intent loginuser = new Intent(Login.this, Activity_main_tablayout.class);
                            loginuser.putExtra("username", email);
                            loginuser.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            loginuser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(loginuser);
                            dbdb.addUserlogin(email, password);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }  catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } }

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
            protected Map<String, String> getParams () {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
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
    /*=======GOOGLE======*/
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String email = acct.getEmail();
            String name = acct.getDisplayName();
            String Idgmail = acct.getId();
            String fname = acct.getGivenName();
            String lname = acct.getFamilyName();
            checkGooglelogin(email,fname,lname,Idgmail);

            Log.e(TAG, "display name: " + acct.getDisplayName());
            Log.e(TAG, "display email: " + acct.getEmail());
            Log.e(TAG, "display email: " + acct.getId());

            if(acct != null) {
                Intent getacct = new Intent(Login.this, Activity_main_tablayout.class);
                getacct.putExtra("gFname", acct.getDisplayName());
                getacct.putExtra("gLname", acct.getEmail());
                getacct.putExtra("gimgurl", acct.getPhotoUrl().toString());
                startActivity(getacct);

            }


//            String personName = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
//            String email = acct.getEmail();
//
//            Log.e(TAG, "Name: " + personName + ", email: " + email
//                    + ", Image: " + personPhotoUrl);
//
//
//
//            txtName.setText(personName);
//            txtEmail.setText(email);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }



    /**
     * function to verify login details in mysql db
     * */
    private void checkGooglelogin(final String gmail_email, final String gmail_fname , final String gmail_lname,final String gmail_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("logging in ....");
        mProgressDialog.setIndeterminate(true);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_GMAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        sessions.setLogin(true);

                        try {

                            dbdb.set_gmail_email(gmail_email);
                            dbdb.set_gmail_firstname(gmail_fname);
                            dbdb.set_gmail_lastname(gmail_lname);
                            dbdb.set_gmail_ID(gmail_id);


                            // Launch main activity
                            Toast.makeText(Login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                            dbdb.addUserGMAILlogin(gmail_email,gmail_id,gmail_fname,gmail_lname);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }  catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } }

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
            protected Map<String, String> getParams () {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("gmail_email", gmail_email);
                params.put("gmail_fname", gmail_fname);
                params.put("gmail_lname", gmail_lname);
                params.put("gmail_id", gmail_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);

        //registerInternetCheckReceiver();

    }

    @Override
    protected void onPause() {

        super.onPause();
       // unregisterReceiver(broadcastReceiver);
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);
        //google
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }

//
//        // G+
//        if (mGoogleApiClient.hasConnectedApi(Plus.API)) {
//            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//            if (person != null) {
//                Log.i(TAG, "--------------------------------");
//                Log.i(TAG, "Display Name: " + person.getDisplayName());
//                Log.i(TAG, "Gender: " + person.getGender());
//                Log.i(TAG, "About Me: " + person.getAboutMe());
//                Log.i(TAG, "Birthday: " + person.getBirthday());
//                Log.i(TAG, "Current Location: " + person.getCurrentLocation());
//                Log.i(TAG, "Language: " + person.getLanguage());
//            } else {
//                Log.e(TAG, "Error!");
//            }
//        } else {
//            Log.e(TAG, "Google+ not connected");
//        }
    }



    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    private void nextActivity(Profile profile){
        if(profile != null){

            String fbname = profile.getFirstName();
            String fblastname = profile.getLastName();
            String facebook_id = profile.getId();
            //String facebook_image = profile.getProfilePictureUri(150, 150).toString();

            checkffblogin(fbname,fblastname, facebook_id);

            Intent main = new Intent(Login.this, Activity_main_tablayout.class);
            main.putExtra("fbname", profile.getFirstName());
            main.putExtra("fbsurname", profile.getLastName());
            main.putExtra("middlename", profile.getMiddleName());

            main.putExtra("imageUrl", profile.getProfilePictureUri(150, 150).toString());
            //Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_LONG).show();
            startActivity(main);

        }
    }




    private void checkffblogin(final String firstname, final String lastname, final String facebook_id) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("logging in ....");
        mProgressDialog.setIndeterminate(true);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_FACEBOOK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Create login session
                        sessions.setLogin(true);

                        try {

                            dbdb.set_fb_firstname(firstname);
                            dbdb.set_fb_lastname(lastname);
                            dbdb.set_fb_id(facebook_id);


                            // Launch main activity
                            Toast.makeText(Login.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                            dbdb.addUserfblogin(firstname, lastname, facebook_id);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                }  catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } }

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
            protected Map<String, String> getParams () {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("fb_fname", firstname);
                params.put("fb_lname", lastname);
                params.put("fb_id", facebook_id);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }




    private void setProfileToView(JSONObject jsonObject) {
        try {
            if (jsonObject!= null){
                Intent fb = new Intent(Login.this, Activity_main_tablayout.class);
                fb.putExtra("fbemail", jsonObject.getString("email"));
                fb.putExtra("fbname", jsonObject.getString("name"));
                startActivity(fb);}
//
//            email.setText(jsonObject.getString("email"));
//            gender.setText(jsonObject.getString("gender"));
//            facebookName.setText(jsonObject.getString("name"));
//
//            profilePictureView.setPresetSize(ProfilePictureView.NORMAL);
//            profilePictureView.setProfileId(jsonObject.getString("id"));
//            infoLayout.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.sign_in_button:
                signIn();

                break;

            //case R.id.btn_sign_out:
            // signOut();
            // break;

            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            //btnSignOut.setVisibility(View.VISIBLE);
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            //btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
        }
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
//
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
//    }


}





