package com.zengjin.hmygz;
//存放在有用的东西

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.zengjin.fragment.LeftMenuFragment;
import com.zengjin.fragment.MainContentFragment;
import com.zengjin.hmygz.utils.CacheUtils;

public class MainUI extends SlidingFragmentActivity {
	
/**
 * 左侧菜单fragment的tag
 */
private final String LEFT_MENU_FRAGMENT_TAG="left_menu";


/**
 * 主菜单fragment的tag
 */
private final String MAIN_MENU_FRAGMENT_TAG="main_menu";
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_content);
		setBehindContentView(R.layout.left_content);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		int mywidth = displayMetrics.widthPixels;
//较简便的写法：
//		Display display = getWindowManager().getDefaultDisplay();
//		int displaywidth=display.getWidth();
		
		slidingMenu.setBehindOffset(mywidth * 5/8);

		CacheUtils.putBoolean(this, WelcomeUI.IS_OPEN_MAIN_PAGE, true);
		initFragment();
	}

	/**
	 * 初始化菜单和主界面fragment
	 */
	private void initFragment() {
	//获取fagment管理对象
		FragmentManager fm = getSupportFragmentManager();
		//开启事务
		FragmentTransaction ft = fm.beginTransaction();
		//替换布局
		
		//替换左侧菜单布局
	ft.replace(R.id.fl_left_content, new LeftMenuFragment(), LEFT_MENU_FRAGMENT_TAG);
	//替换主菜单布局
	ft.replace(R.id.fl_main_content, new MainContentFragment(), MAIN_MENU_FRAGMENT_TAG);

		//提交
		ft.commit();
	}
	/**
	 * @return
	 * 获取左侧菜单的fragment对象
	 */
	public LeftMenuFragment getLeftMenuFragment(){
		FragmentManager fm = getSupportFragmentManager();
		LeftMenuFragment leftMenuFragment = (LeftMenuFragment) fm.findFragmentByTag(LEFT_MENU_FRAGMENT_TAG);
		return leftMenuFragment;
	}
	
	/**
	 * @return
	 * 获取主页面的fragment对象
	 */
	public MainContentFragment getMainContentFragment(){
		FragmentManager fm = getSupportFragmentManager();
		MainContentFragment mainContentFragment = (MainContentFragment) fm.findFragmentByTag(MAIN_MENU_FRAGMENT_TAG);
		return mainContentFragment;
	}

}
