package sdk_sample.sdk.db;

/**
 * Created by Android on 2017/3/23.
 */

import android.content.Context;
import android.os.AsyncTask;

import sdk_sample.sdk.result.BaseResult;


public class LoadDataTask extends AsyncTask<String, Integer, BaseResult> {
    private Context context;
    private LoadDataTask.DataOperate callback;
    private int clazz;

    public LoadDataTask(Context context, LoadDataTask.DataOperate callback, int clazz) {
        this.context = context;
        this.callback = callback;
        this.clazz = clazz;
    }

    protected BaseResult doInBackground(String... params) {
        Object result = null;
        return (BaseResult)result;
    }

    protected void onPostExecute(BaseResult result) {
        if(result != null && result.getCode() == 0) {
            this.callback.displayData(false, result);
        }

        this.callback.loadData(false);
    }

    public interface DataOperate<T extends BaseResult> {
        void loadData(boolean var1);

        void displayData(boolean var1, T var2);
    }
}

