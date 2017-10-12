package cn.net.chenbao.qbyp.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 木头 on 2016/11/1.
 */

public class SelfSupportGoodsDetailWebFragment extends FatherFragment {

    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_self_support_goodsdetail_web;
    }

    @Override
    protected void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        webview.setInitialScale(25);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
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
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }
        });
        RequestParams params=new RequestParams(ApiShop.ProductDescribe());
        params.addBodyParameter("productId",getArguments().getLong(Consts.KEY_DATA)+"");
        x.http().get(params, new WWXCallBack("ProductDescribe") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                webview.loadDataWithBaseURL(null, data.getString("Data"), "text/html", "utf-8", null);
            }

            @Override
            public void onAfterFinished() {

            }
        });
        return rootView;
    }
}
