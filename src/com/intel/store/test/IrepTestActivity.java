package com.intel.store.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.controller.StoreController;
import com.intel.store.view.StoreCommonUpdateView;
import com.pactera.framework.exception.IException;
import com.pactera.framework.util.Loger;

public class IrepTestActivity extends Activity {
	private StoreController storeController;
	private	StoreSalesCountUpdateView storeSalesCountUpdateView;

	TextView login_result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_irep_test);
		login_result=(TextView) findViewById(R.id.txt_login_result);
		storeSalesCountUpdateView = new StoreSalesCountUpdateView(this);
		storeController = new StoreController();
		//String username="prc@testuser.com";
	//	String password="100%intel";
		Loger.d("请求------------");
	//	storeController.irepLogin(storeSalesCountUpdateView, username,password);
	}


	private class StoreSalesCountUpdateView extends
			StoreCommonUpdateView<String> {

		public StoreSalesCountUpdateView(Context context) {
			super(context);
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		public void onException(IException arg0) {
		}

		@Override
		public void onPostExecute(String arg0) {
			Loger.d("返回------------");
			login_result.setText(arg0);
		}

		@Override
		public void handleException(IException ex) {
			// TODO Auto-generated method stub
			
		}

	}
}
