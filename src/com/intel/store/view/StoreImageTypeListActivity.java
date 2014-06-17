package com.intel.store.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class StoreImageTypeListActivity extends BaseActivity {

	private LoadingView loadingView;
	private Button btn_back;
	private LinearLayout layout_empty;
	private StorePhotoController queryPicCategoryController=new StorePhotoController();
	//图片类型列表
	private ListView lv_image_types;
	//图片类型适配
	private ImageTypeAdapter mImageTypeAdapter;
	private ViewHelper mViewHelper=new ViewHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_type_list_layout);
		initPassedValues();
		initView();
		setListener();
	}
	private void initPassedValues() {
			
	}

	private void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
		btn_back = (Button) findViewById(R.id.btn_back);
		lv_image_types = (ListView) findViewById(R.id.common_id_lv);
		mImageTypeAdapter=new ImageTypeAdapter(this);
		lv_image_types.setAdapter(mImageTypeAdapter);
		queryPicCategoryController.queryPicCategory(new QueryPicTypeViewCallback(StoreImageTypeListActivity.this));
	}


	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		lv_image_types
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						//获取选中的图片类型
						MapEntity photoCats=(MapEntity) view.getTag(R.id.photoCats_id);
						if (photoCats!=null) {
							String phtCatName= photoCats.getValue(QueryPicCategoryModel.PHT_CAT_NM).toString();
							String phtQty= photoCats.getValue(QueryPicCategoryModel.PHT_QTY).toString();
							Loger.d("phtCatName:"+phtCatName);
							Intent intent=new Intent();
							//产品上架跳转二级分类
							if (phtCatName.equalsIgnoreCase("产品上架")&&phtQty!=null) {
								Loger.d("this=====");
								intent.putExtra("PHT_QTY",phtQty);
								intent.setClass(StoreImageTypeListActivity.this, ProductAddTypeActivity.class);
							}
							//否则跳转到上传图片
							else {
								intent.setClass(StoreImageTypeListActivity.this,PhotoUploadDetailActivity.class);
								Bundle data=new Bundle();
								data.putSerializable("photoCategory", photoCats);
								intent.putExtras(data);
							}
							startActivity(intent);
							finish();
						}
						//报错
						else{
							Loger.e("MapEntity 空异常");
						}
					}
				});
	/*	lv_image_types.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MapEntity entity=(MapEntity) arg1.getTag(R.id.photoCats_id);
				Loger.d(entity.getString(QueryPicCategoryModel.PHT_CAT_NM));
				Intent intentPhotoUploadDetailActivity=new Intent(StoreImageTypeListActivity.this,PhotoUploadDetailActivity.class);
				Bundle data=new Bundle();
				data.putSerializable("photoCategory", entity);
				intentPhotoUploadDetailActivity.putExtras(data);
				startActivity(intentPhotoUploadDetailActivity);
				finish();
			}
		});*/
	}


	private class QueryPicTypeViewCallback extends
			StoreCommonUpdateView<ArrayList<MapEntity>> {

		public QueryPicTypeViewCallback(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingView.hideLoading();
		}

		@Override
		public void onException(IException arg0) {
			super.onException(arg0);
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					queryPicCategoryController.queryPicCategory(new QueryPicTypeViewCallback(StoreImageTypeListActivity.this));
				}
			};
			DialogInterface.OnClickListener onCancelListener=new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreImageTypeListActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(arg0, positiveListener, onCancelListener);
		}

		@Override
		public void onPostExecute(ArrayList<MapEntity> result) {
			if (result!=null&&result.size() != 0) {
				mImageTypeAdapter.setData(result);
				mImageTypeAdapter.notifyDataSetChanged();
			} else {
				layout_empty.setVisibility(View.VISIBLE);
			}
			loadingView.hideLoading();
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
		}

	}

	public class ImageTypeAdapter extends BaseAdapter {

		private Context mContext;
		private ArrayList<MapEntity> mData;

		public ImageTypeAdapter(Context context) {
			this.mContext = context;
		}

		public void setData(ArrayList<MapEntity> data) {
			this.mData = data;
		}

		@Override
		public int getCount() {
			return mData == null ? 0 : mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData == null ? 0 : mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.list_item_photo_cat, null);
				viewHolder = new ViewHolder();
				viewHolder.tv_pht_qty = (TextView) convertView
						.findViewById(R.id.tv_pic_qty);
				viewHolder.tv_phtCat = (TextView) convertView
						.findViewById(R.id.tv_photoCat);
				viewHolder.iv_phtCmnts = (ImageView) convertView
						.findViewById(R.id.iv_cmnts);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			MapEntity photoCats = (MapEntity) getItem(position);
			if (photoCats != null) {
				convertView.setTag(R.id.photoCats_id, photoCats);
				String picCatName = photoCats.getString(QueryPicCategoryModel.PHT_CAT_NM);
				String picQty = photoCats.getString(QueryPicCategoryModel.PHT_QTY);
				String picCmnts = photoCats.getString(QueryPicCategoryModel.CMNTS);
				viewHolder.tv_phtCat.setText(picCatName);
				viewHolder.tv_pht_qty.setText(picQty);
				viewHolder.iv_phtCmnts.setTag(picCmnts);
				viewHolder.iv_phtCmnts.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mViewHelper.showBTNDialog(v.getTag().toString());
					}
				});
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_pht_qty;
			TextView tv_phtCat;
			ImageView iv_phtCmnts;
		}

	}

}
