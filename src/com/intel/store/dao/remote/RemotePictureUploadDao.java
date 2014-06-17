package com.intel.store.dao.remote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import android.text.TextUtils;

import com.intel.store.util.Constants;
import com.intel.store.util.PictureItem;
import com.intel.store.util.StoreSession;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpPostUtil;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class RemotePictureUploadDao extends StoreRemoteBaseDao {
    
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static final String BOUNDARY = "-----------------------------214941364814433";
    private static final String LINE_END = "\r\n";
    
    public HttpResult uploadPicture(PictureItem pictureItem) throws TimeoutException, NetworkException {
        final String url = PICTURE_HOST + Constants.PICTURE_UPLOAD_SERVELET;
        Loger.i("httpurl:"+url);
        HttpPost httpPost = buildHttpUriRequest(url, pictureItem);
       // logHttpPost(httpPost);
        HttpPostUtil httpClient = new HttpPostUtil();
        HttpResult result = httpClient.executeRequest(httpPost);
        return result;
    }
    
    private HttpPost buildHttpUriRequest(String url, PictureItem pictureItem) {
        HttpPost post = new HttpPost(url);
        try {
            post.setHeader("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
            post.setEntity(buildEntity(pictureItem));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
    
    private ByteArrayEntity buildEntity(PictureItem pictureItem) throws IOException {
        ByteArrayEntity formEntity = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // set param
        outStream.write(setParams(getParams(pictureItem)));
        // set Image Content
        if (!TextUtils.isEmpty(pictureItem.mPictureFileName)) {
            outStream.write(imageContent(pictureItem.mPictureFileName, getFileName(pictureItem.mPictureFileName)));
        }
        byte[] data = outStream.toByteArray();
        outStream.flush();
        outStream.close();
        formEntity = new ByteArrayEntity(data);
        return formEntity;
    }
    
    private String getFileName(String strAbsolutePath) {
        return strAbsolutePath.substring(strAbsolutePath.lastIndexOf("/") + 1);
    }
    
    private HashMap<String, String> getParams(PictureItem pictureItem) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("sessionId",StoreSession.getJsessionId());
        params.put("slsprs_id", pictureItem.mSlsprsId);
        params.put("store_id", pictureItem.mStoreId);
        params.put("last_update_time", pictureItem.mWhen);
        params.put("photo_comment", pictureItem.mComment);
        params.put("loc_longitude", pictureItem.mLongitude);
        params.put("loc_latitude", pictureItem.mLatitude);
        params.put("role_id", pictureItem.mRoleId);
        params.put("photo_cat", "" + pictureItem.mCategoryId);
        params.put("rep_id", "" + pictureItem.mRep_id);
        if ("10".equals(pictureItem.mCategoryId)) {
            params.put("mdl_id", "" + pictureItem.mModelId);
        }
        if ("78".equals(pictureItem.mRoleId)) {
            params.put("cityType", pictureItem.mCityType);
        }
        params.put("stor_addr", pictureItem.mStoreAddr);
        
        return params;
    }
    
    private byte[] setParams(HashMap<String, String> params) throws IOException {
        StringBuffer textEntity = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        return textEntity.toString().getBytes();
    }
    
    private byte[] imageContent(String imgPath, String imgName) throws IOException {
        byte[] result = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        StringBuilder imageParamStringBuilder = new StringBuilder();
        imageParamStringBuilder.append("--" + BOUNDARY + "\r\n");
        imageParamStringBuilder.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + imgName
                + "\"\r\n");
        String imgType = getImageType(imgPath);
        if (imgType != null && imgType.equalsIgnoreCase("image/png") || imgType.equalsIgnoreCase("image/jpeg")) {
            imageParamStringBuilder.append("Content-Type: " + imgType + LINE_END);
            imageParamStringBuilder.append(LINE_END);
            outStream.write(imageParamStringBuilder.toString().getBytes());
            outStream.write(readFileImage(imgPath));
        }
        outStream.write(("\r\n--" + BOUNDARY + "--\r\n").getBytes());
        result = outStream.toByteArray();
        outStream.flush();
        outStream.close();
        return result;
    }
    
    private String getImageType(String imgPath) {
        String type = "";
        if (TextUtils.isEmpty(imgPath)) {
            return type;
        }
        String strTemp = imgPath.toLowerCase();
        if (strTemp.endsWith(".png")) {
            type = "image/png";
        } else if (strTemp.endsWith(".jpg") || strTemp.endsWith(".jpeg")) {
            type = "image/jpeg";
        } else if (strTemp.endsWith(".gif")) {
            type = "image/gif";
        }
        return type;
    }
    
    private byte[] readFileImage(String filename) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));
        int len = bufferedInputStream.available();
        byte[] bytes = new byte[len];
        int r = bufferedInputStream.read(bytes);
        if (len != r) {
            bytes = null;
            bufferedInputStream.close();
            throw new IOException("read file image error");
        }
        bufferedInputStream.close();
        return bytes;
    }

    @SuppressWarnings("unused")
	private void logHttpPost(HttpPost httpPost) {
        Loger.d("log http post begin");
        Loger.d("uri = " + httpPost.getURI());
        Header[] headers = httpPost.getAllHeaders();
        for (int i = 0; i < headers.length; ++i) {
            Loger.d("headers[" + i + "] = " + headers[i].getName() + " : " + headers[i].getValue());
        }
        Loger.d("===================输出Entity===================");
        HttpEntity entity = httpPost.getEntity();
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            is = entity.getContent();
            bis = new BufferedInputStream(is);
            int nBufferSize = 1024;
            StringBuilder bodyContent = new StringBuilder();
            byte[] buffer = new byte[nBufferSize];
            int len = -1;
            while ((len = bis.read(buffer, 0, nBufferSize)) != -1) {
                bodyContent.append(new String(buffer, 0, len));
            }
            Loger.d("body: \r\n" + bodyContent.toString());
            Loger.d("===================输出Entity结束===================");
        } catch (Exception e) {
        	Loger.d("=========输出Entity出错===");
            e.printStackTrace();
        } finally {
        	Loger.d("end: \r\n");
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}