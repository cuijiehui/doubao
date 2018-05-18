package com.xinspace.csevent.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 应用程序缓存
 * 
 * @author linjinyu
 */
public class SharedPreferencesUtil1 {

	public static final String PREFERENCE_NAME = "duobao";
	public static SharedPreferences mSharedPreferences;

	public static void saveString(Context mContext, String key, String value) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context mContext, String key, String... defValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (defValue.length > 0)
			return mSharedPreferences.getString(key, defValue[0]);
		else
			return mSharedPreferences.getString(key, "");
	}

	public static void saveBoolean(Context mContext, String key, Boolean value) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public static Boolean getBoolean(Context mContext, String key, Boolean... defValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (defValue.length > 0)
			return mSharedPreferences.getBoolean(key, defValue[0]);
		else
			return mSharedPreferences.getBoolean(key, false);

	}

	public static void saveFloat(Context mContext, String key, float value) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putFloat(key, value).commit();
	}

	public static float getFloat(Context mContext, String key, float... defValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (defValue.length > 0)
			return mSharedPreferences.getFloat(key, defValue[0]);
		else
			return mSharedPreferences.getFloat(key, 1);

	}

	public static void saveInt(Context mContext, String key, int value) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putInt(key, value).commit();
	}

	public static int getInt(Context mContext, String key, int... defValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (defValue.length > 0)
			return mSharedPreferences.getInt(key, defValue[0]);
		else
			return mSharedPreferences.getInt(key, 1);

	}

	public static void saveLong(Context mContext, String key, long value) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	public static long getLong(Context mContext, String key, long... defValue) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (defValue.length > 0)
			return mSharedPreferences.getLong(key, defValue[0]);
		else
			return mSharedPreferences.getLong(key, 1);

	}

	public static void clear(Context mContext) {
		SharedPreferences mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		mSharedPreferences.edit().clear().commit();
	}

}
