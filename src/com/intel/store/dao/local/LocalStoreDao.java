/**  
 * @Title: LocalStoreDao.java 
 * @Package: com.intel.store.dao.local 
 * @Description:(用一句话描述该文件做什么) 
 * @author: fenghl 
 * @date: 2013年12月27日 上午9:04:11 
 * @version: V1.0  
 */
package com.intel.store.dao.local;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.intel.store.dao.local.LocalDBConstants.PrdCategory;
import com.intel.store.model.ProductTypeModel;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;

/**
 * @Title: LocalStoreDao.java
 * @Package: com.intel.store.dao.local
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2013年12月27日 上午9:04:11
 * @version: V1.0
 */
public class LocalStoreDao extends LocalCommanBaseDao {
	// String table_name;
	private static LocalStoreDao localLoginDao = new LocalStoreDao();

	private LocalStoreDao() {
		super();
	}

	public static LocalStoreDao getInstance() {
		return localLoginDao;
	}

	public void insertPrdCategory(List<MapEntity> prdCategoryList)
			throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];
		delAllPrdCategory();

		SQLiteDatabase db = dbHandler.getDb();
		db.execSQL("PRAGMA cache_size=1000;");
		db.beginTransaction();

		try {
			String sql = "insert into " + table_name + "("
					+ PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID] + ","
					+ PrdCategory.COLUMNS[PrdCategory.PRD_CATEGORY] + ","
					+ PrdCategory.COLUMNS[PrdCategory.BRND_ID] + ","
					+ PrdCategory.COLUMNS[PrdCategory.PRD_BRND] + ","
					+ PrdCategory.COLUMNS[PrdCategory.MDL_ID] + ","
					+ PrdCategory.COLUMNS[PrdCategory.PRD_MODEL] + ") "
					+ " values(?,?,?,?,?,?);";
			for (MapEntity mapEntity : prdCategoryList) {
				String prd_cat_id = mapEntity
						.getString(ProductTypeModel.PRD_CAT_ID);
				String prd_cat_nm = mapEntity
						.getString(ProductTypeModel.PRD_CAT_NM);
				String brnd_id = mapEntity.getString(ProductTypeModel.BRND_ID);
				String brnd_nm = mapEntity.getString(ProductTypeModel.BRND_NM);
				String mdl_id = mapEntity.getString(ProductTypeModel.MDL_ID);
				String mdl_nm = mapEntity.getString(ProductTypeModel.MDL_NM);

				SQLiteStatement sqlListStatment = db.compileStatement(sql);
				sqlListStatment.bindString(1, prd_cat_id);
				sqlListStatment.bindString(2, prd_cat_nm);
				sqlListStatment.bindString(3, brnd_id);
				sqlListStatment.bindString(4, brnd_nm);
				sqlListStatment.bindString(5, mdl_id);
				sqlListStatment.bindString(6, mdl_nm);
				sqlListStatment.executeInsert();
				// db.execSQL(sql);
			}
			db.setTransactionSuccessful();
		} catch (android.database.SQLException e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
			// db.close();
		}
		// for (MapEntity mapEntity : prdCategoryList) {
		// String[] contents = new String[PrdCategory.COLUMNS.length];
		// contents[PrdCategory.PRD_CAT_ID] = mapEntity
		// .getString(ProductTypeModel.PRD_CAT_ID);
		// contents[PrdCategory.PRD_CATEGORY] = mapEntity
		// .getString(ProductTypeModel.PRD_CAT_NM);
		// contents[PrdCategory.BRND_ID] = mapEntity
		// .getString(ProductTypeModel.BRND_ID);
		// contents[PrdCategory.PRD_BRND] = mapEntity
		// .getString(ProductTypeModel.BRND_NM);
		// contents[PrdCategory.MDL_ID] = mapEntity
		// .getString(ProductTypeModel.MDL_ID);
		// contents[PrdCategory.PRD_MODEL] = mapEntity
		// .getString(ProductTypeModel.MDL_NM);
		// insert(table_name, PrdCategory.COLUMNS, contents,
		// PrdCategory.PRD_CAT_ID);
		// }
	}

	public ArrayList<MapEntity> getProductCategory() throws DBException {
		String[] columns = { PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_CATEGORY] };
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];

		Cursor cursor = dbHandler.query(true, table_name, columns, null, null,
				null, null, PrdCategory.COLUMNS[PrdCategory.PRD_CATEGORY]
						+ " ASC", null);
		// Cursor cursor = query(table_name,
		// PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID],PrdCategory.COLUMNS[PrdCategory.PRD_CATEGORY],null,null);
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdCategory = new MapEntity();
				// 查询id和名称
				prdCategory.setValue(ProductTypeModel.COMMON_ID,
						cursor.getString(0));
				prdCategory.setValue(ProductTypeModel.COMMON_NAME,
						cursor.getString(1));
				dataList.add(prdCategory);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

	public ArrayList<MapEntity> getProductBrnd(String strCatId)
			throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];

		String[] columns = { PrdCategory.COLUMNS[PrdCategory.BRND_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_BRND],
				PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID] };
		String selection = PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID]
				+ " = ? ";
		String[] selectionArgs = new String[] { strCatId };
		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				PrdCategory.COLUMNS[PrdCategory.PRD_BRND] + " ASC", null);

		// Cursor cursor = query(table_name,
		// PrdCategory.COLUMNS[PrdCategory.BRND_ID],PrdCategory.COLUMNS[PrdCategory.PRD_BRND],
		// PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID],strCatId,null);
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdBrnd = new MapEntity();
				prdBrnd.setValue(ProductTypeModel.COMMON_ID,
						cursor.getString(0));
				prdBrnd.setValue(ProductTypeModel.COMMON_NAME,
						cursor.getString(1));
				dataList.add(prdBrnd);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

	public ArrayList<MapEntity> getProductMolde(String strCatId,
			String strBrndId) throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];

		String[] columns = { PrdCategory.COLUMNS[PrdCategory.MDL_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL],
				PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID],
				PrdCategory.COLUMNS[PrdCategory.BRND_ID] };
		String selection = PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID]
				+ " = ? and  " + PrdCategory.COLUMNS[PrdCategory.BRND_ID]
				+ " = ?";
		String[] selectionArgs = new String[] { strCatId, strBrndId };
		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL] + " ASC", null);

		// Cursor cursor = query(table_name,
		// PrdCategory.COLUMNS[PrdCategory.MDL_ID],
		// PrdCategory.COLUMNS[PrdCategory.PRD_MODEL],
		// PrdCategory.COLUMNS[PrdCategory.PRD_CAT_ID], strCatId,
		// PrdCategory.COLUMNS[PrdCategory.BRND_ID], strBrndId);
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdModel = new MapEntity();
				prdModel.setValue(ProductTypeModel.COMMON_ID,
						cursor.getString(0));
				prdModel.setValue(ProductTypeModel.COMMON_NAME,
						cursor.getString(1));
				dataList.add(prdModel);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}


	public void delAllPrdCategory() throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];
		dbHandler.delete(table_name, null, null);
	}

	public Boolean exitLocalProductType() {
		String sql = "select count(*) as mycount from "
				+ LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];
		Cursor cursor = dbHandler.getDb().rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			int count = cursor.getInt(cursor.getColumnIndex("mycount"));
			if (count > 0) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<MapEntity> getProductByName(String modelName)
			throws DBException {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];

		String[] columns = { PrdCategory.COLUMNS[PrdCategory.MDL_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL]};
		String selection = null;
		String[] selectionArgs = null;
		if (modelName != null && !"".equals(modelName)) {
			selection = PrdCategory.COLUMNS[PrdCategory.PRD_MODEL]
					+ " like ?";
			selectionArgs = new String[] { "%"+modelName+"%" };
		}

		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL] + " ASC", null);
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdModel = new MapEntity();
				prdModel.setValue(PrdCategory.MDL_ID,
						cursor.getString(0));
				prdModel.setValue(PrdCategory.PRD_MODEL,
						cursor.getString(1));
				dataList.add(prdModel);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

	public List<MapEntity> getAllBrndFromLocal() {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];

		String[] columns = { PrdCategory.COLUMNS[PrdCategory.BRND_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_BRND]};
		String selection = null;
		String[] selectionArgs = null;

		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				PrdCategory.COLUMNS[PrdCategory.PRD_BRND] + " ASC", null);
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdBrnd = new MapEntity();
				prdBrnd.setValue(PrdCategory.BRND_ID,
						cursor.getString(0));
				prdBrnd.setValue(PrdCategory.PRD_BRND,
						cursor.getString(1));
				dataList.add(prdBrnd);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

	public List<MapEntity> getModleByBrndId(String brndId) {
		String table_name = LocalDBConstants.TABLE_NAMES[LocalDBConstants.TABLE_PRD_CATEGORY];
		String[] columns = { PrdCategory.COLUMNS[PrdCategory.MDL_ID],
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL]};
		String selection = PrdCategory.COLUMNS[PrdCategory.BRND_ID]
				+ " = ?";
		String[] selectionArgs = new String[] { brndId};
		ArrayList<MapEntity> dataList = new ArrayList<MapEntity>();
		Cursor cursor = dbHandler.query(true, table_name, columns, selection,
				selectionArgs, null, null,
				PrdCategory.COLUMNS[PrdCategory.PRD_MODEL] + " ASC", null);
		
		if (cursor.moveToFirst()) {
			do {
				MapEntity prdModel = new MapEntity();
				prdModel.setValue(PrdCategory.MDL_ID,
						cursor.getString(0));
				prdModel.setValue(PrdCategory.PRD_MODEL,
						cursor.getString(1));
				dataList.add(prdModel);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return dataList;
	}

}
