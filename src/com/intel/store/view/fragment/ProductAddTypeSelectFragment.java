package com.intel.store.view.fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.model.ProductTypeModel;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.PhotoUploadDetailActivity;
import com.intel.store.view.ProductAddTypeActivity;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @Title: ProductAddTypeSearchFragment.java
 * @Package: com.intel.store.view.fragment
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2013年12月26日 下午5:14:21
 * @version: V1.0
 */
public class ProductAddTypeSelectFragment extends BaseFragment {
	private List<MapEntity> dataList;
	private LinkedList<MapEntity> historyDataList;
	private Button btn_product_type_one;
	private Button btn_product_type_two;
	private Button btn_product_type_three;

	private View rootView;
	private ProductTypeParentListAdapter productTypeListAdapter;

	private ListView lv_product_type;
	private int currentLever = 1;
	private ProductTypeModel productTypeModel = null;
	private ViewHelper mViewHelper;
	private View layout_empty;

	public ProductAddTypeSelectFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.product_type_select_content,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		productTypeModel = new ProductTypeModel();
		mViewHelper = new ViewHelper(getActivity());
		initView();
		setListener();
	}

	private void initView() {
		btn_product_type_one = (Button) rootView
				.findViewById(R.id.btn_product_type_one);
		btn_product_type_one.setBackgroundColor(getResources().getColor(R.color.base_blue_92cfe4_normal));
		btn_product_type_two = (Button) rootView
				.findViewById(R.id.btn_product_type_two);
		btn_product_type_three = (Button) rootView
				.findViewById(R.id.btn_product_type_three);
		
		btn_product_type_two.setVisibility(View.GONE);
		btn_product_type_three.setVisibility(View.GONE);
		layout_empty=rootView
				.findViewById(R.id.layout_empty);
		lv_product_type = (ListView) rootView.findViewById(R.id.common_id_lv);
		dataList = new ArrayList<MapEntity>();
		historyDataList = new LinkedList<MapEntity>();
		productTypeListAdapter = new ProductTypeParentListAdapter(
				getActivity(), dataList);
		lv_product_type.setAdapter(productTypeListAdapter);
	}

	private void setListener() {

		btn_product_type_one.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				historyDataList.clear();
				DataLoaded();
				historyDataList.clear();
				currentLever=1;
				btn_product_type_one.setBackgroundColor(getResources().getColor(R.color.base_blue_92cfe4_normal));
				btn_product_type_one.setTextColor(getActivity().getResources().getColor(R.color.base_white_ffffff_normal));
				btn_product_type_two.setVisibility(View.GONE);
				btn_product_type_three.setVisibility(View.GONE);
			}
		});
		btn_product_type_two.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//DataLoaded();
				String catId = "";
				dataList.clear();
				List<MapEntity> data = new ArrayList<MapEntity>();
				catId = historyDataList.get(0).getString(
						ProductTypeModel.COMMON_ID);
				try {
					data = productTypeModel.getPrdBrndFromLocal(catId);
				} catch (DBException e) {
					e.printStackTrace();
				}
				for (int i = 1; i < historyDataList.size(); i++) {
					historyDataList.remove(i);
				}
				dataList.addAll(data);
				currentLever=2;
				productTypeListAdapter.notifyDataSetChanged();
				btn_product_type_two.setBackgroundColor(getResources().getColor(R.color.base_blue_92cfe4_normal));
				btn_product_type_two.setTextColor(getActivity().getResources().getColor(R.color.base_white_ffffff_normal));
				btn_product_type_three.setVisibility(View.GONE);
			}
		});
		btn_product_type_three.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String catId = "";
				String bandId = "";
				dataList.clear();
				List<MapEntity> data = new ArrayList<MapEntity>();
				catId = historyDataList.get(0).getString(
						ProductTypeModel.COMMON_ID);
				bandId = historyDataList.get(1).getString(
						ProductTypeModel.COMMON_ID);
				try {
					data = productTypeModel.getPrdModelCategoryFromLocal(
							catId, bandId);
				} catch (DBException e) {
					e.printStackTrace();
				}
				dataList.addAll(data);
				currentLever=2;
				productTypeListAdapter.notifyDataSetChanged();
			}
		});

		lv_product_type.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 父节点的数据信息
				MapEntity mapEntity = dataList.get(position);
				historyDataList.add(mapEntity);
				String catId = "";
				String catName = "";
				String bandId = "";
				String bandName= "";
				List<MapEntity> data = new ArrayList<MapEntity>();
				Loger.d("currentLever："+currentLever);
				switch (currentLever) {
				case 1:
					// 加载第二级,保存第一级的内容
					dataList.clear();
					catId = historyDataList.get(0).getString(
							ProductTypeModel.COMMON_ID);
					catName=historyDataList.get(0).getString(
							ProductTypeModel.COMMON_NAME);
					Loger.d("catId:"+catId);
					try {
						data = productTypeModel.getPrdBrndFromLocal(catId);
					} catch (DBException e) {
						e.printStackTrace();
					}
					dataList.addAll(data);
					if(dataList.size()==0){
						lv_product_type.setVisibility(View.GONE);
						layout_empty.setVisibility(View.VISIBLE);
					}else{
						lv_product_type.setVisibility(View.VISIBLE);
						layout_empty.setVisibility(View.GONE);
					}
					currentLever=2;
					btn_product_type_one.setBackgroundColor(getResources().getColor(R.color.base_gray_e0e6ea));
					btn_product_type_one.setTextColor(getActivity().getResources().getColor(R.color.black));
					btn_product_type_two.setText(catName);
					btn_product_type_two.setBackgroundColor(getResources().getColor(R.color.base_blue_92cfe4_normal));
					btn_product_type_two.setTextColor(getActivity().getResources().getColor(R.color.base_white_ffffff_normal));
					btn_product_type_two.setVisibility(View.VISIBLE);
					productTypeListAdapter.notifyDataSetChanged();
					break;
				case 2:
					// 加载第三级
					dataList.clear();
					catId = historyDataList.get(0).getString(
							ProductTypeModel.COMMON_ID);
					bandId = historyDataList.get(1).getString(
							ProductTypeModel.COMMON_ID);
					bandName=historyDataList.get(1).getString(
							ProductTypeModel.COMMON_NAME);
					try {
						data = productTypeModel.getPrdModelCategoryFromLocal(
								catId, bandId);
					} catch (DBException e) {
						e.printStackTrace();
					}
					dataList.addAll(data);
					if(dataList.size()==0){
						lv_product_type.setVisibility(View.GONE);
						layout_empty.setVisibility(View.VISIBLE);
					}else{
						lv_product_type.setVisibility(View.VISIBLE);
						layout_empty.setVisibility(View.GONE);
					}
					currentLever=3;
					btn_product_type_one.setBackgroundColor(getResources().getColor(R.color.base_gray_e0e6ea));
					btn_product_type_one.setTextColor(getActivity().getResources().getColor(R.color.black));
					btn_product_type_two.setBackgroundColor(getResources().getColor(R.color.base_gray_e0e6ea));
					btn_product_type_two.setTextColor(getActivity().getResources().getColor(R.color.black));
					btn_product_type_three.setBackgroundColor(getResources().getColor(R.color.base_blue_92cfe4_normal));
					btn_product_type_three.setText(bandName);
					btn_product_type_three.setTextColor(getActivity().getResources().getColor(R.color.base_white_ffffff_normal));
					btn_product_type_three.setVisibility(View.VISIBLE);
					productTypeListAdapter.notifyDataSetChanged();
					break;
				case 3:
					// 最红的类型，弹出提示框，确认当前选择的类型
					MapEntity temMapEntity = historyDataList.get(2);
					final String pName = temMapEntity
							.getString(ProductTypeModel.COMMON_NAME);
					final String pId = temMapEntity
							.getString(ProductTypeModel.COMMON_ID);
					mViewHelper.showBTN2Dialog("确认类型", pName, "确定", "取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//保存该选择到本地数据库
							
							//确认选择
							Intent photoUpload = new Intent();
							photoUpload.setClass(getActivity(), PhotoUploadDetailActivity.class);
							MapEntity entity = new MapEntity();
							entity.setValue(QueryPicCategoryModel.PHT_CAT_NM, pName);
							entity.setValue(QueryPicCategoryModel.PHT_CAT, pId);
							entity.setValue(QueryPicCategoryModel.PHT_QTY, ProductAddTypeActivity.photeQty);
							Bundle bundle = new Bundle();
							bundle.putSerializable("photoCategory", entity);
							photoUpload.putExtras(bundle);
							startActivity(photoUpload);
							getActivity().finish();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							//productTypeListAdapter.notifyDataSetChanged();
						}
					}, new OnCancelListener() {
						
						@Override
						public void onCancel(DialogInterface dialog) {
							dialog.dismiss();
							//productTypeListAdapter.notifyDataSetChanged();
						}
					});
					break;
				default:
					break;
				}
			}
		});
	}

	public void DataLoaded() {
		dataList.clear();
		List<MapEntity> data = new ArrayList<MapEntity>();
		try {
			data = productTypeModel.getPrdCategoryFromLocal();
		} catch (DBException e) {
			e.printStackTrace();
		}
		dataList.addAll(data);
		if(dataList.size()==0){
			lv_product_type.setVisibility(View.GONE);
			layout_empty.setVisibility(View.VISIBLE);
		}else{
			lv_product_type.setVisibility(View.VISIBLE);
			layout_empty.setVisibility(View.GONE);
		}
		productTypeListAdapter.notifyDataSetChanged();
	}

	class ViewHolder {
		public TextView name;
	}

	public class ProductTypeParentListAdapter extends BaseAdapter {
		// private AsyncImageLoader asyncImageLoader = null;
		private LayoutInflater inflater;
		List<MapEntity> dataList = new ArrayList<MapEntity>();

		public ProductTypeParentListAdapter(Context context,
				List<MapEntity> data) {
			dataList = data;
			inflater = LayoutInflater.from(context);
			// asyncImageLoader = new AsyncImageLoader(this, 5, 30);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = inflater
						.inflate(R.layout.product_type_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_product_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MapEntity storeInfo = (MapEntity) getItem(position);
			String name = storeInfo.getString(ProductTypeModel.COMMON_NAME);
			viewHolder.name.setText(name);
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

}
