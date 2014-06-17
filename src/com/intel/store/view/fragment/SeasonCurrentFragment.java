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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.intel.store.R;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicRoundModel;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.view.StoreImageRoundDetailActivity;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
/**
 * @ClassName: ProductFindFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fenghl
 * @date 2013年10月18日 上午9:18:06
 */
@SuppressLint("ValidFragment")
public class SeasonCurrentFragment extends BaseFragment {
	private StorePhotoController controller;
	private QueryCurrentQuarterUpdateCallback currentQuarterUpdateCallback;
	private QueryPicRoundViewUpdateCallback picRoundViewUpdateCallback;
	private View rootView;
	
	private RoundListAdapter adapter;
	private ArrayList<MapEntity> dataList;
	private ListView list_round;
	
	private LinearLayout emptyLayout;
	private LoadingView loadingView;

	public SeasonCurrentFragment() {
		super();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView=inflater.inflate(R.layout.report_history_content, container,false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setListener();
		DataLoaded();
	}

	private void initView() {
		adapter=new RoundListAdapter(getActivity(),null);
		list_round=(ListView) rootView.findViewById(R.id.common_id_lv);
		emptyLayout=(LinearLayout) rootView.findViewById(R.id.common_id_empty_layout);
		loadingView=(LoadingView) rootView.findViewById(R.id.common_id_ll_loading);
		list_round.setAdapter(adapter);
	}

	private void setListener() {
		list_round.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(), 
						StoreImageRoundDetailActivity.class);
				intent.putExtra("wv_id", (String) view.getTag(R.id.tag_first));
				intent.putExtra("title", (String) view.getTag(R.id.tag_second));
				intent.putExtra("increasable", (Boolean) view.getTag(R.id.tag_third));
				startActivity(intent);
			}
		});
	}
	
	
	public void DataLoaded() {
		if (dataList!=null&&dataList.size()>1) {
			return;
		}
		if (controller==null) {
			controller=new StorePhotoController();
		}if (currentQuarterUpdateCallback==null) {
			currentQuarterUpdateCallback=new QueryCurrentQuarterUpdateCallback(getActivity());
		}
		controller.queryCurrentQuarter(currentQuarterUpdateCallback, null);
	}

	//季度
		class QueryCurrentQuarterUpdateCallback extends StoreCommonUpdateView<MapEntity>{
			
			public QueryCurrentQuarterUpdateCallback(Context context) {
				super(context);
			}
			
			@Override
			public void handleException(IException ex) {
				super.handleException(ex);
				loadingView.hideLoading();
				DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						controller.queryCurrentQuarter(currentQuarterUpdateCallback, null);
					}
				};
				viewHelper.showErrorDialog(ex, positiveListener, null);
			}
			@Override
			public void onPreExecute() {
				super.onPreExecute();
				loadingView.showLoading();
				emptyLayout.setVisibility(View.INVISIBLE);
			}
			@Override
			public void onPostExecute(MapEntity result) {
				if (result==null) {
					emptyLayout.setVisibility(View.VISIBLE);
					return;
				}
				emptyLayout.setVisibility(View.INVISIBLE);
				loadingView.hideLoading();
				if (picRoundViewUpdateCallback==null) {
					picRoundViewUpdateCallback=new QueryPicRoundViewUpdateCallback(getActivity());
				}
				controller.queryPicRound(picRoundViewUpdateCallback, result.getString(QueryPicRoundModel.CURRENT_QUARTER));
			}
			
		}
		//轮次
		class QueryPicRoundViewUpdateCallback extends StoreCommonUpdateView<ArrayList<MapEntity>>{

			public QueryPicRoundViewUpdateCallback(Context context) {
				super(context);
			}

			@Override
			public void onPreExecute() {
				super.onPreExecute();
				loadingView.showLoading();
				emptyLayout.setVisibility(View.INVISIBLE);
			}
			@Override
			public void onPostExecute(ArrayList<MapEntity> result) {
				if (result==null||result.size()==0) {
					emptyLayout.setVisibility(View.VISIBLE);
					loadingView.hideLoading();
					return;
				}
				dataList=result;
				emptyLayout.setVisibility(View.INVISIBLE);
				String currentYyyyQq = "";
				String wv_nm = "";
				String yyyy = "";
				String jd = "";
				int i = 1;
				for (MapEntity mapEntity : dataList) {
					String tempyyyyqq = mapEntity
							.getString(QueryPicRoundModel.YYYYQQ);
					yyyy = tempyyyyqq.substring(0, 4);
					jd = tempyyyyqq.substring(5, 6);

					if (currentYyyyQq.equals(tempyyyyqq)) {
						wv_nm = getString(R.string.txt_pic_upload_round,yyyy,jd,i);
						mapEntity.setValue(QueryPicRoundModel.WV_NM, wv_nm);
					} else {
						wv_nm = getString(R.string.txt_pic_upload_round,yyyy,jd,i);
						mapEntity.setValue(QueryPicRoundModel.WV_NM, wv_nm);
						currentYyyyQq = tempyyyyqq;
					}
					i++;
				}
			
				 adapter.setDataList(dataList);
				 list_round.setAdapter(adapter);
				 loadingView.hideLoading();
			}
			
		}
		
		
		public class RoundListAdapter extends BaseAdapter {
			private LayoutInflater inflater;

			List<MapEntity> dataList = new ArrayList<MapEntity>();

			public void setDataList(List<MapEntity> dataList) {
				this.dataList = dataList;
			}

			public RoundListAdapter(Context context, ArrayList<MapEntity> dataList) {
				inflater = LayoutInflater.from(context);
				this.dataList = dataList;
			}

			@Override
			public int getCount() {
				if (dataList!=null) {
					return dataList.size();
				}
				return 0;
			}

			@Override
			public Object getItem(int arg0) {
				if (dataList!=null) {
					return dataList.get(arg0);
				}
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return arg0;
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
			}

			class ViewHold {
				public TextView tv_head;
				public TextView tv_content1;
				public TextView tv_content2;
			}
		}

}
