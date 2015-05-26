package com.zengjin.base.newscenterimpl;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.zengjin.base.MenuDetailBasePager;
import com.zengjin.domain.NewsCenterBean.ChildRen;
import com.zengjin.domain.TabDetailBean;
import com.zengjin.domain.TabDetailBean.News;
import com.zengjin.domain.TabDetailBean.TopNew;
import com.zengjin.hmygz.NewsDetailUI;
import com.zengjin.hmygz.R;
import com.zengjin.hmygz.utils.CacheUtils;
import com.zengjin.hmygz.utils.Constants;
import com.zengjin.view.HorizontalScrollViewpager;
import com.zengjin.view.RefreshListView;
import com.zengjin.view.RefreshListView.OnRefreshListener;

public class NewsMenuTabDetailPager extends MenuDetailBasePager implements
		OnPageChangeListener, OnRefreshListener, OnItemClickListener {
	@ViewInject(R.id.hsvp_news_menu_tab_detail_top_news)
	private HorizontalScrollViewpager mViewPager;
	@ViewInject(R.id.tv_news_menu_tab_detail_description)
	private TextView tvDescription;
	@ViewInject(R.id.ll_news_menu_tab_detail_point_group)
	private LinearLayout llPointGroup;
	@ViewInject(R.id.rlv_news_menu_tab_detail_list_news)
	private RefreshListView mListView;// 列表新闻的数据
	private ChildRen mChildRen;// 当前页签详情页面数据
	private String url;
	private List<TopNew> topNewsList;// 顶部轮播数据
	// 图片访问框架
	private BitmapUtils bitmapUtils;
	/**
	 * 默认前一个选中点的索引
	 */
	private int previousEnablePosition;
	private InternalHandler mHandler;
	private List<News> newsList;
	private HttpUtils httpUtils;
	private TopNewsAdapter topNewsAdapter;// 顶部轮播图的适配器
	private NewsAdapter newsAdapter;// 列表新闻适配器
	private String moreUrl;// 更多数据的url
	//读过的新闻id
	private final String READ_NEWS_ID_ARRAY_KEY="read_news_id_array";

	public NewsMenuTabDetailPager(Context context, ChildRen childRen) {
		super(context);
		this.mChildRen = childRen;
		bitmapUtils = new BitmapUtils(mContext);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);
	}

	@Override
	public View initView() {
		View view = View.inflate(mContext, R.layout.news_menu_tab_detail, null);
		ViewUtils.inject(this, view);
		View topNewsView = View.inflate(mContext,
				R.layout.news_menu_tab_detail_topnews, null);
		ViewUtils.inject(this, topNewsView);
		// 要在init里面加入
		// mListView.addHeaderView(mViewPager);
		mListView.addCustomHeaderView(topNewsView);
		mListView.isEnabledPullDownRefresh(true);
		mListView.isEnabledLoadingMore(true);
		mListView.setOnRefreshListener(this);
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		url = Constants.SERVICE_URL + mChildRen.url;
		String json = CacheUtils.getString(mContext, url, null);
		if (!TextUtils.isEmpty(json)) {
			processData(json);
		}

		getDataFromNet();
	}

	private void getDataFromNet() {
		httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				System.out.println("新闻中心请求成功" + responseInfo.result);
				// 把数据存储起来
				CacheUtils.putString(mContext, url, responseInfo.result);
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println(mChildRen.title + "新闻中心请求失败" + msg);

			}
		});

	}

	/**
	 * 解析数据
	 */
	private TabDetailBean parserJSON(String json) {
		Gson gson = new Gson();
		TabDetailBean bean = gson.fromJson(json, TabDetailBean.class);
		moreUrl = bean.data.more;
		if (!TextUtils.isEmpty(moreUrl)) {
			moreUrl = Constants.SERVICE_URL + moreUrl;
		}

		return bean;
	}

	/**
	 * @param result
	 * 
	 *            处理数据
	 */
	private void processData(String result) {

		TabDetailBean bean = parserJSON(result);

		// 初始化顶部新闻的数据
		// 因为加入了刷新功能所以加上if else
		topNewsList = bean.data.topnews;
		if (topNewsAdapter == null) {
			topNewsAdapter = new TopNewsAdapter();
			mViewPager.setAdapter(topNewsAdapter);
			mViewPager.setOnPageChangeListener(this);
		} else {
			topNewsAdapter.notifyDataSetChanged();
		}

		// 初始化图片的描述和点

		llPointGroup.removeAllViews();// processData可能执行多次，所以每次初始化都要清空
		for (int i = 0; i < topNewsList.size(); i++) {
			View view = new View(mContext);
			view.setBackgroundResource(R.drawable.tab_detail_top_news_point);
			LayoutParams params = new LayoutParams(5, 5);
			if (i != 0) {
				params.leftMargin = 7;
			}
			view.setEnabled(false);
			view.setLayoutParams(params);
			llPointGroup.addView(view);
		}
		previousEnablePosition = 0;
		tvDescription.setText(topNewsList.get(previousEnablePosition).title);
		llPointGroup.getChildAt(previousEnablePosition).setEnabled(true);

		/**
		 * 1,使用handler执行一个延时任务：postdelayed 2.任务类runnable的run方法会被执行
		 * 3.使用handler发送一个消息 4.Handler类的handleMessage方法接受到消息
		 * 5.在handleMessage方法中，把viewpager的页面切换到下一个，同时使用1.
		 * 
		 * 草草草草草 ！！！！！！！换方法。。
		 * 
		 */
		// 由于这个方法执行了多次，所以要下面这样做（请求网络一次，缓存一次）
		if (mHandler == null) {
			mHandler = new InternalHandler();
		}
		mHandler.removeCallbacksAndMessages(null);// 保证只执行一次
		mHandler.postDelayed(new AutoSwitchPagerRunnable(), 2500);
		// 初始化列表新闻数据
		newsList = bean.data.news;

		if (newsAdapter == null) {
			newsAdapter = new NewsAdapter();
			mListView.setSelector(android.R.color.transparent);
			// mListView.setCacheColorHint(Color.TRANSPARENT);
			mListView.setAdapter(newsAdapter);
		} else {
			newsAdapter.notifyDataSetChanged();
		}

	}

	class NewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return newsList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NewsViewHolder mHolder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.news_menu_tab_detail_news_item, null);
				mHolder = new NewsViewHolder();
				mHolder.ivImage = (ImageView) convertView
						.findViewById(R.id.iv_news_menu_tab_detail_news_item_image);
				mHolder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_news_menu_tab_detail_news_item_title);
				mHolder.tvDate = (TextView) convertView
						.findViewById(R.id.tv_news_menu_tab_detail_news_item_date);
				convertView.setTag(mHolder);
			} else {
				mHolder = (NewsViewHolder) convertView.getTag();
			}
			// 给控件赋值
			News news = newsList.get(position);

			mHolder.tvTitle.setText(news.title);
			mHolder.tvDate.setText(news.pubdate);
			bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);
			bitmapUtils.display(mHolder.ivImage, news.listimage);
			//判断当前新闻是否读过
			String whetheread = CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
			if (!TextUtils.isEmpty(whetheread)&&whetheread.contains(news.id)) {
				mHolder.tvTitle.setTextColor(Color.GRAY);
			}else {
				mHolder.tvTitle.setTextColor(Color.BLACK);
			}
			
			
			
			return convertView;
		}

	}

	class NewsViewHolder {
		public ImageView ivImage;
		public TextView tvTitle;
		public TextView tvDate;

	}

	class TopNewsAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return topNewsList.size();
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
			ImageView iv = new ImageView(mContext);
			iv.setScaleType(ScaleType.FIT_XY);
			iv.setImageResource(R.drawable.home_scroll_default);
			iv.setOnTouchListener(new TopNewItemTouchListener());
			/**
			 * 第一个参数是把图片显示在哪一个控件上:iv.setimagebitmap 第二个参数是图片的url地址
			 */
			bitmapUtils.display(iv, topNewsList.get(position).topimage);
			container.addView(iv);
			return iv;
		}

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
		llPointGroup.getChildAt(previousEnablePosition).setEnabled(false);
		llPointGroup.getChildAt(position).setEnabled(true);
		tvDescription.setText(topNewsList.get(position).title);
		previousEnablePosition = position;

	}

	/**
	 * @author Administrator 内部的handler
	 */
	class InternalHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {

			int currentItem = mViewPager.getCurrentItem() + 1;
			mViewPager.setCurrentItem(currentItem % topNewsList.size());
			mHandler.postDelayed(new AutoSwitchPagerRunnable(), 2500);
		}

	}

	/**
	 * @author Administrator 自动切换任务类
	 */
	class AutoSwitchPagerRunnable implements Runnable {

		@Override
		public void run() {
			mHandler.obtainMessage().sendToTarget();

		}

	}

	class TopNewItemTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				System.out.println("停止播放");
				mHandler.removeCallbacksAndMessages(null);
				break;
			case MotionEvent.ACTION_UP:
				System.out.println("开始播放");
				mHandler.postDelayed(new AutoSwitchPagerRunnable(), 2500);

				break;
			default:
				break;
			}
			return true;
		}

	}

	@Override
	public void onPullDownRefresh() {
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				mListView.onRefreshFinish();
				System.out.println(mChildRen.title + "数据加载成功"
						+ responseInfo.result);
				CacheUtils.putString(mContext, url, responseInfo.result);
				processData(responseInfo.result);
				Toast.makeText(mContext, "下拉刷新完成", 0).show();
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mListView.onRefreshFinish();
				System.out.println(mChildRen.title + "数据加载失败" + msg);
				Toast.makeText(mContext, "下拉刷新失败", 0).show();
			}
		});

	}

	@Override
	public void onLoadingMore() {
		if (TextUtils.isEmpty(moreUrl)) {
			mListView.onRefreshFinish();
			Toast.makeText(mContext, "没有更多数据了", 0).show();

		} else {
			httpUtils.send(HttpMethod.GET, moreUrl,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							mListView.onRefreshFinish();
							// 把最新加载出来的数据，添加到newslist集合中，刷新listview
							TabDetailBean bean = parserJSON(responseInfo.result);
							newsList.addAll(bean.data.news);// news是一个集合
							newsAdapter.notifyDataSetChanged();

							Toast.makeText(mContext, "加载更多完成", 0).show();
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							mListView.onRefreshFinish();
							Toast.makeText(mContext, "加载更多失败", 0).show();
						}
					});
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		News news = newsList.get(position-1);
		//把当前点击的这条新闻id存起来1#2#3
		String readNewsIDArray=CacheUtils.getString(mContext, READ_NEWS_ID_ARRAY_KEY, null);
		String idArray=null;
		if (TextUtils.isEmpty(readNewsIDArray)) {
			idArray=news.id;
		}else {
			idArray=readNewsIDArray+"#"+news.id;
		}
		CacheUtils.putString(mContext, READ_NEWS_ID_ARRAY_KEY,idArray);
		//通知listview刷新
		newsAdapter.notifyDataSetChanged();
		//如果不是在activity里面开启另一个activity，要用context来启动

		Intent intent=new Intent(mContext, NewsDetailUI.class);
		intent.putExtra("url", news.url);
		mContext.startActivity(intent);
	}

}
