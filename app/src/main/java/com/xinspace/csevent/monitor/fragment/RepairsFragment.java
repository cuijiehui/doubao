package com.xinspace.csevent.monitor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xinspace.csevent.monitor.bean.SubmitRepairsBean;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.login.activity.LoginActivity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * Created by Android on 2017/3/17.
 */

public class RepairsFragment extends Fragment{

    private View view;
    private EditText ed_tel;
    private EditText ed_name;
    private EditText et_feedback_txt;
    private TextView tv_submit;
    private SDPreference preference;
    private String uid;
    private String area;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        area = SharedPreferencesUtil1.getString(getActivity(), COMMUNITY_AREA, "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_repairs , null);

        preference = SDPreference.getInstance();
        uid = preference.getContent("userId");

        initView();
        return view;
    }

    private void initView() {

        ed_tel = (EditText) view.findViewById(R.id.ed_tel);
        ed_name = (EditText) view.findViewById(R.id.ed_name);
        et_feedback_txt = (EditText) view.findViewById(R.id.et_feedback_txt);
        tv_submit = (TextView) view.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(clickListener);
    }



    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_submit:
                    if (uid.equals("0")){
                        Intent intent = new Intent(getActivity() , LoginActivity.class);
                        startActivity(intent);
                    }else{
                        submitData();
                    }
                    break;
            }
        }
    };

    private void submitData() {
        String name = ed_name.getText().toString().trim();
        String tel = ed_tel.getText().toString().trim();
        String content = et_feedback_txt.getText().toString().trim();

        String cUid = preference.getContent("cUid");
        String token = preference.getContent("cToken");

        if (TextUtils.isEmpty(name)){
            ToastUtil.makeToast("请输入联系人姓名");
            return;
        }

        if (TextUtils.isEmpty(tel)){
            ToastUtil.makeToast("请输入联系人电话");
            return;
        }else {
            if(!verifyPhone(tel)){
                ToastUtil.makeToast("请输入正确的手机号");
                return;
            }
        }

        if (TextUtils.isEmpty(content)){
            ToastUtil.makeToast("请输入报修内容");
            return;
        }

        SubmitRepairsBean bean = new SubmitRepairsBean();
        bean.setName(name);
        bean.setDescribe(content);
        bean.setPhone(tel);
        bean.setType("2");
        bean.setToken(token);
        bean.setUid(cUid);

        GetDataBiz.submitRepairsData(area, bean, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("报修的提交返回" + result);
                if (result == null || result.equals("")){
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")){
                    ToastUtil.makeToast("报修意见添加成功");
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    private boolean verifyPhone(String phone) {
        //在将数据写入数据库之前,判断用户输入的邮箱格式是否正确
        String filter="^0?1[3|4|5|8|7][0-9]\\d{8}$";
        Pattern p=Pattern.compile(filter);
        Matcher matcher = p.matcher(phone);
        if(!matcher.find()){
            return false;
        }
        return true;
    }


}
