/**  
 * @Package com.intel.store.view.fragment 
 * @FileName: ProductFindFragment.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月18日 上午9:18:06 
 * @version V1.0  
 */
package com.intel.store.view.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.SalesReportController;
import com.intel.store.model.SalesReportModel;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.SaleReportDetailUploadedActivity;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.view.StoreSalesReporteHistoryActivity;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.TimeHelper;

/**
 * @author P0033759 最后修改时间 2014-4-15-下午1:48:59 功能
 */
@SuppressLint("ValidFragment")
public class SaleReportHistoryUploadedFragment extends BaseFragment implements
		OnClickListener {
	private List<MapEntity> dataList;
	private View rootView;
	private ReportHistoryListAdapter reportHistoryListAdapter;
	private ListView lv_report_list;

	private SalesReportController salesReportController;
	private LoadingView loadingView;
	private RemoteSalesRecordUpdateView remoteSalesRecordUpdateView;
	private RadioButton radioButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.report_history_content, container,
				false);
		new ViewHelper(this.getActivity());
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setListener();
	}

	private void initView() {
		loadingView = (LoadingView) rootView
				.findViewById(R.id.common_id_ll_loading);
		lv_report_list = (ListView) rootView.findViewById(R.id.common_id_lv);
		dataList = new ArrayList<MapEntity>();
		reportHistoryListAdapter = new ReportHistoryListAdapter(getActivity());
		lv_report_list.setAdapter(reportHistoryListAdapter);
	}

	private void setListener() {
		lv_report_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						SaleReportDetailUploadedActivity.class);
				Bundle bundle = new Bundle();
				MapEntity entity = dataList.get(position);
				bundle.putSerializable("data", entity);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

	public void DataLoaded() {
		dataList.clear();
		if (salesReportController==null) {
			salesReportController=new SalesReportController();
		}if (remoteSalesRecordUpdateView==null) {
			remoteSalesRecordUpdateView=new RemoteSalesRecordUpdateView(getActivity());
		}
		salesReportController.getSalesReocrd(remoteSalesRecordUpdateView);
		radioButton=(RadioButton) ((StoreSalesReporteHistoryActivity)getActivity()).rg_nav_content.getChildAt(0);
	}

	class ViewHolder {
		private TextView txt_date;
		private TextView txt_name;
		private TextView txt_barcode;
	}

	public class ReportHistoryListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ReportHistoryListAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			radioButton.setText(StoreApplication.getAppContext().getString(R.string.sales_reporte_uploaded)+"（"+dataList.size()+"）");
		}
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
					convertView = inflater.inflate(
							R.layout.list_item_sale_record, null);
					viewHolder = new ViewHolder();
					viewHolder.txt_date = (TextView) convertView
							.findViewById(R.id.txt_date);
					viewHolder.txt_name = (TextView) convertView
							.findViewById(R.id.txt_name);
					viewHolder.txt_barcode = (TextView) convertView
							.findViewById(R.id.txt_barcode);
					convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MapEntity entity = dataList.get(position);
			try {
				String date1 = entity.getString(SalesReportModel.RECORED_LAST_UPD_DTM);
				date1=date1.replaceFirst("-", "年");
				date1=date1.replaceFirst("-", "月");
				date1+="日";
				viewHolder.txt_date.setText(date1);
				viewHolder.txt_name.setText(entity
						.getString(SalesReportModel.RECORED_REP_NM));
				viewHolder.txt_barcode.setText(entity
						.getString(SalesReportModel.RECORED_BARCODE));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return dataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		default:
			break;
		}

	}

	private class RemoteSalesRecordUpdateView extends
			StoreCommonUpdateView<List<MapEntity>> {

		public RemoteSalesRecordUpdateView(Context context) {
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
			if (result!=null) {
				dataList.clear();
				dataList.addAll(result);
				reportHistoryListAdapter.notifyDataSetChanged();
			}
		}

		@Override
		public void handleException(IException ex) {
			loadingView.hideLoading();
			viewHelper.showErrorDialog(ex);
		}
	}
}
