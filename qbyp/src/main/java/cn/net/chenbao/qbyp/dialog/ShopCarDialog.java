package cn.net.chenbao.qbyp.dialog;

import java.util.List;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.ShopCarAdapter;
import cn.net.chenbao.qbyp.adapter.listview.ShopCarAdapter.AddAndSubCallBack;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 购物车弹窗
 *
 * @author xl
 * @date 2016-7-28 上午12:11:54
 * @description
 */
public class ShopCarDialog extends Dialog implements
        android.view.View.OnClickListener, AddAndSubCallBack {

    private TextView mMoney;
    private TextView mCount;

    private Context context;
    private long sellerId;
    private ShopCarAdapter mAdapter;
    private ListView mListView;
    private double sumPrice;
    private int buyNum;
    private List<Goods> list;
    private NumCallBack numListener;
    private ClearCallBack clearListener;
    private String json;

    public ClearCallBack getClearListener() {
        return clearListener;
    }

    public void setClearListener(ClearCallBack clearListener) {
        this.clearListener = clearListener;
    }

    public NumCallBack getNumListener() {
        return numListener;
    }

    public void setNumListener(NumCallBack numListener) {
        this.numListener = numListener;
    }

    public double getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(double sumPrice) {
        this.sumPrice = sumPrice;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public ShopCarDialog(Context context, long sellerId, List<Goods> list,
                         String json) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_shop_car);
        this.sellerId = sellerId;
        this.context = context;
        this.list = list;
        this.json = json;
        doRequest();
        LayoutParams params = getWindow().getAttributes();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels;
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        initView();
    }

    private void doRequest() {
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
                    mMoney.setText(WWViewUtil.numberFormatPrice(sumPrice));
                }
                JSONArray jsonArray = data.getJSONArray("Data");
                List<Goods> list = JSON.parseArray(jsonArray.toJSONString(),
                        Goods.class);
                for (Goods shopCar : list) {
                    if (shopCar.Quantity != 0) {
                        buyNum += shopCar.Quantity;
                    }
                }
                if (buyNum != 0) {
                    if (mCount.getVisibility() == View.GONE) {
                        mCount.setVisibility(View.VISIBLE);
                    }
                    mCount.setText(buyNum + "");
                }
                mAdapter.setDatas(list);
            }

            @Override
            public void onAfterFinished() {

            }
        });

    }

    private void initView() {
        mAdapter = new ShopCarAdapter(context);
        mAdapter.setGoodsPriceListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        mMoney = (TextView) findViewById(R.id.tv_money);
        mCount = (TextView) findViewById(R.id.tv_count);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        findViewById(R.id.tv_pay).setOnClickListener(this);
        findViewById(R.id.ll_clear).setOnClickListener(this);
        findViewById(R.id.rl_shop_car).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_pay:// 结算
                getOrderPreview();
                break;
            case R.id.ll_clear:// 清空
                doClear();
                break;

            case R.id.iv_back:
                dismiss();
            case R.id.rl_shop_car:
                dismiss();
                break;
        }

    }
    /**
     * 主要获取商品
     */
    private void getOrderPreview() {

        RequestParams params = new RequestParams(ApiTrade.getOrderDetail());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("sellerId", sellerId + "");
        x.http().get(params, new WWXCallBack("OrderPreview") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startMakeSureOrderActivity((Activity) context, sellerId,
                        0, json,JSONObject.toJSONString(data));
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    /**
     * 清空购物车
     */
    private void doClear() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("sellerId", sellerId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiTrade.clearShopCar()), new WWXCallBack("CartClear") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        mAdapter.getDatas().clear();
                        mAdapter.notifyDataSetChanged();
                        buyNum = 0;
                        sumPrice = 0.0;
                        mMoney.setText(WWViewUtil.numberFormatPrice(sumPrice));
                        mCount.setVisibility(View.GONE);
                        mCount.setText(buyNum + "");
                        clearListener.clearListener();
                        dismiss();
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });

    }

    @Override
    public void addGoodsListener(double price, Goods goods) {
        buyNum++;
        sumPrice = Arith.add(sumPrice, price);
        if (buyNum <= 99) {
            if (mCount.getVisibility() == View.GONE) {
                mCount.setVisibility(View.VISIBLE);
            }
            mCount.setText(buyNum + "");
        }
        mMoney.setText(WWViewUtil.numberFormatPrice(sumPrice));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).GoodsId == goods.GoodsId) {
                goods.CartNum = goods.Quantity;// 字段不同
                numListener.numListener(goods);
            }
        }
    }

    @Override
    public void subGoodsListener(double price, Goods goods) {
        buyNum--;
        sumPrice = Arith.sub(sumPrice, price);
        if (buyNum <= 0) {
            mCount.setVisibility(View.GONE);
        } else {
            mCount.setText(buyNum + "");
        }
        mMoney.setText(WWViewUtil.numberFormatPrice(sumPrice));
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).GoodsId == goods.GoodsId) {
                goods.CartNum = goods.Quantity;// 字段不同
                numListener.numListener(goods);
            }
        }
        if (buyNum <= 0) {
            dismiss();
        }
    }

    public interface NumCallBack {
        void numListener(Goods goods);
    }

    public interface ClearCallBack {
        void clearListener();

    }
}