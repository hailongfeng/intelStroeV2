package com.intel.store.test;

import android.app.Activity;
import android.os.Bundle;
import com.intel.store.R;
import com.pactera.framework.http.HttpGetUtil;
import com.pactera.framework.http.HttpResult;
import com.pactera.framework.util.Loger;

public class HttpTestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_http_test);

		for (int i = 0; i < 50; i++) {
			new Thread(new Runnable() {
				public void run() {
//					try {
//						HttpResult result = new HttpGetUtil()
//								.executeRequest("http://www.baidu.com/",0);
//						Loger.d(result.getResponse());
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
				}
			}).start();
		}
	}

}
