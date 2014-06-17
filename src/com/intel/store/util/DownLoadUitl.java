package com.intel.store.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadUitl {

	public static long downloadFile(String downloadUrl, File saveFile) throws Exception {
		int currentSize = 0;
		long totalSize = 0;

		final int CONN_TIMEOUT = 60000;
		final int READ_TIMEOUT = 60000;

		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			String tempFileName="temp_"+saveFile.getName();
			File tempFile=new File(saveFile.getParentFile().getAbsolutePath()+File.separator+tempFileName);
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes=" + currentSize + "-");
			}
			httpConnection.setConnectTimeout(CONN_TIMEOUT);
			httpConnection.setReadTimeout(READ_TIMEOUT);
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("download error 404");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(tempFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
			}
			tempFile.renameTo(saveFile);
			tempFile.deleteOnExit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpConnection != null) {
				httpConnection.disconnect();
			}
			if (is != null) {
				is.close();
			}
			if (fos != null) {
				fos.close();
			}
		}
		return totalSize;
	}
}
