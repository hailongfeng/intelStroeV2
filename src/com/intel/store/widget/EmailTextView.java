package com.intel.store.widget;

import com.intel.store.R;
import com.intel.store.StoreApplication;
import com.pactera.framework.util.ToastHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class EmailTextView extends TextView {

	public EmailTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public EmailTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EmailTextView(Context context) {
		super(context);
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
        	String errMsg=e.getMessage();
        	if (errMsg!=null) {
    			ToastHelper.getInstance().showLongMsg(errMsg);
			}else {
				errMsg=StoreApplication.getAppContext().getString(R.string.txt_link_error);
				ToastHelper.getInstance().showLongMsg(errMsg);
			}
        }
        return false;
    }

}
