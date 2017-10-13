package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.JiFenShopActivity;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.SearchActiivty;
import cn.net.chenbao.qbyp.activity.SelfSupportActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportCategoryActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.adapter.SelfSupportCategrayGridViewAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportGoodsItemTwoAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.bean.ShopFloorProducts;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.ExpandListview;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.NoScrollGridView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城首页
 */

public class SelfSupportFragment extends FatherFragment {
    @BindView(R.id.rl_head_left)
    LinearLayout rlHeadLeft;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_shopcart_count)
    TextView tvShopcartCount;
    @BindView(R.id.rl_head_right)
    RelativeLayout rlHeadRight;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    private NoScrollGridView gvCategray;
    private LinearLayout ll_recommend, ll_container_imgad, ll_container_floor;
    private SelfSupportCategrayGridViewAdapter mGridAdapter;
    private View fake_status_bar;
    private int width;
    private int pxHeight;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_self_support;
    }

    @Override
    protected void initView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private SelfSupportGoodsItemTwoAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private RelativeLayout ll_banner_container;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        pxHeight = width * 384 / 720;
        fake_status_bar = mGroup.findViewById(R.id.fake_status_bar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fake_status_bar.getLayoutParams();
            params1.height = WWViewUtil.getStatusBarHeight(getActivity());
            fake_status_bar.setLayoutParams(params1);
            fake_status_bar.setVisibility(View.VISIBLE);

        }
