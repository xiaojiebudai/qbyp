package cn.net.chenbao.qbyp.activity;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportOrderManagerAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopOrderOutline;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by ppsher on 2017/1/3.
 * Description: 跟本地订单一样的界面
 */

public class SelfSupportOrderActivity2 extends FatherActivity {
    private PullToRefreshListView mListView;
    private SelfSupportOrderManagerAdapter mAdapter;
    private int cuttertPager = 0;
    private List<ShopOrderOutline> orders;
    // 分页数据
    private int totalCount = 0;
    private int status;
    public static final int REQUEST_CODE = 10088;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_local_order;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order, true);
        mListView = (PullToRefreshListView) findViewById(R.id.listview_orders);
        WWViewUtil.setEmptyView(mListView.getRefreshableView());

        mAdapter = new SelfSupportOrderManagerAdapter(this);
        mAdapter.setOperateListener(new SelfSupportOrderManagerAdapter.OperateCallBack() {
            @Override
            public void operateListener() {
                cuttertPager = 0;
                doRequest();
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
    }

    @Override
    protected void initView() {
        status = getIntent().getIntExtra(Consts.KEY_DATA, -1);
    }

    @Override
    protected void doOperate() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        cuttertPager = 0;
        doRequest();
    }

    public void doRequest() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.OrderOutlines());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        if (status != -1) {
            params.addBodyParameter("status", "" + status);
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
