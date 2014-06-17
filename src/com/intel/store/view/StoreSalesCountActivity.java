package com.intel.store.view;

import java.text.NumberFormat;
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
import com.intel.store.model.SalesCountModel;
import com.intel.store.model.SalesCountModel.SaleQuarter;
import com.intel.store.model.SalesCountModel.StoreSaleCount;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;

/**
 * @author P0033759 最后修改时间 2014-2-20-下午1:22:58 功能 显示上季度和当前季度的门店销量
 */
public class StoreSalesCountActivity extends BaseActivity {
	private LoadingView loadingView;
	private StoreController storeController;
	private StoreSalesCountUpdateView storeSalesCountUpdateView;

	private LinearLayout empty_layout;
	private String storeId = "";

	private StoreSaleListAdapter adapter;
	private ListView listView;
	List<StoreSaleCount> data = new ArrayList<SalesCountModel.StoreSaleCount>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_sales_count);
		initView();
		setListener();
		storeSalesCountUpdateView = new StoreSalesCountUpdateView(
				StoreSalesCountActivity.this);
		storeController = new StoreController();
		storeId = StoreSession.getCurrentStoreId();

		adapter = new StoreSaleListAdapter();
		listView.setAdapter(adapter);

		storeController.getAllStoreSalesCount(storeSalesCountUpdateView,
				StoreSession.getRepID());

	}

	private void setListener() {
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		// storeFunctionNames = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.common_id_lv);
		empty_layout = (LinearLayout) findViewById(R.id.common_id_empty_layout);
	}

	class ViewHolder {
		public TextView name;
		public TextView c_all_sales;
		public TextView c_quarter_por;
		public TextView c_sales_rate;
		public TextView l_all_sales;
		public TextView l_quarter_por;
		public TextView l_sales_rate;
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
				convertView = LayoutInflater.from(StoreSalesCountActivity.this)
						.inflate(R.layout.list_item_store_sales_count, null);
				viewHolder = new ViewHolder();
				// text_store_last_quarter_all_sales;
				// private TextView text_store_last_quarter_por;
				// private TextView text_store_last_quarter_sales_rate;
				//
				// private TextView text_store_current_quarter_all_sales;
				// private TextView text_store_current_quarter_por;
				// private TextView text_store_current_quarter_sales_rate;
				viewHolder.name= (TextView) convertView
						.findViewById(R.id.txt_store_sales_count_store_name);
				viewHolder.c_all_sales = (TextView) convertView
						.findViewById(R.id.txt_store_current_quarter_all_sales);
				viewHolder.c_quarter_por = (TextView) convertView
						.findViewById(R.id.txt_store_current_quarter_por);
				viewHolder.c_sales_rate = (TextView) convertView
						.findViewById(R.id.txt_store_current_quarter_sales_rate);
				viewHolder.l_all_sales = (TextView) convertView
						.findViewById(R.id.txt_store_last_quarter_all_sales);
				viewHolder.l_quarter_por = (TextView) convertView
						.findViewById(R.id.txt_store_last_quarter_por);
				viewHolder.l_sales_rate = (TextView) convertView
						.findViewById(R.id.txt_store_last_quarter_sales_rate);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			StoreSaleCount storeSaleCount = data.get(position);
			SaleQuarter first = null;
			SaleQuarter second = null;
			if (storeSaleCount.data.size() > 0)
				first = storeSaleCount.data.get(0);
			if (storeSaleCount.data.size() > 1)
				second = storeSaleCount.data.get(0);

			if (first != null) {
				viewHolder.c_all_sales.setText(first.SO);
				viewHolder.c_quarter_por.setText(first.POr);
				double rate = Double.parseDouble(first.POr)
						/ Double.parseDouble(first.SO);
				NumberFormat nt = NumberFormat.getPercentInstance();
				// 设置百分数精确度2即保留两位小数
				nt.setMinimumFractionDigits(2);
				viewHolder.c_sales_rate.setText(nt.format(rate) + "");
			}
			viewHolder.name.setText(storeSaleCount.storeName);
			
			if (second != null) {
				viewHolder.l_all_sales.setText(second.SO);
				viewHolder.l_quarter_por.setText(second.POr);
				double rate = Double.parseDouble(first.POr)
						/ Double.parseDouble(first.SO);
				NumberFormat nt = NumberFormat.getPercentInstance();
				// 设置百分数精确度2即保留两位小数
				nt.setMinimumFractionDigits(2);
				viewHolder.l_sales_rate.setText(nt.format(rate) + "");
			} else {
				String nodata = getApplication().getResources().getString(
						R.string.empty);
				viewHolder.l_all_sales.setText(nodata);
				viewHolder.l_quarter_por.setText(nodata);
				viewHolder.l_sales_rate.setText(nodata);
			}
			return convertView;
		}

	}

	private class StoreSalesCountUpdateView extends
			StoreCommonUpdateView<List<StoreSaleCount>> {

		public StoreSalesCountUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(List<StoreSaleCount> data1) {
			loadingView.hideLoading();
			if(data1.size()>0){
				data.clear();
				data.addAll(data1);
				adapter.notifyDataSetChanged();
			}else{
				empty_layout.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public void handleException(IException ex) {
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					storeController.getAllStoreSalesCount(storeSalesCountUpdateView,
							StoreSession.getRepID());
				}
			};
			DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreSalesCountActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);
		}

	}

}
