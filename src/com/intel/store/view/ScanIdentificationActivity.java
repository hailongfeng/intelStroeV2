package com.intel.store.view;

import com.intel.store.R;
import com.intel.store.util.MyActivityInterface;
import com.pactera.framework.util.ToastHelper;
import com.zxing.activity.CaptureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author P0033759
 * 最后修改时间	2014-2-25-下午3:42:21
 * 功能	TODO
 */
public class ScanIdentificationActivity extends Activity implements MyActivityInterface,OnClickListener  {

	private static final int SCAN=1;
	Button btn_scan;
	Button btn_back;
	TextView txt_result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_identification);
		initView();
		setListener();
	}
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		btn_scan=(Button) findViewById(R.id.btn_scan);
		btn_back=(Button) findViewById(R.id.btn_back);
		txt_result=(TextView) findViewById(R.id.txt_result);
	}

	@Override
	public void setListener() {
		btn_scan.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan:
			// 打开扫描界面扫描条形码或二维码
			Intent intentScan = new Intent(ScanIdentificationActivity.this, CaptureActivity.class);
			startActivityForResult(intentScan, SCAN);
			break;
		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);	
		if (resultCode == Activity.RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			switch (requestCode) {
			case SCAN:
				txt_result.setText(scanResult);
				break;
			default:
				break;
			}

		} else {
			ToastHelper.getInstance().showShortMsg(getString(R.string.scan_give_up));
		}
	}
	
}