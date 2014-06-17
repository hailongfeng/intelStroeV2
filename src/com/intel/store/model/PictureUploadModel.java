package com.intel.store.model;

import org.json.JSONException;

import com.intel.store.dao.remote.RemotePictureUploadDao;
import com.intel.store.util.FileHelper;
import com.intel.store.util.PictureItem;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class PictureUploadModel extends StoreBaseModel {

	public Boolean uploadPicture(PictureItem pictureItem) throws TimeoutException, NetworkException {
		try {
			HttpResult result = new RemotePictureUploadDao()
					.uploadPicture(pictureItem);
			String response = preParseHttpResult(result);
			Loger.d(response);
			preParseResponse(response);
			Loger.d("delete " + pictureItem.mPictureFileName);
			if (pictureItem.mPictureFileName != null) {
				FileHelper.deleteFile(pictureItem.mPictureFileName);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
