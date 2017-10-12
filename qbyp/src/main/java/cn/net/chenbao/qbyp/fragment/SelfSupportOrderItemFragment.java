package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportOrderManagerAdapter;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopOrderOutline;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by 木头 on 2016/11/4.  //TODO 如果使用该类 ,adapter中需要进行处理
 */

public class SelfSupportOrderItemFragment extends FatherFragment {

    public static final int MODE_ALL = 0;
    public static final int MODE_PREDEL = 1;
    public static final int MODE_OTHER = 2;

    public static final int REQUEST_CODE = 10088;
    PullToRefreshListView mListView;
    private SelfSupportOrderManagerAdapter mAdapter;
    private RadioGroup mRb;
    private int cuttertPager = 0;
    private List<ShopOrderOutline> orders;
    private int mode;
    // 分页数据
    private int totalCount = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_self_order;
    }

    public void doRequest() {
        RequestParams params = new RequestParams(ApiShop.OrderOutlines());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        switch (mode) {
            case 0:
                break;
            case 1:
                params.addBodyParameter("status", "0,1,2");
                break;
            case 2:
                params.addBodyParameter("status", "3,4,5,6");
                break;
        }
        params.addBodyParameter("pageIndex", cuttertPager + "");
        x.http().get(params, new WWXCallBack("OrderOutlines") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                cuttertPager++;
                JSONArray jsonArray = data.getJSONArray("Data");
                orders = JSON.parseArray(jsonArray.toJSONString(), ShopOrderOutline.class);
                totalCount = data.getIntValue("PageCount");
                if (cuttertPager > 1) {
                    mAdapter.addDatas(orders);
                } else {
                    mAdapter.setDatas(orders);
                    if (mode == MODE_PREDEL) {
                        getActivity().sendBroadcast(new Intent("order_self").putExtra(Consts.KEY_MODULE, SelfSupportOrderFragment.EDIT).putExtra(Consts.KEY_DATA, data.getIntValue("TotalCount")));
                    }
                }
            }

            @Override
            public void onAfterFinished() {
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

        mode = getArguments().getInt(Consts.KEY_MODULE);
        mListView = (PullToRefreshListView) mGroup
                .findViewById(R.id.listview_orders);
        ListView listView = mListView.getRefreshableView();
        WWViewUtil.setEmptyView(listView);
        mAdapter = new SelfSupportOrderManagerAdapter(getActivity());//TODO 如果使用该类 ,adapter中需要进行处理
        mAdapter.setOperateListener(new SelfSupportOrderManagerAdapter.OperateCallBack() {
            @Override
            public void operateListener() {
                //操作之后去刷新所有的页面
                getActivity().sendBroadcast(new Intent("order_self").putExtra(Consts.KEY_MODULE, SelfSupportOrderFragment.REFRESH));
            }
        });
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
        cuttertPager = 0;
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

