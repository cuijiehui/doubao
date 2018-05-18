package com.xinspace.csevent.baskorder.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinspace.csevent.R;
import com.xinspace.csevent.app.utils.TimeHelper;
import com.xinspace.csevent.app.weiget.FileHelper;
import com.xinspace.csevent.baskorder.adapter.AlbumAdapter2;
import com.xinspace.csevent.baskorder.weiget.ImageFloder;
import com.xinspace.csevent.baskorder.weiget.ListImageDirPopupWindow;
import com.xinspace.csevent.util.LogUtil;
import com.xinspace.csevent.util.ToastUtil;
import com.xinspace.csevent.ui.activity.BaseActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class AlbumAct extends BaseActivity implements ListImageDirPopupWindow.OnImageDirSelected {
    public static AlbumAct2 instance = null;

    private TextView tv_preview;
    private TextView tv_yes;
    private LinearLayout ll_back;
    private TextView tv_cancel;

    private static final String TAG = "AlbumAct";
    private ProgressDialog mProgressDialog;

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所属的图片
     */
    private List<String> mImgs = new Vector<String>();

    /**
     * 所有的图片
     */
    private List<String> allImgs = new Vector<String>();

    private GridView mGirdView;
    private AlbumAdapter2 mAdapter;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    private RelativeLayout mBottomLy;

    private TextView mChooseDir;
    private TextView mImageCount;
    int totalCount = 0;
    private int mScreenHeight;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    private static final int ALBUM_PERMESHION = 9;

    private boolean isSuportFace = true;
    private boolean isSuportClip = false;
    private boolean isQrClip = false;
    private boolean isAlbumFisrt = false;

    private HashMap<String, Boolean> isSelected;
    private List<String> ch_images;

    private static final int GO_CAMERA = 1000;

    private ArrayList<String> list;

    private List<String> contentImgList;

    private Intent intent;

    private int hasImgSize;

    private String flag;

    private Cursor mCursor;

    private ContentResolver mContentResolver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (ch_images.size() != 0) {
                        ch_images.clear();
                    }
                    Iterator<Map.Entry<String, Boolean>> iterator = isSelected.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Boolean> entry = iterator.next();
                        String imagePath = entry.getKey();
                        ch_images.add(imagePath);
                    }
                    break;
                case 2:
                    Toast.makeText(AlbumAct.this, "最多添加9张图片", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    LogUtil.i("www" +"-------------最多添加5张图片-------------");
                    Toast.makeText(AlbumAct.this, "最多添加5张图片", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
        }
    };

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {

            LogUtil.i("11111111111111111111111111111");

            if (allImgs.isEmpty()) {
                Toast.makeText(getApplicationContext(), "手机暂时没有图片哦", Toast.LENGTH_SHORT).show();
            }
            mAdapter = new AlbumAdapter2(getApplicationContext(), allImgs, "",  handler,
                    isSelected, hasImgSize, flag);

        } else {

            LogUtil.i("22222222222222222222222222222222222");

            mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                        return true;
                    return false;
                }
            }));
            mImgs = new ArrayList<>(mImgs);
            mAdapter = new AlbumAdapter2(getApplicationContext(), mImgs, mImgDir.getAbsolutePath(),  handler,
                    isSelected, hasImgSize, flag);
        }

        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */

        mGirdView.setAdapter(mAdapter);
        mImageCount.setText(totalCount + "张");

        mGirdView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//                if (arg2 == 0) {
