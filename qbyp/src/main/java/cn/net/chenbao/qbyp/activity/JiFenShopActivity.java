package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportGoodsItemTwoAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

/**
 * Created by admin on 2017/10/1.
 */

public class JiFenShopActivity extends FatherActivity {
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private SelfSupportGoodsItemTwoAdapter mAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.act_jifenshop;
    }

    @Override
    protected void initValues() {
        initDefautHead("积分商城", true);
        initTextHeadRigthImg(R.drawable.search_w, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.startSearchActivity(JiFenShopActivity.this, SearchActiivty.SELFSHOP, 1);
            }
        });
    }

    @Override
    protected void initView() {

        mAdapter = new SelfSupportGoodsItemTwoAdapter(this);

        listview.setAdapter(mAdapter);
        WWViewUtil.setEmptyView(listview.getRefreshableView());
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
            }
        });
        getListGoods();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //排序方式(time,a加入时间升序,price,d价格除序，sale销售数量默认降序)，选填，
                switch (  tab.getPosition()){
                    case  0:
                        sortCol="";
                        break;
                    case  1:
                        sortCol="time,a";
                        break;
                    case  2:
                        sortCol="price,d";
                        break;
                    case  3:
                        sortCol="sale";
                        break;
                }
                mCurrentPage = 0;
                getListGoods();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 获取商品
     */
    private String sortCol="";
    public void getListGoods() {
        RequestParams requestParams = new RequestParams(ApiShop.ProductVip());
        requestParams.addBodyParameter("pageIndex", mCurrentPage
                + "");
        if(!TextUtils.isEmpty(sortCol)){
            requestParams.addBodyParameter("sortCol",sortCol);
        }
        x.http().get(requestParams, new WWXCallBack("ProductVip") {
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

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
