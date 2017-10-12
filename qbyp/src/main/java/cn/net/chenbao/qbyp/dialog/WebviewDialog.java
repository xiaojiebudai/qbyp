package cn.net.chenbao.qbyp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.net.chenbao.qbyp.R;

/**
 * Created by ZXJ on 2017/6/17.
 */

public class WebviewDialog extends Dialog {
    /**
     * url
     */
    public static final int URL = 0;
    /**
     * data
     */
    public static final int DATA = 1;
    private WebView webview;

    public WebviewDialog(Context context, int model, String data) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_webwiew);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        attributes.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.7);
        getWindow().setAttributes(attributes);
        findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        webview = (WebView) findViewById(R.id.webview);
        webview.setInitialScale(25);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false); //支持屏幕缩放
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

            default:
                break;
        }
    }

}
