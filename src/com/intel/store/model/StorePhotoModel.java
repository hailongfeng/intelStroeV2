package com.intel.store.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.intel.store.dao.remote.RemoteStorePhotoDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Utils;

public class StorePhotoModel extends StoreBaseModel {

	public static final int PHT_CAT_NM = 1;// 类别
	public static final int PIC_ID = 2;// 图片ID
	public static final int STOR_ID = 3;// 门店ID
	public static final int WV_ID = 4;// ??
	public static final int PHT_PTH = 5;// 图片路径
	public static final int PHT_CAT = 6;// 类别ID
	public static final int LAST_UPD_USR_ID = 7;// 上次更新的用户
	public static final int LAST_UPD_DTM = 8;// 上次更新的时间
	public static final int CMNS = 9;// 评论
	public static final int SR_NAME = 10;// 评论
	public static final int LAST_UPD_USR_NM = 11;// 上次更新的用户名

	public ArrayList<MapEntity> getStorePhoto(String storId, String slsprsId)
			throws TimeoutException, ServerException, NetworkException {
		HttpResult httpResult = new RemoteStorePhotoDao().getStorePhotoList(
				storId, slsprsId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		ArrayList<MapEntity> mapList = new ArrayList<MapEntity>();
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONArray dataJsonArray = data.getJSONArray("data");
				String strName = data.getString("sr_name");
				int dataLength = dataJsonArray.length();
				if (dataLength <= 0) {
					return null;
				}

				for (int i = 0; i < dataLength; i++) {
					JSONObject dataJsonObj = dataJsonArray.getJSONObject(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(PHT_CAT_NM,
							dataJsonObj.getString("pht_cat_nm"));
					mapEntity.setValue(PIC_ID, dataJsonObj.getString("pic_id"));
					mapEntity.setValue(STOR_ID,
							dataJsonObj.getString("stor_id"));
					mapEntity.setValue(WV_ID, dataJsonObj.getString("wv_id"));
					String pictureHost = Utils.getNetConfigProperties()
							.getProperty("pictureHost");
					mapEntity.setValue(PHT_PTH,
							pictureHost + "readSingleRspPicServlet?pht_pth="
									+ dataJsonObj.getString("pht_pth"));
					mapEntity.setValue(PHT_CAT,
							dataJsonObj.getString("pht_cat"));
					mapEntity.setValue(LAST_UPD_USR_ID,
							dataJsonObj.getString("last_upd_usr_id"));
					mapEntity.setValue(LAST_UPD_USR_NM,
							dataJsonObj.getString("last_upd_usr_nm"));
					if (dataJsonObj.has("cmnts")) {
						mapEntity
								.setValue(CMNS, dataJsonObj.getString("cmnts"));
					} else {
						mapEntity.setValue(CMNS, "");
					}
					mapEntity.setValue(SR_NAME, strName);

					String time = dataJsonObj.getString("last_upd_dtm");

					if (!TextUtils.isEmpty(time)) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm");
						Date joindateDate = null;
						try {
							joindateDate = sdf.parse(time);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						time = sdf.format(joindateDate);
						mapEntity.setValue(LAST_UPD_DTM, time);
					}

					mapList.add(mapEntity);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mapList;
	}

	public boolean delPictureByid(String pic_id) throws NetworkException {
		Loger.d("pic_id：" + pic_id);
		HttpResult httpResult = new RemoteStorePhotoDao()
				.delStorePhotoById(pic_id);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {

			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
