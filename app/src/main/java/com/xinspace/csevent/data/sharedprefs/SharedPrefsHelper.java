package com.xinspace.csevent.data.sharedprefs;

/**
 * Created by Android on 2017/8/16.
 */

public interface SharedPrefsHelper {

    void setDefault(Boolean isSetOpenDefault);  //设置门禁为默认主页。

    Boolean getDefault();

}
