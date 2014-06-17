package com.intel.store.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.Toast;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicModel;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.util.ImageInfoWarp;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;

public class StoreImageShowByTypeActivity extends BaseActivity implements
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
	private String wvId;
	private StorePhotoUpdateView storePhotoUpdateView;
	private TextView txt_require;
	private TextView txt_require_number;
	private MapEntity entity;
	private boolean increasable = false;// 是否可以增加拍照
	private BitmapDrawable defaultBitmapDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initPassedValues();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_show_by_type);
		storePhotoUpdateView = new StorePhotoUpdateView(this);
		initView();
		setListener();
		StoreApplication.asyncImageLoader
				.setImageLoadCallback(StoreImageShowByTypeActivity.this);
	}

	private void initPassedValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		wvId = bundle.getString("wv_id");
		increasable = bundle.getBoolean("increasable");
		entity = (MapEntity) bundle.getSerializable("entity");
		if (entity == null) {
			Loger.e("wrong");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (increasable) {
			MenuItem addPhoto = menu.add("拍照");
			MenuItemCompat.setShowAsAction(addPhoto,
					MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			addPhoto.setIcon(R.drawable.canmal_icon);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getTitle().toString().contains("拍照")) {
			addPhoto();
		}
		return true;
	}

	@Override
	public void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		grid_store_photo = (GridView) findViewById(R.id.grid_store_photo);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_photo_add = (Button) findViewById(R.id.btn_photo_add);
		if (!increasable) {
			btn_photo_add.setVisibility(View.GONE);
		}
		txt_empty = (TextView) findViewById(R.id.txt_empty);
		txt_require = (TextView) findViewById(R.id.txt_require);
		txt_require_number = (TextView) findViewById(R.id.txt_require_number);
		// 设置title
		txt_title_msg = (TextView) findViewById(R.id.txt_title_msg);
		txt_title_msg.setText(entity
				.getString(QueryPicCategoryModel.PHT_CAT_NM));
		// actionBar.setTitle(entity.getString(QueryPicCategoryModel.PHT_CAT_NM));

		txt_require.setText(getString(R.string.txt_take_picture_require) + ":"
				+ entity.getString(QueryPicCategoryModel.CMNTS));
		txt_require_number.setText(entity
				.getString(QueryPicCategoryModel.PHT_CAT_NM)
				+ ","
				+ getString(R.string.txt_take_picture_upload_num,
						entity.getString(QueryPicCategoryModel.PHT_QTY)));
		mPhotoController = new StorePhotoController();
		Loger.d("initView..........");
		photoList = new ArrayList<MapEntity>();
		try {
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.store_list_default);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		mAdapter = new PhotoAdapter(StoreImageShowByTypeActivity.this,
				photoList);
		grid_store_photo.setAdapter(mAdapter);

		if (PhoneStateUtil.hasInternet()) {
			getImageViews();
		}
	}

	private void getImageViews() {
		if (storePhotoUpdateView == null) {
			storePhotoUpdateView = new StorePhotoUpdateView(this);
		}
		mPhotoController.listPhotoByWvCategory(storePhotoUpdateView, wvId,
				entity.getString(QueryPicCategoryModel.PHT_CAT));
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
				Intent intent = new Intent(StoreImageShowByTypeActivity.this,
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
				addPhoto();
			}
		});

	}

	protected void addPhoto() {
		Intent intent_photo_taken = new Intent();
		intent_photo_taken.setClass(StoreImageShowByTypeActivity.this,
				PhotoUploadDetailActivity.class);
		Bundle data = new Bundle();
		data.putSerializable("photoCategory", entity);
		intent_photo_taken.putExtras(data);
		startActivity(intent_photo_taken);

	}

	private class StorePhotoUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {
		public StorePhotoUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
			super.onPreExecute();
		}

		@Override
		public void onPostExecute(List<MapEntity> result) {
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
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getImageViews();
				}
			};
			DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreImageShowByTypeActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);

		}
	}

	public class PhotoAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		List<MapEntity> mDataList;

		public void setDataList(List<MapEntity> dataList) {
			mDataList = null;
			mDataList = new ArrayList<MapEntity>();
			this.mDataList.addAll(dataList);
		}

		public PhotoAdapter(Context context, List<MapEntity> dataList) {
			inflater = LayoutInflater.from(context);
			this.mDataList = new ArrayList<MapEntity>();
			this.mDataList.addAll(dataList);
		}

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mDataList.get(arg0);
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
				convertView = inflater.inflate(
						R.layout.store_image_grid_by_type_item, null);
				hold.img_photo = (ImageView) convertView
						.findViewById(R.id.img_photo);
				hold.txt_category = (TextView) convertView
						.findViewById(R.id.txt_time);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}
			MapEntity entity = mDataList.get(position);

			String url = entity.getString(QueryPicModel.PHT_PTH);
			Loger.d("url:" + url);
			String patternData = "yyyy-MM-dd";
			String patternView = "yyyy年MM月dd日";
			SimpleDateFormat dateFormatData = new SimpleDateFormat(patternData);
			SimpleDateFormat dateFormatView = new SimpleDateFormat(patternView);
			String lasyUpTime = entity.getString(QueryPicModel.LAST_UPD_DTM)
					.substring(0, 10);
			try {
				Date date = dateFormatData.parse(lasyUpTime);
				lasyUpTime = dateFormatView.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			hold.txt_category.setText(lasyUpTime);

			Bitmap bm = StoreApplication.asyncImageLoader
					.getBitmapFromCache(url);

			if (bm != null) {
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bm);
				hold.img_photo.setBackgroundDrawable(bitmapDrawable);
			} else {
				defaultBitmapDrawable = new BitmapDrawable(getResources(),
						defaultBitmap);
				hold.img_photo.setBackgroundDrawable(defaultBitmapDrawable);
				ImageInfoWarp imageInfo = new ImageInfoWarp(url, true, true,
						500);
				imageInfo.setImageView(hold.img_photo);
				StoreApplication.asyncImageLoader.loadImage(imageInfo);
			}

			return convertView;
		}

		private class ViewHold {
			public ImageView img_photo;
			public TextView txt_category;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initView();
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
