package com.intel.store.util;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.intel.store.StoreApplication;
import com.intel.store.R;
import com.intel.store.view.StoreImageShowActivity;
import com.pactera.framework.util.Loger;

public class NotificationHelper {
    public static void showPictureUploadNotification(String storeId, String msg) {
        // Notification
        Notification notification = new Notification();
        notification.icon = R.drawable.intel_logo_1;
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        notification.tickerText = msg;

        Intent resultIntent = new Intent(StoreApplication.getAppContext(),
                StoreImageShowActivity.class);
        Bundle data = new Bundle();
        data.putString("store_id", storeId);
        Loger.d("notif storeId = " + storeId);
        resultIntent.putExtras(data);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(StoreApplication.getAppContext(), Integer.parseInt(storeId),
        		resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(StoreApplication.getAppContext(), "图片上传", msg,
                contentIntent);

        StoreApplication.getAppContext();
		NotificationManager mNotificationManager = (NotificationManager) StoreApplication
                .getAppContext().getSystemService(
                		Context.NOTIFICATION_SERVICE);
        mNotificationManager
                .notify(Integer.parseInt(storeId), notification);
    }
}