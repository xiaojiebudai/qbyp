package cn.net.chenbao.qbyp.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.GoodsAdaper;
import cn.net.chenbao.qbyp.adapter.listview.GoodsCategoryAdapter;
import cn.net.chenbao.qbyp.adapter.listview.GoodsAdaper.PriceCallBack;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.bean.GoodsKind;
import cn.net.chenbao.qbyp.dialog.GoodsDialog;
import cn.net.chenbao.qbyp.dialog.ShopCarDialog;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.JsonUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;

/**
 * 商品模块
 *
 * @author xl
 * @date 2016-7-27 下午9:38:19
 * @description
 */
public class GoodsFragment extends FatherFragment implements OnClickListener,
        OnItemClickListener {

    public static final int REQUEST_CODE = 10086;
    private ListView mListView;
    private PullToRefreshListView mPullToRefreshListView;

    private GoodsCategoryAdapter mGoodsCategoryAdapter;
    private GoodsAdaper mGoodsAdaper;

    private GoodsDialog mGoodsDialog;
    private long sellerId;
    private int classId;

    private int currentPager;
    private TextView money;

    private double sumPrice;
    private int buyNum;
    private TextView mBuyNum;
    private ShopCarDialog mShopCarDialog;
    private String json;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods;
    }

    @Override
    protected void initView() {
        sellerId = getArguments().getLong(Consts.KEY_DATA);
        json = getArguments().getString("json");
        doRequest();
        mListView = (ListView) mGroup.findViewById(R.id.listview_categories);
        money = (TextView) mGroup.findViewById(R.id.tv_money);
        mListView.setOnItemClickListener(this);
        // 模拟点击第一个
        mPullToRefreshListView = (PullToRefreshListView) mGroup
                .findViewById(R.id.listview_datas);
        ListView listView = mPullToRefreshListView.getRefreshableView();
        WWViewUtil.setEmptyView(listView);
        mBuyNum = (TextView) mGroup.findViewById(R.id.tv_buynum);
        mGoodsAdaper = new GoodsAdaper(getActivity(), GoodsAdaper.MODE_GOODS);
        mGoodsCategoryAdapter = new GoodsCategoryAdapter(getActivity());
        mListView.setAdapter(mGoodsCategoryAdapter);
        mGroup.findViewById(R.id.tv_pay).setOnClickListener(this);
        mGoodsAdaper.setPriceListener(new PriceCallBack() {

            @Override
            public void subGoodsListener(double price) {// 减
                sumPrice = Arith.sub(sumPrice, price);
                buyNum--;
                if (buyNum <= 0) {
                    mBuyNum.setVisibility(View.GONE);
                } else {
                    mBuyNum.setText(buyNum + "");
                }
                money.setText(WWViewUtil.numberFormatPrice(sumPrice));
            }

            @Override
            public void addGoodsListener(double price) {// 加
                sumPrice = Arith.add(sumPrice, price);
                buyNum++;
                if (buyNum <= 99) {
                    if (mBuyNum.getVisibility() == View.GONE) {
                        mBuyNum.setVisibility(View.VISIBLE);
                    }
                    mBuyNum.setText(buyNum + "");
                }
                money.setText(WWViewUtil.numberFormatPrice(sumPrice));
            }
        });
        mPullToRefreshListView.setAdapter(mGoodsAdaper);
        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if(!TimeUtil.isFastClick()){
                            Goods item = mGoodsAdaper.getItem(position - 1);
                            mGoodsDialog = new GoodsDialog(getActivity(), item);
                            mGoodsDialog
                                    .setOnDismissListener(new OnDismissListener() {

                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                            buyNum = 0;
                                            sumPrice = 0.0;
                                            currentPager = 0;
                                            getGoodsMessage(classId);
                                            getShopCarInfo();
                                        }
                                    });
                            mGoodsDialog
                                    .setNumAndPriceListener(new GoodsDialog.NumAndPriceCallBack() {

                                        @Override
                                        public void subListener(double price) {
                                            buyNum--;
                                            if (buyNum == 0) {
                                                mBuyNum.setVisibility(View.GONE);
                                            } else {
                                                mBuyNum.setText(buyNum + "");
                                            }
                                            sumPrice = Arith.sub(sumPrice, price);
                                            if (sumPrice == 0) {
                                                money.setText("¥0.00");
                                            } else {
                                                money.setText(WWViewUtil.numberFormatPrice(sumPrice));
                                            }

                                        }

                                        @Override
                                        public void addListener(double price) {
                                            buyNum++;
                                            if (mBuyNum.getVisibility() == View.GONE) {
                                                mBuyNum.setVisibility(View.VISIBLE);
                                            }
                                            mBuyNum.setText(buyNum + "");
                                            sumPrice = Arith.add(sumPrice, price);
                                            money.setText(WWViewUtil.numberFormatPrice(sumPrice));
                                        }
                                    });
                            mGoodsDialog.show();
                        }

                    }
                });
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if(!isRequestGoods){
                            currentPager = 0;
                            getGoodsMessage(classId);
                        }else{
                            mPullToRefreshListView.onRefreshComplete();
                        }
                    }
                });
        mPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        if(HaveNextPage){
                            if(!isRequestGoods){
                                getGoodsMessage(classId);
                            }else{
                                mPullToRefreshListView.onRefreshComplete();
                            }
                        }else{
                            WWToast.showShort(R.string.nomore_data);
                            mPullToRefreshListView.onRefreshComplete();
                        }

                    }
                });
        mGroup.findViewById(R.id.rl_shop_car).setOnClickListener(this);
    }

    private void doRequest() {
        getCategroyGoods();
        getShopCarInfo();
    }

    /**
     * 获得购物车信息
     */
    private void getShopCarInfo() {
        RequestParams params = new RequestParams(ApiTrade.shopCarGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("sellerId", sellerId + "");
        x.http().get(params, new WWXCallBack("CartGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                double doubleValue = data.getDoubleValue("TotalAmount");
                double round = Arith.round(doubleValue, 2);
                if (round != 0) {
                    sumPrice = round;
                    money.setText(WWViewUtil.numberFormatPrice(sumPrice));
                } else {
                    money.setText("¥0.00");
                }
                JSONArray jsonArray = data.getJSONArray("Data");
                List<Goods> list = JSON.parseArray(jsonArray.toJSONString(),
                        Goods.class);
                if (list != null && list.size() > 0) {
                    for (Goods shopCar : list) {
                        if (shopCar.Quantity != 0) {
                            buyNum += shopCar.Quantity;
                        }
                    }
                    if (buyNum != 0) {
                        if (mBuyNum.getVisibility() == View.GONE) {
                            mBuyNum.setVisibility(View.VISIBLE);
                        }
                        mBuyNum.setText(buyNum + "");
                    }
                } else {
                    mBuyNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAfterFinished() {

            }
        });

    }

    /**
     * 获得分组
     */
    private void getCategroyGoods() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.getSellerClass());
        params.addBodyParameter("sellerId", sellerId + "");
        x.http().get(params, new WWXCallBack("ClassGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<GoodsKind> parseArray = JSON.parseArray(
                        JsonUtils.parserJsonDataArray(data), GoodsKind.class);
                mGoodsCategoryAdapter.setDatas(parseArray);
                if (mGoodsCategoryAdapter.getDatas().size() > 0) {
                    mListView.performItemClick(null, 0, 0);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_shop_car:
                mShopCarDialog = new ShopCarDialog(getActivity(), sellerId,
                        mGoodsAdaper.getDatas(), json);
                mShopCarDialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        currentPager = 0;
                        getGoodsMessage(classId);
                        // int buyNum2 = mShopCarDialog.getBuyNum();
                        // buyNum = buyNum2;
                        // sumPrice = mShopCarDialog.getSumPrice();
                        // if (buyNum2 == 0) {
                        // mBuyNum.setVisibility(View.GONE);
                        // } else {
                        // mBuyNum.setText(buyNum + "");
                        // }
                        // money.setText("¥" + sumPrice + "");
                        buyNum = 0;
                        sumPrice = 0;
                        getShopCarInfo();
                    }
                });
                mShopCarDialog.setClearListener(new ShopCarDialog.ClearCallBack() {

                    @Override
                    public void clearListener() {
                        List<Goods> datas = mGoodsAdaper.getDatas();
                        for (Goods goods : datas) {
                            goods.CartNum = 0;
                        }
                        mGoodsAdaper.setDatas(datas);
                    }
                });
                mShopCarDialog.setNumListener(new ShopCarDialog.NumCallBack() {

                    @Override
                    public void numListener(Goods goods) {
                        for (int i = 0; i < mGoodsAdaper.getDatas().size(); i++) {
                            if (mGoodsAdaper.getDatas().get(i).GoodsId == goods.GoodsId) {
                                mGoodsAdaper.getDatas().remove(i);
                                mGoodsAdaper.getDatas().add(i, goods);
                            }
                        }
                        mGoodsAdaper.setDatas(mGoodsAdaper.getDatas());
                    }
                });
                if (buyNum > 0) {
                    mShopCarDialog.show();
                }
                break;

            case R.id.tv_pay:// 结算
                if (buyNum != 0) {
                    getOrderPreview();

                } else {
                    WWToast.showShort(R.string.please_add_you_want_good);
                }
                break;
        }
    }

    /**
     * 主要获取商品
     */
    private void getOrderPreview() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiTrade.getOrderDetail());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("sellerId", sellerId + "");
        x.http().get(params, new WWXCallBack("OrderPreview") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startMakeSureOrderActivity(getActivity(), sellerId,
                        REQUEST_CODE, json, JSONObject.toJSONString(data));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    public static GoodsFragment newInstance(long sellerId, String json) {
        GoodsFragment goodsFragment = new GoodsFragment();
        Bundle args = new Bundle();
        args.putLong(Consts.KEY_DATA, sellerId);
        args.putString("json", json);
        goodsFragment.setArguments(args);
        return goodsFragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (classId != mGoodsCategoryAdapter.getItem(position).ClassId) {
            classId = mGoodsCategoryAdapter.getItem(position).ClassId;

            currentPager = 0;
            getGoodsMessage(mGoodsCategoryAdapter.getItem(position).ClassId);
        }

    }
private boolean isRequestGoods;
    /**
     * 请求具体的商品
     *
     * @param classId
     */
    private boolean HaveNextPage;
    public void getGoodsMessage(int classId) {
        showWaitDialog();
        isRequestGoods=true;
        RequestParams params = new RequestParams(ApiSeller.getGoods());
        params.addBodyParameter("classId", classId + "");
        params.addBodyParameter("pageIndex", currentPager + "");
        params.addBodyParameter("sellerId", sellerId + "");// 新加sellerId
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("GoodsGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                currentPager++;
                HaveNextPage=data.getBoolean("HaveNextPage");
                JSONArray jsonArray = data.getJSONArray("Data");
                List<Goods> list = JSON.parseArray(jsonArray.toJSONString(),
                        Goods.class);
                List<Goods> newList = new ArrayList<Goods>();
                for (Goods goods : list) {
                    if (goods.Status == 0) {
                        newList.add(goods);
                    }
                }
                if (currentPager == 1) {
                    mGoodsAdaper.setDatas(newList);
                } else {
                        mGoodsAdaper.addDatas(newList);
                }
            }

            @Override
            public void onAfterFinished() {
                isRequestGoods=false;
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

}
