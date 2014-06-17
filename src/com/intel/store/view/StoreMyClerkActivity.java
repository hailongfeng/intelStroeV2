package com.intel.store.view;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.intel.store.controller.ClerkController;
import com.intel.store.model.ClerkModel;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.ToastHelper;

@SuppressLint("CutPasteId")
public class StoreMyClerkActivity extends BaseActivity implements
		MyActivityInterface, OnClickListener {

	private static final int STORE_MY_CLERK_ADD = 21;
	private static final int STORE_MY_CLERK_DETAIL = 22;
	private Button btn_back;
	private Button btn_add_my_clerk;
	private ListView mlist_my_clerk;

	private MyClerkListControllerAdapter myClerkListControllerAdapter;
	private Bitmap defaultBitmap;
	private LinearLayout empty_layout;
	private LoadingView loadingView;
	private LoadingView loadingViewDele;
	private ClerkController clerkController;
	private String mRoleId=StoreSession.getRoleId();
	private ClerkListUpdateViewAsyncCallback viewUpdateCallback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_my_clerk);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initView();
		setListener();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	public void initView() {
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		loadingViewDele = (LoadingView) findViewById(R.id.common_id_ll_loading);
		empty_layout=(LinearLayout) findViewById(R.id.empty_layout);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_add_my_clerk = (Button) findViewById(R.id.btn_add_my_clerk);
	
		mlist_my_clerk = (ListView) findViewById(R.id.list_my_clerk);
		
		clerkController = new ClerkController();
		viewUpdateCallback=new ClerkListUpdateViewAsyncCallback(StoreMyClerkActivity.this);
		clerkController.getMyClerk(viewUpdateCallback);
		try {
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.myclerk_pic);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (mRoleId!=null&&mRoleId.equalsIgnoreCase("1")) {
			registerForContextMenu(mlist_my_clerk);
			setIconEnable(menu, true);
			getMenuInflater().inflate(R.menu.my_clerk, menu);
			btn_add_my_clerk.setVisibility(View.VISIBLE);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_add:
			Intent intentAddMyClerk = new Intent();
			intentAddMyClerk.setClass(StoreMyClerkActivity.this,
					StoreMyClerkAddActivity.class);
			startActivityForResult(intentAddMyClerk, STORE_MY_CLERK_ADD);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setListener() {
		loadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mlist_my_clerk.setVisibility(View.VISIBLE);
				loadingView.hideLoading();
				clerkController.getMyClerk(viewUpdateCallback);
			}
		});
		btn_back.setOnClickListener(this);
		btn_add_my_clerk.setOnClickListener(this);
		mlist_my_clerk.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Loger.d("mlist_my_clerk position:"+position);
				if (myClerkListControllerAdapter.getCount()>=1) {
					
					final MapEntity clerkInfo = (MapEntity) myClerkListControllerAdapter
							.getItem(position);
					Intent intentStoreMyClerkDetail = new Intent();
					intentStoreMyClerkDetail.setClass(StoreMyClerkActivity.this,
							StoreMyClerkDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("clerkInfo", clerkInfo);
					intentStoreMyClerkDetail.putExtras(bundle);
					startActivityForResult(intentStoreMyClerkDetail,
							STORE_MY_CLERK_DETAIL);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			StoreMyClerkActivity.this.finish();
			break;
		case R.id.btn_add_my_clerk:
			Intent intentAddMyClerk = new Intent();
			intentAddMyClerk.setClass(StoreMyClerkActivity.this,
					StoreMyClerkAddActivity.class);
			startActivityForResult(intentAddMyClerk, STORE_MY_CLERK_ADD);
			break;
		default:
			break;
		}
	}

	private	class MyClerkListControllerAdapter extends BaseAdapter{
		List<MapEntity>  mClerks;
		LayoutInflater inflater;
		public MyClerkListControllerAdapter(List<MapEntity> entities,Context context) {
			setData(entities);
			inflater=LayoutInflater.from(context);
		}
		
		public void setData(List<MapEntity> entities){
			if (entities!=null) {
				empty_layout.setVisibility(View.GONE);
				mClerks=entities;
			}else {
				empty_layout.setVisibility(View.VISIBLE);
				mClerks=new ArrayList<MapEntity>();
			}
		}
		@Override
		public int getCount() {
			return mClerks.size();
		}

		@Override
		public Object getItem(int position) {
			return mClerks.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView =inflater.inflate(R.layout.list_item_clerk, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.txt_clerk_name);
				viewHolder.position = (TextView) convertView
						.findViewById(R.id.txt_clerk_position);
				viewHolder.time = (TextView) convertView
						.findViewById(R.id.txt_clerk_phone_number);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MapEntity clerkInfo = (MapEntity) getItem(position);
			viewHolder.name.setText(clerkInfo
					.getString(ClerkModel.REP_NM));
			viewHolder.position.setText(clerkInfo
					.getString(ClerkModel.REP_ASGN_ROLE_NM));
			viewHolder.time.setText(clerkInfo
					.getString(ClerkModel.REP_TEL));
			return convertView;
		}
	}

	class ViewHolder {
		public TextView name;
		public TextView position;
		public TextView time;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == StoreMyClerkActivity.STORE_MY_CLERK_DETAIL) {
			switch (resultCode) {
			case RESULT_OK:
				Loger.d("resultCode=RESULT_OK");
				initView();
				break;

			default:
				break;
			}
		}
		if (requestCode == StoreMyClerkActivity.STORE_MY_CLERK_ADD) {
			switch (resultCode) {
			case RESULT_OK:
				initView();
				break;
				
			default:
				break;
			}
		}
	}

	private class DelMyclerkUpdateView extends
			StoreCommonUpdateView<Boolean> {

		public DelMyclerkUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			loadingViewDele.hideLoading();
		}

		@Override
		public void onPreExecute() {
			loadingViewDele.showLoading();
		}

		@Override
		public void onPostExecute(Boolean arg0) {
			loadingViewDele.hideLoading();

			if (arg0) {
				ToastHelper.getInstance().showLongMsg(getString(R.string.clerk_txt_delete_success));
				initView();
			} else {
				ToastHelper.getInstance().showLongMsg(getString(R.string.clerk_txt_delete_fail));
			}
		
		}

		@Override
		public void handleException(IException ex) {
			super.handleException(ex);
			loadingViewDele.hideLoading();
			viewHelper.showErrorDialog(ex);
		}
	}
	
	private class ClerkListUpdateViewAsyncCallback extends StoreCommonUpdateView<List<MapEntity>>{

		public ClerkListUpdateViewAsyncCallback(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			loadingView.showLoading();
			super.onPreExecute();
		}
		@Override
		public void onPostExecute(List<MapEntity> result) {
			if (myClerkListControllerAdapter==null) {
				myClerkListControllerAdapter=new MyClerkListControllerAdapter(result, StoreMyClerkActivity.this);
			}else {
				myClerkListControllerAdapter.setData(result);
			}
			mlist_my_clerk.setAdapter(myClerkListControllerAdapter);
			loadingView.hideLoading();
			
		}
		
		@Override
		public void onException(IException ie) {
			super.onException(ie);
		}
		
		@Override
		public void handleException(IException ex) {
			super.handleException(ex);
			loadingView.hideLoading();
			DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					clerkController.getMyClerk(viewUpdateCallback);
				}
			
		};
			DialogInterface.OnClickListener onCancelListener=new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StoreMyClerkActivity.this.finish();
				}
			};
			viewHelper.showErrorDialog(ex, positiveListener, onCancelListener);
		}
	}
}
