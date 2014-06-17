package com.intel.store.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.ToastHelper;

public class StoreSelectActivity extends BaseActivity {
	private List<MapEntity> storeList;
	private StoreListAdapter adapter;
	private ListView listView;
	private StoreController storeController;
	private StoreListUpdateView storeListUpdateView;
	private LoadingView loadingView;
	String sr_id;
	Class<?> whichActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_select);
		initView();
		setListener();
		whichActivity = (Class<?>) getIntent().getExtras().getSerializable(
				"whichActivity");

		storeController = new StoreController();
		storeList=new ArrayList<MapEntity>();
		storeListUpdateView = new StoreListUpdateView(this);
		sr_id = StoreSession.getRepID();
		storeController.getStoreByRepId(storeListUpdateView, sr_id);
		adapter = new StoreListAdapter();
		listView.setAdapter(adapter);

	}

	void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		listView = (ListView) findViewById(R.id.common_id_lv);
	}

	void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(StoreSelectActivity.this, whichActivity);
				String storeId =storeList.get(position).getString(StoreListModel.STOR_ID);
				String storeName =storeList.get(position).getString(StoreListModel.STOR_NM);
				String storeRole=storeList.get(position).getString(StoreListModel.ROLE_ID);
				String storeCityTypeName=storeList.get(position).getString(StoreListModel.CITY_TYPE_NM);
				StoreSession.setCurrentCityTypeName(storeCityTypeName);
				StoreSession.setCurrentStoreId(storeId);
				StoreSession.setCurrentStoreRole(storeRole);
				StoreSession.setCurrentStoreNAME(storeName);
				intent.putExtra("storeInfo", (Serializable)storeList.get(position));
				startActivity(intent);
			}
		});
		loadingView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingView.showLoading();
				storeController.getStoreByRepId(storeListUpdateView, sr_id);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.store_select, menu);
		return true;
	}

	class ViewHolder {
		public TextView storeName;
		public TextView storeaddress;
	}

	private class StoreListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return storeList.size();
		}

		@Override
		public Object getItem(int position) {
			return storeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MapEntity mapEntity = storeList.get(position);
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_store_list, null);
				viewHolder = new ViewHolder();
				viewHolder.storeName = (TextView) convertView
						.findViewById(R.id.txt_store_name);
				viewHolder.storeaddress = (TextView) convertView
						.findViewById(R.id.txt_store_address);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			String name = mapEntity.getString(StoreListModel.STOR_NM);
			String address = mapEntity.getString(StoreListModel.STOR_ADDR);
			viewHolder.storeName.setText(name);
			viewHolder.storeaddress.setText("地址："+address);
			return convertView;
		}

	}

	private class StoreListUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {

		public StoreListUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<MapEntity> data) {
			storeList = data;
			adapter.notifyDataSetChanged();
			loadingView.hideLoading();
		}

		@SuppressWarnings("deprecation")
		@Override
		public void handleException(IException ex) {
			loadingView.errorLoaded(ex.getMessage()
					+ ","
					+ getResources()
							.getString(R.string.comm_click_screen_retry));
		}

	}
}
