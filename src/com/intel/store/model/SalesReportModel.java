package com.intel.store.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.StoreRemoteDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class SalesReportModel extends StoreBaseModel {

	// public static final int YYYYQQ = 1;
	// public static final int ISCURRQUARTER = 2;
/**
 * diy
 *  					"id": 1,
			            "stor_id": 38,
			            "rep_id": 77,
			            "last_upd_dtm": "2014-04-04 10:46:07.86",
			            "barcode": "6911989262553",
			            "pic_loc": "",
			            "rep_nm": "沈云昆"
	oem
	     "id": 9,
            "stor_id": 1360383,
            "rep_id": 78,
            "last_upd_dtm": "2014-04-09 09:16:28.01",
            "brnd_id": 146,
            "mdl_id": 641,
            "barcode": "2R201361A2295",
            "pic_loc": "http://intel.store.oos.ctyunapi.cn/mob_a_1360383_salesReporte_20140409091545_244_96.jpg",
            "rep_nm": "浦莉冬"	
            "Store Name"	            
 * */
	public static final int RTL_BOX_SN = 1;
	public static final int PCSR_NBR = 2;
	public static final int EXTRNL_PRD_NM = 3;
	
	public static final int RECORED_STORE_ID = 4;
	public static final int RECORED_REP_ID = 5;
	public static final int RECORED_LAST_UPD_DTM = 6;
	public static final int RECORED_BARCODE = 7;
	public static final int RECORED_PIC_LOC = 8;
	public static final int RECORED_REP_NM = 9;
	public static final int RECORED_ID = 10;
	public static final int RECORED_BRND_ID = 11;
	public static final int RECORED_MDL_ID= 12;
	public static final int RECORED_STOR_NM= 13;
	public static final int RECORED_BRND_NM= 14;
	public static final int RECORED_MDL_NM= 15;

	public SalesReportModel() {
	}

	public List<MapEntity> salesReporte(String storeId)
			throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.getSalesCountById(storeId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		if (null == response || "".equals(response)) {
			throw new ServerException();
		}
		List<MapEntity> dataList = new ArrayList<MapEntity>();
		try {
			//JSONObject data = 
					preParseResponse(response);

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;
	}

	public boolean oemSalesReporte(String storeId, String roleId,
			String barcode, String brand_id, String mdl_id, String pic_loc)
			throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.oemSalesReporte(storeId, roleId,
				barcode, brand_id, mdl_id, pic_loc);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			//JSONObject data = 
					preParseResponse(response);
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return true;
	}

	public boolean diySalesReporte(String storeId, String rep_id,
			String barcode, String pic_loc) throws TimeoutException,
			NetworkException {
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.diySalesReporte(storeId, rep_id,
				barcode, pic_loc);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
//			JSONObject data =
					preParseResponse(response);
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<MapEntity> listOemSaleData() throws TimeoutException,
			NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.listOemSaleData();
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonArray = data.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonArray.get(i);
					 MapEntity mapEntity = new MapEntity();
					 
			         mapEntity.setValue(RECORED_ID, jsonMessage.optString("id"));
					 mapEntity.setValue(RECORED_STORE_ID, jsonMessage.optString("stor_id"));
					 mapEntity.setValue(RECORED_REP_ID, jsonMessage.optString("rep_id"));
					 mapEntity.setValue(RECORED_LAST_UPD_DTM, jsonMessage.optString("last_upd_dtm").substring(0, 10));
					 mapEntity.setValue(RECORED_BARCODE, jsonMessage.optString("barcode"));
					 mapEntity.setValue(RECORED_PIC_LOC, jsonMessage.optString("pic_loc"));
					 mapEntity.setValue(RECORED_REP_NM, jsonMessage.optString("rep_nm"));
					 mapEntity.setValue(RECORED_STOR_NM, jsonMessage.optString("Store Name"));
					 mapEntity.setValue(RECORED_BRND_NM, jsonMessage.optString("brnd_nm"));
					 mapEntity.setValue(RECORED_MDL_NM, jsonMessage.optString("mdl_nm"));
					 result.add(mapEntity);
				}
			return result;
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return null;
		}
		return null;
	}

	public ArrayList<MapEntity> listDiySaleData() throws TimeoutException,
			NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.listDiySaleData();
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonArray = data.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonArray.get(i);
					 MapEntity mapEntity = new MapEntity();
			         mapEntity.setValue(RECORED_ID, jsonMessage.optString("id"));
					 mapEntity.setValue(RECORED_STORE_ID, jsonMessage.optString("stor_id"));
					 mapEntity.setValue(RECORED_REP_ID, jsonMessage.optString("rep_id"));
					 mapEntity.setValue(RECORED_LAST_UPD_DTM, jsonMessage.optString("last_upd_dtm").substring(0, 10));
					 mapEntity.setValue(RECORED_BARCODE, jsonMessage.optString("barcode"));
					 mapEntity.setValue(RECORED_PIC_LOC, jsonMessage.optString("pic_loc"));
					 mapEntity.setValue(RECORED_REP_NM, jsonMessage.optString("rep_nm"));
					 mapEntity.setValue(RECORED_STOR_NM, jsonMessage.optString("stor_nm"));

					 mapEntity.setValue(RECORED_BRND_ID, jsonMessage.optString("brnd_id"));
					 mapEntity.setValue(RECORED_MDL_ID, jsonMessage.optString("mdl_id"));
					 result.add(mapEntity);
				}
			return result;
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
			return null;
		}
		return null;
	}

	public List<MapEntity> validateBarcode(String barcode)
			throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.validateBarcode(barcode);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		List<MapEntity> dataList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			JSONArray products = data.getJSONArray("data");
			if (products.length() > 0) {
				JSONObject oneCPU = (JSONObject) products.get(0);
				MapEntity entity = new MapEntity();
				entity.setValue(RTL_BOX_SN, oneCPU.getString("rtl_box_sn"));
				entity.setValue(PCSR_NBR, oneCPU.getString("rtl_box_sn"));
				entity.setValue(EXTRNL_PRD_NM,
						oneCPU.getString("extrnl_prd_nm"));
				dataList.add(entity);
			}

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;
	}

}
