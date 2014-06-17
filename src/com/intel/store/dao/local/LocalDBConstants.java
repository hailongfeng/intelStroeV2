package com.intel.store.dao.local;

import java.util.ArrayList;
import java.util.List;

import com.pactera.framework.dao.local.DBConstants;
import com.pactera.framework.util.Loger;

public class LocalDBConstants extends DBConstants {
	static final int ID = 0;// Table id, said the primary key every table

	// static List<String> DB_DROP_SQL;

    public static final String[] TABLE_NAMES = { "picture_category","product_category","prd_record","sale_report_record"};
	/**
	 * The names of the table array subscript, when the call for more intuitive
	 */
    public static final int TABLE_PICTURE_CATEGORY = 0;//picture category
	public static final int TABLE_PRD_CATEGORY = 1;// product_category
	public static final int TABLE_PRD_RECORD = 2;//prd_record
	public static final int TABLE_SALE_REPORT_RECORD = 3;//sale_report_record

	static {
		DB_CREATE_SQL = new ArrayList<String>();
        PictureCategory pictureCategory = new PictureCategory();
        DB_CREATE_SQL.add(pictureCategory.produceCreateSQL());
		
		PrdCategory prdCategory = new PrdCategory();
		DB_CREATE_SQL.add(prdCategory.produceCreateSQL());
		
		ProductRecord prdRecord = new ProductRecord();
		DB_CREATE_SQL.add(prdRecord.produceCreateSQL());
		
		SaleReportRecord saleReportRecord = new SaleReportRecord();
		DB_CREATE_SQL.add(saleReportRecord.produceCreateSQL());
        
    }
	
	public static List<String> getCreateSqls() {
        DB_CREATE_SQL = new ArrayList<String>();
        
        PictureCategory pictureCategory = new PictureCategory();
        DB_CREATE_SQL.add(pictureCategory.produceCreateSQL());
        
        PrdCategory prdCategory = new PrdCategory();
        DB_CREATE_SQL.add(prdCategory.produceCreateSQL());
        
        ProductRecord prdRecord = new ProductRecord();
		DB_CREATE_SQL.add(prdRecord.produceCreateSQL());
		
		SaleReportRecord saleReportRecord = new SaleReportRecord();
		DB_CREATE_SQL.add(saleReportRecord.produceCreateSQL());
        
        return DB_CREATE_SQL;
	}

    
    public static class PictureCategory implements ITable {
        /** ==Table column name== */
        public static final String[] COLUMNS = { "id", "category_id",
                "category_name", "category_comment", "photo_qty", "display_order_number", "role_id", "city_type"};

        public static final int CATEGORY_ID = 1;
        public static final int CATEGORY_NAME = 2;
        public static final int CATEGORY_COMMENT = 3;
        public static final int PHOTO_QTY = 4;
        public static final int DISPLAY_ORDER_NUMBER = 5;
        public static final int ROLE_ID = 6;
        public static final int CITY_TYPE = 7;

        @Override
        public String produceCreateSQL() {
            final String sql = "CREATE TABLE IF NOT EXISTS "
                    + TABLE_NAMES[TABLE_PICTURE_CATEGORY] + "(" 
                    + COLUMNS[ID]+ " INTEGER PRIMARY KEY, " 
                    + COLUMNS[CATEGORY_ID]+ " TEXT ," 
                    + COLUMNS[CATEGORY_NAME]+ " TEXT ," 
                    + COLUMNS[CATEGORY_COMMENT] + " TEXT ,"
                    + COLUMNS[PHOTO_QTY] + " TEXT ," 
                    + COLUMNS[DISPLAY_ORDER_NUMBER] + " TEXT ,"
                    + COLUMNS[ROLE_ID] + " TEXT ,"
                    + COLUMNS[CITY_TYPE] + " TEXT )";
            //Log.d(getClass().getSimpleName(), " createSQL = " + sql);
            return sql;
        }
    }

	public static class PrdCategory implements ITable {
		/** ==Table column name== */
		public static final String[] COLUMNS = { "id","prd_cat_id", "prd_category","brnd_id","prd_brnd","mdl_id", "prd_model" };

