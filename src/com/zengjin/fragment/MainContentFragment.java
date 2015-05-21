package com.zengjin.fragment;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zengjin.base.BaseFragment;
import com.zengjin.base.TabBasePager;
import com.zengjin.base.impl.GovAffairsPager;
import com.zengjin.base.impl.HomePager;
import com.zengjin.base.impl.NewsCenterPager;
import com.zengjin.base.impl.SettingsPager;
import com.zengjin.base.impl.SmartServicePager;
import com.zengjin.hmygz.MainUI;
import com.zengjin.hmygz.R;
import com.zengjin.view.NoScrollViewPager;

/**
 * @author Administrator 主界面fragment
 */
public class MainContentFragment extends BaseFragment implements
		OnCheckedChangeListener {
	private List<TabBasePager> pagerList;
	@ViewInject(R.id.vp_content_fragment)
	private NoScrollViewPager mViewPager;
	@ViewInject(R.id.rg_content_fragment)
	private RadioGroup mRadioGroup;

	@Override
	public View initView(LayoutInflater inflater) {
		// inflater可用可不用，只要返回一个view就ok
		View view = inflater.inflate(R.layout.content_fragment, null);
		// 把当前view对象注入到xutils注解中
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void initData() {
		pagerList = new ArrayList<TabBasePager>();
		pagerList.add(new HomePager(mActivity));
		pagerList.add(new NewsCenterPager(mActivity));
		pagerList.add(new SmartServicePager(mActivity));
		pagerList.add(new GovAffairsPager(mActivity));
		pagerList.add(new SettingsPager(mActivity));
		ContentAdapter adapter = new ContentAdapter();
		mViewPager.setAdapter(adapter);
		mRadioGroup.check(R.id.rb_content_fragment_home);
		pagerList.get(0).initData();// 初始化首页的数据 
		((MainUI)mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		mRadioGroup.setOnCheckedChangeListener(this);

	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pagerList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// 注意,container的addview,和return view是分开的两回事
			TabBasePager tabBasePager = pagerList.get(position);
			View view = tabBasePager.getRootView();
			container.addView(view);
			// tabBasePager.initData();
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			// 以后就这样写!
			container.removeView((View) object);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		int index = -1;
		switch (checkedId) {
		case R.id.rb_content_fragment_home:
			index = 0;
			break;
		case R.id.rb_content_fragment_newscenter:
			index = 1;
			break;

		case R.id.rb_content_fragment_smartservice:
			index = 2;
			break;

		case R.id.rb_content_fragment_govaffairs:
			index = 3;
			break;
		case R.id.rb_content_fragment_settings:
			index = 4;
			break;

		default:
			break;
		}
		mViewPager.setCurrentItem(index);

		pagerList.get(index).initData();// 初始化数据
		if (index==0||index==4) {
			//菜单不可用
			((MainUI)mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}else {
			//菜单可用
			((MainUI)mActivity).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

		}
	}

	/**
	 * @return
	 * 获取新闻中心页面
	 */
	public NewsCenterPager getNewscCenterPager(){
		return (NewsCenterPager) pagerList.get(1);
		
	}
}
