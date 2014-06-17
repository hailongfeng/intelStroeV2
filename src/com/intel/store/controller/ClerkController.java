package com.intel.store.controller;

import java.util.List;

import com.intel.store.model.ClerkModel;
import com.intel.store.model.LoginModel;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.model.MapEntity;

public class ClerkController extends BaseController {
	private ClerkModel clerkModel;
	
	/**	登录 */
	private static final int LOGIN_KEY = 2;
	/**	退出登录 */
	private static final int LOGOUT_KEY = 3;
	/**	增加店员 */
	private static final int ADD_CLERK_KEY = 4;
	/**	删除店员 */
	private static final int DEL_CLERK_KEY = 5;
	/**	修改店员 */
	private static final int MODEF_CLERK_KEY = 6;
	/**	获取我的店员 */
	private static final int GET_MY_CLERK_KEY = 7;
	
	public void loginFromRemote(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			final String userName, final String password, final String userType) {
		final LoginModel loginModel = new LoginModel();
		doAsyncTask(LOGIN_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<String, Boolean>() {
					@Override
					public Boolean doAsyncTask(String... params)
							throws IException {
						// throw new ServerException();
						return loginModel.loginFromRemote(params[0], params[1],
								params[2]);
					}
				}, userName, password, userType);

	}

	public void logout(UpdateViewAsyncCallback<Boolean> viewUpdateCallback) {
		final LoginModel loginModel = new LoginModel();
		doAsyncTask(LOGOUT_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<Void, Boolean>() {

					@Override
					public Boolean doAsyncTask(Void... params)
							throws IException {
						return loginModel.logout();
					}
				});
	}

	/**
	 * cancel logout
	 */
	public void cancelLogout() {
		cancel(LOGOUT_KEY);
	}

	public void addClerk(
			final UpdateViewAsyncCallback<String> viewUpdateCallback,
			MapEntity clerkEntity) {
		doAsyncTask(ADD_CLERK_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<MapEntity, String>() {
					@Override
					public String doAsyncTask(MapEntity... arg0)
							throws IException {
						clerkModel = new ClerkModel();
						return clerkModel.addMyClerk(arg0[0]);
					}
				}, clerkEntity);

	}
	public void modefyClerk(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			MapEntity clerkEntity) {
		doAsyncTask(MODEF_CLERK_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<MapEntity, Boolean>() {
			@Override
			public Boolean doAsyncTask(MapEntity... arg0)
					throws IException {
				clerkModel = new ClerkModel();
				return clerkModel.modefyMyClerk(arg0[0]);
			}
		}, clerkEntity);
		
	}
	public void delClerk(
			final UpdateViewAsyncCallback<Boolean> viewUpdateCallback,
			MapEntity clerkEntity) {
		doAsyncTask(DEL_CLERK_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<MapEntity, Boolean>() {
			@Override
			public Boolean doAsyncTask(MapEntity... arg0)
					throws IException {
				clerkModel = new ClerkModel();
				return clerkModel.delMyClerk(arg0[0]);
			}
		}, clerkEntity);
		
	}

	@Override
	public void cancel(int asyncTaskKey) {
		super.cancel(asyncTaskKey);
	}
	
	public void getMyClerk(
			final UpdateViewAsyncCallback<List<MapEntity>> viewUpdateCallback) {
		doAsyncTask(GET_MY_CLERK_KEY, viewUpdateCallback,
				new DoAsyncTaskCallback<MapEntity, List<MapEntity>>() {
			@Override
			public List<MapEntity> doAsyncTask(MapEntity... arg0)
					throws IException {
				clerkModel = new ClerkModel();
				return clerkModel.getMyClerkListFromRemote();
			}
		});
		
	}
	
}
