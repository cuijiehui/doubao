package sdk_sample.sdk.views;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sdk_sample.sdk.utils.ToolsUtil;


/**
 * Created by Android on 2017/3/23.
 */

public class AlertDialog {
    Context context;
    android.app.AlertDialog ad;
    TextView tv_title;
    TextView tv_message;
    Button btn_left;
    Button btn_right;

    public AlertDialog(Context context) {
        this.context = context;
        this.ad = (new android.app.AlertDialog.Builder(context)).create();
        this.ad.show();

        try {
            this.ad.setContentView(ToolsUtil.getIdByName(context, "layout", "sayee_alert_dialog"));
            this.tv_title = (TextView)this.ad.findViewById(ToolsUtil.getIdByName(context, "id", "tv_title"));
            this.tv_message = (TextView)this.ad.findViewById(ToolsUtil.getIdByName(context, "id", "tv_message"));
            this.btn_left = (Button)this.ad.findViewById(ToolsUtil.getIdByName(context, "id", "btn_left"));
            this.btn_right = (Button)this.ad.findViewById(ToolsUtil.getIdByName(context, "id", "btn_right"));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void setTitle(int resId) {
        this.tv_title.setText(resId);
    }

    public void setTitle(String title) {
        this.tv_title.setText(title);
    }

    public void setMessage(int resId) {
        this.tv_message.setText(resId);
    }

    public void setMessage(String message) {
        this.tv_message.setText(message);
    }

    public void setPositiveButton(View.OnClickListener listener) {
        if(this.btn_right != null) {
            this.btn_right.setOnClickListener(listener);
        }

    }

    public void setNegativeButton(View.OnClickListener listener) {
        if(this.btn_left != null) {
            this.btn_left.setOnClickListener(listener);
        }

    }

    public void dismiss() {
        this.ad.dismiss();
    }
}

