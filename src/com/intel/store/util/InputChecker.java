package com.intel.store.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.intel.store.model.ClerkModel;
import com.pactera.framework.model.MapEntity;

/**
 * InputChecker
 * 
 * @version 1.0.0
 */
public class InputChecker extends com.pactera.framework.util.InputChecker {
	protected static boolean isMatcher(String regex, String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static boolean isName(String str) {
		String regex = "[0-9a-zA-Z\u4E00-\u9FA5]+";
		return isMatcher(regex, str);
	}

	public static CheckResponse isRep(MapEntity clerkEntity) {
		Boolean checked = true;
		StringBuilder mcherk_info_ok = new StringBuilder();
		StringBuilder mcherk_info_error = new StringBuilder();
		String oprateRepId = clerkEntity.getString(ClerkModel.CREATER_REP_ID);
		String repName = clerkEntity.getString(ClerkModel.REP_NM);
		String repTel = clerkEntity.getString(ClerkModel.REP_TEL);
		String emailAddress = clerkEntity.getString(ClerkModel.REP_EMAIL);
		String storeId = clerkEntity.getString(ClerkModel.Store_ID);

		if (oprateRepId == null || oprateRepId.length() == 0) {
			mcherk_info_error.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_ask) + StoreApplication.getAppContext().getString(R.string.txt_input_check_msg_id) + "\n");
			checked = false;
		} else {
			mcherk_info_ok.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_msg_id)+"：" + oprateRepId + "\n");
		}
		if (repName == null || repName.length() == 0
				|| !InputChecker.isName(repName)) {
			mcherk_info_error.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_ask) + StoreApplication.getAppContext().getString(R.string.txt_input_check_name) + "\n");
			checked = false;
		} else {
			mcherk_info_ok.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_name)+"：" + repName + "\n");
		}
		if (repTel == null || !(InputChecker.checkMobile(repTel))) {
			mcherk_info_error.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_ask) + StoreApplication.getAppContext().getString(R.string.txt_input_check_mobile) + "\n");
			checked = false;
		} else {
			mcherk_info_ok.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_mobile)+"：" + repTel + "\n");
		}
		if (emailAddress == null || !(InputChecker.checkEmail(emailAddress))) {
			mcherk_info_error.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_ask) + StoreApplication.getAppContext().getString(R.string.txt_input_check_email) + "\n");
			checked = false;
		} else {
			mcherk_info_ok.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_email)+"：" + emailAddress + "\n");
		}
		if (storeId == null || storeId.length() == 0) {
			mcherk_info_error.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_ask) + StoreApplication.getAppContext().getString(R.string.txt_input_check_store_id) + "\n");
			checked = false;
		} else {
			mcherk_info_ok.append(StoreApplication.getAppContext().getString(R.string.txt_input_check_store_id)+"：" + storeId + "\n");
		}

		CheckResponse response = new CheckResponse();
		response.setResBoolean(checked);
		if (checked) {
			response.setResString(mcherk_info_ok);
		} else {
			response.setResString(mcherk_info_error);
		}
		return response;
	}

}
