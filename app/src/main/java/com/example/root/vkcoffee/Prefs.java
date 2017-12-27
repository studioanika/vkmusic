package com.example.root.vkcoffee;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 27.12.17.
 */

public class Prefs {

    Context context;
    private static final String APP_PREFERENCES = "config";
    private static final String APP_PREFERENCES_FIRST = "first_v";
    private SharedPreferences mSettings;

    public Prefs(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public int  getFirst(){
        return Integer.parseInt(mSettings.getString(APP_PREFERENCES_FIRST,"0"));
    }

    public void setFirst(){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_FIRST, "1");
        editor.apply();
    }
}
