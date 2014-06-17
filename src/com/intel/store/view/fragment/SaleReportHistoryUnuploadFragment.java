/**  
 * @Package com.intel.store.view.fragment 
 * @FileName: ProductFindFragment.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月18日 上午9:18:06 
 * @version V1.0  
 */
package com.intel.store.view.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.controller.StoreController;
import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.intel.store.dao.local.LocalSaleReportDao;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ViewHelper;
import com.intel.store.view.SaleReportDetailActivity;
import com.intel.store.view.StoreCommonUpdateView;
import com.intel.store.view.StoreSalesReporteHistoryActivity;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @author P0033759 最后修改时间 2014-4-15-下午1:48:59 功能
 */
@SuppressLint("ValidFragment")
public class SaleReportHistoryUnuploadFragment extends BaseFragment implements
		OnClickListener {
	public static final int RequestCodeDel = 10;
	
	private ArrayList<MapEntity> dataList;
	private ArrayList<MapEntity> TmpDataList;
	private static final int ISChecked = 100;
	private View rootView;
	private ReportHistoryListAdapter reportHistoryListAdapter;
	private ListView lv_report_list;

	// 上传
	private CheckBox checkBoxAll;
	/** 上传状态*/
	public Boolean inUpload = false;
	/** 已上传数据是否改变 */
	public Boolean f1Changed = false;
	private Button btnDelete, btnUpload;
	private LoadingView loadingView;
	private ViewHelper mViewHelper;
	private AmazonS3Client s3Client;
	private StoreController storeController;
	public int i = 0;
	private RadioButton radioButton;

	protected boolean xxx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.report_history_content, container,
				false);
		s3Client = new AmazonS3Client(new BasicAWSCredentials(
				Constants.ACCESS_KEY_ID, Constants.SECRET_KEY));
		s3Client.setEndpoint(Constants.ENDPOINT);
		mViewHelper = new ViewHelper(this.getActivity());
		storeController = new StoreController();
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		setListener();
	}

	private void initView() {
		checkBoxAll = (CheckBox) rootView.findViewById(R.id.cb_all);
		checkBoxAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
			//XXX
				if (isChecked) {
					xxx=true;
					for (MapEntity entity : dataList) {
						entity.setValue(ISChecked, true);
					}
					xxx=false;
					
				} 
				else{
					for (MapEntity entity : dataList) {
						if (!entity.getBool(ISChecked)) {
							//如果是全选状态下点击的子项
							return;
						}
					}
					//如果是全选状态下点击的全选按钮
					for (MapEntity entity : dataList) {
						entity.setValue(ISChecked, false);
					}
				}
				reportHistoryListAdapter.notifyDataSetChanged();
			}
		});

		btnDelete = (Button) rootView.findViewById(R.id.btn_delete_all);
		btnUpload = (Button) rootView.findViewById(R.id.btn_upload_all);
		loadingView = (LoadingView) rootView
				.findViewById(R.id.common_id_ll_loading);
		lv_report_list = (ListView) rootView.findViewById(R.id.common_id_lv);
		dataList = new ArrayList<MapEntity>();
		reportHistoryListAdapter = new ReportHistoryListAdapter(getActivity(),
				dataList);
		lv_report_list.setAdapter(reportHistoryListAdapter);
	}

	private void setListener() {
		lv_report_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						SaleReportDetailActivity.class);
				Bundle bundle = new Bundle();
				MapEntity entity = dataList.get(position);
				bundle.putSerializable("data", entity);
				intent.putExtras(bundle);
				startActivityForResult(intent, RequestCodeDel);
			}
		});
		btnDelete.setOnClickListener(this);
		btnUpload.setOnClickListener(this);

	}

	public void DataLoaded() {
		dataList.clear();
		LocalSaleReportDao localSaleReportDao = new LocalSaleReportDao();
		ArrayList<MapEntity> data;
		try {
			data = localSaleReportDao.getSaleReportRecords(
					StoreSession.getName(), StoreSession.getCurrentStoreId());
			for (MapEntity mapEntity : data) {
				mapEntity.setValue(ISChecked, false);
			}
			dataList.addAll(data);
			radioButton=(RadioButton) ((StoreSalesReporteHistoryActivity)getActivity()).rg_nav_content.getChildAt(1);
			radioButton.setText(StoreApplication.getAppContext().getString(R.string.sales_reporte_not_uploaded)+"（"+dataList.size()+"）");
		} catch (DBException e) {
			e.printStackTrace();
		}
		reportHistoryListAdapter.notifyDataSetChanged();
	}

	class ViewHolder {
		private TextView txt_date;
		private TextView txt_name;
		private TextView txt_barcode;
		private CheckBox cb;
	}

	public class ReportHistoryListAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		@Override
		public void notifyDataSetChanged() {
			if (dataList==null||dataList.size()==0) {
				rootView.findViewById(R.id.common_id_empty_layout).setVisibility(View.VISIBLE);
				rootView.findViewById(R.id.rl_unupload).setVisibility(View.GONE);
			}else {
				rootView.findViewById(R.id.common_id_empty_layout).setVisibility(View.GONE);
				rootView.findViewById(R.id.rl_unupload).setVisibility(View.VISIBLE);
			}
			super.notifyDataSetChanged();
			radioButton.setText(StoreApplication.getAppContext().getString(R.string.sales_reporte_not_uploaded)+"（"+dataList.size()+"）");
		}
		public ReportHistoryListAdapter(Context context,
				ArrayList<MapEntity> data) {
			dataList = data;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (null == convertView) {
				convertView = inflater.inflate(
						R.layout.list_item_sale_record_unupload, null);
				viewHolder = new ViewHolder();
				viewHolder.txt_date = (TextView) convertView
						.findViewById(R.id.txt_date);
				viewHolder.txt_name = (TextView) convertView
						.findViewById(R.id.txt_name);
				viewHolder.txt_barcode = (TextView) convertView
						.findViewById(R.id.txt_barcode);
				viewHolder.cb = (CheckBox) convertView.findViewById(R.id.cb);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			MapEntity entity = dataList.get(position);
			try {
				Date date1 = new Date(Long.valueOf(entity
						.getString(SaleReportRecord.DATA_TIME)));
				String pattern = "yyyy年MM日dd";
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				String date2 = dateFormat.format(date1);
				viewHolder.txt_date.setText(date2);
				viewHolder.txt_name.setText(entity
						.getString(SaleReportRecord.USER_NAME));
				viewHolder.txt_barcode.setText(entity
						.getString(SaleReportRecord.SERIAL_NUMBER));
			} catch (Exception e) {
				e.printStackTrace();
			}
			final int pos = position; // pos必须声明为final
			viewHolder.cb
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							dataList.get(pos).setValue(ISChecked, isChecked);
							if (xxx) {
								return;
							}
							for (MapEntity mapEntity : dataList) {
								if (mapEntity.getBool(ISChecked) == false) {
									checkBoxAll.setChecked(false);
									return;
								}
							}
							checkBoxAll.setChecked(true);
						}
					});
			viewHolder.cb.setChecked(dataList.get(pos).getBool(ISChecked));
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete_all:
			//只能在temp中删除
			//XXX
			DialogInterface.OnClickListener positiveListener=new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ArrayList<MapEntity> tmpDelete = new ArrayList<MapEntity>();
					for (MapEntity entity : dataList) {
						if (entity.getBool(ISChecked)) {
							tmpDelete.add(entity);
						}
					}
					try {
						for (MapEntity entity : tmpDelete) {
							deleteReport(entity);
						}
						dataList.removeAll(tmpDelete);
					} catch (DBException e) {
						mViewHelper.showErrorDialog(e);
						e.printStackTrace();
					}
					reportHistoryListAdapter.notifyDataSetChanged();
				}
			};
			for (MapEntity entity : dataList) {
				if (entity.getBool(ISChecked)) {
					mViewHelper.showBTNDialog("确定删除？", "确认", positiveListener, null);
					return;
				}
			}
			break;
		case R.id.btn_upload_all:
			if (inUpload) {
				return;
			}
			TmpDataList=new ArrayList<MapEntity>();
			for (MapEntity entity : dataList) {
				if (entity.getBool(ISChecked)) {
					TmpDataList.add(entity);
				}
			}
			if (TmpDataList.size()==0) {
				mViewHelper.showBTNDialog("请先选择后再上传");
				return;
			}
			inUpload=true;
			upSalesReporte(TmpDataList.get(0));
			
			// 先上传图片到天翼云
			break;

		default:
			break;
		}

	
	}

	private void deleteReport(MapEntity entity) throws DBException {
		LocalSaleReportDao localSaleReportDao = new LocalSaleReportDao();
		localSaleReportDao.deleteSaleReportRecord(entity.getString(0));
		File file = new File(entity.getString(SaleReportRecord.PIC_PATH));
		file.delete();
	}

	private void upSalesReporte(MapEntity entity) {
		if (!inUpload) {
			return;
		}
		Loger.i(" uri=" + entity.getString(SaleReportRecord.PIC_PATH));
		S3TaskResult result = new S3TaskResult();
		result.mapEntity = entity;
		new S3UpPictureTask().execute(result);
	}

	private class S3TaskResult {
		private String errorMessage = null;
		private MapEntity mapEntity = null;

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

	}

	/**
	 * 上传到天翼云
	 * 
	 * @Title: StoreSalesReporteActivity.java
	 * @Package: com.intel.store.view
	 * @Description:(用一句话描述该文件做什么)
	 * @author: fenghl
	 * @date: 2014年4月4日 下午1:36:07
	 * @version: V1.0
	 */
	private class S3UpPictureTask extends
			AsyncTask<S3TaskResult, Void, S3TaskResult> {

		S3TaskResult result;
		private Uri uri;

		protected void onPreExecute() {
			loadingView.showLoading();
		}

		protected S3TaskResult doInBackground(S3TaskResult... results) {

			if (results == null || results.length != 1) {
				return null;
			}
			result = results[0];
			Loger.d("上传图片："
					+ result.mapEntity.getString(SaleReportRecord.PIC_PATH)
							.toString());
			uri = Uri.parse(result.mapEntity.getString(
					SaleReportRecord.PIC_PATH).toString());
			Loger.d("上传图片：" + uri.toString());
			File file = new File(uri.toString());
			System.out.println(file.getName());
			try {
				Loger.e("上传图片" + uri);
				s3Client.putObject(Constants.BUCKET_PICTURE_NAME,
						file.getName(), file);

			} catch (Exception exception) {
				exception.printStackTrace();
				Loger.e(exception.getMessage());
				result.setErrorMessage(exception.getMessage());
			}

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {
			loadingView.hideLoading();

			if (result.getErrorMessage() != null) {
				// 上传失败，则删除图片
				mViewHelper.showBTNDialog("申报失败");
				new S3DeletePictureTask().doInBackground(uri.toString());
				inUpload=false;
			} else {
				MapEntity entity = result.mapEntity;
				String barcode = entity
						.getString(SaleReportRecord.SERIAL_NUMBER);
				File file = new File(entity
						.getString(SaleReportRecord.PIC_PATH));
				String pic_loc = Constants.PICTURE_DOMAIN + "/"
						+ file.getName();

				// 图片上传成功之后进行数据保存
				if (Integer.parseInt(StoreSession.getCurrentStoreRole()) == StoreListModel.DIY) {
					storeController.diySalesReporte(
							new SalesReporteUpdateView(
									SaleReportHistoryUnuploadFragment.this
											.getActivity(), entity), barcode,
							pic_loc);

				} else {
					String currentProductTypeId = entity
							.getString(SaleReportRecord.PRO_BRAND_ID);
					String currentProductModelId = entity
							.getString(SaleReportRecord.PRO_MODEL_ID);

					storeController.oemSalesReporte(
							new SalesReporteUpdateView(
									SaleReportHistoryUnuploadFragment.this
											.getActivity(), entity), barcode,
							currentProductTypeId, currentProductModelId,
							pic_loc);
				}

			}

		}
	}

	private class S3DeletePictureTask extends
			AsyncTask<String, Void, S3TaskResult> {

		protected void onPreExecute() {

		}

		
		protected S3TaskResult doInBackground(String... uris) {

			S3TaskResult result = new S3TaskResult();
			try {
				s3Client.deleteObject(Constants.BUCKET_PICTURE_NAME, uris[0]);
			} catch (Exception exception) {
				TmpDataList.clear();
				result.setErrorMessage(exception.getMessage());
				mViewHelper.showBTNDialog(result.getErrorMessage());
			}

			return result;
		}

		protected void onPostExecute(S3TaskResult result) {
			Loger.e(result.getErrorMessage());

		}
	}

	private class SalesReporteUpdateView extends StoreCommonUpdateView<Boolean> {

		private MapEntity entity;

		public SalesReporteUpdateView(Context context, MapEntity entity) {
			super(context);
			this.entity = entity;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			loadingView.showLoading();
		}

		@Override
		public void onPostExecute(Boolean result) {

			loadingView.hideLoading();
			try {
				Loger.d("SalesReporteUpdateView " + i++);
				deleteReport(entity);
				dataList.remove(entity);
				TmpDataList.remove(0);
				f1Changed=true;
				if (TmpDataList.size()!=0) {
					upSalesReporte(TmpDataList.get(0));
				}else {
					viewHelper.showBTNDialog("上传完毕！");
					inUpload=false;
				}
				reportHistoryListAdapter.notifyDataSetChanged();
			} catch (IException e) {
				e.printStackTrace();
				viewHelper.showErrorDialog(e);
			}
		}

		@Override
		public void handleException(IException ex) {
			// 执行删除object操作
			TmpDataList.clear();
			loadingView.hideLoading();
			viewHelper.showBTNDialog(ex.getMessage(), "申报结果");
			inUpload=false;
			new S3DeletePictureTask().execute(entity
					.getString(SaleReportRecord.PIC_PATH));
		}
	}

}
