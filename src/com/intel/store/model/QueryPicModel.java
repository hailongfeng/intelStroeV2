package com.intel.store.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.intel.store.dao.remote.RemoteQueryPicByDateDao;
import com.intel.store.dao.remote.RemoteQueryPicDao;
import com.intel.store.dao.remote.StoreRemoteBaseDao;
import com.intel.store.util.Constants;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;


public class QueryPicModel extends StoreBaseModel {
	/**
    "pht_cat_nm":"日常拍照",
    "pic_id":2,
    "stor_id":99801,
    "wv_id":54,
    "pht_pth":"\/photo\/mob_a___20131218172907_994_13.jpg",
    "pht_cat":30,
    "last_upd_usr_id":"53825",
    "cmnts":"高奔6",
    "last_upd_dtm":"2013-12-18 17:31:45.663",
    "last_upd_usr_nm":"栾玲玲
    "*/
	public static final int PHT_CAT_NM = 1;
	public static final int PIC_ID = 2;
	public static final int STOR_ID = 3;
	public static final int WV_ID = 4;
	public static final int PHT_PTH = 5;
	public static final int PHT_CAT = 6;
	public static final int LAST_UPD_USR_ID = 7;
	public static final int CMNTS = 8;
	public static final int LAST_UPD_DTM = 9;
	public static final int SR_NAME = 10;
	public static final int LAST_UPD_USR_NM = 11;
	
	public ArrayList<MapEntity> queryPicByDate(String slsprsId,String last_upd_dtm,String stor_id) throws TimeoutException,
			ServerException, NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicByDateDao().queryPicByDate(slsprsId, last_upd_dtm,stor_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
            JSONObject data = preParseResponse(response);
            if (data != null) {
                JSONArray jsonDatePic = data.getJSONArray("data");
                for (int i = 0; i < jsonDatePic.length(); ++i) {
                    JSONObject jsonMessage = (JSONObject) jsonDatePic.get(i);
                    MapEntity mapEntity = new MapEntity();
                    mapEntity.setValue(PHT_CAT_NM, jsonMessage.optString("pht_cat_nm"));
                    mapEntity.setValue(PIC_ID, jsonMessage.optString("pic_id"));
                    mapEntity.setValue(LAST_UPD_USR_NM, jsonMessage.optString("last_upd_usr_nm"));
                    mapEntity.setValue(STOR_ID, jsonMessage.optString("stor_id"));
                    mapEntity.setValue(WV_ID, jsonMessage.optString("wv_id"));
                    mapEntity.setValue(PHT_PTH, StoreRemoteBaseDao.PICTURE_HOST
                            + "readSingleRspPicServlet?pht_pth="
                            + jsonMessage.optString("pht_pth"));
                    mapEntity.setValue(PHT_CAT, jsonMessage.optString("pht_cat"));
                    mapEntity.setValue(LAST_UPD_USR_ID, jsonMessage.optString("last_upd_usr_id"));
                    
                    if (jsonMessage.has("cmnts")) {
                        mapEntity
                                .setValue(CMNTS, jsonMessage.getString("cmnts"));
                    } else {
                        mapEntity.setValue(CMNTS, "");
                    }
                    
                    mapEntity.setValue(SR_NAME,"");
                    
                    String time = jsonMessage.getString("last_upd_dtm");

                    if (!TextUtils.isEmpty(time)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date joindateDate = null;
                        try {
                            joindateDate = sdf.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        time = sdf.format(joindateDate);
                        mapEntity.setValue(LAST_UPD_DTM, time);
                    }
                    
                    result.add(mapEntity);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
	}
	
	public ArrayList<MapEntity> listPhotoByWvCategory(String wvId,
			String category) throws TimeoutException, ServerException,
			NetworkException {

		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new RemoteQueryPicDao().listPhotoByWvCategory(
				wvId, category);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray jsonDate = data.getJSONArray("data");

				for (int i = 0; i < jsonDate.length(); ++i) {
					JSONObject jsonMessage = (JSONObject) jsonDate.get(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(PIC_ID, jsonMessage.getString("pic_id"));
					mapEntity.setValue(STOR_ID,jsonMessage.getString("stor_id"));
					mapEntity.setValue(WV_ID, jsonMessage.getString("wv_id"));
					mapEntity.setValue(PHT_PTH,StoreRemoteBaseDao.PICTURE_HOST
                            + Constants.READ_SINGLE_RSP_PIC_SERVELET+jsonMessage.getString("pht_pth"));
					mapEntity.setValue(PHT_CAT,jsonMessage.getString("pht_cat"));
					mapEntity.setValue(LAST_UPD_USR_ID,jsonMessage.getString("last_upd_usr_id"));
					mapEntity.setValue(LAST_UPD_USR_NM,jsonMessage.getString("last_upd_usr_nm"));
					mapEntity.setValue(PHT_CAT_NM,jsonMessage.getString("pht_cat_nm"));
			        String time = jsonMessage.getString("last_upd_dtm");
                    if (!TextUtils.isEmpty(time)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(
                                "yyyy-MM-dd");
                        Date joindateDate = null;
                        try {
                            joindateDate = sdf.parse(time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        time = sdf.format(joindateDate);
                        mapEntity.setValue(LAST_UPD_DTM, time);
                    }
					
					if (jsonMessage.has("cmnts")) {
						mapEntity.setValue(CMNTS, jsonMessage.getString("cmnts"));
					} else {
						mapEntity.setValue(CMNTS, "");
					}
					result.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}
