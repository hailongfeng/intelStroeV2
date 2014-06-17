package com.intel.store.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.PhotoUploadController;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.util.LocationManager;
import com.intel.store.util.PhotoUploadHelper;
import com.intel.store.util.PictureItem;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PictureUtil;
import com.pactera.framework.util.ToastHelper;

/**
 * @author P0033759 最后修改时间 2014-2-18-下午5:57:08 功能 照片上传的具体页面
 */
public class PhotoUploadDetailActivity extends BaseActivity {
	// 拍照请求
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;
	// 查看照片请求
	private static final int REQUEST_ID_TO_PREVIEW = 102;
	private Button ibtn_title_back;
	private TextView txt_title_msg;
	private TextView txt_photo_upload_type;
	private TextView txt_photo_upload_notice;
	private Button btn_title_send;
	private EditText edt_photo_upload_desc;
	private GridView gv_thumbnails;
	private ArrayList<PictureItem> m_lstPictures = new ArrayList<PictureItem>();
	private PhotoUploadThumbnailAdapter photoUploadThumbnailAdapter;
	private String mStoreId = "";
	private String mCategoryId = "";
	private String mCategoryName = "";
	private String mPictureAbsolutePath = "";
	private String mCompressedPictureAbsolutePath = "";
	private PhotoUploadController mPhotoUploadController = new PhotoUploadController();
	private LoadingView loadingView_upload;
	private ViewHelper mviewHelper = new ViewHelper(this);
	private MapEntity photoCategory;
	private boolean itemClickable =true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_upload_detail);
		initPassedValues();
		initView();
		setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.store_image_upload, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_upload:
			if (itemClickable) {
				postPhoto();
			}
			break;
		case android.R.id.home:
			if (m_lstPictures.size() != 0) {
				showLeavePageDialog();
			} else {
				finish();
			}
		default:
			break;
		}
		return true;
	}
	private void initPassedValues() {
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		if (data != null) {
			photoCategory = (MapEntity) getIntent().getExtras().getSerializable("photoCategory");
		}
		StoreApplication.bitmapCache = new BitmapCache(20);
	}

	private void initView() {
		loadingView_upload = (LoadingView) findViewById(R.id.common_id_ll_loading);
		ibtn_title_back = (Button) findViewById(R.id.ibtn_title_back);
		txt_title_msg = (TextView) findViewById(R.id.txt_title_msg);
		btn_title_send = (Button) findViewById(R.id.btn_title_send);
		txt_title_msg.setText(getString(R.string.txt_pic_upload_title));
		txt_photo_upload_type = (TextView) findViewById(R.id.txt_pic_upload_type_choice);
		txt_photo_upload_notice = (TextView) findViewById(R.id.txt_pic_upload_type_notice);
		edt_photo_upload_desc = (EditText) findViewById(R.id.edt_photo_upload_desc);
		if (!StoreSession.getGPSstatu()) {
			android.content.DialogInterface.OnClickListener positiveListener = new android.content.DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreSession.setGPSstatu(true);
					LocationManager.getInstance().locationOnce(null);
				}
			};
			android.content.DialogInterface.OnClickListener negativeListener = null;
			OnCancelListener onCancelListener = null;
			mviewHelper.showBTN2Dialog("", getString(R.string.sales_reporte_ask_location),getString(R.string.sales_reporte_location_on),getString(R.string.sales_reporte_location_off),
					positiveListener, negativeListener, onCancelListener);
		} else {
			LocationManager.getInstance().locationOnce(null);
		}

		gv_thumbnails = (GridView) findViewById(R.id.gv_thumbnails);
		setPicCatgeory();

		// 初始化选择
		photoUploadThumbnailAdapter = new PhotoUploadThumbnailAdapter(this);
		gv_thumbnails.setAdapter(photoUploadThumbnailAdapter);
		enableBtns();
	}

	private void setPicCatgeory() {
		txt_photo_upload_type.setText(photoCategory
				.getString(QueryPicCategoryModel.PHT_CAT_NM));
		txt_photo_upload_notice
				.setText("("+getString(R.string.txt_pic_upload_least,photoCategory
						.getString(QueryPicCategoryModel.PHT_QTY))
						+ "）");
	}

	private void setListener() {
		ibtn_title_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (m_lstPictures.size() != 0) {
					showLeavePageDialog();
				} else {
					finish();
				}
			}
		});
		btn_title_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Loger.d("m_lstPictures.size()" + m_lstPictures.size());
				postPhoto();
			}
		});
		gv_thumbnails.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				PictureItem pictureItem = (PictureItem) photoUploadThumbnailAdapter
						.getItem(position);
				if (pictureItem.mIsPlusBtn) {
					startCameraForResult();
				} else {
					navigateToPreviewPage(pictureItem, position);
				}
			}
		});

		// XXX
		/*
		 * loadingView_upload.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * Loger.d("m_lstPictures.size()" + m_lstPictures.size()); postPhoto();
		 * } });
		 */
	}

	private void startCameraForResult() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// sdcard/store/
			mCategoryName = photoCategory
					.getString(QueryPicCategoryModel.PHT_CAT_NM);
			mPictureAbsolutePath = PhotoUploadHelper.createImageFile(mStoreId,
					mCategoryId);
			Loger.d("mPictureAbsolutePath:" + mPictureAbsolutePath);
			Loger.d("mCategoryNamet:" + mCategoryName);
			File file = new File(mPictureAbsolutePath);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(file));
			startActivityForResult(takePictureIntent,
					CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		} catch (IOException e) {
			ToastHelper.getInstance().showShortMsg(e.getMessage());
			e.printStackTrace();
		}
	}

	private void navigateToPreviewPage(PictureItem pictureItem, int position) {
		Intent intent = new Intent(PhotoUploadDetailActivity.this,
				PhotoUploadPreviewActivity.class);
		Bundle data = new Bundle();
		data.putString("absolute_picture_path", pictureItem.mPictureFileName);
		data.putInt("position", position);
		intent.putExtras(data);
		startActivityForResult(intent, REQUEST_ID_TO_PREVIEW);
	}

	private void enableBtns() {
		btn_title_send.setEnabled(!m_lstPictures.isEmpty());
		itemClickable=!m_lstPictures.isEmpty();
		edt_photo_upload_desc.setEnabled(true);
		gv_thumbnails.setEnabled(true);
		gv_thumbnails.setFocusable(true);
		gv_thumbnails.setClickable(true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Loger.d("mPictureAbsolutePath = " + mPictureAbsolutePath
						+ ", mCategoryName = " + mCategoryName);
				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				long currentMiliSec = System.currentTimeMillis();
				mCompressedPictureAbsolutePath = "";
				mCompressedPictureAbsolutePath = PhotoUploadHelper
						.preparePhoto(mPictureAbsolutePath, mCategoryName,
								currentMiliSec);

				PictureItem pictureItem = newPictureItem(
						mCompressedPictureAbsolutePath, currentMiliSec);
				Loger.i("mCompressedPictureAbsolutePath:"+mCompressedPictureAbsolutePath);
				if (pictureItem != null) {
					m_lstPictures.add(pictureItem);
					photoUploadThumbnailAdapter.setData(m_lstPictures);
					photoUploadThumbnailAdapter.notifyDataSetChanged();
					btn_title_send.setEnabled(!m_lstPictures.isEmpty());
					itemClickable=!m_lstPictures.isEmpty();
				}
				// 删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mPictureAbsolutePath);
			}
		} else if (requestCode == REQUEST_ID_TO_PREVIEW) {
			if (resultCode == Activity.RESULT_OK) {
			} else if (resultCode == PhotoUploadPreviewActivity.PREVIEW_RESULT_DELETED) {
				Bundle extras = data.getExtras();
				int position = extras.getInt("position");
				if (position != -1) {
					m_lstPictures.remove(position);
					photoUploadThumbnailAdapter.setData(m_lstPictures);
					photoUploadThumbnailAdapter.notifyDataSetChanged();
					btn_title_send.setEnabled(!m_lstPictures.isEmpty());
					itemClickable=!m_lstPictures.isEmpty();
				}
			}
		}
	}

	private PictureItem newPictureItem(String absolutePicturePath,
			long currentMiliSec) {
		if (photoCategory != null) {
			// XXX 写死的photo类型
			// String photoCat = "30";
			// photoCategory.getString(QueryPicCategoryModel.PHT_CAT);
			String photoCat = photoCategory
					.getString(QueryPicCategoryModel.PHT_CAT);
			String slsprsId = StoreSession.getRepID();
			String storeId = StoreSession.getCurrentStoreId();
			String roleId = StoreSession.getCurrentStoreRole();
			if (StoreSession.getGPSstatu()
					&& StoreSession.getLocateOkId().equals("success")) {
				return new PictureItem(slsprsId, storeId,
						System.currentTimeMillis() + "",
						"",// comment 上传的时候再添加
						StoreSession.getLongitude(),
						StoreSession.getLongitude(),
						roleId,// role_id
						photoCat,// photo_cat 30
						"", // mdl_id
						"", // cityType
						StoreSession.getCurrentAddress(),// stor_addr
						absolutePicturePath,// pictureFileName
						photoCategory
								.getString(QueryPicCategoryModel.PHT_CAT_NM),// categoryName
						"", false, slsprsId);
			}
			// 不定位或者定位失败，则不上传地址
			return new PictureItem(slsprsId, storeId, ""
					+ System.currentTimeMillis(), "",// comment 上传的时候再添加
					null, null, roleId,// role_id
					photoCat,// photo_cat 30
					"", // mdl_id
					"", // cityType
					null,// stor_addr
					absolutePicturePath,// pictureFileName
					photoCategory.getString(QueryPicCategoryModel.PHT_CAT_NM),// categoryName
					"", false, slsprsId);
		} else {
			mviewHelper.showBTNDialog("请先选择图片类型");
			return null;
		}
	}

	private void postPhoto() {
		if (m_lstPictures == null || m_lstPictures.size() == 0) {
			return;
		}
		String comment = edt_photo_upload_desc.getText().toString();
		setComment(m_lstPictures, comment);
		mPhotoUploadController.uploadPhoto(new UploadPhotoViewCallback(this),
				m_lstPictures, this);
	}

	private void setComment(ArrayList<PictureItem> lstPictures, String comment) {
		for (int i = 0; i < lstPictures.size(); ++i) {
			PictureItem pictureItem = lstPictures.get(i);
			if (pictureItem != null) {
				pictureItem.mComment = comment;
			}
		}
	}

	private void resetLstPictures(ArrayList<PictureItem> list) {
		if (m_lstPictures == null) {
			m_lstPictures = new ArrayList<PictureItem>();
			m_lstPictures.clear();
		}

		m_lstPictures.removeAll(list);
		photoUploadThumbnailAdapter.setData(m_lstPictures);
		photoUploadThumbnailAdapter.notifyDataSetChanged();

		btn_title_send.setEnabled(!m_lstPictures.isEmpty());
		itemClickable=!m_lstPictures.isEmpty();
		// edt_photo_upload_desc.setText("");
	}

	private void showLeavePageDialog() {
		new ViewHelper(PhotoUploadDetailActivity.this).showBTN2Dialog(getString(R.string.txt_pic_upload_title),
				getString(R.string.txt_pic_upload_give_up), getString(R.string.txt_yes), getString(R.string.txt_no),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						finish();
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}, new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
					}
				});
	}

	@Override
	public void onBackPressed() {
		if (m_lstPictures.size() != 0) {
			showLeavePageDialog();
		} else {
			finish();
		}
	}

	private class PhotoUploadThumbnailAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<PictureItem> mPictureItems;

		public PhotoUploadThumbnailAdapter(Context context) {
			this.mContext = context;
			resetPictureItems();
		}

		private void resetPictureItems() {
			if (mPictureItems == null) {
				mPictureItems = new ArrayList<PictureItem>();
			}
			mPictureItems.clear();
			mPictureItems.add(PictureItem.pictureItemPlusSign);
		}

		public void setData(ArrayList<PictureItem> lstData) {
			resetPictureItems();
			mPictureItems.addAll(0, lstData);
		}

		@Override
		public int getCount() {
			return mPictureItems == null ? 0 : mPictureItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mPictureItems == null ? null : mPictureItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			Loger.d("----getView----:" + getCount());
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.photo_upload_detail_thumbnail_item, null);
				holder.ibtn_thumbnail = (ImageButton) convertView
						.findViewById(R.id.ibtn_thumbnail);
				holder.txt_thumbnail_category = (com.intel.store.widget.ScrollAlwaysTextView) convertView
						.findViewById(R.id.txt_thumbnail_category);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PictureItem pictureItem = (PictureItem) getItem(position);
			if (pictureItem.mIsPlusBtn) {
				holder.ibtn_thumbnail
						.setBackgroundResource(R.drawable.photo_upload_add_bg2);
				holder.txt_thumbnail_category.setText("");
			} else {
				Loger.d("---------getView");
				String fileName = pictureItem.mPictureFileName;
				Bitmap bitmap = BitmapFactory.decodeFile(fileName);
				// Bitmap bitmap = PictureUtil.getSmallBitmap(fileName, 240,
				// 320);
				BitmapDrawable bitDrawable = new BitmapDrawable(bitmap);
				holder.ibtn_thumbnail.setBackgroundDrawable(bitDrawable);

				String strCategory = pictureItem.mCategoryName;
				holder.txt_thumbnail_category.setText(strCategory);
			}
			return convertView;
		}

		private class ViewHolder {
			public ImageButton ibtn_thumbnail;
			public com.intel.store.widget.ScrollAlwaysTextView txt_thumbnail_category;
		}
	}

	public class UploadPhotoViewCallback extends
			StoreCommonUpdateView<ArrayList<PictureItem>> {

		public UploadPhotoViewCallback(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingView_upload.hideLoading();
		}

		@Override
		public void onPreExecute() {
			btn_title_send.setClickable(false);
			itemClickable=false;
			loadingView_upload.showLoading();
		}

		@Override
		public void onPostExecute(ArrayList<PictureItem> items) {
			btn_title_send.setClickable(true);
			itemClickable=true;
			if (items != null) {
				int unsuccess = m_lstPictures.size() - items.size();
				resetLstPictures(items);
				if (unsuccess == 0) {
					ToastHelper.getInstance().showLongMsg(getString(R.string.txt_pic_upload_all_success));
					edt_photo_upload_desc.setText("");
				} else {
					ToastHelper.getInstance().showLongMsg(getString(R.string.txt_pic_upload_fail,unsuccess));
				}
			} else {

			}
			loadingView_upload.hideLoading();
		}

		@Override
		public void handleException(IException ex) {
			// TODO Auto-generated method stub
			mviewHelper.showErrorDialog(ex);
			btn_title_send.setClickable(true);
			itemClickable=true;
			loadingView_upload.hideLoading();
		}
	}

	@Override
	protected void onDestroy() {
		if (m_lstPictures!=null&&m_lstPictures.size()>0) {
			for (PictureItem pictureItem:m_lstPictures) {
				Loger.i("delete: "+	pictureItem.mPictureFileName);
				PictureUtil.deleteTempFile(	pictureItem.mPictureFileName);
			}
		}
		if (StoreApplication.bitmapCache != null) {
			StoreApplication.bitmapCache.clearMemoryCache();
			StoreApplication.bitmapCache = null;
		}
		super.onDestroy();
	}

}
