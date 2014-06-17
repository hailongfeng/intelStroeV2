/**  
 * @Package com.intel.store.model 
 * @FileName: UploadLatestDate.java 
 * @Description:
 * @author fenghl
 * @date 2013年10月23日 上午9:18:22 
 * @version V1.0  
 */
package com.intel.store.model;

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.intel.store.util.Constants;
import com.intel.store.util.DownLoadUitl;
import com.intel.store.util.StoreSession;
import com.intel.store.util.ZipUtil;
import com.intel.store.view.ProductFindActivityOld;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Utils;

/**
 * @ClassName: UploadLatestDate
 * @Description: (这里用一句话描述这个类的作用)
 * @author fenghl
 * @date 2013年10月23日 上午9:18:22
 */
public class UpdateDataVersion {

	// private Context context;
	SaleWord saleWord;
	private Handler handler;

	public UpdateDataVersion(Context context, Handler handler) {
		super();
		saleWord=new SaleWord(context);
		this.handler = handler;
	}

	public void update(MapEntity result) {
		// 有更新，要下载更新
		Loger.i("网络下载");
		String fileUrl = result.getString(CheckTarVersionModel.FILEURL);
		String host = Utils.getNetConfigProperties().getProperty(
				StoreSession.getLine());
		String upgradeVersionUrl = host + "getUpdateAPKServlet?fileurl="
				+ fileUrl;
		Loger.d("upgradeVersionUrl=" + upgradeVersionUrl);
		new UpdateVersionThread(result, upgradeVersionUrl).start();
	}

	public void LoadDataFromLocal() {
		if (saleWord.isLocalDataExit()) {
			handler.sendEmptyMessage(ProductFindActivityOld.UPDATE_SUCCESS);
		}
	}

	private class UpdateVersionThread extends Thread {

		MapEntity result;
		String upgradeVersionUrl;

		public UpdateVersionThread(MapEntity result, String upgradeVersionUrl) {
			super();
			this.upgradeVersionUrl = upgradeVersionUrl;
			this.result = result;
		}

		@Override
		public void run() {
			super.run();
			try {
				File zipFile = new File(saleWord.getZipFilePath());
				if (!zipFile.exists()) {
					zipFile.createNewFile();
				}
				long downloadSize = DownLoadUitl.downloadFile(
						upgradeVersionUrl, zipFile);
				if (downloadSize > 0) {
					StoreSession.setDataVersion(result
							.getString(CheckTarVersionModel.VERSION));
					ZipUtil.UnZipFolder(saleWord.getZipFilePath(),
							saleWord.getCacheDir());
					handler.sendEmptyMessage(ProductFindActivityOld.UPDATE_SUCCESS);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				Message message = handler.obtainMessage();
				message.obj = ex.getMessage();
				message.what = ProductFindActivityOld.UPDATE_FAIL;
				handler.sendMessage(message);
			}
		}
	}

	private class SaleWord {

		// private Context mContext;
		private String cacheDir;
		private String zipFilePath;
		private String htmlFilePath;
		private String dbFilePath;
		private static final String dbName = "product.db";

		public SaleWord(Context mContext) {
			super();
			// this.mContext = mContext;
			cacheDir = mContext.getFilesDir().getParent() + File.separator
					+ Constants.SALE_WORD_DIR;
			Loger.d(cacheDir);
			File cache=new File(cacheDir);
			if(!cache.exists()){
				cache.mkdirs();
			}
			zipFilePath = cacheDir + File.separator
					+ Constants.SALE_WORD_ZIP_FILENAME;
			htmlFilePath = cacheDir + File.separator
					+ Constants.SALE_WORD_HTML_FILENAME;
			dbFilePath = cacheDir + File.separator + dbName;
		}

		public boolean isLocalDataExit() {
			if (!isFileExit(getDbFilePath()) || !isFileExit(getHtmlFilePath())) {
				if (!isFileExit(getZipFilePath())) {

					return false;
				} else {
					try {
						ZipUtil.UnZipFolder(saleWord.getZipFilePath(),
								saleWord.getCacheDir());
						return true;
					} catch (Exception e) {
						Loger.d(e.getMessage());
						return false;
					}

				}
			} else {
				return true;
			}

		}

		public boolean isFileExit(String fileName) {
			File file = new File(fileName);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		}

		public String getZipFilePath() {
			return zipFilePath;
		}

		public String getHtmlFilePath() {
			return htmlFilePath;
		}

		public String getDbFilePath() {
			return dbFilePath;
		}

		public String getCacheDir() {
			return cacheDir;
		}

	}
}
