package com.intel.store.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.intel.store.R;
import com.intel.store.util.ViewHelper;
import com.pactera.framework.util.ToastHelper;

public class ExerciseDetailActivity extends BaseActivity {

	private Button btn_back;
	private Button btn_exercise_take;
	private ViewHelper mViewHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_detail);
		int itemId = getIntent().getIntExtra("id", 0);
		ToastHelper.getInstance().showShortMsg(itemId + "");
		initView();
		setListener();
	}

	protected void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_exercise_take.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewHelper.showBTNDialog("加入成功", "信息提示");
			}
		});
	}

	protected void initView() {
		mViewHelper = new ViewHelper(ExerciseDetailActivity.this);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_exercise_take = (Button) findViewById(R.id.btn_exercise_take);
	}

}