//                    Intent intent = new Intent(AlbumAct.this, CameraActivity.class);
//                    intent.putExtra(TakeImage.IS_FACE_DETECT, false);
//                    intent.putExtra("flag", "social");
//                    startActivityForResult(intent, GO_CAMERA);
//                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case ALBUM_PERMESHION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImages();
                } else {
                    //没有获取到相对于的权限,显示错误页面
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext()).inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_album);
        //EventBus.getDefault().register(this);
        //instance = this;
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;
        deleteAllFiles(FileHelper.getImgTypeFile("photo"));

        intent = getIntent();
        hasImgSize = intent.getIntExtra("size", 0);
        flag = intent.getStringExtra("flag");

        intentInit();
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            getImages();
        }
        initEvent();
    }

    private void intentInit() {
//        Intent intent = getIntent();
//        if (intent != null) {
//            isSuportFace = intent.getBooleanExtra(TakeImage.IS_FACE_DETECT, true);
//            isSuportClip = intent.getBooleanExtra(TakeImage.IS_PATH_CLIP, false);
//            isQrClip = intent.getBooleanExtra(TakeImage.IS_QR_CLIP, false);
//            isAlbumFisrt = intent.getBooleanExtra(TakeImage.IS_ALBUM_FIRST, false);
//        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        int code = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (code != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AlbumAct.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    AlbumAct.ALBUM_PERMESHION);
            return;
        }
        getImages();
    }

    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }

    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_back:
                    AlbumAct.this.finish();
                    break;
                case R.id.tv_cancel: //取消

                    break;
                case R.id.tv_preview: // 预览

                    break;
                case R.id.tv_yes:  // 确定
                    Collection<String> c = mAdapter.map.values();
                    Iterator<String> it = c.iterator();
                    for (; it.hasNext(); ) {
                        list.add(it.next());
                    }
                    LogUtil.i("www" + "选择图片张数" + list.size());
                    //EventBus.getDefault().post(list);
                    Intent intent = new Intent(AlbumAct.this, AddBaskOrderAct.class);
                    intent.putExtra("imgList", list);
                    setResult(1001, intent);
                    AlbumAct.this.finish();
                    break;
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LogUtil.i("按机械的返回键了");
        AlbumAct.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.destory();
        }
        mImgs = null;
        allImgs = null;
        mImageFloders = null;
        mDirPaths = null;
        System.gc();
        handler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacksAndMessages(null);
        clickListener = null;
        mCursor.close();
        mCursor = null;
        mContentResolver = null;
        mHandler.removeMessages(0);

    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        // sendBroadcast(new Intent(
        // Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
        // Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;
                mContentResolver = AlbumAct.this.getContentResolver();
                // 只查询jpeg和png的图片
                mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        "date_modified DESC");
                LogUtil.i("Cursor有没有关闭" + mCursor.isClosed());

                int i = 0;
                try {
                    while (mCursor.moveToNext()) {
                        i++;
                        String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        LogUtil.i("path" +  "path   " + path + "第几个" + i);
                        if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".jpeg")) {
                            allImgs.add(path);
                        }

                        // 拿到第一张图片的路径
                        if (firstImage == null) {
                            firstImage = path;
                        }
                        // 获取该图片的父路径名
                        File parentFile = new File(path).getParentFile();
                        if (parentFile == null)
                            continue;
                        String dirPath = parentFile.getAbsolutePath();
                        ImageFloder imageFloder = null;
                        // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                        if (mDirPaths.contains(dirPath)) {
                            continue;
                        } else {
                            mDirPaths.add(dirPath);
                            // 初始化imageFloder
                            imageFloder = new ImageFloder();
                            imageFloder.setDir(dirPath);
                            imageFloder.setFirstImagePath(path);
                        }

                        String[] srcs = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String filename) {
                                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                                        || filename.endsWith(".jpeg"))
                                    return true;
                                return false;
                            }
                        });
                        if (srcs == null) {
                            continue;
                        }
                        int picSize = srcs.length;
                        totalCount += picSize;

                        imageFloder.setCount(picSize);
                        mImageFloders.add(imageFloder);

                        if (picSize > mPicsSize) {
                            mPicsSize = picSize;
                            mImgDir = parentFile;
                        }
                    }
                    mCursor.close();
                    // 扫描完成，辅助的HashSet也就可以释放内存了
                    mDirPaths = null;
                    mImgDir = null;
                    // 通知Handler扫描图片完成
                    mHandler.sendEmptyMessage(0x110);
                    LogUtil.i("Cursor 的关闭对象" + mCursor.isClosed());

                } catch (NullPointerException e) {
                    // TODO: handle exception
                }
            }
        }).start();

    }

    /**
     * 初始化View
     */
    public void initView() {

        list = new ArrayList<String>();
        contentImgList = new ArrayList<String>();
        isSelected = new HashMap<String, Boolean>();
        ch_images = new ArrayList<String>();
        mGirdView = (GridView) findViewById(R.id.id_gridView);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.rel_bottom_ly);

        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(clickListener);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(clickListener);
        tv_preview = (TextView) findViewById(R.id.tv_preview);
        tv_yes = (TextView) findViewById(R.id.tv_yes);

        tv_preview.setOnClickListener(clickListener);
        tv_yes.setOnClickListener(clickListener);

    }

    private void initEvent() {
        /**
         * 为底部的布局设置点击事件，弹出popupWindow
         */
        mBottomLy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListImageDirPopupWindow != null) {
                    if (!mListImageDirPopupWindow.isShowing()) {
                        //mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                        mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1.0f;
                        getWindow().setAttributes(lp);
                    } else {
                        mListImageDirPopupWindow.dismiss();
                    }
                } else {
                    ToastUtil.makeToast("暂无其他相册");
                }

            }
        });
    }

    @Override
    public void selected(ImageFloder floder) {

        mImgDir = new File(floder.getDir());
        mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));

        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
        mAdapter = new AlbumAdapter2(getApplicationContext(), mImgs, mImgDir.getAbsolutePath(),  handler,
                isSelected, hasImgSize, flag);
        mGirdView.setAdapter(mAdapter);
        // mAdapter.notifyDataSetChanged();
        mImageCount.setText(floder.getCount() + "张");
        mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mListImageDirPopupWindow.isShowing()) {
            mListImageDirPopupWindow.dismiss();
        } else {
            AlbumAct.this.finish();
        }
        return true;
    }

    private void sendResult(String name) {
        if (isQrClip || isSuportClip) {
            Uri uri = Uri.fromFile(new File(name));
            if (isQrClip) {
                startClip(uri, 100);
            } else {
                startClip(uri, 300);
            }
        } else {
//            Intent intent = new Intent();
//            intent.putExtra(TakeImage.RESULT, name);
//            AlbumAct.this.setResult(Activity.RESULT_OK, intent);
//            AlbumAct.this.finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
//            case GO_CAMERA://如果这里再接收到回调,则代表着
//                if (resultCode == Activity.RESULT_OK && data != null) {
//                    String imgpath = data.getStringExtra(TakeImage.RESULT);
//                    if (imgpath != null) {
//                        list.add(imgpath);
//                    }
//                }
//                break;
//            case Crop.REQUEST_CROP:
//                if (resultCode == Activity.RESULT_OK) {
//                    sendResult(clipeOutput.getPath());
//                }
//                if (resultCode == Crop.RESULT_ERROR)
//                    SettingToast.otherToast(getApplicationContext(), "数据返回失败,请重试!");
//                break;
        }
    }


    private final File cacheDir = FileHelper.getCacheFile();
    private Uri clipeOutput;

    // 调用裁剪
    private void startClip(Uri uri, int size) {
        File outputFile = new File(cacheDir, String.valueOf(TimeHelper.getCurrentTimeMillis()));
        clipeOutput = Uri.fromFile(outputFile);
//        if (isQrClip) {
//            new Crop(uri).output(clipeOutput).withAspect(1, 1).withMaxSize(size, size).asQrPic().start(this);
//        } else {
//            new Crop(uri).output(clipeOutput).withAspect(1, 1).withMaxSize(size, size).setCropType(false).start(this);
//        }
    }







