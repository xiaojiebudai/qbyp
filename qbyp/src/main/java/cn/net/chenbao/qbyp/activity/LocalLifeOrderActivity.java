package cn.net.chenbao.qbyp.activity;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.OrderAdapter;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by ppsher on 2016/12/27.
 */

public class LocalLifeOrderActivity extends FatherActivity {


    public static final int REQUEST_CODE = 10087;
    PullToRefreshListView mListView;
    private OrderAdapter mAdapter;

    private int cuttertPager = 0;
    private List<Order> orders;
    // 分页数据
    private int totalCount = 0;
    private int status;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_local_order;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order, true);
        mListView = (PullToRefreshListView) findViewById(R.id.listview_orders);
        WWViewUtil.setEmptyView(mListView.getRefreshableView());
        mAdapter = new OrderAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void initView() {
        status = getIntent().getIntExtra(Consts.KEY_DATA, -1);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                cuttertPager = 0;
                doRequest();
            }
        });
        // 设置 end-of-list监听
        mListView
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (cuttertPager >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            doRequest();
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cuttertPager = 0;
        doRequest();
    }


    @Override
    protected void doOperate() {

    }

    public void doRequest() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiTrade.ordersGet());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("sendMode", "-1");
        if (status != -1) {
            params.addBodyParameter("status", "" + status);
        }
        params.addBodyParameter("pageIndex", cuttertPager + "");
        x.http().get(params, new WWXCallBack("OrdersGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                cuttertPager++;
                JSONArray jsonArray = data.getJSONArray("Data");
                orders = JSON.parseArray(jsonArray.toJSONString(), Order.class);
                totalCount = data.getIntValue("PageCount");
                if (cuttertPager > 1) {
                    mAdapter.addDatas(orders);
                } else {
                    mAdapter.setDatas(orders);
                }


            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mListView.onRefreshComplete();
            }
        });
    }

    public int getCuttertPager() {
        return cuttertPager;
    }

    public void setCuttertPager(int cuttertPager) {
        this.cuttertPager = cuttertPager;
    }
}
