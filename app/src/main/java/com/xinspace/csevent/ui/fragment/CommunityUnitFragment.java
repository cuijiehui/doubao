package com.xinspace.csevent.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.data.biz.CommunityUnit;
import com.xinspace.csevent.data.biz.GetDataBiz;
import com.xinspace.csevent.monitor.bean.AuditBean;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.SharedPreferencesUtil1;
import com.xinspace.csevent.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.xinspace.csevent.app.AppConfig.COMMUNITY_AREA;

/**
 * 扫二维码添加社区
 * author by Mqz
 * Created by Android on 2017/9/18.
 */

public class CommunityUnitFragment extends Fragment{

    private CommunityUnit data;
    private WheelPicker wheelPicker;
    private TextView btnOK , btnCancel;
    private SDPreference preference;
    private String cToken;
    private String cUid;
    private String mobile;
    private String communityId;
    private String unitId;
    private String flag;
    private String area;

    public static CommunityUnitFragment newInstance(){
        CommunityUnitFragment fragment = new CommunityUnitFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        area = SharedPreferencesUtil1.getString(getActivity(), COMMUNITY_AREA, "");
        data = getArguments().getParcelable("data");
        preference = SDPreference.getInstance();
        cToken = preference.getContent("cToken");
        cUid = preference.getContent("cUid");
        mobile = preference.getContent("mobile");
        communityId = preference.getContent("community_id");
        unitId = preference.getContent("unit_id");
        flag = getArguments().getString("flag");
        ToastUtils.init(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_unit_fragment, container, false);
        initViews(view);
        setData();
        return view;
    }

    private void setData() {
        List<String> names = new ArrayList<>();
        List<CommunityUnit.DataBean> lists = data.getData();
        for (CommunityUnit.DataBean data : lists) {
            names.add(data.getName());
        }
        wheelPicker.setData(names);
    }

    private void initViews(View view) {
        wheelPicker = (WheelPicker) view.findViewById(R.id.unit_chooser);
        btnOK = (TextView)view.findViewById(R.id.btn_unit_ok);
        btnCancel = (TextView) view.findViewById(R.id.btn_unit_cancel);
        wheelPicker.setSelected(true);
        view.setClickable(true);//防止事件传播到下一层Fragment
        btnOK.setOnClickListener(new MyClickListener());
        btnCancel.setOnClickListener(new MyClickListener());
    }

    class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_unit_ok:
                    String roomName = null;
                    String roomId = null;
                    int position = wheelPicker.getCurrentItemPosition();
                    for (int i = 0; i < data.getData().size(); i++) {
                        roomId = data.getData().get(position).getId();
                        roomName = data.getData().get(position).getName();
                    }
                    AuditBean auditBean = new AuditBean();
                    auditBean.setUid(cUid);
                    auditBean.setToken(cToken);
                    auditBean.setName(mobile);
                    auditBean.setCommunity_id(communityId);     //社区ID
                    auditBean.setProperty_id(roomId);           //房间ID
                    auditBean.setUnit_id(unitId);               //单元ID
                    auditBean.setType("5");                     //默认身份为"其他"

                    GetDataBiz.submitCommunityData(area, auditBean, new HttpRequestListener() {
                        @Override
                        public void onHttpRequestFinish(String result) throws JSONException {
                            JSONObject jsonObject = new JSONObject(result);
                            if (jsonObject.getString("code").equals("200")){
                                ToastUtils.showToast(jsonObject.getString("message"));
                                closeDelayCurrentView();

                            }else {
                                ToastUtils.showToast(jsonObject.getString("message"));
                                closeDelayCurrentView();
                            }
                        }

                        @Override
                        public void onHttpRequestError(String error) {
                            ToastUtils.showToast("网络出错");
                        }
                    });
                    break;

                case R.id.btn_unit_cancel://取消添加社区
                    closeDelayCurrentView();
                    break;

            }
        }
    }

    private void closeDelayCurrentView() {
        new CountDownTimer(500, 500){
            @Override
            public void onFinish() {
                LogUtil.e("flag= " + flag);
                getFragmentManager().popBackStack();
            }

            @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
