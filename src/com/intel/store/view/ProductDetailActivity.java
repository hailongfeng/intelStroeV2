package com.intel.store.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.intel.store.R;

public class ProductDetailActivity extends BaseActivity {
	private Button btn_back;
	private TextView txt_title, txt_cache, txt_cache_name, txt_processor_number, txt_cores, txt_threads,
			txt_clock_speed, txt_turbo_frequency, txt_lithography, txt_graphics_model, txt_dyamic_frequency;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		initView();
		setListener();
	}

	private void initView() {
		btn_back = (Button) findViewById(R.id.common_id_btn_back);

		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_cache = (TextView) findViewById(R.id.txt_cache);
		txt_cache_name = (TextView) findViewById(R.id.txt_cache_name);
		txt_processor_number = (TextView) findViewById(R.id.txt_processor_number);
		txt_cores = (TextView) findViewById(R.id.txt_cores);
		txt_threads = (TextView) findViewById(R.id.txt_threads);
		txt_clock_speed = (TextView) findViewById(R.id.txt_clock_speed);
		txt_turbo_frequency = (TextView) findViewById(R.id.txt_turbo_frequency);
		txt_lithography = (TextView) findViewById(R.id.txt_lithography);
		txt_graphics_model = (TextView) findViewById(R.id.txt_graphics_model);
		txt_dyamic_frequency = (TextView) findViewById(R.id.txt_dyamic_frequency);

		Bundle bundle = getIntent().getExtras();
		String pro_nm = bundle.getString("pro_nm");
		String cache = bundle.getString("cache");
		String code_nm = bundle.getString("code_nm");
		String core_nbr = bundle.getString("core_nbr");
		String thd_cnt = bundle.getString("thd_cnt");
		String clk_spd = bundle.getString("clk_spd");
		String max_freq = bundle.getString("max_freq");
		String lith = bundle.getString("lith");
		String gfx_mdl = bundle.getString("gfx_mdl");
		String gfx_max_freq = bundle.getString("gfx_max_freq");
		String type = bundle.getString("type");

		txt_title.setText("Intel@Core " + pro_nm + " Processor " + type);
		txt_cache.setText(cache);
		txt_cache_name.setText(code_nm);
		txt_processor_number.setText(pro_nm);
		txt_cores.setText(core_nbr);
		txt_threads.setText(thd_cnt);
		txt_clock_speed.setText(clk_spd);
		txt_turbo_frequency.setText(max_freq);
		txt_lithography.setText(lith);
		txt_graphics_model.setText(gfx_mdl);
		txt_dyamic_frequency.setText(gfx_max_freq);

	}

	private void setListener() {
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
