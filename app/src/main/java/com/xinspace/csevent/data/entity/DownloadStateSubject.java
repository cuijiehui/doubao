package com.xinspace.csevent.data.entity;

import com.xinspace.csevent.myinterface.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 * 主题类
 */
public class DownloadStateSubject {

    private List<Observer> observers = new ArrayList<>();
    private int state;//下载状态
    private String percentage;//下载进度
    private int position;//相应的位置
    private String apkPath;//apk路径

    public void setData(int state,String percentage,int position,String apkPath) {
        this.state=state;
        this.percentage=percentage;
        this.position=position;
        this.apkPath=apkPath;
        Notify();
    }

    /// <summary>
    /// 增加观察者
    /// </summary>
    /// <param name="observer"></param>
    public void Attach(Observer observer) {
        observers.add(observer);
    }

    /// <summary>
    /// 移除观察者
    /// </summary>
    /// <param name="observer"></param>
    public void Detach(Observer observer) {
        observers.remove(observer);
    }

    /// <summary>
    /// 向观察者（们）发出通知
    /// </summary>
    public void Notify() {
        for (Observer observer:observers) {
            observer.updateDownloadState(state,percentage,position,apkPath);
        }
    }
}
