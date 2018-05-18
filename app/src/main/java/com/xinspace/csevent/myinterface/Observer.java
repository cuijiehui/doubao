package com.xinspace.csevent.myinterface;

/**
 * 观察者接口
 */
public interface Observer {
    /**
     * 下载观察者的回调
     * @param state 下载状态
     * @param percentage 下载进度
     * @param position 相应的位置
     * @param apkPath 下载完成后的apk地址
     */
    void updateDownloadState(int state, String percentage, int position, String apkPath);
}
