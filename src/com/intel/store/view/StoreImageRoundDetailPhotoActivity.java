package com.intel.store.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.intel.store.widget.LoadingView;
import com.pactera.framework.model.MapEntity;

public class StoreImageRoundDetailPhotoActivity extends BaseActivity{
	private ListView list_rounds;	//图片上传轮次列表
	private Button btn_back;

	private LoadingView loadingView;
	private LinearLayout layout_empty;
	private String storeID;
	private RoundsListDetailAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_image_rounds);
		initPassedValues();
		initView();
		setListener();
	}

	private void initPassedValues() {
		//TODO
		//Intent intent = getIntent();
		//Bundle bundle = intent.getExtras();
	}

	public void initView() {
		layout_empty=(LinearLayout) findViewById(R.id.empty_layout);
		list_rounds=(ListView) findViewById(R.id.common_id_lv);
		btn_back=(Button) findViewById(R.id.common_id_btn_back);
		getRounds();
	}

	/**
	 * 加载X轮次详细列表
	 */
	private void getRounds() {
		//TODO add data
		List<MapEntity> entities=new ArrayList<MapEntity>();
		adapter=new RoundsListDetailAdapter(this, entities);
		list_rounds.setAdapter(adapter);
	}

	public void setListener() {

		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		list_rounds.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
	}


	public class RoundsListDetailAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		List<MapEntity> dataList = new ArrayList<MapEntity>();

		public void setDataList(List<MapEntity> dataList) {
			this.dataList = dataList;
		}

		public RoundsListDetailAdapter(Context context, List<MapEntity> dataList) {
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
			//TODO prase the entity
			hold.tv_head.setText("");
			hold.tv_content.setText("");
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


}
