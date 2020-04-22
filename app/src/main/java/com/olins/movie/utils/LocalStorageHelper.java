package com.olins.movie.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.olins.movie.BuildConfig;

public class LocalStorageHelper {

    private final Context context;
    private final SharedPreferences mSharedPref;

    public LocalStorageHelper(Context context) {
        this.context = context;
        mSharedPref = context.getSharedPreferences("MyMovie", Context.MODE_PRIVATE);
    }

    public void saveLanguage(String language) {
        mSharedPref.edit().putString("language", language).apply();
    }

    public String getLanguage() {
        return mSharedPref.getString("language", "en");
    }

}
