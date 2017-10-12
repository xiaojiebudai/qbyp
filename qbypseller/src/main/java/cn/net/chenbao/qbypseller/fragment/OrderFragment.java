package cn.net.chenbao.qbypseller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.adapter.listview.OrderAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiTrade;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.eventbus.OrderEvent;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.JsonUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.common.Callback.Cancelable;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 订单模块
 *
 * @author xl
 * @date 2016-7-30 下午5:14:01
 * @description
 */
public class OrderFragment extends FatherFragment {

    public static final String KEY_STATE = "state";
    public static final String KEY_DATE = "date";
    public static final String KEY_DO_REFRESH = "do_refresh";

    private OrderAdapter mAdapter;
    private int mState;

    private String date;
    private boolean withTime;

    /**
     * 省流量模式,通过首界面直接切换到对应的模块,预加载模的fragment不拉数据
     */
    private boolean doRefresh;
    private int mCurrentPage = 0;
    private int totalCount = 0;

    public static final int REQUEST_CODE_DETAILS = 10;

    /**
     * @param state 订单状态
     * @return
     */
    public static OrderFragment getInstance(int state, String date,
                                            int initState) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_STATE, state);
        bundle.putString(KEY_DATE, date);
        bundle.putBoolean(KEY_DO_REFRESH, state == initState);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order;
    }

    private PullToRefreshListView pullToRefreshListView;

    @Override
    protected void initView() {
        pullToRefreshListView = (PullToRefreshListView) mGroup
                .findViewById(R.id.listview_orders);
        WWViewUtil.setEmptyView(pullToRefreshListView.getRefreshableView());
        mAdapter = new OrderAdapter(getActivity(), this);
        pullToRefreshListView.setAdapter(mAdapter);
        pullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        withTime=false;
                        requestData();
                    }
                });
        // 设置 end-of-list监听
        pullToRefreshListView
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            requestData();
                        }
                    }
                });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mState = bundle.getInt(KEY_STATE);
            date = bundle.getString(KEY_DATE);
            doRefresh = bundle.getBoolean(KEY_DO_REFRESH);
        }
        if (doRefresh) {
            withTime = false;
            requestData();
        }

    }

    private Cancelable cancelable;

    private void requestData() {
        showWaitDialog();
        RequestParams params = ParamsUtils.getSessionParams(ApiTrade
                .ordersGet());
        params.addQueryStringParameter("status", mState==Order.STATE_CANCEL? mState+","+Order.STATE_CLOSE:mState+"");

        params.addQueryStringParameter(Api.KEY_PAGE_INDEX, mCurrentPage + "");
        if(withTime){
            params.addQueryStringParameter("queryDate", date);
        }

        params.addQueryStringParameter("sendMode", Order.SEND_MODE_ALL + "");
        ZLog.showPost(params.toString());
        if (cancelable != null) {
            cancelable.cancel();// 中断之前的一个下载
        }
        cancelable = x.http()
                .get(params,
                        new WWXCallBack("OrdersGet") {
                            @Override
                            public void onAfterSuccessOk(JSONObject data) {
                                mCurrentPage++;
                                totalCount = data.getIntValue("PageCount");
                                List<Order> list = JSON.parseArray(data
                                        .getJSONArray(Api.KEY_DATA)
                                        .toJSONString(), Order.class);
                                if (mCurrentPage > 1) {
                                    mAdapter.addDatas(list);
                                } else {
                                    mAdapter.setDatas(list);
                                }
                                int count = JsonUtils.parserTotalCount(data);
                                EventBus.getDefault().post(
                                        new OrderEvent(count + ""));
                            }

                            @Override
                            public void onAfterFinished() {
                                dismissWaitDialog();
                                pullToRefreshListView.onRefreshComplete();

                            }
                        });
    }

    public interface OnDateChangeListener {

    }

    @Override
    public void onRefresh() {
        mCurrentPage = 0;
        requestData();
    }

    public void changeDate(String date, boolean withTime) {
        this.date = date;
        this.withTime = withTime;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_DETAILS:
                    if (data != null) {
                        mAdapter.removeItem(data.getIntExtra(Consts.KEY_POSITION,
                                -1));
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
