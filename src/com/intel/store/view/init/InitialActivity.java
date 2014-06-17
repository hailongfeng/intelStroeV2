package com.intel.store.view.init;

import com.intel.store.R;
import com.intel.store.view.LoginActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class InitialActivity extends Activity {
	private Handler handler;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_initial);
		super.onCreate(savedInstanceState);
		handler=new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
					jump();
			}
		}, 2000);
		
	}
	void jump(){
		Intent intent = new Intent(InitialActivity.this,LoginActivity.class);
		startActivity(intent);
		InitialActivity.this.finish();
	}
	
	
}
