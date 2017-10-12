package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase.OnScrollChangeListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.view.ViewHelper;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BussinessAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.Location;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.utils.BaiDuLocationUtils;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.TabScrollView;
import cn.net.chenbao.qbyp.view.TitlePopup;
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
public class LocalLifeActivity extends FatherActivity implements
        OnClickListener, TabScrollView.OnItemSelectListener {

    public static final int REQUEST_LOCTION = 10086;
    private TextView tv_head_center;
    private List<TradesCategory> tradesCategoryList = new ArrayList<TradesCategory>();
    private BaiDuLocationUtils instance;
    private List<View> list = new ArrayList<View>();
    private LinearLayout llOperation;
    private LinearLayout llTagContainer;
    private RadioGroup rbTabs;
    private int currentTab;
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private View banner;
    private int bannerHight;
    private PullToRefreshListView mListView;
    private BussinessAdapter mAdapter;
    private View scrollview;
    private boolean isJump = true;
    private int jumpY;
    private int mScrollState;
    private int screenWidth;
    private boolean isRefresh;
    private Location location;
    private boolean isLocation = true;
    private TitlePopup popup;
    private RelativeLayout ll_banner_container;


    @Override
    protected int getLayoutId() {
        return R.layout.act_local_life;
    }

    @Override
    protected void initValues() {

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        bannerHight = screenWidth * 165 / 720;
        banner = View.inflate(this, R.layout.b0, null);
        adImgs = (HorizontalInnerViewPager) banner.findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) banner.findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(this, bannerHight);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        rbTabs = (RadioGroup) findViewById(R.id.tabs);
        llOperation = (LinearLayout) findViewById(R.id.ll_level_operation);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llOperation.getLayoutParams();
        params.setMargins(0, bannerHight, 0, 0);
        llOperation.setLayoutParams(params);
        ll_banner_container = (RelativeLayout) banner.findViewById(R.id.ll_banner_container);
        findViewById(R.id.rl_head_order).setOnClickListener(this);
        findViewById(R.id.ll_loacte).setOnClickListener(this);
        findViewById(R.id.iv_head_back).setOnClickListener(this);
        findViewById(R.id.rl_head_common_menu).setOnClickListener(this);
        // mGroup.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速,为了显示虚线
        scrollview = findViewById(R.id.scrollview);
        tv_head_center = (TextView) findViewById(R.id.tv_head_center);
        mListView.getRefreshableView().addHeaderView(banner);
        WWViewUtil.setEmptyView(mListView.getRefreshableView());
        mAdapter = new BussinessAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                mScrollState = scrollState;

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (mScrollState == 0) {// 下拉刷新回滚过程中会回调一次
                    return;
                }
                int top = banner.getTop();
                int scrollY = top;
                if (firstVisibleItem > 1 || scrollY <= -bannerHight) {
                    scrollY = -bannerHight;
                }
                ViewHelper.setTranslationY(llOperation, scrollY);
            }
        });
        mListView.setOnScrollChangeListener(new OnScrollChangeListener() {

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                if (!mListView.isRefreshing()) {
                    isJump = true;
                    ViewHelper.setTranslationY(llOperation, -t);
                } else {
                    if (isJump) {
                        isJump = !isJump;
                        jumpY = -oldt;
                    } else {
                        jumpY = jumpY + oldt - t;
                        ZLog.showPost("t" + t + "oldt" + oldt + "jumpY" + jumpY);
                        ViewHelper.setTranslationY(llOperation, jumpY);
                    }
                }
            }
        });
        mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                initBanner();
                if (tradesCategoryList.size() < 1) {
                    initData();
                    return;
                }
                TradesCategory category = tradesCategoryList.get(currentTab);
                category.currentPager = 0;
                getSelller(category);
            }
        });
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
                    Intent intent = new Intent(LocalLifeActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                } else {
                    PublicWay.startStoreActivity(LocalLifeActivity.this,
                            mAdapter.getItem(position - 2).SellerId, 0);
                }
            }
        });
    }

    @Override
    protected void initView() {
        instance = new BaiDuLocationUtils(this);
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
                        isLocation = false;
                        if (tradesCategoryList.size() < 1) {
                            initBanner();
                            initData();
                            return;
                        }
                        TradesCategory category = tradesCategoryList.get(currentTab);
                        category.currentPager = 0;
                        getSelller(category);
                    }
                }
            }
        });
        instance.startLocation();
        mListView.setOnScrollChangeListener(new OnScrollChangeListener() {

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                ViewHelper.setTranslationY(llOperation, -t);
            }
        });
        mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (tradesCategoryList.size() < 1) {
                    initData();
                    return;
                }
                TradesCategory category = tradesCategoryList.get(currentTab);
                getSelller(category);
            }
        });
        findViewById(R.id.tv_more).setOnClickListener(// 更多
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (location != null) {
                            PublicWay.startCategoryActivity(LocalLifeActivity.this,
                                    location.latitudes, location.Longitudes);
                        }
                    }
                });
        llTagContainer = (LinearLayout) findViewById(R.id.ll_tags_container);
        popup = PublicWay.startCommonFunctionPop(this);
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = bannerHight;
        params.width = screenWidth;
        ll_banner_container.setLayoutParams(params);
    }

    @Override
    protected void doOperate() {
        initBanner();
        initData();
        WWViewUtil.addGuideImage(this, R.drawable.guide, R.id.ll_container);
    }

    /**
     * 从后台取tab
     */
    private void initData() {
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("TradesGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesCategory> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesCategory.class);
                for (int i = 0; i < (list.size() < 4 ? list.size() + 1 : 4); i++) {
                    TradesCategory category = new TradesCategory();
                    if (i == 0) {
                        category.TradeName = getString(R.string.all);
                        category.TradeId = "null";
                    } else {
                        category = list.get(i - 1);
                    }
                    tradesCategoryList.add(category);
                }
                for (int i = 0; i < tradesCategoryList.size(); i++) {
                    final RadioButton childRb = (RadioButton) rbTabs.getChildAt(i);
                    final TradesCategory tradesCategory = tradesCategoryList.get(i);
                    childRb.setText(tradesCategoryList.get(i).TradeName);
                    childRb.setTag(i);
                    childRb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            if (isChecked) {
                                TradesCategory category = tradesCategoryList
                                        .get((Integer) childRb.getTag());
                                category.TradeId = tradesCategoryList
                                        .get((Integer) childRb.getTag()).TradeId;
                                currentTab = (Integer) childRb.getTag();
                                if (category.tradesList != null) {// 不为null内存里拿

                                    mAdapter.setDatas(category.tradesList);
                                    if (currentTab != 0) {
                                        scrollview.setVisibility(View.VISIBLE);
                                        banner.setPadding(0, 0, 0,
                                                DensityUtil.dip2px(LocalLifeActivity.this, 80));
                                        createTab(tradesCategory);
                                    } else {
                                        banner.setPadding(0, 0, 0,
                                                DensityUtil.dip2px(LocalLifeActivity.this, 40));
                                        scrollview.setVisibility(View.GONE);
                                    }
                                    if (mAdapter.getCount() < 6) {
                                        mListView.setRefreshing();
                                    }

                                } else if (category.tradesList == null// 为null第一个tab
                                        && currentTab == 0) {
                                    banner.setPadding(0, 0, 0,
                                            DensityUtil.dip2px(LocalLifeActivity.this, 40));
                                    scrollview.setVisibility(View.GONE);
                                    category.currentPager = 0;
                                    isRefresh = true;
                                    getSelller(category);
                                } else if (category.tradesList == null) {
                                    category.currentPager = 0;
                                    scrollview.setVisibility(View.VISIBLE);
                                    banner.setPadding(0, 0, 0,
                                            DensityUtil.dip2px(LocalLifeActivity.this, 80));
                                    isRefresh = true;
                                    getSelller(category);
                                    ZLog.showPost(category.TradeName);
                                    getTabButton(category);
                                }
                            }
                        }
                    });
                }
                RadioButton childAt = (RadioButton) rbTabs.getChildAt(0);
                childAt.setChecked(true);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void initBanner() {
        RequestParams params = new RequestParams(ApiBaseData.BannersGet());
        params.addBodyParameter("place", Banner.LOCAL_Life_BANNER + "");
        showWaitDialog();
        x.http().get(params,
                new WWXCallBack("BannersGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        List<Banner> list = JSON.parseArray(
                                jsonArray.toJSONString(), Banner.class);
                        if (list != null) {
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
                                    LocalLifeActivity.this, 3));
                            indicator.setViewPager(adImgs);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    private String getRightImgScreen(String picUrl) {
        return picUrl.replace("__", "_" + screenWidth + "x" + bannerHight);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_back:
                finish();
                break;
            case R.id.ll_loacte:
                if (location != null) {
                    PublicWay.startLocateActivity(this, REQUEST_LOCTION, JSONObject.toJSONString(location));
                }
                break;
            case R.id.rl_head_order:
                startActivity(new Intent(this, LocalLifeOrderActivity.class));
                break;
            case R.id.rl_head_common_menu:
                popup.showWindow(tv_head_center);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelect(int index, View itemView) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_LOCTION:// 地理位置
                    location = JSON.parseObject(
                            data.getStringExtra("location"), Location.class);
                    SharedPreferenceUtils.getInstance().saveLocation(JSON.toJSONString(location));
                    tv_head_center.setText(location.name);
                    if (WWApplication.getInstance().isLogin()) {
                        sendAddressRequest(location);
                    }
                    if (tradesCategoryList.size() < 1) {
                        initData();
                        return;
                    }
                    TradesCategory category = tradesCategoryList.get(currentTab);
                    category.currentPager = 0;
                    getSelller(category);
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

    /**
     * 获取商家
     *
     * @param tradesCategory
     */
    public void getSelller(final TradesCategory tradesCategory) {
        if (location == null) {
            mListView.onRefreshComplete();
            return;
        }
        showWaitDialog();
        RequestParams requestParams = new RequestParams(ApiSeller.searchTrade());
        requestParams.addBodyParameter("longitude", location.Longitudes + "");
        requestParams.addBodyParameter("latitude", location.latitudes + "");
        ZLog.showPost("tradesCategory.TradeId--" + tradesCategory.TradeId);
        if (!tradesCategory.TradeId.equals("null")) {// 全部
            requestParams.addBodyParameter("tradeId", tradesCategory.TradeId);
        }
        requestParams.addBodyParameter("pageIndex", tradesCategory.currentPager
                + "");
        x.http().get(requestParams, new WWXCallBack("Search") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                tradesCategory.currentPager++;
                int PageCount = data.getIntValue("PageCount");
                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesMessage> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesMessage.class);
                if (tradesCategory.currentPager == 1) {
                    tradesCategory.tradesList = list;
                    mAdapter.setDatas(tradesCategory.tradesList);

                } else {
                    if (tradesCategory.currentPager > PageCount) {
                        WWToast.showShort(R.string.nomore_data);
                    } else {
                        mAdapter.addDatas(list);
                    }
                }
            }

            @Override
            public void onAfterFinished() {

                mListView.onRefreshComplete();
                dismissWaitDialog();
                if (isRefresh) {
                    isRefresh = false;
                    if (mAdapter.getCount() < 6) {
                        mListView.setRefreshing();
                    }
                }
            }
        });
    }

    /**
     * 小标签
     */
    public void getTabButton(final TradesCategory tradesCategory) {
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        params.addBodyParameter("parentId", tradesCategory.TradeId);
        showWaitDialog();
        x.http().get(params, new WWXCallBack("TradesGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                final ArrayList<TradesCategory> arrayList = new ArrayList<TradesCategory>();
                TradesCategory object = new TradesCategory();
                object.TradeId = tradesCategory.TradeId;
                object.TradeName = getString(R.string.all);
                arrayList.add(object);
                JSONArray parseArray = data.getJSONArray("Data");
                List<TradesCategory> parseArray2 = JSON.parseArray(
                        parseArray.toJSONString(), TradesCategory.class);
                for (TradesCategory tradesCategory : parseArray2) {
                    arrayList.add(tradesCategory);
                }
                tradesCategory.categoryList = arrayList;
                createTab(tradesCategory);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    /**
     * 创建tab方法
     *
     * @param tradesCategory
     */
    private void createTab(final TradesCategory tradesCategory) {
        llTagContainer.removeAllViews();
        if (!list.isEmpty()) {
            list.clear();
        }
        final List<TradesCategory> arrayList = tradesCategory.categoryList;
        for (int i = 0; i < arrayList.size(); i++) {
            final TradesCategory category = arrayList.get(i);
            final LinearLayout view = (LinearLayout) View.inflate(
                    this, R.layout.home_tab_button, null);
            final TextView inflate = (TextView) view.findViewById(R.id.tv);
            if (tradesCategory.TradeId.equals(category.TradeId)) {
                inflate.setSelected(true);
            }
            inflate.setText(category.TradeName);
            inflate.setTag(i);
            inflate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!inflate.isSelected()) {
                        inflate.setSelected(true);
                        tradesCategory.currentPager = 0;
                        tradesCategory.TradeId = arrayList
                                .get((Integer) inflate.getTag()).TradeId;
                        arrayList.get((Integer) inflate.getTag()).currentPager = 0;
                        isRefresh = true;
                        getSelller(arrayList.get((Integer) inflate.getTag()));
                        setTextSelectState(v);
                    }
                }
            });
            list.add(inflate);
            llTagContainer.addView(view);
        }
    }

    public void setTextSelectState(View v) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != v) {
                list.get(i).setSelected(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance.stopLocation();
    }
}
