package com.xinspace.csevent.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xinspace.csevent.R;
import com.yljt.cascadingmenu.CascadingMenuFragment;
import com.yljt.cascadingmenu.DBhelper;
import com.yljt.cascadingmenu.interfaces.CascadingMenuViewOnSelectListener;
import com.yljt.entity.Area;

import java.util.ArrayList;

/**
 * 选择城市页面
 */
public class SelectCitiesActivity extends FragmentActivity {

    private TextView tvCurrentPosition;
    private TextView tvLockArea;
    private LinearLayout ll_back;

    ArrayList<Area> provinceList;
    // 两级联动菜单数据
    private CascadingMenuFragment cascadingMenuFragment=null;
    private DBhelper dBhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_cities);
        //向三级menu添加地区数据
        dBhelper = new DBhelper(this);
        provinceList = dBhelper.getProvince();

        setViews();
        setListeners();
        
        getData();
    }
    //获取上个页面传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        String address=intent.getStringExtra("data");
        String district=intent.getStringExtra("district");
        tvCurrentPosition.setText(address);
        tvLockArea.setText(district);
    }
    private void setListeners() {
        //点击返回
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setViews() {
        tvCurrentPosition = (TextView) findViewById(R.id.tv_select_guess_position);
        tvLockArea = (TextView) findViewById(R.id.tv_select_lock_area);
        ll_back = (LinearLayout) findViewById(R.id.ll_select_city_back);
        showFragmentMenu();
    }

    //显示省市区Fragment
    public void showFragmentMenu() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.short_menu_pop_in,
                R.anim.short_menu_pop_out);

        if (cascadingMenuFragment == null) {
            cascadingMenuFragment = CascadingMenuFragment.getInstance();
            cascadingMenuFragment.setMenuItems(provinceList);
            cascadingMenuFragment
                    .setMenuViewOnSelectListener(new NMCascadingMenuViewOnSelectListener());
            fragmentTransaction.replace(R.id.ll_select_fragment, cascadingMenuFragment);
        } else {
            fragmentTransaction.remove(cascadingMenuFragment);
            cascadingMenuFragment = null;
        }
        fragmentTransaction.commit();
    }

    // 级联菜单选择回调接口
    class NMCascadingMenuViewOnSelectListener implements
            CascadingMenuViewOnSelectListener {
        @Override
        public void getValue(Area area) {
            cascadingMenuFragment = null;
            Toast.makeText(getApplicationContext(), "" + area.getName(),
                    Toast.LENGTH_SHORT).show();
            //回传城市到上一个页面
            Intent intent=new Intent();
            intent.putExtra("city",area.getName());
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
