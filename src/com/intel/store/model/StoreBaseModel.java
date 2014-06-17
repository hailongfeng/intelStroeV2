package com.intel.store.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.pactera.framework.exception.ServerException;
import com.pactera.framework.model.BaseModel;

public class StoreBaseModel extends BaseModel {

	@Override
	protected JSONObject preParseResponse(String response)
			throws JSONException, ServerException {
		if (response != null && !"".equals(response.trim())) {
			JSONObject jsonRes = new JSONObject(response);
			if (jsonRes.has("status")) {
				JSONObject status = jsonRes.getJSONObject("status");
				if ("true".equals(status.getString("success"))) {
					return jsonRes;
				} else {
					throw new ServerException(status.getString("errorCode"),
							status.getString("errorMsg"));
				}
			} else {
				throw new ServerException("-1", StoreApplication.getAppContext().getString(R.string.comm_txt_server_error));
			}
		} else {
			throw new ServerException("-1", StoreApplication.getAppContext().getString(R.string.comm_txt_server_error));
		}
	}
}
