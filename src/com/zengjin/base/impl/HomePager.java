package com.zengjin.base.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zengjin.base.TabBasePager;

public class HomePager extends TabBasePager {

	public HomePager(Context context) {
		super(context);
	
	
	}

	
	@Override
	public void initData() {
	tvTitle.setText("智慧广州");
	ibMenu.setVisibility(View.GONE);
	TextView tv=new TextView(mContext);
	tv.setText("主页");
	tv.setTextSize(25);
	tv.setTextColor(Color.RED);
	tv.setGravity(Gravity.CENTER);
	flContent.addView(tv);
		
	}
}
