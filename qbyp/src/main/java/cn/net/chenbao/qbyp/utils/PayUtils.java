package cn.net.chenbao.qbyp.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

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
				sPayResultLisentner.PayResult((String) msg.obj, ALIPAY);

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
