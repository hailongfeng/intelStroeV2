package com.intel.store.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.pactera.framewidget.MyImageView;
import com.pactera.framework.imgload.AsyncImageLoader;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.util.Loger;

/**
 * @author P0033759
 * 最后修改时间	2014-2-20-上午9:15:37
 * 功能	查看大图
 */
public class PhotoZoomActivity extends Activity implements
		IImageLoadCallback {
	public static String IMG_TYPE = "img_type";
	public static String IMG_VERTICAL = "img_vertical";
	public static String IMG_STORE_NAME = "img_store_name";
	public static String IMG_STORE_ADDRESS = "img_store_address";
	public static String IMG_STYLE = "img_style";
	public static String IMG_URL = "img_url";

	private LinearLayout img_container;
	private LinearLayout.LayoutParams lp;
	private MyImageView imageView;
	private ImageView img_pic;
	private TextView img_loading;
	//private AsyncImageLoader asyncImageLoader;
	private Bitmap defBitmap;

	private String photoUrl;
	/**	显示的图片 */
	private Bitmap photo_btm;
	private boolean unupload = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_zoom);
		init();
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			//存在本地副本unupload
			if (bundle.containsKey("unupload")&&bundle.getBoolean("unupload")) {
				unupload = true;
				//获取副本图片地址
				if (bundle.containsKey("absolute_picture_path")) {
					//缓存获取副本图片
					String m_strPicturePath = bundle.getString("absolute_picture_path");
					System.err.println("m_strPicturePath:"+m_strPicturePath);
						photo_btm = StoreApplication.bitmapCache.getBitmap(m_strPicturePath);
						//decode获取副本图片
						if (photo_btm==null) {
							photo_btm = BitmapFactory.decodeFile(m_strPicturePath);
							StoreApplication.bitmapCache.addBitmap(m_strPicturePath, photo_btm);
						}
				}else {
					//失败则载入默认图片
					photo_btm =defBitmap;
				}
			} else {
				photoUrl = bundle.getString("photo_url");
			}
		}
		int height;
		int bigImgWidth = getBigImageViewWidth();
		height = 4 * bigImgWidth / 3;
		lp = new LinearLayout.LayoutParams(bigImgWidth, height);
		FrameLayout.LayoutParams container_lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		img_container.setLayoutParams(container_lp);
		imageView = new MyImageView(this, getBigImageViewWidth(),
				getBigImageViewHeight());
		img_container.addView(imageView, container_lp);
		
		if (unupload) {
			updateView.sendEmptyMessage(1);
		} else {
			updateView.sendEmptyMessage(0);
		}

		img_container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//XXX 不知道要不要finish
				//finish();
			}
		});
	}

	private void init() {
		//asyncImageLoader = new AsyncImageLoader(this, 1, 1);
		img_loading = (TextView) findViewById(R.id.img_loading);
		img_pic = (ImageView) findViewById(R.id.img_pic);
		img_container = (LinearLayout) findViewById(R.id.img_container);
		try {
			defBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.default_img);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler updateView = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Loger.d("-------------getBitmapFromCache");
				Bitmap bmp = StoreApplication.asyncImageLoader.getBitmapFromCache(photoUrl);
				if (bmp != null) {
					img_pic.setVisibility(View.GONE);
					img_loading.setVisibility(View.GONE);
					imageView.setImageBitmap(bmp);
				} else {
					StoreApplication.asyncImageLoader.loadImage(new ImageInfo("header_brand",
							photoUrl, lp.height));
					img_pic.setImageBitmap(defBitmap);
				}
				break;
			case 1:
				Loger.d("case 1");
				Bitmap bmp2 = photo_btm;
				if (bmp2 != null) {
					Loger.d("bmp2 nut null");
					img_pic.setVisibility(View.GONE);
					img_loading.setVisibility(View.GONE);
					imageView.setImageBitmap(bmp2);
				}else {
					Loger.d("bmp2 null");
					
				}
				break;
			}
		}

	};

	protected void onDestroy() {
		super.onDestroy();
	/*	if (defBitmap != null) {
			defBitmap.recycle();
			defBitmap = null;
		}
		if (photo_btm!=null) {
			photo_btm.recycle();
			photo_btm=null;
		}*/
	/*	if (asyncImageLoader != null) {
			asyncImageLoader.stopThreads();
			asyncImageLoader = null;
		}*/
	};

	int getBigImageViewWidth() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		return width;
	}

	/**
	 * @return 屏幕高度
	 */
	int getBigImageViewHeight() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int height = dm.heightPixels;
		return height;
	}

	@Override
	public void success(ImageInfo imageInfo) {
		updateView.sendEmptyMessage(0);
	}

	@Override
	public void fail(ImageInfo imageInfo) {
		img_loading.setText(getString(R.string.txt_pic_fail));
	}

}