package com.xinspace.csevent.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xinspace.csevent.monitor.activity.MyLeaseAct;
import com.xinspace.csevent.R;
import com.xinspace.csevent.app.view.CircleImageView;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.baskorder.activity.MyBaskOrderAct;
import com.xinspace.csevent.data.biz.GetProfileBiz;
import com.xinspace.csevent.data.entity.ProfileDataEntity;
import com.xinspace.csevent.login.activity.CallCenterAct;
import com.xinspace.csevent.login.activity.CrowdRecordAct;
import com.xinspace.csevent.login.activity.CrowdWinRecordAct;
import com.xinspace.csevent.login.activity.MyMessageAct;
import com.xinspace.csevent.login.activity.SettingAct;
import com.xinspace.csevent.login.weiget.RechargeDialog;
import com.xinspace.csevent.login.weiget.SsRecordPop;
import com.xinspace.csevent.login.weiget.WinRecordPop;
import com.xinspace.csevent.myinterface.HttpRequestListener;
import com.xinspace.csevent.shop.activity.ConvertRecordAct;
import com.xinspace.csevent.shop.activity.FreeTrialAct;
import com.xinspace.csevent.shop.activity.MyGroupAct;
import com.xinspace.csevent.shop.activity.StoreOrderAct;
import com.xinspace.csevent.sweepstake.activity.CrowdBuyRecordAct;
import com.xinspace.csevent.util.Const;
import com.xinspace.csevent.util.ImagerLoaderUtil;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.AddressManagerActivity;
import com.xinspace.csevent.login.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.framework.ShareSDK;

public class MinePageFragment extends Fragment {

    private CircleImageView iv_photo;
    private View view;
    private RelativeLayout relUserMessage;
    private TextView tvCharge;

    private static final int REQUEST_CODE_REFRESH_MY_INFO = 240;//更新我的个人资料
    public static final int REQUEST_CODE_GO_INTO_LOGIN = 200;//启动登录页面请求码
    public static final int REQUEST_CODE_GO_INTO_CHARGE = 201;//启动充值页面请求码

    private TextView tvTel;
    private TextView tvIntegral;
    private RelativeLayout relMineSnatch, relAwardLog, relSaleLog, relMineOrder, relMyAddress,
            relCallCenter, relMySetting;
    private String jpegPath = Const.APK_STEORY_PATH + "profile.jpeg";

    private SDPreference preference;

    private ScrollView scrollView;

    private WinRecordPop winRecordPop;

    private SsRecordPop ssRecordPop;

    private RechargeDialog rechargeDialog;

    private String integral;
    private String gold;
    private List<Object> enty_list;
    private ProfileDataEntity enty;

    private RelativeLayout rel_all_order;
    private LinearLayout lin_obligation; //待付款
    private LinearLayout lin_no_send; //待发货
    private LinearLayout lin_wait_receiving; //待收货
    private LinearLayout lin_no_evaluate; //待评价
    private LinearLayout lin_after_sale; //售后退货

    private LinearLayout lin_spell_group;       // 我的拼团
    private LinearLayout lin_try_out;          // 我的试用
    private LinearLayout lin_my_integral;     // 我的积分
    private LinearLayout lin_make_money;     // 我要赚钱

    private LinearLayout lin_my_collect;     //我的收藏
    private LinearLayout lin_system_notice;  //系统消息
    private LinearLayout lin_my_service;     // 我的客服
    private LinearLayout lin_my_coupon;      // 优惠卷

    private ImageView iv_my_setting;
    private String userId;
    private String openId;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    if (msg.obj != null) {
                        enty = (ProfileDataEntity) msg.obj;
                        tvTel.setText(enty.getNickname());


                        if (!TextUtils.isEmpty(enty.getAvatar())){
                            ImagerLoaderUtil.displayImage(enty.getAvatar() , iv_photo);
                        }else{
                            ImagerLoaderUtil.displayImage( "drawable://" + R.drawable.icon_userhead_no_login , iv_photo);
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine_page3, null);

        preference = SDPreference.getInstance();
        setView();
        setListener();
        isShowGuide();
        return view;
    }

    //判断是否显示新手教程
    private void isShowGuide() {
        //NewcomersTutorialUtil.loadToNewcomersTutorial(getActivity(), "minePage", R.layout.dialog_tutorial_for_mine);
    }


    //每次在"我的"页面都获取个人信息
//    @Override
//    public void onResume() {
//        super.onResume();
//        isLogin();
//        LogUtil.i("我的resume()");
//    }

