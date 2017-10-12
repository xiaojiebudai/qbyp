package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.safeWebViewBridge.HtmlInteractiveAndroid;
import cn.net.chenbao.qbyp.utils.safeWebViewBridge.InjectedChromeClient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/***
 * Description:打开网页 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-31
 */
public class WebViewActivity extends FatherActivity {
    private WebView webview;
    private String data = "";
    public static final int REQUEST_CODE = 10086;
    private int model;

    /**
     * url
     */
    public static final int URL = 0;
    /**
     * data
     */
    public static final int DATA = 1;
    /**
     * pay
     */
    public static final int Pay = 2;
    private boolean noBack = false;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_webveiw;
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
        // 这个对象主要是获取返回的数据，进行处理,向读取的URL传送对象
        HtmlInteractiveAndroid.HtmlInteractiveSetContext(this);
        webview.setWebChromeClient(new CustomChromeClient("htmlInteracAndroid",
                HtmlInteractiveAndroid.class));
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
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
                    //隐藏头部返回
                    view.loadUrl("javascript:window.hideHeader=function(){if(document.getElementsByClassName('sj_header').length>0) document.getElementsByClassName('sj_header')[0].style.display='none';};");
                    view.loadUrl("javascript:hideHeader()");
                    if (url.contains("http://api.szhysy.cn/Pay") || url.contains("http://api.zgcyk.net/Pay")|| url.contains("http://api.51wanj.com/Pay")) {
                        view.loadUrl("javascript:window.htmlInteracAndroid.toast(document.body.innerHTML);");
                    } else if (url.contains("/newpay/success_popbox.html") && url.contains("RESPONSECODE=0000")) {
                        noBack = true;
                        findViewById(R.id.rl_head_left).setVisibility(View.GONE);
                    } else {
                        noBack = false;
                        findViewById(R.id.rl_head_left).setVisibility(View.VISIBLE);
                    }
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
            if (noBack) {
                return false;
            } else if (webview.canGoBack()) {
                webview.goBack();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    public class CustomChromeClient extends InjectedChromeClient {
        public CustomChromeClient(String injectedName, Class injectedCls) {
            super(injectedName, injectedCls);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 final JsResult result) {
            // to do your work
            // ...
            return super.onJsAlert(view, url, message, result);
        }

        // 在web上面的进度条的显示
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message,
                                  String defaultValue, JsPromptResult result) {
            // to do your work
            // ...
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String str) {
            super.onReceivedTitle(view, str);
        }
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
