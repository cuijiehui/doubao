package sdk_sample.sdk;


import sdk_sample.sdk.result.BaseResult;

/**
 * Created by Android on 2017/3/23.
 */

public interface HttpRespListener {
    void onSuccess(int var1, BaseResult var2);

    void onFail(int var1, String var2);
}
