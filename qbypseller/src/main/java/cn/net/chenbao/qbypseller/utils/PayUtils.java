package cn.net.chenbao.qbypseller.utils;

import cn.net.chenbao.qbypseller.R;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;

/***
 * Description:支付相关 Company: Zhubaoyi Version：2.0
 * 
 * @title PayUtils.java
 * @author ZXJ
 * @date 2016-5-19
 ***/
public class PayUtils {
	// 支付宝
	private static final int SDK_PAY_FLAG = 1;
	private static final int ALIPAY = 0;
	private static PayResultLisentner sPayResultLisentner;
	// 支付结果处理
	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				AlipayPayResult payResult = new AlipayPayResult(
						(String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// 支付成功
					sPayResultLisentner.PayResult((String) msg.obj, ALIPAY);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						WWToast.showShort(R.string.pay_result_is_confirming);
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						WWToast.showShort(R.string.pay_fail);
					}
				}
				break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 * @param payInfo
	 *            支付信息（这个需要在后台处理完成）
	 * 
	 */
	public static void pay(final Activity ctx, final String payInfo,
			PayResultLisentner payResultLisentner) {
		sPayResultLisentner = payResultLisentner;
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(ctx);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/***
	 * Description:支付结果的回调 Company: Zhubaoyi Version：2.0
	 * 
	 * @title PayUtils.java
	 * @author ZXJ
	 * @date 2016-5-19
	 ***/
	public interface PayResultLisentner {
		void PayResult(String payResult, int payType);
	}
}
