package com.intel.store.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StorePhotoController;
import com.intel.store.model.QueryPicModel;
import com.intel.store.model.StorePhotoModel;
import com.intel.store.util.ViewHelper;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.imgload.AsyncImageLoader;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class StoreImageDetailActivity extends BaseActivity implements
		IImageLoadCallback {
	private ViewPager mViewPager;
	private List<MapEntity> photoList;
	private Button btn_back;
	private Button btn_delete;
	private int pagePosition;
	//private AsyncImageLoader asyncImageLoader = null;
	private PhotoAdapter mAdapter;
	private Bitmap defaultBitmap;
	private TextView txt_category;
	private TextView txt_category2;
	private TextView txt_comment;
	private TextView txt_user;
	private TextView txt_time;
	private String pic_id;
	private LoadingView loadingView;
	private StorePhotoController photoController;
	private ViewHelper mviewHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_image_detail_viewpager);
		initPassedValues();
		initView();
		setListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	        MenuItem addPhoto = menu.add("删除");
	        MenuItemCompat.setShowAsAction(addPhoto, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
	        addPhoto.setIcon(R.drawable.action_bar_menu_item_del_clerk_icon);
		return super.onCreateOptionsMenu(menu);
	}
	  @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
		  super.onOptionsItemSelected(item);
	    	if (item.getTitle().toString().contains("删除")) {
				android.content.DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteCurPictrue();
					}
				};
				mviewHelper.showBTN2Dialog(getString(R.string.personal_txt_title), getString(R.string.photo_delete_ask), getString(R.string.txt_yes), getString(R.string.txt_no),
						positiveListener, null, null);
	    	}
	    	return true;
	    }
	@Override
    protected void onDestroy() {
        super.onDestroy();
        if (defaultBitmap != null) {
            defaultBitmap.recycle();
            defaultBitmap = null;
        }
    /*    if (asyncImageLoader != null) {
            asyncImageLoader.stopThreads();
            asyncImageLoader = null;
        }*/
    }
	@SuppressWarnings("unchecked")
	private void initPassedValues() {
		photoList = new ArrayList<MapEntity>();
		Bundle bundle = getIntent().getExtras();
		pagePosition = bundle.getInt("position");
		photoList = (List<MapEntity>) bundle.getSerializable("photoList");
	}

	private void initView() {
		mviewHelper=new ViewHelper(this);
		loadingView=(LoadingView) findViewById(R.id.common_id_ll_loading);
		btn_delete=(Button) findViewById(R.id.btn_delete);
		btn_back=(Button) findViewById(R.id.btn_back);
		
		txt_category = (TextView) findViewById(R.id.txt_title_msg);
		txt_category2 = (TextView) findViewById(R.id.txt_img_cat);
		txt_comment = (TextView) findViewById(R.id.txt_comment);
		txt_user = (TextView) findViewById(R.id.txt_user);
		txt_time = (TextView) findViewById(R.id.txt_time);
		txt_comment.setMovementMethod(new ScrollingMovementMethod());
		
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOffscreenPageLimit(1);

		//asyncImageLoader = new AsyncImageLoader(this, 3, 30);

		mAdapter = new PhotoAdapter(this, photoList);
		try {
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.store_img_detail_default);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		mViewPager.setAdapter(mAdapter);
		Loger.d("setCurrentItem"+pagePosition);
		setCurrentItem(mViewPager,pagePosition);
	}
	private void setCurrentItem(ViewPager viewPager,int position){
		mViewPager.setCurrentItem(position);
		Loger.e("onPageSelected:"+position);
	    txt_category.setText(photoList.get(position).getString(QueryPicModel.PHT_CAT_NM));
	    txt_category2.setText(photoList.get(position).getString(QueryPicModel.PHT_CAT_NM));
	    //两格空格
	    //txt_comment.setText("\u3000\u3000"+photoList.get(position).getString(QueryPicByDateModel.CMNTS));
	    txt_comment.setText(photoList.get(position).getString(QueryPicModel.CMNTS));
	    txt_user.setText( photoList.get(position).getString(QueryPicModel.LAST_UPD_USR_NM));
	    txt_time.setText(photoList.get(position).getString(QueryPicModel.LAST_UPD_DTM));
		pic_id=photoList.get(position).getString(QueryPicModel.PIC_ID);
	}
	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				android.content.DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteCurPictrue();
					}
				};
				mviewHelper.showBTN2Dialog(getString(R.string.personal_txt_title), getString(R.string.photo_delete_ask), getString(R.string.txt_yes), getString(R.string.txt_no),
						positiveListener, null, null);
			}

		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				setCurrentItem(mViewPager, position);
			
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
	}

	private void deleteCurPictrue() {
		if (photoController==null) {
			photoController=new StorePhotoController();
		}
		photoController.delPictureByIdFromRemote(new DelPhotoUpdateViewCallback(StoreImageDetailActivity.this), pic_id);
	}


	@Override
	protected void onPostResume() {
		mViewPager.setCurrentItem(pagePosition);
		super.onPostResume();
	}

	public class PhotoAdapter extends PagerAdapter {
		private List<MapEntity> dataList = new ArrayList<MapEntity>();

		private Context mContext;

		public PhotoAdapter(Context cx, List<MapEntity> dataList) {
			mContext = cx.getApplicationContext();
			this.dataList = dataList;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == (View) obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView img = new ImageView(mContext);
			img.setScaleType(ScaleType.CENTER);
			final Bitmap bm = StoreApplication.asyncImageLoader.getBitmapFromCache(dataList
					.get(position).getString(StorePhotoModel.PHT_PTH));
			if (bm != null) {
				img.setImageBitmap(bm);
			} else {
				img.setImageBitmap(defaultBitmap);
				StoreApplication.asyncImageLoader.loadImage(new ImageInfo(
						dataList.get(position).getString(StorePhotoModel.PHT_PTH), true,true,500));
			}
			if (bm != null) {
				img.setScaleType(ScaleType.CENTER_CROP);
				img.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			
            img.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StoreImageDetailActivity.this,
                            PhotoZoomActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("photo_url", dataList.get(position).getString(StorePhotoModel.PHT_PTH));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    pagePosition = position;

                }
            });
            
            
			((ViewPager) container).addView(img, 0);
			return img;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void success(ImageInfo imageInfo) {
		mViewPager.removeAllViews();
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void fail(ImageInfo imageInfo) {
	}
	class DelPhotoUpdateViewCallback extends StoreCommonUpdateView<Boolean>{

		public DelPhotoUpdateViewCallback(Context context) {
			super(context);
		}

		@Override
		public void onCancelled() {
			btn_delete.setClickable(true);
			loadingView.hideLoading();
		}
		
		@Override
		public void onException(IException arg0) {
			super.onException(arg0);
			loadingView.hideLoading();
			if (arg0.getMessage()!=null&&arg0.getMessage().contains("不存在")) {
					//服务器已经删除，则直接删除
					onPostExecute(true);
			}
		DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				deleteCurPictrue();
			}
		};
		DialogInterface.OnClickListener onCancelListener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StoreImageDetailActivity.this.finish();
			}
		};

		viewHelper.showErrorDialog(arg0, positiveListener, onCancelListener);
		btn_delete.setClickable(true);
		}

		@Override
		public void onPostExecute(Boolean arg0) {
			Loger.d("onPostExecute");
			btn_delete.setClickable(true);
			if (arg0) {
				pagePosition=mViewPager.getCurrentItem();
				photoList.remove(pagePosition);
				mAdapter = new PhotoAdapter(StoreImageDetailActivity.this, photoList);
				mViewPager.removeAllViews();
				mViewPager.setAdapter(mAdapter);
				if (photoList.size()>0) {
					if (pagePosition>0) {
					setCurrentItem(mViewPager, pagePosition-1);
					}else {
						setCurrentItem(mViewPager, pagePosition);
					}
					mviewHelper.showBTNDialog(getString(R.string.photo_deleted));
				}else {
					mviewHelper.showBTNDialog(getString(R.string.photo_deleted_null));
					StoreImageDetailActivity.this.finish();
				}
			}
			loadingView.hideLoading();
		}

		@Override
		public void onPreExecute() {
			btn_delete.setClickable(false);
			loadingView.showLoading();
		}
		
	}
}
