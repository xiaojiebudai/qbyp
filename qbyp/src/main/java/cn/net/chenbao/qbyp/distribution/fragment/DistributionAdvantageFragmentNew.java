package cn.net.chenbao.qbyp.distribution.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.JiFenShopActivity;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiVariable;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.CartSum;
import cn.net.chenbao.qbyp.dialog.WebviewDialog;
import cn.net.chenbao.qbyp.distribution.activity.DistributionShopCartActivity;
import cn.net.chenbao.qbyp.distribution.adapter.AdAdapter;
import cn.net.chenbao.qbyp.distribution.adapter.DistribtutionGoodsItemAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionGood;
import cn.net.chenbao.qbyp.distribution.been.DistributionProtocol;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.DistributionDocumentDialog;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZXJ on 2017/6/17.
 */

public class DistributionAdvantageFragmentNew extends FatherFragment {
    private RecyclerView lvData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DistributionGood> list = new ArrayList<DistributionGood>();
    private DistribtutionGoodsItemAdapter mAdapter;

    TextView tvTitle;
    TextView distribution_shopcar_num;
    private RelativeLayout rlTitle;
    private int width;
    private int bannerHight;
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private ListView lv;
    private AdAdapter adAdapter;
    private boolean hasBanner;
    private int y = 0;

    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_distribution_advantage_new;
    }

    @Override
    protected void initView() {
        View headerView = mInflater.inflate(R.layout.distribution_advantage_header, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rlTitle = (RelativeLayout) mGroup.findViewById(R.id.ll_title);
        tvTitle = (TextView) mGroup.findViewById(R.id.tv_title);
        distribution_shopcar_num = (TextView) mGroup.findViewById(R.id.distribution_shopcar_num);
        mGroup.findViewById(R.id.distribution_shopcar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(getActivity(), LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, true));
                    return;
                }
                DistributionAdvantageFragmentNew.this.startActivity(new Intent(getActivity(), DistributionShopCartActivity.class));
            }
        });
        mGroup.findViewById(R.id.ll_rule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.startWebViewActivity(getActivity(), "分销规则",
                        ApiDistribution.FENXIAO_RULE, WebViewActivity.URL);
            }
        });


        RelativeLayout ll_banner_container = (RelativeLayout) headerView.findViewById(R.id.ll_banner_container);
        adImgs = (HorizontalInnerViewPager) headerView.findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) headerView.findViewById(R.id.indicator);
        width = DensityUtil.getScreenWidth(getActivity());
        bannerHight = width * 340 / 720;
        mbAdapter = new BannerPagerAdapter(getActivity(), bannerHight);
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = bannerHight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);

        lv = (ListView) headerView.findViewById(R.id.lv);
        adAdapter = new AdAdapter(getActivity(), width * 384 / 720);
        lv.setAdapter(adAdapter);


        lvData = (RecyclerView) mGroup.findViewById(R.id.lv_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mGroup.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!hasBanner) {
                    initBanner(Banner.DISTRIBUTION_BANNER);
                }
                initBanner(Banner.DISTRIBUTION_AD_LIST);
                mCurrentPage = 0;
                if (!listRefreshing) {
                    getListGoods();
                }

            }
        });
        // 添加滚动监听。

        lvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                y += dy;
                int titleHeight = DensityUtil.dip2px(getActivity(), 44);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    titleHeight = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
                }
                if (y <= 0) {   //设置标题的背景颜色
                    rlTitle.setBackgroundColor(Color.argb((int) 0, 255,140,27));//cyk 230, 162, 43
                    tvTitle.setTextColor(getResources().getColor(R.color.transparent));
                } else if (y > 0 && y <= titleHeight) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / titleHeight;
                    int alpha = (int) (255 * scale);
                    rlTitle.setBackgroundColor(Color.argb((int) alpha, 255,140,27));
                    tvTitle.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                } else {    //滑动到banner下面设置普通颜色
                    rlTitle.setBackgroundColor(Color.argb((int) 255, 255,140,27));
                    tvTitle.setTextColor(getResources().getColor(R.color.white));
                }
                if (!recyclerView.canScrollVertically(1)) {// 手指不能向上滑动了
                    // TODO 这里有个注意的地方，如果你刚进来时没有数据，但是设置了适配器，这个时候就会触发加载更多，需要开发者判断下是否有数据，如果有数据才去加载更多。
                    if (mCurrentPage >= totalCount) {
//                        WWToast.showShort(R.string.nomore_data);
                    } else {
                        if (!listRefreshing) {
                            getListGoods();
                        }

                    }
                }
            }
        });
        int widthImg = (DensityUtil.getScreenWidth(getActivity()) - DensityUtil.dip2px(getActivity(), 20)) / 2;
        mAdapter = new DistribtutionGoodsItemAdapter(list, widthImg);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                final DistributionGood shopProduct = (DistributionGood) mAdapter.getData().get(position);
                PublicWay.startDistributionGoodsDetailActivity(getActivity(), shopProduct.FenXiao.ProductId);
            }
        });

        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        lvData.setItemAnimator(new DefaultItemAnimator());
