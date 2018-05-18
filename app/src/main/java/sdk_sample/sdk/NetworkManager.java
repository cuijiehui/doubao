package sdk_sample.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.linphone.LinphoneManager;

/**
 * Created by Android on 2017/3/23.
 */

public class NetworkManager extends BroadcastReceiver {
    public NetworkManager() {
    }

    public void onReceive(Context context, Intent intent) {
        ConnectivityManager localConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(intent != null) {
            boolean localBoolean = intent.getBooleanExtra("noConnectivity", false);
            if(LinphoneManager.isInstanciated()) {
                LinphoneManager.getInstance().connectivityChanged(localConnectivityManager, localBoolean);
            } else if(localConnectivityManager != null) {
                NetworkInfo eventInfo = localConnectivityManager.getActiveNetworkInfo();
                if(eventInfo != null && eventInfo.getState() == NetworkInfo.State.CONNECTED) {
                    SayeeManager.getInstance().turnOnCall(context);
                    int i = 0;

//                    while(!LinphoneService.isReady()) {
//                        try {
//                            Thread.sleep(30L);
//                            ++i;
//                            if(i == 150) {
//                                System.gc();
//                                break;
//                            }
//                        } catch (InterruptedException var8) {
//                            var8.printStackTrace();
//                        }
//                    }
                }
            }
        }

    }
}

