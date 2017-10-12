package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.SelfSupportMakesureOrderActivity;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportGoodsEvaluateListActivity;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.bean.ShopProductJudge;
import cn.net.chenbao.qbyp.bean.ShopProductSku;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.SelfSupportGoodsParameterPop;
import cn.net.chenbao.qbyp.view.SelfSupportGoodsSelectPop;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城商品详情
 */

public class SelfSupportGoodsDetailFragment extends FatherFragment {
    @BindView(R.id.vp_goods_details_imgs)
    HorizontalInnerViewPager vpGoodsDetailsImgs;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.ll_goods_details_banner_container)
    RelativeLayout llGoodsDetailsBannerContainer;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_price_now)
    TextView tvPriceNow;
    @BindView(R.id.tv_price_gone)
    TextView tvPriceGone;
    @BindView(R.id.tv_sales)
    TextView tvSales;
    @BindView(R.id.tv_standard)
    TextView tvStandard;
    @BindView(R.id.ll_choose_standard)
    LinearLayout llChooseStandard;
    @BindView(R.id.ll_parameter)
    LinearLayout llParameter;
    @BindView(R.id.tv_shops_name)
    TextView tvShopsName;
    @BindView(R.id.tv_shops_address)
    TextView tvShopsAddress;
    @BindView(R.id.tv_evaluate_num)
    TextView tvEvaluateNum;
    @BindView(R.id.ll_evaluate_more)
    LinearLayout llEvaluateMore;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.ll_collect)
    LinearLayout llCollect;
    @BindView(R.id.tv_shop_cart_count)
    TextView tvShopCartCount;
    @BindView(R.id.rl_shop_car)
    RelativeLayout rlShopCar;
    @BindView(R.id.tv_add_cart)
    TextView tvAddCart;
    @BindView(R.id.tv_go_pay)
    TextView tvGoPay;
    @BindView(R.id.tv_jifen_can_use)
    TextView tv_jifen_can_use;
    @BindView(R.id.tv_jifen_used)
    TextView tv_jifen_used;
    private BannerPagerAdapter mAdapter;
    private long ProductId;
    private boolean isCollect;
    private ShopProduct shopProduct;
    private SelfSupportGoodsParameterPop selfSupportGoodsParameterPop;
    private SelfSupportGoodsSelectPop selfSupportGoodsSelectPop;
    private ShopProductSku selectSku;
    private int selectNum = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_self_support_goodsdetail;
    }

    @Override
    protected void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        ProductId = getArguments().getLong(Consts.KEY_DATA);

        // 动态设置高度
        int height = getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams layoutParams = vpGoodsDetailsImgs
                .getLayoutParams();
        layoutParams.height = height;
        vpGoodsDetailsImgs.setLayoutParams(layoutParams);
        mAdapter = new BannerPagerAdapter(getActivity());
        getData();
        if (selfSupportGoodsSelectPop == null)
            selfSupportGoodsSelectPop = new SelfSupportGoodsSelectPop(getActivity(), getLayoutId(), ProductId);
        selfSupportGoodsSelectPop.setOkListener(new SelfSupportGoodsSelectPop.OkCallBack() {
            @Override
            public void okBuyNowListener(ShopProductSku sp, int num) {
                selectSku = sp;
                selectNum = num;
                tvStandard.setText(selectSku.ProName + "       " + selectNum + "件");
                if (WWApplication.getInstance().isLogin()) {
                    if (shopProduct.HaveSku) {
                        if (selectSku != null) {
                            buyNow();
                        } else {
                            selfSupportGoodsSelectPop.showChooseWindow();
                        }
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
                    SelfSupportGoodsDetailFragment.this.startActivity(intent);
                }

            }

            @Override
            public void okListener(ShopProductSku sp, int num) {
                selectSku = sp;
                selectNum = num;
                tvStandard.setText(selectSku.ProName + "       " + selectNum + "件");
                if (WWApplication.getInstance().isLogin()) {
                    if (shopProduct.HaveSku) {
                        if (selectSku != null) {
                            addToCart();
                        } else {
                            selfSupportGoodsSelectPop.showChooseWindow();
                        }
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
                    SelfSupportGoodsDetailFragment.this.startActivity(intent);
                }
            }
        });
        return rootView;
    }

    private void collectOperate() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("productId", ProductId + "");
        jsonObject.put("fav", !isCollect + "");
        RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiShop.ProductFav());

        x.http().post(sessionParams, new WWXCallBack("ProductFav") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getBoolean("Data")) {
                    isCollect = !isCollect;
                    if (isCollect) {
                        ivCollect.setImageResource(R.drawable.mall_details_collectcheck_icon);
                    } else {
                        ivCollect.setImageResource(R.drawable.mall_details_collect_icon);
                    }
                }
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }


    private void isColect() {
        RequestParams sessionParams = ParamsUtils.getSessionParams(ApiShop.ProductFaved());
        sessionParams.addBodyParameter("productId", ProductId + "");
        x.http().get(sessionParams, new WWXCallBack("ProductFaved") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                isCollect = data.getBoolean("Data");
                if (isCollect) {
                    ivCollect.setImageResource(R.drawable.mall_details_collectcheck_icon);
                } else {
                    ivCollect.setImageResource(R.drawable.mall_details_collect_icon);
                }
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    //图片组
    private List<String> urls = new ArrayList<String>();

    private void getData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.ProductInfo());
        params.addBodyParameter("productId", ProductId + "");
        x.http().get(params,
                new WWXCallBack("ProductInfo") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        shopProduct = JSONObject.parseObject(data.getString("Data"), ShopProduct.class);
                        if (shopProduct.Images != null) {
                            ArrayList<String> mImages = new ArrayList<String>();
                            for (int i = 0; i < shopProduct.Images.size(); i++) {
                                mImages.add(shopProduct.Images.get(i).ImageUrl);
                            }
                            mAdapter.setData(mImages);
                            vpGoodsDetailsImgs.setAdapter(mAdapter);
                            indicator.setFillColor(getResources().getColor(
                                    R.color.yellow_ww));
                            indicator.setPageColor(0xAAFFFFFF);
                            indicator.setStrokeWidth(0);
                            indicator.setRadius(DensityUtil.dip2px(
                                    getActivity(), 3));
                            indicator.setViewPager(vpGoodsDetailsImgs);
                        }

                        WWViewUtil.textInsertDrawable(getActivity(), tvGoodsName, shopProduct.ProductName, false, shopProduct.IsVipLevel);
                        tvDescription.setText(shopProduct.ProductBrief);
                        tvSales.setText(shopProduct.SaleQty + "");
                        tvPriceNow.setText(WWViewUtil.numberFormatPrice(shopProduct.SalePrice));
                        tvPriceGone.setText(WWViewUtil.numberFormatPrice(shopProduct.SourcePrice));
                        tvPriceGone.getPaint().setAntiAlias(true);
                        tvPriceGone.getPaint()
                                .setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        tv_jifen_can_use.setText(WWViewUtil.numberFormatPrice(shopProduct.ConsumePayAmt));
                        tv_jifen_used.setText(WWViewUtil.numberFormatPrice(shopProduct.SalePrice-shopProduct.ConsumePayAmt));


                        if (shopProduct.HaveSku) {
                            llChooseStandard.setVisibility(View.VISIBLE);
                        } else {
                            llChooseStandard.setVisibility(View.GONE);
                        }
                        if (shopProduct.Status == 0) {
                            mGroup.findViewById(R.id.tv_soldout).setVisibility(View.VISIBLE);
                            tvAddCart.setVisibility(View.GONE);
                            tvGoPay.setVisibility(View.GONE);
                        } else {
                            mGroup.findViewById(R.id.tv_soldout).setVisibility(View.GONE);
                            tvAddCart.setVisibility(View.VISIBLE);
                            tvGoPay.setVisibility(View.VISIBLE);
                        }


                        tvShopsName.setText(shopProduct.VenderBrief);
                        tvShopsAddress.setText(shopProduct.VenderLocation);
                        tvEvaluateNum.setText("评价(" + shopProduct.JudgeCount + ")");

                        if (shopProduct.Judge != null) {
                            urls.clear();
                            llEvaluate.removeAllViews();
                            View view = getActivity().getLayoutInflater().inflate(R.layout.list_self_support_goods_evalute_item, null);
                            TextView tv_name = (TextView) view
                                    .findViewById(R.id.tv_name);
                            TextView tv_time = (TextView) view
                                    .findViewById(R.id.tv_time);
                            TextView tv_info = (TextView) view.findViewById(R.id.tv_info);
                            RatingBar rb = (RatingBar) view.findViewById(R.id.rb);
                            ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                            LinearLayout ll_container = (LinearLayout) view.findViewById(R.id.ll_container);
                            ShopProductJudge item = shopProduct.Judge;
                            ImageUtils.setCircleHeaderImage(getActivity(), item.HeadUrl, iv_img);
                            if (item.IsAnonymous) {
                                tv_name.setText(R.string.no_name);
                            } else {
                                tv_name.setText(item.UserName);
                            }
                            tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                            tv_info.setText(item.Content);
                            rb.setStar(item.JudgeLevel);
                            ll_container.removeAllViews();

                            if (!TextUtils.isEmpty(item.ImageUrl1)) {
                                ll_container.addView(creatImgView(item.ImageUrl1, 0));
                                urls.add(item.ImageUrl1);
                            }
                            if (!TextUtils.isEmpty(item.ImageUrl2)) {
                                ll_container.addView(creatImgView(item.ImageUrl2, 1));
                                urls.add(item.ImageUrl2);
                            }
                            if (!TextUtils.isEmpty(item.ImageUrl3)) {
                                ll_container.addView(creatImgView(item.ImageUrl3, 2));
                                urls.add(item.ImageUrl3);
                            }
                            if (!TextUtils.isEmpty(item.ImageUrl4)) {
                                ll_container.addView(creatImgView(item.ImageUrl4, 3));
                                urls.add(item.ImageUrl4);
                            }
                            if (!TextUtils.isEmpty(item.ImageUrl5)) {
                                ll_container.addView(creatImgView(item.ImageUrl5, 4));
                                urls.add(item.ImageUrl4);
                            }
                            llEvaluate.addView(view);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    /**
     * 图片组装
     */
    private View creatImgView(String url, final int pos) {
        int width = (getResources().getDisplayMetrics().widthPixels
                - DensityUtil.dip2px(getActivity(), 20)) / 5;
        final ImageView imageView = new ImageView(getActivity());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                width, width);
        imageView.setPadding(DensityUtil.dip2px(getActivity(), 5), DensityUtil.dip2px(getActivity(), 5), DensityUtil.dip2px(getActivity(), 5), DensityUtil.dip2px(getActivity(), 5));
        imageView.setLayoutParams(layoutParams);
        ImageUtils.setCommonImage(getActivity(), ImageUtils.getRightImgScreen(url, width, width), imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.startBigImageActivity(getActivity(), urls, pos);
            }
        });
        return imageView;
    }


    @OnClick({R.id.ll_choose_standard, R.id.ll_parameter, R.id.ll_evaluate_more, R.id.ll_collect, R.id.rl_shop_car, R.id.tv_add_cart, R.id.tv_go_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_choose_standard:
                if (shopProduct != null && shopProduct.HaveSku) {
                    selfSupportGoodsSelectPop.showChooseWindow();
                }
                break;
            case R.id.ll_parameter:
                if (selfSupportGoodsParameterPop == null)
                    selfSupportGoodsParameterPop = new SelfSupportGoodsParameterPop(getActivity(), getLayoutId(), ProductId);
                selfSupportGoodsParameterPop.showWindow();
                break;
            case R.id.ll_evaluate_more:
                getActivity().startActivity(new Intent(getActivity(), SelfSupportGoodsEvaluateListActivity.class).putExtra(Consts.KEY_DATA, ProductId));
                break;
            case R.id.ll_collect:
                if (WWApplication.getInstance().isLogin()) {
                    collectOperate();
                } else {
                    Intent intent1 = new Intent(getActivity(), LoginActivity.class);
                    intent1.putExtra(LoginActivity.ISFRAOMMAIN, false);
                    SelfSupportGoodsDetailFragment.this.startActivity(intent1);
                }
                break;
            case R.id.rl_shop_car:
                PublicWay.stratSelfSupportShopCartActivity(SelfSupportGoodsDetailFragment.this);
                break;
            case R.id.tv_add_cart:
                if (WWApplication.getInstance().isLogin()) {
                    if (shopProduct != null && shopProduct.HaveSku) {
                        selfSupportGoodsSelectPop.setOperateView(false, true);
                        selfSupportGoodsSelectPop.showChooseWindow();
                    } else {
                        addToCart();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
                    SelfSupportGoodsDetailFragment.this.startActivity(intent);
                }

                break;
            case R.id.tv_go_pay:
                if (WWApplication.getInstance().isLogin()) {
                    if (shopProduct != null && shopProduct.HaveSku) {
                        selfSupportGoodsSelectPop.setOperateView(true, false);
                        selfSupportGoodsSelectPop.showChooseWindow();
                    } else {
                        buyNow();
                    }
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
                    SelfSupportGoodsDetailFragment.this.startActivity(intent);
                }
                break;
        }
    }

    private void buyNow() {
        try {
            if ((selectSku != null && selectNum > selectSku.StockQty) || selectNum > shopProduct.StockQty) {
                WWToast.showShort(R.string.stockQty_less_tips);
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                    .getInstance().getSessionId());
            jsonObject.put("productId", ProductId + "");
            jsonObject.put("quantity", selectNum + "");
            if (selectSku != null) {
                jsonObject.put("skuId", selectSku.SkuId + "");
            }

            RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiShop.CartAtOnce());

            x.http().post(sessionParams, new WWXCallBack("CartAtOnce") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    long[] selectGoodsFlowId = new long[1];
                    selectGoodsFlowId[0] = data.getLong("Data");
                    excuteOrderPreview(selectGoodsFlowId);
                }

                @Override
                public void onAfterFinished() {

                }
            });

        } catch (NullPointerException e) {
        }
    }

    private void excuteOrderPreview(final long[] selectGoodsFlowId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", selectGoodsFlowId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderPreview()),
                new WWXCallBack("OrderPreview") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        String string = data.getString("Data");
                        Intent intent = new Intent(getActivity(), SelfSupportMakesureOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Consts.KEY_DATA, string);
                        bundle.putLongArray("FlowsId", selectGoodsFlowId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                    }
                });
    }

    private void addToCart() {
        try {
            if ((selectSku != null && selectNum > selectSku.StockQty) || selectNum > shopProduct.StockQty) {
                WWToast.showShort(R.string.stockQty_less_tips);
                return;
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                    .getInstance().getSessionId());
            jsonObject.put("productId", ProductId + "");
            jsonObject.put("quantity", selectNum + "");
            if (selectSku != null) {
                jsonObject.put("skuId", selectSku.SkuId + "");
            }

            showWaitDialog();
            RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiShop.CartAdd());

            x.http().post(sessionParams, new WWXCallBack("CartAdd") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    WWToast.showShort(R.string.add_cart_success);
                    int cartNum = SharedPreferenceUtils.getInstance().getCartNum() + selectNum;
                    SharedPreferenceUtils.getInstance().saveCartNum(cartNum);
                    setCartNum(cartNum);
                }

                @Override
                public void onAfterFinished() {
                    dismissWaitDialog();
                }
            });
        } catch (NullPointerException e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WWApplication.getInstance().isLogin()) {
            isColect();
            int Quantity = SharedPreferenceUtils.getInstance().getCartNum();
            setCartNum(Quantity);
        }
    }

    public void setCartNum(int quantity) {
        if (quantity > 0) {
            tvShopCartCount.setVisibility(View.VISIBLE);
            if (quantity > 99) {
                tvShopCartCount.setText("99+");
            } else {
                tvShopCartCount.setText(quantity + "");
            }
        } else {
            tvShopCartCount.setVisibility(View.GONE);
        }
    }

}
