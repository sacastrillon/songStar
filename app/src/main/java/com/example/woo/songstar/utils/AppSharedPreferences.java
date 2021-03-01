package com.example.woo.songstar.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppSharedPreferences {
    public static final String USER_ID = "user_id";
    public static final String NAME = "name";
    public static final String USERNAME = "userName";
    public static final String LANGUAGE = "language";
    public static final String PHOTO = "photo";
    public static final String IS_ADMIN = "isAdmin";


    private static AppSharedPreferences appSharedPreferences;
    public static AppSharedPreferences getInstance() {
        if(appSharedPreferences == null){
            appSharedPreferences = new AppSharedPreferences();
        }

        return appSharedPreferences;
    }

    public void putString(Context context, String key, String value) {
        try{
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, null);
    }

    public void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key, false);
    }

    public void clearAllPreferences(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
