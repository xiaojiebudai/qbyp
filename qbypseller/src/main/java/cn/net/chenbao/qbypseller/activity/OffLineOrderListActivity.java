package cn.net.chenbao.qbypseller.activity;

import android.text.format.Time;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.JiFenOrderAdapter;
import cn.net.chenbao.qbypseller.api.ApiOfflineTrade;
import cn.net.chenbao.qbypseller.bean.JiFen;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.view.SelectTimePop;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * Created by Administrator on 2017/2/13.
 */

public class OffLineOrderListActivity extends FatherActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    @BindView(R.id.listview_data)
    PullToRefreshListView listviewData;
    private JiFenOrderAdapter adapter;
    // 分页数据
    private int mCurrentPage = 0;
    // 提现
    private ArrayList<JiFen> list;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    private SelectTimePop selectTimePop;
    private TextView tv_money_total, tv_total;
    @Override
    protected int getLayoutId() {
        return R.layout.act_offlineorderlist;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.offlineorder, true);
    }

    @Override
    protected void initView() {
        listviewData.setOnRefreshListener(this);
        listviewData
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            requestData(withTime);
                        }

                    }
                });
        tv_money_total = (TextView) findViewById(R.id.tv_money_total);
        tv_total = (TextView) findViewById(R.id.tv_total);
        WWViewUtil.setEmptyView(listviewData.getRefreshableView());
        adapter = new JiFenOrderAdapter(this);
        listviewData.setAdapter(adapter);
        initTextHeadRigth(R.string.query, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectTimePop==null){
                    selectTimePop = new SelectTimePop(
                            OffLineOrderListActivity.this,
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

    public void onRefresh() {
        mCurrentPage = 0;
        requestData(withTime);
    }
    @Override
    protected void doOperate() {
        Time time = new Time("GMT+8");
        time.setToNow();
        year = time.year;
        month = time.month + 1;
        requestData(withTime);
    }
    private int totalCount = 0;
    private void requestData(boolean withTime) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiOfflineTrade.OfflineOrders());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        if (withTime) {
            ZLog.showPost("queryDate>>>"+year + ""
                    + (month > 9 ? month : ("0" + month)));
            params.addBodyParameter("queryDate", year + ""
                    + (month > 9 ? month : ("0" + month)));
        }
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        x.http().get(params, new WWXCallBack("OfflineOrders") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                list = (ArrayList<JiFen>) JSONArray.parseArray(
                        data.getString("Data"), JiFen.class);
                totalCount = data.getIntValue("PageCount");
                tv_money_total.setText(WWViewUtil.numberFormatPrice(data.getDouble("TotalAmount")));
                tv_total.setText(String.format(getString(R.string.pen_num),data.getIntValue("TotalCount")));
                    if (mCurrentPage > 1) {
                        adapter.addDatas(list);
                    } else {
                        adapter.setDatas(list);

                    }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                listviewData.onRefreshComplete();
            }
        });}
    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mCurrentPage = 0;
        requestData(withTime);
    }

}
