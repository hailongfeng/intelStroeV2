package com.intel.store.util;

import android.widget.ImageView;

import com.pactera.framework.imgload.ImageInfo;

public class ImageInfoWarp extends ImageInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4364872715306492040L;
	private ImageView imageView;

	public ImageView getImageView() {
		return imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}
	
	public ImageInfoWarp(String url, boolean useCache, boolean save2sd, int maxSizeInPixel) {
		super(url, useCache, save2sd, maxSizeInPixel);
		// TODO Auto-generated constructor stub
	}

	public ImageInfoWarp(String url, int maxSizeInPixel) {
		super(url, maxSizeInPixel);
		// TODO Auto-generated constructor stub
	}

	public ImageInfoWarp(String key, String url, boolean useCache, boolean save2sd, int maxSizeInPixel) {
		super(key, url, useCache, save2sd, maxSizeInPixel);
		// TODO Auto-generated constructor stub
	}

	public ImageInfoWarp(String key, String url, int maxSizeInPixel) {
		super(key, url, maxSizeInPixel);
		// TODO Auto-generated constructor stub
	}

	public ImageInfoWarp(String key, String url) {
		super(key, url);
		// TODO Auto-generated constructor stub
	}

	public ImageInfoWarp(String url) {
		super(url);
		// TODO Auto-generated constructor stub
	}


	
	
}
