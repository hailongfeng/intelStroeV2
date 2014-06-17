package com.intel.store.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.intel.store.model.QueryPicRoundModel;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.InputChecker;

public class StoreImageRecentActivity extends BaseActivity {
	private ListView list_rounds; // 图片上传轮次列表
	private List<MapEntity> dataList;
	private Button btn_back;

	private LoadingView loadingView;
	private LinearLayout empty_layout;
	private RoundsListAdapter adapter;
	private StorePhotoController photoController;
	private StorePicRoundsUpdateView storePicRoundsUpdateView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_rounds);
		initView();
		setListener();
		photoController = new StorePhotoController();
		dataList = new ArrayList<MapEntity>();
		adapter = new RoundsListAdapter(StoreImageRecentActivity.this);
		list_rounds.setAdapter(adapter);
		storePicRoundsUpdateView = new StorePicRoundsUpdateView(
				StoreImageRecentActivity.this);
		photoController.queryHistoryWv(storePicRoundsUpdateView);
	}

	public void initView() {
		list_rounds = (ListView) findViewById(R.id.common_id_lv);
		btn_back = (Button) findViewById(R.id.common_id_btn_back);
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		empty_layout = (LinearLayout) findViewById(R.id.common_id_empty_layout);

	}

	public void setListener() {

		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		loadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photoController.queryHistoryWv(storePicRoundsUpdateView);
			}
		});
		list_rounds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent StoreImageDateListIntent = new Intent(
						StoreImageRecentActivity.this,
						StoreImageRoundDetailActivity.class);
				StoreImageDateListIntent.putExtra(
						"wv_id",
						dataList.get(position).getString(
								QueryPicRoundModel.WV_ID));
				StoreImageDateListIntent.putExtra(
						"title",
						dataList.get(position).getString(
								QueryPicRoundModel.WV_NM));
				StoreImageDateListIntent.putExtra("store_id",
						StoreSession.getCurrentStoreId());
				startActivity(StoreImageDateListIntent);
			}
		});

	}

	private class RoundsListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public RoundsListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			if (dataList != null) {
				return dataList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			if (dataList != null) {
				return dataList.get(arg0);
			}
			return null;
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
						R.layout.list_item_photo_upload_rounds, null);
				hold.tv_head = (TextView) convertView
						.findViewById(R.id.txt_head);
			} else {
				hold = (ViewHold) convertView.getTag();
			}
			MapEntity map = dataList.get(position);
			String name = map.getString(QueryPicRoundModel.WV_NM);
			hold.tv_head.setText(name);
			return convertView;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initView();
	}

	class ViewHold {
		public TextView tv_head;
	}

	private class StorePicRoundsUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {

		public StorePicRoundsUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<MapEntity> data) {
			loadingView.hideLoading();
			if (data.size() > 0) {
				String currentYyyyQq = "";
				String wv_nm = "";
				String yyyy = "";
				String jd = "";
				int i = 1;
				for (MapEntity mapEntity : data) {
					String tempyyyyqq = mapEntity
							.getString(QueryPicRoundModel.YYYYQQ);
					if (!InputChecker.isEmpty(tempyyyyqq)
							&& tempyyyyqq.length() == 6) {
						yyyy = tempyyyyqq.substring(0, 4);
						jd = tempyyyyqq.substring(5, 6);

						if (currentYyyyQq.equals(tempyyyyqq)) {
							i++;
							wv_nm =getString(R.string.txt_pic_upload_round,yyyy,jd,i);
							mapEntity.setValue(QueryPicRoundModel.WV_NM, wv_nm);
						} else {
							i = 1;
							wv_nm =getString(R.string.txt_pic_upload_round,yyyy,jd,i);
							mapEntity.setValue(QueryPicRoundModel.WV_NM, wv_nm);
							currentYyyyQq = tempyyyyqq;
						}
					} else {
						mapEntity.setValue(QueryPicRoundModel.WV_NM, getString(R.string.txt_pic_upload_wv_nm_error));					}
				}
				dataList = data;
				adapter.notifyDataSetChanged();
			} else {
				empty_layout.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public void handleException(IException ex) {
			loadingView.errorLoaded(ex.getMessage());
		}

	}

}
