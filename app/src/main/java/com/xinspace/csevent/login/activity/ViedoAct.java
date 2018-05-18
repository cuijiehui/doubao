package com.xinspace.csevent.login.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.CoresunApp;
import com.xinspace.csevent.app.weiget.SDPreference;
import com.xinspace.csevent.util.LogUtil;

/**
 * Created by Android on 2016/11/4.
 *
 * 影檬 H5界面
 *
 */
public class ViedoAct extends Activity {
    private FrameLayout videoview;// 全屏时视频加载view
    //private Button videolandport;
    private WebView videowebview;
    private Boolean islandport = true;//true表示此时是竖屏，false表示此时横屏。
    private View xCustomView;
    private xWebChromeClient xwebchromeclient;

   // private String url = "http://ym.lemon95.com/lacel/index/auto?secretKey=3FBE81EF167FFB4B1C8F8CD4340B36943BC535C8530F586610BCD6D99F516C770FA1CDA553A0D75295C71781A8C3095FB50388F56D00FF617077D4BC53FB152E62884E63744D1C1F9CBF279130E676FA&color=ef5948&title=时讯";
   // private String url = "http://ym.lemon95.com/lacel/index/auto?SecretKey=07059c7e010345a3ad302630d78a5315&AppKey=1260758855&Mobile=&NickName=&title=拾得影院&color=ef5948";

    private String NickName ;
    private String Mobile ;

    private String url ;

    private WebChromeClient.CustomViewCallback 	xCustomViewCallback;

    private ImageView iv_load;
    private AnimationDrawable animationDrawable;

    private SDPreference preference;

    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preference = SDPreference.getInstance();

        time = System.currentTimeMillis();
        preference.putContent("video" , "1");
        preference.putContent("videoStartTime" , time);
        setContentView(R.layout.act_video);

