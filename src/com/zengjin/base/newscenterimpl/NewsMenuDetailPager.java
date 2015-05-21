package com.zengjin.base.newscenterimpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;
import com.zengjin.base.MenuDetailBasePager;
import com.zengjin.domain.NewsCenterBean.ChildRen;
import com.zengjin.domain.NewsCenterBean.NewsCenterData;
import com.zengjin.hmygz.MainUI;
import com.zengjin.hmygz.R;

/**
 * @author Administrator 新闻菜单对应的页面
 */
public class NewsMenuDetailPager extends MenuDetailBasePager implements
		OnPageChangeListener {

	@ViewInject(R.id.tpi_news_menu)
	private TabPageIndicator mIndicator;
	// @ViewInject(R.id.ib_news_menu_next_tab)
	// private ImageButton ibNextTab;
	@ViewInject(R.id.vp_news_menu_content)
	private ViewPager mViewPager;
	// 当前页面页签的数据
	private List<ChildRen> childrenList;
	//页签详情页面
	private List<NewsMenuTabDetailPager> tabPagerList;

	public NewsMenuDetailPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NewsMenuDetailPager(Context context, NewsCenterData newsCenterData) {
		super(context);
		childrenList = newsCenterData.children;

	}

	@Override
	public View initView() {

		View view = View.inflate(mContext, R.layout.news_menu, null);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		tabPagerList = new ArrayList<NewsMenuTabDetailPager>();
		for (int i = 0; i < childrenList.size(); i++) {
			tabPagerList.add(new NewsMenuTabDetailPager(mContext,childrenList.get(i)));
		
		}
		NewsMenuAdapter mAdapter = new NewsMenuAdapter();
		mViewPager.setAdapter(mAdapter);
		// 把viewpager对象设置给indicator
		// 关联完之后页签的数据都来自于newsmenuadapter.它自己会处理，把title传给indicator
		mIndicator.setViewPager(mViewPager);
		mIndicator.setOnPageChangeListener(this);

	}

	class NewsMenuAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return childrenList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//			TextView tv = new TextView(mContext);
//			tv.setText(childrenList.get(position).title + "的页面");
//			tv.setTextSize(25);
//			tv.setTextColor(Color.RED);
//			container.addView(tv);
			NewsMenuTabDetailPager pager = tabPagerList.get(position);
		View rootView=pager.getRootView();
		container.addView(rootView);
		//初始化数据
		pager.initData();
			return rootView;
		}

		/**
		 * 返回的字符串作为tabpageindicator的页签数据来展示
		 */
		@Override
		public CharSequence getPageTitle(int position) {

			return childrenList.get(position).title;
		}

	}

	@OnClick(R.id.ib_news_menu_next_tab)
	public void nextTab(View v) {
		mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// // 如果position=0，菜单可用
		// if (position == 0) {
		// // 设置整个屏幕可以滑动出来
		// isEnableSlidingMenu(true);
		// } else {
		// // 不可以滑动
		// isEnableSlidingMenu(false);
		// }
		// 可以将上面的代码优化成这样！:
		isEnableSlidingMenu(position == 0);
	}

	private void isEnableSlidingMenu(boolean isEnable) {
		if (isEnable) {
			((MainUI) mContext).getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_FULLSCREEN);

		} else {
			((MainUI) mContext).getSlidingMenu().setTouchModeAbove(
					SlidingMenu.TOUCHMODE_NONE);

		}
	}

}
