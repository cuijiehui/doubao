package com.xinspace.csevent.myinterface;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Android on 2017/9/29.
 */

public abstract class BaseFragment extends Fragment{

    /**
     * 所有继承BaseFragment的子类都将在这个方法中实现物理Back键按下后的逻辑
     * FragmentActivity捕捉到物理返回键点击事件后会首先询问Fragment是否消费该事件
     * 如果没有Fragment消息时FragmentActivity自己才会消费该事件
     */
    public abstract boolean onBackPressed();

    protected BackHandleInterface mBackHandleInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof BackHandleInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            this.mBackHandleInterface = (BackHandleInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBackHandleInterface.setSelectedFragment(this);
    }
}
