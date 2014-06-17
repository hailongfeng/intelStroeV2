package com.intel.store.dao.remote;

import java.util.HashMap;

import com.intel.store.util.Constants;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class StoreRemoteDao extends StoreRemoteBaseDao {

	public HttpResult login(String userName, String password, String userType)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_LOGINSERVLET;
		Loger.d("url:" + url);
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("username", userName);
		hmParams.put("password", password);
		return intelPost(url, hmParams);
	}
	public HttpResult logout()
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_LOGOUTSERVLET;
		Loger.d("url:" + url);
		HashMap<String, String> hmParams = new HashMap<String, String>();
		return intelPost(url, hmParams);
	}

	public HttpResult getStoreBySrId(String rep_id) throws TimeoutException,
			NetworkException {
		final String url = HOST + Constants.API_LISTRSPSTORE;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("rep_id", rep_id);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult getSalesCountById(String storeId)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_DIYSALESDATASERVLET;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		Loger.d("stor_id = " + storeId);
		hmParams.put("stor_id", storeId);
		hmParams.put("sessionId", StoreSession.getJsessionId());

		return intelPost(url, hmParams);
	}

	public HttpResult getStoreIntegralById(String storeId)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_STOREBONUSSERVLET;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("stor_id", storeId);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult checkTarVersion(String version) throws TimeoutException,
			NetworkException {
		final String url = HOST + Constants.API_CHECKTARVERSIONSERVLET;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("version", version);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult getProductInfo(String getType) throws TimeoutException,
			NetworkException {
		final String url = HOST + Constants.API_ALLPRODUCTSERVLET;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("product", getType);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult oemSalesReporte(String storeId, String roleId,
			String barcode, String brand_id, String mdl_id, String pic_loc)
			throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ADDOEMSALEDATA;
		HashMap<String, String> hmParams = new HashMap<String, String>();

		hmParams.put("stor_id", storeId);
		hmParams.put("rep_id", roleId);
		hmParams.put("barcode", barcode);
		hmParams.put("brand_id", brand_id);
		hmParams.put("mdl_id", mdl_id);
		hmParams.put("pic_loc", pic_loc);
		hmParams.put("sessionId", StoreSession.getJsessionId());

		return intelPost(url, hmParams);
	}

	public HttpResult diySalesReporte(String storeId, String rep_id,
			String barcode,String pic_loc) throws TimeoutException, NetworkException {
		final String url = HOST + Constants.API_ADDDIYSALEDATA;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("stor_id", storeId);
		hmParams.put("rep_id", rep_id);
		hmParams.put("barcode", barcode);
		hmParams.put("pic_loc", pic_loc);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult validateBarcode(String barcode) throws TimeoutException,
			NetworkException {
		final String url = HOST + Constants.API_VALIDATEBARCODE;
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("cpu_id", barcode);
		hmParams.put("sessionId", StoreSession.getJsessionId());
		return intelPost(url, hmParams);
	}

	public HttpResult listOemSaleData()
			throws TimeoutException, NetworkException {
		final String url = HOST + "/storeRspMgmt/listOemSaleData.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put("stor_id", StoreSession.getCurrentStoreId());
		return intelPost(url, hmParams);
	}
	public HttpResult listDiySaleData()
			throws TimeoutException, NetworkException {
		final String url = HOST + "/storeRspMgmt/listDiySaleData.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put("stor_id", StoreSession.getCurrentStoreId());
		return intelPost(url, hmParams);
	}
	public HttpResult getAllStoreSalesCount(String rsp_id) 
			throws TimeoutException, NetworkException {
		final String url = HOST + "/newStoreRspMgmt/listStoreSalesData.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put("rsp_id", rsp_id);
		return intelPost(url, hmParams);
	}
	public HttpResult getAllStoreIntegral(String rsp_id) 
			throws TimeoutException, NetworkException {
		final String url = HOST + "/newStoreRspMgmt/listStoreBonus.do";
		HashMap<String, String> hmParams=new HashMap<String, String>();
		hmParams.put("rsp_id", rsp_id);
		return intelPost(url, hmParams);
	}
	
}
