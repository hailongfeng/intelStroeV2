package com.intel.store.view.webview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.intel.store.R;
import com.intel.store.view.BaseActivity;
import com.intel.store.widget.LoadingView;
import com.pactera.framework.util.Loger;

public class PrivacyPolicyActivity extends BaseActivity {

	private WebView web_terminology;
	// private StoreController storeController;
	private Button btn_back;
	private Button btn_forward;
	private Button btn_reload;
	private Button btn_activity_back;

	private final static int UNZIP_COMPLETE = 3;

	private LoadingView loadingView;
	private RelativeLayout rl_privacy_policy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_privacy_policy);
		initView();
		setListener();
		loadingView.showLoading();
		loadWebView();

	}

	private void setListener() {
		loadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// checkVersion();
			}
		});
		btn_activity_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (web_terminology.canGoBack()) {
					web_terminology.goBack();
					// web_terminology.setsc
					// web_terminology.getScrollY();
					setMenuState();
				}
			}
		});
		btn_forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (web_terminology.canGoForward()) {
					web_terminology.goForward();
					setMenuState();
				}
			}
		});
		btn_reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				web_terminology.reload();
				setMenuState();
			}
		});
	}

	private void initView() {
		Loger.i("=====初始化");
		loadingView = (LoadingView) findViewById(R.id.common_id_ll_loading);
		rl_privacy_policy = (RelativeLayout) findViewById(R.id.ll_sale_words);
		btn_activity_back = (Button) findViewById(R.id.common_id_btn_back);
		btn_back = (Button) findViewById(R.id.btn_back);

		web_terminology = (WebView) findViewById(R.id.web_terminology);
		btn_forward = (Button) findViewById(R.id.btn_forward);
		btn_reload = (Button) findViewById(R.id.btn_reload);
		// storeController = new StoreController();
		// setMenuState();

	}

	private void loadWebView() {
		web_terminology.clearView();

		web_terminology.setBackgroundColor(Color.WHITE);

		web_terminology.getSettings().setLoadsImagesAutomatically(true);
		web_terminology.getSettings().setDefaultTextEncodingName("UTF-8");
		web_terminology.getSettings().setSupportZoom(true);
		web_terminology.getSettings().supportMultipleWindows();
		web_terminology.getSettings().setJavaScriptEnabled(true);

		web_terminology.setWebViewClient(new WebViewClient() {
			// Intercepts url loading
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Loger.d("url=" + url);
				view.loadUrl(url);
				setMenuState();
				return true;
			}

			// Intercepts the resource loading events
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				setMenuState();
			}

			// Intercepts error message
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Loger.i(failingUrl);
				Toast.makeText(PrivacyPolicyActivity.this, "页面加载失败",
						Toast.LENGTH_LONG).show();
			}

			// Intercepts form resubmission
			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				Loger.i("Resubmission");
				Toast.makeText(PrivacyPolicyActivity.this,
						"不可重复提交，按Back回到上级网页", Toast.LENGTH_SHORT).show();

			}

			// Intercepts page started event
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				Loger.i("Page load start,url=" + url);
			}

			// Intercepts page finished event
			@Override
			public void onPageFinished(WebView view, String url) {
				view.scrollTo(0, 0);
				Loger.i("Page load finish");
				loadingView.hideLoading();
				rl_privacy_policy.setVisibility(View.VISIBLE);
			}
		});

		web_terminology.setWebChromeClient(new WebChromeClient() {
			public void onReceivedTitle(WebView view, String title) {
				// StoreListViewPageFragment.txt_title_msg.setText(title);
				super.onReceivedTitle(view, title);
			}
		});
		web_terminology.loadUrl("http://www.intel.cn/content/www/cn/zh/privacy/intel-online-privacy-notice.html");
	}

	/**
	 * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && web_terminology.canGoBack()) {
			web_terminology.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case UNZIP_COMPLETE:
				loadWebView();
				setMenuState();
				loadingView.hideLoading();
				rl_privacy_policy.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	private void setMenuState() {
		if (web_terminology.canGoBack()) {
			btn_back.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sale_word_left));
		} else {
			btn_back.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sale_word_left_press));
		}
		if (web_terminology.canGoForward()) {
			btn_forward.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sale_word_right));
		} else {
			btn_forward.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.sale_word_right_press));
		}

	}

}
