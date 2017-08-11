package com.veeritsolutions.uhelpme.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.veeritsolutions.uhelpme.R;
import com.veeritsolutions.uhelpme.helper.PrefHelper;
import com.veeritsolutions.uhelpme.utility.Debug;
import com.veeritsolutions.uhelpme.utility.Utils;

import java.util.Locale;

import static com.veeritsolutions.uhelpme.R.string.settings;


/**
 * Created by Admin on 5/10/2017.
 */

public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
               /* if (!PrefHelper.getInstance().getBoolean(PrefHelper.FIRST_TIME_APP_OPEN, false)) {
                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {*/

//                int i = 1 / 0;
//                Debug.trace(String.valueOf(i));

                Utils.printFbKeyHash();
                PrefHelper.getInstance().setLong(PrefHelper.IMAGE_CACHE_FLAG, System.currentTimeMillis());

                if (PrefHelper.getInstance().getBoolean(PrefHelper.IS_LOGIN, false)) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    //intent.putExtra(Constants.IS_FROM_SIGN_UP, false);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();

                } else {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

            }
        };
        handler.postDelayed(runnable, 1000);
    }
}
