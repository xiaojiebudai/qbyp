package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
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

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.GalleryAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportGoodsItemTwoAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.TitlePopup;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ppsher on 2016/12/30.
 * Description:幸福专区
 */

public class HappyAreaActivity extends FatherActivity {
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
    @BindView(R.id.rl_common_menu)
    LinearLayout rlCommonMenu;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    //    private LinearLayout ll_recommend, ll_container_imgad, ll_container_floor;
    private GalleryAdapter adapter;
    private SelfSupportGoodsItemTwoAdapter mAdapter;
    private BannerPagerAdapter mbAdapter;
    private TitlePopup popup;
    private RecyclerView mRecyclerview_H;
    private ImageView iv_ad00, iv_ad01, iv_ad02, iv_ad03;
    private RelativeLayout ll_banner_container;
    private int width;
    private int pxHeight;

    @Override
    protected int getLayoutId() {
        return R.layout.act_happy_area;
    }

    @Override
    protected void initValues() {
        width = getWindowManager().getDefaultDisplay().getWidth();
        pxHeight = width * 384 / 720;
    }

    @Override
    protected void initView() {
        View hearder = View.inflate(this, R.layout.header_happy_area, null);
        adImgs = (HorizontalInnerViewPager) hearder.findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) hearder.findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(this, pxHeight);
        ll_banner_container = (RelativeLayout) hearder.findViewById(R.id.ll_banner_container);
        //得到控件
        mRecyclerview_H = (RecyclerView) hearder.findViewById(R.id.recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerview_H.setLayoutManager(linearLayoutManager);
        //设置适配器
        adapter = new GalleryAdapter(this);
        adapter.setOnItemClickListener(new GalleryAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                PublicWay.stratSelfSupportSearchDataActivity(HappyAreaActivity.this, "", ((ShopClass) data).ClassId);
            }
        });
        mRecyclerview_H.setAdapter(adapter);

        iv_ad00 = (ImageView) hearder.findViewById(R.id.iv_ad00);
        iv_ad01 = (ImageView) hearder.findViewById(R.id.iv_ad01);
        iv_ad02 = (ImageView) hearder.findViewById(R.id.iv_ad02);
        iv_ad03 = (ImageView) hearder.findViewById(R.id.iv_ad03);
        listview.getRefreshableView().addHeaderView(hearder);
        WWViewUtil.setEmptyView(listview.getRefreshableView());
        mAdapter = new SelfSupportGoodsItemTwoAdapter(this);
        listview.setAdapter(mAdapter);
        // 设置 end-of-list监听
        listview
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getProductRecommend();
                        }

                    }
                });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 0;
                initBanner(Banner.SELF_BANNER);
                initBanner(Banner.SELF_AD);
                getClasses();
                getProductRecommend();
            }
        });

        popup = PublicWay.startCommonFunctionPop(this);
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = pxHeight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);
    }

    @Override
    protected void doOperate() {
        setHeaderView();
        getCartSum();
        WWViewUtil.addGuideImage(this, R.drawable.guide, R.id.ll_container);
    }

    /**
     * 设置headerview
     */
    private void setHeaderView() {
        initBanner(Banner.HAPPY_AREA_BANNER);
        initBanner(Banner.HAPPY_AREA_AD_LEFT);
        initBanner(Banner.HAPPY_AREA_AD_RIGHT_UP);
        initBanner(Banner.HAPPY_AREA_AD_RIGHT_DOWN);
        getClasses();
        getProductRecommend();
    }

    private void getProductRecommend() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.ProductHot());
        params.addBodyParameter("classId", "10");
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        x.http().get(params, new WWXCallBack("ProductHot") {
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
                dismissWaitDialog();
            }
        });
    }

    /**
     * 获取类目
     */
    private void getClasses() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.Classes());
        params.addBodyParameter("parentId", "10");
        x.http().get(params, new WWXCallBack("Classes") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<ShopClass> shopClassList = JSONObject.parseArray(
                        data.getString("Data"), ShopClass.class);
                adapter.setDatas(shopClassList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
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
        showWaitDialog();
        x.http().get(params,
                new WWXCallBack("BannersGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        List<Banner> list = JSON.parseArray(
                                jsonArray.toJSONString(), Banner.class);
                        if (list != null) {
                            switch (index) {
                                case Banner.HAPPY_AREA_BANNER:
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
                                            HappyAreaActivity.this, 3));
                                    indicator.setViewPager(adImgs);
                                    break;
                                case Banner.HAPPY_AREA_AD_LEFT:
                                    setImgAndLink(list.get(0), iv_ad00);
                                    break;
                                case Banner.HAPPY_AREA_AD_RIGHT_UP:
                                    setImgAndLink(list.get(0), iv_ad01);
                                    break;
                                case Banner.HAPPY_AREA_AD_RIGHT_DOWN:
                                    setImgAndLink(list.get(0), iv_ad02);
                                    setImgAndLink(list.get(1), iv_ad03);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    private String getRightImgScreen(String picUrl) {
        return picUrl.replace("__", "_" + width + "x" + pxHeight);
    }

    /**
     * 设置广告位跳转
     *
     * @param banner
     * @param iv
     */
    private void setImgAndLink(final Banner banner, ImageView iv) {

        ImageUtils.setCommonImage(this, banner.PicUrl, iv);
        iv.setOnClickListener(new View.OnClickListener() {
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
                break;
            case 2:
                //原生界面
                if (banner.OperValue.equals(Banner.PAGECODE_SELF)) {
                    startActivity(new Intent(this, SelfSupportActivity.class));
                }else if(banner.OperValue.equals(Banner.CONSUME_AREA)){
                   startActivity(new Intent(this, JiFenShopActivity.class));
                }
                break;
            case 3:
                //自营商品
                PublicWay.stratSelfSupportGoodsDetailActivity(this, Long.parseLong(banner.OperValue));
                break;
            case 4:
                //本地商家店铺
                if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
                    Intent intent = new Intent(this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    PublicWay.startStoreActivity(this, Long.parseLong(banner.OperValue), 0);
                }
                break;
            case 5:
                //分销产品详情
                PublicWay.startDistributionGoodsDetailActivity(this, Long.parseLong(banner.OperValue));
                break;
        }
    }

    @OnClick({R.id.rl_head_left, R.id.ll_search, R.id.rl_head_right, R.id.rl_common_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head_left:
                finish();
                break;
            case R.id.ll_search:
                PublicWay.startSearchActivity(this, SearchActiivty.SELFSHOP, 1);
                break;
            case R.id.rl_head_right:
                PublicWay.stratSelfSupportShopCartActivity(this);
                break;
            case R.id.rl_common_menu:
                popup.showWindow(rlCommonMenu);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
