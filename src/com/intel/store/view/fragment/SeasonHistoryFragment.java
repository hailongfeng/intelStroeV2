/**  
 * @Package com.intel.store.view.fragment 
 * @FileName: ProductFindFragment.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月18日 上午9:18:06 
 * @version V1.0  
 */
package com.intel.store.view.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.SalesReportController;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicRoundModel;
import com.intel.store.model.SalesReportModel;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.SaleReportDetailUploadedActivity;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.view.StoreImageActivity;
import com.intel.store.view.StoreImageRecentActivity;
import com.intel.store.view.StoreImageRoundDetailActivity;
import com.intel.store.view.StoreSalesReporteHistoryActivity;
import com.intel.store.view.fragment.SaleReportHistoryUploadedFragment.ReportHistoryListAdapter;
import com.intel.store.view.fragment.SaleReportHistoryUploadedFragment.ViewHolder;
import com.intel.store.view.fragment.SeasonCurrentFragment.RoundListAdapter.ViewHold;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.InputChecker;

/**
 * @author P0033759 最后修改时间 2014-4-15-下午1:48:59 功能
 */
@SuppressLint("ValidFragment")
public class SeasonHistoryFragment extends BaseFragment {
	private List<MapEntity> dataList;
	private View rootView;
	private ListView lv_report_list;

	private LoadingView loadingView;
	private LinearLayout empty_layout;
	private RoundsListAdapter adapter;
	private StorePhotoController photoController;
	private StorePicRoundsUpdateView storePicRoundsUpdateView;
	
	
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
		adapter = new RoundsListAdapter(getActivity());
		lv_report_list.setAdapter(adapter);
	}

	private void setListener() {
		lv_report_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent StoreImageDateListIntent = new Intent(
						getActivity(),
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

	public void DataLoaded() {
		if (dataList!=null&&dataList.size()>1) {
			return;
		}
		dataList.clear();
		if (photoController==null) {
			photoController=new StorePhotoController();
		}if (storePicRoundsUpdateView==null) {
			storePicRoundsUpdateView=new StorePicRoundsUpdateView(getActivity());
		}
		photoController.queryHistoryWv(storePicRoundsUpdateView);
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
				convertView = inflater.inflate(R.layout.list_item_photo_upload_round,
						null);
				hold.tv_head = (TextView) convertView
						.findViewById(R.id.txt_head);
				hold.tv_content1 = (TextView) convertView
						.findViewById(R.id.txt_content_1);
				hold.tv_content2 = (TextView) convertView
						.findViewById(R.id.txt_content_2);
				convertView.setTag(hold);
			} else {
				hold = (ViewHold) convertView.getTag();
			}
			MapEntity entity=dataList.get(position);
			String wvId=entity.getString(QueryPicRoundModel.WV_ID);
			String title=(entity.getString(QueryPicRoundModel.WV_NM));
			Boolean increasable=false;
			String patternData="yyyy-MM-dd HH:mm:ss";
			String patternView=getString(R.string.txt_pic_upload_time_pattern);
			SimpleDateFormat formatData=new SimpleDateFormat(patternData);
			SimpleDateFormat formatView=new SimpleDateFormat(patternView);
			String startTime = entity.getString(QueryPicRoundModel.WV_START_DT);
			String endTime = entity.getString(QueryPicRoundModel.WV_END_DT);
			Date dateStart=null;
			Date dateEnd=null;
			Date dataNow = new Date();
			try {
				dateStart = formatData.parse(startTime);
				dateEnd = formatData.parse(endTime);
				if (dataNow.after(dateStart)&&dataNow.before(dateEnd)) {
					increasable=true;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			convertView.setTag(R.id.tag_first,wvId);
			convertView.setTag(R.id.tag_second,title);
			convertView.setTag(R.id.tag_third,increasable);
			hold.tv_head.setText(title);
			hold.tv_content1.setText(getString(R.string.txt_pic_upload_start_time)
					+formatView.format(dateStart));
			hold.tv_content2.setText(getString(R.string.txt_pic_upload_death_time)
					+formatView.format(dateEnd));
			return convertView;
		
			/*
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
		*/}

		class ViewHold {

			public TextView tv_head;
			public TextView tv_content1;
			public TextView tv_content2;
		/*
			public TextView tv_head;
		*/}
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
