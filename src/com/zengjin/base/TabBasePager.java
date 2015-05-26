package com.zengjin.base;

import java.util.ArrayList;
import java.util.List;

import com.zengjin.base.impl.GovAffairsPager;
import com.zengjin.base.impl.HomePager;
import com.zengjin.base.impl.NewsCenterPager;
import com.zengjin.base.impl.SettingsPager;
import com.zengjin.base.impl.SmartServicePager;
import com.zengjin.hmygz.R;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
//注意了！要学会抽象
public class TabBasePager {
	//要想给子类用就要改成public
	public Context mContext;
	public TextView tvTitle;
	public ImageButton ibMenu;
	public FrameLayout flContent;
	private View rootView;
	public ImageButton ibListOrGrid;


	public TabBasePager(Context context) {
		this.mContext = context;
		rootView = initView();
	}
 
	private View initView() {
		
View view = View.inflate(mContext, R.layout.tab_base_pager, null);
tvTitle = (TextView) view.findViewById(R.id.tv_title_bar_title);
ibMenu = (ImageButton) view.findViewById(R.id.ib_title_bar_menu);
ibListOrGrid = (ImageButton) view.findViewById(R.id.ib_title_bar_list_or_gird);

flContent = (FrameLayout) view.findViewById(R.id.fl_tab_pager_content);
		return view;
	}
	
	/**
	 * @return
	 * 获得当前页面布局对象
	 */
	public View getRootView(){
		return rootView;
		
	}
	
	public void initData(){
		
		
		
	}

}
