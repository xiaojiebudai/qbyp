package cn.net.chenbao.qbyp.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.umeng.analytics.MobclickAgent;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.JiFenShopActivity;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.SearchActiivty;
import cn.net.chenbao.qbyp.activity.SearchDataActiivty;
import cn.net.chenbao.qbyp.activity.SelfSupportActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.LocalCategoryAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.Location;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.BaiDuLocationUtils;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.view.TabScrollView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 首页模块  2015-3-20期修改
 *
 * @author xl
 * @date:2016-7-25上午10:57:47
 * @description
 */
public class HomePageFragment2 extends FatherFragment implements
        OnClickListener, TabScrollView.OnItemSelectListener, PermissionUtil.PermissionCallbacks {
    private int bannerHight;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private int width;
    //广告区域
    private LinearLayout ll_ad0, ll_ad1;
    private ImageView iv_ad00, iv_ad01, iv_ad10, iv_ad11;

    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private RelativeLayout ll_banner_container;
    private LinearLayout ll_title, ll_category;
    private HorizontalInnerViewPager vp_category;
    private CirclePageIndicator category_indicator;
    private BaiDuLocationUtils instance;
    private Location location;
    private TextView tv_head_center;
    private boolean isLocation = true;
    public static final int REQUEST_LOCTION = 10086;
    public static final int REQUEST_CODE_ASK_LOCATION = 10010;
    public int sellerCurrentPager = 0;
    private LinearLayout ll_container;
    private TextView tv_search_tips;
    private boolean hasBanner, hasAd, hasCategory;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_pager_new2;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        findView();
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        // mGroup.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速,为了显示虚线
        width = DensityUtil.getScreenWidth(getActivity());
        bannerHight = width * 340 / 720;
        mbAdapter = new BannerPagerAdapter(getActivity(), bannerHight);

        mPullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ObservableScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                if (!hasBanner) {
                    initBanner(Banner.LOCAL_BANNER);
                }
                if (!hasAd) {
                    initBanner(Banner.LOCAL_LIFE_AD);
                }
                if (!hasCategory) {
                    getTradesCategory();
                }

                if (!isInterfaceLoading) {
                    sellerCurrentPager = 0;
                    getSelller();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                if (HaveNextPage) {
                    if (!isInterfaceLoading) {
                        getSelller();
                    }
                } else {
                    mPullToRefreshScrollView.onRefreshComplete();
                    WWToast.showShort(R.string.nomore_data);
                }

            }
        });

        LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                width, width / 360 * 80);
        params0.setMargins(0, DensityUtil.dip2px(getActivity(), 10), 0, 0);
        ll_ad0.setLayoutParams(params0);
        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                width, width / 360 * 80);
        params1.setMargins(0, 0, 0, 0);
        ll_ad1.setLayoutParams(params1);

        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = bannerHight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);

        //重新设置高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) ll_title.getLayoutParams();
            paramsTitle.height = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
            ll_title.setLayoutParams(paramsTitle);
            ll_title.setPadding(0, WWViewUtil.getStatusBarHeight(getActivity()), 0, 0);
            ll_title.setBackgroundColor(Color.argb((int) 0, 255, 140, 27));//cyk 230, 162, 43
            tv_search_tips.setTextColor(getResources().getColor(R.color.white));
        }

        mPullToRefreshScrollView.getRefreshableView().setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
                int titleHeight = DensityUtil.dip2px(getActivity(), 44);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    titleHeight = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
                }
                if (y <= 0) {   //设置标题的背景颜色
                    ll_title.setBackgroundColor(Color.argb((int) 0, 255, 140, 27));
                    mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_translate_shape);
                    tv_search_tips.setTextColor(getResources().getColor(R.color.white));
                } else if (y > 0 && y <= titleHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / titleHeight;
                    int alpha = (int) (255 * scale);
                    ll_title.setBackgroundColor(Color.argb((int) alpha, 255, 140, 27));
                    if (y - titleHeight > 100) {
                        mGroup.findViewById(R.id.ll_search).getBackground().setAlpha(255);
                        mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_shape);
                        tv_search_tips.setTextColor(getResources().getColor(R.color.text_c1));
                    }
                } else {    //滑动到banner下面设置普通颜色
                    ll_title.setBackgroundColor(Color.argb((int) 255, 255, 140, 27));
                    mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_shape);
                    tv_search_tips.setTextColor(getResources().getColor(R.color.text_c1));
                }
            }
        });
        startLocation();
        initBanner(Banner.LOCAL_BANNER);
        initBanner(Banner.LOCAL_LIFE_AD);
        getTradesCategory();
    }

    private void findView() {
        mPullToRefreshScrollView = (PullToRefreshScrollView) mGroup.findViewById(R.id.listview);
        tv_head_center = (TextView) mGroup.findViewById(R.id.tv_head_center);
        tv_search_tips = (TextView) mGroup.findViewById(R.id.tv_search_tips);
        ll_title = (LinearLayout) mGroup.findViewById(R.id.ll_title);
        ll_category = (LinearLayout) mGroup.findViewById(R.id.ll_category);
        ll_container = (LinearLayout) mGroup.findViewById(R.id.ll_containner);
        ll_ad0 = (LinearLayout) mGroup.findViewById(R.id.ll_ad0);
        ll_ad1 = (LinearLayout) mGroup.findViewById(R.id.ll_ad1);
        iv_ad00 = (ImageView) mGroup.findViewById(R.id.iv_ad00);
        iv_ad01 = (ImageView) mGroup.findViewById(R.id.iv_ad01);
        iv_ad10 = (ImageView) mGroup.findViewById(R.id.iv_ad10);
        iv_ad11 = (ImageView) mGroup.findViewById(R.id.iv_ad11);
        ll_banner_container = (RelativeLayout) mGroup.findViewById(R.id.ll_banner_container);
        adImgs = (HorizontalInnerViewPager) mGroup.findViewById(R.id.vp_imgs);
        vp_category = (HorizontalInnerViewPager) mGroup.findViewById(R.id.vp_category);
        category_indicator = (CirclePageIndicator) mGroup.findViewById(R.id.category_indicator);
        indicator = (CirclePageIndicator) mGroup.findViewById(R.id.indicator);
        mGroup.findViewById(R.id.ll_search).setBackgroundResource(R.drawable.bg_white_oval_translate_shape);

        mGroup.findViewById(R.id.ll_locate).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_search).setOnClickListener(this);
    }

    private void startLocation() {
        instance = new BaiDuLocationUtils(getActivity());
        instance.setLocationListener(new BaiDuLocationUtils.LocationListener() {
            @Override
            public void LocationMessageCallBack(BDLocation bdLocation) {
                if (bdLocation.getAddress().address != null) {
                    location = new Location();
                    location.latitudes = bdLocation.getLatitude();
                    location.Longitudes = bdLocation.getLongitude();
                    location.city = bdLocation.getCity();
                    tv_head_center.setText(bdLocation.getAddress().address);
                    SharedPreferenceUtils.getInstance().saveLocation(JSON.toJSONString(location));
                    instance.stopLocation();
                    if (isLocation) {
                        if (!isInterfaceLoading) {
                            isLocation = false;
                            sellerCurrentPager = 0;
                            getSelller();
                        }
                    }
                }
            }
        });
        ArrayList<String> list = new ArrayList<String>();
        if (!PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION})) {
            list.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        }
        if (!PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION})) {
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //处理友盟统计关键权限
        if (PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE})) {
            MobclickAgent.setScenarioType(getActivity(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        } else {
            list.add(Manifest.permission.READ_PHONE_STATE);

        }
        if (list.size() > 0) {

            PermissionUtil.requestPermissions(this, REQUEST_CODE_ASK_LOCATION, (String[]) list.toArray(new String[list.size()]));
        }
        instance.startLocation();
    }

    /**
     * 获取商家
     */
    private boolean HaveNextPage;
    private boolean isInterfaceLoading = false;

    public void getSelller() {
        isInterfaceLoading = true;

        if (location == null) {
            WWToast.showShort(R.string.cannot_get_current_position);
            mPullToRefreshScrollView.onRefreshComplete();
            isInterfaceLoading = false;
            return;
        }
        showWaitDialog();

        RequestParams requestParams = new RequestParams(ApiSeller.searchTrade());
        requestParams.addBodyParameter("longitude", location.Longitudes + "");
        requestParams.addBodyParameter("latitude", location.latitudes + "");
        requestParams.addBodyParameter("pageIndex", sellerCurrentPager
                + "");
        x.http().get(requestParams, new WWXCallBack("Search") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (sellerCurrentPager == 0) {
                    ll_container.removeAllViews();
                }
                sellerCurrentPager++;
                HaveNextPage = data.getBoolean("HaveNextPage");

                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesMessage> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesMessage.class);

                for (int i = 0; i < list.size(); i++) {
                    TradesMessage message = list.get(i);
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.listview_bussiness_item, null);
                    view.setTag(message.SellerId);
                    TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                    tv_distance.setText(message.Distance < 1000 ? message.Distance + "m"
                            : message.Distance / 1000 + "km");
                    ImageView iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
                    ImageUtils.setCommonRadiusImage(getActivity(), message.ShopPicture, iv_pic);
                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                    tv_name.setText(message.SellerName);
                    TextView tv_fen = (TextView) view.findViewById(R.id.tv_fen);
                    tv_fen.setText((float) Arith.round(message.JudgeLevel, 1) + "");
                    TextView tv_sales = (TextView) view.findViewById(R.id.tv_sales);
                    tv_sales.setText(String.format(getString(R.string.have_sale_number), message.OrderCount));
                    RatingBar rb = (RatingBar) view.findViewById(R.id.rb);
                    rb.setStar((float) Arith.round(message.JudgeLevel, 1));
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
                                Intent intent = new Intent(getActivity(),
                                        LoginActivity.class);
                                intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
                                startActivity(intent);
                            } else {
                                PublicWay.startStoreActivity(getActivity(),
                                        (long) view.getTag(), 0);
                            }
                        }
                    });
                    ll_container.addView(view);
                }
            }

            @Override
            public void onAfterFinished() {
                isInterfaceLoading = false;
                mPullToRefreshScrollView.onRefreshComplete();
                dismissWaitDialog();
            }
        });
    }

    private void initBanner(final int index) {
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
                                    hasBanner = true;
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
                                    break;
                                case Banner.LOCAL_AD_1_LEFT:
                                    setImgAndLink(list.get(0), iv_ad10);
                                    break;
                                case Banner.LOCAL_AD_1_RIGHT:
                                    setImgAndLink(list.get(0), iv_ad11);
                                    break;
                                case Banner.LOCAL_AD_2:
                                    break;
                                case Banner.LOCAL_LIFE_AD:
                                    hasAd = true;
                                    setImgAndLink(list.get(0), iv_ad00);
                                    setImgAndLink(list.get(1), iv_ad01);
                                    setImgAndLink(list.get(2), iv_ad10);
                                    setImgAndLink(list.get(3), iv_ad11);
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        mPullToRefreshScrollView.onRefreshComplete();
                    }
                });

    }

    private void getTradesCategory() {
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        x.http().get(params, new WWXCallBack("TradesGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesCategory> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesCategory.class);
                list.add(list.size(), new TradesCategory("-1", "全部分类"));
                hasCategory = true;
                ll_category.setVisibility(View.VISIBLE);
                HashMap hashMap = new HashMap<Integer, List<TradesCategory>>();

                int pageCount = (list.size() / 8) + (list.size() % 8 > 0 ? 1 : 0);
                for (int i = 0; i < pageCount; i++) {
                    List<TradesCategory> sublist;
                    if (i == 0 && pageCount == 1) {
                        sublist = list.subList(0, list.size());
                    } else if (i == pageCount - 1) {
                        sublist = list.subList(i * 8, list.size());
                    } else {
                        sublist = list.subList(i * 8, (i + 1) * 8);
                    }
                    hashMap.put(i, sublist);
                }
                LocalCategoryAdapter localCategoryAdapter = new LocalCategoryAdapter(getActivity(), hashMap, pageCount);
                vp_category.setAdapter(localCategoryAdapter);

                localCategoryAdapter.setCategoryClickListener(new LocalCategoryAdapter.CategoryClickListener() {
                    @Override
                    public void categoryClick(TradesCategory tradesCategory) {
                        if (tradesCategory.TradeIco == "-1") {
                            PublicWay.startCategoryActivity(getActivity(),
                                    location == null ? 0 : location.latitudes, location == null ? 0 : location.Longitudes);
                        } else {
                            PublicWay.startSearchResultActivity(
                                    getActivity(),
                                    tradesCategory.TradeId, 0,
                                    location == null ? 0 : location.latitudes, location == null ? 0 : location.Longitudes,
                                    SearchDataActiivty.MORE_MODE);
                        }
                    }
                });

                category_indicator.setFillColor(getResources().getColor(
                        R.color.yellow_ww));
                category_indicator.setPageColor(getResources().getColor(R.color.gray_bg));
                category_indicator.setStrokeWidth(0);
                category_indicator.setRadius(DensityUtil.dip2px(
                        getActivity(), 3));
                category_indicator.setViewPager(vp_category);
            }

            @Override
            public void onAfterFinished() {

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
                PublicWay.startWebViewActivity(getActivity(), banner.BannerName, banner.OperValue, WebViewActivity.URL);
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
            case R.id.ll_locate:
                if (location != null) {
                    PublicWay.startLocateActivity(this, REQUEST_LOCTION, JSONObject.toJSONString(location));
                } else {
                    showNoLocaltionPermissionDialog();
                }
                break;
            case R.id.ll_search:
                PublicWay.startSearchActivity(getActivity(), SearchActiivty.LOCATION, 0);
                break;
            default:
                break;
        }
    }

    private void showNoLocaltionPermissionDialog() {
        final CommonDialog commonDialogTwiceConfirm = DialogUtils.getCommonDialogTwiceConfirm(getActivity(), R.string.get_position_fail_open_permission, true);
        commonDialogTwiceConfirm.setRightButtonText(getString(R.string.go_settle));
        commonDialogTwiceConfirm.setRightButtonCilck(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialogTwiceConfirm.dismiss();
                Uri packageURI = Uri.parse("package:" + getActivity().getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
            }
        });
        commonDialogTwiceConfirm.show();
    }

    @Override
    public void onItemSelect(int index, View itemView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOCTION:// 地理位置
                    location = JSON.parseObject(
                            data.getStringExtra("location"), Location.class);
                    SharedPreferenceUtils.getInstance().saveLocation(JSON.toJSONString(location));
                    tv_head_center.setText(location.name);
                    if (WWApplication.getInstance().isLogin()) {
                        sendAddressRequest(location);
                    }
                    if (!isInterfaceLoading) {
                        sellerCurrentPager = 0;
                        getSelller();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void sendAddressRequest(final Location location) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("longitude", location.Longitudes);
        jsonObject.put("latitude", location.latitudes);
        jsonObject.put("address", location.street);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiUser.getLocationSubmit()),
                new WWXCallBack("LocationSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                    }

                    @Override
                    public void onAfterFinished() {
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_ASK_LOCATION && perms.contains(Manifest.permission.READ_PHONE_STATE)) {
            MobclickAgent.setScenarioType(getActivity(), MobclickAgent.EScenarioType.E_UM_NORMAL);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_ASK_LOCATION) {
            WWToast.showShort(R.string.locate_permission_deny_cannot_get_nearby);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            instance.stopLocation();
        }
    }
}
