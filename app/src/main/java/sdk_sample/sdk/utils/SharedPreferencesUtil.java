package sdk_sample.sdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Android on 2017/3/23.
 */

public class SharedPreferencesUtil {
    private static final String FILE_NAME = "sayee_save_file_name";
    public static final String SAYEE_TWO_URL_KEY = "sayee_tow_url_key";
    public static final String SAYEE_USER_NAME_KEY = "sayee_user_name_key";
    public static final String SAYEE_USER_PASSWORD_KEY = "sayee_user_password_key";
    public static final String SAYEE_USER_TOKEN_KEY = "sayee_user_token_key";
    public static final String SAYEE_USER_IP_KEY = "sayee_user_ip_key";
    public static final String SAYEE_USER_SIP_NAME_KEY = "sayee_user_sip_name_key";
    public static final String SAYEE_USER_SIP_PASSWORD_KEY = "sayee_user_sip_password_key";
    public static final String SAYEE_USER_SIP_DOMAIN_KEY = "sayee_user_sip_domain_key";
    public static final String SAYEE_USER_SIP_PORT_KEY = "sayee_user_sip_port_key";
    public static final String SAYEE_USER_SIP_TIME_KEY = "sayee_user_sip_time_key";
    public static final String SAYEE_USER_NEIGBOR_ID_KEY = "sayee_user_neigbor_id_key";
    public static final String SAYEE_USER_NEIGBOR_NAME_KEY = "sayee_user_neigbor_name_key";
    public static final String SAYEE_SIP_IS_CALL_KEY = "sayee_sip_is_call_key";
    public static final String SAYEE_DOMAIN_SN_KEY = "sayee_domain_sn_key";
    public static final String SAYEE_TYPE_KEY = "sayee_type_key";
    public static final String SAYEE_TO_SIP_NUMBER_KEY = "sayee_to_sip_number_key";
    public static final String SAYEE_DEAL_TIME_KEY = "sayee_deal_time";
    public static final String SAYEE_ALLOW_RINGING_KEY = "sayee_allow_ringing_key";
    public static final String SAYEE_ALLOW_VIBRATE_KEY = "sayee_allow_vibrate_key";
    private static SharedPreferences sharedPreferences = null;

    public SharedPreferencesUtil() {
    }

    public static SharedPreferences sharedPreferences(Context context) {
        if(sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences("sayee_save_file_name", 0);
        }

        return sharedPreferences;
    }

    public static void saveData(Context context, String key, Object data) {
        if(data != null) {
            String type = data.getClass().getSimpleName();
            SharedPreferences sharedPreferences = sharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if("Integer".equals(type)) {
                editor.putInt(key, ((Integer)data).intValue());
            } else if("Boolean".equals(type)) {
                editor.putBoolean(key, ((Boolean)data).booleanValue());
            } else if("String".equals(type)) {
                editor.putString(key, (String)data);
            } else if("Float".equals(type)) {
                editor.putFloat(key, ((Float)data).floatValue());
            } else if("Long".equals(type)) {
                editor.putLong(key, ((Long)data).longValue());
            }

            editor.commit();
        }
    }

    public static Object getData(Context context, String key, Object defValue) {
        if(key == null) {
            return null;
        } else {
            String type = defValue.getClass().getSimpleName();
            SharedPreferences sharedPreferences = sharedPreferences(context);
            return "Integer".equals(type)? Integer.valueOf(sharedPreferences.getInt(key, ((Integer)defValue).intValue())):("Boolean".equals(type)? Boolean.valueOf(sharedPreferences.getBoolean(key, ((Boolean)defValue).booleanValue())):("String".equals(type)?sharedPreferences.getString(key, (String)defValue):("Float".equals(type)? Float.valueOf(sharedPreferences.getFloat(key, ((Float)defValue).floatValue())):("Long".equals(type)? Long.valueOf(sharedPreferences.getLong(key, ((Long)defValue).longValue())):null))));
        }
    }

    public static String getTwoUrl(Context context) {
        SharedPreferences sharedPreferences = sharedPreferences(context);
        return sharedPreferences.getString("sayee_tow_url_key", "");
    }

    public static String getDataString(Context context, String key) {
        if(key == null) {
            return null;
        } else {
            SharedPreferences sharedPreferences = sharedPreferences(context);
            return sharedPreferences.getString(key, "");
        }
    }

    public static String getToken(Context context) {
        return sharedPreferences(context).getString("sayee_user_token_key", "");
    }

    public static String getUserName(Context context) {
        return sharedPreferences(context).getString("sayee_user_name_key", "");
    }

    public static String getUserID(Context context) {
        return sharedPreferences(context).getString("sayee_user_ip_key", "");
    }

    public static String getUserSipName(Context context) {
        return sharedPreferences(context).getString("sayee_user_sip_name_key", "");
    }

    public static String getUserNeigborId(Context context) {
        return sharedPreferences(context).getString("sayee_user_neigbor_id_key", "");
    }

    public static String getUserPassword(Context context) {
        return sharedPreferences(context).getString("sayee_user_password_key", "");
    }

    public static boolean isAllowRinging(Context context) {
        return context == null?true:sharedPreferences(context).getBoolean("sayee_allow_ringing_key", true);
    }

    public static void setAllowRinging(Context context, boolean isAllowRinging) {
        saveData(context, "sayee_allow_ringing_key", Boolean.valueOf(isAllowRinging));
    }

    public static boolean isAllowVibrate(Context context) {
        return context == null?true:sharedPreferences(context).getBoolean("sayee_allow_vibrate_key", true);
    }

    public static void setAllowVibrate(Context context, boolean isAllowVibrate) {
        saveData(context, "sayee_allow_vibrate_key", Boolean.valueOf(isAllowVibrate));
    }

    public static String getUrl(Context context) {
        if(context == null) {
            return "";
        } else {
            SharedPreferences sharedPreferences = sharedPreferences(context);
            return sharedPreferences.getString("sayee_tow_url_key", "") + sharedPreferences.getString("sayee_user_name_key", "") + sharedPreferences.getString("sayee_user_neigbor_id_key", "");
        }
    }

    public static String getUserSipDomin(Context context) {
        return sharedPreferences(context).getString("sayee_user_sip_domain_key", "");
    }

    public static long getDealTime(Context context) {
        return sharedPreferences(context).getLong("sayee_deal_time", 0L);
    }

    public static boolean getSipIsCall(Context context) {
        return sharedPreferences(context).getBoolean("sayee_sip_is_call_key", false);
    }

    public static String getDomainSn(Context context) {
        return sharedPreferences(context).getString("sayee_domain_sn_key", "");
    }

    public static int getType(Context context) {
        return sharedPreferences(context).getInt("sayee_type_key", 0);
    }

    public static String getToSipNumber(Context context) {
        return sharedPreferences(context).getString("sayee_to_sip_number_key", "");
    }
}

