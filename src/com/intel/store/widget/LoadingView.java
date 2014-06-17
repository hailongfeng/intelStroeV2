package com.intel.store.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.intel.store.R;
import com.intel.store.util.ViewHelper;
import com.pactera.framework.exception.IException;

public class LoadingView extends LinearLayout {

	private TextView textView;
	private ImageView imageView;
	private AnimationDrawable animationDrawable;
	private static final int normal = 0;
	private static final int onLoading = 1;
	private static final int errorLoaded = 2;
	private int statue = normal;

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoadingView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

		textView = new TextView(context);
		textView.setText("");
		textView.setTextSize(16);
		textView.setTextColor(getResources().getColor(
				R.color.base_blue_0289b9_normal));

		imageView = new ImageView(context);
		imageView.setPadding(5, 5, 5, 5);
		imageView.setImageResource(R.anim.loading);
		imageView.setBackgroundColor(getResources().getColor(R.color.transparent));
		animationDrawable = (AnimationDrawable) imageView.getDrawable();
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		textView.setVisibility(View.GONE);
		addView(textView, layoutParams);
		addView(imageView, layoutParams);
	}

	public void showLoading() {
		if (statue != onLoading) {
			statue = onLoading;
			textView.setVisibility(View.GONE);
			
			imageView.setVisibility(View.VISIBLE);
			imageView.post(new Runnable() {
				@Override
				public void run() {
					animationDrawable.start();
				}
			});
			this.setVisibility(View.VISIBLE);
		}
	}

	public void hideLoading() {
		// if (statue == onLoading) {
		statue = normal;
		textView.setVisibility(View.GONE);
		imageView.setVisibility(View.VISIBLE);
		animationDrawable.stop();
		this.setVisibility(View.GONE);
		
		// }
	}
	
	/**
	 * @param msg
	 * @deprecated Use {@link ViewHelper#showErrorDialog(IException)}
	 */
	
	public void errorLoaded(String msg) {
		statue = errorLoaded;
		textView.setText(msg);
		textView.setVisibility(View.VISIBLE);
		animationDrawable.stop();
		imageView.setVisibility(View.GONE);
		this.setVisibility(View.VISIBLE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (statue == onLoading) {
			return true;
		} else {
			return super.onTouchEvent(event);
		}

	}
}
