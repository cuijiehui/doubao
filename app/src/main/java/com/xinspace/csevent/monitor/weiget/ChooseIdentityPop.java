package com.xinspace.csevent.monitor.weiget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xinspace.csevent.monitor.adapter.IdentityAdapter;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.ScreenUtils;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择身份弹窗
 *
 * Created by Android on 2017/6/5.
 */

public class ChooseIdentityPop extends PopupWindow{

    private View view;
    private Context context;
    private SDPreference preference;
    private String cUid;
    private String token;

    private TextView tv_submit;
    private TextView tv_cancel;
    private IdentityAdapter adapter;

    private List<String> identityList = new ArrayList<>();
    private int current = -1;
    private ListView lv_code;
    private TextView textView;

    private IdentityResultListener identityResultListener;

    public ChooseIdentityPop(Context context , IdentityResultListener identityResultListener) {
        super(context);
        this.context = context;
        this.identityResultListener = identityResultListener;
        view = LayoutInflater.from(context).inflate(R.layout.pop_choose_code, null);
        this.setContentView(view);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(ScreenUtils.getScreenHeight(context) - ScreenUtils.getStatusBarHeight(context) - ScreenUtils.dpToPx(context , Float.valueOf(45)));

        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        initView();
        initData();
    }

    private void initData() {

        preference = SDPreference.getInstance();
        cUid = preference.getContent("cUid");
        token = preference.getContent("cToken");

        identityList.add("业主");
        identityList.add("家人");
        identityList.add("物业员工");
        identityList.add("物业高管");
        identityList.add("其他");

        tv_submit = (TextView) view.findViewById(R.id.tv_submit);
        tv_submit.setOnClickListener(clickListener);

        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(clickListener);

        textView = (TextView) view.findViewById(R.id.text);
        textView.setText("选择身份");

        lv_code = (ListView) view.findViewById(R.id.lv_code);
        adapter = new IdentityAdapter(context);
        adapter.setList(identityList);
        lv_code.setAdapter(adapter);


        lv_code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                current = position;
                adapter.setSelectItem(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {

    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_submit:
                    if (current != -1){
                        identityResultListener.identityResult(identityList.get(current));
                        ChooseIdentityPop.this.dismiss();
                    }else{
                        ToastUtil.makeToast("未选择身份");
                    }
                    break;
                case R.id.tv_cancel:

                    ChooseIdentityPop.this.dismiss();

                    break;
            }
        }
    };

}
