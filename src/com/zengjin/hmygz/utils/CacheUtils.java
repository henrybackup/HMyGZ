package com.zengjin.hmygz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CacheUtils {
	public static final String CACHE_FILE_NAME="zengjin";
	public static  SharedPreferences sharedPreferences;

/**
 * @param context
 * @param key
 * @param defValue
 * @return
 * 缓存类，所有数据采用sharedpreferences的方式存储获取
 */
public static boolean getBoolean(Context context,String key,boolean defValue) {
	  if (sharedPreferences==null) {
		
		  sharedPreferences=context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
	}
	//defvalue自己设置
	return sharedPreferences.getBoolean(key, defValue);
	
}
public static void putBoolean(Context context ,String key,boolean value){
	  if (sharedPreferences==null) {	
		  sharedPreferences=context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
	}
	  Editor editor=sharedPreferences.edit();
	  editor.putBoolean(key, value);
	  editor.commit();
	
}
public static void putString(Context context,String key,String value){
	if (sharedPreferences==null) {	
		  sharedPreferences=context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
	}
	  Editor editor=sharedPreferences.edit();
	  editor.putString(key, value);
	  editor.commit();
}
public static String getString(Context context,String key,String defValue) {
	  if (sharedPreferences==null) {
		
		  sharedPreferences=context.getSharedPreferences(CACHE_FILE_NAME, Context.MODE_PRIVATE);
	}
	//defvalue自己设置
	return sharedPreferences.getString(key, defValue);
	
}
}
