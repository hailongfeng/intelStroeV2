package com.intel.store.widget;

import com.intel.store.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自实现支持4.0版本之前的switch按钮
 * 
 * @author weihaoshuai
 * @version 1.0
 * @date 2013-06-09
 */
public class SwitchButton extends RelativeLayout implements OnGestureListener,
		OnTouchListener {
	//选择默认值
	private static final String TEXT_ON_DEFAULT = "开";
	private static final String TEXT_OFF_DEFAULT = "关";
	
	private TextView view_text_on, view_text_off;// 开关文字描述
	private ImageView toggle_btn, image_btn;// 可移动image与背景image
	private Drawable toggle_drawable, image_drawable;// 可移动image与背景image
	private OnCheckedChangeListener checkedChangeImpl;// 点击事件接口实现

	private static final int TEXTON_ID = 1;// 开的id

	private static final int MIN_OFFSET = 0;// 判断是否有手势移动

	private GestureDetector gestureDetector;// 监听屏幕事件

	private LayoutParams toggle_params;// 布局参数

	private TranslateAnimation switch_on, switch_off, text_on, text_off;// 动画效果开关，开关文本

	private String text_on_res, text_off_res; // 开关文本按钮，从xml文件获取

	private boolean isChecked = false;// 判断开关，默认关闭

	public SwitchButton(Context context) {
		super(context);
		Log.d(this.toString(), "SwitchButton(Context context)");
		initView();
		initAnimation();
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(this.toString(), "SwitchButton(Context context, AttributeSet attrs)" + attrs.toString());
		initView();
		initAnimation();

		initText(context, attrs);
	}

	public SwitchButton(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		Log.d(this.toString(), "SwitchButton(Context context, AttributeSet attrs, int style)" + attrs.toString());
		initView();
		initAnimation();

		initText(context, attrs);
	}

	// 从xml读取文本给view赋值
	public void initText(Context context, AttributeSet attrs) {
		TypedArray tar = context.obtainStyledAttributes(attrs,
				R.styleable.switchbutton);
		text_on_res = tar.getString(R.styleable.switchbutton_textOn);
		text_off_res = tar.getString(R.styleable.switchbutton_textOff);
		tar.recycle();// 回收资源
		text_on_res = (text_on_res == null || "".equals(text_on_res)) ? TEXT_ON_DEFAULT
				: text_on_res;
		text_off_res = (text_off_res == null || "".equals(text_off_res)) ? TEXT_OFF_DEFAULT
				: text_off_res;
		setTextOn(text_on_res);
		setTextOff(text_off_res);
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		gestureDetector = new GestureDetector(this);

		// 添加背景图
		toggle_drawable = getContext().getResources()
				.getDrawable(R.drawable.bg_switch);
		image_drawable = getContext().getResources().getDrawable(
				R.drawable.toggle);
		// 设定开文本相关属性
		view_text_on = new TextView(getContext());
		view_text_on.setId(TEXTON_ID);
		view_text_on.setTextColor(getContext().getResources().getColor(
				R.color.base_white_ffffff_normal));
		view_text_on.setBackgroundResource(R.drawable.on_bg);
		LayoutParams texton_params = new LayoutParams(
				toggle_drawable.getIntrinsicWidth(),
				toggle_drawable.getIntrinsicHeight());
		view_text_on.setLayoutParams(texton_params);
		view_text_on.setPadding(0, 0, image_drawable.getIntrinsicWidth(), 0);
		view_text_on.setGravity(Gravity.CENTER);
		// view_text_on.setText("yes");

		// 设定关文本相关属性
		view_text_off = new TextView(getContext());
		view_text_off.setTextColor(getContext().getResources().getColor(
				R.color.black));
		view_text_off.setBackgroundResource(R.drawable.off_bg);
		LayoutParams textoff_params = new LayoutParams(
				toggle_drawable.getIntrinsicWidth(),
				toggle_drawable.getIntrinsicHeight());
		view_text_off.setLayoutParams(textoff_params);
		view_text_off.setPadding(image_drawable.getIntrinsicWidth(), 0, 0, 0);
		view_text_off.setGravity(Gravity.CENTER);
		// view_text_off.setText("no");
		if (isChecked) {
			view_text_off.setVisibility(View.GONE);
		} else {
			view_text_off.setVisibility(View.VISIBLE);
		}

		// 设定可移动图标的相关属性
		toggle_btn = new ImageView(getContext());
		toggle_btn.setBackgroundDrawable(image_drawable);
		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		if (isChecked) {
			toggle_params.addRule(ALIGN_RIGHT, TEXTON_ID);
		} else {
			toggle_params.addRule(ALIGN_PARENT_LEFT);
		}
		toggle_btn.setLayoutParams(toggle_params);

		// 设定背景
		image_btn = new ImageView(getContext());
		image_btn.setBackgroundDrawable(toggle_drawable);

		// 前图片上有手势事件触发
		toggle_btn.setOnTouchListener(this);
		toggle_btn.setLongClickable(true);
		// 背景图上有手势事件触发
		image_btn.setOnTouchListener(this);
		image_btn.setLongClickable(true);

		// 添加视图
		addView(view_text_on);
		addView(view_text_off);
		addView(image_btn);
		addView(toggle_btn);
	}

	/**
	 * 初始化动画，摆放相关控件的位置
	 */
	private void initAnimation() {
		switch_on = new TranslateAnimation(
				(toggle_drawable.getIntrinsicWidth() - image_drawable.getIntrinsicWidth())
						* -1, 0, 0, 0);
		switch_on.setDuration(300);
		switch_on.setFillAfter(true);

		switch_off = new TranslateAnimation(toggle_drawable.getIntrinsicWidth()
				- image_drawable.getIntrinsicWidth(), 0, 0, 0);
		switch_off.setDuration(300);
		switch_off.setFillAfter(true);

		text_on = new TranslateAnimation(0, toggle_drawable.getIntrinsicWidth()
				- image_drawable.getIntrinsicWidth(), 0, 0);
		text_on.setDuration(300);
		text_on.setFillAfter(true);

		text_off = new TranslateAnimation(toggle_drawable.getIntrinsicWidth()
				- image_drawable.getIntrinsicWidth(), 0, 0, 0);
		text_off.setDuration(300);
		text_off.setFillAfter(true);
	}

	/**
	 * 添加监听器
	 * 
	 * @param listener
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		checkedChangeImpl = listener;
	}

	/**
	 * 设置打开显示字
	 * 
	 * @param text
	 */
	public void setTextOn(CharSequence text) {
		view_text_on.setText(text);
	}

	/**
	 * 设置关闭显示字
	 * 
	 * @param text
	 */
	public void setTextOff(CharSequence text) {
		view_text_off.setText(text);
	}

	/**
	 * 获得打开显示字
	 * 
	 * @return CharSequence
	 */
	public CharSequence getTextOn() {
		return view_text_on.getText();
	}

	/**
	 * 获得关闭显示字
	 * 
	 * @return CharSequence
	 */
	public CharSequence getTextOff() {
		return view_text_off.getText();
	}

	/**
	 * 设置打开显示字体颜色
	 * 
	 * @param color
	 */
	public void setTextOnColor(int color) {
		view_text_on.setTextColor(color);
	}

	/**
	 * 设置关闭显示字体颜色
	 * 
	 * @param color
	 */
	public void setTextOffColor(int color) {
		view_text_off.setTextColor(color);
	}

	/**
	 * 设置当前打开状态
	 */
	public void setChecked(boolean flag) {
		if (this.isChecked != flag) {
			if (isChecked)
				off();
			else
				on();
			this.isChecked = flag;
		}
	}

	/**
	 * 设定子控件的大小
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(toggle_drawable.getIntrinsicWidth(),
				toggle_drawable.getIntrinsicHeight());
	}

	public boolean onTouch(View v, MotionEvent event) {
		// Log.d("onTouch", "toggle_btn: " + (v == toggle_btn) + " isChecked: "
		// + isChecked);
		// Log.d("onTouch", "bg: " + (v == bg) + " isChecked: " + isChecked);
		boolean handle = false;
		if (v == toggle_btn || v == image_btn) {
			handle = gestureDetector.onTouchEvent(event);
		}
		return handle;
	}

	/**
	 * 返回true 则判断下一步是否onFling
	 */
	public boolean onDown(MotionEvent e) {
		Log.d("onDown", "onDown: " + isChecked);
		return true;
	}

	/**
	 * 手势在控件上有滑动时触发
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.d("onFling", "onFling: " + isChecked + " "
				+ (e1.getX() - e2.getX()));
		if ((e1.getX() - e2.getX()) != MIN_OFFSET) {
			if (isChecked)
				off();
			else
				on();
		}
		return false;
	}

	/**
	 * 长按时触发
	 */
	public void onLongPress(MotionEvent e) {
		Log.d("onLongPress", "onLongPress: " + isChecked);
		if (isChecked)
			off();
		else
			on();
	}

	/**
	 * 手势滚动时触发
	 */
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/**
	 * 点击后滚动前触发
	 */
	public void onShowPress(MotionEvent e) {
	}

	/**
	 * 点击前景图标时触发
	 */
	public boolean onSingleTapUp(MotionEvent e) {
		if (isChecked)
			off();
		else
			on();
		return false;
	}

	/**
	 * 开启按钮相关属性设置
	 */
	private void on() {
		toggle_params = null;
		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		toggle_params.addRule(ALIGN_RIGHT, TEXTON_ID);
		toggle_btn.setLayoutParams(toggle_params);
		toggle_btn.startAnimation(switch_on);
		view_text_off.startAnimation(text_on);
		view_text_off.setVisibility(View.GONE);
		isChecked = true;
		try {
			// 实现监听则调此方法，不实现不抛错误
			checkedChangeImpl.checkedChange(isChecked);
		} catch (RuntimeException e) {
		}
	}

	/**
	 * 关闭按钮相关属性设置
	 */
	private void off() {
		toggle_params = null;
		toggle_params = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		toggle_params.addRule(ALIGN_PARENT_LEFT);
		toggle_btn.setLayoutParams(toggle_params);
		toggle_btn.startAnimation(switch_off);
		view_text_off.setVisibility(View.VISIBLE);
		view_text_off.startAnimation(text_off);
		isChecked = false;
		try {
			// 实现监听则调此方法，不实现不抛错误
			checkedChangeImpl.checkedChange(isChecked);
		} catch (RuntimeException e) {
		}
	}

	/**
	 * 自定义监听事件
	 * 
	 */
	public interface OnCheckedChangeListener {
		public void checkedChange(boolean isChecked);
	}
}
