package com.intel.store.model;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteMessageDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class MessageModel extends StoreBaseModel{
	
	public static final int MSG_ID = 1; //ID
	public static final int MSG_TTL = 2;//标题
	public static final int MSG_CNTNT = 3;//内容
	public static final int LAST_UPD_USR_ID = 4;//发布人ID
	public static final int LAST_UPD_DTM = 5;//发表时间
	public static final int MSG_PSH_STS = 6;//消息状态
	public static final int MSG_PSH_DTM = 7;//
	public static final int MSG_TYPE_ID = 8;//消息类型
	public static final int MSG_SCHDL_DTM = 9;//
	public static final int MSG_TYPE_NM = 10;//类型名
	
	public int countUnReadMsg(String rspId) throws TimeoutException, NetworkException{
		HttpResult httpResult=	new RemoteMessageDao().countUnReadMsg(rspId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				int count = data.getInt("data");
				return count;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
//	hmParams.put("rsp_id", "733");//oem
//	hmParams.put("statusStr", "2");//0:无效的 1：有效未读 2有效已读
	public ArrayList<MapEntity> listMessage(String rspId,String statusStr) throws NetworkException{
		HttpResult httpResult=	new RemoteMessageDao().listMessage(rspId,statusStr);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);	
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				ArrayList<MapEntity> mapEntities= new ArrayList<MapEntity>();
				JSONArray messages = data.getJSONArray("data");
				for (int i = 0; i < messages.length(); i++) {
					MapEntity entity=new MapEntity();
					JSONObject message=(JSONObject) messages.get(i);
					entity.setValue(MSG_ID, message.getString("MSG_ID".toLowerCase()));
					entity.setValue(MSG_TTL, message.getString("MSG_TTL".toLowerCase()));
					entity.setValue(MSG_CNTNT, message.getString("MSG_CNTNT".toLowerCase()));
					entity.setValue(LAST_UPD_USR_ID, message.getString("LAST_UPD_USR_ID".toLowerCase()));
					entity.setValue(LAST_UPD_DTM, message.getString("LAST_UPD_DTM".toLowerCase()));
					entity.setValue(MSG_PSH_STS, message.getString("MSG_PSH_STS".toLowerCase()));
					entity.setValue(MSG_PSH_DTM, message.getString("MSG_PSH_DTM".toLowerCase()));
					entity.setValue(MSG_TYPE_ID, message.getString("MSG_TYPE_ID".toLowerCase()));
					entity.setValue(MSG_SCHDL_DTM, message.getString("MSG_SCHDL_DTM".toLowerCase()));
					entity.setValue(MSG_TYPE_NM, message.getString("MSG_TYPE_NM".toLowerCase()));
					mapEntities.add(entity);
				}
				return mapEntities;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateMessageStatus(String rspId,String msgId) 
			throws TimeoutException, NetworkException {
		HttpResult httpResult=	new RemoteMessageDao().updateMessageStatus(rspId,msgId);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);	
	}

}
