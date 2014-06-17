package com.intel.store.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.remote.RemoteCheckVersionDao;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class CheckVersionModel extends StoreBaseModel {

    public static final int FILEURL = 1;
    public static final int UPDATENOTE = 2;
    public static final int RESULT = 3;
    public static final int VERSION = 4;

    public MapEntity checkVersion() throws TimeoutException,
            ServerException, NetworkException {
    	
        MapEntity result = new MapEntity();
        HttpResult httpResult = new RemoteCheckVersionDao().getVersion();
        String response = preParseHttpResult(httpResult);
        Loger.d(response);
        try {
            JSONObject data = preParseResponse(response);
            if (data != null) {
                JSONObject jsonData = data.getJSONObject("data");
                result.setValue(FILEURL, jsonData.getString("fileurl"));
                result.setValue(RESULT, jsonData.getString("result"));
                result.setValue(VERSION, jsonData.getString("version"));
                result.setValue(UPDATENOTE, jsonData.getString("updatenote"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}