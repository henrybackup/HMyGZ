package com.zengjin.base;

import android.content.Context;
import android.view.View;

/**
 * @author Administrator
 *菜单对应页面的基类
 */
public abstract class MenuDetailBasePager {
public Context mContext;
private View rootView;	
	public MenuDetailBasePager(Context context) {
	this.mContext=context;
	rootView = initView();
	
	
	}
public abstract View initView();
	
	public View getRootView(){
		
		return rootView;
	}
/**
 * 子类需要覆盖该方法，实现自己数据的初始化
 */
public void initData(){
	
	
}
}