//    @Override
//    public void barClickRight() {
//        if (flag.equals("1") || flag.equals("2")) {
//            Collection<String> c = mAdapter.map.values();
//            Iterator<String> it = c.iterator();
//            for (; it.hasNext(); ) {
//                list.add(it.next());
//            }
//            if (flag.equals("1")) {                      // 9张
//                LogManger.i("www", "选择图片张数" + list.size());
//                EventBus.getDefault().post(list);
//            } else if (flag.equals("2")) {                // 5张
//                Intent intent = new Intent(AlbumAct.this, SendGoodsAttrAct.class);
//                intent.putExtra("imgList", list);
//                setResult(1001, intent);
//            }
//        } else if (flag.equals("3")) {
//            Collection<String> c = mAdapter.contentImgMap.values();
//            Iterator<String> it = c.iterator();
//            for (; it.hasNext(); ) {
//                contentImgList.add(it.next());
//            }
//            Intent intent = new Intent(AlbumAct.this, SendGoodsAttrAct.class);
//            intent.putExtra("imgList", (Serializable) contentImgList);
//            setResult(1002, intent);
//        }
//        finish();
//    }
//
//    @Override
//    public void boxLeft() {
//        //this.finish();
//    }
//
//    @Override
//    public void boxRight() {
//
//    }

}
