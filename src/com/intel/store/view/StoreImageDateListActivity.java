package com.intel.store.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicDateModel;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PhoneStateUtil;

public class StoreImageDateListActivity extends BaseActivity {
	private static final int REQUEST_TO_IMAGE_SHOW = 1;

	protected static final int ADD_PHOTO = 1;
	private LoadingView loadingView;
	private ListView lstView_store_image_date;
	private Button btn_back;
	private Button btn_photo_add;
	private LinearLayout layout_empty;
	private TextView emptyTextView;
	private StorePhotoController mQueryPicDateController;
	private ArrayList<MapEntity> dateList;
	private ImageDateAdapter mImageDateAdapter;
	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_date_list_layout);
		initPassedValues();
		initView();
		setListener();
	}

	@Override
	protected void onRestart() {
		Loger.d("onRestart");
		super.onRestart();
		initView();
	}

	private void initPassedValues() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		storeId = bundle.getString("store_id");
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		// layout_progress = (LinearLayout) findViewById(R.id.layout_progress);
		layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
		emptyTextView = (TextView) layout_empty.findViewById(R.id.txt_empty);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_photo_add = (Button) findViewById(R.id.btn_photo_add);
		lstView_store_image_date = (ListView) findViewById(R.id.common_id_lv);
		mImageDateAdapter = new ImageDateAdapter(this);
		lstView_store_image_date.setAdapter(mImageDateAdapter);

		queryPicDate();
	}

	private void queryPicDate() {
		dateList = new ArrayList<MapEntity>();
		if (mQueryPicDateController == null) {
			mQueryPicDateController = new StorePhotoController();
		}
		String params[] = new String[2];
		String slsprsId = StoreSession.getRepID();
		params[0] = slsprsId;
		params[1] = storeId;
		Loger.d("storeId------>" + storeId);
		mQueryPicDateController.queryPicDate(new QueryPicDateViewCallback(
				StoreImageDateListActivity.this), params);
	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_photo_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_photo_taken = new Intent();
				intent_photo_taken.setClass(StoreImageDateListActivity.this,
						StoreImageTypeListActivity.class);
				/*
				 * intent_photo_taken.setClass(StoreImageDateListActivity.this,
				 * PhotoUploadDetailActivity.class);
				 */
				startActivityForResult(intent_photo_taken, ADD_PHOTO);

			}
		});
		lstView_store_image_date
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(
								StoreImageDateListActivity.this,
								StoreImageShowActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("last_upd_dtm", dateList.get(position)
								.getString(QueryPicDateModel.LAST_UPD_DTM));
						bundle.putString("store_id", storeId);
						intent.putExtras(bundle);
						startActivityForResult(intent, REQUEST_TO_IMAGE_SHOW);
					}
				});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_PHOTO) {
			Loger.d("完成增加照片上传");
		}

		if (resultCode == StoreImageShowActivity.RESULT_OK) {
			if (PhoneStateUtil.hasInternet()) {
				Bundle bundle = new Bundle();
				bundle = data.getExtras();
				String params[] = new String[3];
				params[0] = StoreSession.getRepID();
				params[1] = bundle.getString("store_id");
				Loger.d("onActivityResult======");
				if (PhoneStateUtil.hasInternet()) {
					mQueryPicDateController.queryPicDate(
							new QueryPicDateViewCallback(
									StoreImageDateListActivity.this), params);
				}
			}
		}
	}

	private class QueryPicDateViewCallback extends
			StoreCommonUpdateView<ArrayList<MapEntity>> {

		public QueryPicDateViewCallback(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingView.hideLoading();
		}

		@Override
		public void onException(IException ie) {
			super.onException(ie);
		}

		@Override
		public void handleException(IException ex) {
			super.handleException(ex);
			emptyTextView.setText("");
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					queryPicDate();
				}
			};
			DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreImageDateListActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);
		}

		@Override
		public void onPostExecute(ArrayList<MapEntity> result) {
			if (result != null && result.size() != 0) {
				dateList = result;
				mImageDateAdapter.setData(result);
				mImageDateAdapter.notifyDataSetChanged();
				layout_empty.setVisibility(View.INVISIBLE);
			} else {
				emptyTextView.setText("服务器没有图片数据");
				layout_empty.setVisibility(View.VISIBLE);
			}
			loadingView.hideLoading();
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
		}

	}

	public class ImageDateAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<MapEntity> mData;

		public ImageDateAdapter(Context context) {
			this.mContext = context;
		}

		public void setData(ArrayList<MapEntity> data) {
			this.mData = data;
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData == null ? 0 : mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_store_image_date, null);
				viewHolder = new ViewHolder();
				viewHolder.txt_image_upload_time = (TextView) convertView
						.findViewById(R.id.txt_image_date);
				viewHolder.txt_image_count = (TextView) convertView
						.findViewById(R.id.txt_image_count);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			MapEntity me = (MapEntity) getItem(position);
			if (me != null) {
				String date = me.getString(QueryPicDateModel.LAST_UPD_DTM);
				String params[] = new String[3];
				params[0] = date.substring(0, 4);
				params[1] = date.substring(4, 6);
				params[2] = date.substring(6, 8);
				viewHolder.txt_image_upload_time.setText(params[0] + "年"
						+ params[1] + "月" + params[2] + "日");
				viewHolder.txt_image_count.setText("共上传 "
						+ me.getString(QueryPicDateModel.PIC_COUNT) + " 张图片");
			}
			return convertView;
		}

		class ViewHolder {
			TextView txt_image_upload_time;
			TextView txt_image_count;
		}

	}

}
