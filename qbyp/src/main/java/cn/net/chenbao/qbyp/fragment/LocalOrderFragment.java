package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.widget.ListView;
import android.widget.RadioGroup;

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
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by 木头 on 2016/11/4.
 */

public class LocalOrderFragment extends FatherFragment {

    public static final int MODE_GO_SHOP = 0;
    public static final int MODE_PEI_SONG = 1;

    public static final int REQUEST_CODE = 10087;
    PullToRefreshListView mListView;
    private OrderAdapter mAdapter;
    private RadioGroup mRb;
    private int cuttertPager = 0;
    private List<Order> orders;
    private int mode = MODE_PEI_SONG;
    // 分页数据
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_local_order;
    }

    public void doRequest() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiTrade.ordersGet());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("sendMode", "-1");
        params.addBodyParameter("pageIndex", cuttertPager + "");
        x.http().get(params, new WWXCallBack("OrdersGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
//                mILoadingView.hideLoading();
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

    @Override
    protected void initView() {
        mListView = (PullToRefreshListView) mGroup
                .findViewById(R.id.listview_orders);
//        setListEmptyView(mListView, new LoadingView.ReloadDataListener() {
//            @Override
//            public void loadData() {
//                doRequest();
//            }
//        });
//        mILoadingView.showLoading();
        ListView listview = mListView.getRefreshableView();
        WWViewUtil.setEmptyView(listview);

        mAdapter = new OrderAdapter(getActivity(), LocalOrderFragment.this);
        mListView.setAdapter(mAdapter);

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
        doRequest();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                cuttertPager = 0;
                doRequest();
            }
        }
    }
}
