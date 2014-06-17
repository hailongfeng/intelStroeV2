package com.intel.store.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.setting.StoreSettingActivity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PictureFileHelper;
import com.pactera.framework.util.ToastHelper;

/**
 * @author P0033759 最后修改时间 2014-2-20-上午9:13:48 功能 浏览图片、删除图片
 */
public class PhotoUploadPreviewActivity extends BaseActivity {

	public static final int PREVIEW_RESULT_DELETED = 101;
	private Button btn_title_back;
	private TextView txt_title_msg;
	private Button btn_title_delete;
	private ImageView imgBigPicture;
	private String m_strPicturePath;
	private int mPosition = -1;
	private Bitmap bm = null;
	private ViewHelper viewHelper;
	private Bundle data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_upload_preview);
		initPassedValues();
		initView();
		setListener();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setIconEnable(menu, true);
		getMenuInflater().inflate(R.menu.upload_preview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_del:
			PictureFileHelper.DeleteFile(m_strPicturePath);
			Intent intent = new Intent();
			Bundle data = new Bundle();
			data.putInt("position", mPosition);
			intent.putExtras(data);
			setResult(PREVIEW_RESULT_DELETED, intent);
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	private void initPassedValues() {
		Intent intent = getIntent();
		data = intent.getExtras();
		m_strPicturePath = data.getString("absolute_picture_path");
		mPosition = data.getInt("position");
	}

	private void initView() {
		// set title
		btn_title_back = (Button) findViewById(R.id.ibtn_title_back);
		txt_title_msg = (TextView) findViewById(R.id.txt_title_msg);
		btn_title_delete = (Button) findViewById(R.id.btn_title_send);
		imgBigPicture = (ImageView) findViewById(R.id.imgBigPicture);

		txt_title_msg.setText(this.getString(R.string.txt_take_picture));
		bm = StoreApplication.bitmapCache.getBitmap(m_strPicturePath);
		if (bm==null) {
			Loger.d("unsual");
			bm=BitmapFactory.decodeFile(m_strPicturePath);
			StoreApplication.bitmapCache.addBitmap(m_strPicturePath, bm);
		}
		imgBigPicture.setImageBitmap(bm);
		viewHelper = new ViewHelper(this);
	}

	private void setListener() {
		btn_title_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_OK, new Intent());
				finish();
			}
		});

		btn_title_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						PictureFileHelper.DeleteFile(m_strPicturePath);
						Intent intent = new Intent();
						Bundle data = new Bundle();
						data.putInt("position", mPosition);
						intent.putExtras(data);
						setResult(PREVIEW_RESULT_DELETED, intent);
						finish();
					}
				};
				viewHelper.showBTN2Dialog(getString(R.string.personal_txt_title), getString(R.string.photo_delete_ask), getString(R.string.txt_yes), getString(R.string.txt_no),
						positiveListener, null, null);
			}
		});

		imgBigPicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("unupload", true);
				intent.putExtra("absolute_picture_path",m_strPicturePath);
				intent.setClass(PhotoUploadPreviewActivity.this,
						PhotoZoomActivity.class);
				startActivity(intent);
			}
		});
	}
}