package cn.net.chenbao.qbyp.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.FileProvider;

import cn.net.chenbao.qbyp.activity.WelcomeActivity;

import cn.net.chenbao.qbyp.R;

public class UpdateVersionService extends Service {

	// 标题
	private int titleId = 0;
	// 下载地址
	private String downloadUrl;
	// 有关文件的描述
	private String app_desc;
	// 文件存储
	private File updateDir = null;
	private File updateFile = null;
	// 通知栏
	private NotificationManager updateNotificationManager = null;
	private Notification updateNotification = null;
	// 通知栏跳转Intent
	private Intent updateIntent = null;
	private PendingIntent updatePendingIntent = null;
	// 下载状态
	private final static int DOWNLOAD_COMPLETE = 0;
	private final static int DOWNLOAD_FAIL = 1;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try{
			// 获取传值
			titleId = intent.getIntExtra("titleId", 0);
			// 获取下载地址
			downloadUrl = intent.getStringExtra("downloadUrl");

			app_desc = intent.getStringExtra("app_desc");
			// 创建文件
			if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
					.getExternalStorageState())) {
				updateDir = new File(Environment.getExternalStorageDirectory(),
						"app/download/");
				updateFile = new File(updateDir.getPath(), "qbyp.apk");
			}
			this.updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			// 设置下载过程中，点击通知栏，回到主界面
			updateIntent = new Intent(this, WelcomeActivity.class);
			updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent,
					0);
			// 发出通知
			this.updateNotification = new Notification.Builder(this).
					setContentText("开始下载0%").setAutoCancel(true)
					.setContentTitle(getString(R.string.app_name)).setContentIntent(updatePendingIntent)
					.setSmallIcon(R.drawable.logo).build();
			updateNotificationManager.notify(0, updateNotification);
			// 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
			new Thread(new UpdateRunnable()).start();// 这个是下载的重点，是下载的过程
		}catch (NullPointerException e){

		}
		return super.onStartCommand(intent, flags, startId);
	}

	class UpdateRunnable implements Runnable {
		Message message = updateHandler.obtainMessage();

		public void run() {
			message.what = DOWNLOAD_COMPLETE;
			try {
				// 增加权限<uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
				if (!updateDir.exists()) {
					updateDir.mkdirs();
				}
				if (!updateFile.exists()) {
					updateFile.createNewFile();
				}
				// 增加权限<uses-permission
				// android:name="android.permission.INTERNET">;
				long downloadSize = downloadUpdateFile(downloadUrl, updateFile);
				if (downloadSize > 0) {
					// 下载成功
					updateHandler.sendMessage(message);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				message.what = DOWNLOAD_FAIL;
				// 下载失败
				updateHandler.sendMessage(message);
			}
		}
	}

	public long downloadUpdateFile(String downloadUrl, File saveFile)
			throws Exception {
		// 这样的下载代码很多，我就不做过多的说明
		int downloadCount = 0;
		int currentSize = 0;
		long totalSize = 0;
		int updateTotalSize = 0;
		HttpURLConnection httpConnection = null;
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			URL url = new URL(downloadUrl);
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection
					.setRequestProperty("User-Agent", "PacificHttpClient");
			if (currentSize > 0) {
				httpConnection.setRequestProperty("RANGE", "bytes="
						+ currentSize + "-");
			}
			httpConnection.setConnectTimeout(10000);
			httpConnection.setReadTimeout(20000);
			updateTotalSize = httpConnection.getContentLength();
			if (httpConnection.getResponseCode() == 404) {
				throw new Exception("fail!");
			}
			is = httpConnection.getInputStream();
			fos = new FileOutputStream(saveFile, false);
			byte buffer[] = new byte[4096];
			int readsize = 0;
			while ((readsize = is.read(buffer)) > 0) {
				fos.write(buffer, 0, readsize);
				totalSize += readsize;
				// 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
				if ((downloadCount == 0)
						|| (int) (totalSize * 100 / updateTotalSize) - 10 > downloadCount) {
					downloadCount += 10;
					this.updateNotification = new Notification.Builder(this).
							setContentText("拼命下载中，请稍后"+(int) totalSize * 100 / updateTotalSize + "%").setAutoCancel(true)
							.setContentTitle(getString(R.string.app_name)).setContentIntent(updatePendingIntent)
							.setSmallIcon(R.drawable.logo).build();

					updateNotificationManager.notify(0, updateNotification);
				}
			}
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

	private Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_COMPLETE:
				// 点击安装PendingIntent
				Intent installIntent = new Intent(Intent.ACTION_VIEW);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//兼容7.0
					installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
							"cn.net.chenbao.qbyp.fileprovider", updateFile);
					installIntent.setDataAndType(contentUri, "application/vnd.android.package-archive");
				} else {
					installIntent.setDataAndType(Uri.fromFile(updateFile), "application/vnd.android.package-archive");
					installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				}

				updatePendingIntent = PendingIntent.getActivity(
						UpdateVersionService.this, 0, installIntent, 0);
				updateNotification = new Notification.Builder(UpdateVersionService.this).
						setContentText("下载完成,点击安装。").setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
						.setContentTitle(getString(R.string.app_name)).setContentIntent(updatePendingIntent)
						.setSmallIcon(R.drawable.logo).build();
				updateNotificationManager.notify(0, updateNotification);
				// 停止服务
				stopService(updateIntent);
				updateNotificationManager.cancel(0);
				startActivity(installIntent);
				break;
			case DOWNLOAD_FAIL:
				// 下载失败
				updateNotification = new Notification.Builder(UpdateVersionService.this).
						setContentText("下载出错了").setAutoCancel(true).setDefaults(Notification.DEFAULT_SOUND)
						.setContentTitle(getString(R.string.app_name)).setContentIntent(updatePendingIntent)
						.setSmallIcon(R.drawable.logo).build();
				updateNotificationManager.notify(0, updateNotification);
				break;
			default:
				stopService(updateIntent);
			}
		}
	};


	/*
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
