package com.olins.movie.data.prefs;

import android.content.SharedPreferences;

import com.olins.movie.TrackingApplication;



public class PrefHelper {

    private static SharedPreferences preferences;

    private static void initPref() {
        preferences = TrackingApplication.getInstance().getSharedPreferences();
    }

    public static void clearPref(PrefKey key) {
        initPref();
        preferences = TrackingApplication.getInstance().getSharedPreferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key.toString());
        editor.apply();
    }

    public static void setString(PrefKey key, String value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key.toString(), value);
        editor.apply();
    }

    public static String getString(PrefKey key) {
        initPref();
        return preferences.getString(key.toString(), "");
    }

    public static void setInt(PrefKey key, int value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key.toString(), value);
        editor.apply();
    }

    public static int getInt(PrefKey key) {
        initPref();
        return preferences.getInt(key.toString(), -1);
    }

    public static void setBoolean(PrefKey key, boolean value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key.toString(), value);
        editor.apply();
    }

    public static boolean getBoolean(PrefKey key) {
        initPref();
        return preferences.getBoolean(key.toString(), false);
    }

    public static void clearPreference(PrefKey key) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key.toString());
        editor.apply();
    }

    public static void clearAllPreferences() {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }


    public static boolean getIsFirstRunFromPreference(PrefKey key) {
        initPref();
        return preferences.getBoolean(key.toString(), true);
    }

    public static void setIsFirstRunToPreference(PrefKey key, boolean value) {
        initPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key.toString(), value);
        editor.apply();
    }


}
