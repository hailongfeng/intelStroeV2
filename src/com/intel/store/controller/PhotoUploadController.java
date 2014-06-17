package com.intel.store.controller;

import java.util.ArrayList;

import android.content.Context;

import com.intel.store.model.PictureUploadModel;
import com.intel.store.util.PictureItem;
import com.intel.store.util.StoreSession;
import com.pactera.framework.controller.BaseController;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.Loger;

public class PhotoUploadController extends BaseController {

	public static final int PHOTO_UPLOAD_KEY = 1;


	/**
	 * 
	 * @param viewUpdateCallback
	 * @param alFileNames
	 * @param strComment
	 * @param strCategory
	 */
	@SuppressWarnings("unchecked")
	public void uploadPhoto(
			final UpdateViewAsyncCallback<ArrayList<PictureItem>> viewUpdateCallback,
			final ArrayList<PictureItem> lstPictures, Context context) {
		Loger.d("start uploadPhoto");
		DoAsyncTaskCallback<ArrayList<PictureItem>, ArrayList<PictureItem>> doAsyncTaskCallback = new DoAsyncTaskCallback<ArrayList<PictureItem>, ArrayList<PictureItem>>() {
			/**
			 * @param arg0
			 * @return successed:成功上传的图片数组
			 * @throws IException
			 */
			@Override
			public ArrayList<PictureItem> doAsyncTask(
					ArrayList<PictureItem>... arg0) throws IException {
				ArrayList<PictureItem> toUpload = arg0[0];
				ArrayList<PictureItem> successed = new ArrayList<PictureItem>();
				PictureUploadModel pictureUploadModel = new PictureUploadModel();
				Loger.d("pictureItems.size():"+toUpload.size());
				for (int index = 0; index < toUpload.size(); index++) {
					PictureItem tmp = toUpload.get(index);
					Boolean uploadSucc = pictureUploadModel
							.uploadPicture(tmp);
					if (uploadSucc) {
						if (tmp.mCategoryId.equals("2011")) {
							//店面照片
							StoreSession.STORE_INFO_CHANGED=true;
						}
						successed.add(toUpload.get(index));
					}
				}

				return successed;
			}
		};
		doAsyncTask(PHOTO_UPLOAD_KEY, viewUpdateCallback, doAsyncTaskCallback,
				lstPictures);
	}

/*	private void logPictureItems(ArrayList<PictureItem> lstPictures) {
		Loger.d("log pictureItem before saving ---- begin");
		for (int i = 0; i < lstPictures.size(); ++i) {
			PictureItem pictureItem = lstPictures.get(i);
			if (pictureItem != null) {
				Loger.d("pictureItem " + i + ": " + pictureItem.toString());
			}
		}
		Loger.d("log pictureItem before saving ---- end");
	}

	private void savePictureItemToFile(ArrayList<PictureItem> lstPictures,
			Context context) {
		String path = context.getFilesDir().getAbsolutePath();

		Loger.d("save path = " + path);
		for (int i = 0; i < lstPictures.size(); ++i) {
			PictureItem pictureItem = lstPictures.get(i);
			if (pictureItem != null) {
				FileHelper.saveToFile(context, pictureItem);
			}
		}
	}*/
}