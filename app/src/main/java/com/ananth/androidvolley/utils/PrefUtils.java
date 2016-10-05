package com.ananth.androidvolley.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Babu on 9/29/2016.
 */
public class PrefUtils {
    public static boolean mSave=true;
    public static final String SAVEPREF = "Volley";
    public static void saveData(String key, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SAVEPREF, Activity.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getData(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SAVEPREF, Activity.MODE_PRIVATE);
        return prefs.getString(key, "");
    }
}
