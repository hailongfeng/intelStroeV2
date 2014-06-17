package com.intel.store.dao.remote;


import java.util.HashMap;

import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class RemoteStorePhotoDao extends StoreRemoteBaseDao {

    public HttpResult getStorePhotoList(String storId,String slsprsId) throws TimeoutException, NetworkException {
        final String url = HOST + "queryStorePicServlet";
        HashMap<String, String> hmParams = new HashMap<String, String>();
        hmParams.put("stor_id", storId);
        hmParams.put("slsprs_id", slsprsId);
        hmParams.put("sessionId", StoreSession.getJsessionId());
        return intelPost(url, hmParams);
    }
    public HttpResult delStorePhotoById(String pic_id) throws TimeoutException, NetworkException {
    	final String url = PICTURE_HOST + "deleteRspPicServlet";
    	HashMap<String, String> hmParams = new HashMap<String, String>();
    	hmParams.put("pic_id", pic_id);
    	hmParams.put("slsprs_id", StoreSession.getRepID());
    	hmParams.put("stor_id", StoreSession.getCurrentStoreId());
    	hmParams.put("sessionId", StoreSession.getJsessionId());
    	printParams(hmParams);
    	return intelPost(url, hmParams);
    }

}
