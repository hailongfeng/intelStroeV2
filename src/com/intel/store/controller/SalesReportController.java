package com.intel.store.controller;

import java.util.List;

import com.intel.store.model.SalesReportModel;
import com.intel.store.model.StoreListModel;
import com.intel.store.util.StoreSession;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class SalesReportController extends BaseController {
	private static final int  GETSALESRECORD=1;
	public void getSalesReocrd(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback) {
		final SalesReportModel salesReportModel = new SalesReportModel();

		doAsyncTask(GETSALESRECORD, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public List<MapEntity> doAsyncTask(String... params)
							throws IException {
						int cityRole=Integer.valueOf(StoreSession.getCurrentStoreRole());
						Loger.e("city role="+cityRole);
						switch (cityRole) {
						case StoreListModel.OEM:
							return salesReportModel.listOemSaleData();
						case StoreListModel.DIY:
							return salesReportModel.listDiySaleData();
						default:
							Loger.e("city role is empty");
							break;
						}
						return null;
					}
				}, "");
	}

}
