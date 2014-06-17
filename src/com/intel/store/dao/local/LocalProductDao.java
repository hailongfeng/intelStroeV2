/**  
 * @Package com.intel.store.dao.local 
 * @FileName: LocalProductDao.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月24日 下午2:13:27 
 * @version V1.0  
 */
package com.intel.store.dao.local;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.intel.store.StoreApplication;
import com.intel.store.util.Constants;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @ClassName: LocalProductDao
 * @Description: 该类没有继承通用本地数据库访问接口，因为要使用的数据来自网络上下载的数据库
 * @author fenghl
 * @date 2013年10月24日 下午2:13:27
 */
public class LocalProductDao {
	private SQLiteDatabase db = null;
	private final String DB_NAME = "product.db";
	private final String TABLE_NAME = "product";

	public static final int ID = 1;
	public static final int PRO_NM = 2;
	public static final int SER = 3;
	public static final int CACHE = 4;
	public static final int CORE_NBR = 5;
	public static final int CODE_NM = 6;
	public static final int THD_CNT = 7;
	public static final int MAX_FREQ = 8;
	public static final int LITH = 9;
	public static final int GFX_MDL = 10;
	public static final int GFX_MAX_FREQ = 11;
	public static final int TYPE = 12;
	public static final int CLK_SPD = 13;
	public static final int URL = 14;

	public LocalProductDao() {
		super();
		String parentPath = StoreApplication.getAppContext().getFilesDir().getParent() + File.separator
				+ Constants.SALE_WORD_DIR;
		File file = new File(parentPath + File.separator + DB_NAME);
		db = SQLiteDatabase.openOrCreateDatabase(file, null);
	}

	public List<MapEntity> getProducts(String selection, String[] selectionArgs) {
		List<MapEntity> list = new ArrayList<MapEntity>();
		try {
			Cursor cursor = db.query(TABLE_NAME, new String[] {}, selection, selectionArgs, null, null, null);

			while (cursor.moveToNext()) {
				MapEntity map = new MapEntity();
				map.setValue(ID, cursor.getString(cursor.getColumnIndex("id")));
				map.setValue(PRO_NM, cursor.getString(cursor.getColumnIndex("pro_nm")));
				map.setValue(SER, cursor.getString(cursor.getColumnIndex("ser")));
				map.setValue(CACHE, cursor.getString(cursor.getColumnIndex("cache")));
				map.setValue(CORE_NBR, cursor.getString(cursor.getColumnIndex("core_nbr")));
				map.setValue(CODE_NM, cursor.getString(cursor.getColumnIndex("code_nm")));
				map.setValue(THD_CNT, cursor.getString(cursor.getColumnIndex("thd_cnt")));
				map.setValue(MAX_FREQ, cursor.getString(cursor.getColumnIndex("max_freq")));
				map.setValue(LITH, cursor.getString(cursor.getColumnIndex("lith")));
				map.setValue(GFX_MDL, cursor.getString(cursor.getColumnIndex("gfx_mdl")));
				map.setValue(GFX_MAX_FREQ, cursor.getString(cursor.getColumnIndex("gfx_max_freq")));
				map.setValue(TYPE, cursor.getString(cursor.getColumnIndex("type")));
				map.setValue(CLK_SPD, cursor.getString(cursor.getColumnIndex("clk_spd")));
				map.setValue(URL, cursor.getString(cursor.getColumnIndex("url")));
				list.add(map);

			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			Loger.e(e.toString());
		}
		return list;
	}

}
