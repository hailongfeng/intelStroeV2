package com.intel.store.view.setting;

import java.util.ArrayList;

import com.intel.store.R;
import com.intel.store.util.StoreSession;
import com.intel.store.util.UpgradeProgress;
import com.intel.store.view.BaseActivity;
import com.intel.store.view.webview.PrivacyPolicyActivity;
import com.pactera.framework.util.Version;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SettingAboutActivity extends BaseActivity {

	private TextView tv_app_version;
	private ListView lv_setting;
	private ArrayList<AboutItem> dataList;
	private SettingAboutAdapter settingAboutAdapter;
	private TextView txt_private;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_about);
		initView();
		setListener();
	}

	protected void initView() {
		tv_app_version=(TextView) findViewById(R.id.tv_app_version);
		tv_app_version.setText(getString(R.string.app_name)+Version.getVersionName());
		dataList=new ArrayList<AboutItem>();
		AboutItem aboutItem1=new AboutItem();
		aboutItem1.classJump=SettingFunIntro.class;
		aboutItem1.isNew=false;
		aboutItem1.itemName="功能介绍";
		dataList.add(aboutItem1);
		
		AboutItem aboutItem2=new AboutItem();
		aboutItem2.classJump=null;
		aboutItem2.isNew=false;
		aboutItem2.itemName="检查新版本";
		dataList.add(aboutItem2);
		
		settingAboutAdapter=new SettingAboutAdapter(dataList);
		txt_private=(TextView) findViewById(R.id.txt_private);
		lv_setting=(ListView) findViewById(R.id.lv_setting_about);
		lv_setting.setAdapter(settingAboutAdapter);
	}

	protected void setListener() {
		lv_setting.setOnItemClickListener(new OnItemClickListener() {
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AboutItem aboutItem=(AboutItem) parent.getItemAtPosition(position);
				if (aboutItem.itemName.equals("检查新版本")) {
						UpgradeProgress upgradeProgress = UpgradeProgress.getInstance();
						upgradeProgress.checkVersion(SettingAboutActivity.this, false);
				}
				if (aboutItem.classJump!=null) {
					Intent intent=new Intent(SettingAboutActivity.this,aboutItem.classJump);
					startActivity(intent);
				}
			}
		});
		txt_private.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(SettingAboutActivity.this, PrivacyPolicyActivity.class);
				startActivity(intent);
			}
		});
	}

	class SettingAboutAdapter extends BaseAdapter {

		ArrayList<AboutItem> list = new ArrayList<AboutItem>();

		public SettingAboutAdapter(ArrayList<AboutItem> dataList) {
			this.list = dataList;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder  holder;
			if (convertView==null) {
				holder=new ViewHolder();
				convertView=LayoutInflater.from(mContext).inflate(R.layout.list_item_setting_about, null);
				holder.textView=(TextView) convertView.findViewById(R.id.tv_name);
				holder.imageView=(TextView) convertView.findViewById(R.id.tv_new);
				convertView.setTag(holder);
			}else {
				holder=(ViewHolder) convertView.getTag();
			}
			AboutItem aboutItem=(AboutItem) getItem(position);
			holder.textView.setText(aboutItem.itemName);
			if (aboutItem.isNew) {
				holder.imageView.setVisibility(View.VISIBLE);
			}
			
			return convertView;
		}

		class ViewHolder {
			TextView textView;
			TextView imageView;
		}
		
	}
	class AboutItem{
		Class<?> classJump;
		String itemName;
		boolean isNew;
	}
}