        initwidget();
        initListener();

    }

    private void initListener() {
        // TODO Auto-generated method stub
        //videolandport.setOnClickListener(new Listener());
    }


    @Override
    protected void onResume() {
        super.onResume();
        NickName = CoresunApp.username;
        Mobile = CoresunApp.USER_TEL;
        url = "http://ym.lemon95.com/lacel/index/auto?SecretKey=07059c7e010345a3ad302630d78a5315&AppKey=1260758855&Mobile=" + Mobile +"&NickName="+ NickName +"&color=ea5205&title=拾得影院";
        //url = "http://shop.coresun.net/weixin/test.html";
        LogUtil.i("url" + url);

        videowebview.loadUrl(url);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        time = System.currentTimeMillis();
        preference.putContent("videoEndTime" , time);
    }

    private void initwidget() {
        // TODO Auto-generated method stub
        videoview = (FrameLayout) findViewById(R.id.video_view);
        iv_load = (ImageView) findViewById(R.id.iv_load);
        //MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.animation_load_img,iv_load,null,null);

        iv_load.setImageResource(R.drawable.animation_load_img);
        animationDrawable = (AnimationDrawable) iv_load.getDrawable();
        animationDrawable.start();

        //videolandport = (Button) findViewById(R.id.video_landport);
        videowebview = (WebView) findViewById(R.id.video_webview);
        WebSettings ws = videowebview.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setAllowFileAccess(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setUseWideViewPort(true);
        ws.setSupportMultipleWindows(true);
        ws.setLoadWithOverviewMode(true);
        ws.setAppCacheEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setGeolocationEnabled(true);
        ws.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        ws.setPluginState(WebSettings.PluginState.ON_DEMAND);
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        /**
         * setAllowFileAccess 启用或禁止WebView访问文件数据 setBlockNetworkImage 是否显示网络图像
         * setBuiltInZoomControls 设置是否支持缩放 setCacheMode 设置缓冲的模式
         * setDefaultFontSize 设置默认的字体大小 setDefaultTextEncodingName 设置在解码时使用的默认编码
         * setFixedFontFamily 设置固定使用的字体 setJavaSciptEnabled 设置是否支持Javascript
         * setLayoutAlgorithm 设置布局方式 setLightTouchEnabled 设置用鼠标激活被选项
         * setSupportZoom 设置是否支持变焦
         * */

        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕

		/*ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);// 启用地理定位
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);*/
        xwebchromeclient = new xWebChromeClient();
        videowebview.setWebChromeClient(xwebchromeclient);
        videowebview.setWebViewClient(new xWebViewClientent());
        //videowebview.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        videowebview.setOverScrollMode(View.SCROLL_AXIS_NONE);

        videowebview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                iv_load.setVisibility(View.GONE);
                videowebview.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.i("www拦截" + url);
//                String a = "http://ym.lemon95.com/lacel/customer/private/userManage?secretKey=67E1325C46C1388F021BEF72B26A809CB7000606181262735C40E180162940145888B5205D8F0AB4AA099EDFCC8FC2C334168565E1D9058692C142BE30FD6A5D28A781730C6D4B3076F4C45CFF6DEB1ED5D9CD3D04A5EE5A";
//                String b = "http://ym.lemon95.com/lacel/customer/private/loginOut?secretKey=67E1325C46C1388F021BEF72B26A809CB7000606181262735C40E180162940145888B5205D8F0AB4AA099EDFCC8FC2C334168565E1D9058692C142BE30FD6A5D28A781730C6D4B3076F4C45CFF6DEB1ED5D9CD3D04A5EE5A";

                String a = "http://ym.lemon95.com/lacel/index/rigister?";
                String b = "http://ym.lemon95.com/lacel/customer/private/loginOut?";
                String c = "http://ym.lemon95.com/lacel/index/login?";
                String d = "http://ym.lemon95.com/lacel/customer/private/userManage?";
                String f = "http://ym.lemon95.com/lacel/customer/private/submit?";

                if (url.contains(a) || url.equals(c) || url.contains(d) || url.contains(f)){
                    if (!NickName.equals("") && !Mobile.equals("")){

                    }else{
                        Intent intent = new Intent(ViedoAct.this , LoginActivity.class);
                        intent.putExtra("flag" , "video");
                        startActivity(intent);
                        ViedoAct.this.finish();
                    }
                }else if (url.contains(b)){
                    CoresunApp.USER_ID = null;
                    //将全局的个人信息置空
                    CoresunApp.username = "";
                    CoresunApp.userIcon = null;
                    CoresunApp.USER_TEL = "";
                    ViedoAct.this.finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
			/*case R.id.video_landport:
				if (islandport) {
					Log.i("testwebview", "竖屏切换到横屏");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					videolandport.setText("全屏不显示该按扭，点击切换竖屏");
				}else {

					Log.i("testwebview", "横屏切换到竖屏");
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					videolandport.setText("全屏不显示该按扭，点击切换横屏");
				}
				break;
*/
                default:
                    break;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                hideCustomView();
                return true;
            }else {
                if (videowebview.canGoBack()){
                    videowebview.goBack();
                }else{
                    videowebview.loadUrl("about:blank");
//		       		 mTestWebView.loadData("", "text/html; charset=UTF-8", null);
                    ViedoAct.this.finish();
                    Log.i("testwebview", "===>>>2");
                }
            }
        }
        return true;
    }

    /**
     * 判断是否是全屏
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }
    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
    }
    /**
     * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
     * @author
     */
    public class xWebChromeClient extends WebChromeClient {
        private Bitmap xdefaltvideo;
        private View xprogressvideo;
        @Override
        //播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view, CustomViewCallback callback)
        {
            if (islandport) {

            }
            else{

//				ii = "1";
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            videowebview.setVisibility(View.GONE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            videoview.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoview.setVisibility(View.VISIBLE);
        }

        @Override
        //视频播放退出全屏会被调用的
        public void onHideCustomView() {

            if (xCustomView == null)//不是全屏播放状态
                return;

            // Hide the custom view.
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            videoview.removeView(xCustomView);
            xCustomView = null;
            videoview.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();

            videowebview.setVisibility(View.VISIBLE);

            //Log.i(LOGTAG, "set it to webVew");
        }
        //视频加载添加默认图标
        @Override
        public Bitmap getDefaultVideoPoster() {
            //Log.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (xdefaltvideo == null) {
                xdefaltvideo = BitmapFactory.decodeResource(
                        getResources(), R.drawable.loading);
            }
            return xdefaltvideo;
        }
        //视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            //Log.i(LOGTAG, "here in on getVideoLoadingPregressView");

            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater.from(ViedoAct.this);
                xprogressvideo = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }
        //网页标题
        @Override
        public void onReceivedTitle(WebView view, String title) {
            (ViedoAct.this).setTitle(title);
        }

//         @Override
//       //当WebView进度改变时更新窗口进度
//         public void onProgressChanged(WebView view, int newProgress) {
//        	 (OpenDoorActivity.this).getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress*100);
//         }

    }

    /**
     * 处理各种通知、请求等事件
     * @author
     */
    public class xWebViewClientent extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.i("webviewtest" + "shouldOverrideUrlLoading: "+ url);
            return true;
        }
    }

    /**
     * 当横竖屏切换时会调用该方法
     * @author
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("testwebview", "=====<<<  onConfigurationChanged  >>>=====");
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.i("webview", "   现在是横屏1");
            islandport = false;
        }else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i("webview", "   现在是竖屏1");
            islandport = true;
        }
    }

}
