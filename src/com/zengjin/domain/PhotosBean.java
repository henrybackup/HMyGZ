package com.zengjin.domain;

import java.util.List;

import android.R.integer;

public class PhotosBean {
public int retcode;
public PhotosData data;

public class PhotosData{
	public String countcommenturl;
	public String more;
	public List<PhotoItem> news;
	public String title;
	public List<String> topic;
	
}
public class PhotoItem{
	public String comment;
	public String commentlist;
	public String commenturl;
	public String id;
	public String largeimage;
	public String listimage;
	public String pubdate;
	public String smallimage;
	public String title;
	public String type;
	public String url;
	
}


}
