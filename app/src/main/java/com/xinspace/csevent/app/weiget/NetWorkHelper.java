package com.xinspace.csevent.app.weiget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.xinspace.csevent.app.CoresunApp;

public class NetWorkHelper {

	public static ConnectivityManager cm = (ConnectivityManager) CoresunApp.context
			.getSystemService(Context.CONNECTIVITY_SERVICE);

	public static boolean getNetWorkState() {

		State wifiState = null;
		State mobileState = null;

		wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

		if (wifiState != null && mobileState != null && State.CONNECTED != wifiState
				&& State.CONNECTED != mobileState) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean getNetState() {
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		} else {
			return true;
		}
	}
}
