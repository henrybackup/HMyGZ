package com.zengjin.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HorizontalScrollViewpager extends ViewPager {

	private int downX;
	private int downY;

	public HorizontalScrollViewpager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			downX = (int) ev.getX();
			downY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getX();
			int moveY = (int) ev.getY();

			int diffX = downX - moveX;
			int diffY = downY - moveY;
			if (Math.abs(diffX) > Math.abs(diffY)) {// 当前是横向活动，父元素不需要拦截
			// 当前页面等于第一个页面,并且从左向右滑动，可以拦截
				if (getCurrentItem() == 0 && diffX < 0) {
					getParent().requestDisallowInterceptTouchEvent(false);

				} else if ((getCurrentItem()==getAdapter().getCount()-1)&&diffX>0) {
					// 当前页面最后一个个页面,并且从右往左滑动，可以拦截
					getParent().requestDisallowInterceptTouchEvent(false);

				}else {
					getParent().requestDisallowInterceptTouchEvent(true);

				}

			} else {// 当前是横向活动，父元素需要拦截
				getParent().requestDisallowInterceptTouchEvent(false);

			}
			break;
		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}
}
