/**   
 * @Title: MyClerkEditController.java 
 * @Package com.intel.store.controller 
 * @Description: TODO(编辑保存我的店员资料)
 * @author P0033759
 * @date 2013-10-18 下午2:40:35 
 * @version V1.0   
 */
package com.intel.store.controller;

import com.intel.store.model.PersonalInfoEditModel;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;

/**
 * @author P0033759
 * 
 */
/**
 * @author P0033759
 * 
 */
public class PersonalInfoEditorController extends BaseController {

	private PersonalInfoEditModel editModel;

	private static final int MyPersonalInfo_Edit_KEY = 30;
	private static final int MyClerkInfo_Edit_KEY = 31;
	private static final int MyClerk_Add_KEY = 32;

	public PersonalInfoEditorController() {
		this.editModel = new PersonalInfoEditModel();
	}

	/**
	 * 编辑我的店员信息
	 */
	public void editMyClerkFromRemote(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String[] strings) {

		doAsyncTask(MyClerkInfo_Edit_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return editModel.Edit_MyClerkInfo_FromRemote(params[0],
								params[1]);
					}
				}, strings);

	}

	/**
	 * 取消修改我的员工
	 */
	public void cancleEditMyClerkFromRemote() {
		cancel(MyClerkInfo_Edit_KEY);
	}

	/**
	 * 增加我的店员
	 */
	public void addMyClerkFromRemote(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String[] strings) {

		doAsyncTask(MyClerk_Add_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return editModel.Add_MyClerk_FromRemote(params[0],
								params[1]);
					}
				}, strings);

	}

	/**
	 * 取消我的店员
	 */
	public void cancleAddMyClerkFromRemote() {
		cancel(MyClerk_Add_KEY);
	}

	/**
	 * 修改我的信息
	 */
	public void editMyPersonalInfoFromRemote(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			String[] strings) {

		doAsyncTask(MyClerkInfo_Edit_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						return editModel.Edit_MyClerkInfo_FromRemote(params[0],
								params[1]);
					}
				}, strings);

	}

	/**
	 * 取消修改我的信息
	 */
	public void cancleEditMyPersonalInfoFromRemote() {
		cancel(MyPersonalInfo_Edit_KEY);
	}

}