//        rlHeadLeft.setVisibility(View.INVISIBLE);
//        mGroup.findViewById(R.id.rl_common_menu).setVisibility(View.INVISIBLE);
        View hearder = mInflater.inflate(R.layout.header_self_support, null);
        ll_banner_container = (RelativeLayout) hearder.findViewById(R.id.ll_banner_container);
        adImgs = (HorizontalInnerViewPager) hearder.findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) hearder.findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(getActivity(), pxHeight);
        gvCategray = (NoScrollGridView) hearder.findViewById(R.id.gv_categray);
        ll_recommend = (LinearLayout) hearder.findViewById(R.id.ll_recommend);
        ll_container_imgad = (LinearLayout) hearder.findViewById(R.id.ll_container_imgad);
        ll_container_floor = (LinearLayout) hearder.findViewById(R.id.ll_container_floor);
        listview.getRefreshableView().addHeaderView(hearder);
        mAdapter = new SelfSupportGoodsItemTwoAdapter(getActivity());
        listview.setAdapter(mAdapter);
        // 设置 end-of-list监听
        listview
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getListGoods();
                        }

                    }
                });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 0;
                getListGoods();
                initBanner(Banner.SELF_BANNER);
                initBanner(Banner.SELF_AD);
                getIndexClass();
                getProductRecommend();
                getProductFloor();
            }
        });
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = pxHeight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);
        setHeaderView();
        getListGoods();
        getCartSum();
    }

    /**
     * 设置headerview
     */
    private void setHeaderView() {
        mGridAdapter = new SelfSupportCategrayGridViewAdapter(getActivity());
        gvCategray.setAdapter(mGridAdapter);
        gvCategray.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == (mGridAdapter.getCount() - 1)) {
                    startActivity(new Intent(getActivity(), SelfSupportCategoryActivity.class));
                } else {
                    PublicWay.stratSelfSupportSearchDataActivity(getActivity(), "", mGridAdapter.getItem(position).ClassId);
                }
            }
        });
        initBanner(Banner.SELF_BANNER);
        initBanner(Banner.SELF_AD);
        getIndexClass();
        getProductRecommend();
        getProductFloor();
    }

    private void getProductFloor() {
        RequestParams requestParams = new RequestParams(ApiShop.ShopFloorProductsGet());
        requestParams.addBodyParameter("pageCode", "ShopArea");
        x.http().get(requestParams, new WWXCallBack("ShopFloorProductsGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ArrayList<ShopFloorProducts> list = (ArrayList<ShopFloorProducts>) JSONArray.parseArray(
                        data.getString("Data"), ShopFloorProducts.class);
                if (list != null && list.size() > 0) {
                    ll_container_floor.setVisibility(View.VISIBLE);
                    ll_container_floor.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        View view = mInflater.inflate(R.layout.list_selfsupport_floor_goods, null);
                        ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
                        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                        ShopFloorProducts item = list.get(i);
                        ExpandListview lv = (ExpandListview) view.findViewById(R.id.lv);
                        SelfSupportGoodsItemTwoAdapter adapter = new SelfSupportGoodsItemTwoAdapter(getActivity());
                        lv.setAdapter(adapter);
                        ImageUtils.setCommonImage(getActivity(), item.IcoUrl, iv_img);
                        tv_name.setText(item.FloorName);
                        adapter.setDatas(item.Products);
                        ll_container_floor.addView(view);
                    }
                } else {
                    ll_container_floor.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAfterFinished() {
                listview.onRefreshComplete();
            }
        });
    }

    private void getProductRecommend() {
        x.http().get(new RequestParams(ApiShop.ProductRecommends()), new WWXCallBack("ProductRecommends") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<ShopProduct> list = JSON.parseArray(
                        jsonArray.toJSONString(), ShopProduct.class);
                ll_recommend.removeAllViews();
                for (int i = 0; i < list.size(); i++) {
                    View view = mInflater.inflate(R.layout.list_selfsupport_goods_item, null);
                    ImageView iv_good_img = (ImageView) view.findViewById(R.id.iv_good_img);
                    TextView tv_good_name = (TextView) view.findViewById(R.id.tv_good_name);
                    TextView tv_good_price = (TextView) view.findViewById(R.id.tv_good_price);
                    ShopProduct shopProduct = list.get(i);
                    ImageUtils.setCommonImage(getActivity(), shopProduct.ImageUrl, iv_good_img);
                    WWViewUtil.textInsertDrawable(getActivity(), tv_good_name, shopProduct.ProductName, false, shopProduct.IsVipLevel);

                    tv_good_price.setText(WWViewUtil.numberFormatPrice(shopProduct.SalePrice));
                    view.setTag(shopProduct.ProductId);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.stratSelfSupportGoodsDetailActivity(SelfSupportFragment.this, (Long) v.getTag());
                        }
                    });
                    ll_recommend.addView(view);
                }
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    /**
     * 获取首页类目
     */
    private void getIndexClass() {
        x.http().get(new RequestParams(ApiShop.Classes()), new WWXCallBack("Classes") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<ShopClass> list = JSON.parseArray(
                        jsonArray.toJSONString(), ShopClass.class);
                if (list.size() > 9) {
                    for (int i = 9; i < list.size(); i++) {
                        list.remove(i);
                    }
                }

                ShopClass shopAll = new ShopClass();
                shopAll.ClassName = "全部";
                list.add(shopAll);
                mGridAdapter.setDatas(list);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    /**
     * 获取购物车数量
     */
    public void getCartSum() {
        if (WWApplication.getInstance().isLogin()) {
            int Quantity = SharedPreferenceUtils.getInstance().getCartNum();
            if (Quantity > 0) {
                tvShopcartCount.setVisibility(View.VISIBLE);
                if (Quantity > 99) {
                    tvShopcartCount.setText("99+");
                } else {
                    tvShopcartCount.setText(Quantity + "");
                }
            } else {
                tvShopcartCount.setVisibility(View.GONE);
            }
        }
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
                                case Banner.SELF_BANNER:
                                    ArrayList<String> mImages = new ArrayList<String>();
                                    for (int i = 0; i < list.size(); i++) {
                                        mImages.add(getRightImgScreen(list.get(i).PicUrl));
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
                                case Banner.SELF_AD:
                                    ll_container_imgad.removeAllViews();
                                    for (int i = 0; i < list.size(); i++) {
                                        int width = getActivity().getResources().getDisplayMetrics().widthPixels;
                                        final ImageView imageView = new ImageView(getActivity());
                                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                                                width, width / 3);
                                        imageView.setLayoutParams(layoutParams);
                                        final Banner banner = list.get(i);
                                        ImageUtils.setCommonImage(getActivity(), banner.PicUrl, imageView);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
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
                                        });
                                        ll_container_imgad.addView(imageView);
                                    }
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                    }
                });

    }

    private String getRightImgScreen(String picUrl) {
        return picUrl.replace("__", "_" + width + "x" + pxHeight);
    }

    @Override
    public void onResume() {
        getCartSum();
        super.onResume();
    }

    /**
     * 获取商品
     */
    public void getListGoods() {
        RequestParams requestParams = new RequestParams(ApiShop.ProductHot());
        requestParams.addBodyParameter("pageIndex", mCurrentPage
                + "");
        x.http().get(requestParams, new WWXCallBack("ProductHot") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<ShopProduct> list = (ArrayList<ShopProduct>) JSONArray.parseArray(
                        data.getString("Data"), ShopProduct.class);
                totalCount = data.getIntValue("PageCount");
                if (list != null && list.size() > 0) {
                    if (mCurrentPage > 1) {
                        mAdapter.addDatas(list);
                    } else {
                        mAdapter.setDatas(list);
                    }
                }

            }

            @Override
            public void onAfterFinished() {
                listview.onRefreshComplete();
            }
        });
    }

    @OnClick({R.id.ll_search, R.id.rl_head_right})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_search:
                PublicWay.startSearchActivity(getActivity(), SearchActiivty.SELFSHOP, 1);
                break;
            case R.id.rl_head_right:
                PublicWay.stratSelfSupportShopCartActivity(SelfSupportFragment.this);
                break;
        }
    }
}
