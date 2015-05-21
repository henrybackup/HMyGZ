package com.zengjin.hmygz;

import java.util.ArrayList;
import java.util.List;

import com.zengjin.hmygz.utils.CacheUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideUI extends Activity implements OnClickListener {
	private ViewPager mViewPager;
	private Button btStartExperience;
	private int lastPointPosition;
	private LinearLayout llPointGroup;
	private List<ImageView> imageList;
	private View mySelectPointView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.guide);
		initView();
		GuideAdaper guideAdaper = new GuideAdaper();
		mViewPager.setAdapter(guideAdaper);
	btStartExperience.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
	//当页面被选中时		
			@Override
			public void onPageSelected(int position) {
			llPointGroup.getChildAt(position).setEnabled(true);
			llPointGroup.getChildAt(lastPointPosition).setEnabled(false);
			lastPointPosition=position;
			if (imageList.size()-1==position) {
				btStartExperience.setVisibility(View.VISIBLE);
			}else {
				btStartExperience.setVisibility(View.GONE);
			}
				
			}
//当页面滚动状态改变时
			@Override
			public void onPageScrollStateChanged(int arg0) {
		
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
	
			}
			

		});

	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		btStartExperience = (Button) findViewById(R.id.btn_guide_start_experience);
		llPointGroup = (LinearLayout) findViewById(R.id.ll_guide_point_group);
		btStartExperience=(Button) findViewById(R.id.btn_guide_start_experience);
	
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		int[] imageResIds = { R.drawable.guide_1, R.drawable.guide_2,
				R.drawable.guide_3 };
		imageList = new ArrayList<ImageView>();

		ImageView imageView;
		ImageView point;
	
		LayoutParams layoutParams;
		for (int i = 0; i < imageResIds.length; i++) {
			imageView = new ImageView(this);
			imageView.setBackgroundResource(imageResIds[i]);
			imageList.add(imageView);
			point = new ImageView(this);
			point.setBackgroundResource(R.drawable.point_bg);
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			layoutParams = new LayoutParams(16,16);
			if (i!=0) {
				layoutParams.leftMargin=13; 
			}
			point.setLayoutParams(layoutParams);
			llPointGroup.addView(point);
		}
	}

	class GuideAdaper extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			// 这样子也可以container.removeView(imageList.get(position));
			container.removeView((View) object);

			// 这行代码不能留 super.destroyItem(container, position, object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView iv = imageList.get(position);
			// 向viewpager中添加一个对象
			container.addView(iv);
			// 返回当前添加的view对象
			return iv;
		}

	}

	@Override
	public void onClick(View v) {
		CacheUtils.putBoolean(this, WelcomeUI.IS_OPEN_MAIN_PAGE, true);
		startActivity(new Intent(GuideUI.this, MainUI.class));
		finish();
		
	}
}
