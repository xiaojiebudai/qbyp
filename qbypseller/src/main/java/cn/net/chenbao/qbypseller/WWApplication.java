package cn.net.chenbao.qbypseller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;

import cn.net.chenbao.qbypseller.activity.BusinessLoginActivity;
import cn.net.chenbao.qbypseller.activity.MainActivity;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;

import org.xutils.x;

import cockroach.Cockroach;

public class WWApplication extends Application {
	/** 用户id */
	private String sessionId = null;
	/** 卖家id */
	private static String sellerId = null;
	private static WWApplication instance;
	/**
	 * 如果内存为空,取sp,sp为空就跳登录界面
	 * 
	 * @return
	 */
	public String getSessionId() {
		if (sessionId == null) {
			sessionId = SharedPreferenceUtils.getInstance().getSessionId();
		}
		return sessionId;
	}

	/** 只在登陆的时候用 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
		SharedPreferenceUtils.getInstance().saveSessionId(sessionId);// 存本地
		isSessionPast = false;// 设置id的时候(登录)sessionPast重置
	}

	/** 只在登陆的时候用 */
	public void setSessionPast() {
		isSessionPast = false;// sessionPast重置
	}

	/** session失效 */
	private boolean isSessionPast;
	/** session失效 */
	private boolean isHome = true;

	/** 只在登陆的时候用 */
	public void setisHome(boolean ishome) {
		this.isHome = ishome;// sessionPast重置
	}

	public void dealSessionPast() {
		if (isSessionPast) {
			return;
		} else {
			isSessionPast = true;
			clearLoginInfo();
			// TODO:弹窗?跳界面
			if (isHome) {
				startActivity(new Intent(this, MainActivity.class).putExtra(
						Consts.KEY_SESSION_ERROR, true).setFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK));
				isHome=true;
			} else {
				startActivity(new Intent(this, BusinessLoginActivity.class).setFlags(
						Intent.FLAG_ACTIVITY_NEW_TASK));
			}

		}
	}

	public static final String getSellerId() {
		if (null == sellerId) {
			return SharedPreferenceUtils.getInstance().getSellerId();
		}
		return sellerId;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		x.Ext.init(this);
		x.Ext.setDebug(true);
		WWToast.init(this);
		SharedPreferenceUtils.init(this);
		sessionId = SharedPreferenceUtils.getInstance().getSessionId();
		SDKInitializer.initialize(this);// 百度地图初始化
		ZLog.isDebug=true;
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
							WWToast.showShort(R.string.system_error_please_exit_and_try_again);
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

	public void clearLoginInfo() {
		sessionId = null;
		sellerId = null;
		SharedPreferenceUtils.getInstance().clearLoginData();
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base); MultiDex.install(this);
	}
}
