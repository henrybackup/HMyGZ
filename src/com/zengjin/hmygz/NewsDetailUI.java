package com.zengjin.hmygz;

import com.zengjin.hmygz.utils.ShareUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class NewsDetailUI extends Activity implements OnClickListener {

	private String url;
	private ProgressBar mProgressBar;
	private WebView mWebView;
	private int currentSelectTextSizePosition = 2;// 正常字体大小
	private int tempSelectTextSizePosition;// 临时选择字体大小
	private WebSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_detail);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");

		initView();
	}

	private void initView() {
		findViewById(R.id.ib_title_bar_menu).setVisibility(View.GONE);
		findViewById(R.id.tv_title_bar_title).setVisibility(View.GONE);
		findViewById(R.id.ib_title_bar_back).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_share).setVisibility(View.VISIBLE);
		findViewById(R.id.ib_title_bar_textsize).setVisibility(View.VISIBLE);

		findViewById(R.id.ib_title_bar_back).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_share).setOnClickListener(this);
		findViewById(R.id.ib_title_bar_textsize).setOnClickListener(this);

		mWebView = (WebView) findViewById(R.id.wv_news_detail);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_news_detail);
		settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);// 设置WebView属性，能够执行Javascript脚本
		// 界面上放大缩小控制器
		settings.setBuiltInZoomControls(true);
		// 双击放大缩小
		settings.setUseWideViewPort(true);
		// settings.setTextSize(t)
		mWebView.setWebViewClient(new WebViewClient() {
			// 当前页面加载完成，需要将进度条隐藏
			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

		});
		// 要先设置监听再loadurl
		mWebView.loadUrl(url);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_title_bar_back:
			finish();

			break;
		case R.id.ib_title_bar_textsize:
			showTextSizeDialog();
			break;
		case R.id.ib_title_bar_share:
			ShareUtils utils = new ShareUtils();
			utils.showShare(this, "哈哈" + url,
					"/storage/sdcard/Pictures/myPicture.png");
			break;
		default:
			break;
		}

	}

	private void showTextSizeDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("选择字体大小");
		String[] items = { "超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体" };
		tempSelectTextSizePosition = currentSelectTextSizePosition;
		builder.setSingleChoiceItems(items, currentSelectTextSizePosition,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						tempSelectTextSizePosition = which;

					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				currentSelectTextSizePosition = tempSelectTextSizePosition;
				changeWebViewTextSize();

			}

		});
		// 不处理事件默认关闭
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 根据currenttextsize改变字体大小
	 */
	private void changeWebViewTextSize() {

		switch (currentSelectTextSizePosition) {
		case 0:
			settings.setTextSize(TextSize.LARGEST);
			break;
		case 1:
			settings.setTextSize(TextSize.LARGER);
			break;
		case 2:
			settings.setTextSize(TextSize.NORMAL);
			break;
		case 3:
			settings.setTextSize(TextSize.SMALLER);
			break;
		case 4:
			settings.setTextSize(TextSize.SMALLEST);
			break;

		default:
			break;
		}
	}
}
