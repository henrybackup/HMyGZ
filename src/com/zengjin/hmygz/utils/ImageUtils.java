package com.zengjin.hmygz.utils;

import android.graphics.Bitmap;
import android.os.Handler;

public class ImageUtils {
	
	
private NetCache mNetCache;
private MemoryCache mMemoryCache;
public ImageUtils(Handler handler) {

	//定义内存缓存对象，就是往map放集合
mMemoryCache=new MemoryCache();
	
	mNetCache = new NetCache(handler,mMemoryCache);


	}

public Bitmap getImageFromUrl(String url, int tag){
	//1.从内存中取，然后直接返回
	Bitmap bm=mMemoryCache.getBitmap(url);
	if (bm!=null) {
		System.out.println("从内存种取");
		return bm;
	}
	//2.从本地种取，然后直接返回
	bm=LocalCache.getBitmap(url);
	if (bm!=null) {
		System.out.println("从本地种取");
		
		//return之后就不走网络那一块了
		return bm;
	}
	//3.从网络中取，从子线程异步抓取，不能直接返回,抓取完毕之后，-得到图片，使用handler发送消息给使用者
	System.out.println("从网络中取得");
	mNetCache.getBitmapFromNet(url,tag);
	return null;
	
}
}
