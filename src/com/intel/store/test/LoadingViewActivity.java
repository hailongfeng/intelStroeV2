package com.intel.store.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.intel.store.R;
import com.intel.store.widget.LoadingView;

public class LoadingViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_view);
		final LoadingView lv=(LoadingView) findViewById(R.id.common_id_ll_loading);
		Button button=(Button) findViewById(R.id.btn_test_lv);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lv.showLoading();
			}
		});
	}

}
