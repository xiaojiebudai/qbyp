package cn.net.chenbao.qbypseller.utils;

import android.app.Activity;
import android.content.Context;
import android.webkit.JavascriptInterface;

public class HtmlInteractiveAndroid {
	private  Context mContext;

	public  HtmlInteractiveAndroid(Context htmlActivity) {
		mContext = htmlActivity;
	}
	/**
	 * 短暂气泡提醒
	 *
	 * @param message
	 *            提示信息
	 * */
    @JavascriptInterface
	public void toast(String message, long orderId,
			String moneyCount) {
    	if (message.equals("success")) {
			WWToast.showShort(message);
			((Activity) mContext).setResult(Activity.RESULT_OK);
			((Activity) mContext).finish();
		} else if (message.equals("fail")) {
			WWToast.showShort(message);
			((Activity) mContext).finish();
		}
	}
}
