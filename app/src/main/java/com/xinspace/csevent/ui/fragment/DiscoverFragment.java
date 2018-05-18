package com.xinspace.csevent.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.adapter.DiscoverSoftwareAdapter;
import com.xinspace.csevent.data.biz.AddIntegralBiz;
import com.xinspace.csevent.data.biz.GetDiscoverListBiz;
import com.xinspace.csevent.customview.CustomDownloadSuccessDialog;
import com.xinspace.csevent.data.entity.SoftwareEntity;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.myinterface.Observer;
import com.xinspace.csevent.util.parser.JsonPaser;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.NewcomersTutorialUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.SoftwareDownloadActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiscoverFragment extends Fragment implements Observer{
    private View view;
    private ListView listview;
    private List<Object> softwares;//推荐软件的列表
    private Map<Integer,Integer> stateMap;//保存下载状态的集合
    private Map<Integer,String> percentMap;//保存下载进度的集合
    private Map<Integer,String> apkPathList;//保存apk路径的集合
    private NetworkChangeReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover,null);

        //添加到观察者队列
        CoresunApp.downloadSubject.Attach(this);

        setView();
        setListener();
        getAdvertisementList();
        /**判断是不是第一次使用程序,是则加载新手指导页面*/
        NewcomersTutorialUtil.loadToNewcomersTutorial(getActivity(),"discoverPage",R.layout.dialog_tutorial_for_discover);

        registerReceiver();
        return view;
    }

    //注册广播接收器
    private void registerReceiver() {
        receiver=new NetworkChangeReceiver();
        IntentFilter filter=new IntentFilter(Const.ACTION_CONNECTIVITY_CHANGE);
        getActivity().registerReceiver(receiver,filter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消广播接收器的注册
        getActivity().unregisterReceiver(receiver);
    }
    //设置监听器
    private void setListener() {
        //进入相应的软件下载页面
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), SoftwareDownloadActivity.class);
                intent.putExtra("data",(SoftwareEntity)softwares.get(position));
                intent.putExtra("position",position);
                intent.putExtra("state", stateMap.get(position));
                intent.putExtra("percentage", percentMap.get(position));
                intent.putExtra("apkPath",apkPathList.get(position));
                startActivity(intent);
            }
        });
    }
    //处理软件下载成功之后,添加积分接口返回的信息
    private void handleDownloadResponse(String result) {
        try {
            JSONObject obj=new JSONObject(result);
            String res=obj.getString("result");
            if(res.equals("200")){
                //成功添加积分
                String integral=obj.getString("integral");
                LogUtil.i("奖励的积分:"+integral);
                CustomDownloadSuccessDialog dialog=CustomDownloadSuccessDialog.getInstance(getActivity());
                dialog.show();
                dialog.setText(integral+"积分");
            }else if(res.equals("201")){
                //已经下载过了,不能再次添加积分
                ToastUtil.makeToast("已下载过该软件,不能重复获取积分");
            }else if(res.equals("202")){
                //添加积分异常
                ToastUtil.makeToast("添加积分错误");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //设置推荐软件的Adapter
    private void setAdapter() {
        DiscoverSoftwareAdapter adapter=new DiscoverSoftwareAdapter(softwares,getActivity());

        //添加到观察者队列
        CoresunApp.downloadSubject.Attach(adapter);
        //创建保存所有下载状态的集合
        stateMap =new HashMap<>();
        percentMap =new HashMap<>();
        apkPathList=new HashMap<>();
        for (int i=0;i<softwares.size();i++){
            stateMap.put(i,Const.DOWNLOAD_STATE_READY);
        }
        listview.setAdapter(adapter);
    }
    //获取活动列表
    private void getAdvertisementList() {
        GetDiscoverListBiz.getdiscover(new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("发现数据:"+result);
                JSONObject obj=new JSONObject(result);
                JSONArray ary = obj.getJSONArray("data");
                softwares = JsonPaser.parserAry(ary.toString(), SoftwareEntity.class);
                //显示列表
                setAdapter();
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }
    //初始化组件
    private void setView() {
        listview= (ListView) view.findViewById(R.id.lv_discover_listview);
    }
    //观察者回调
    @Override
    public void updateDownloadState(int state, String percentage, int position,String apkPath) {
        LogUtil.i("fragment观察者:"+state+";"+percentage+";"+position);
        //更新下载状态,和进度
        stateMap.put(position,state);
        percentMap.put(position,percentage);
        apkPathList.put(position,apkPath);

        //查询集合的大小
        LogUtil.i("集合大小:"+stateMap.size());

        //如果percentag为null，则赋值0
        if(percentage==null){
            percentage="0";
        }
        View childView = listview.getChildAt ( position - listview.getFirstVisiblePosition());

        //如果这个view不可见的话,就不更新ui了
        if(null==childView) return;

        LogUtil.i("位置是:"+position);
        LogUtil.i("找到的位置view:"+childView);
        TextView tv_percentage = (TextView) childView.findViewById(R.id.tv_discover_percentage);
        LogUtil.i("找到的tv:"+tv_percentage);

        TextView tv_state= (TextView) childView.findViewById(R.id.tv_discover_download);

        //显示进度
        if(tv_percentage.getVisibility()==View.GONE){
            tv_percentage.setVisibility(View.VISIBLE);
        }
        tv_percentage.setText(percentage+"%");
        //显示状态
        if(state== Const.DOWNLOAD_STATE_DOWNLOADING){//下载中
            tv_state.setText("暂停");
        }else if(state==Const.DOWNLOAD_STATE_PAUSE){
            tv_state.setText("继续");
        }else if(state==Const.DOWNLOAD_STATE_FINISH){
            tv_state.setText("打开");
            tv_state.setBackgroundResource(R.drawable.selector_tv_state_open_software);
            //隐藏进度
            if(tv_percentage.getVisibility()==View.VISIBLE){
                tv_percentage.setVisibility(View.GONE);
            }
            //下载完成之后调用奖励积分的接口
            SoftwareEntity enty = (SoftwareEntity) softwares.get(position);
            AddIntegralBiz.addIntegral(getActivity(), enty.getId(), new HttpRequestListener() {
                @Override
                public void onHttpRequestFinish(String result) throws JSONException {
                    //下载软件奖励积分
                    handleDownloadResponse(result);
                }

                @Override
                public void onHttpRequestError(String error) {

                }
            });
        }
    }
    //网络状态改变的广播接收器
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Const.ACTION_CONNECTIVITY_CHANGE)){
                getAdvertisementList();
            }
        }
    }
}
