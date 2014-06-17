package com.intel.store.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.StorePhotoModel;
import com.intel.store.util.ImageInfoWarp;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;

@SuppressLint("SimpleDateFormat")
public class StoreImageShowActivity extends BaseActivity implements
		IImageLoadCallback, MyActivityInterface {
	private GridView grid_store_photo;
	private Button btn_back;
	private Button btn_photo_add;
	private TextView txt_title_msg;

	private ArrayList<MapEntity> photoList;
	private PhotoAdapter mAdapter;
	private LoadingView loadingView;
	private Bitmap defaultBitmap;
	private StorePhotoController mPhotoController;
	private TextView txt_empty;
	private String dateOfPic;
	private String storeID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_show);
		initPassedValues();
		initView();
		setListener();
		StoreApplication.asyncImageLoader
				.setImageLoadCallback(StoreImageShowActivity.this);
	}

	private void initPassedValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		dateOfPic = bundle.getString("last_upd_dtm");
		storeID = bundle.getString("store_id");
	}

	@Override
	public void initView() {

		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		grid_store_photo = (GridView) findViewById(R.id.grid_store_photo);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_photo_add = (Button) findViewById(R.id.btn_photo_add);
		txt_empty = (TextView) findViewById(R.id.txt_empty);
		// 设置title
		txt_title_msg = (TextView) findViewById(R.id.txt_title_msg);
		String params[] = new String[3];
		params[0] = dateOfPic.substring(0, 4);
		params[1] = dateOfPic.substring(4, 6);
		params[2] = dateOfPic.substring(6, 8);
		txt_title_msg.setText(getString(R.string.txt_pic_upload_time_title,params[0],params[1],params[2]));

		mPhotoController = new StorePhotoController();
		Loger.d("initView..........");
		photoList = new ArrayList<MapEntity>();
		try {
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.store_list_default);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		mAdapter = new PhotoAdapter(this, photoList);
		grid_store_photo.setAdapter(mAdapter);

		if (PhoneStateUtil.hasInternet()) {
			getImageViews();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		try {
			int currentday = dateFormat.parse(dateOfPic).getDate();
			if (currentday != new Date().getDate()) {
				btn_photo_add.setVisibility(View.GONE);
			}
		} catch (ParseException e) {
			btn_photo_add.setVisibility(View.GONE);
			e.printStackTrace();
		}
	}

	/**
	 * 加载当天的图片
	 */
	private void getImageViews() {
		String[] arg = new String[3];
		arg[0] = StoreSession.getRepID();
		arg[1] = dateOfPic;
		arg[2] = storeID;
		mPhotoController.queryPicByDate(new StorePhotoUpdateView(StoreImageShowActivity.this), arg);
	}

	@Override
	public void setListener() {

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		grid_store_photo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View veiw,
					int position, long id) {
				Intent intent = new Intent(StoreImageShowActivity.this,
						StoreImageDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("position", position);
				bundle.putSerializable("photoList", photoList);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		btn_photo_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_photo_taken = new Intent();
				intent_photo_taken.setClass(StoreImageShowActivity.this,
						StoreImageTypeListActivity.class);
				startActivity(intent_photo_taken);

			}
		});

	}

	private class StorePhotoUpdateView extends
			StoreCommonUpdateView<ArrayList<MapEntity>> {
		public StorePhotoUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
			super.onPreExecute();
		}

		@Override
		public void onPostExecute(ArrayList<MapEntity> result) {
			if (result != null) {
				photoList.clear();
				photoList.addAll(result);
				mAdapter.setDataList(photoList);
				Loger.d("onPostExecute-->");
				mAdapter.notifyDataSetChanged();
			}
			if (photoList.size() == 0) {
				txt_empty.setVisibility(View.VISIBLE);
			} else {
				txt_empty.setVisibility(View.GONE);
			}
			loadingView.hideLoading();
		}


		@Override
		public void handleException(IException ex) {
			super.handleException(ex);
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					getImageViews();
				}
			};
			DialogInterface.OnClickListener onCancelListener=new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				StoreImageShowActivity.this.finish();	
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);
			
		}
	}

	public class PhotoAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		List<MapEntity> dataList = new ArrayList<MapEntity>();

		public void setDataList(List<MapEntity> dataList) {
			this.dataList = dataList;
		}

		public PhotoAdapter(Context context, List<MapEntity> dataList) {
			inflater = LayoutInflater.from(context);
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold = null;
			if (convertView == null) {
				hold = new ViewHold();
				convertView = inflater.inflate(R.layout.store_image_grid_item,
						null);
				hold.img_photo = (ImageView) convertView
						.findViewById(R.id.img_photo);
				hold.txt_category = (TextView) convertView
						.findViewById(R.id.txt_category);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}

			String url = dataList.get(position).getString(
					StorePhotoModel.PHT_PTH);
			hold.txt_category.setText(dataList.get(position).getString(
					StorePhotoModel.PHT_CAT_NM));

			Bitmap bm = StoreApplication.asyncImageLoader
					.getBitmapFromCache(url);

			if (bm != null) {

				hold.img_photo.setImageBitmap(bm);
			} else {
				hold.img_photo.setImageBitmap(defaultBitmap);
				ImageInfoWarp imageInfo = new ImageInfoWarp(url, true, true,
						100);
				imageInfo.setImageView(hold.img_photo);
				StoreApplication.asyncImageLoader.loadImage(imageInfo);
			}

			return convertView;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initView();
	}

	class ViewHold {
		public ImageView img_photo;
		public TextView txt_category;
	}

	@Override
	public void fail(ImageInfo arg0) {
		Loger.d("fail");
	}

	@Override
	public void success(ImageInfo imageInfo) {
		mAdapter.notifyDataSetChanged();

	}

}
