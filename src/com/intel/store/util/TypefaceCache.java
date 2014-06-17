package com.intel.store.util;

import java.util.TreeMap;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pactera.framework.util.Loger;

/**
 * @Title: TypefaceCache.java
 * @Package: com.intel.shangbanla.view
 * @Description:(用一句话描述该文件做什么)
 * @author: fenghl
 * @date: 2014年6月6日 上午10:01:09
 * @version: V1.0
 */
public class TypefaceCache {
	// Font names from asset:
	public static final String FONT_fzcyjt = "fonts/fzcyjt.ttf";
	public static final String FONT_fzxyjt = "fonts/fzxyjt.ttf";
	public static final String FONT_fzzyjt = "fonts/fzzyjt.ttf";
	public static final String FONT_DEFAULT = FONT_fzzyjt;

	private static TreeMap<String, Typeface> fontCache = new TreeMap<String, Typeface>();

	public static Typeface getFont(String fontName, Context context) {
		Typeface tf = fontCache.get(fontName);
		if (tf == null) {
			try {
				tf = Typeface.createFromAsset(context.getAssets(), fontName);
			} catch (Exception e) {
				return null;
			}
			fontCache.put(fontName, tf);
		}
		return tf;
	}

	public static void changeFonts(View root, Context context) {
		Typeface typeface = getFont(FONT_DEFAULT, context);
		if (root instanceof ViewGroup) {
			ViewGroup rootViewGroup = (ViewGroup) root;
			for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
				View v = rootViewGroup.getChildAt(i);
				changeFonts(v, context);
			}
		} else {
			if (root instanceof TextView) {
				Loger.d("TextView 字体应用。");
				((TextView) root).setTypeface(typeface);
			} else if (root instanceof Button) {
				Loger.d("Button 字体应用。");
				((Button) root).setTypeface(typeface);
			} else if (root instanceof EditText) {
				Loger.d("EditText 字体应用。");
				((EditText) root).setTypeface(typeface);
			}
		}
	}

	public static SpannableString  creatFontText(String text,Context context) {
		SpannableString s = new SpannableString(text);
		Typeface typeface = getFont(FONT_DEFAULT, context);
		
		s.setSpan(new TypefaceSpan(typeface), 0, s.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		return s;
	}

	public static void changeFonts(View root, String fontName, Context context) {
		Typeface typeface = getFont(fontName, context);
		if (root instanceof ViewGroup) {
			ViewGroup rootViewGroup = (ViewGroup) root;
			for (int i = 0; i < rootViewGroup.getChildCount(); i++) {
				View v = rootViewGroup.getChildAt(i);
				changeFonts(v, fontName, context);
			}
		} else {
			if (root instanceof TextView) {
				((TextView) root).setTypeface(typeface);
			} else if (root instanceof Button) {
				((Button) root).setTypeface(typeface);
			} else if (root instanceof EditText) {
				((EditText) root).setTypeface(typeface);
			}
		}
	}
}
