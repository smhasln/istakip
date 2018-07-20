package com.sengelgrup.istakip;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        Log.d(REG_TOKEN, recent_token);

        //Verileri hafızaya at.
        SharedPreferences token_at = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor token_at_editor = token_at.edit();
        token_at_editor.putString("token", recent_token);
        token_at_editor.commit();
    }
}

