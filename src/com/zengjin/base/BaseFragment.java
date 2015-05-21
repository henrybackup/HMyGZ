package com.zengjin.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Administrator 所有fragment的基类
 */
public abstract class BaseFragment extends Fragment {
	
	
	/**
	 * 把fragment绑定到哪个activity上，就是上下文对象，就是哪个activity
	 */
	public Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	}
@Override
public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	initData();
}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = initView(inflater);

		return view;
	}
	
	/**
	 * 如果子类需要初始化数据，则覆盖此方法
	 */
	public void initData() {
	// TODO Auto-generated method stub
	
}
	/**
	 * @param inflater
	 * @return
	 * 子类必须实现此方法,作为当前fragment的布局来显示
	 */
	public abstract View initView(LayoutInflater inflater);

}
