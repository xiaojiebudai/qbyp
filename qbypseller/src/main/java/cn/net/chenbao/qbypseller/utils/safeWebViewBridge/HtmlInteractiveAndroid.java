package cn.net.chenbao.qbypseller.utils.safeWebViewBridge;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebView;

//HtmlInteractiveAndroid中需要被JS调用的函数，必须定义成public static，且必须包含WebView这个参数
public class HtmlInteractiveAndroid {
	private static Context mContext;

	public static void HtmlInteractiveSetContext(Context htmlActivity) {
		HtmlInteractiveAndroid.mContext = htmlActivity;
	}

	/**
	 * 短暂气泡提醒
	 * 
	 * @param webView
	 *            浏览器
	 * @param message
	 *            提示信息
	 * */
	public static void toast(WebView webView, String message) {
		if (message.equals("success")) {
			((Activity) webView.getContext()).setResult(Activity.RESULT_OK);
			((Activity) webView.getContext()).finish();
		} else if (message.equals("fail")) {
			((Activity) webView.getContext()).finish();
		}
		
	}

	/**
	 * 获取设备IMSI
	 * 
	 * @param webView
	 *            浏览器
	 * @return 设备IMSI
	 * */
	public static String getIMSI(WebView webView) {
		return ((TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}

	/**
	 * 获取用户系统版本大小
	 * 
	 * @param webView
	 *            浏览器
	 * @return 安卓SDK版本
	 * */
	public static int getOsSdk(WebView webView) {
		return Build.VERSION.SDK_INT;
	}

	/**
	 * 结束当前窗口
	 * 
	 * @param view
	 *            浏览器
	 * */
	public static void goBack(WebView view) {
		if (view.getContext() instanceof Activity) {
			((Activity) view.getContext()).finish();
		}
	}

	public static int overloadMethod(WebView view, int val) {
		return val;
	}

	public static String overloadMethod(WebView view, String val) {
		return val;
	}

	public static class RetJavaObj {
		public int intField;
		public String strField;
		public boolean boolField;
	}

	public static List<RetJavaObj> retJavaObject(WebView view) {
		RetJavaObj obj = new RetJavaObj();
		obj.intField = 1;
		obj.strField = "mine str";
		obj.boolField = true;
		List<RetJavaObj> rets = new ArrayList<RetJavaObj>();
		rets.add(obj);
		return rets;
	}

	public static void delayJsCallBack(WebView view, int ms,
			final String backMsg, final JsCallback jsCallback) {
		TaskExecutor.scheduleTaskOnUiThread(ms * 1000, new Runnable() {
			@Override
			public void run() {
				try {
					jsCallback.apply(backMsg);
				} catch (JsCallback.JsCallbackException je) {
					je.printStackTrace();
				}
			}
		});
	}

	public static long passLongType(WebView view, long i) {
		return i;

	}

	/**
	 * 将数据发送给JS
	 * 
	 * @param webView
	 *            webview 就是回调androidapp的那个webview
	 * @date 2015-12-05 09:20:39
	 * @author zxj // sendJoToJs(webView,"sayHello",jo.toString());
	 */
	public static void sendJoToJs(WebView webView, String jsfunction,
			String jsonstr) {
		webView.loadUrl("javascript:" + jsfunction + "('" + jsonstr + "')");
	}

}
