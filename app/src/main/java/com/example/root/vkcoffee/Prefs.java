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
    private static final String APP_PREFERENCES_ID = "id";
    private static final String APP_PREFERENCES_NAME = "name";
    private static final String APP_PREFERENCES_PHOTO = "photo";
    private static final String APP_PREFERENCES_REVIEW = "review";
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

    public int  getID(){
        return Integer.parseInt(mSettings.getString(APP_PREFERENCES_ID,"0"));
    }

    public void setID(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_ID, id);
        editor.apply();
    }

    public String  getPHOTO(){
        return mSettings.getString(APP_PREFERENCES_PHOTO,"0");
    }

    public void setPHOTO(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PHOTO, id);
        editor.apply();
    }

    public String  getNAME(){
        return mSettings.getString(APP_PREFERENCES_NAME,"0");
    }

    public void setNAME(String id){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_NAME, id);
        editor.apply();
    }

    public void setReview(int pred){
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_REVIEW, String.valueOf(pred));
        editor.apply();
    }
    public int getReview(){
        return Integer.parseInt(mSettings.getString(APP_PREFERENCES_REVIEW,"0"));
    }
}


