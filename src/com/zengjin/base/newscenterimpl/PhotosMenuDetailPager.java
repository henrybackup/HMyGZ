package com.zengjin.base.newscenterimpl;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.zengjin.base.MenuDetailBasePager;
import com.zengjin.domain.NewsCenterBean.NewsCenterData;
import com.zengjin.domain.PhotosBean;
import com.zengjin.domain.PhotosBean.PhotoItem;
import com.zengjin.hmygz.R;
import com.zengjin.hmygz.utils.CacheUtils;
import com.zengjin.hmygz.utils.Constants;
import com.zengjin.hmygz.utils.ImageUtils;
import com.zengjin.hmygz.utils.NetCache;

/**
 * @author Administrator 新闻菜单对应的页面
 */
public class PhotosMenuDetailPager extends MenuDetailBasePager {

	private GridView mGridView;
	private ListView mListView;
	private List<PhotoItem> photoList;
	private boolean isDisplayList=true;//当前是否显示的是列表页面，默认为listview
	private ImageUtils imageUtils;//图片三级缓存工具类
	//因为第一次加载要用到网络，所以要用handler在ui上显示图片
private Handler handler=new Handler(){

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		switch (msg.what) {
		case NetCache.SUCCESS:
			Bitmap bm=(Bitmap) msg.obj;
			int tag=msg.arg1;//当前抓取图片的tag
			ImageView iv = (ImageView) mListView.findViewWithTag(tag);
			iv.setImageBitmap(bm);
			break;
	case NetCache.FAILED:
			Toast.makeText(mContext, "请求图片失败", 0);
			break;
		default:
			break;
		}
	
	}
	
	
};
	public PhotosMenuDetailPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PhotosMenuDetailPager(Context context, NewsCenterData newsCenterData) {
		super(context);

	}

	@Override
	public View initView() {
		
		View view = View.inflate(mContext, R.layout.photos, null);
		mListView = (ListView) view.findViewById(R.id.lv_photos);
		mGridView = (GridView) view.findViewById(R.id.gv_photos);
		return view;
	}


	@Override
	public void initData() {
		
		imageUtils = new ImageUtils(handler);
		
		
String json = CacheUtils.getString(mContext, Constants.PHOTOS_URL, null);
if (!TextUtils.isEmpty(json)) {
	processData(json);
	
}else {
	
		HttpUtils httpUtils=new HttpUtils();
		httpUtils.send(HttpMethod.GET, Constants.PHOTOS_URL, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
			System.out.println("组图请求成功"+responseInfo.result);
				CacheUtils.putString(mContext, Constants.PHOTOS_URL, responseInfo.result);
				processData(responseInfo.result);
			}


			@Override
			public void onFailure(HttpException error, String msg) {
				System.out.println("组图请求failed"+msg);
				
			}
		});
}
	}
	/**
	 * 解析并处理数据
	 * @param result
	 * 
	 */
	private void processData(String result) {
		Gson gson=new Gson();
		PhotosBean bean=gson.fromJson(result, PhotosBean.class);
		System.out.println(bean.data.news.get(0).title);
		photoList = bean.data.news;
		PhotoAdapter photoAdapter=new PhotoAdapter();
		mListView.setAdapter(photoAdapter);
		
	}
	class PhotoAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return photoList.size();
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
			PhotosNewsHolder mHolder=null;
		if (convertView==null) {
			convertView=View.inflate(mContext, R.layout.photos_item, null);
			mHolder=new PhotosNewsHolder();
			mHolder.ivImage=(ImageView) convertView.findViewById(R.id.iv_photos_item);
			mHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_photos_item);
			convertView.setTag(mHolder);
			
		}else {
			mHolder=(PhotosNewsHolder) convertView.getTag();
		}
		PhotoItem photoItems= photoList.get(position);
		mHolder.tvTitle.setText(photoItems.title);
//		BitmapUtils bitmapUtils=new BitmapUtils(mContext);
//		bitmapUtils.display(mHolder.ivImage, photoItems.listimage);
		//为了防止图片错乱，给ivimage设置一张默认图片
		mHolder.ivImage.setImageResource(R.drawable.pic_item_list_default);
	//给ivimage打一个tag（标识）;
		mHolder.ivImage.setTag(position);
		//取图片
Bitmap bitmap=imageUtils.getImageFromUrl(photoItems.listimage,position);
if (bitmap!=null) {
	//当前是从内存或本地取得图片
	mHolder.ivImage.setImageBitmap(bitmap);
}
			return convertView;
		}
		
		
	}
	 class PhotosNewsHolder{
		public ImageView ivImage;
		public TextView tvTitle;
		
	}
	/**
	 * @param ibListOrGrid
	 * 切换当前页面的显示，gridorlist，且修改按钮背景
	 */
	public void switchCurrentPager(ImageButton ibListOrGrid) {
		if (isDisplayList) {
			//切换到grid页面
			mGridView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mGridView.setAdapter(new PhotoAdapter());
			isDisplayList=false;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_list_type);
		}else {
			//切换到list
			mGridView.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
			mListView.setAdapter(new PhotoAdapter());
			isDisplayList=true;
			ibListOrGrid.setImageResource(R.drawable.icon_pic_grid_type);
		}
		
	}

}
