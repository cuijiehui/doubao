package sdk_sample.sdk;

import android.content.Context;
import android.content.Intent;

import org.linphone.LinphoneManager;
import org.linphone.core.LinphoneCoreException;
import org.linphone.mediastream.Log;

/**
 * Created by Android on 2017/3/23.
 */

public class SayeeManager {
    boolean isOpen = false;
    private static SayeeManager instance = null;

    private SayeeManager() {
    }

    public static SayeeManager getInstance() {
        if(instance == null) {
            Class var0 = SayeeManager.class;
            synchronized(SayeeManager.class) {
                if(instance == null) {
                    instance = new SayeeManager();
                }
            }
        }

        return instance;
    }

    public void initalize(Context context) {
        if(context == null) {
            try {
                throw new LinphoneCoreException("initalize error,context connot be null");
            } catch (LinphoneCoreException var3) {
                var3.printStackTrace();
            }
        }

        //context.startService(new Intent(context, LinphoneService.class));
        this.isOpen = true;
    }

    public void turnOffCall(Context context) {
        if(context == null) {
            Log.e("turn off fail,context cannot be null");
        } else {
//            if(LinphoneService.isReady()) {
//                context.stopService(new Intent(context, LinphoneService.class));
//            }

            LinphoneManager.destroy();
            this.isOpen = false;
            System.gc();
        }
    }

    public void turnOnCall(Context context) {
        if(context == null) {
            Log.e("turn on fail,context cannot be null");
        } else {
            this.initalize(context);
        }
    }

    public boolean isEnableCall() {
        return this.isOpen;
    }
}

