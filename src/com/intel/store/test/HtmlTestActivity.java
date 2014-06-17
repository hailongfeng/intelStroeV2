package com.intel.store.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.view.BaseActivity;
import com.pactera.framework.util.Loger;

public class HtmlTestActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_html_test);

		TextView tv = (TextView) findViewById(R.id.textView1);
		InputStream in = null;
		StringBuilder html = new StringBuilder();
		try {
			in = getResources().getAssets().open("test.txt");
			String encoding = "utf-8";
			InputStreamReader read = new InputStreamReader(in, encoding);// 考虑到编码格式
			BufferedReader bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				html.append(lineTxt);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// tv.setMovementMethod(ScrollingMovementMethod.getInstance());// 滚动
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		ImageGetter imgGetter = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				URL url;
				try {
					Loger.d(source);
					url = new URL(source);
					drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
				} catch (Exception e) {
					return null;
				}
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				return drawable;
			}
		};
		tv.setText(Html.fromHtml(html.toString(), imgGetter, null));

//		CharSequence text = tv.getText();
//		if (text instanceof Spannable) {
//			int end = text.length();
//			Spannable sp = (Spannable) tv.getText();
//			URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//			SpannableStringBuilder style = new SpannableStringBuilder(text);
//			style.clearSpans();// should clear old spans
//			for (URLSpan url : urls) {
//				MyURLSpan myURLSpan = new MyURLSpan(url.getURL());
//				style.setSpan(myURLSpan, sp.getSpanStart(url),
//						sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//			}
//			tv.setText(style);
//		}
	}
//
//	public class MyURLSpan extends ClickableSpan {
//
//		private String mUrl;
//
//		MyURLSpan(String url) {
//			mUrl = url;
//		}
//
//		@Override
//		public void onClick(View widget) {
//			// Toast.makeText(HtmlTestActivity.this, mUrl,
//			// Toast.LENGTH_LONG).show();
//			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
//			it.setClassName("com.android.browser",
//					"com.android.browser.BrowserActivity");
//			HtmlTestActivity.this.startActivity(it);
//			widget.setBackgroundColor(Color.parseColor("#00000000"));
//		}
//	}
}
