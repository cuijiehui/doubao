package com.xinspace.csevent.app.weiget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.xinspace.csevent.app.CoresunApp;


public class SDPreference {

    public SDPreference() {
        preferences = CoresunApp.context.getSharedPreferences("ShowBodyPreference", Context.MODE_PRIVATE);
    }

    public static SharedPreferences preferences = null;

    public static SDPreference instance = null;

    public static SDPreference getInstance() {
        if (instance == null) {
            instance = new SDPreference();
        }
        return instance;
    }

    public void setLoad(boolean b) {
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putBoolean("isload", b);
            editor.commit();
        }
    }

    public boolean isLoad() {
        return preferences.getBoolean("isload", false);
    }

    public void putContent(String tag, int content) {
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putInt(tag, content);
            editor.commit();
        }
    }

    public void putContent(String tag, String content) {
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putString(tag, content);
            editor.commit();
        }
    }


    public void putContent(String tag, long content) {
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putLong(tag, content);
            editor.commit();
        }
    }


    public void putContent(String tag, Boolean b) {
        if (preferences != null) {
            Editor editor = preferences.edit();
            editor.putBoolean(tag, b);
            editor.commit();
        }
    }

    public int getIntContent(String tag) {
        int myF = 0;
        if (preferences != null) {
            myF = preferences.getInt(tag, -1);
        }
        return myF;
    }

    public int getHairIntContent(String tag) {
        int myF = 0;
        if (preferences != null) {
            myF = preferences.getInt(tag, 0);
        }
        return myF;
    }

    public String getContent(String tag) {
        String myF = null;
        if (preferences != null) {
            myF = preferences.getString(tag, "0");
        }
        return myF;
    }

    public Long getLongContent(String tag) {
        Long myF = null;
        if (preferences != null) {
            myF = preferences.getLong(tag , 0);
        }
        return myF;
    }



    public boolean getBooleanContent(String tag) {
        boolean myF = true;
        if (preferences != null) {
            myF = preferences.getBoolean(tag, true);
        }
        return myF;
    }

    public boolean getBooleanTuiSongAndTongbu(String tag) {
        boolean myF = false;
        if (preferences != null) {
            myF = preferences.getBoolean(tag, false);
        }
        return myF;
    }

}
