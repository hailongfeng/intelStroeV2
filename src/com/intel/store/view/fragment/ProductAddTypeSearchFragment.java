/**  
 * @Title: ProductAddTypeSearchFragment.java 
 * @Package: com.intel.store.view.fragment 
 * @Description:(用一句话描述该文件做什么) 
 * @author: fenghl 
 * @date: 2013年12月26日 下午5:14:21 
 * @version: V1.0  
 */
package com.intel.store.view.fragment;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.intel.store.R;
import com.intel.store.dao.local.LocalDBConstants.PrdCategory;
import com.intel.store.model.ProductTypeEnum;
import com.intel.store.model.ProductTypeModel;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.PhotoUploadDetailActivity;
import com.intel.store.view.ProductAddTypeActivity;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.ToastHelper;

/**
 * @Title: ProductAddTypeSearchFragment.java
 * @Package: com.intel.store.view.fragment
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2013年12月26日 下午5:14:21
 * @version: V1.0
 */
@SuppressLint("ValidFragment")
public class ProductAddTypeSearchFragment extends BaseFragment {
	private List<MapEntity> dataList;

	private View rootView;
	private ProductListAdapter productListAdapter;
	private ProductTypeEnum productType;

	private ImageButton ibtn_search;
	private EditText edt_product_name;
	private ListView lv_product_list;
	private ViewHelper mViewHelper;

	@SuppressLint("ValidFragment")
	public ProductAddTypeSearchFragment(ProductTypeEnum productType) {
		super();
		this.productType = productType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.product_find_content, container,
				false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mViewHelper = new ViewHelper(getActivity());
		initView();
		setListener();
	}

	private void initView() {
		// defaultBitmap = BitmapFactory.decodeResource(getResources(),
		// R.drawable.store_list_default);
		edt_product_name = (EditText) rootView
				.findViewById(R.id.edt_product_name);
		lv_product_list = (ListView) rootView.findViewById(R.id.common_id_lv);
		dataList = new ArrayList<MapEntity>();
		productListAdapter = new ProductListAdapter(getActivity(), dataList);
		lv_product_list.setAdapter(productListAdapter);
		ibtn_search = (ImageButton) rootView
				.findViewById(R.id.btn_product_search);
	}

	private void setListener() {
		ibtn_search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String productName = edt_product_name.getText().toString()
						.trim();
				searchProductByName(productName);
			}
		});
		lv_product_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MapEntity temMapEntity = dataList.get(position);
				final String pName = temMapEntity
						.getString(PrdCategory.PRD_MODEL);
				final String pId = temMapEntity.getString(PrdCategory.MDL_ID);
				mViewHelper.showBTN2Dialog("确认类型", pName, "确定", "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 保存该选择到本地数据库

								// 确认选择
								Intent photoUpload = new Intent();
								photoUpload.setClass(getActivity(),
										PhotoUploadDetailActivity.class);
								MapEntity entity = new MapEntity();
								entity.setValue(
										QueryPicCategoryModel.PHT_CAT_NM, pName);
								entity.setValue(QueryPicCategoryModel.PHT_CAT,
										pId);
								entity.setValue(QueryPicCategoryModel.PHT_QTY,
										ProductAddTypeActivity.photeQty);
								Bundle bundle = new Bundle();
								bundle.putSerializable("photoCategory", entity);
								photoUpload.putExtras(bundle);
								startActivity(photoUpload);
								getActivity().finish();
							}
						}, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// productTypeListAdapter.notifyDataSetChanged();
							}
						}, new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								dialog.dismiss();
								// productTypeListAdapter.notifyDataSetChanged();
							}
						});
			}
		});
	}

	public void DataLoaded() {
		ProductTypeModel productTypeModel = new ProductTypeModel();

		List<MapEntity> data = new ArrayList<MapEntity>();
		try {
			data = productTypeModel.getProductFromLocal(null);
		} catch (DBException e) {
			e.printStackTrace();
		}
		dataList.addAll(data);
		productListAdapter.notifyDataSetChanged();
	}

	private void searchProductByName(String name) {
		dataList.clear();
		ProductTypeModel productTypeModel = new ProductTypeModel();

		List<MapEntity> data = new ArrayList<MapEntity>();
		try {
			data = productTypeModel.getProductFromLocal(name);
		} catch (DBException e) {
			e.printStackTrace();
		}
		dataList.addAll(data);
		productListAdapter.notifyDataSetChanged();
		ToastHelper.getInstance().showShortMsg("查询完成");
	}

	class ViewHolder {
		public TextView name;
		public TextView id;

	}

	public class ProductListAdapter extends BaseAdapter {
		// private AsyncImageLoader asyncImageLoader = null;
		private LayoutInflater inflater;
		List<MapEntity> dataList = new ArrayList<MapEntity>();

		public ProductListAdapter(Context context, List<MapEntity> data) {
			dataList = data;
			inflater = LayoutInflater.from(context);
			// asyncImageLoader = new AsyncImageLoader(this, 5, 30);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = inflater.inflate(R.layout.product_type_list_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_product_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MapEntity storeInfo = (MapEntity) getItem(position);
			String name = storeInfo.getString(PrdCategory.PRD_MODEL);
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
