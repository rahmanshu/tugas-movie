package com.olins.movie;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.olins.movie.BuildConfig;
import com.olins.movie.data.api.TrackingService;
import com.olins.movie.utils.FontsOverride;
import com.olins.movie.utils.LocaleUtil;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Goodie on 10/02/2019.
 */

public class TrackingApplication extends Application {

    private static TrackingApplication instance;
    private TrackingService trackingService;
    private SharedPreferences sharedPreferences;

    public static TrackingApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        LocaleUtil.setLocale(new Locale("ID"));
        LocaleUtil.updateConfig(this, getBaseContext().getResources().getConfiguration());
        instance = this;
        setupTimber();
        setupWebService();
        setupSharedPreferences();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/HelveticaNeueLts.otf");
    }

    public TrackingService getTrackingService(){
        return trackingService;
    }

    private void setupTimber(){
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){}
            );
        }
    }

    private void setupWebService() {
        trackingService = new TrackingService(this);
    }


    private void setupSharedPreferences(){
        this.sharedPreferences = getSharedPreferences(TrackingApplication.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        LocaleUtil.updateConfig(this,newConfig);
    }


}
