package com.yingchen.bmc.session;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import com.liuyufei.bmc_android.R;

import java.util.HashMap;

/**
 * Created by Ying Chen on 06/09/17.
 */

public class SessionManager {
    private static String TAG="SessionManager";
    /* Declaration  shared preference */
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="email";

    public SessionManager(Context context) {
        this._context=context;
        pref=_context.getSharedPreferences(String.valueOf(R.string.PREF_NAME),R.integer.PRIVATE_MODE);
        editor=pref.edit();
    }

    public void createLoginSession(String name, String email){
        /* store preference */
        editor.putBoolean(String.valueOf(R.string.IsLoggedIn),true);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public HashMap<String, String> getUserSession(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME,null));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        return user;
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(String.valueOf(R.string.IsLoggedIn), false);
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
        //
        Log.i(TAG,"Logout");
    }

    /**
     * Function to display simple Alert Dialog
     * @param context -application context
     * @param title - alert dialog title
     * @param message - alert message
     * @param status - true false
     */
    public void showAlertDialog(
                Context context,
                String title,
                String message,
                Boolean status
    ){
        // Declare a dialog
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        if(status!=null){
            alertDialog.setIcon((status)? R.drawable.success:R.drawable.fail);
        }
        // Set the OK button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }
}
