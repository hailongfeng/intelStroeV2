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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.intel.store.R;
import com.intel.store.dao.local.LocalProductDao;
import com.intel.store.model.ProductTypeEnum;
import com.intel.store.view.ProductDetailActivity;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.ToastHelper;

/**
 * @ClassName: ProductFindFragment
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fenghl
 * @date 2013年10月18日 上午9:18:06
 */
@SuppressLint("ValidFragment")
public class ProductFindFragment extends BaseFragment {
	private List<MapEntity> dataList;

	private View rootView;
	private ProductListAdapter productListAdapter;
	private ProductTypeEnum productType;

	private ImageButton ibtn_search;
	private EditText edt_product_name;
	private ListView lv_product_list;

	public ProductFindFragment() {
		super();
	}

	@SuppressLint("ValidFragment")
	public ProductFindFragment(ProductTypeEnum productType) {
		this();
		this.productType = productType;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.product_find_content, container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setListener();
	}

	private void initView() {
		//defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.store_list_default);
		edt_product_name = (EditText) rootView.findViewById(R.id.edt_product_name);
		lv_product_list = (ListView) rootView.findViewById(R.id.common_id_lv);
		dataList = new ArrayList<MapEntity>();
		productListAdapter = new ProductListAdapter(getActivity(), dataList);
		lv_product_list.setAdapter(productListAdapter);
		ibtn_search = (ImageButton) rootView.findViewById(R.id.btn_product_search);
	}

	private void setListener() {
		ibtn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String productName = edt_product_name.getText().toString().trim();
				// ToastHelper.getInstance().showShortMsg(productName);
				if ("".equals(productName)) {
					DataLoaded();
				} else {
					searchProductByName(productName);
				}
			}
		});
		lv_product_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("pro_nm", dataList.get(position).getString(LocalProductDao.PRO_NM));
				bundle.putString("cache", dataList.get(position).getString(LocalProductDao.CACHE));
				bundle.putString("code_nm", dataList.get(position).getString(LocalProductDao.CODE_NM));
				bundle.putString("core_nbr", dataList.get(position).getString(LocalProductDao.CORE_NBR));
				bundle.putString("thd_cnt", dataList.get(position).getString(LocalProductDao.THD_CNT));
				bundle.putString("clk_spd", dataList.get(position).getString(LocalProductDao.CLK_SPD));
				bundle.putString("max_freq", dataList.get(position).getString(LocalProductDao.MAX_FREQ));
				bundle.putString("lith", dataList.get(position).getString(LocalProductDao.LITH));
				bundle.putString("gfx_mdl", dataList.get(position).getString(LocalProductDao.GFX_MDL));
				bundle.putString("gfx_max_freq", dataList.get(position).getString(LocalProductDao.GFX_MAX_FREQ));
				bundle.putString("type", dataList.get(position).getString(LocalProductDao.TYPE));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}

	public void DataLoaded() {
		dataList.clear();
		LocalProductDao localProductDao = new LocalProductDao();
		String selection = "type like ?";
		String selectionArgs[] = null;
		if (productType.value() == ProductTypeEnum.Mobile.value()) {
			selectionArgs = new String[] { "Mobile" };
		} else if (productType.value() == ProductTypeEnum.Desktop.value()) {
			selectionArgs = new String[] { "Desktop" };
		}
		List<MapEntity> data = localProductDao.getProducts(selection, selectionArgs);
		dataList.addAll(data);
		productListAdapter.notifyDataSetChanged();
	}

	private void searchProductByName(String name) {
		dataList.clear();
		LocalProductDao localProductDao = new LocalProductDao();
		String selection = "type like ? and pro_nm like ?";
		String selectionArgs[] = null;
		if (productType.value() == ProductTypeEnum.Mobile.value()) {
			selectionArgs = new String[] { "Mobile", "%" + name + "%" };
		} else if (productType.value() == ProductTypeEnum.Desktop.value()) {
			selectionArgs = new String[] { "Desktop", "%" + name + "%" };
		}
		List<MapEntity> data = localProductDao.getProducts(selection, selectionArgs);
		dataList.addAll(data);
		productListAdapter.notifyDataSetChanged();
		ToastHelper.getInstance().showShortMsg("查询完成");
	}

	class ViewHolder {
		public TextView name;
		public TextView config;

	}

	public class ProductListAdapter extends BaseAdapter {
		//private AsyncImageLoader asyncImageLoader = null;
		private LayoutInflater inflater;
		List<MapEntity> dataList = new ArrayList<MapEntity>();

		public ProductListAdapter(Context context, List<MapEntity> data) {
			dataList = data;
			inflater = LayoutInflater.from(context);
			//asyncImageLoader = new AsyncImageLoader(this, 5, 30);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = inflater.inflate(R.layout.product_list_item, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView.findViewById(R.id.txt_product_name);
				viewHolder.config = (TextView) convertView.findViewById(R.id.txt_product_price);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MapEntity storeInfo = (MapEntity) getItem(position);
			String name = storeInfo.getString(LocalProductDao.PRO_NM);
			String config = "";

			if (productType.value() == ProductTypeEnum.Mobile.value()) {
				name += " Processor(Mobile)";
			} else {
				name += " Processor(Desktop)";
			}
			if (TextUtils.isEmpty(dataList.get(position).getString(LocalProductDao.MAX_FREQ))) {
				config = "(" + dataList.get(position).getString(LocalProductDao.CACHE) + " Cache "
						+ dataList.get(position).getString(LocalProductDao.CLK_SPD) + ")";
			} else {
				config = "(" + dataList.get(position).getString(LocalProductDao.CACHE) + " Cache up to "
						+ dataList.get(position).getString(LocalProductDao.MAX_FREQ) + ")";
			}

			viewHolder.name.setText(name);
			viewHolder.config.setText(config);
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
