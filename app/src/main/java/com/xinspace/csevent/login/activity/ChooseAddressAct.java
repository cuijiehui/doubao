package com.xinspace.csevent.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.xinspace.csevent.R;
import com.xinspace.csevent.util.StatusBarUtils;
import com.xinspace.csevent.ui.widget.widget.OnWheelChangedListener;
import com.xinspace.csevent.ui.widget.widget.WheelView;
import com.xinspace.csevent.ui.widget.widget.adapters.ArrayWheelAdapter;

public class ChooseAddressAct extends AddressBaseActivity implements OnClickListener, OnWheelChangedListener {
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private Button mBtnConfirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatusBarUtils.setWindowStatusBarColor(this , R.color.app_bottom_color);
		setContentView(R.layout.activity_main2);
		setUpViews();
		setUpListener();
		setUpData();
	}

	private void setUpViews() {
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
	}

	private void setUpListener() {
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加onclick事件
		mBtnConfirm.setOnClickListener(this);
	}

	private void setUpData() {
		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(ChooseAddressAct.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(8);
		mViewCity.setVisibleItems(8);
		mViewDistrict.setVisibleItems(8);
		updateCities();
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_confirm:
				showSelectedResult();
				break;
			default:
				break;
		}
	}

	private void showSelectedResult() {
//		Toast.makeText(ChangeAddressAct.this, "当前选中:"+mCurrentProviceName+","+mCurrentCityName+","
//				+mCurrentDistrictName+","+mCurrentZipCode, Toast.LENGTH_SHORT).show();

		String provice = mCurrentProviceName.toString();
		String city = mCurrentCityName.toString();
		String district = mCurrentDistrictName.toString();

		Intent intent = this.getIntent();
		Bundle bundle = new Bundle();
		bundle.putString("provice", provice);
		bundle.putString("city", city);
		bundle.putString("District", district);
		intent.putExtras(bundle);
		this.setResult(1000, intent);
		this.finish();
	}
}
