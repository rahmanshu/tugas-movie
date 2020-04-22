package com.olins.movie.ui.activities;

import android.graphics.Color;

import com.afollestad.materialdialogs.MaterialDialog;
import com.olins.movie.R;
import com.olins.movie.data.prefs.PrefHelper;




public class BasePresenter {

    private BaseActivity activity;

    public BasePresenter(BaseActivity activity) {
        this.activity = activity;
    }

    void invalid() {
        activity.dismissProgressDialog();
        new MaterialDialog.Builder(activity)
                .iconRes(R.mipmap.ic_launcher)
                .backgroundColor(activity.cAppsPrimary)
                .title(activity.getString(R.string.logout).toUpperCase())
                .titleColor(Color.WHITE)
                .content(activity.expiredSession)
                .contentColor(Color.WHITE)
                .neutralText(R.string.ok)
                .show();

        PrefHelper.clearAllPreferences();
        LoginActivity.startActivityFromAutoLogout(activity);
        activity.finish();
    }



}
