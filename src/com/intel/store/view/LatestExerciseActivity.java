package com.intel.store.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.LatestExerciseListByPageController;
import com.pactera.framework.imgload.AsyncImageLoader;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.page.AbstractPageAdapter;
import com.pactera.framework.util.page.DataLoaderHandler;

public class LatestExerciseActivity extends BaseActivity {

	private ListView mLatestExerciseListView;
	private Button btn_back;
	private Bitmap defaultBitmap;
	private LatestExerciseControllerListAdapter adapter;
	private LatestExerciseListByPageController mLatestExerciseController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_latest_exercise);
		initView();
		setListener();
		try {
			defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.store_list_default);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		mLatestExerciseController = new LatestExerciseListByPageController();
		adapter = new LatestExerciseControllerListAdapter(mLatestExerciseListView, LatestExerciseActivity.this,
				R.layout.loading_add_data, mLatestExerciseController);
		adapter.setNoDataLayout((LinearLayout) findViewById(R.id.layout_empty));
		mLatestExerciseListView.setAdapter(adapter);
		mLatestExerciseListView.setOnScrollListener(adapter);
	}

	private void initView() {
		mLatestExerciseListView = (ListView) findViewById(R.id.common_id_lv);
		btn_back = (Button) findViewById(R.id.common_id_btn_back);
	}

	private void setListener() {
		mLatestExerciseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MapEntity mapEntity = (MapEntity) parent.getItemAtPosition(position);
				int itemId = mapEntity.getInt(LatestExerciseListByPageController.ID);
				//ToastHelper.getInstance().showShortMsg(itemId + "");
				
				Intent exerciseDetailIntent = new Intent(LatestExerciseActivity.this, ExerciseDetailActivity.class);
				exerciseDetailIntent.putExtra("id", itemId);
				startActivity(exerciseDetailIntent);
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	class ViewHolder {
		public ImageView img;
		public TextView name;
		public TextView time;
	}

	public class LatestExerciseControllerListAdapter extends AbstractPageAdapter<MapEntity> implements
			IImageLoadCallback {
		private AsyncImageLoader asyncImageLoader = null;

		public LatestExerciseControllerListAdapter(ListView listView, Context context, int loadingViewResourceId,
				DataLoaderHandler<MapEntity> handler) {
			super(listView, context, loadingViewResourceId, handler);
			asyncImageLoader = new AsyncImageLoader(this, 5, 30);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(LatestExerciseActivity.this).inflate(R.layout.latest_exercise_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.img = (ImageView) convertView.findViewById(R.id.iv_exercise);
				viewHolder.name = (TextView) convertView.findViewById(R.id.txt_exercise_name);
				viewHolder.time = (TextView) convertView.findViewById(R.id.txt_exercise_time);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final MapEntity storeInfo = (MapEntity) getItem(position);
			viewHolder.name.setText(storeInfo.getString(LatestExerciseListByPageController.NAME));
			viewHolder.time.setText(storeInfo.getString(LatestExerciseListByPageController.TIME));
			final String photoUrl = "";
			Bitmap bm = asyncImageLoader.getBitmapFromCache(photoUrl);
			if (bm != null) {
				viewHolder.img.setImageBitmap(bm);
			} else {
				viewHolder.img.setImageBitmap(defaultBitmap);
				asyncImageLoader.loadImage(new ImageInfo(photoUrl, 100));
			}
			return convertView;
		}


		@Override
		public void onLoadData(boolean arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void fail(ImageInfo arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void success(ImageInfo arg0) {
			notifyDataSetChanged();
		}

		@Override
		public void handleDetailException(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

	}

}
