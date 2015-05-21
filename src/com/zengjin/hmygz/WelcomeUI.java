package com.zengjin.hmygz;

import com.zengjin.hmygz.utils.CacheUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class WelcomeUI extends Activity implements AnimationListener {
	public static final String IS_OPEN_MAIN_PAGE = "IS_OPEN_MAIN_PAGE";
	private RelativeLayout rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();

	}

	private void init() {
		rootView = (RelativeLayout) findViewById(R.id.rl_welcome_root);
		// 旋转动画
		RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation.setDuration(800);
		rotateAnimation.setFillAfter(true);// 设置动画执行完毕时，停留在完毕状态
		// 放大动画:0和1是倍数，后面的四个参数是指，以自己为参照物，中心为1/2x,1/2y
		ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(800);

		scaleAnimation.setFillAfter(true);
		// 从不显示到显示
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(1000);
		alphaAnimation.setFillAfter(true);
		// 把三个动画合在一起
		AnimationSet animationSet = new AnimationSet(false);
		animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(alphaAnimation);
		animationSet.addAnimation(scaleAnimation);

		animationSet.setAnimationListener(this);
		rootView.startAnimation(animationSet);

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
					boolean isopenmainpage = CacheUtils.getBoolean(
							getApplicationContext(), IS_OPEN_MAIN_PAGE, false);
					Intent intent = new Intent();
					if (isopenmainpage == true) {
						// 已打开过主界面，直接去主界面
						intent.setClass(WelcomeUI.this, MainUI.class);

					} else {
						intent.setClass(WelcomeUI.this, GuideUI.class);
						// 没打开过主界面，去引导页
					}
					startActivity(intent);
					// 关掉之后不会再回到欢迎页面
					finish();

				} catch (Exception e) {

					e.printStackTrace();
				}

			}
		}).start();

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}
}
