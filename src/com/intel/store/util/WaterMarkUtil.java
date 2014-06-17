package com.intel.store.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.util.Log;

public class WaterMarkUtil {
	public static Bitmap getMutableBitmap(Bitmap original) {
		Bitmap mutableBitmap = original.copy(Bitmap.Config.RGB_565, true);
		return mutableBitmap;
	}

	public static Bitmap addWaterMark(Bitmap original, String strCategory,
			long currentMiliSec) {
		Paint paint = new Paint();
		paint.setStyle(Style.FILL);
		paint.setColor(Color.argb(0xCC, 0xFF, 0xFF, 0xFF));
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.SANS_SERIF);
		paint.setTextSize(24);
		Canvas canvas = new Canvas(original);
		Log.d("", "canvas.getWidth() = " + canvas.getWidth());
		Log.d("", "canvas.getHeight() = " + canvas.getHeight());
		
		//draw photo time
		Date date = new Date(currentMiliSec);
		String strDate = new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(date);
		Rect dateTextBounds = new Rect();
		paint.getTextBounds(strDate, 0, strDate.length(), dateTextBounds);
		Log.d("", "dateTextBounds.width() = " + dateTextBounds.width());
		Log.d("", "dateTextBounds.height() = " + dateTextBounds.height());
		canvas.drawText(strDate, canvas.getWidth() / 2 - dateTextBounds.width()
				/ 2, canvas.getHeight() - 10, paint);

		//draw photo category name
		Rect categoryTextBounds = new Rect();
		paint.getTextBounds(strCategory, 0, strCategory.length(),
				categoryTextBounds);
		canvas.drawText(strCategory,
				canvas.getWidth() / 2 - categoryTextBounds.width() / 2,
				canvas.getHeight() - 10 - dateTextBounds.height() - 10, paint);

		
		//draw photo location
			String strlocation ="FOR INTEL INTERNAL USE ONLY";
			Rect locationTextBounds = new Rect();

			paint.getTextBounds(strlocation, 0, strlocation.length(),
					locationTextBounds);
			canvas.drawText(strlocation,
					canvas.getWidth() / 2 - locationTextBounds.width() / 2,
					canvas.getHeight() - 10 -categoryTextBounds.height()-10- categoryTextBounds.height() - 10, paint);	
			
		
		return original;
	}
}