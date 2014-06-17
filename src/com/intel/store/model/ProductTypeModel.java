package com.intel.store.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.dao.local.LocalStoreDao;
import com.intel.store.dao.remote.StoreRemoteDao;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class ProductTypeModel extends StoreBaseModel {

	public static final int COMMON_NAME = 8;
	public static final int COMMON_ID = 9;

	public static final int PRD_TYPE = 7;

	public static final int PRD_CAT_ID = 1;
	public static final int PRD_CAT_NM = 2;
	public static final int BRND_ID = 3;
	public static final int BRND_NM = 4;
	public static final int MDL_ID = 5;
	public static final int MDL_NM = 6;

	public Boolean exitLocalProductType() throws TimeoutException,
			ServerException, NetworkException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.exitLocalProductType();
	}

	public ArrayList<MapEntity> getAllProductInfoFromRemote(String getType)
			throws TimeoutException, ServerException, NetworkException {
		ArrayList<MapEntity> result = new ArrayList<MapEntity>();
		HttpResult httpResult = new StoreRemoteDao().getProductInfo(getType);
		String response = preParseHttpResult(httpResult);
		Loger.d(response);
		try {
			JSONObject data = preParseResponse(response);
			if (data != null) {
				JSONObject allData = data.getJSONObject("data");
				JSONArray allProducts = allData.getJSONArray("result");
				for (int i = 0; i < allProducts.length(); ++i) {
					JSONObject product = allProducts.getJSONObject(i);
					MapEntity mapEntity = new MapEntity();
					mapEntity.setValue(PRD_CAT_ID,
							"" + product.optInt("prd_cat_id"));
					mapEntity.setValue(PRD_CAT_NM,
							product.optString("prd_cat_nm"));
					mapEntity.setValue(BRND_ID, "" + product.optInt("brnd_id"));
					mapEntity.setValue(BRND_NM, product.optString("brnd_nm"));
					mapEntity.setValue(MDL_ID, "" + product.optInt("mdl_id"));
					mapEntity.setValue(MDL_NM, product.optString("mdl_nm"));
					result.add(mapEntity);
				}
				// try {
				// LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
				// localStoreDao.insertPrdCategory(result);
				// } catch (DBException e) {
				// e.printStackTrace();
				// }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<MapEntity> getPrdCategoryFromLocal() throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getProductCategory();
	}

	public ArrayList<MapEntity> getPrdBrndFromLocal(String strCatId)
			throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getProductBrnd(strCatId);
	}

	public ArrayList<MapEntity> getPrdModelCategoryFromLocal(String strCatId,
			String strBrndId) throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getProductMolde(strCatId, strBrndId);
	}

	public void saveProductType(List<MapEntity> data) {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		try {
			localStoreDao.insertPrdCategory(data);
		} catch (DBException e) {
			e.printStackTrace();
		}

	}

	public List<MapEntity> getAllBrndFromLocal() throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getAllBrndFromLocal();
	}

	public List<MapEntity> getProductFromLocal(String modelName)
			throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getProductByName(modelName);
	}

	public List<MapEntity> getModleByBrndId(String brndId) {
		if (brndId != null && brndId != "") {
			LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
			return localStoreDao.getModleByBrndId(brndId);
		}
		return new ArrayList<MapEntity>();
	}

}
