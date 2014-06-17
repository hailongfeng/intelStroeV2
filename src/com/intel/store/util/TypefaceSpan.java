package com.intel.store.util;

import com.pactera.framework.util.Loger;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * @Title: TypefaceSpan.java
 * @Package: com.intel.shangbanla.view
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2014年6月6日 下午2:15:26
 * @version: V1.0
 */
public class TypefaceSpan extends MetricAffectingSpan {

	private Typeface mTypeface;

	public TypefaceSpan(Typeface mTypeface) {
		this.mTypeface = mTypeface;
	}

	@Override
	public void updateMeasureState(TextPaint p) {
		Loger.d("updateMeasureState。");
		p.setTypeface(mTypeface);
		p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		Loger.d("updateDrawState。");
		tp.setTypeface(mTypeface);
		tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}
}