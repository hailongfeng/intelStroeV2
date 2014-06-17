package com.intel.store.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.dao.remote.StoreRemoteBaseDao;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.Constants;
import com.intel.store.util.ImageInfoWarp;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.util.StoreSession;
import com.pactera.framework.imgload.AsyncImageLoader.IImageLoadCallback;
import com.pactera.framework.imgload.ImageInfo;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @author P0033759
 * 
 *         门店资料
 */
public class StoreInfoActivity extends BaseActivity implements
		MyActivityInterface, OnClickListener, IImageLoadCallback {
	/**返回按钮*/
	private	Button btn_back;
	/**更多信息之门店销量*/
	private LinearLayout linnear_store_sales_volume_select;
	/**更多信息之门店积分*/
	private	LinearLayout linnear_store_score_select;
	/**更多信息之我的店员*/
	private	LinearLayout linnear_store_my_clerk_select;
	/**显示门店等级*/
	private TextView tv_store_city_type_nm;
	/**显示门店类型*/
	private TextView tv_store_type;
	/**显示门店地址*/
	private TextView tv_store_addr;
	/**显示门店邮箱*/
	private TextView tv_store_email;
	/**显示门店电话*/
	private TextView tv_store_tel;
	/**显示门店名称*/
	private TextView tv_store_name;
	/**门店实体数据*/
	private MapEntity storeInfoEntity;
	/**门店店面照片*/
	private ImageView iv_store_image;
	/**照片图像*/
	private Bitmap storeBitmap;
	/**门店店面照片*/
	private ImageInfoWarp storeImageInfo;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_info);
		storeInfoEntity = (MapEntity) getIntent().getExtras().getSerializable(
				"storeInfo");
		initView();
		setListener();
	}

	@Override
	public void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		linnear_store_sales_volume_select = (LinearLayout) findViewById(R.id.linnear_store_sales_volume_select);
		int roleId = Integer.parseInt(StoreSession.getCurrentStoreRole());
		if (roleId==StoreListModel.OEM) {
			linnear_store_sales_volume_select.setVisibility(View.GONE);
		}
		linnear_store_score_select = (LinearLayout) findViewById(R.id.linnear_store_score_select);
		linnear_store_my_clerk_select = (LinearLayout) findViewById(R.id.linnear_store_my_clerk_select);
		tv_store_city_type_nm = (TextView) findViewById(R.id.tv_store_city_type_nm);
		tv_store_type = (TextView) findViewById(R.id.tv_store_type);
		tv_store_addr = (TextView) findViewById(R.id.tv_store_addr);
		tv_store_email = (TextView) findViewById(R.id.tv_store_email);
		tv_store_tel = (TextView) findViewById(R.id.tv_store_tel);
		tv_store_name = (TextView) findViewById(R.id.tv_store_name);
		//获取门店信息
		if (storeInfoEntity != null) {
			tv_store_name.setText(storeInfoEntity
					.getString(StoreListModel.STOR_NM));
			tv_store_city_type_nm.setText(
					 storeInfoEntity.getString(StoreListModel.CAT_TYPE_NM));
			String roleName = "";
			if (roleId==StoreListModel.DIY) {
				roleName = StoreListModel.DIY_NAME;
			}
			//如果是OEM门店，则没有门店积分，不显示门店积分
			else if (roleId==StoreListModel.OEM) {
				linnear_store_score_select.setVisibility(View.GONE);
				roleName = StoreListModel.OEM_NAME;
			}
			else {
				Loger.e("unknow roleId:"+roleId);
				roleName="unknow role:"+roleId;
			}
			tv_store_type.setText( roleName);
			tv_store_addr.setText(
					storeInfoEntity.getString(StoreListModel.STOR_ADDR));
			tv_store_email.setText( storeInfoEntity.getString(StoreListModel.EMAIL));
			tv_store_tel.setText( storeInfoEntity.getString(StoreListModel.STOR_TEL));
			 url = StoreRemoteBaseDao.PICTURE_HOST+Constants.READ_SINGLE_RSP_PIC_SERVELET
					+ storeInfoEntity.getString(StoreListModel.PHT_PTH);
			Loger.d(url);
			iv_store_image = (ImageView) findViewById(R.id.iv_store_image);
			try {
				storeBitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.default_img);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			iv_store_image.setImageBitmap(storeBitmap);
			if (url != null) {
				storeImageInfo = new ImageInfoWarp(url, true,
						true, 100);
				storeImageInfo.setImageView(iv_store_image);
				StoreApplication.asyncImageLoader
						.setImageLoadCallback(StoreInfoActivity.this);
				StoreApplication.asyncImageLoader.loadImage(storeImageInfo);
			}
		} else {
			Loger.d("没有获取门店详细信息");
		}
	}

	@Override
	public void setListener() {
		btn_back.setOnClickListener(this);
		linnear_store_sales_volume_select.setOnClickListener(this);
		linnear_store_score_select.setOnClickListener(this);
		linnear_store_my_clerk_select.setOnClickListener(this);
		iv_store_image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		// 门店销量
		case R.id.linnear_store_sales_volume_select:
			Intent intentStoreSalesVolume = new Intent();
			intentStoreSalesVolume.setClass(StoreInfoActivity.this,
					StoreSalesCountActivity.class);
			startActivity(intentStoreSalesVolume);
			break;
		// 门店积分
		case R.id.linnear_store_score_select:

			Intent intentStoreIntegral = new Intent();
			intentStoreIntegral.setClass(StoreInfoActivity.this,
					StoreIntegralActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("storeInfo", storeInfoEntity);
			intentStoreIntegral.putExtras(bundle);
			startActivity(intentStoreIntegral);
			break;
		// 我的员工
		case R.id.linnear_store_my_clerk_select:
			Intent intentStoreMyClerk = new Intent();
			intentStoreMyClerk.setClass(StoreInfoActivity.this,
					StoreMyClerkActivity.class);
			startActivity(intentStoreMyClerk);
			break;
		// 门店图片	
		case R.id.iv_store_image:
			Intent intent = new Intent();
			if (url!=null) {
				intent.putExtra("photo_url",url);
			}else {
				intent.putExtra("unupload",false);
			}
               intent.setClass(StoreInfoActivity.this,
                       PhotoZoomActivity.class);
               startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void fail(ImageInfo imageInfo) {
		Loger.d("获取门店图片失败");
	}

	@Override
	public void success(ImageInfo imageInfo) {
		storeBitmap=imageInfo.getBitmap();
		((ImageInfoWarp) imageInfo).getImageView().setImageBitmap(
				storeBitmap);
	}

}
