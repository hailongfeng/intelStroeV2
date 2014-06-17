package com.intel.store.view;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;

public class StoreListActivity extends BaseActivity {
	ListView listView;
	private StoreListAdapter adapter;
	private StoreController storeController;
	List<MapEntity> storeList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_list);
		storeController = new StoreController();
		StoreListUpdateView storeListUpdateView = new StoreListUpdateView(StoreListActivity.this);
		String sr_id = "53825";
		storeController.getStoreByRepId(storeListUpdateView, sr_id);
		listView = (ListView) findViewById(R.id.lv_store_list);
		adapter = new StoreListAdapter();
		listView.setAdapter(adapter);
	}

	private class StoreListUpdateView extends StoreCommonUpdateView<List<MapEntity>> {

		public StoreListUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPostExecute(List<MapEntity> arg0) {
			storeList=arg0;
		}

		@Override
		public void handleException(IException ex) {
			ex.printStackTrace();
		}

	}

	class ViewHolder {
		public ImageView img;
		public TextView name;
	}

	public class StoreListAdapter extends BaseAdapter implements IImageLoadCallback {

		@Override
		public int getCount() {
			return storeList.size();
		}

		@Override
		public Object getItem(int position) {
			return storeList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return null;
		}

		@Override
		public void fail(ImageInfo arg0) {

		}

		@Override
		public void success(ImageInfo arg0) {

		}

	}

}