		/** ==Table the index number (starting from 0)== */
		public static final int PRD_CAT_ID = 1;//prd_cat_id
		public static final int PRD_CATEGORY = 2;// pro_category
		public static final int BRND_ID = 3;//brnd_id
		public static final int PRD_BRND = 4;// pro_brnd
		public static final int MDL_ID = 5;//mdl_id
		public static final int PRD_MODEL = 6;// pro_model

		@Override
		public String produceCreateSQL() {
			
			final String sql = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_NAMES[TABLE_PRD_CATEGORY] + "(" + COLUMNS[ID]
					+ " INTEGER PRIMARY KEY, " + COLUMNS[PRD_CAT_ID]
					+ " TEXT NOT NULL ," + COLUMNS[PRD_CATEGORY]
					+ " TEXT NOT NULL ," + COLUMNS[BRND_ID]
					+ " TEXT NOT NULL ," + COLUMNS[PRD_BRND]
					+ " TEXT NOT NULL ," + COLUMNS[MDL_ID]
					+ " TEXT NOT NULL ," + COLUMNS[PRD_MODEL]
					+ " TEXT NOT NULL)";
			//Loger.d(" createSQL = " + sql);
			return sql;
		}
	}
	public static class ProductRecord implements ITable {
		/** ==Table column name== */
		public static final String[] COLUMNS = { "id","model_id","model_name","create_time"};
		
		/** ==Table the index number (starting from 0)== */
		public static final int MODEL_ID = 1;//model_id
		public static final int MODEL_NAME = 2;// model_name
		public static final int CREATE_TIME = 3;// model_name
		
		@Override
		public String produceCreateSQL() {

			final String sql = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_NAMES[TABLE_PRD_RECORD] + "(" 
					+ COLUMNS[ID]+ " INTEGER PRIMARY KEY AUTOINCREMENT, "  
					+ COLUMNS[MODEL_ID]+ " TEXT NOT NULL ,"
					+ COLUMNS[MODEL_NAME]+ " TEXT NOT NULL ,"
					+ COLUMNS[CREATE_TIME]+ " BIGINT NOT NULL)";
			//Loger.d(" createSQL = " + sql);
			return sql;
		}
	}
	public static class SaleReportRecord implements ITable {
		/** ==Table column name== */
		public static final String[] COLUMNS = { "id","user_name","store_id","store_name","store_type","pro_brand_id","pro_brand_name","pro_model_id","pro_model_name","pic_path","serial_number","data_time"};
		
		/** ==Table the index number (starting from 0)== */
		public static final int USER_NAME = 1;
		public static final int STORE_ID = 2;
		public static final int STORE_NAME = 3;
		public static final int STORE_TYPE = 4;
		public static final int PRO_BRAND_ID = 5;
		public static final int PRO_BRAND_NAME = 6;
		public static final int PRO_MODEL_ID = 7;
		public static final int PRO_MODEL_NAME = 8;
		public static final int PIC_PATH = 9;
		public static final int SERIAL_NUMBER = 10;
		public static final int DATA_TIME = 11;
		
		@Override
		public String produceCreateSQL() {

			final String sql = "CREATE TABLE IF NOT EXISTS "
					+ TABLE_NAMES[TABLE_SALE_REPORT_RECORD] + "(" 
					+ COLUMNS[ID]+ " INTEGER PRIMARY KEY AUTOINCREMENT, "  
					+ COLUMNS[USER_NAME]+ " TEXT NOT NULL ,"
					+ COLUMNS[STORE_ID]+ " TEXT NOT NULL ,"
					+ COLUMNS[STORE_NAME]+ " TEXT NOT NULL ,"
					+ COLUMNS[STORE_TYPE]+ " TEXT NOT NULL ,"
					+ COLUMNS[PRO_BRAND_ID]+ " TEXT,"
					+ COLUMNS[PRO_BRAND_NAME]+ " TEXT,"
					+ COLUMNS[PRO_MODEL_ID]+ " TEXT,"
					+ COLUMNS[PRO_MODEL_NAME]+ " TEXT,"
					+ COLUMNS[PIC_PATH]+ " TEXT,"
					+ COLUMNS[SERIAL_NUMBER]+ " TEXT,"
					+ COLUMNS[DATA_TIME]+ " TEXT NOT NULL)";
			Loger.d(" createSQL = " + sql);
			return sql;
		}
	}

}
