package com.intel.store.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteClerkDao;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class ClerkModel extends StoreBaseModel {

	public static final String REP_MANAGER = "1";
	public static final int REP_ID = 1;
	public static final int REP_ASGN_ROLE_ID = 2;
	public static final int REP_ASGN_ROLE_NM = 3;
	public static final int REP_NM = 4;
	public static final int REP_TEL = 5;
	public static final int REP_EMAIL = 6;
	public static final int REP_GVRNMT_ID = 7;
	public static final int Store_ID = 8;
	public static final int CREATER_REP_ID = 9;

	public ArrayList<MapEntity> getMyClerkListFromRemote()
			throws TimeoutException, ServerException, NetworkException {
		Loger.d("getMyClerkListFromRemote");
		String repId = StoreSession.getRepID();
		String storId = StoreSession.getCurrentStoreId();
		HttpResult httpResult = new RemoteClerkDao().getMyClerkList(repId,
				storId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		ArrayList<MapEntity> mapList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray dataJsonArray = data.getJSONArray("data");
				int dataLength = dataJsonArray.length();
				if (dataLength <= 0) {
					return null;
				}
				for (int i = 0; i < dataLength; i++) {
					JSONObject dataJsonObj = dataJsonArray.getJSONObject(i);
					MapEntity mapEntity = new MapEntity();
					if (dataJsonObj.has("rep_id")) {
						mapEntity.setValue(REP_ID,
								dataJsonObj.getString("rep_id"));
					} else {
						mapEntity.setValue(REP_ID, "0");
					}
					mapEntity.setValue(REP_ASGN_ROLE_ID,
							dataJsonObj.optString("rep_asgn_role_id"));
					if (dataJsonObj.optString("rep_asgn_role_nm").equals("RSP")) {
						
						mapEntity.setValue(REP_ASGN_ROLE_NM,
								"销售员");
					}else {
						
						mapEntity.setValue(REP_ASGN_ROLE_NM,
								dataJsonObj.optString("rep_asgn_role_nm"));
					}
					mapEntity.setValue(REP_NM, 
							dataJsonObj.optString("rep_nm"));
					mapEntity.setValue(REP_TEL,
							dataJsonObj.optString("rep_tel"));
					mapEntity.setValue(REP_EMAIL,
							dataJsonObj.optString("rep_email"));
					mapEntity.setValue(REP_GVRNMT_ID,
							dataJsonObj.optString("rep_gvrnmt_id"));
					mapList.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StoreSession.setLastSyncTime(System.currentTimeMillis() + "");
		return mapList;
	}

	// 增加我的店员
	public String addMyClerk(MapEntity entity) throws TimeoutException,
			ServerException, NetworkException {
		Loger.d("add myclerk");
		String storId = entity.getString(ClerkModel.Store_ID);
		String rep_nm = entity.getString(ClerkModel.REP_NM);
		String rep_tel = entity.getString(ClerkModel.REP_TEL);
		String rep_email = entity.getString(ClerkModel.REP_EMAIL);
		String last_upd_usr_id = entity.getString(ClerkModel.CREATER_REP_ID);

		HttpResult httpResult = new RemoteClerkDao().addClerk(storId, rep_nm,
				rep_tel, rep_email, last_upd_usr_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			preParseResponse(response);
		} catch (JSONException e) {
			e.printStackTrace();
			return "false";
		}
		StoreSession.setLastSyncTime(System.currentTimeMillis() + "");
		return "true";
	}

	// 修改我的店员
	public Boolean modefyMyClerk(MapEntity entity) throws TimeoutException,
			ServerException, NetworkException {
		// TODO
		Loger.d("modefy myclerk");
		String rsp_id = entity.getString(ClerkModel.REP_ID);
		String rep_tel = entity.getString(ClerkModel.REP_TEL);
		String rep_email = entity.getString(ClerkModel.REP_EMAIL);
		String operate_rsp_id = entity.getString(ClerkModel.CREATER_REP_ID);

		HttpResult httpResult = new RemoteClerkDao().modefyClerk(rsp_id,
				rep_tel, rep_email, operate_rsp_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			preParseResponse(response);
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		StoreSession.setLastSyncTime(System.currentTimeMillis() + "");
		return true;
	}

	public Boolean delMyClerk(MapEntity entity) throws TimeoutException,
			NetworkException {
		// TODO
		Loger.d("del myclerk");
		String rsp_id = entity.getString(ClerkModel.REP_ID);
		String operate_rsp_id = entity.getString(ClerkModel.CREATER_REP_ID);

		HttpResult httpResult = new RemoteClerkDao().delClerk(rsp_id,
				operate_rsp_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			preParseResponse(response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		StoreSession.setLastSyncTime(System.currentTimeMillis() + "");
		return true;
	}
}
