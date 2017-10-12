package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.HtmlInteractiveAndroid;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/***
 * Description:TBS打开网页 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class TBSWebViewActivity extends FatherActivity {
	private WebView webview;
	private String data = "";
	public static final int REQUEST_CODE = 10086;
	private int model;
	/** url */
	public static final int URL = 0;
	/** data */
	public static final int DATA = 1;
	/** pay */
	public static final int Pay = 2;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_tbs_webview;
	}

	@Override
	protected void initValues() {
		model = getIntent().getIntExtra(Consts.KEY_MODULE, URL);
		data = getIntent().getStringExtra(Consts.KEY_DATA);
		TextView center = (TextView) findViewById(R.id.tv_head_center);
		center.setText(getIntent().getStringExtra(Consts.TITLE));
		center.setTextColor(getResources().getColor(R.color.white));
		center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		View left = findViewById(R.id.rl_head_left);
		left.findViewById(R.id.tv_head_left).setBackgroundResource(
				R.drawable.arrow_back);
		left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
		protected void initView() {
			webview = (WebView) findViewById(R.id.webview);
			webview.setInitialScale(25);
			WebSettings webSettings = webview.getSettings();
			webSettings.setJavaScriptEnabled(true);
			// 缩放开关；设置此属性，仅支持双击缩放，不支持触摸缩放
			webSettings.setSupportZoom(false);
			// 设置是否可缩放如果设置了此属性，那么webView.getSettings().setSupportZoom(true);也默认设置为true
			webSettings.setBuiltInZoomControls(true);
			// 无限缩放;设置此属性，可任意比例缩放。
			webSettings.setUseWideViewPort(true);
			// 实现双击放大，再次双击显示缩略图的功能。
			webSettings.setLoadWithOverviewMode(true);
			// 设置WebView 缓存模式
			webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置为不使用缓存
			webview.setHorizontalScrollBarEnabled(false);
			webview.setVerticalScrollBarEnabled(false);
			
			
			
			
			
			webview.addJavascriptInterface(new HtmlInteractiveAndroid(this), "htmlInteracAndroid");
			webview.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}

				@Override
				public void onLoadResource(WebView view, String url) {
					// TODO Auto-generated method stub
					super.onLoadResource(view, url);
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					// 通过内部类定义的方法获取html页面加载的内容，这个需要添加在webview加载完成后的回调中
					if (model == Pay) {
						long a = getIntent().getLongExtra(Consts.KEY_ORDERID, 0);
						String b = getIntent()
								.getStringExtra(Consts.KEY_MONEYCOUNT);
						view.loadUrl("javascript:window.htmlInteracAndroid.toast(document.body.innerHTML,"
								+ a + ",'" + b + "');");
					}
					super.onPageFinished(view, url);
				}

				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					// TODO Auto-generated method stub
					super.onPageStarted(view, url, favicon);
				}
			});

			switch (model) {
			case DATA:
				webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
				break;
			case URL:
				webview.loadUrl(data);
				break;
			case Pay:
				webview.loadUrl(data);
				break;
			default:
				break;
			}

		}

	/*
	 * 初始化有关点击返回键的功能，如果有上一个网页就返回上一个网页，如果没有则结束activity；
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (webview.canGoBack()) {
				webview.goBack();
			} else {
				finish();
			}
			return false;
		}
		return true;
	}

	@Override
	protected void doOperate() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		// 支付完成
		if (arg1 == RESULT_OK) {
			if (arg0 == REQUEST_CODE) {
				setResult(RESULT_OK);
				finish();
			}
		}
	}
}
