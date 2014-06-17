package com.intel.store.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteQueryPicCategoryDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class QueryPicCategoryModel extends StoreBaseModel {

	/**
	 * 图片类型
	 */
	public static final int PHT_CAT = 1;
	/**
	 * 图片类型名
	 */
	public static final int PHT_CAT_NM = 2;
	public static final int DSPLY_ORD_NBR = 3;
	/**
	 * 图片评论
	 */
	public static final int CMNTS = 4;
	/**
	 * 需要上传的张数
	 */
	public static final int PHT_QTY = 5;
	/**
	 * 已经上传的张数
	 */
	public static final int PIC_COUNT = 6;

	public ArrayList<MapEntity> queryPicCategoryModel()
			throws TimeoutException, ServerException, NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicCategoryDao()
				.queryPicCategory();
		String response = preParseHttpResult(httpResult);
		Loger.d("response:"+response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonDate = data.getJSONArray("data");
				for (int i = 0; i < jsonDate.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(PHT_CAT,
							jsonMessage.optString("pht_cat"));
					mapEntity.setValue(PHT_CAT_NM,
							jsonMessage.optString("pht_cat_nm"));
					mapEntity.setValue(DSPLY_ORD_NBR,
							jsonMessage.optString("disply_ord_nbr"));
					mapEntity.setValue(CMNTS, jsonMessage.optString("cmnts"));
					mapEntity.setValue(PHT_QTY,
							jsonMessage.optString("pht_qty"));
					result.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	public ArrayList<MapEntity> listCategoryByWvModel(String wvId)
			throws TimeoutException, ServerException, NetworkException {
		Loger.d("wvIdwvId:"+wvId);
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicCategoryDao()
		.listCategoryByWv(wvId);
		String response = preParseHttpResult(httpResult);
		Loger.d("response:"+response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonDate = data.getJSONArray("data");
				for (int i = 0; i < jsonDate.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(PHT_CAT,
							jsonMessage.optString("pht_cat"));
					mapEntity.setValue(PHT_CAT_NM,
							jsonMessage.optString("pht_cat_nm"));
					mapEntity.setValue(DSPLY_ORD_NBR,
							jsonMessage.optString("disply_ord_nbr"));
					mapEntity.setValue(CMNTS, jsonMessage.optString("cmnts"));
					mapEntity.setValue(PHT_QTY,
							jsonMessage.optString("pht_qty"));
					mapEntity.setValue(PIC_COUNT,
							jsonMessage.optString("pic_count"));
					result.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
