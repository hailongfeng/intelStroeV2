package com.intel.store.widget;

import com.intel.store.R;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SblAlertDialog extends AlertDialog {
    private static final String TAG = "SblAlertDialog";
    private View mContentView;
    private Context mContext;
    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mPosBtnText;
    private CharSequence mNegBtnText;
    private View.OnClickListener mPosBtnListener;
    private View.OnClickListener mNegBtnListener;

    public SblAlertDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    public SblAlertDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    public SblAlertDialog(Context context) {
        this(context, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate()");
        mContentView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_layout, null);
        setContentView(mContentView);

        View topPanel = findViewById(R.id.topPanel);
        topPanel.setVisibility(TextUtils.isEmpty(mTitle) ? View.GONE
                : View.VISIBLE);

        TextView alertTitle = (TextView) findViewById(R.id.alertTitle);
        if (alertTitle != null) {
            alertTitle.setText(TextUtils.isEmpty(mTitle) ? "" : mTitle);
        }
        TextView message = (TextView) findViewById(R.id.message);
        if (message != null) {
            message.setText(TextUtils.isEmpty(mMessage) ? "" : mMessage);
        }

        Button button1 = (Button) findViewById(R.id.button1);
            button1.setText(mPosBtnText);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (mPosBtnListener != null) {
                        mPosBtnListener.onClick(arg0);
                    }
                    SblAlertDialog.this.dismiss();
                }
            });

        Button button2 = (Button) findViewById(R.id.button2);
            button2.setText(mNegBtnText);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (mNegBtnListener != null) {
                    	mNegBtnListener.onClick(arg0);
                    }
                    SblAlertDialog.this.dismiss();
                }
            });

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
    }

    public void setPositiveButton(int textId,
            final View.OnClickListener listener) {
        setPositiveButton(mContext == null ? "" : mContext.getText(textId),
                listener);
    }

    public void setPositiveButton(CharSequence text,
            final View.OnClickListener listener) {
        mPosBtnText = text;
        mPosBtnListener = listener;
    }

    public void setNegativeButton(int textId,
            final View.OnClickListener listener) {
        setNegativeButton(mContext == null ? "" : mContext.getText(textId),
                listener);
    }

    public void setNegativeButton(CharSequence text,
            final View.OnClickListener listener) {
        mNegBtnText = text;
        mNegBtnListener = listener;
    }

}