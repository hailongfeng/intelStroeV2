/**  
 * @Title: MyGridView.java 
 * @Package: com.intel.store.widget 
 * @Description:(用一句话描述该文件做什么) 
 * @author: fenghl 
 * @date: 2013年12月25日 下午4:45:59 
 * @version: V1.0  
 */
package com.intel.store.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @项目名称：Store @类名称：MyGridView 类描述： 创建人：fenghl 创建时间：2013年12月25日 下午4:45:59
 *             修改人：Administrator 修改时间：2013年12月25日 下午4:45:59 修改备注： version
 *             1.0.0.0
 */
public class MyGridView extends GridView {

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
