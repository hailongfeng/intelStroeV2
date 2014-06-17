package com.intel.store.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;


public class DialogHelper {
	public static void showDialog(final Activity activityContext,
            CharSequence title, CharSequence message,
            CharSequence posBtnText, View.OnClickListener posBtnListener ,
            CharSequence negBtnText, View.OnClickListener negBtnListener) 
    {
        SblAlertDialog dialog = new SblAlertDialog(activityContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(posBtnText, posBtnListener);
        dialog.setNegativeButton(negBtnText, negBtnListener);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				activityContext.finish();
			}
		});
        dialog.show();
    }

}