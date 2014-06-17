package com.intel.store.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.model.SalesReportModel;
import com.intel.store.util.ImageInfoWarp;
import com.intel.store.util.StoreSession;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.ToastHelper;

public class SaleReportDetailUploadedActivity extends BaseActivity implements
		IImageLoadCallback {
	private Button btn_back;
	private TextView reportStore, reporter, reportDate, reportBarcode,
			reportBoard, reportType;
	private ImageView iv_paper;
	private LinearLayout ll_paper, ll_oem_see;
	private Bitmap picBitmap;
	private MapEntity entity;
	private String picPath;
	private ImageInfoWarp paperImageInfo;
	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale_report_uploaded_detail);
		initView();
		setListener();
	}

	private void initView() {
		btn_back = (Button) findViewById(R.id.common_id_btn_back);
		reportBoard = (TextView) findViewById(R.id.txt_report_broad);
		reportType = (TextView) findViewById(R.id.txt_report_type);
		reportStore = (TextView) findViewById(R.id.txt_report_store_name);
		reporter = (TextView) findViewById(R.id.txt_reporter_name);
		reportDate = (TextView) findViewById(R.id.txt_report_time);
		reportBarcode = (TextView) findViewById(R.id.txt_report_barcode);
		ll_paper = (LinearLayout) findViewById(R.id.ll_paper);
		iv_paper = (ImageView) findViewById(R.id.iv_paper);
		ll_oem_see = (LinearLayout) findViewById(R.id.ll_oem_see);
		ll_paper = (LinearLayout) findViewById(R.id.ll_paper);

		try {
			Bundle bundle = getIntent().getExtras();
			entity = (MapEntity) bundle.getSerializable("data");
			reportStore.setText(entity
					.getString(SalesReportModel.RECORED_STOR_NM));
			reporter.setText(entity.getString(SalesReportModel.RECORED_REP_NM));
			reportDate.setText(entity
					.getString(SalesReportModel.RECORED_LAST_UPD_DTM));
			reportBarcode.setText(entity
					.getString(SalesReportModel.RECORED_BARCODE));
			// oem门店才有显示 品牌型号
			if (StoreSession.getCurrentStoreRole().equals(StoreSession.OEM)) {
				ll_oem_see.setVisibility(View.VISIBLE);
				try {
					String brandNm = entity
							.getString(SalesReportModel.RECORED_BRND_NM);
					String MODEL_NAME = entity
							.getString(SalesReportModel.RECORED_MDL_NM);
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
			picPath = entity.getString(SalesReportModel.RECORED_PIC_LOC);
			loadPic(picPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadPic(String picPath2) {
		if (picPath != null && picPath.length() > 1) {

			if (picPath != null) {
				Loger.d("picPath:" + picPath);
				try {
					paperImageInfo = new ImageInfoWarp(picPath, true, true,
							480);
					paperImageInfo.setImageView(iv_paper);
					iv_paper.setPadding(5, 5, 5, 5);
					iv_paper.setImageResource(R.anim.loading);
					animationDrawable = (AnimationDrawable) iv_paper.getDrawable();
					iv_paper.post(new Runnable() {
						@Override
						public void run() {
							animationDrawable.start();
						}
					});
					
					StoreApplication.asyncImageLoader
							.setImageLoadCallback(SaleReportDetailUploadedActivity.this);
					StoreApplication.asyncImageLoader
							.loadImage(paperImageInfo);

				} catch (Exception e) {
					picBitmap = BitmapFactory.decodeResource(
							getResources(), R.drawable.default_img);
					iv_paper.setImageBitmap(picBitmap);
					e.printStackTrace();
				}
				ll_paper.setVisibility(View.VISIBLE);
			}
		}		
	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	@Override
	public void fail(ImageInfo arg0) {
		picBitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.default_img);
		iv_paper.setImageBitmap(picBitmap);
		ToastHelper.getInstance().showShortMsg(getString(R.string.txt_pic_fail_retry));
		iv_paper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadPic(picPath);
			}
		});
	}

	@Override
	public void success(ImageInfo imageInfo) {
		((ImageInfoWarp) imageInfo).getImageView().setImageBitmap(imageInfo.getBitmap());
		animationDrawable.stop();
		iv_paper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (picPath!=null) {
					Intent intent = new Intent();
					intent.putExtra("photo_url",picPath);
					intent.putExtra("unupload",false);
					intent.setClass(SaleReportDetailUploadedActivity.this,
							PhotoZoomActivity.class);
					startActivity(intent);
				}
			}
		});
	}

}
