package com.zengjin.base.newscenterimpl;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.zengjin.base.MenuDetailBasePager;

/**
 * @author Administrator
 *新闻菜单对应的页面
 */
public class TopicMenuDetailPager extends MenuDetailBasePager {

	public TopicMenuDetailPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
	TextView tv=new TextView(mContext);
	tv.setText("专题菜单页面");


	tv.setTextSize(25);
	tv.setTextColor(Color.RED);
	tv.setGravity(Gravity.CENTER); 

		return tv;
	}

}
