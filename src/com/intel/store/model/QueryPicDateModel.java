package com.intel.store.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteQueryPicDateFileDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class QueryPicDateModel extends StoreBaseModel {

	public static final int LAST_UPD_DTM = 1;
	public static final int PIC_COUNT = 2;
	
	public ArrayList<MapEntity> queryPicDateModel(String rep_id,String stor_id) throws TimeoutException,
			ServerException, NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicDateFileDao().queryPicDateFile(rep_id,stor_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
            JSONObject data = preParseResponse(response);
            if (data != null) {
                JSONArray jsonDate = data.getJSONArray("data");
                for (int i = 0; i < jsonDate.length(); ++i) {
                    JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
                    MapEntity mapEntity = new MapEntity();
                    mapEntity.setValue(LAST_UPD_DTM, jsonMessage.optString("last_upd_dtm"));
                    mapEntity.setValue(PIC_COUNT, jsonMessage.optString("pic_count"));
                    result.add(mapEntity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
	}
}
