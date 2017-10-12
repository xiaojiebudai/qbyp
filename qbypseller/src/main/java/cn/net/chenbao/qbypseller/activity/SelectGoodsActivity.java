package cn.net.chenbao.qbypseller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.GoodsSelectAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxj on 2017/4/14.
 *
 * @description
 */

public class SelectGoodsActivity extends FatherActivity {
    @BindView(R.id.tv_upload_goods)
    TextView tvUploadGoods;
    @BindView(R.id.listview_goods)
    PullToRefreshListView listviewGoods;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.tv_money_total)
    TextView tv_money_total;
    private GoodsSelectAdapter adapter;
    private ArrayList<Goods> goodsSelected = new ArrayList<Goods>();

    @Override
    protected int getLayoutId() {
        return R.layout.act_select_goods;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.select_goods, true);
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Consts.KEY_DATA))) {
            goodsSelected = (ArrayList<Goods>) JSON.parseArray(getIntent().getStringExtra(Consts.KEY_DATA),
                    Goods.class);
            setPriceAndNum();
        }


    }

    /**
     *
     */
    private void setPriceAndNum() {
        int num = 0;
        double price = 0;
        for (int i = 0; i < goodsSelected.size(); i++) {
            num += goodsSelected.get(i).Quantity;
            price += goodsSelected.get(i).Quantity * goodsSelected.get(i).Price;
        }
        tv_total.setText(num + "");
        tv_money_total.setText(WWViewUtil.numberFormatPrice(price));
    }

    @Override
    protected void initView() {


        WWViewUtil.setEmptyView(listviewGoods
                .getRefreshableView(), R.layout.emptyview_imgbg);
        listviewGoods
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        mPageIndex = 0;
                        requestGoodsData();
                    }
                });
        listviewGoods
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (HaveNextPage) {
                            requestGoodsData();
                        } else {
                            listviewGoods.onRefreshComplete();
                            WWToast.showShort(R.string.nomore_data);
                        }
                    }
                });
        adapter = new GoodsSelectAdapter(this);
        adapter.setPriceListener(new GoodsSelectAdapter.PriceCallBack() {


            @Override
            public void refreshGoodsListener(Goods goods) {
                for (int i = 0; i < goodsSelected.size(); i++) {
                    if (goodsSelected.get(i).GoodsId == goods.GoodsId) {
                        goodsSelected.remove(i);
                        break;
                    }
                }
                if (goods.Quantity > 0) {
                    goodsSelected.add(goods);
                }
                setPriceAndNum();

            }
        });
        listviewGoods.setAdapter(adapter);

    }

    private int mPageIndex;
    private boolean HaveNextPage;

    /**
     * 请求商品数据
     *
     * @description
     */
    private void requestGoodsData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.goodsGet());
        params.addQueryStringParameter("status", Goods.STATE_ONSALE + "");
        params.addQueryStringParameter(Api.KEY_PAGE_INDEX, mPageIndex + "");
        params.addQueryStringParameter(Api.KEY_PAGE_SIZE, Api.DEFAULT_PAGE_SIZE);
        params.addQueryStringParameter(Api.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("GoodsGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mPageIndex++;
                HaveNextPage = data.getBoolean("HaveNextPage");
                List<Goods> list = JSON.parseArray(
                        data.getJSONArray(Api.KEY_DATA).toJSONString(),
                        Goods.class);
                //重新赋值数量
                for (int i = 0; i < goodsSelected.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (goodsSelected.get(i).GoodsId == list.get(j).GoodsId) {
                            list.get(j).Quantity = goodsSelected.get(i).Quantity;
                        }
                    }
                }
                if (mPageIndex > 1) {
                    adapter.addDatas(list);
                } else {
                    adapter.setDatas(list);
                }
            }

            @Override
            public void onAfterFinished() {
                listviewGoods.onRefreshComplete();
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void doOperate() {
        requestGoodsData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_commit)
    public void onViewClicked() {
        if (goodsSelected != null && goodsSelected.size() > 0) {
            Intent intent = new Intent();
            intent.putExtra(Consts.KEY_DATA, JSONObject.toJSONString(goodsSelected));
            setResult(RESULT_OK, intent);
            finish();
        } else {
            WWToast.showShort(R.string.please_select_goods);
        }

    }
}
