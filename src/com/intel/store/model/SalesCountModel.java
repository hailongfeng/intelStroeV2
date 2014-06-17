package com.intel.store.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.dao.remote.StoreRemoteDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class SalesCountModel extends StoreBaseModel {

	public static final int YYYYQQ = 1;
	public static final int ISCURRQUARTER = 2;
	public static final int STOR_ID = 3;
	public static final int CITY_TYPE = 4;
	public static final int POR_NAME = 5;
	public static final int POR = 6;
	public static final int SO = 7;
	public static final int ACHIEVRATE = 8;
	public static final int HIGH_END_POR_NAME = 9;
	public static final int HIGH_END_POR = 11;
	public static final int HIGH_END_SO = 10;
	public static final int HIGHACHIEVRATE = 12;

	public SalesCountModel() {
		// remoteStoreListDao = new RemoteStoreListDao();
	}

	public List<MapEntity> getSalesCountById(String storeId)
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
			JSONObject data = preParseResponse(response);
			JSONArray storeData = data.getJSONArray("data");
			for (int i = 0; i < storeData.length(); i++) {
				JSONObject store = storeData.getJSONObject(i);
				MapEntity mapEntity = new MapEntity();
				mapEntity.setValue(YYYYQQ, store.get("yyyyqq"));
				mapEntity.setValue(ISCURRQUARTER, store.get("isCurrQuarter"));
				mapEntity.setValue(STOR_ID, store.get("stor_id"));
				mapEntity.setValue(CITY_TYPE, store.get("city_type"));
				mapEntity.setValue(POR_NAME, store.get("por_name"));
				mapEntity.setValue(POR, store.get("por"));
				mapEntity.setValue(SO, store.get("so"));
				mapEntity.setValue(ACHIEVRATE, store.get("achievRate"));
				mapEntity.setValue(HIGH_END_POR_NAME,
						store.get("high_end_por_name"));
				mapEntity.setValue(HIGH_END_POR, store.get("high_end_por"));
				mapEntity.setValue(HIGH_END_SO, store.get("high_end_so"));
				mapEntity.setValue(HIGHACHIEVRATE, store.get("highAchievRate"));
				dataList.add(mapEntity);
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;
	}

	public List<StoreSaleCount> getAllStoreSalesCount(String userId)
			throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao = new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.getAllStoreSalesCount(userId);
		String preResponse = preParseHttpResult(httpResult);
		Loger.d(preResponse);

		if (null == preResponse || "".equals(preResponse)) {
			throw new ServerException();
		}
		 List<StoreSaleCount> storeSaleCountList=new ArrayList<SalesCountModel.StoreSaleCount>();
		try {
			JSONObject preData = new JSONObject(preResponse);

			String myHashMap = preData.getString("myHashMap");
			
			Loger.d(myHashMap);
			
			// 相当于正常的json
			JSONObject jsonObject1 =new JSONObject(myHashMap);
			
			if (jsonObject1.has("status")) {
				
				JSONObject status = jsonObject1.getJSONObject("status").getJSONObject("myHashMap");
				if ("true".equals(status.getString("success"))) {
					//return jsonRes;
				} else {
					throw new ServerException(status.getString("errorCode"),
							status.getString("errorMsg"));
				}
			} else {
				throw new ServerException("-1", StoreApplication.getAppContext().getString(R.string.comm_txt_server_error));
			}
			
			JSONObject jsonObject2 = (JSONObject) jsonObject1.get("data");
			JSONObject jsonObject3 = (JSONObject) jsonObject2.get("myHashMap");
			
			Iterator it = jsonObject3.keys();
			// 每一个门店
			while (it.hasNext()) {
				StoreSaleCount oneStroe = new StoreSaleCount();
				oneStroe.storeName = "电脑";
				
				JSONArray ja1 = jsonObject3.getJSONArray(it.next().toString());
				
				for (int i = 0; i < ja1.length(); i++) {
					// 每一个季度
					JSONObject one = ja1.getJSONObject(i).getJSONObject("myHashMap");
					
					SaleQuarter quarter = new SaleQuarter();
					quarter.yyyyqq = one.getString("yyyyqq");
					quarter.Stor_id = one.getString("Stor_id");
					quarter.City_type = one.getString("City_type");
					quarter.por_name = one.getString("por_name");
					quarter.POr = one.getString("POr");
					quarter.SO = one.getString("SO");
					quarter.high_end_por_name = one
							.getString("high_end_por_name");
					quarter.High_end_por = one.getString("High_end_por");
					quarter.High_end_SO = one.getString("High_end_SO");
					
					oneStroe.storeName=one.getString("stor_nm");
					
					oneStroe.data.add(quarter);
				}
				storeSaleCountList.add(oneStroe);
			}

		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return storeSaleCountList;
	}

	public class SaleQuarter {
		public String yyyyqq;
		public String Stor_id;
		public String City_type;
		public String por_name;
		public String POr;
		public String SO;
		public String high_end_por_name;
		public String High_end_por;
		public String High_end_SO;
	}

	public class StoreSaleCount {
		public String storeName;
		public List<SaleQuarter> data;
		public StoreSaleCount() {
			super();
			data=new ArrayList<SalesCountModel.SaleQuarter>();
		}
		
		
	}
}
