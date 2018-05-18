package com.xinspace.csevent.data.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android on 2017/8/16.
 */

public class AppSharedPrefs implements SharedPrefsHelper{

    private static final String DEFAULT_OPENDOOR= "set_opendoor_default";
    private static final String DEFAULT_PREFS_NAME = "CORESUN";

    private final SharedPreferences prefs;

    public AppSharedPrefs(Context context) {
        prefs = context.getSharedPreferences(DEFAULT_PREFS_NAME, context.MODE_PRIVATE);
    }

    @Override
    public void setDefault(Boolean isSetOpenDefault) {
        prefs.edit().putBoolean(DEFAULT_OPENDOOR, isSetOpenDefault).commit();
    }

    @Override
    public Boolean getDefault() {
        return prefs.getBoolean(DEFAULT_OPENDOOR, false);
    }
}
