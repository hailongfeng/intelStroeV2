package com.intel.store.view;

import java.util.ArrayList;
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
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class StoreImageRoundDetailActivity extends BaseActivity{
	private ListView list_rounds;	//图片上传轮次列表
	private TextView tv_title;
	private ListPhoCatViewUpdateCallback updateCallback;
	private LoadingView loadingView;
	private LinearLayout layout_empty;
	private RoundsListDetailAdapter adapter;
	private String wvId;
	private StorePhotoController controller;
	private String title;
	private boolean increasable;//当前轮次是否可以拍照

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_rounds);
		initPassedValues();
		initView();
		setListener();
	}

	private void initPassedValues() {
		Intent intent = getIntent();
		wvId=intent.getStringExtra("wv_id");
		title=intent.getStringExtra("title");
		increasable=intent.getBooleanExtra("increasable",false);
	}

	public void initView() {
		loadingView=(LoadingView) findViewById(R.id.common_id_ll_loading);
		layout_empty=(LinearLayout) findViewById(R.id.common_id_empty_layout);
		list_rounds=(ListView) findViewById(R.id.common_id_lv);
		tv_title=(TextView) findViewById(R.id.tv_title);
		if (title!=null) {
			tv_title.setText(title);
		}
		getRounds();
	}

	/**
	 * 加载X轮次详细列表
	 */
	private void getRounds() {
		if (controller==null) {
			controller=new StorePhotoController();
		}
		if (updateCallback==null) {
			updateCallback=new ListPhoCatViewUpdateCallback(this);
		}
		if (wvId!=null) {
			controller.listPhotoByStoreWv(updateCallback, wvId);
		}
	}

	public void setListener() {

		list_rounds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(StoreImageRoundDetailActivity.this,
						StoreImageShowByTypeActivity.class); 
				MapEntity entity=(MapEntity) view.getTag(R.id.tag_first);
				intent.putExtra("wv_id", wvId);
				Bundle bundle=new Bundle();
				bundle.putSerializable("entity", entity);
				bundle.putBoolean("increasable", increasable);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}


	public class RoundsListDetailAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();

		public void setDataList(ArrayList<MapEntity> dataList) {
			this.dataList = dataList;
		}

		public RoundsListDetailAdapter(Context context, ArrayList<MapEntity> dataList) {
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
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold = null;
			if (convertView == null) {
				hold = new ViewHold();
				convertView = inflater.inflate(R.layout.list_item_photo_upload_round_detail,
						null);
				hold.tv_head = (TextView) convertView
						.findViewById(R.id.txt_head);
				hold.tv_content = (TextView) convertView
						.findViewById(R.id.txt_content_1);
			} else {
				hold = (ViewHold) convertView.getTag();
			}
			MapEntity entity=dataList.get(position);
			String catName=	entity.getString(QueryPicCategoryModel.PHT_CAT_NM);
			String phtQty=	entity.getString(QueryPicCategoryModel.PHT_QTY);
			String picCount=	entity.getString(QueryPicCategoryModel.PIC_COUNT);
			
			convertView.setTag(R.id.tag_first,entity);
			hold.tv_head.setText(catName);
			hold.tv_content.setText(getString(R.string.txt_take_picture_upload_description,phtQty,picCount));
			return convertView;
		}

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		initView();
	}

	class ViewHold {
		public TextView tv_head;
		public TextView tv_content;
	}

 class	ListPhoCatViewUpdateCallback extends 
 StoreCommonUpdateView<ArrayList<MapEntity>>{

	public ListPhoCatViewUpdateCallback(Context context) {
		super(context);
	}

	@Override
	public void onPreExecute() {
		super.onPreExecute();
		layout_empty.setVisibility(View.GONE);
		loadingView.showLoading();
	}
	@Override
	public void onPostExecute(ArrayList<MapEntity> result) {
		loadingView.hideLoading();
		Loger.d("onPostExecute"+result);
		if (result==null||result.size()==0) {
			layout_empty.setVisibility(View.VISIBLE);
			return;
		}
		adapter=new RoundsListDetailAdapter(StoreImageRoundDetailActivity.this, result);
		list_rounds.setAdapter(adapter);
	}
	@Override
	public void handleException(IException ex) {
		super.handleException(ex);
		viewHelper.showErrorDialog(ex);
		loadingView.hideLoading();
	}
 }
 }
