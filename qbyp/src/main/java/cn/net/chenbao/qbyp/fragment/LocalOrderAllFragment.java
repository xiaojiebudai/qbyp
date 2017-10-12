package cn.net.chenbao.qbyp.fragment;

import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by zxj on 2017/3/14.
 *
 * @description
 */

public class LocalOrderAllFragment extends FatherFragment {

    public static final int REQUEST_CODE = 10087;
    PullToRefreshListView mListView;
    private OrderAdapter mAdapter;

    private int cuttertPager = 0;
    private List<Order> orders;
    // 分页数据
    private int totalCount = 0;
    private int status;
    private View fake_status_bar;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_local_order;
    }

    @Override
    protected void initView() {
        TextView center = (TextView) mGroup.findViewById(R.id.tv_head_center);
        center.setText("订单");
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        mListView = (PullToRefreshListView) mGroup.findViewById(R.id.listview_orders);
        WWViewUtil.setEmptyView(mListView.getRefreshableView());

        mAdapter = new OrderAdapter(getActivity(), LocalOrderAllFragment.this);
        mListView.setAdapter(mAdapter);

        status = -1;
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
        fake_status_bar = mGroup.findViewById(R.id.fake_status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fake_status_bar.getLayoutParams();
            params1.height = WWViewUtil.getStatusBarHeight(getActivity());
            fake_status_bar.setLayoutParams(params1);
            fake_status_bar.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (WWApplication.getInstance().isLogin()) {
            cuttertPager = 0;
            doRequest();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            if (WWApplication.getInstance().isLogin()) {
                cuttertPager = 0;
                doRequest();
            }
        }
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
}
