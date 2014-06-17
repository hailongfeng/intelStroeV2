package com.intel.store.dao.local;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.intel.store.dao.local.LocalDBConstants.SaleReportRecord;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @Title: LocalSaleReportDao.java
 * @Package: com.intel.store.dao.local
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2014年4月15日 上午9:36:49
 * @version: V1.0
 */
public class LocalSaleReportDao extends LocalCommanBaseDao {

	public void insertSaleReportRecord(List<MapEntity> records)
			throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_SALE_REPORT_RECORD];
		SQLiteDatabase db = dbHandler.getDb();
		// ContentValues contentValues =new ContentValues();
		// contentValues.put(key, value);
		// dbHandler.insert(table_name, contentValues);

		db.execSQL("PRAGMA cache_size=1000;");
		db.beginTransaction();
		Loger.e(db.getVersion() + "");
		try {
			String sql = "insert into " + table_name + "("
					+ SaleReportRecord.COLUMNS[SaleReportRecord.USER_NAME]
					+ "," + SaleReportRecord.COLUMNS[SaleReportRecord.STORE_ID]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.STORE_NAME]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.STORE_TYPE]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_ID]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_NAME]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_ID]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_NAME]
					+ "," + SaleReportRecord.COLUMNS[SaleReportRecord.PIC_PATH]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.SERIAL_NUMBER]
					+ ","
					+ SaleReportRecord.COLUMNS[SaleReportRecord.DATA_TIME]
					+ ") " + " values(?,?,?,?,?,?,?,?,?,?,?);";
			for (MapEntity mapEntity : records) {
				String user_name = mapEntity
						.getString(SaleReportRecord.USER_NAME);
				String store_id = mapEntity
						.getString(SaleReportRecord.STORE_ID);
				String store_name = mapEntity
						.getString(SaleReportRecord.STORE_NAME);
				String store_type = mapEntity
						.getString(SaleReportRecord.STORE_TYPE);
				String pro_brand_id = mapEntity
						.getString(SaleReportRecord.PRO_BRAND_ID);
				String pro_brand_name = mapEntity
						.getString(SaleReportRecord.PRO_BRAND_NAME);
				String pro_model_id = mapEntity
						.getString(SaleReportRecord.PRO_MODEL_ID);
				String pro_model_name = mapEntity
						.getString(SaleReportRecord.PRO_MODEL_NAME);
				String pic_path = mapEntity
						.getString(SaleReportRecord.PIC_PATH);
				String serial_number = mapEntity
						.getString(SaleReportRecord.SERIAL_NUMBER);
				String data_time = mapEntity
						.getString(SaleReportRecord.DATA_TIME);

				Loger.d("pro_brand_name"+pro_brand_name);
				Loger.d("pro_model_name"+pro_model_name);
				SQLiteStatement sqlListStatment = db.compileStatement(sql);
				sqlListStatment.bindString(1, user_name);
				sqlListStatment.bindString(2, store_id);
				sqlListStatment.bindString(3, store_name);
				sqlListStatment.bindString(4, store_type);
				sqlListStatment.bindString(5, pro_brand_id);
				sqlListStatment.bindString(6, pro_brand_name);
				sqlListStatment.bindString(7, pro_model_id);
				sqlListStatment.bindString(8, pro_model_name);
				sqlListStatment.bindString(9, pic_path);
				sqlListStatment.bindString(10, serial_number);
				sqlListStatment.bindString(11, data_time);

				sqlListStatment.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (android.database.SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}

	}

	public ArrayList<MapEntity> getSaleReportRecords(String userName,
			String storeId) throws DBException {
		String[] columns = { SaleReportRecord.COLUMNS[LocalDBConstants.ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.USER_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_TYPE],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.PIC_PATH],
				SaleReportRecord.COLUMNS[SaleReportRecord.SERIAL_NUMBER],
				SaleReportRecord.COLUMNS[SaleReportRecord.DATA_TIME]

		};
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_SALE_REPORT_RECORD];

		String selection = SaleReportRecord.COLUMNS[SaleReportRecord.USER_NAME]
				+ " = ? and "
				+ SaleReportRecord.COLUMNS[SaleReportRecord.STORE_ID] + " = ?";
		String[] selectionArgs = new String[] { userName, storeId };

		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				SaleReportRecord.COLUMNS[SaleReportRecord.DATA_TIME] + " ASC ",
				null);

		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdCategory = new MapEntity();
				// 查询id和名称
				prdCategory.setValue(LocalDBConstants.ID, cursor.getInt(0));
				prdCategory.setValue(SaleReportRecord.USER_NAME,
						cursor.getString(1));
				prdCategory.setValue(SaleReportRecord.STORE_ID,
						cursor.getString(2));
				prdCategory.setValue(SaleReportRecord.STORE_NAME,
						cursor.getString(3));
				prdCategory.setValue(SaleReportRecord.STORE_TYPE,
						cursor.getString(4));
				prdCategory.setValue(SaleReportRecord.PRO_BRAND_ID,
						cursor.getString(5));
				prdCategory.setValue(SaleReportRecord.PRO_BRAND_NAME,
						cursor.getString(6));
				prdCategory.setValue(SaleReportRecord.PRO_MODEL_ID,
						cursor.getString(7));
				prdCategory.setValue(SaleReportRecord.PRO_MODEL_NAME,
						cursor.getString(8));
				prdCategory.setValue(SaleReportRecord.PIC_PATH,
						cursor.getString(9));
				prdCategory.setValue(SaleReportRecord.SERIAL_NUMBER,
						cursor.getString(10));
				prdCategory.setValue(SaleReportRecord.DATA_TIME,
						cursor.getString(11));
				dataList.add(prdCategory);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

	public MapEntity getOneSaleReportRecord(String recordId) throws DBException {
		String[] columns = { SaleReportRecord.COLUMNS[LocalDBConstants.ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.USER_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.STORE_TYPE],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_BRAND_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_ID],
				SaleReportRecord.COLUMNS[SaleReportRecord.PRO_MODEL_NAME],
				SaleReportRecord.COLUMNS[SaleReportRecord.PIC_PATH],
				SaleReportRecord.COLUMNS[SaleReportRecord.SERIAL_NUMBER],
				SaleReportRecord.COLUMNS[SaleReportRecord.DATA_TIME]

		};
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_SALE_REPORT_RECORD];

		String selection = SaleReportRecord.COLUMNS[LocalDBConstants.ID]
				+ " = ? ";
		String[] selectionArgs = new String[] { recordId };

		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null, null, null);

		MapEntity prdCategory = null;
		if (cursor.moveToFirst()) {
			// 查询id和名称
			prdCategory = new MapEntity();
			prdCategory.setValue(LocalDBConstants.ID, cursor.getInt(0));
			prdCategory.setValue(SaleReportRecord.USER_NAME,
					cursor.getString(1));
			prdCategory
					.setValue(SaleReportRecord.STORE_ID, cursor.getString(2));
			prdCategory.setValue(SaleReportRecord.STORE_NAME,
					cursor.getString(3));
			prdCategory.setValue(SaleReportRecord.STORE_TYPE,
					cursor.getString(4));
			prdCategory.setValue(SaleReportRecord.PRO_BRAND_ID,
					cursor.getString(5));
			prdCategory.setValue(SaleReportRecord.PRO_BRAND_NAME,
					cursor.getString(6));
			prdCategory.setValue(SaleReportRecord.PRO_MODEL_ID,
					cursor.getString(7));
			prdCategory.setValue(SaleReportRecord.PRO_MODEL_NAME,
					cursor.getString(8));
			prdCategory
					.setValue(SaleReportRecord.PIC_PATH, cursor.getString(9));
			prdCategory.setValue(SaleReportRecord.SERIAL_NUMBER,
					cursor.getString(10));
			prdCategory.setValue(SaleReportRecord.DATA_TIME,
					cursor.getString(11));
			Loger.d("1pro_brand_name"+cursor.getString(6));
			Loger.d("1pro_model_name"+cursor.getString(8));
		}
		cursor.close();
		return prdCategory;
	}

	public void deleteSaleReportRecord(String recordId) throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_SALE_REPORT_RECORD];
		String selection = SaleReportRecord.COLUMNS[LocalDBConstants.ID]
				+ " = ? ";
		String[] selectionArgs = new String[] { recordId };
		dbHandler.delete(table_name, selection, selectionArgs);
	}
}
