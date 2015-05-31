package com.zengjin.hmygz.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

/**
 * @author Administrator 网络缓存类
 */
public class NetCache {
	public static final int SUCCESS=0;
	public static final int FAILED=1;
	private Handler mHandler;
	private ExecutorService mExecutorService;
	private MemoryCache mMemoryCache;

	public NetCache(Handler handler,MemoryCache mMemoryCache) {
		this.mHandler = handler;
		this.mMemoryCache=mMemoryCache;
		mExecutorService = Executors.newFixedThreadPool(5);

	}

	public Bitmap getBitmapFromNet(String url,int tag) {
//	new Thread(new InternalRunnable(url,tag)).start();
		mExecutorService.execute(new InternalRunnable(url, tag));
		return null;

	}
class InternalRunnable implements Runnable{

	private String url;//当前任务需要请求的网咯地址
	private int tag;//当前这次请求图片的标识



	public InternalRunnable(String url,int tag){
		this.url=url;
		this.tag=tag;
	}
	
	
	
	@Override
	public void run() {
		HttpURLConnection conn=null;
	//访问网络，抓取图片
try {
	URL myurl=new URL(url);
	 conn=(HttpURLConnection)myurl.openConnection();
	conn.setRequestMethod("GET");
	conn.setConnectTimeout(5000);
	conn.setReadTimeout(5000);
	conn.connect();
	int responseCode=conn.getResponseCode();
	if (responseCode==200) {
		InputStream is = conn.getInputStream();
	Bitmap bm = BitmapFactory.decodeStream(is);
	Message msg = mHandler.obtainMessage();
	msg.arg1=tag;
	msg.obj=bm;
	msg.what=SUCCESS;
	//就是把消息给mhandler
	msg.sendToTarget();
	//向本地存一份
	LocalCache.putBitmap(url, bm);
	//向内存存一份
	mMemoryCache.putBitmap(url, bm);
	
//不再执行下面代码
	return;
	}
	
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}finally{
	if (conn!=null) {
		conn.disconnect();
	}
	
}
		//有错误的话直接跳到catch，然后执行下面语句，因为有return
mHandler.obtainMessage(FAILED).sendToTarget();

	}
	
	
}
}
