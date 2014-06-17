package com.intel.store.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.model.ClerkModel;
import com.intel.store.util.MyActivityInterface;
import com.intel.store.util.StoreSession;
import com.intel.store.widget.EmailTextView;
import com.pactera.framework.model.MapEntity;
import com.pactera.framework.util.Loger;

/**
 * @author P0033759
 * 
 */
public class StoreMyClerkDetailActivity extends BaseActivity implements
		MyActivityInterface, OnClickListener {
	private MapEntity mclerkInfo;
	private Bundle mbundle;
	private Button mbtn_back;
	private Button mbtn_myclerk_edit;
	private TextView mtxt_username;
	private TextView mtxt_clerk_phone_number;
	private TextView mtxt_clerk_position;
	private EmailTextView mtxt_clerk_email;
	private int EDIT_MYCLERK_REQUEST = 1;
	private Boolean myClerkChanged = false;
	private String mRoleId=StoreSession.getRoleId();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_my_clerk_detial);
		mbundle = getIntent().getExtras();
		if (mclerkInfo==null) {
			mclerkInfo = (MapEntity) mbundle.getSerializable("clerkInfo");
		}
		initView();
		setListener();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
			setIconEnable(menu, true);
			getMenuInflater().inflate(R.menu.my_clerk_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bar_menu_item_edit:
			Intent intentStoreMyClerkEdit = new Intent();
			intentStoreMyClerkEdit.setClass(StoreMyClerkDetailActivity.this,
					StoreMyClerkEditActivity.class);
			mbundle.remove("clerkInfo");
			mbundle.putSerializable("clerkInfo", mclerkInfo);
			intentStoreMyClerkEdit.putExtras(mbundle);
			startActivityForResult(intentStoreMyClerkEdit, EDIT_MYCLERK_REQUEST);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void initView() {
		mbtn_myclerk_edit = (Button) findViewById(R.id.btn_myclerk_edit);
		mbtn_back = (Button) findViewById(R.id.btn_back);

		mtxt_username = (TextView) findViewById(R.id.txt_username);
		mtxt_clerk_phone_number = (TextView) findViewById(R.id.txt_clerk_phone_number);
		mtxt_clerk_position = (TextView) findViewById(R.id.txt_clerk_position);
		mtxt_clerk_email = (EmailTextView) findViewById(R.id.txt_clerk_email);

		mtxt_username.setText(mclerkInfo
				.getString(ClerkModel.REP_NM));
		mtxt_clerk_phone_number.setText(""
				+ mclerkInfo.getString(ClerkModel.REP_TEL));
		String clerkPosition= mclerkInfo
				.getString(ClerkModel.REP_ASGN_ROLE_NM);
		/*if (clerkPosition!=null&&clerkPosition.equalsIgnoreCase("RSP")) {
			clerkPosition=getString(R.string.txt_rsp);
		}else{
		}*/
		mtxt_clerk_position
				.setText(clerkPosition);
		mtxt_clerk_email.setText(""
				+ mclerkInfo.getString(ClerkModel.REP_EMAIL));
		// 为ListView注册ContextMenu，roleId为1是经理，设置可以编辑
		if (mRoleId!=null&&mRoleId.equalsIgnoreCase(ClerkModel.REP_MANAGER)) {
			mbtn_myclerk_edit.setVisibility(View.VISIBLE);
		}
		else {
			mbtn_myclerk_edit.setVisibility(View.GONE);
		}
	}

	@Override
	public void setListener() {
		mbtn_back.setOnClickListener(this);
		mbtn_myclerk_edit.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			Intent intent = new Intent();
			if (myClerkChanged) {
				setResult(RESULT_OK, intent);
				Loger.d("setResult(RESULT_OK)");
			} else {
				Loger.d("setResult(RESULT_CANCELED)");
				setResult(RESULT_CANCELED, intent);
			}
			finish();
			break;
		case R.id.btn_myclerk_edit:
			Intent intentStoreMyClerkEdit = new Intent();
			intentStoreMyClerkEdit.setClass(StoreMyClerkDetailActivity.this,
					StoreMyClerkEditActivity.class);
			mbundle.remove("clerkInfo");
			mbundle.putSerializable("clerkInfo", mclerkInfo);
			intentStoreMyClerkEdit.putExtras(mbundle);
			startActivityForResult(intentStoreMyClerkEdit, EDIT_MYCLERK_REQUEST);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == EDIT_MYCLERK_REQUEST) {
			Loger.d("EDIT_MYCLERK_REQUEST");
			switch (resultCode) {
			case RESULT_OK:
				Loger.d("RESULT_OK");
				myClerkChanged = true;
				mclerkInfo=null;
				if (data!=null) {
					if ( data.getExtras()!=null) {
						mclerkInfo=(MapEntity) data.getExtras().getSerializable("clerkInfo");
					}
				}
				if (mclerkInfo==null) {
					Intent intent = new Intent();
					if (myClerkChanged) {
						setResult(RESULT_OK, intent);
						Loger.d("setResult(RESULT_OK)");
					} else {
						setResult(RESULT_CANCELED, intent);
					}
					this.finish();
				}else {
					initView();
				}
				break;
			default:
				break;
			}
		}
	}

}
