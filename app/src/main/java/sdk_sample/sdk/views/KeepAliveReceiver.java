package sdk_sample.sdk.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;


import org.linphone.LinphoneManager;
import org.linphone.LinphoneService;
import org.linphone.mediastream.Log;


/**
 * Created by Android on 2017/3/23.
 */

public class KeepAliveReceiver extends BroadcastReceiver {
    Handler mHandler = null;
    Context context = null;

    public KeepAliveReceiver() {
    }

    public void onReceive(Context context, Intent intent) {
        if(!LinphoneService.isReady()) {
            Log.i("Keep alive broadcast received while Linphone service not ready");
        } else {
            if(LinphoneManager.isInstanciated() && LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
                if(intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_ON")) {
                    LinphoneManager.getLc().enableKeepAlive(true);
                } else if(intent.getAction().equalsIgnoreCase("android.intent.action.SCREEN_OFF")) {
                    LinphoneManager.getLc().enableKeepAlive(false);
                }
            }

        }
    }
}
