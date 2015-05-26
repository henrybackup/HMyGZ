package com.zengjin.base.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zengjin.base.MenuDetailBasePager;
import com.zengjin.base.TabBasePager;
import com.zengjin.base.newscenterimpl.InteractMenuDetailPager;
import com.zengjin.base.newscenterimpl.NewsMenuDetailPager;
import com.zengjin.base.newscenterimpl.PhotosMenuDetailPager;
import com.zengjin.base.newscenterimpl.TopicMenuDetailPager;
import com.zengjin.domain.NewsCenterBean;
import com.zengjin.domain.NewsCenterBean.NewsCenterData;
import com.zengjin.fragment.LeftMenuFragment;
import com.zengjin.hmygz.MainUI;
import com.zengjin.hmygz.utils.CacheUtils;
import com.zengjin.hmygz.utils.Constants;

public class NewsCenterPager extends TabBasePager {

	/**
	 * 左侧菜单对应页面
	 */
	private List<MenuDetailBasePager> pagerList;

	/**
	 * 左侧菜单数据
	 */
	private List<NewsCenterData> leftMenuDataList;

	public NewsCenterPager(Context context) {
		super(context);

	}

	@Override
	public void initData() {
		tvTitle.setText("新闻");
		ibMenu.setVisibility(View.VISIBLE);
//把本地缓存的数据取出来
		String json=CacheUtils.getString(mContext, Constants.NEWSCENTER_URL, null);
		if (!TextUtils.isEmpty(json)) {
			processData(json);//先把旧的新闻显示出来
		}
		
		
		// 去服务器抓取数据
		getDataFromNet();

		// TextView tv = new TextView(mContext);
		// tv.setText("新闻中心");
		// tv.setTextSize(25);
		// tv.setTextColor(Color.RED);
		// tv.setGravity(Gravity.CENTER);
		// flContent.addView(tv);
	}

	/**
	 * 抓取数据
	 */
	private void getDataFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constants.NEWSCENTER_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// TODO Auto-generated method stub
						System.out.println("新闻中心请求成功" + responseInfo.result);
						
						//把数据存储起来
						CacheUtils.putString(mContext, Constants.NEWSCENTER_URL, responseInfo.result);
						
						processData(responseInfo.result);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						System.out.println("新闻中心请求失败" + msg);
					}
				});
	}

	protected void processData(String result) {
		Gson gson = new Gson();
		NewsCenterBean bean = gson.fromJson(result, NewsCenterBean.class);
		
		
	//	System.out.println(bean.data.get(0).children.get(0).title);
			pagerList = new ArrayList<MenuDetailBasePager>();
		pagerList.add(new NewsMenuDetailPager(mContext,bean.data.get(0)));
		pagerList.add(new TopicMenuDetailPager(mContext));
		pagerList.add(new PhotosMenuDetailPager(mContext,bean.data.get(2)));
		pagerList.add(new InteractMenuDetailPager(mContext));
		
		leftMenuDataList = bean.data;
		LeftMenuFragment leftMenuFragment = ((MainUI) mContext)
				.getLeftMenuFragment();
		leftMenuFragment.setMenuListData(leftMenuDataList);
	
	}

	/**
	 * @param position
	 *            根据位置切换当前菜单对应的页面
	 */

	public void switchCurrentPager(int position) {
		 final MenuDetailBasePager pager = pagerList.get(position);
		// 把pager中的view对象添加到framlayout中
		View view = pager.getRootView();

		flContent.removeAllViews();
		flContent.addView(view);

		// 标题要跟着去改变
		tvTitle.setText(leftMenuDataList.get(position).title);
		// 初始化当天pager数据
		pager.initData();
		if (position==2) {
			 ibListOrGrid.setVisibility(View.VISIBLE);
			 ibListOrGrid.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				PhotosMenuDetailPager photosPager=(PhotosMenuDetailPager)pager;
					photosPager.switchCurrentPager(ibListOrGrid);
			
				}
			});
		}else {
			 ibListOrGrid.setVisibility(View.GONE);
		}
		
	}
}
