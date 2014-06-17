package com.intel.store.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.model.StoreIntegralModel;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;

public class StoreIntegralActivity extends BaseActivity {
	
	StoreIntegralUpdateView storeSalesCountUpdateView;
	private LoadingView loadingView;
	private StoreController storeController;

	private LinearLayout empty_layout;
	private String rspId = "";

	private StoreSaleListAdapter adapter;
	private ListView listView;
	private List<MapEntity> data = new ArrayList<MapEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_integral);
		initView();
		setListener();
		storeSalesCountUpdateView = new StoreIntegralUpdateView(
				StoreIntegralActivity.this);
		storeController = new StoreController();
		adapter = new StoreSaleListAdapter();
		listView.setAdapter(adapter);

		rspId= StoreSession.getRepID();
		storeController
				.getAllStoreIntegral(storeSalesCountUpdateView, rspId);

	}

	private void setListener() {
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		// storeFunctionNames = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.common_id_lv);
		 empty_layout = (LinearLayout)
		 findViewById(R.id.common_id_empty_layout);
	}

	class ViewHolder {
		public TextView store_name;
		public TextView integral_all;
		public TextView integral_bonus;
		public TextView integral_train;
		public TextView integral_lever;
	}

	private class StoreSaleListAdapter extends BaseAdapter {
		// private AsyncImageLoader asyncImageLoader = null;

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(StoreIntegralActivity.this)
						.inflate(R.layout.list_item_store_integral, null);
				viewHolder = new ViewHolder();
				viewHolder.store_name = (TextView) convertView
						.findViewById(R.id.txt_store_name);
				viewHolder.integral_all = (TextView) convertView
						.findViewById(R.id.txt_store_integral_all);
				viewHolder.integral_train = (TextView) convertView
						.findViewById(R.id.txt_store_integral_train);
				viewHolder.integral_bonus = (TextView) convertView
						.findViewById(R.id.txt_store_integral_bonus);
				viewHolder.integral_lever = (TextView) convertView
						.findViewById(R.id.txt_store_integral_lever);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.store_name.setText(data.get(position).getString(StoreIntegralModel.stor_nm));
			viewHolder.integral_all.setText(data.get(position).getString(StoreIntegralModel.integral_all));
			viewHolder.integral_train.setText(data.get(position).getString(StoreIntegralModel.train));
			viewHolder.integral_bonus.setText(data.get(position).getString(StoreIntegralModel.bonus));
			viewHolder.integral_lever.setText(data.get(position).getString(StoreIntegralModel.lever));
			return convertView;
		}

	}

	private class StoreIntegralUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {

		public StoreIntegralUpdateView(Context context) {
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
			if(result.size()>0){
				data.clear();
				data.addAll(result);
				adapter.notifyDataSetChanged();
			}else{
				empty_layout.setVisibility(View.VISIBLE);
			}

			
		}

		@Override
		public void handleException(IException ex) {
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					storeController
					.getAllStoreIntegral(storeSalesCountUpdateView, rspId);
}
			};
			DialogInterface.OnClickListener onCancelListener =new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreIntegralActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);
			
		}

	}
}
