package cn.net.chenbao.qbyp.utils;

import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.PayResultActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
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
			PublicWay.stratPayResultActivity((Activity) mContext,
					PayResultActivity.SUCCESS, orderId, moneyCount, WebViewActivity.REQUEST_CODE, PayOrdersActivity.LOCAL);
		} else if (message.equals("fail")) {
			PublicWay.stratPayResultActivity((Activity) mContext,
					PayResultActivity.FAILE, -1, null, WebViewActivity.REQUEST_CODE, PayOrdersActivity.LOCAL);
		}
	}
}