    @Override
    public void onResume() {
        super.onResume();
        isLogin();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //判断是否已经登录
    private void isLogin() {
        userId = preference.getContent("userId");
        String tel = preference.getContent("tel");

        if (userId != null && !userId.equals("0")) {
            LogUtil.i("www" + "userid" + userId + "名字" + tel);
            tvTel.setText(tel);
            //已登录
            openId = preference.getContent("openid");
            getProfile();
            getUserProfile();

        } else {
            tvTel.setText("拾得用户");
            tvIntegral.setText("剩余0积分");
            ImagerLoaderUtil.displayImage("drawable://" + R.drawable.icon_userhead_no_login, iv_photo);
        }
    }

    //获取用户个人积分
    public void getProfile() {
        if (userId.equals("0")) {
            return;
        }
        GetProfileBiz.getProfile2(openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                LogUtil.i("www" + jsonObject.getString("message"));
                showProfile(result);
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    public void getUserProfile() {
        if (userId.equals("0")) {
            return;
        }
        GetProfileBiz.getProfile3(openId, new HttpRequestListener() {
            @Override
            public void onHttpRequestFinish(String result) throws JSONException {
                LogUtil.i("会员信息" + result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("code").equals("200")) {
                    Gson gson = new Gson();
                    ProfileDataEntity bean = gson.fromJson(jsonObject.getJSONObject("data").toString(), ProfileDataEntity.class);
                    handler.obtainMessage(200 , bean).sendToTarget();
                }
            }

            @Override
            public void onHttpRequestError(String error) {

            }
        });
    }


    //回调
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //更新个人信息
        if (requestCode == REQUEST_CODE_REFRESH_MY_INFO && resultCode == Activity.RESULT_OK) {
            //getProfile();
        }
        //充值成功
        if (requestCode == REQUEST_CODE_GO_INTO_CHARGE && resultCode == Activity.RESULT_OK) {
            ToastUtil.makeToast("充值成功");
        }
        //登录成功
        if (requestCode == REQUEST_CODE_GO_INTO_LOGIN && resultCode == Activity.RESULT_OK) {
            try {
                //getProfile();
                LogUtil.i("登陆成功了");
                Intent intent = new Intent(Const.ACTION_TO_CHECK_THE_STATE_OF_QIAN_DAO);
                getActivity().sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(getContext());
    }

    //设置监听

    private void setListener() {
        //完善个人信息
//        ll_youhuiquan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (CoresunApp.USER_ID == null) {
//                    ToastUtil.makeToast("请先登录");
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), SupplyInfoActivity.class);
//                    startActivityForResult(intent, REQUEST_CODE_REFRESH_MY_INFO);
//                }
//            }
//        });
        //夺宝记录
//        ll_my_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        //中奖记录
//        ll_zhongjiang_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        //会员中心
//        ll_my_order.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (CoresunApp.USER_ID == null) {
//                    ToastUtil.makeToast("请先登录");
//                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), MemberCenterActivity.class);
//                    startActivity(intent);
//                }
//            }
//        });
        //关于我们
//        ll_about_us.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AboutUsActivity.class));
//            }
//        });
        //退出登录
//        ll_logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logout();
//            }
//        });
        //上传头像
//        iv_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.profile_image: // 点击头像
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyMessageAct.class);
                        intent.putExtra("data", enty);
                        startActivity(intent);
                    }
                    break;
                case R.id.bt_mine_charge: // 充值
//                    Intent intentCharge = new Intent(getActivity(), ReChargeActivity.class);
//                    startActivity(intentCharge);
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        showDialog();
                    }
                    break;
                case R.id.rel_mine_snatch: //夺宝记录
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //showSSRecordPop();
                        Intent intent = new Intent(getActivity(), CrowdRecordAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_award_log: //中奖记录
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        //showWinRecordPop();
                        Intent intent = new Intent(getActivity(), CrowdWinRecordAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_sale_log:  //购买记录
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), CrowdBuyRecordAct.class);
                        intent.putExtra("flag", "home");
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_mine_order: //我的晒单
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyBaskOrderAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_my_address: //收货地址
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), AddressManagerActivity.class);
                        intent.putExtra("flag", "home");
                        startActivity(intent);
                    }
                    break;
                case R.id.rel_call_center: //客服中心
                    Intent intent1 = new Intent(getActivity(), CallCenterAct.class);
                    startActivity(intent1);

                    break;
                case R.id.rel_my_setting:  //我的设置
                    Log.i("www", "点击我的设置了");
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SettingAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.iv_my_setting:
                    Log.i("www", "点击我的设置了");
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), SettingAct.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };


    private void showWinRecordPop() {
        if (winRecordPop == null) {
            winRecordPop = new WinRecordPop(getActivity());
        }

        if (!winRecordPop.isShowing()) {
            winRecordPop.showAtLocation(scrollView, Gravity.BOTTOM, 0, 0);
            winRecordPop.isShowing();
        } else {
            winRecordPop.dismiss();
        }
    }

    private void showSSRecordPop() {
        if (ssRecordPop == null) {
            ssRecordPop = new SsRecordPop(getActivity());
        }

        if (!ssRecordPop.isShowing()) {
            ssRecordPop.showAtLocation(scrollView, Gravity.BOTTOM, 0, 0);
            ssRecordPop.isShowing();
        } else {
            ssRecordPop.dismiss();
        }
    }


    //点击头像更换
    private void changeMyPhoto() {
        if (userId != null && userId.equals("0")) {
            ToastUtil.makeToast("请先登录");
        } else {
            //弹出选择框
            //showPopupWindow();
        }
    }


    //初始化组件
    private void setView() {

        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        iv_photo = (CircleImageView) view.findViewById(R.id.profile_image);
        iv_photo.setOnClickListener(clickListener);

        tvCharge = (TextView) view.findViewById(R.id.bt_mine_charge);
        tvCharge.setOnClickListener(clickListener);

        relUserMessage = (RelativeLayout) view.findViewById(R.id.ll_profile_container);

        tvTel = (TextView) view.findViewById(R.id.tv_mine_tel);
        tvIntegral = (TextView) view.findViewById(R.id.tv_mine_integral);

        relMineSnatch = (RelativeLayout) view.findViewById(R.id.rel_mine_snatch);
        relAwardLog = (RelativeLayout) view.findViewById(R.id.rel_award_log);
        relSaleLog = (RelativeLayout) view.findViewById(R.id.rel_sale_log);
        relMineOrder = (RelativeLayout) view.findViewById(R.id.rel_mine_order);
        relMyAddress = (RelativeLayout) view.findViewById(R.id.rel_my_address);
        relCallCenter = (RelativeLayout) view.findViewById(R.id.rel_call_center);
        relMySetting = (RelativeLayout) view.findViewById(R.id.rel_my_setting);

        relMineSnatch.setOnClickListener(clickListener);
        relAwardLog.setOnClickListener(clickListener);
        relSaleLog.setOnClickListener(clickListener);
        relMineOrder.setOnClickListener(clickListener);
        relMyAddress.setOnClickListener(clickListener);
        relCallCenter.setOnClickListener(clickListener);
        relMySetting.setOnClickListener(clickListener);

        rel_all_order = (RelativeLayout) view.findViewById(R.id.rel_all_order);
        rel_all_order.setOnClickListener(onClickListener);

        lin_obligation = (LinearLayout) view.findViewById(R.id.lin_obligation);
        lin_no_send = (LinearLayout) view.findViewById(R.id.lin_no_send);
        lin_wait_receiving = (LinearLayout) view.findViewById(R.id.lin_wait_receiving);
        lin_no_evaluate = (LinearLayout) view.findViewById(R.id.lin_no_evaluate);
        lin_after_sale = (LinearLayout) view.findViewById(R.id.lin_after_sale);

        lin_obligation.setOnClickListener(onClickListener);
        lin_no_send.setOnClickListener(onClickListener);
        lin_no_evaluate.setOnClickListener(onClickListener);
        lin_wait_receiving.setOnClickListener(onClickListener);
        lin_after_sale.setOnClickListener(onClickListener);

        lin_spell_group = (LinearLayout) view.findViewById(R.id.lin_spell_group);
        lin_try_out = (LinearLayout) view.findViewById(R.id.lin_try_out);
        lin_my_integral = (LinearLayout) view.findViewById(R.id.lin_my_integral);
        lin_make_money = (LinearLayout) view.findViewById(R.id.lin_make_money);

        lin_spell_group.setOnClickListener(onClickListener);
        lin_try_out.setOnClickListener(onClickListener);
        lin_my_integral.setOnClickListener(onClickListener);
        lin_make_money.setOnClickListener(onClickListener);

        lin_my_collect = (LinearLayout) view.findViewById(R.id.lin_my_collect);
        lin_system_notice = (LinearLayout) view.findViewById(R.id.lin_system_notice);
        lin_my_service = (LinearLayout) view.findViewById(R.id.lin_my_service);
        lin_my_coupon = (LinearLayout) view.findViewById(R.id.lin_my_coupon);

        lin_my_collect.setOnClickListener(onClickListener);
        lin_system_notice.setOnClickListener(onClickListener);
        lin_my_service.setOnClickListener(onClickListener);
        lin_my_coupon.setOnClickListener(onClickListener);

        iv_my_setting = (ImageView) view.findViewById(R.id.iv_my_setting);
        iv_my_setting.setOnClickListener(clickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_all_order:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StoreOrderAct.class);
                        intent.putExtra("flag", "0");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_obligation:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StoreOrderAct.class);
                        intent.putExtra("flag", "1");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_no_send:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StoreOrderAct.class);
                        intent.putExtra("flag", "2");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_wait_receiving:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StoreOrderAct.class);
                        intent.putExtra("flag", "3");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_no_evaluate:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), StoreOrderAct.class);
                        intent.putExtra("flag", "4");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_spell_group: // 我的拼团
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyGroupAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_my_integral:   //积分兑换
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ConvertRecordAct.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_try_out:    //我的试用
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), FreeTrialAct.class);
                        intent.putExtra("flag" , "address");
                        startActivity(intent);
                    }
                    break;
                case R.id.lin_make_money:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), MyLeaseAct.class);
                        startActivity(intent);
                    }
                    break;

                case R.id.lin_after_sale:
                case R.id.lin_my_collect:
                case R.id.lin_system_notice:
                case R.id.lin_my_service:
                case R.id.lin_my_coupon:
                    if (userId != null && userId.equals("0")) {
                        ToastUtil.makeToast("请先登录");
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtil.makeToast("此功能待完善");
                    }
                    break;
            }
        }
    };

    //显示个人信息
    private void showProfile(String result) throws JSONException {

        Log.i("www", "显示个人信息" + result);


        JSONObject jsonObject = new JSONObject(result);
        if (jsonObject.getString("code").equals("200")) {
            JSONObject dataJsonObject = jsonObject.getJSONObject("data");
            integral = dataJsonObject.getString("integral");
            preference.putContent("integral", integral);
        }

//        enty_list = JsonPaser2.parserAry(result, ProfileDataEntity.class, "data");
//        enty = (ProfileDataEntity) enty_list.get(0);
//        String tel = enty.getTel();
//        String nickName = enty.getNickname();
//
//        CoresunApp.USER_TEL = tel;
//        CoresunApp.username = nickName;
//
//        integral = enty.getIntegral();
//        gold = enty.getGold();
//        this.integral = enty.getIntegral();
//
//        Log.i("www" , "执行个人信息显示:" + result);
//        //因为服务器有缓存,所以重新请求头像地址后加个一个随机数
//        String image = enty.getImage();
//        Log.i("www" , "userImage" + image) ;
//
//        //属于第三方登录
//        if (tel.equals("0")) {
//            if (nickName.equals("")) {
//                tvTel.setText(CoresunApp.username);
//            } else {
//                //有昵称则显示昵称,否则显示第三方昵称
//                tvTel.setText(nickName);
//            }
//        } else {
//            if (nickName.equals("")) {
//                tvTel.setText(tel);
//            } else {
//                tvTel.setText(nickName);
//            }
//        }
//        if (image.equals("")) {
//            //为了区分"第一次用户注册时也没有头像"和"第三方账号登录的情况"
//            if (tel.equals("0")) {
//                //显示第三方账号的头像
//                ImagerLoaderUtil.displayImage(CoresunApp.userIcon, iv_photo);
//                preference.putContent("userImgPath" , CoresunApp.userIcon);
//            }
//        } else {
//            //因为服务器有缓存,所以重新请求头像地址后加个一个随机数
//            image += "?" + Math.random() * 1000000;
//            ImagerLoaderUtil.displayImage(image, iv_photo);
//            preference.putContent("userImgPath" , image);
//        }
        tvIntegral.setText("剩余" + integral + "积分");
    }


    private void showDialog() {
        rechargeDialog = new RechargeDialog(getActivity());
        rechargeDialog.setCanceledOnTouchOutside(false);
//        //元宝
//        rechargeDialog.setOnGoldListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ReChangeAct.class);
//                intent.putExtra("gold", gold);
//                startActivity(intent);
//                rechargeDialog.dismiss();
//            }
//        });

        // 积分
//        rechargeDialog.setOnIntegralListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity() , ExChangeAct.class);
//                intent.putExtra("gold" , gold);
//                startActivity(intent);
//                rechargeDialog.dismiss();
//            }
//        });
//        rechargeDialog.show();
    }

}
