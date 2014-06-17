package com.intel.store.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**  
 * @Title: MyListView.java 
 * @Package: com.intel.store.widget 
 * @Description:(用一句话描述该文件做什么) 
 * @author: fenghl 
 * @date: 2014年1月3日 下午5:36:51 
 * @version: V1.0  
 */
public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListView(Context context) {
		super(context);
	}
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//得到的宽高是当前可用的宽高，可以改变高宽和模式。
		int expandSpec =MeasureSpec.makeMeasureSpec(300,
					MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
