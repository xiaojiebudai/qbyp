package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.HappyAreaActivity;
import cn.net.chenbao.qbyp.activity.JiFenShopActivity;
import cn.net.chenbao.qbyp.activity.LocalLifeActivity;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.RechargeActivity;
import cn.net.chenbao.qbyp.activity.SearchActiivty;
import cn.net.chenbao.qbyp.activity.SelfSupportActivity;
import cn.net.chenbao.qbyp.activity.VipActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BussinessAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.TabScrollView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页模块
 *
 * @author xl
 * @date:2016-7-25上午10:57:47
 * @description
 */
public class HomePageFragment extends FatherFragment implements
        OnClickListener, TabScrollView.OnItemSelectListener {
    private View banner;
    private int bannerHight;
    private PullToRefreshScrollView mListView;
    private BussinessAdapter mAdapter;
    private int width;
    //广告区域
    private LinearLayout ll_ad0, ll_ad1, ll_ad2;
    private ImageView iv_ad00, iv_ad01, iv_ad02, iv_ad03, iv_ad10, iv_ad11, iv_ad12, iv_ad13, iv_ad14, iv_ad20, iv_ad21, iv_ad22;

    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private RelativeLayout ll_banner_container;
    private LinearLayout ll_title;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_pager_new;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        mGroup.findViewById(R.id.ll_search).setOnClickListener(this);
        // mGroup.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速,为了显示虚线
        width = DensityUtil.getScreenWidth(getActivity());
        bannerHight = width * 340 / 720;

        mListView = (PullToRefreshScrollView) mGroup.findViewById(R.id.listview);
        mAdapter = new BussinessAdapter(getActivity());

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ObservableScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                initBanner(Banner.LOCAL_BANNER);
                initBanner(Banner.LOCAL_AD_0_LEFT);
                initBanner(Banner.LOCAL_AD_0_RIGHT_DOWN);
                initBanner(Banner.LOCAL_AD_0_RIGHT_UP);
                initBanner(Banner.LOCAL_AD_1_LEFT);
                initBanner(Banner.LOCAL_AD_1_RIGHT);
                initBanner(Banner.LOCAL_AD_2);
            }
        });
        banner = mInflater.inflate(R.layout.b, null);
        ll_banner_container = (RelativeLayout) banner.findViewById(R.id.ll_banner_container);
        adImgs = (HorizontalInnerViewPager) banner.findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) banner.findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(getActivity(), bannerHight);
        banner.findViewById(R.id.ll_vip).setOnClickListener(this);
        banner.findViewById(R.id.ll_recharge_center).setOnClickListener(this);
        banner.findViewById(R.id.ll_e).setOnClickListener(this);
        banner.findViewById(R.id.ll_happy_area).setOnClickListener(this);
        banner.findViewById(R.id.ll_self).setOnClickListener(this);

        ll_ad0 = (LinearLayout) banner.findViewById(R.id.ll_ad0);
        ll_ad1 = (LinearLayout) banner.findViewById(R.id.ll_ad1);
        ll_ad2 = (LinearLayout) banner.findViewById(R.id.ll_ad2);
        iv_ad00 = (ImageView) banner.findViewById(R.id.iv_ad00);
        iv_ad01 = (ImageView) banner.findViewById(R.id.iv_ad01);
        iv_ad02 = (ImageView) banner.findViewById(R.id.iv_ad02);
        iv_ad03 = (ImageView) banner.findViewById(R.id.iv_ad03);
        iv_ad10 = (ImageView) banner.findViewById(R.id.iv_ad10);
        iv_ad11 = (ImageView) banner.findViewById(R.id.iv_ad11);
        iv_ad12 = (ImageView) banner.findViewById(R.id.iv_ad12);
        iv_ad13 = (ImageView) banner.findViewById(R.id.iv_ad13);
        iv_ad14 = (ImageView) banner.findViewById(R.id.iv_ad14);
        iv_ad20 = (ImageView) banner.findViewById(R.id.iv_ad20);
        iv_ad21 = (ImageView) banner.findViewById(R.id.iv_ad21);
        iv_ad22 = (ImageView) banner.findViewById(R.id.iv_ad22);


        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                width, width / 180 * 91);
        params0.setMargins(0, DensityUtil.dip2px(getActivity(), 10), 0, 0);
        ll_ad0.setLayoutParams(params0);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                width, width / 180 * 105);
        params1.setMargins(0, DensityUtil.dip2px(getActivity(), 10), 0, 0);
        ll_ad1.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                width, width / 180 * 85);
        params2.setMargins(0, DensityUtil.dip2px(getActivity(), 10), 0, 0);
        ll_ad2.setLayoutParams(params2);

        mListView.getRefreshableView().addView(banner);

        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = bannerHight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);
        initBanner(Banner.LOCAL_BANNER);
        initBanner(Banner.LOCAL_AD_0_LEFT);
        initBanner(Banner.LOCAL_AD_0_RIGHT_DOWN);
        initBanner(Banner.LOCAL_AD_0_RIGHT_UP);
        initBanner(Banner.LOCAL_AD_1_LEFT);
        initBanner(Banner.LOCAL_AD_1_RIGHT);
        initBanner(Banner.LOCAL_AD_2);
        ll_title = (LinearLayout) mGroup.findViewById(R.id.ll_title);
        //重新设置高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) ll_title.getLayoutParams();
            paramsTitle.height = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
            ll_title.setLayoutParams(paramsTitle);
            ll_title.setPadding(0, WWViewUtil.getStatusBarHeight(getActivity()), 0, 0);
            ll_title.setBackgroundColor(Color.argb((int) 0, 230, 162, 43));
        }
        mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_translate_shape);

        mListView.getRefreshableView().setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
                int titleHeight = DensityUtil.dip2px(getActivity(), 44);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    titleHeight = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
                }
                if (y <= 0) {   //设置标题的背景颜色
                    ll_title.setBackgroundColor(Color.argb((int) 0, 230, 162, 43));
                    mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_translate_shape);
                } else if (y > 0 && y <= titleHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / titleHeight;
                    int alpha = (int) (255 * scale);
                    ll_title.setBackgroundColor(Color.argb((int) alpha, 230, 162, 43));
                    if (y - titleHeight > 100) {
                        mGroup.findViewById(R.id.ll_search).getBackground().setAlpha(255);
                        mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_shape);
                    }
                } else {    //滑动到banner下面设置普通颜色
                    ll_title.setBackgroundColor(Color.argb((int) 255, 230, 162, 43));
                    mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_shape);
                }
            }
        });
    }

    private void initBanner(final int index) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiBaseData.BannersGet());
        params.addBodyParameter("place", index + "");
        x.http().get(params,
                new WWXCallBack("BannersGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        List<Banner> list = JSON.parseArray(
                                jsonArray.toJSONString(), Banner.class);
                        if (list != null) {
                            switch (index) {
                                case Banner.LOCAL_BANNER:
                                    ArrayList<String> mImages = new ArrayList<String>();
                                    for (int i = 0; i < list.size(); i++) {
                                        mImages.add(getRightImgScreen(list.get(i).PicUrl, width, bannerHight));
                                    }
                                    mbAdapter.setData(mImages, list);
                                    adImgs.setAdapter(mbAdapter);
                                    indicator.setFillColor(getResources().getColor(
                                            R.color.yellow_ww));
                                    indicator.setPageColor(0xAAFFFFFF);
                                    indicator.setStrokeWidth(0);
                                    indicator.setRadius(DensityUtil.dip2px(
                                            getActivity(), 3));
                                    indicator.setViewPager(adImgs);
                                    break;
                                case Banner.LOCAL_AD_0_LEFT:
                                    setImgAndLink(list.get(0), iv_ad00);
                                    break;
                                case Banner.LOCAL_AD_0_RIGHT_UP:
                                    setImgAndLink(list.get(0), iv_ad01);
                                    break;
                                case Banner.LOCAL_AD_0_RIGHT_DOWN:
                                    setImgAndLink(list.get(0), iv_ad02);
                                    setImgAndLink(list.get(1), iv_ad03);
                                    break;
                                case Banner.LOCAL_AD_1_LEFT:
                                    setImgAndLink(list.get(0), iv_ad10);
                                    break;
                                case Banner.LOCAL_AD_1_RIGHT:
                                    setImgAndLink(list.get(0), iv_ad11);
                                    setImgAndLink(list.get(1), iv_ad12);
                                    setImgAndLink(list.get(2), iv_ad13);
                                    setImgAndLink(list.get(3), iv_ad14);
                                    break;
                                case Banner.LOCAL_AD_2:
                                    setImgAndLink(list.get(0), iv_ad20);
                                    setImgAndLink(list.get(1), iv_ad21);
                                    setImgAndLink(list.get(2), iv_ad22);
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                        mListView.onRefreshComplete();
                    }
                });

    }

    /**
     * 设置广告位跳转
     *
     * @param banner
     * @param iv
     */
    private void setImgAndLink(final Banner banner, ImageView iv) {

        ImageUtils.setCommonImageNo(getActivity(), banner.PicUrl, iv);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AdLink(banner);
            }
        });
    }

    private void AdLink(Banner banner) {
        switch (banner.OperType) {
            case 0:
                //无操作
                break;
            case 1:
                //网页
                PublicWay.startWebViewActivity(getActivity(),banner.BannerName,banner.OperValue, WebViewActivity.URL);
                break;
            case 2:
                //原生界面
                if (banner.OperValue.equals(Banner.PAGECODE_SELF)) {
                    startActivity(new Intent(getActivity(), SelfSupportActivity.class));
                }else if(banner.OperValue.equals(Banner.CONSUME_AREA)){
                   startActivity(new Intent(getActivity(), JiFenShopActivity.class));
                }
                break;
            case 3:
                //自营商品
                PublicWay.stratSelfSupportGoodsDetailActivity(getActivity(), Long.parseLong(banner.OperValue));
                break;
            case 4:
                //本地商家店铺
                if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
                    Intent intent = new Intent(getActivity(),
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    PublicWay.startStoreActivity(getActivity(), Long.parseLong(banner.OperValue), 0);
                }
                break;
            case 5:
                //分销产品详情
                PublicWay.startDistributionGoodsDetailActivity(getActivity(), Long.parseLong(banner.OperValue));
                break;
        }
    }

    private String getRightImgScreen(String picUrl, int width, int height) {
        return picUrl.replace("__", "_" + width + "x" + height);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_search:
                PublicWay.startSearchActivity(getActivity(), SearchActiivty.LOCATION, 0);
                break;
            case R.id.ll_vip:
                startActivity(new Intent(getActivity(), VipActivity.class));
                break;
            case R.id.ll_recharge_center:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), RechargeActivity.class));
                break;
            case R.id.ll_e:
                startActivity(new Intent(getActivity(), LocalLifeActivity.class));
                break;
            case R.id.ll_happy_area:
                startActivity(new Intent(getActivity(), HappyAreaActivity.class));
                break;
            case R.id.ll_self:
                startActivity(new Intent(getActivity(), SelfSupportActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelect(int index, View itemView) {

    }
}
