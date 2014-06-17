package com.intel.store.controller;

import java.util.List;
import java.util.Random;

import com.intel.store.model.ChangePasswordModel;
import com.intel.store.model.CheckTarVersionModel;
import com.intel.store.model.CheckVersionModel;
import com.intel.store.model.IrepModel;
import com.intel.store.model.ProductTypeModel;
import com.intel.store.model.SalesCountModel;
import com.intel.store.model.SalesCountModel.StoreSaleCount;
import com.intel.store.model.SalesReportModel;
import com.intel.store.model.StoreIntegralModel;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.StoreSession;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;

public class StoreController extends BaseController {

	public static final int GETSALESCOUNTBYID = 1;
	public static final int GETSTOREINTEGRALBYID = 2;
	public static final int CHECKTARVERSION = 3;
	public static final int Password_Change_KEY = 4;
	public static final int STORELIST = 5;
	public static final int GETALLPRODUCT = 6;
	public static final int REMOTE_UPGRADE_KEY = 7;
	public static final int OEM_SALES_REPORTE = 8;
	public static final int DIY_SALES_REPORTE = 9;

	public void checkVersion(
			final UpdateViewAsyncCallback<MapEntity> viewUpdateCallback) {
		doAsyncTask(REMOTE_UPGRADE_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, MapEntity>() {
					@Override
					public MapEntity doAsyncTask(String... params)
							throws IException {
						return new CheckVersionModel().checkVersion();
					}
				});
	}

	public void cancelCheckVersion() {
		cancel(REMOTE_UPGRADE_KEY);
	}

	public void getStoreByRepId(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			String sr_id) {
		final StoreListModel storeListModel = new StoreListModel();
		doAsyncTask(STORELIST, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return storeListModel.getStoreBySrId(params[0]);
					}
				}, sr_id);

	}

	/**
	 * 获得门店销量
	 * 
	 * @param viewUpdateCallback
	 * @param storeId
	 */
	public void getSalesCountById(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			String storeId) {
		final SalesCountModel salesCountModel = new SalesCountModel();

		doAsyncTask(GETSALESCOUNTBYID, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return salesCountModel.getSalesCountById(params[0]);
					}
				}, storeId);
	}

	/**
	 * 获得门店积分
	 * 
	 * @param viewUpdateCallback
	 * @param storeId
	 */

	public void getStoreIntegralById(
			UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			String storeId) {
		final StoreIntegralModel storeIntegralModel = new StoreIntegralModel();

		doAsyncTask(GETSTOREINTEGRALBYID, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return storeIntegralModel
								.getStoreIntegralById(params[0]);
					}
				}, storeId);
	}

	public void checkTarVersion(
			UpdateViewAsyncCallback<MapEntity> viewUpdateCallback,
			final String dataVersion) {
		doAsyncTask(CHECKTARVERSION, viewUpdateCallback,
				new DoAsyncTaskCallback<String, MapEntity>() {
					@Override
					public MapEntity doAsyncTask(String... params)
							throws IException {
						return new CheckTarVersionModel()
								.checkTarVersion(dataVersion);
					}
				});
	}

	public void cancelCheckTarVersion() {
		cancel(CHECKTARVERSION);
	}

	public void changePasswordFromRemote(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			final String username, final String oldpwd, final String newpwd) {
		final ChangePasswordModel changePasswordModel = new ChangePasswordModel();
		doAsyncTask(Password_Change_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return changePasswordModel.changePasswordFromRemote(
								username, oldpwd, newpwd);
					}
				}, username, oldpwd, newpwd);

	}

	public boolean exitLocalProductType() {
		ProductTypeModel productTypeModel = new ProductTypeModel();
		try {
			return productTypeModel.exitLocalProductType();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void saveProductType(List<MapEntity> data) {
		ProductTypeModel productTypeModel = new ProductTypeModel();
		productTypeModel.saveProductType(data);
	}

	public void getProductTypeFromeRemote(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			final String getType) {
		final ProductTypeModel productTypeModel = new ProductTypeModel();
		doAsyncTask(GETALLPRODUCT, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return productTypeModel
								.getAllProductInfoFromRemote(params[0]);
					}
				}, getType);

	}

	public void oemSalesReporte(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String barcode, String brand_id, String mdl_id, String pic_loc) {
		final SalesReportModel salesReportModel = new SalesReportModel();
		doAsyncTask(OEM_SALES_REPORTE, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return salesReportModel.oemSalesReporte(params[0],
								params[1], params[2], params[3], params[4],
								params[5]);
					}
				}, StoreSession.getCurrentStoreId(),
				StoreSession.getRepID(), barcode, brand_id, mdl_id,
				pic_loc);
	}

	public void diySalesReporte(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String barcode,String pic_loc) {
		final SalesReportModel salesReportModel = new SalesReportModel();
		doAsyncTask(new Random().nextInt(1000000)+"", viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return salesReportModel.diySalesReporte(params[0],
								params[1], params[2], params[3]);
					}
				}, StoreSession.getCurrentStoreId(),
				StoreSession.getRepID(), barcode,pic_loc);

	}

	public void validateBarcode(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			String barcode) {
		final SalesReportModel salesReportModel = new SalesReportModel();
		doAsyncTask(DIY_SALES_REPORTE, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return salesReportModel.validateBarcode(params[0]);
					}
				}, barcode);

	}

	public void irepLogin(UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String username, String password) {
		final IrepModel irepModel = new IrepModel();
		doAsyncTask(DIY_SALES_REPORTE, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return irepModel.irepLogin(params[0],params[1]);
					}
				}, username,password);
	}

	public void getAllStoreSalesCount(
			final UpdateViewAsyncCallback<List<StoreSaleCount>> viewUpdateCallback,
			String sr_id) {
		final SalesCountModel model = new SalesCountModel();
		doAsyncTask(STORELIST, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<StoreSaleCount>>() {
					@Override
					public List<StoreSaleCount> doAsyncTask(String... params)
							throws IException {
						return model.getAllStoreSalesCount(params[0]);
					}
				}, sr_id);

	}

	public void getAllStoreIntegral(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			String sr_id) {
		final StoreIntegralModel model = new StoreIntegralModel();
		doAsyncTask(STORELIST, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						return model.getAllStoreIntegral(params[0]);
					}
				}, sr_id);

	}

}
