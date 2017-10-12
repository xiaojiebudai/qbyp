package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.OfflineOrderAdapter;
import cn.net.chenbao.qbyp.api.ApiOfflineTrade;
import cn.net.chenbao.qbyp.bean.OfflineOrder;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.SelectTimePop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wuri on 2017/2/13.
 * Description:线下订单
 */

public class OfflineOrderActivity extends FatherActivity {
    @BindView(R.id.listview)
    PullToRefreshListView mListView;
    private SelectTimePop selectTimePop;
    private OfflineOrderAdapter mAdapter;
    private ArrayList<OfflineOrder> list;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.act_offline_order;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.offline_order, true);
        initTextHeadRigth(R.string.query, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            OfflineOrderActivity.this,
                            R.layout.act_person_public_list, new SelectTimePop.OnSelectOKLisentner() {

                        @Override
                        public void selectOk(int years, int months) {
                            year = years;
                            month = months;
                            withTime = true;
                            onRefresh();
                        }

                        @Override
                        public void selectAll() {
                            withTime = false;
                            onRefresh();
                        }
                    });
                }
                selectTimePop.showChooseWindow();
            }
        });
    }

    @Override
    protected void initView() {
        WWViewUtil.setEmptyView(mListView.getRefreshableView());
        mAdapter = new OfflineOrderAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 0;
                requestData(withTime);
            }
        });

        mListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (mCurrentPage >= totalCount) {
                    WWToast.showShort(R.string.nomore_data);
                } else {
                    requestData(withTime);
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PublicWay.startStoreActivity(OfflineOrderActivity.this,
                        mAdapter.getItem(i - 1).SellerId, 0);
            }
        });
    }

    @Override
    protected void doOperate() {
        requestData(false);
    }

    public void onRefresh() {
        mCurrentPage = 0;
        requestData(withTime);
    }

    private void requestData(boolean withTime) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiOfflineTrade.OfflineOrders());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        if (withTime) {
            ZLog.showPost("queryDate>>>" + year + ""
                    + (month > 9 ? month : ("0" + month)));
            params.addBodyParameter("queryDate", year + ""
                    + (month > 9 ? month : ("0" + month)));
        }
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack("OfflineOrders") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                list = (ArrayList<OfflineOrder>) JSONArray.parseArray(
                        data.getString("Data"), OfflineOrder.class);
                totalCount = data.getIntValue("PageCount");
                if (mCurrentPage > 1) {
                    mAdapter.addDatas(list);
                } else {
                    mAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mListView.onRefreshComplete();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
