package com.xinspace.csevent.login.util;

import android.content.Context;
import com.xinspace.csevent.app.weiget.SDPreference;
/**
 * Created by Android on 2017/8/18.
 */

public class LoginStatusUtil {

    public static boolean checkLoginStatus() {
        String userId = SDPreference.getInstance().getContent("userId");
        if (userId!=null && userId.equals("0")){
            return false;
        }
        else{
            return true;
        }
    }


}
