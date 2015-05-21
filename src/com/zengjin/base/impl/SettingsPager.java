package com.zengjin.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zengjin.base.TabBasePager;

public class SettingsPager extends TabBasePager {

	public SettingsPager(Context context) {
		super(context);

	
	}

	
	@Override
	public void initData() {
	
			tvTitle.setText("设置");
	ibMenu.setVisibility(View.GONE);
	TextView tv=new TextView(mContext);
	tv.setText("设置");
	tv.setTextSize(25);
	tv.setTextColor(Color.RED);
	tv.setGravity(Gravity.CENTER);
	flContent.addView(tv);
	}
}
