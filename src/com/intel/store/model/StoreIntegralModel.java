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

public class StoreIntegralModel extends StoreBaseModel {

	/*
	 
        {
            "stor_id": 99801,
            "yr_qtr_int_nbr": 201302,
            "上季度4S积分总数": 420,
            "奖励总额(元)": 1980,
            "培训达标": "达标",
            "等级变更": "升级"
        }
*/

	public static final int stor_id = 1;
	public static final int yr_qtr_int_nbr = 2;
	public static final int integral_all = 3;
	public static final int bonus = 4;
	public static final int train = 5;
	public static final int lever = 6;
	public static final int stor_nm = 7;

	public StoreIntegralModel() {
		//remoteStoreListDao = new RemoteStoreListDao();
	}


	public List<MapEntity> getStoreIntegralById(String storeId) throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao= new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.getStoreIntegralById(storeId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		if(null==response||"".equals(response)){
			throw new ServerException();
		}
		List<MapEntity> dataList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			JSONArray storeData = data.getJSONArray("data");
			for (int i = 0; i < storeData.length(); i++) {
				JSONObject store = storeData.getJSONObject(i);
				MapEntity mapEntity = new MapEntity();
				mapEntity.setValue(stor_id, store.get("stor_id"));
				mapEntity.setValue(yr_qtr_int_nbr, store.get("yr_qtr_int_nbr"));
				mapEntity.setValue(integral_all, store.get("上季度4S积分总数"));
				mapEntity.setValue(bonus, store.get("奖励总额(元)"));
				mapEntity.setValue(train, store.get("培训达标"));
				mapEntity.setValue(lever, store.get("等级变更"));
				dataList.add(mapEntity);
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;
	}


	public List<MapEntity> getAllStoreIntegral(String sr_id)  throws TimeoutException, NetworkException {
		StoreRemoteDao storeRemoteDao= new StoreRemoteDao();
		HttpResult httpResult = storeRemoteDao.getAllStoreIntegral(sr_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		if(null==response||"".equals(response)){
			throw new ServerException();
		}
		List<MapEntity> dataList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			JSONArray storeData = data.getJSONArray("data");
			for (int i = 0; i < storeData.length(); i++) {
				JSONObject store = storeData.getJSONObject(i);
				MapEntity mapEntity = new MapEntity();
//				  "stor_id": 1341218,
//		            "yr_qtr_int_nbr": 201401,
//		            "上季度4S积分总数": 120,
//		            "奖励总额(元)": 480,
//		            "培训达标": "不达标",
//		            "等级变更": "升级",
//		            "stor_nm": "百易达"
				mapEntity.setValue(stor_id, store.get("stor_id"));
				mapEntity.setValue(stor_nm, store.get("stor_nm"));
				mapEntity.setValue(yr_qtr_int_nbr, store.get("yr_qtr_int_nbr"));
				mapEntity.setValue(integral_all, store.get("上季度4S积分总数"));
				mapEntity.setValue(bonus, store.get("奖励总额(元)"));
				mapEntity.setValue(train, store.get("培训达标"));
				mapEntity.setValue(lever, store.get("等级变更"));
				dataList.add(mapEntity);
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;
	}

}
