package com.xinspace.csevent.sweepstake.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinspace.csevent.R;

/**
 * Created by Android on 2016/12/6.
 *
 * 智慧社区
 *
 */
public class CommunityFragment extends Fragment{

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_security , null);

        return view;
    }
}
