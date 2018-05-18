package com.xinspace.csevent.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.xinspace.csevent.R;
import com.xinspace.csevent.monitor.activity.OpenDoorRecordAct;

/**
 * Created by Android on 2017/8/23.
 */

public class AppOpenDoorProvider extends AppWidgetProvider{

    public static final String ACTION_OPEN_RECORDE = "com.xinspace.csevent.action.CLICK_OPEN_RECORDE"; // 开门记录
    public static final String ACTION_OPEN_PASSWORD = "com.xinspace.csevent.action.CLICK_OPEN_PASSWORD"; // 开门密码
    public static final String ACTION_OPEN_LOCK = "com.xinspace.csevent.action.CLICK_OPEN_LOCK";     // 门禁开锁
    public static final String ACTION_OPEN_MONITOR = "com.xinspace.csevent.action.CLICK_OPEN_MONITOR"; //门禁监控
    private String flag;

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_weight_layout);

        //1.开门记录
        Intent openRecord = new Intent(ACTION_OPEN_RECORDE);
        PendingIntent piOpenRecord = PendingIntent.getBroadcast(context, 100, openRecord, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_widget_open_record, piOpenRecord);

        //2.密码开门
        Intent openPassword = new Intent(ACTION_OPEN_PASSWORD);
        PendingIntent piOpenByPassword = PendingIntent.getBroadcast(context, 100, openPassword, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_widget_password, piOpenByPassword);

        //3.门禁开锁
        Intent openLock = new Intent(ACTION_OPEN_LOCK);
        PendingIntent piOpenLock = PendingIntent.getBroadcast(context, 100, openLock, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_widget_open_lock, piOpenLock);

        //4.门禁监控
        Intent openMonitor = new Intent(ACTION_OPEN_MONITOR);
        PendingIntent piMachineCtrl = PendingIntent.getBroadcast(context, 100, openMonitor, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.app_widget_machine_ctrl, piMachineCtrl);

        for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_OPEN_RECORDE.equals(intent.getAction())) {
            Intent openRecord = new Intent(context, OpenDoorRecordAct.class);
            openRecord.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(openRecord);
        }

        if (ACTION_OPEN_PASSWORD.equals(intent.getAction())) {
            flag = "3";
            Intent openDoor3 = new Intent(context, OpenDoorService.class);
            openDoor3.putExtra("theflag", flag);
            context.startService(openDoor3);
        }

        if (ACTION_OPEN_LOCK.equals(intent.getAction())) {
            flag = "1";
            Intent openDoor1 = new Intent(context, OpenDoorService.class);
            openDoor1.putExtra("theflag", flag);
            context.startService(openDoor1);
        }

        if (ACTION_OPEN_MONITOR.equals(intent.getAction())) {
            flag = "2";
            Intent openDoor2 = new Intent(context, OpenDoorService.class);
            openDoor2.putExtra("theflag", flag);
            context.startService(openDoor2);
        }

    }

    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

}
