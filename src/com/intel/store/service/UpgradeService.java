package com.intel.store.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.intel.store.R;
import com.intel.store.dao.remote.StoreRemoteBaseDao;
import com.intel.store.util.Constants;
import com.pactera.framework.util.Loger;
import com.pactera.framework.util.Utils;
public class UpgradeService extends Service {
    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;

    public final static String BROADCAST_ACTION = "com.intel.store.service.UpgradeService";

    private String upgradeApkUrl = null;

    private int titleId = 0;

    private Handler updateHandler;

    private File updateDir = null;
    private File updateFile = null;

    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;

    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

    private boolean hasStarted = false;

    private Context context;
    
    public static int SERVICE_ID_UPGRADE = 0;
    public static int SERVICE_ID_PUSH = 1;

    @SuppressLint("HandlerLeak")
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (hasStarted) {
            return super.onStartCommand(intent, flags, startId);
        } else {
            hasStarted = true;
        }

        if (!hasInternet()) {
            Toast.makeText(context, R.string.comm_no_internet,
                    Toast.LENGTH_SHORT).show();
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        boolean hasSDCard = android.os.Environment.MEDIA_MOUNTED
                .equals(android.os.Environment.getExternalStorageState());
        if (!hasSDCard) {
            return super.onStartCommand(intent, flags, startId);
        }

        updateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                hasStarted = false;
                switch (msg.what) {
                case DOWNLOAD_COMPLETE:
                	Loger.d("ok");
                    Uri uri = Uri.fromFile(updateFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    installIntent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                 startActivity(installIntent);
                 updateNotificationManager.cancel(0);
                 sendMsg2Activity();
                 stopSelf();
                    break;
                case DOWNLOAD_FAIL:
                    updateNotification.setLatestEventInfo(UpgradeService.this,
                            getString(R.string.upgrade_notification_title),
                            getString(R.string.upgrade_fail_down),
                            updatePendingIntent);
                    updateNotificationManager.notify(
                            SERVICE_ID_UPGRADE, updateNotification);
                    sendMsg2Activity();

                    stopSelf();
                    break;
                default:
                    stopService(updateIntent);
                }
                stopSelf();
            }

            /**
             * @Title: sendMsg2Activity
             */
            private void sendMsg2Activity() {
                Intent intent = new Intent();
                intent.setAction(BROADCAST_ACTION);
                intent.putExtra("downloadDone", true);
                context.sendBroadcast(intent);
            }
        };

        if (null == intent) {
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        titleId = intent.getIntExtra("titleId", 0);
        upgradeApkUrl = intent.getStringExtra("upgradeApkUrl");

        updateDir = new File(Environment.getExternalStorageDirectory(),
                Utils.getNetConfigProperties().getProperty("downloadDir"));
        
        
        updateFile = new File(updateDir, getResources().getString(titleId)+ ".apk");
        Loger.d("==upgradeApkUrl==" + upgradeApkUrl + "========");

        this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        this.updateNotification = new Notification();

        updateIntent = new Intent();
        updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,0);
        updateNotification.flags = Notification.FLAG_ONGOING_EVENT; // 设置常驻 Flag
        updateNotification.icon = R.drawable.intel_logo_1;
        updateNotification.tickerText = getString(R.string.upgrade_start_download);
        updateNotification.setLatestEventInfo(this,
                getString(R.string.upgrade_notification_title), "0%",
                updatePendingIntent);

        updateNotificationManager.notify(SERVICE_ID_UPGRADE,
                updateNotification);

        new Thread(new UpdateRunnable()).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * 
     * @Title: downloadUpdateFile
     * @param downloadUrl
     * @param saveFile
     * @return
     * @throws Exception
     */
    public long downloadUpdateFile(String downloadUrl, File saveFile)
            throws Exception {
        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;

        final int CONN_TIMEOUT = 60000;
        final int READ_TIMEOUT = 60000;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(StoreRemoteBaseDao.HOST+Constants.API_UPDATESERVLET+downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection
                    .setRequestProperty("User-Agent", "PacificHttpClient");
            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes="
                        + currentSize + "-");
            }
            httpConnection.setConnectTimeout(CONN_TIMEOUT);
            httpConnection.setReadTimeout(READ_TIMEOUT);
            updateTotalSize = httpConnection.getContentLength();

            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("download error 404");
            }

            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;

            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                totalSize += readsize;

                if ((downloadCount == 0)
                        || (int) (totalSize * 100 / updateTotalSize) - 5 > downloadCount) {
                    downloadCount += 5;
                    updateNotification.setLatestEventInfo(UpgradeService.this,
                            getString(R.string.upgrade_downloading),
                            (int) totalSize * 100 / updateTotalSize + "%",
                            updatePendingIntent);
                    updateNotificationManager.notify(0, updateNotification);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }
    /**
     *  此方法用来创建文件目录，支持多层不存在的文件目录创建
     * @param file
     */
    public static void createFile (File file) {
        if (file!=null) {
            createFile(file.getParentFile());
            if (!file.exists()) {
                file.mkdir();
            }
        }

    }
    class UpdateRunnable implements Runnable {
        Message message = updateHandler.obtainMessage();
        public void run() {
            if (null == upgradeApkUrl) {
                return;
            }
            message.what = DOWNLOAD_COMPLETE;
            try {
               // if (!updateDir.exists()) {
               //     updateDir.mkdirs();
                    createFile(updateDir);
              //  }

                if (!updateFile.exists()) {
                	Loger.d(updateFile.getAbsolutePath());
                    updateFile.createNewFile();
                }

                long downloadSize = downloadUpdateFile(upgradeApkUrl,
                        updateFile);

                if (downloadSize > 0) {
                    updateHandler.sendMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                updateHandler.sendMessage(message);
            }
        }
    }

    protected boolean hasInternet() {
        ConnectivityManager manager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }
}
