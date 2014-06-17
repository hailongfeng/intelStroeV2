package com.intel.store.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.StoreApplication;
import com.intel.store.dao.remote.StoreRemoteDao;
import com.intel.store.util.ACache;
import com.intel.store.util.InputChecker;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class StoreListModel extends StoreBaseModel {

	private StoreRemoteDao storeRemoteDao;
	/*
	 * "city_type_nm": "Full Model City", "stor_nm": "百脑汇英俊", "stor_addr":
	 * "百脑汇3D06", "pht_pth": "/photo/mob_a_99801_110_20130911130317_524_12.jpg",
	 * "city_nm": "无锡市", "cat_type_nm": "五星级", "stor_tel": "13255111813",
	 * "stor_id": 99801, "stor_manager_name": "栾玲玲", "mall_nm": "无锡百脑汇",
	 * "email": "wxybkj@sohu.com", "role_id": 77
	 */

	public static final int CITY_TYPE_NM = 1;

	/** 门店的名称 */
	public static final int STOR_NM = 2;
	/** 门店的地址 */
	public static final int STOR_ADDR = 3;
	/** 门店店面照片的url地址 */
	public static final int PHT_PTH = 4;
	/** 门店所在的城市名称 */
	public static final int CITY_NM = 5;
	/** 门店的等级 */
	public static final int CAT_TYPE_NM = 6;
	/** 门店的电话号码 */
	public static final int STOR_TEL = 7;
	/** 门店的ID */
	public static final int STOR_ID = 8;
	/** 门店的经理姓名 */
	public static final int STOR_MANAGER_NAME = 9;
	/** 门店所在的卖场名称 */
	public static final int MALL_NM = 10;
	/** 门店的邮箱地址 */
	public static final int EMAIL = 11;
	/** 门店的权限ID：DIY或者OEM */
	public static final int ROLE_ID = 12;
	/** 门店品牌 */
	public static final int STOR_BRAND = 13;

	public static final int NEW_CITY_TYPE = 14;
	/** DIY的编号是77 */
	public static final int DIY = 77;
	/** OEM的编号是78 */
	public static final int OEM = 78;
	public static final String OEM_NAME = "OEM";
	public static final String DIY_NAME = "DIY";

	public StoreListModel() {
		storeRemoteDao = new StoreRemoteDao();
	}

	public List<MapEntity> getStoreBySrId(String sr_id)
			throws TimeoutException, NetworkException {

		ACache mCache = ACache.get(StoreApplication.getApp());
		String storeList = mCache.getAsString("storeList");
		String response = "";
		if (!InputChecker.isEmpty(storeList)) {
			response = storeList;
		} else {
			HttpResult httpResult = storeRemoteDao.getStoreBySrId(sr_id);
			response = preParseHttpResult(httpResult);
			mCache.put("storeList", response);
		}

		Loger.d(response);
		List<MapEntity> dataList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			JSONArray storeData = data.getJSONArray("data");
			for (int i = 0; i < storeData.length(); i++) {
				JSONObject store = storeData.getJSONObject(i);
				MapEntity mapEntity = new MapEntity();
				mapEntity.setValue(CITY_TYPE_NM, store.get("city_type_nm"));
				mapEntity.setValue(STOR_NM, store.get("stor_nm"));
				mapEntity.setValue(STOR_ADDR, store.get("stor_addr"));
				mapEntity.setValue(PHT_PTH, store.get("pht_pth"));
				mapEntity.setValue(CITY_NM, store.get("city_nm"));
				mapEntity.setValue(CAT_TYPE_NM, store.get("cat_type_nm"));
				mapEntity.setValue(STOR_TEL, store.get("stor_tel"));
				mapEntity.setValue(STOR_MANAGER_NAME,
						store.get("stor_manager_name"));
				mapEntity.setValue(MALL_NM, store.get("mall_nm"));
				mapEntity.setValue(EMAIL, store.get("email"));
				mapEntity.setValue(ROLE_ID, store.get("role_id"));
				mapEntity.setValue(STOR_ID, store.get("stor_id"));
				mapEntity.setValue(STOR_BRAND, store.get("Brand"));
				mapEntity.setValue(NEW_CITY_TYPE, store.get("new_city_type"));
				dataList.add(mapEntity);
			}
		} catch (JSONException jsonException) {
			jsonException.printStackTrace();
		}
		return dataList;

	}

}