//        mAdapter.openLoadAnimation(false);

        mAdapter.addHeaderView(headerView);
        lvData.setAdapter(mAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AdLink(adAdapter.getItem(i));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams paramsTitle = (RelativeLayout.LayoutParams) rlTitle.getLayoutParams();
            paramsTitle.height = DensityUtil.dip2px(getActivity(), 44) + WWViewUtil.getStatusBarHeight(getActivity());
            rlTitle.setLayoutParams(paramsTitle);
            rlTitle.setPadding(0, WWViewUtil.getStatusBarHeight(getActivity()), 0, 0);
            rlTitle.setBackgroundColor(Color.argb((int) 0, 255,140,27));
        }

        initBanner(Banner.DISTRIBUTION_BANNER);
        initBanner(Banner.DISTRIBUTION_AD_LIST);
        getListGoods();

        getHavaFenxiaoProtocol();

    }

    //获取是否有协议
    private void getHavaFenxiaoProtocol() {
        if (WWApplication.getInstance().isLogin()) {
            RequestParams requestParams = ParamsUtils.getSessionParams(ApiDistribution.HaveFenxiaoProtocol());
            x.http().get(requestParams, new WWXCallBack("HaveFenxiaoProtocol") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    if (!TextUtils.isEmpty(data.getString("Data"))) {
                        final DistributionProtocol distributionProtocol = JSON.parseObject(data.getString("Data"), DistributionProtocol.class);
                        final DistributionDocumentDialog dialog = new DistributionDocumentDialog(getActivity());
                        dialog.setContentText(distributionProtocol.Title);
                        dialog.setDocumentText("《" + distributionProtocol.Name + "》");
                        dialog.setCheck(true);
                        dialog.setDocumentCilck(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TimeUtil.isFastClick()) {
                                    if (dialogWeb != null) {
                                        dialogWeb.show();
                                    } else {
                                        openProtocol(distributionProtocol.LevelId);
                                    }

                                }
                            }
                        });
                        dialog.setRightButtonCilck(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog.getIsCheck()) {
                                    ConfirmFenxiaoProtocol(distributionProtocol.OrderId);
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }


                }

                @Override
                public void onAfterFinished() {
                    if (mCurrentPage < 2)
                        mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    private void ConfirmFenxiaoProtocol(long orderID) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("orderId", orderID);

        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiDistribution.ConfirmFenxiaoProtocol()), new WWXCallBack("ConfirmFenxiaoProtocol") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                WWToast.showShort("协议签订成功");
            }

            @Override
            public void onAfterFinished() {

            }
        });


    }

    private WebviewDialog dialogWeb;

    private void openProtocol(int UpLevelId) {
        RequestParams requestParams = new RequestParams(ApiVariable.FenxiaoAgentProcotol());
        requestParams.addBodyParameter("levelId", UpLevelId + "");      //TODO  levelId
        x.http().get(requestParams, new WWXCallBack("FenxiaoAgentProcotol") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                dialogWeb = new WebviewDialog(getActivity(), WebviewDialog.DATA, data.getString("Data"));
                dialogWeb.show();

            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private boolean listRefreshing;

    /**
     * 获取商品
     */
    public void getListGoods() {

        listRefreshing = true;
        RequestParams requestParams = new RequestParams(ApiDistribution.Products());
        requestParams.addBodyParameter("pageIndex", mCurrentPage
                + "");
        requestParams.addBodyParameter("pageSize", 10
                + "");
        x.http().get(requestParams, new WWXCallBack("Products") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<DistributionGood> listNow = (ArrayList<DistributionGood>) JSONArray.parseArray(
                        data.getString("Data"), DistributionGood.class);
                totalCount = data.getIntValue("PageCount");
                if (listNow != null && listNow.size() > 0) {
                    if (mCurrentPage > 1) {
                        mAdapter.addData(listNow);
                    } else {
                        mAdapter.setNewData(listNow);
                    }
                }

            }

            @Override
            public void onAfterFinished() {
                listRefreshing = false;
                if (mCurrentPage < 2)
                    mSwipeRefreshLayout.setRefreshing(false);
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
                                case Banner.DISTRIBUTION_BANNER:
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
                                case Banner.DISTRIBUTION_AD_LIST:
                                    adAdapter.setDatas(list);
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onAfterFinished() {
                    }
                });
    }

    private String getRightImgScreen(String picUrl, int width, int height) {
        return picUrl.replace("__", "_" + width + "x" + height);
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


    public void onResume() {
        super.onResume();
        getCarNum();
    }

    private void getCarNum() {
        if (WWApplication.getInstance().isLogin()) {
            x.http().get(ParamsUtils.getSessionParams(ApiDistribution.CartSumGet()), new WWXCallBack("CartSumGet") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    CartSum shopCar = JSONObject.parseObject(data.getString("Data"), CartSum.class);
                        distribution_shopcar_num.setVisibility(View.VISIBLE);
                        if (shopCar.Quantity > 99) {
                            distribution_shopcar_num.setText("99+");
                        } else {
                            distribution_shopcar_num.setText(shopCar.Quantity + "");
                        }
                }

                @Override
                public void onAfterFinished() {

                }
            });

        }
    }
}
