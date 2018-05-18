package sdk_sample.sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.linphone.LinphoneManager;
import org.linphone.mediastream.Log;

/**
 * Created by Android on 2017/3/23.
 */

public class KeepAliveHandler extends BroadcastReceiver {
    public KeepAliveHandler() {
    }

    public void onReceive(Context context, Intent intent) {
        Log.i("------------Keep alive handler invoked------------");
        if(LinphoneManager.getLcIfManagerNotDestroyedOrNull() != null) {
            LinphoneManager.getLc().refreshRegisters();

            try {
                Thread.sleep(2000L);
            } catch (InterruptedException var4) {
                var4.printStackTrace();
            }
        }

    }
}
