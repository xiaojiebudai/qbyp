package cn.net.chenbao.qbyp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.net.chenbao.qbyp.activity.MainActivity;
import cn.net.chenbao.qbyp.utils.Constants;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.ZLog;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cockroach.Cockroach;

public class WWApplication extends Application {
	/** 用户id */
	private static String userId;
	/** sessionId */
	private String sessionId;

	private static WWApplication instance;

	public String getSessionId() {
		if (sessionId == null) {
			sessionId = SharedPreferenceUtils.getInstance().getSessionId();
		}
		return sessionId;
	}

	/** 用于登录用 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		SharedPreferenceUtils.getInstance().saveSessionId(sessionId);
		isSessionPast = false;// 设置id的时候(登录)sessionPast重置
	}

	/** session失效 */
	private boolean isSessionPast;

	public void dealSessionPast() {
		if (isSessionPast) {
			return;
		} else {
			isSessionPast = true;
			// TODO:弹窗?跳界面

			startActivity(new Intent(this, MainActivity.class).putExtra(
					Consts.KEY_SESSION_ERROR, true).setFlags(
					Intent.FLAG_ACTIVITY_NEW_TASK));

		}
	}

	/** 暂用于清除 */
	public void updataSessionId(String sessionId) {
		this.sessionId = sessionId;isSessionPast = false;

	}

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
//		setTypeface();
		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		SDKInitializer.initialize(this);// 百度地图初始化
		Config.DEBUG = false;
		UMShareAPI.get(this);
		PlatformConfig.setQQZone(Constants.QQ_APP_ID, Constants.QQ_APP_KEY);
		PlatformConfig.setSinaWeibo(Constants.SINA_APP_KEY,
				Constants.SINA_APP_SECRET);
		Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
		PlatformConfig.setWeixin(Constants.WX_APP_ID, Constants.WX_APP_KEY);
		SharedPreferenceUtils.init(this);
		x.Ext.init(this);
		x.Ext.setDebug(true);
		WWToast.init(this);
		ZLog.isDebug=true;
		sessionId = SharedPreferenceUtils.getInstance().getSessionId();
	}
//保证应用不crash
	private void install() {

		Cockroach.install(new Cockroach.ExceptionHandler() {

			// handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

			@Override
			public void handlerException(final Thread thread, final Throwable throwable) {
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						try {
							WWToast.showShort("系统异常，请退出应用重试");
						} catch (Throwable e) {

						}
					}
				});
			}
		});
	}

	public static WWApplication getInstance() {
		return instance;
	}

	/**
	 * 判断登录状态
	 * 
	 * @return
	 */
	public boolean isLogin() {
		String userId = SharedPreferenceUtils.getInstance().getSessionId();
		return !TextUtils.isEmpty(userId);
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base); MultiDex.install(this);
	}
}
