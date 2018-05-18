package com.xinspace.csevent.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 加载图片的工具类
 */
public class ImagerLoaderUtil {

    //显示图片,做了缓存
    public static void displayImage(String url,ImageView iv){

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                               //启用内存缓存
                .cacheOnDisk(true)                                 //启用外存缓存
                .considerExifParams(true)                          //启用EXIF和JPEG图像格式
                .build();

        ImageLoader.getInstance().displayImage(url,iv,options);
    }

    //显示圆角图片,做了缓存
    public static void displayRoundedImage(String url,ImageView iv){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)                               //启用内存缓存
                .cacheOnDisk(true)                                 //启用外存缓存
                .considerExifParams(true)//启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(15))//设置圆角以及角度
                .build();

        ImageLoader.getInstance().displayImage(url,iv,options);
    }

    /**
     * 加载过程成显示未加载完成的图片
     * @param url 图片地址
     * @param iv 组件
     * @param resId 设置加载过程中显示的图片
     */
    public static void displayImageWithLoadingIcon(String url,ImageView iv,int resId){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId) // 设置图片下载期间显示的图片
                .bitmapConfig(Bitmap.Config.RGB_565)
                .resetViewBeforeLoading(false)  // default 设置图片在加载前是否重置、复位
                .cacheInMemory(true) // default  设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // default  设置下载的图片是否缓存在SD卡中
                .build();

        ImageLoader.getInstance().displayImage(url,iv,options);
    }

    public static void clearImageMemory(){
        ImageLoader.getInstance().clearMemoryCache();
    }





}
