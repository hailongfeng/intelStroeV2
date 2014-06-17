package com.intel.store.dao.remote;

import java.util.HashMap;

import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Version;

public class RemoteCheckVersionDao extends StoreRemoteBaseDao {
    public HttpResult getVersion() throws TimeoutException,
            NetworkException {
        final String url =HOST + "checkRspApkVersionServlet";
        HashMap<String, String> hmParams = new HashMap<String, String>();
        Loger.d("Version.getVersionName()" + Version.getVersionName());
        hmParams.put("version", Version.getVersionName());
        return intelPost(url, hmParams);
    }
}