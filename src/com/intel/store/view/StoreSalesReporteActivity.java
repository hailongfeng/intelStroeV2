package com.intel.store.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StoreController;
import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.intel.store.dao.local.LocalSaleReportDao;
import com.intel.store.model.ProductTypeModel;
import com.intel.store.model.SalesReportModel;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.InputChecker;
import com.intel.store.util.PhotoUploadHelper;
import com.intel.store.util.PictureItem;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.CacheDir;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PictureUtil;
import com.pactera.framework.util.ToastHelper;
import com.zxing.activity.CaptureActivity;

public class StoreSalesReporteActivity extends BaseActivity implements
		OnClickListener {

	private RelativeLayout rl_sales_reporte_pro_type_select;

	private RelativeLayout rl_sales_reporte_pro_model_selects;
	private Button btn_scan_first;
	private Button btn_sales_reporte_submit;
	private TextView txt_sales_reporte_pro_type;
	private TextView txt_sales_reporte_pro_model;
	private EditText edt_scan_first;


	private List<MapEntity> mProductTypeDataList;
	private String[] mProductTypeNameList;

	private StoreController storeController;

	private ViewHelper mViewHelper;

	private List<MapEntity> mProductModelDataList;
	private String[]  mProductModelNameList;

	private String upPhotoName = "";

	private LoadingView loadingView;

	private String currentProductTypeId = null;
	private String currentProductModelId = null;

	private ArrayList<PictureItem> m_lstPictures = new ArrayList<PictureItem>();
	private GridView gv_thumbnails;
	private PhotoUploadThumbnailAdapter photoUploadThumbnailAdapter;
	private String mPictureAbsolutePath = "";
	private String mCompressedPictureAbsolutePath = "";

	// 扫码请求
	private static final int SCAN = 25;
	// 拍照请求
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 7;
	// 查看照片请求
	private static final int REQUEST_ID_TO_PREVIEW = 8;
	private static int storeTypeId = 0;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Loger.d(StoreSession.getCurrentStoreRole());
		storeTypeId = Integer.parseInt(StoreSession.getCurrentStoreRole());
		storeController = new StoreController();
		mViewHelper = new ViewHelper(StoreSalesReporteActivity.this);
		StoreApplication.bitmapCache = new BitmapCache(30);

		if (storeTypeId == StoreListModel.DIY) {
			setContentView(R.layout.activity_store_sales_diy_reporte);
			initDiyView();
			setDiyListener();
			// 初始化图片添加
			photoUploadThumbnailAdapter = new PhotoUploadThumbnailAdapter(this);
			gv_thumbnails.setAdapter(photoUploadThumbnailAdapter);

		} else if (storeTypeId == StoreListModel.OEM) {
			setContentView(R.layout.activity_store_sales_oem_reporte);
			initOemView();

			mProductTypeDataList = new ArrayList<MapEntity>();
			mProductModelDataList = new ArrayList<MapEntity>();
			setOemListener();
			// 初始化图片添加
			photoUploadThumbnailAdapter = new PhotoUploadThumbnailAdapter(this);
			gv_thumbnails.setAdapter(photoUploadThumbnailAdapter);
			storeController.getProductTypeFromeRemote(
					new ProductLoadUpdateView(StoreSalesReporteActivity.this),
					"all");

		}// oem结束

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.store_sale_reporte, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent	intent = null;
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_store_sales_report_history:
			intent=new Intent(StoreSalesReporteActivity.this,StoreSalesReporteHistoryActivity.class);
			break;
		default:
			break;
		}
		if (intent!=null) {
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void DataLoaded() {
		ProductTypeModel productTypeModel = new ProductTypeModel();
		List<MapEntity> data = new ArrayList<MapEntity>();
		try {
			data = productTypeModel.getAllBrndFromLocal();
		} catch (DBException e) {
			e.printStackTrace();
		}
		mProductTypeDataList.addAll(data);
		mProductTypeNameList = new String[mProductTypeDataList.size()];
		for (int i = 0; i < mProductTypeDataList.size(); i++) {
			mProductTypeNameList[i] = mProductTypeDataList.get(i).getString(
					ProductTypeModel.BRND_NM);
		}
		Loger.i("size=" + mProductTypeDataList.size());
	}
	
	private void initDiyView() {
		btn_scan_first = (Button) findViewById(R.id.btn_scan_first);
		btn_sales_reporte_submit = (Button) findViewById(R.id.btn_sales_reporte_submit);
		edt_scan_first = (EditText) findViewById(R.id.edt_scan_first);
		gv_thumbnails = (GridView) findViewById(R.id.gv_thumbnails);
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);

	}

	private void initOemView() {
		rl_sales_reporte_pro_type_select = (RelativeLayout) findViewById(R.id.rl_sales_reporte_pro_type_select);
		rl_sales_reporte_pro_model_selects = (RelativeLayout) findViewById(R.id.rl_sales_reporte_pro_model_selects);

		btn_scan_first = (Button) findViewById(R.id.btn_scan_first);
		btn_sales_reporte_submit = (Button) findViewById(R.id.btn_sales_reporte_submit);
		txt_sales_reporte_pro_type = (TextView) findViewById(R.id.txt_sales_reporte_pro_type);
		txt_sales_reporte_pro_model = (TextView) findViewById(R.id.txt_sales_reporte_pro_model);
		edt_scan_first = (EditText) findViewById(R.id.edt_scan_first);
		gv_thumbnails = (GridView) findViewById(R.id.gv_thumbnails);
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);

	}

	private boolean diyValidate() {
		if (InputChecker.isEmpty(edt_scan_first.getText().toString())) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.sales_reporte_scan_ask));
			return false;
		}
		if (m_lstPictures.size() <= 0) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.sales_reporte_take_pic_ask));
			return false;
		}
		return true;
	}

	private boolean oemValidate() {
		if (InputChecker.isEmpty(currentProductTypeId)) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.txt_select_product_type));
			return false;
		}
		if (InputChecker.isEmpty(currentProductModelId)) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.txt_select_product_model));
			return false;
		}
		if (InputChecker.isEmpty(edt_scan_first.getText().toString())) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.sales_reporte_scan_ask));
			return false;
		}
		if (m_lstPictures.size() <= 0) {
			ToastHelper.getInstance().showLongMsg(
					getString(R.string.sales_reporte_take_pic_ask));
			return false;
		}
		return true;
	}

	private void diyDataReset() {
		edt_scan_first.setText("");
		edt_scan_first.invalidate();
		m_lstPictures.clear();
		photoUploadThumbnailAdapter.resetPictureItems();
		photoUploadThumbnailAdapter.notifyDataSetChanged();
	}

	private void oemDataReset() {
		currentProductTypeId = null;
		txt_sales_reporte_pro_type.setText(R.string.txt_select_product_type);
		currentProductModelId = null;
		txt_sales_reporte_pro_model.setText(R.string.txt_select_product_model);
		edt_scan_first.setText("");
		m_lstPictures.clear();
		photoUploadThumbnailAdapter.resetPictureItems();
		photoUploadThumbnailAdapter.notifyDataSetChanged();
	}

	private void setDiyListener() {
		btn_scan_first.setOnClickListener(this);
		btn_sales_reporte_submit.setOnClickListener(this);
		
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
	}

	private void setOemListener() {
		// btn_sales_reporte_pro_type_select.setOnClickListener(this);
		// btn_sales_reporte_pro_model_select.setOnClickListener(this);
		btn_scan_first.setOnClickListener(this);
		rl_sales_reporte_pro_type_select.setOnClickListener(this);
		rl_sales_reporte_pro_model_selects.setOnClickListener(this);
		btn_sales_reporte_submit.setOnClickListener(this);
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

		loadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				storeController.getProductTypeFromeRemote(
						new ProductLoadUpdateView(
								StoreSalesReporteActivity.this), "all");
			}
		});
	}

	private void startCameraForResult() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			// sdcard/store/
			String mCategoryId = "salesReporte";
			mPictureAbsolutePath = PhotoUploadHelper.createImageFile(
					StoreSession.getCurrentStoreId(), mCategoryId);
			Loger.d("mPictureAbsolutePath:" + mPictureAbsolutePath);
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
		Intent intent = new Intent(StoreSalesReporteActivity.this,
				PhotoUploadPreviewActivity.class);
		Bundle data = new Bundle();
		data.putString("absolute_picture_path", pictureItem.mPictureFileName);
		data.putInt("position", position);
		intent.putExtras(data);
		startActivityForResult(intent, REQUEST_ID_TO_PREVIEW);
	}

	private void scanSampleImagePreviewPage(int resourceId) {
		Intent intent = new Intent();
		intent.putExtra("resourceId", resourceId);
		intent.setClass(StoreSalesReporteActivity.this, PhotoZoomActivity.class);
		// startActivity(intent);
	}

	private PictureItem createPictureItem(String absolutePicturePath,
			long currentMiliSec) {
		String slsprsId = StoreSession.getRepID();
		String storeId = StoreSession.getCurrentStoreId();
		String roleId = StoreSession.getCurrentStoreRole();
		return new PictureItem(slsprsId, storeId, ""
				+ System.currentTimeMillis(), "", StoreSession.getLongitude(),
				StoreSession.getLongitude(), roleId,// role_id
				"",// photo_cat
				"", // mdl_id
				"", // cityType
				"",// stor_addr
				absolutePicturePath,// pictureFileName
				"",// categoryName
				"", false, slsprsId);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				// 添加到图库,这样可以在手机的图库程序中看到程序拍摄的照片
				long currentMiliSec = System.currentTimeMillis();
				mCompressedPictureAbsolutePath = "";
				mCompressedPictureAbsolutePath = PhotoUploadHelper
						.preparePhoto(
								mPictureAbsolutePath,
								getString(R.string.main_txt_store_sales_reportor),
								currentMiliSec);

				PictureItem pictureItem = createPictureItem(
						mCompressedPictureAbsolutePath, currentMiliSec);
				if (pictureItem != null) {
					m_lstPictures.add(pictureItem);
					photoUploadThumbnailAdapter.setData(m_lstPictures);
					photoUploadThumbnailAdapter.notifyDataSetChanged();

				}
				// 删除已经创建的临时文件。
				PictureUtil.deleteTempFile(mPictureAbsolutePath);
			}
		} else if (requestCode == REQUEST_ID_TO_PREVIEW) {
			if (resultCode == PhotoUploadPreviewActivity.PREVIEW_RESULT_DELETED) {
				Bundle extras = data.getExtras();
				int position = extras.getInt("position");
				if (position != -1) {
					m_lstPictures.remove(position);
					photoUploadThumbnailAdapter.resetPictureItems();
					photoUploadThumbnailAdapter.notifyDataSetChanged();
				}
			}
		} else if (requestCode == SCAN) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle bundle = data.getExtras();
				// 添加扫码验证逻辑
				edt_scan_first.setText(bundle.getString("result"));
			} else {
				ToastHelper.getInstance().showShortMsg(
						getString(R.string.scan_error));
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.rl_sales_reporte_pro_type_select:
			Dialog alertDialog = new AlertDialog.Builder(this)
					.setTitle("选择品牌")
					.setSingleChoiceItems(mProductTypeNameList, 0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setCurrentProductType(which);
									txt_sales_reporte_pro_type
											.setText(mProductTypeNameList[which]);
									dialog.dismiss();
								}
							}).create();
			alertDialog.show();
			break;
		case R.id.rl_sales_reporte_pro_model_selects:
			Dialog alertDialog1 = new AlertDialog.Builder(this)
					.setTitle("选择型号")
					.setSingleChoiceItems(mProductModelNameList, 0,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									setCurrentProductModel(which);
									txt_sales_reporte_pro_model
											.setText(mProductModelNameList[which]);
									dialog.dismiss();
								}
							}).create();
			alertDialog1.show();
			break;
		case R.id.btn_scan_first:
			// 打开扫描界面扫描条形码或二维码
			Intent intentScan = new Intent(StoreSalesReporteActivity.this,
					CaptureActivity.class);
			startActivityForResult(intentScan, SCAN);
			break;
		case R.id.btn_sales_reporte_submit:
			if (storeTypeId == StoreListModel.DIY) {
				if (!diyValidate()) {
					return;
				} else {
					// 先进行条码验证
					String barcode = edt_scan_first.getText().toString();
					storeController.validateBarcode(
							new ValidateBarcodeUpdateView(
									StoreSalesReporteActivity.this), barcode);
				}
			} else {
				if (!oemValidate()) {
					return;
				} else {
					addToLoclDB();
				}
			}

			break;
		case R.id.common_id_btn_back:
			finish();
			break;
		default:
			break;
		}

	}

	/** 图片上传成功前进行数据保存,保存到本地数据库 */
	private void addToLoclDB() {
		Loger.d("addToLoclDB()");
		File file = new File(m_lstPictures.get(0).mPictureFileName);
		CacheDir cacheDir = new CacheDir();
		File saveFileDir = cacheDir.getLocalSavePictureDirCache();
		File saveFile = new File(saveFileDir, file.getName());
		if (file.renameTo(saveFile)) {
			upPhotoName = saveFile.getAbsolutePath();
			file.delete();
		} else {
			Loger.e("图片保存失败");
		}
		String barcode = edt_scan_first.getText().toString();
		LocalSaleReportDao dao = new LocalSaleReportDao();
		ArrayList<MapEntity> saleRecords = new ArrayList<MapEntity>();
		MapEntity saleRecord = new MapEntity();
		saleRecord.setValue(SaleReportRecord.USER_NAME, StoreSession.getName());
		saleRecord.setValue(SaleReportRecord.STORE_ID,
				StoreSession.getCurrentStoreId());
		Loger.d(StoreSession.getCurrentStoreNAME());
		saleRecord.setValue(SaleReportRecord.STORE_NAME,
				StoreSession.getCurrentStoreNAME());
		saleRecord.setValue(SaleReportRecord.SERIAL_NUMBER, barcode);
		saleRecord.setValue(SaleReportRecord.PIC_PATH, upPhotoName);
		saleRecord.setValue(SaleReportRecord.DATA_TIME,
				((Long) new Date().getTime()).toString());

		if (storeTypeId == StoreListModel.DIY) {

		} else {
			saleRecord.setValue(SaleReportRecord.PRO_BRAND_ID,
					currentProductTypeId);
			saleRecord.setValue(SaleReportRecord.PRO_MODEL_ID,
					currentProductModelId);
			saleRecord.setValue(SaleReportRecord.PRO_BRAND_NAME,
					txt_sales_reporte_pro_type.getText().toString());
			saleRecord.setValue(SaleReportRecord.PRO_MODEL_NAME,
					txt_sales_reporte_pro_model.getText().toString());

		}
		saleRecord.setValue(SaleReportRecord.STORE_TYPE, "");
		saleRecords.add(saleRecord);
		try {
			dao.insertSaleReportRecord(saleRecords);
			Loger.d("insertSaleReportRecord");
			mViewHelper
					.showBTNDialog(getString(R.string.sales_reporte_save_success));
			if (storeTypeId == StoreListModel.DIY) {
				diyDataReset();
			} else {
				oemDataReset();
			}
		} catch (DBException e) {
			e.printStackTrace();
			Loger.e(e.toString());
			mViewHelper.showErrorDialog(e);
		}
		m_lstPictures.clear();
	}

	protected void setCurrentProductType(int position) {
		MapEntity entity = mProductTypeDataList.get(position);
		currentProductTypeId = entity.getString(ProductTypeModel.BRND_ID);

		txt_sales_reporte_pro_type.setText(entity
				.getString(ProductTypeModel.BRND_NM));
		txt_sales_reporte_pro_model
				.setText(getString(R.string.txt_select_product_model));
		currentProductModelId = null;
		// 修改型号
		ProductTypeModel productTypeModel = new ProductTypeModel();
		List<MapEntity> data = productTypeModel.getModleByBrndId(entity
				.getString(ProductTypeModel.BRND_ID));
		mProductModelDataList.clear();
		mProductModelDataList.addAll(data);
		
		mProductModelNameList = new String[mProductModelDataList.size()];
		for (int i = 0; i < mProductModelDataList.size(); i++) {
			String name = mProductModelDataList.get(i).getString(ProductTypeModel.MDL_NM);
			String names[] = name.split("-");
			String shortName = names[names.length - 1];
			if (shortName.startsWith("[")) {
				shortName = shortName.substring(1);
			}
			if (shortName.endsWith("]")) {
				shortName = shortName.substring(0, shortName.length() - 1);
			}
			mProductModelNameList[i] =shortName;
		}
	}

	protected void setCurrentProductModel(int position) {
		MapEntity entity = mProductModelDataList.get(position);

		String name = entity.getString(ProductTypeModel.MDL_NM);
		String names[] = name.split("-");
		String shortName = names[names.length - 1];
		if (shortName.startsWith("[")) {
			shortName = shortName.substring(1);
		}
		if (shortName.endsWith("]")) {
			shortName = shortName.substring(0, shortName.length() - 1);
		}

		txt_sales_reporte_pro_model.setText(shortName);
		currentProductModelId = entity.getString(ProductTypeModel.MDL_ID);

	}

	class ViewHolder {
		public TextView name;
	}

	private class ProductListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mProductTypeDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return mProductTypeDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapEntity mapEntity = mProductTypeDataList.get(position);
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(
						StoreSalesReporteActivity.this).inflate(
						R.layout.store_function_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_store_function_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String name = mapEntity.getString(ProductTypeModel.BRND_NM);
			viewHolder.name.setText(name);
			return convertView;
		}

	}

	private class ProductModelListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mProductModelDataList.size();
		}

		@Override
		public Object getItem(int position) {
			return mProductModelDataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapEntity mapEntity = mProductModelDataList.get(position);
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(
						StoreSalesReporteActivity.this).inflate(
						R.layout.store_function_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_store_function_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String name = mapEntity.getString(ProductTypeModel.MDL_NM);
			String names[] = name.split("-");
			String shortName = names[names.length - 1];
			if (shortName.startsWith("[")) {
				shortName = shortName.substring(1);
			}
			if (shortName.endsWith("]")) {
				shortName = shortName.substring(0, shortName.length() - 1);
			}
			viewHolder.name.setText(shortName);
			return convertView;
		}

	}

	private class ValidateBarcodeUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {
		public ValidateBarcodeUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<MapEntity> result) {
			loadingView.hideLoading();
			String model = getResources().getString(
					R.string.sales_reporte_result_content);
			if (result.size() > 0)
				model = result.get(0).getString(SalesReportModel.EXTRNL_PRD_NM);

			mViewHelper
					.showBTN2Dialog(
							getResources().getString(
									R.string.sales_reporte_result_title),
							model,
							getResources().getString(
									R.string.sales_reporte_go_reporte),
							getResources().getString(R.string.btn_cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									addToLoclDB();

									// upSalesReporte();
								}
							}, new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}, new DialogInterface.OnCancelListener() {

								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();
								}
							});
		}

		@Override
		public void handleException(IException ex) {
			Loger.d(ex.getMessage());
			loadingView.hideLoading();
			viewHelper.showBTNDialog(ex.getMessage());
		}
	}

	private class ProductLoadUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {
		public ProductLoadUpdateView(Context context) {
			super(context);

		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<MapEntity> data) {
			// storeController.deleteProductType();
			storeController.saveProductType(data);
			// 下载完成后，相当于本地存在
			DataLoaded();
			loadingView.hideLoading();
		}

		@SuppressWarnings("deprecation")
		@Override
		public void handleException(IException ex) {
			ex.printStackTrace();
			loadingView.errorLoaded(ex.getMessage()
					+ ","
					+ getResources()
							.getString(R.string.comm_click_screen_retry));
		}
	}

	private class PhotoUploadThumbnailAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<PictureItem> mPictureItems;

		public PhotoUploadThumbnailAdapter(Context context) {
			this.mContext = context;
			resetPictureItems();
		}

		public void resetPictureItems() {
			if (mPictureItems == null) {
				mPictureItems = new ArrayList<PictureItem>();
			}
			mPictureItems.clear();
			mPictureItems.add(PictureItem.pictureItemPlusSign);
		}

		public void setData(ArrayList<PictureItem> lstData) {
			mPictureItems.clear();
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
				holder.txt_thumbnail_category = (TextView) convertView
						.findViewById(R.id.txt_thumbnail_category);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			PictureItem pictureItem = (PictureItem) getItem(position);
			if (pictureItem.mIsPlusBtn) {
				holder.ibtn_thumbnail
						.setBackgroundResource(R.drawable.photo_upload_add_bg);
				holder.txt_thumbnail_category.setText("");
			} else {
				Loger.d("---------getView");
				String fileName = pictureItem.mPictureFileName;
				Bitmap bitmap = BitmapFactory.decodeFile(fileName);
				BitmapDrawable bitDrawable = new BitmapDrawable(bitmap);
				holder.ibtn_thumbnail.setBackgroundDrawable(bitDrawable);

				String strCategory = pictureItem.mCategoryName;
				holder.txt_thumbnail_category.setText(strCategory);
			}
			return convertView;
		}

		private class ViewHolder {
			public ImageButton ibtn_thumbnail;
			public TextView txt_thumbnail_category;
		}
	}

	@Override
	protected void onDestroy() {
		// 删除不上传的拍照缓存
		super.onDestroy();
		if (m_lstPictures == null || m_lstPictures.size() == 0) {
			return;
		}
		if (m_lstPictures.get(0) != null) {
			Uri uri = Uri.parse(m_lstPictures.get(0).mPictureFileName);
			Loger.d("delete: " + uri.toString());
			PictureUtil.deleteTempFile(uri.toString());
		}
	}

}
