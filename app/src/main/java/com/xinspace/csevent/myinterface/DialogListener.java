package com.xinspace.csevent.myinterface;


/**
 * dialog获取数据成功后回传数据给activity的接口
 */
public interface DialogListener {
    /**
     * 回调方法
     * @param result 数据
     */
    void onDialogFinish(String result) ;
}
