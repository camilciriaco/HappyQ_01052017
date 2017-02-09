package com.happyq.rvn.happyq;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.happyq.rvn.happyq.data.SharedPref;
import com.happyq.rvn.happyq.data.GCM.GcmLoader;
import com.happyq.rvn.happyq.data.Callback;
import com.happyq.rvn.happyq.data.Tools;
import com.happyq.rvn.happyq.R;

import com.happyq.rvn.happyq.sigin.Login;
import com.happyq.rvn.happyq.data.PermissionUtil;

import java.util.Timer;
import java.util.TimerTask;

public class Splashscreen extends AppCompatActivity {
    private SharedPref sharedPref;
    SharedPreferences sharedPreferences;
    boolean isAppInstalled = false;
    private View parent_view;
    final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            startMainActivity();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        parent_view = findViewById(R.id.parent_view);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        isAppInstalled= sharedPreferences.getBoolean("isAppInstalled", false);
        if(isAppInstalled==false) {
            Intent intent1 = new Intent(getApplicationContext(), Splashscreen.class);
            intent1.setAction(Intent.ACTION_MAIN);
            Intent intent2 = new Intent();
            intent2.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent1);
            intent2.putExtra(Intent.EXTRA_SHORTCUT_NAME, "HappyQ");
            intent2.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.ic_happyqv));
            intent2.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent2);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();

        }
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        sharedPref = new SharedPref(this);
        Tools.initImageLoader(getApplicationContext());

        parent_view.setBackgroundColor(sharedPref.getThemeColorInt());
        // for system bar in lollipop
        Tools.systemBarLolipop(this);
    }

    private void startProvisioningGcm(){
        if(sharedPref.isNeedRegisterGcm() && Tools.cekConnection(this)){
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            // register gcm
            new GcmLoader(this, new Callback<String>() {
                @Override
                public void onSuccess(String result) {
                    new Timer().schedule(task, 5000);
                }

                @Override
                public void onError(String result) {
                    new Timer().schedule(task, 1000);
                }
            }).execute("");
        } else {
            try{
                // Show splash screen for 1 seconds
                new Timer().schedule(task, 5000);
            }catch (Exception e){}
        }

    }

    private void startMainActivity() {
        // go to the main activity
        Intent i = new Intent(Splashscreen.this, Login.class);
        startActivity(i);
        // kill current activity
        finish();
    }

    @Override
    protected void onResume() {
        if(!PermissionUtil.isAllPermissionGranted(this)){
            showDialogPermission();
        }else{
            startProvisioningGcm();
        }
        super.onResume();
    }

    private void showDialogPermission(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_permission));
        builder.setMessage(getString(R.string.dialog_content_permission));
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PermissionUtil.goToPermissionSettingScreen(Splashscreen.this);
            }
        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                new Timer().schedule(task, 1000);
//            }
//        });
        builder.show();
    }
}
