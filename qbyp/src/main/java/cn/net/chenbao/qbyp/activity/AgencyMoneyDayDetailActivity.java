package cn.net.chenbao.qbyp.activity;

import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.AgencyMoneyAdapter;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyAcount;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;

/***
 * Description:代理余额日明细 Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyMoneyDayDetailActivity extends FatherActivity implements
        OnClickListener, OnRefreshListener<ListView> {
    private PullToRefreshListView mPullToRefreshListView;
    private AgencyMoneyAdapter agencyMoneyAdapter;
    private TextView tv_time;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private int day;
    private boolean withTime = true;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private int agentId = -1;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_agency_money_day_detail;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.remaining_des, true);
        agentId = getIntent().getIntExtra(Consts.AGENT_ID, -1);

    }

    @Override
    protected void initView() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.iv_right).setOnClickListener(this);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_datas);
        // 设置刷新监听
        WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());

        mPullToRefreshListView.setOnRefreshListener(this);
        // 设置 end-of-list监听
        mPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            requestData(withTime);
                        }
                    }
                });
        agencyMoneyAdapter = new AgencyMoneyAdapter(this,
                AgencyMoneyAdapter.DAY, agentId);
        mPullToRefreshListView.setAdapter(agencyMoneyAdapter);
    }

    @Override
    protected void doOperate() {
        Time time = new Time("GMT+8");
        time.setToNow();
        String CreateDate = getIntent().getStringExtra(Consts.KEY_DATA);
        year = Integer.parseInt(CreateDate.substring(0, 4));
        month = Integer.parseInt(CreateDate.substring(5, 7));
        day = Integer.parseInt(CreateDate.substring(8, 10));
        setTimeUI(year, month, day);
        requestData(withTime);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_left:
                if (!TimeUtil.isFastClick()) {
                    setTime(true);
                    withTime = true;
                    onRefresh();
                } else {

                    WWToast.showShort(R.string.oprate_fast);
                }
                break;
            case R.id.iv_right:
                if (!TimeUtil.isFastClick()) {
                    setTime(false);
                    withTime = true;
                    onRefresh();
                } else {
                    WWToast.showShort(R.string.oprate_fast);
                }
                break;
            default:
                break;
        }
    }

    /***
     * Description: 请求数据
     *
     * @author ZXJ
     * @date 2016-10-12
     * @param withTime
     ***/
    private void requestData(boolean withTime) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiAgency.getAccountDetail());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        if (withTime) {
            params.addBodyParameter("queryDate", year + ""
                    + (month > 9 ? month : ("0" + month))
                    + (day > 9 ? day : ("0" + day)));
        }
        params.addBodyParameter("agentId", agentId + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack("AccountDetail") {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                totalCount = data.getIntValue("PageCount");
                ArrayList<AgencyAcount> dataList = (ArrayList<AgencyAcount>) JSONArray
                        .parseArray(data.getString("Data"), AgencyAcount.class);
                if (mCurrentPage > 1) {
                    agencyMoneyAdapter.addDatas(dataList);
                } else {
                    agencyMoneyAdapter.setDatas(dataList);
                }
            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mCurrentPage = 0;
        requestData(withTime);
    }

    public void onRefresh() {
        mCurrentPage = 0;
        requestData(withTime);
    }

    /**
     * 设置时间
     *
     * @param b true减
     */
    private void setTime(boolean b) {
        Calendar cl = TimeUtil.setCalendar(year, month, day);
        if (b) {
            cl = TimeUtil.getBeforeDay(cl);
        } else {
            cl = TimeUtil.getAfterDay(cl);
        }
        year = cl.get(Calendar.YEAR);
        day = cl.get(Calendar.DAY_OF_MONTH);
        month = cl.get(Calendar.MONTH) + 1;
        setTimeUI(year, month, day);
    }

    private void setTimeUI(int year2, int month2, int day2) {
        tv_time.setText(year2 + "年" + month2 + "月" + day2 + "日");
    }

}
