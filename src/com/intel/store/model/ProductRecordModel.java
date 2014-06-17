package com.intel.store.model;

import java.util.ArrayList;

import com.intel.store.dao.local.LocalStoreDao;
import com.pactera.framework.exception.DBException;
import com.pactera.framework.model.MapEntity;

/**  
 * @Title: ProductRecordModel.java 
 * @Package: com.intel.store.model 
 * @Description:(用一句话描述该文件做什么) 
 * @author: fenghl 
 * @date: 2013年12月31日 上午9:36:37 
 * @version: V1.0  
 */
public class ProductRecordModel extends StoreBaseModel {
	public static final int MODEL_ID = 1;//model_id
	public static final int MODEL_NAME = 2;// model_name
	public static final int CREATE_TIME = 3;// create_time
	
	public ArrayList<MapEntity> getProductRecodeByName(
			String modelName) throws DBException {
		LocalStoreDao localStoreDao = LocalStoreDao.getInstance();
		return localStoreDao.getProductByName(modelName);
	}
	
}
