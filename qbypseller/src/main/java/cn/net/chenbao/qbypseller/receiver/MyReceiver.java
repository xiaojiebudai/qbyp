package cn.net.chenbao.qbypseller.receiver;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cn.net.chenbao.qbypseller.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.net.chenbao.qbypseller.activity.WelcomeActivity;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
		
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
                        
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
        	Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
        	processCustomMessage(context, bundle);
        
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            try {
				JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
				String sound=json.getString("Sound");
				if(!TextUtils.isEmpty(sound)&&sound.equals("1")){
					if(!isFastShow()){
						showVoice(context);
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            if(!isAppRuning(context, 2)){
            	//打开自定义的Activity
            	Intent i = new Intent(context, WelcomeActivity.class);
              	i.putExtras(bundle);
            	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            	context.startActivity(i);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        	
        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
        	boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
        	Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
        	Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
	}

	private void showVoice(Context context) {
		 MediaPlayer mp=MediaPlayer.create(context,R.raw.music);
	        mp.stop();
	        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，  
         //以便其他应用程序可以使用该资源:  
	        mp.setOnCompletionListener(new OnCompletionListener(){  
             @Override  
             public void onCompletion(MediaPlayer mp) {  
                 mp.release();//释放音频资源  
             }  
         });  
         try {  
             //在播放音频资源之前，必须调用Prepare方法完成些准备工作  
         	mp.prepare();  
             //开始播放音频  
         	mp.start();  
         } catch (IllegalStateException e) {  
             e.printStackTrace();  
         } catch (IOException e) {  
             e.printStackTrace();  
         }  
		
	}
	private static long lastClickTime;
    //3s响一次
	public synchronized static boolean isFastShow() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 3000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			}else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
				if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
					Log.i(TAG, "This message has no Extra data");
					continue;
				}

				try {
					JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
					Iterator<String> it =  json.keys();

					while (it.hasNext()) {
						String myKey = it.next().toString();
						sb.append("\nkey:" + key + ", value: [" +
								myKey + " - " +json.optString(myKey) + "]");
					}
				} catch (JSONException e) {
					Log.e(TAG, "Get message extra JSON error!");
				}

			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}
	
	//send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		//发广播
//		if (MainActivity.isForeground) {
//			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//			if (!ExampleUtil.isEmpty(extras)) {
//				try {
//					JSONObject extraJson = new JSONObject(extras);
//					if (null != extraJson && extraJson.length() > 0) {
//						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//					}
//				} catch (JSONException e) {
//				}
//
//			}
//			context.sendBroadcast(msgIntent);
//		}
	}
	private void startApp(Context context, String type, String value) {
		// TODO Auto-generated method stub
		// 打开应用
		Intent intent4 = new Intent();
		intent4.setComponent(new ComponentName("cn.net.chenbao.qbypseller",
				"cn.net.chenbao.qbypseller.activity.WelcomeActivity"));
		intent4.setAction(Intent.ACTION_VIEW);
		if (type != null && value != null) {
			intent4.putExtra("type", type);
			intent4.putExtra("value", value);
		}
		intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent4);
	}

	// top 1表示取应用是否在后台运行，2表示取应用是否在栈顶执行
	private boolean isAppRuning(Context context, int top) {
		Boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		if (top == 1) {
			for (RunningTaskInfo info : list) {
				if (info.topActivity.getPackageName().equals("cn.net.chenbao.qbypseller")
						&& info.baseActivity.getPackageName().equals(
								"cn.net.chenbao.qbypseller")) {
					isAppRunning = true;
					// find it, break
					break;
				}
			}
		} else if (top == 2) {
			if (list != null) {
				if (list.get(0).topActivity.getPackageName().equals(
						"cn.net.chenbao.qbypseller")) {
					isAppRunning = true;
				}
			}
		}
		return isAppRunning;
	}
}
