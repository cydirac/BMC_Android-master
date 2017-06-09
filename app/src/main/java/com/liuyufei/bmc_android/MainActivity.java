package com.liuyufei.bmc_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.liuyufei.bmc_android.admin.AdminActivity;
import com.yingchen.bmc.login.LoginActivity;
import com.yingchen.bmc.session.SessionManager;

public class MainActivity extends AppCompatActivity {
    static String TAG="MainActivity";

    // Declare SessionManager
    private SessionManager session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial Session
        session = new SessionManager(getApplicationContext());
        Log.i(TAG, String.valueOf(session.isLoggedIn()));

        // Check User Login
        if(!session.isLoggedIn()){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(this,AdminActivity.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        // When stop clear session
        session.logoutUser();
    }

}
