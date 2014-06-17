package com.intel.store.dao.remote;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.intel.store.util.Constants;
import com.pactera.framework.exception.NetworkException;
import com.pactera.framework.exception.TimeoutException;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.http.HttpsPostUtil;
import com.pactera.framework.util.Loger;

public class IrepRemoteDao extends StoreRemoteBaseDao {


	public HttpResult irepLogin(String userName, String password)
			throws TimeoutException, NetworkException {

		final String url = Constants.IREP_API_HTTP_HOST + Constants.IREP_API_AUTH_LOGIN;
		Loger.d("url:" + url);
		HashMap<String, String> hmParams = new HashMap<String, String>();
		hmParams.put("username", userName);
		hmParams.put("password", password);
		HttpPost post = buildHttpUriRequest(url, hmParams);
		return new HttpsPostUtil().executeRequest(post);
	}

	private HttpPost buildHttpUriRequest(String uri,
			HashMap<String, String> hmParams) {
		HttpPost post = new HttpPost(uri);
		post.addHeader("Accept", "application/json");
		post.addHeader("Accept-Charset","UTF-8");
		post.addHeader("dataType", "json");
		post.addHeader("Mobile", "1");
		post.addHeader("PaxUID", "");

		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		Iterator<Entry<String, String>> iter = hmParams.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return post;
	}

}
