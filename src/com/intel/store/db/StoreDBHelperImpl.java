package com.intel.store.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.pactera.framework.dao.local.DBHelperImpl;
import com.pactera.framework.util.Loger;

/**
 * @Title: StoreDBHelperImpl.java
 * @Package: com.intel.store.db
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2014年4月15日 下午1:46:12
 * @version: V1.0
 */
public class StoreDBHelperImpl extends DBHelperImpl {

	public StoreDBHelperImpl(String name, int version,
			List<String> databaseCreate, Context context, CursorFactory factory) {
		super(name, version, databaseCreate, context, factory);
	}

	public StoreDBHelperImpl(String name, int version,
			List<String> databaseCreate, Context context) {
		super(name, version, databaseCreate, context);
	}

	public StoreDBHelperImpl(String name, int version,
			List<String> databaseCreate) {
		super(name, version, databaseCreate);
	}

	@Override
	protected void UpgradeDB(SQLiteDatabase db, int oldVersion, int newVersion) {
		super.UpgradeDB(db, oldVersion, newVersion);
		Loger.d("Upgrading database from version " + oldVersion + " to "
				+ newVersion + ".");
		db.beginTransaction();
		try {
			switch (oldVersion) {
			case 1:
				if (newVersion <=1) {
					break;
				} else {
					upgradeDatabaseToVersion1(db);
				}
				break;

			default:
				break;
			}
			db.setTransactionSuccessful();
		} catch (Throwable ex) {
			Loger.e(ex.getMessage());
		} finally {
			db.endTransaction();
		}
		return;

	}

	// 更新到version2 1->2
	private void upgradeDatabaseToVersion1(SQLiteDatabase db) {
		SaleReportRecord saleReportRecord = new SaleReportRecord();
		db.execSQL(saleReportRecord.produceCreateSQL());
	}
}
