/**  
* @Package com.intel.store.controller 
* @FileName: LatestExerciseListByPageController.java 
* @Description:
* @author fenghl
* @date 2013年10月15日 上午10:03:47 
* @version V1.0  
*/ 
package com.intel.store.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.model.PageEntity;
import com.pactera.framework.util.page.AbstractDataController;

/**
 * @ClassName: LatestExerciseListByPageController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author fenghl
 * @date 2013年10月15日 上午10:03:47
 */
public class LatestExerciseListByPageController extends AbstractDataController {

	public static final int ID = 1;
	public static final int NAME = 2;
	public static final int TIME = 3;
	public static final int IMAGE = 4;

	@Override
	public PageEntity getPrePageData(int page, int size) throws IException {
		PageEntity result = new PageEntity();
		List<MapEntity> data = getDateByPage(page,  size);
		result.setPageData(data);
		result.setTotal(100);
		return result;
	}
	
	private List<MapEntity> getDateByPage(int page, int size) {
		int start=(page-1)*size;
		int end=page*size;
		List<MapEntity> data = new ArrayList<MapEntity>();
		for (int i = start; i < end; i++) {
			MapEntity mapEntity = new MapEntity();
			mapEntity.setValue(ID, i);
			mapEntity.setValue(NAME, "活动" + i);
			mapEntity.setValue(TIME,new Date().toLocaleString());
			mapEntity.setValue(IMAGE, "http://g.hiphotos.baidu.com/pic/w%3D230/sign=5f21b571d62a60595210e6191835342d/a2cc7cd98d1001e9372f4db7b90e7bec54e79772.jpg");
			data.add(mapEntity);
		}
		return data;
	}

}
