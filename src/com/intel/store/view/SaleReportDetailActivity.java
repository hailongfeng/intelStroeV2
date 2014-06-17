package com.intel.store.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.intel.store.dao.local.LocalSaleReportDao;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.imgload.BitmapCache;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class SaleReportDetailActivity extends BaseActivity {
	// private Button btn_back, btn_delete;
	private TextView reportStore, reporter, reportDate, reportBarcode,
			reportBoard, reportType;
	private ImageView iv_paper;
	private LinearLayout ll_paper, ll_oem_see;
	private Bitmap picBitmap;
	private MapEntity entity;
	private String picPath;
	private ViewHelper mViewHelper;
	public static final int ResultCodeDelSuccess = 48;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_report_detail);
		initView();
		setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.sale_report_datail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_delete:
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					LocalSaleReportDao dao = new LocalSaleReportDao();
					try {
						dao.deleteSaleReportRecord(entity.getString(0));
						if (picPath != null && picPath != "") {
							File file = new File(picPath);
							file.delete();
						}
					} catch (DBException e) {
						e.printStackTrace();
					}
					setResult(ResultCodeDelSuccess, getIntent());
					finish();
				}
			};
			mViewHelper.showBTN2Dialog(getString(R.string.personal_txt_title),
					getString(R.string.photo_delete_ask),
					getString(R.string.btn_change),
					getString(R.string.btn_cancel), positiveListener, null,
					null);

			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		mViewHelper = new ViewHelper(this);
		ll_oem_see = (LinearLayout) findViewById(R.id.ll_oem_see);
		// btn_back = (Button) findViewById(R.id.common_id_btn_back);
		// btn_delete = (Button) findViewById(R.id.btn_delete);
		reportStore = (TextView) findViewById(R.id.txt_report_store_name);
		reporter = (TextView) findViewById(R.id.txt_reporter_name);
		reportDate = (TextView) findViewById(R.id.txt_report_time);
		reportBarcode = (TextView) findViewById(R.id.txt_report_barcode);
		reportType = (TextView) findViewById(R.id.txt_report_type);
		reportBoard = (TextView) findViewById(R.id.txt_report_broad);
		ll_paper = (LinearLayout) findViewById(R.id.ll_paper);
		iv_paper = (ImageView) findViewById(R.id.iv_paper);
		try {
			Bundle bundle = getIntent().getExtras();
			entity = (MapEntity) bundle.getSerializable("data");
			Date date1 = new Date(Long.valueOf(entity
					.getString(SaleReportRecord.DATA_TIME)));
			String pattern = "yyyy-MM-dd";
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			String date2 = dateFormat.format(date1);
			// XXX
			reportStore.setText(entity.getString(SaleReportRecord.STORE_NAME));
			reporter.setText(entity.getString(SaleReportRecord.USER_NAME));
			reportDate.setText(date2);
			reportBarcode.setText(entity
					.getString(SaleReportRecord.SERIAL_NUMBER));
			picPath = entity.getString(SaleReportRecord.PIC_PATH);
			// oem门店才有显示 品牌型号
			if (StoreSession.getCurrentStoreRole().equals(StoreSession.OEM)) {
				ll_oem_see.setVisibility(View.VISIBLE);
				try {
					String brandNm = entity
							.getString(SaleReportRecord.PRO_BRAND_NAME);
					String MODEL_NAME = entity
							.getString(SaleReportRecord.PRO_MODEL_NAME);
					String names[] = MODEL_NAME.split("-");
					String modelNm = names[names.length - 1];
					if (modelNm.startsWith("[")) {
						modelNm = modelNm.substring(1);
					}
					if (modelNm.endsWith("]")) {
						modelNm = modelNm.substring(0, modelNm.length() - 1);
					}
					if (!TextUtils.isEmpty(brandNm)
							&& !TextUtils.isEmpty(modelNm)) {
						reportBoard.setText(brandNm);
						reportType.setText(modelNm);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (picPath != null && picPath.length() > 1) {
				if (StoreApplication.bitmapCache == null) {
					StoreApplication.bitmapCache = new BitmapCache(30);
				}

				if (picPath != null) {
					Loger.d("picPath:" + picPath);
					try {
						picBitmap = BitmapFactory.decodeFile(picPath);
						System.err.println(picBitmap);
						StoreApplication.bitmapCache.addBitmap(picPath,
								picBitmap);
						picBitmap = getBitmapFromSD2(picPath, 480);
						System.err.println(picBitmap);
					} catch (OutOfMemoryError e) {
						picBitmap = BitmapFactory.decodeResource(
								getResources(), R.drawable.default_img);
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					ll_paper.setVisibility(View.VISIBLE);
					iv_paper.setImageBitmap(picBitmap);
				}
				iv_paper.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						if (picPath != null) {
							intent.putExtra("absolute_picture_path", picPath);
							intent.putExtra("unupload", true);
						}
						intent.setClass(SaleReportDetailActivity.this,
								PhotoZoomActivity.class);
						startActivity(intent);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListener() {
		// btn_back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// finish();
		// }
		// });
		// btn_delete.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// DialogInterface.OnClickListener positiveListener = new
		// DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// LocalSaleReportDao dao = new LocalSaleReportDao();
		// try {
		// dao.deleteSaleReportRecord(entity.getString(0));
		// if (picPath != null && picPath != "") {
		// File file = new File(picPath);
		// file.delete();
		// }
		// } catch (DBException e) {
		// e.printStackTrace();
		// }
		// setResult(ResultCodeDelSuccess, getIntent());
		// finish();
		// }
		// };
		// mViewHelper
		// .showBTN2Dialog(getString(R.string.personal_txt_title),getString(R.string.photo_delete_ask),
		// getString(R.string.btn_change),getString(R.string.btn_cancel),
		// positiveListener, null,null);
		// }
		// });
	}

	private Bitmap getBitmapFromSD2(String filePath, int maxSize)
			throws OutOfMemoryError {
		String sdcardState = Environment.getExternalStorageState();
		if (sdcardState.equals(Environment.MEDIA_MOUNTED)
				&& !sdcardState.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			try {
				File file = new File(filePath);
				if (file.exists()) {
					InputStream is = new FileInputStream(file);
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					Bitmap bm = BitmapFactory.decodeStream(is, null, o);
					if (bm != null) {
						bm.recycle();
						bm = null;
					}
					is.close();

					int sampleSize = 1;
					if (o.outHeight > maxSize || o.outWidth > maxSize) {
						sampleSize = (int) Math.pow(
								2,
								(int) Math.round(Math.log(maxSize
										/ (double) Math.max(o.outHeight,
												o.outWidth))
										/ Math.log(0.5)));
					}
					Loger.d("getBitmapFromSD sampleSize = " + sampleSize);

					is = new FileInputStream(file);
					BitmapFactory.Options opts = new BitmapFactory.Options();
					// opts.inTempStorage = new byte[16 * 1024];
					opts.inSampleSize = sampleSize;
					Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
					is.close();
					return bitmap;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
