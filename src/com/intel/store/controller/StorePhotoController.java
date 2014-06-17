package com.intel.store.controller;

import java.util.ArrayList;
import java.util.List;

import com.intel.store.model.QueryPicCategoryModel;
import com.intel.store.model.QueryPicDateModel;
import com.intel.store.model.QueryPicModel;
import com.intel.store.model.QueryPicRoundModel;
import com.intel.store.model.StorePhotoModel;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

public class StorePhotoController extends BaseController {

	private static final int QUERY_PIC_DATE_KEY = 1;
    private static final int REMOTE_STORE_PHOTO_DEL_KEY = 2;
    private static final int QUERY_PIC_BY_DATE_KEY = 3;
	private static final int QUERY_PIC_CATEGORY_KEY = 4;
	private static final int QUERY_CURRENT_QUARTER_KEY = 5;
	private static final int QUERY_PIC_ROUND_KEY = 6;
	private static final int QUERY_HISTORYWV = 7;
	private static final int QUERY_PIC_CAT_KEY = 8;
	private static final int LIST_PHOTO_BY_WV_CATEGORY = 9;


    
    /**删除指定id的照片
     * @param viewUpdateCallback
     * @param pic_id
     */
    public void delPictureByIdFromRemote(final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
    		final String pic_id) {
    	doAsyncTask(REMOTE_STORE_PHOTO_DEL_KEY, viewUpdateCallback, new DoAsyncTaskCallback<String, Boolean>() {

			@Override
			public Boolean doAsyncTask(String... arg0) throws IException {
				return new StorePhotoModel().delPictureByid(arg0[0]);
			}
		}, pic_id);
    }




	/**获取指定年月日的照片列表
	 * @param viewUpdateCallback
	 * @param params
	 */
	public void queryPicByDate(
			final UpdateViewAsyncCallback<ArrayList<MapEntity>> viewUpdateCallback,
			final String params[]) {
		doAsyncTask(QUERY_PIC_BY_DATE_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, ArrayList<MapEntity>>() {
					@Override
					public ArrayList<MapEntity> doAsyncTask(String... params)
							throws IException {
						return new QueryPicModel().queryPicByDate(params[0], params[1],params[2]);
					}
				},params);
	}

	public void cancelQueryPicByDate() {
		cancel(QUERY_PIC_BY_DATE_KEY);
	}



	/** 按日期获取图片的张数
	 * @param viewUpdateCallback
	 * @param params
	 */
	public void queryPicDate(
			final UpdateViewAsyncCallback<ArrayList<MapEntity>> viewUpdateCallback,
			final String params[]) {
		doAsyncTask(QUERY_PIC_DATE_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, ArrayList<MapEntity>>() {
					@Override
					public ArrayList<MapEntity> doAsyncTask(String... params)
							throws IException {
						return new QueryPicDateModel().queryPicDateModel(params[0],params[1]);
					}
				},params);
	}
	/** 
	 * 获取当前季度
	 * @param viewUpdateCallback
	 * @param params
	 */
	public void queryCurrentQuarter(
			final UpdateViewAsyncCallback<MapEntity> viewUpdateCallback,
			final String params[]) {
		doAsyncTask(QUERY_CURRENT_QUARTER_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, MapEntity>() {
			@Override
			public MapEntity doAsyncTask(String... params)
					throws IException {
				return new QueryPicRoundModel().queryCurrentQuarter();
			}
		},params);
	}
	/** 
	 * 获取季度的图片上传轮次
	 * @param viewUpdateCallback
	 * @param params
	 */
	public void queryPicRound(
			final UpdateViewAsyncCallback<ArrayList<MapEntity>> viewUpdateCallback,
			final String param) {
		doAsyncTask(QUERY_PIC_ROUND_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, ArrayList<MapEntity>>() {
			@Override
			public ArrayList<MapEntity> doAsyncTask(String... params)
					throws IException {
				return new QueryPicRoundModel().listWvByQuarter(param);
			}
		},param);
	}
	/** 
	 * 获取轮次的图片类型
	 * @param viewUpdateCallback
	 * @param params
	 */
	public void listPhotoByStoreWv(
			final UpdateViewAsyncCallback<ArrayList<MapEntity>> viewUpdateCallback,
			final String param) {
		doAsyncTask(QUERY_PIC_CAT_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, ArrayList<MapEntity>>() {
			@Override
			public ArrayList<MapEntity> doAsyncTask(String... params)
					throws IException {
				return new QueryPicCategoryModel().listCategoryByWvModel(param);
			}
		},param);
	}

	public void cancelQueryPicDate() {
		cancel(QUERY_PIC_DATE_KEY);
	}
	
	

	public void queryPicCategory(
			final UpdateViewAsyncCallback<ArrayList<MapEntity>> viewUpdateCallback) {
		doAsyncTask(QUERY_PIC_CATEGORY_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, ArrayList<MapEntity>>() {
					@Override
					public ArrayList<MapEntity> doAsyncTask(String... params)
							throws IException {
						Loger.d("queryPicCategoryController");
						return new QueryPicCategoryModel().queryPicCategoryModel();
					}
				});
	}

	
	public void queryHistoryWv(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback) {
		doAsyncTask(QUERY_HISTORYWV, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
					@Override
					public ArrayList<MapEntity> doAsyncTask(String... params)
							throws IException {
						Loger.d("queryPicCategoryController");
						return new QueryPicRoundModel().queryHistoryWv();
					}
				});
	}
	
	public void cancelQueryqueryPicCategory() {
		cancel(QUERY_PIC_CATEGORY_KEY);
	}
	public void listPhotoByWvCategory(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback,
			final String wvId,final String category) {
		doAsyncTask(LIST_PHOTO_BY_WV_CATEGORY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, List<MapEntity>>() {
			@Override
			public ArrayList<MapEntity> doAsyncTask(String... params)
					throws IException {
				Loger.d("queryPicCategoryController");
				return new QueryPicModel().listPhotoByWvCategory(wvId,category);
			}
		},wvId,category);
	}
	

}
