package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.bean.CartSum;
import cn.net.chenbao.qbyp.distribution.been.DistributionGood;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.DistributionInputNumDialog;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ppsher on 2017/4/16.
 * Description: 商品详情
 */

public class DistributionGoodDetailActivity extends FatherActivity {

    @BindView(R.id.web_detail)
    WebView webDetail;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_good_integral)
    TextView tvGoodIntegral;
    @BindView(R.id.vp_goods_details_imgs)
    HorizontalInnerViewPager vpGoodsDetailsImgs;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.v_no_params)
    TextView vNoParams;
    @BindView(R.id.ll_params_container)
    LinearLayout llParamsContainer;
    @BindView(R.id.tv_make_sure)
    TextView tvMakeSure;

    @BindView(R.id.tv_shop_cart_count)
    TextView tvShopCartCount;
    @BindView(R.id.rl_shop_car)
    RelativeLayout rlShopCar;
    @BindView(R.id.tv_add_cart)
    TextView tvAddCart;
    private long productId;
    private DistributionInputNumDialog numDialog;
    private DistributionGood good;
    private BannerPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_good_detail;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.good_detail, true);
    }

    @Override
    protected void initView() {
        productId = getIntent().getLongExtra(Consts.KEY_DATA, -1);

        // 动态设置高度
        int height = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams layoutParams = vpGoodsDetailsImgs
                .getLayoutParams();
        layoutParams.height = height;
        vpGoodsDetailsImgs.setLayoutParams(layoutParams);
        mAdapter = new BannerPagerAdapter(this);

        webDetail.setInitialScale(25);
        WebSettings webSettings = webDetail.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webDetail.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器
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
    }

    @Override
    protected void doOperate() {
        getProductInfo();
        getProductDescribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCarNum();
    }

    private void getProductInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiDistribution.ProductGet());
        params.addBodyParameter("productId", productId + "");
        x.http().get(params, new WWXCallBack("ProductGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {//参数
                good = JSONObject.parseObject(data.getString("Data"), DistributionGood.class);

                if (good != null) {
                    if (good.FenXiao.Status == 0) {
                        tvMakeSure.setText(R.string.good_offline);
                        tvMakeSure.setTextColor(getResources().getColor(R.color.white));
                        tvMakeSure.setBackgroundResource(R.color.text_gray_s);
                        tvMakeSure.setClickable(false);
                        tvAddCart.setVisibility(View.GONE);
                    } else {
                        tvMakeSure.setClickable(true);
                        tvAddCart.setVisibility(View.VISIBLE);
                    }

                    if (good.Images != null) {
                        ArrayList<String> mImages = new ArrayList<String>();
                        for (int i = 0; i < good.Images.size(); i++) {
                            mImages.add(good.Images.get(i).ImageUrl);
                        }
                        mAdapter.setData(mImages);
                        vpGoodsDetailsImgs.setAdapter(mAdapter);
                        indicator.setFillColor(getResources().getColor(
                                R.color.yellow_ww));
                        indicator.setPageColor(0xAAFFFFFF);
                        indicator.setStrokeWidth(0);
                        indicator.setRadius(DensityUtil.dip2px(
                                DistributionGoodDetailActivity.this, 3));
                        indicator.setViewPager(vpGoodsDetailsImgs);
                    }

                    tvGoodsName.setText(good.FenXiao.ProductName);
                    tvDescription.setText(good.ProductBrief);
                    tvPrice.setText(WWViewUtil.numberFormatPrice(good.FenXiao.SalePrice));
                    tvGoodIntegral.setText(WWViewUtil.numberFormatWithTwo(good.FenXiao.ConsumeNum));
                    if (good.ProductPropertes.size() > 0) {
                        vNoParams.setVisibility(View.GONE);
                        LayoutInflater inflater = LayoutInflater.from(DistributionGoodDetailActivity.this);
                        for (int i = 0; i < good.ProductPropertes.size(); i++) {
                            View view = inflater.inflate(R.layout.list_item_goods_parameter, null);
                            ((TextView) view.findViewById(R.id.tv_0)).setText(good.ProductPropertes.get(i).ProName);
                            ((TextView) view.findViewById(R.id.tv_1)).setText(good.ProductPropertes.get(i).ValName);
                            llParamsContainer.addView(view);
                        }
                    } else {
                        llParamsContainer.setVisibility(View.GONE);
                        vNoParams.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void getProductDescribe() {
        RequestParams params = new RequestParams(ApiDistribution.ProductDescribe());
        params.addBodyParameter("productId", productId + "");
        x.http().get(params, new WWXCallBack("ProductDescribe") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                webDetail.loadDataWithBaseURL(null, data.getString("Data"), "text/html", "utf-8", null);
            }

            @Override
            public void onAfterFinished() {
            }
        });
    }

    private void commitOrder(int quantity) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, SharedPreferenceUtils.getInstance().getSessionId());
        jsonObject.put("productId", productId);
        jsonObject.put("quantity", quantity);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiDistribution.OrderPreview()), new WWXCallBack("OrderPreview") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startDistributionMakeSureOrderActivity(DistributionGoodDetailActivity.this, data.getString("Data"));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_shop_car, R.id.tv_add_cart, R.id.tv_make_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_shop_car:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(DistributionGoodDetailActivity.this, LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, true));
                    return;
                }
                startActivity(new Intent(DistributionGoodDetailActivity.this, DistributionShopCartActivity.class));
                break;
            case R.id.tv_add_cart:
                if (good != null) {
                    if (!WWApplication.getInstance().isLogin()) {
                        startActivity(new Intent(DistributionGoodDetailActivity.this, LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, false));
                        return;
                    }
                    //弹出对话框
                    if (numDialog == null) {
                        numDialog = new DistributionInputNumDialog(this, good.FenXiao);

                    }
                    numDialog.setMakesureListener(new DistributionInputNumDialog.MakesureListener() {
                        @Override
                        public void onClickOk(int quantity) {
                            addToCar(quantity);
                        }
                    });

                    numDialog.show();
                }
                break;
            case R.id.tv_make_sure:
                if (good != null) {
                    if (!WWApplication.getInstance().isLogin()) {
                        startActivity(new Intent(DistributionGoodDetailActivity.this, LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, false));
                        return;
                    }
                    //弹出对话框
                    if (numDialog == null) {
                        numDialog = new DistributionInputNumDialog(this, good.FenXiao);

                    }
                    numDialog.setMakesureListener(new DistributionInputNumDialog.MakesureListener() {
                        @Override
                        public void onClickOk(int quantity) {
                            commitOrder(quantity);
                        }
                    });

                    numDialog.show();
                }
                break;
        }
    }

    private void addToCar(int quantity) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, SharedPreferenceUtils.getInstance().getSessionId());
        jsonObject.put("productId", productId);
        jsonObject.put("quantity", quantity);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiDistribution.CartAdd()), new WWXCallBack("CartAdd") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                getCarNum();
                WWToast.showShort(R.string.addto_shoppingcart_success);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void getCarNum() {
        if (WWApplication.getInstance().isLogin()) {
            x.http().get(ParamsUtils.getSessionParams(ApiDistribution.CartSumGet()), new WWXCallBack("CartSumGet") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    CartSum shopCar = JSONObject.parseObject(data.getString("Data"), CartSum.class);
                    if (shopCar.Quantity > 0) {
                        tvShopCartCount.setVisibility(View.VISIBLE);
                        if (shopCar.Quantity > 99) {
                            tvShopCartCount.setText("99+");
                        } else {
                            tvShopCartCount.setText(shopCar.Quantity + "");
                        }
                    } else {
                        tvShopCartCount.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAfterFinished() {

                }
            });

        }
    }

}
