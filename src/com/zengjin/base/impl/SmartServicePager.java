package com.zengjin.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zengjin.base.TabBasePager;

public class SmartServicePager extends TabBasePager {

	public SmartServicePager(Context context) {
		super(context);

	}

	@Override
	public void initData() {

		tvTitle.setText("生活");
		ibMenu.setVisibility(View.VISIBLE);
		TextView tv = new TextView(mContext);
		tv.setText("智慧服务");
		tv.setTextSize(25);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);
		flContent.addView(tv);
	}
}
