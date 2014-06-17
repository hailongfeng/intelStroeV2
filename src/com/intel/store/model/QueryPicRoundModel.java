package com.intel.store.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteQueryPicDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class QueryPicRoundModel extends StoreBaseModel {

	public static final int CURRENT_QUARTER = 0;
	public static final int WV_ID = 1;
	public static final int WV_NM = 2;
	public static final int YYYYQQ = 3;
	public static final int WV_START_DT = 4;
	public static final int WV_END_DT = 5;
	public static final int LAST_IPD_USR_ID = 6;
	public static final int LAST_UPD_DTM = 7;
	public static final int WV_TYPE_ID = 8;
	public static final int WV_TYPE_NM = 9;

	/**
	 * @return 获取当前季度
	 * @throws TimeoutException
	 * @throws ServerException
	 * @throws NetworkException
	 */
	public MapEntity queryCurrentQuarter() throws TimeoutException,
			ServerException, NetworkException {
		HttpResult httpResult = new RemoteQueryPicDao().queryPicRound();
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		MapEntity mapEntity = new MapEntity();
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				String currentQuarter =  data.get("data")+"";
				mapEntity.setValue(CURRENT_QUARTER, currentQuarter);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapEntity;
	}

	/**
	 * @param 
	 * @return 获取当前季度的轮次
	 * @throws TimeoutException
	 * @throws ServerException
	 * @throws NetworkException
	 */
	public ArrayList<MapEntity> listWvByQuarter(String quarter)
			throws TimeoutException, ServerException, NetworkException {

		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicDao()
				.listWvByQuarter(quarter);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonDate = data.getJSONArray("data");
				
				for (int i = 0; i < jsonDate.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(WV_ID, jsonMessage.optString("wv_id"));
					mapEntity.setValue(WV_NM, jsonMessage.optString("wv_nm"));
					mapEntity.setValue(YYYYQQ, jsonMessage.optString("yyyyqq"));
					mapEntity.setValue(WV_START_DT,
							jsonMessage.optString("wv_strt_dt"));
					mapEntity.setValue(WV_END_DT,
							jsonMessage.optString("wv_end_dt"));
					mapEntity.setValue(LAST_IPD_USR_ID,
							jsonMessage.optString("last_upd_usr_id"));
					mapEntity.setValue(LAST_UPD_DTM,
							jsonMessage.optString("last_upd_dtm"));
					mapEntity.setValue(WV_TYPE_ID,
							jsonMessage.optString("wv_type_id"));
					mapEntity.setValue(WV_TYPE_NM,
							jsonMessage.optString("wv_type_nm"));
					result.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	public ArrayList<MapEntity> queryHistoryWv()
			throws TimeoutException, ServerException, NetworkException {

		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicDao()
				.queryHistoryWv();
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonDate = data.getJSONArray("data");
				
				for (int i = 0; i < jsonDate.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(WV_ID, jsonMessage.optString("wv_id"));
					mapEntity.setValue(WV_NM, jsonMessage.optString("wv_nm"));
					mapEntity.setValue(YYYYQQ, jsonMessage.optString("yyyyqq"));
					mapEntity.setValue(WV_START_DT,
							jsonMessage.optString("wv_strt_dt"));
					mapEntity.setValue(WV_END_DT,
							jsonMessage.optString("wv_end_dt"));
					mapEntity.setValue(LAST_IPD_USR_ID,
							jsonMessage.optString("last_upd_usr_id"));
					mapEntity.setValue(LAST_UPD_DTM,
							jsonMessage.optString("last_upd_dtm"));
					mapEntity.setValue(WV_TYPE_ID,
							jsonMessage.optString("wv_type_id"));
					mapEntity.setValue(WV_TYPE_NM,
							jsonMessage.optString("wv_type_nm"));
					result.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
