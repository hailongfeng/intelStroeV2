package com.intel.store.test;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;

import com.intel.store.R;
import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.intel.store.dao.local.LocalSaleReportDao;
import com.intel.store.model.StoreListModel;
import com.intel.store.view.BaseActivity;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class LocalDBTestActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_dbtest);
		List<MapEntity> records=new ArrayList<MapEntity>();
		
		LocalSaleReportDao dao=new LocalSaleReportDao();
		for (int i = 0; i < 20; i++) {
			MapEntity object=new MapEntity();
			object.setValue(SaleReportRecord.USER_NAME, "1866103666");
			object.setValue(SaleReportRecord.STORE_ID, "110");
			object.setValue(SaleReportRecord.STORE_NAME, "无锡店");
			object.setValue(SaleReportRecord.STORE_TYPE, StoreListModel.OEM);
			object.setValue(SaleReportRecord.PRO_BRAND_ID, 10);
			object.setValue(SaleReportRecord.PRO_BRAND_NAME, "惠普"+i);
			object.setValue(SaleReportRecord.PRO_MODEL_ID, "120");
			object.setValue(SaleReportRecord.PRO_MODEL_NAME, "惠普2563"+i);
			object.setValue(SaleReportRecord.PIC_PATH, "mnt/pic/aaa.png");
			object.setValue(SaleReportRecord.SERIAL_NUMBER, ""+i);
			object.setValue(SaleReportRecord.DATA_TIME, System.currentTimeMillis());
			records.add(object);
		}
		try {
			dao.insertSaleReportRecord(records);
			List<MapEntity> data=dao.getSaleReportRecords("1866103666","110");
			Loger.e(data.size()+"");
		} catch (DBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.local_dbtest, menu);
		return true;
	}

}
