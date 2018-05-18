package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xinspace.csevent.R;
import com.xinspace.csevent.login.adapter.MoreQuesAdapter;
import com.xinspace.csevent.login.model.HotQuestionBean;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android on 2016/11/10.
 */
public class MoreQuesAct extends BaseActivity{


    private LinearLayout ll_back;
    private Intent intent;
    private List<HotQuestionBean> list;
    private List<HotQuestionBean> dataList;
    private PullToRefreshListView lv_more_ques;
    private MoreQuesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
        setContentView(R.layout.act_moreques);

        intent = getIntent();
        if (intent != null ){
            list = (List<HotQuestionBean>) intent.getSerializableExtra("data");
        }

        dataList = new ArrayList<HotQuestionBean>();
        for (int i = 5 ; i < list.size() ; i++){
            dataList.add(list.get(i));
        }
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clickListener = null;
    }

    private void initView() {
        ll_back  = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);

        lv_more_ques = (PullToRefreshListView) findViewById(R.id.lv_more_ques);
        adapter = new MoreQuesAdapter(MoreQuesAct.this);
        adapter.setDataList(dataList);
        lv_more_ques.setAdapter(adapter);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    MoreQuesAct.this.finish();
                    break;
            }
        }
    };

}
