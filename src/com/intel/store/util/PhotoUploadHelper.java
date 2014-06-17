package com.intel.store.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.intel.store.StoreApplication;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.PictureUtil;
import com.pactera.framework.util.ToastHelper;

public class PhotoUploadHelper {
	public static int getUnuploadedPictureNumber(String fileFolder) {
		ArrayList<String> fileNames = getToUploadFileNames(fileFolder);
		return fileNames == null ? 0 : fileNames.size();
	}

	public static ArrayList<PictureItem> getUnuploadedPictureItems(
			String fileFolder) {
		ArrayList<String> filesToUpload = getToUploadFileNames(fileFolder);
		return inflatePictureItems(filesToUpload);
	}

	private static ArrayList<String> getToUploadFileNames(String fileFolder) {
		ArrayList<String> filesToUpload = new ArrayList<String>();
		File folder = new File(fileFolder + File.separator + "pck");
		Loger.d("send mFileFoldler = " + fileFolder + File.separator + "pck");
		if (folder.exists() && folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; ++i) {
				File file = files[i];
				if (file.getName().endsWith(".pck")) {
					filesToUpload.add(file.getAbsolutePath());
				}
			}
		}
		return filesToUpload;
	}

	private static ArrayList<PictureItem> inflatePictureItems(
			ArrayList<String> lstFileNames) {
		ArrayList<PictureItem> pictureItems = new ArrayList<PictureItem>();
		for (int i = 0; i < lstFileNames.size(); ++i) {
			PictureItem pictureItem = FileHelper
					.inflatePictureItemFromFile(lstFileNames.get(i));
			pictureItems.add(pictureItem);
		}
		return pictureItems;
	}

	public static ArrayList<PictureItem> getStoreLoad(
			ArrayList<PictureItem> pictures) {
		if (pictures.size() == 0) {
			return null;
		}
		ArrayList<PictureItem> result = new ArrayList<PictureItem>();
		PictureItem first = pictures.get(0);
		String storeId = first.mStoreId;
		for (int i = 0; i < pictures.size(); ++i) {
			if (storeId.equals(pictures.get(i).mStoreId)) {
				result.add(pictures.get(i));
			}
		}
		for (int i = pictures.size() - 1; i >= 0; --i) {
			if (storeId.equals(pictures.get(i).mStoreId)) {
				pictures.remove(i);
			}
		}
		return result;
	}

	public static void deletePictureItem(PictureItem pictureItem) {
		String absolutePckPath = pictureItem.mAbsolutePckFileName;
		FileHelper.deleteFile(absolutePckPath);
	}

	public static String createImageFile(String storeId, String categoryId)
			throws IOException {
		Date current = new Date();
		String strTime = new SimpleDateFormat("yyyyMMddHHmmss").format(current);
		String strMili = new SimpleDateFormat("SSS").format(current);
		String rand = String.format("%d", (int) (Math.random() * 10))
				+ String.format("%d", (int) (Math.random() * 10));
		String imageFileName = "big_mob_a_" + storeId + "_" + categoryId + "_"
				+ strTime + "_" + strMili + "_" + rand + ".jpg";
		// pictures/ConfigProperties.FILE_PATH;
		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		String absolutePicturePath = image.getAbsolutePath();
		return absolutePicturePath;
	}

	public static String preparePhoto(String absolutePicturePath,
			String categoryName, long currentMiliSec) {
		if (absolutePicturePath != null) {
			FileOutputStream fos = null;
			Bitmap bm = null;
			Bitmap bitmap = null;
			try {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(absolutePicturePath, options);
				final int height = options.outHeight;
				final int width = options.outWidth;
				int reqHWidth = 720;
				int reqHeight = 1280;
				if (width > height) {
					Loger.d("-----------交换宽高");
					int temp = reqHWidth;
					reqHWidth = reqHeight;
					reqHeight = temp;
				}
				File f = new File(absolutePicturePath);
				// 获取小图，再变幻颜色，最后加水印，否则容易OOM
				bm = PictureUtil.getSmallBitmap(absolutePicturePath, reqHWidth,
						reqHeight);
				bm = WaterMarkUtil.getMutableBitmap(bm);
				bitmap = WaterMarkUtil.addWaterMark(bm, categoryName,
						currentMiliSec);

				// 把bitmap图片的数据流传入文件nFile
				File nFile = new File(PictureUtil.getAlbumDir(), f.getName()
						.substring(4));
				
				fos = new FileOutputStream(nFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 40, fos);
				
				// 把bitmap图片的数据缓存，方便马上查看
				if (StoreApplication.bitmapCache!=null) {
					StoreApplication.bitmapCache.addBitmap(absolutePicturePath, bitmap);
				}else {
					Loger.e("StoreApplication.BitmapCache null error");
				}
				// 回收资源
				if (null != bm) {
					bm.recycle();
					bm = null;
				}

				FileInputStream fis = new FileInputStream(nFile);
				Loger.d("压缩后大小：" + fis.available() / 1024 + "K");
				fis.close();

				return nFile.getPath();
			} catch (Exception e) {
				ToastHelper.getInstance().showShortMsg(e.getMessage());
				e.printStackTrace();
			} finally {
				if (null != fos) {
					try {
						fos.close();
					} catch (IOException e) {
					}
				}
				if (null != bm) {
					bm.recycle();
					bm = null;
				}
				if (null != bitmap) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
		return null;
	}

}