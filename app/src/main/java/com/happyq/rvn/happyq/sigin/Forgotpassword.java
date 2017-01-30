package com.happyq.rvn.happyq.sigin;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.happyq.rvn.happyq.R;

/**
 * Created by RVN on 12/16/2016.
 */
public class Forgotpassword  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Code to remove the title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Code for Fullscreen feature
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Code to set the screen orientation to portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.forgotpw);


    }
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if(keyCode == android.view.KeyEvent.KEYCODE_BACK) {
               Intent i = new Intent(this, Login.class);
                startActivity(i);
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
}
