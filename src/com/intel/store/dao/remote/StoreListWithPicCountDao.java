package com.intel.store.dao.remote;

import java.util.HashMap;

import android.text.TextUtils;

import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;

public class StoreListWithPicCountDao extends StoreRemoteBaseDao {
    public HttpResult getDiyStoreWithPicCount( String city,
            String page, String size) throws TimeoutException, NetworkException {
        final String url = HOST + "dIYStoreWithPicCountServlet";
        HashMap<String, String> hmParams = new HashMap<String, String>();
        hmParams.put("slsprs_id", StoreSession.getRepID());
        hmParams.put("sessionId", StoreSession.getJsessionId());
        hmParams.put("role_id", "77");
        hmParams.put("city", city);
        if (!TextUtils.isEmpty(page) && !TextUtils.isEmpty(size)) {
            hmParams.put("page", page);
            hmParams.put("size", size);
        }
        return intelPost(url, hmParams);
    }

    public HttpResult getOemStoreWithPicCount( String city,
            String page, String size) throws TimeoutException, NetworkException {
        final String url = HOST + "oEMStoreWithPicCountServlet";
        HashMap<String, String> hmParams = new HashMap<String, String>();
        hmParams.put("slsprs_id", StoreSession.getRepID());
        hmParams.put("sessionId", StoreSession.getJsessionId());
        hmParams.put("role_id", "78");
        hmParams.put("city", city);
        if (!TextUtils.isEmpty(page) && !TextUtils.isEmpty(size)) {
            hmParams.put("page", page);
            hmParams.put("size", size);
        }
        return intelPost(url, hmParams);
    }
}