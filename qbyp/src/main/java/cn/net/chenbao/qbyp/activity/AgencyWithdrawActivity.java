package cn.net.chenbao.qbyp.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.AgencySelectAdapter;
import cn.net.chenbao.qbyp.adapter.listview.AgencyWithdrawAdapter;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyChargeAccount;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.SelectTimePop;
import cn.net.chenbao.qbyp.view.SelectTimePop.OnSelectOKLisentner;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:代理提现明细(还款明细) Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyWithdrawActivity extends FatherActivity implements
        OnClickListener, OnRefreshListener<ListView> {
    private PullToRefreshListView mPullToRefreshListView;
    private int model;
    /**
     * 提现
     */
    public final static int WITHDRAW = 0;
    /**
     * 还款
     */
    public final static int REPAYMENT = 1;
    private LinearLayout ll_area_select, ll_areachoose;
    private AgencyWithdrawAdapter agencyWithdrawAdapter;
    private TextView tv_area, tv_0, tv_1, tv_2;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private String requestResultCode;
    private String url;
    private int agentId = 0;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    private SelectTimePop selectTimePop;
    //添加一个空view
    private View view_empty;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_agency_withdraw;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, WITHDRAW);
        initTextHeadRigth(R.string.query, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            AgencyWithdrawActivity.this,
                            R.layout.act_person_public_list,
                            new OnSelectOKLisentner() {

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
        ll_area_select = (LinearLayout) findViewById(R.id.ll_area_select);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_datas);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        view_empty=findViewById(R.id.view_empty);
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
        switch (model) {
            case WITHDRAW:
                initDefautHead(R.string.tixian_des, true);
                ll_areachoose = (LinearLayout) findViewById(R.id.ll_areachoose);
                ll_areachoose.setVisibility(View.VISIBLE);
                view_empty.setVisibility(View.VISIBLE);
                tv_area = (TextView) findViewById(R.id.tv_area);
                ll_area_select.setOnClickListener(this);
                agencyListView = new ListView(this);
                agencyListView.setBackgroundResource(R.color.gray_bg);
                agencyListView.setPadding(DensityUtil.dip2px(this, 1),
                        DensityUtil.dip2px(this, 1), DensityUtil.dip2px(this, 1),
                        DensityUtil.dip2px(this, 1));
                agencySelectAdapter = new AgencySelectAdapter(this);
                agencyListView.setAdapter(agencySelectAdapter);
                agencyInfo = new AgencyInfo();
                getAgencyList();
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.commit_time);
                tv_2.setText(R.string.state);

                agencyWithdrawAdapter = new AgencyWithdrawAdapter(this, WITHDRAW);
                url = ApiAgency.CashDetail();
                mPullToRefreshListView.setAdapter(agencyWithdrawAdapter);
                requestResultCode = "CashDetail";

                break;
            case REPAYMENT:
                agentId = getIntent().getIntExtra(Consts.AGENT_ID, 0);
                initDefautHead(R.string.repayment_detail, true);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.time);
                tv_2.setText(R.string.remark);

                agencyWithdrawAdapter = new AgencyWithdrawAdapter(this, REPAYMENT);
                url = ApiAgency.getChargeDetail();
                mPullToRefreshListView.setAdapter(agencyWithdrawAdapter);
                requestResultCode = "ChargeDetail";
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
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        if (withTime) {
            params.addBodyParameter("queryDate", year + ""
                    + (month > 9 ? month : ("0" + month)));
        }
        params.addBodyParameter("agentId", agentId + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack(requestResultCode) {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                totalCount = data.getIntValue("PageCount");
                ArrayList<AgencyChargeAccount> dataList = (ArrayList<AgencyChargeAccount>) JSONArray
                        .parseArray(data.getString("Data"),
                                AgencyChargeAccount.class);
                if (mCurrentPage > 1) {
                    agencyWithdrawAdapter.addDatas(dataList);
                } else {
                    agencyWithdrawAdapter.setDatas(dataList);
                }
            }
        });

    }

    /***
     * Description: 获取区域列表
     *
     * @author ZXJ
     * @date 2016-10-12
     ***/
    private void getAgencyList() {
        showWaitDialog();
        x.http().get(ParamsUtils.getSessionParams(ApiAgency.getMyAgents()),
                new WWXCallBack("MyAgents") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        agencyInfos = (ArrayList<AgencyInfo>) JSON.parseArray(
                                data.getString("Data"), AgencyInfo.class);
                        AgencyInfo agencyInfo = new AgencyInfo();
                        agencyInfo.AreaName = "全部";
                        agencyInfo.AgentId = 0;
                        agencyInfos.add(0, agencyInfo);
                        if (agencyInfos != null && agencyInfos.size() > 0) {
                            agencyInfo = agencyInfos.get(0);
                            tv_area.setText(agencyInfo.AreaName);
                        }
                        agencySelectAdapter.setDatas(agencyInfos);
                        agencySelectAdapter.setSelectPostion(0);
                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });

    }

    @Override
    protected void doOperate() {
        // Time time = new Time("GMT+8");
        // time.setToNow();
        // year = time.year;
        // month = time.month + 1;
        requestData(withTime);
    }

    /**
     * 选择区域
     */
    private ArrayList<AgencyInfo> agencyInfos;
    private AgencySelectAdapter agencySelectAdapter;
    private ListView agencyListView;
    private AgencyInfo agencyInfo;

    private void seleteAgency() {
        final PopupWindow popupWindow = new PopupWindow(agencyListView,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WWViewUtil.setPopInSDK7(popupWindow,ll_area_select);
//        popupWindow.showAsDropDown(ll_area_select);
        agencyListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                agencyInfo = agencyInfos.get(position);
                tv_area.setText(agencyInfo.AreaName);
                agencySelectAdapter.setSelectPostion(position);
                agentId = agencyInfo.AgentId;
                // 设置时间无效
                withTime = false;
                onRefresh();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_area_select:
                seleteAgency();
                break;

            default:
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        // 设置时间无效
        withTime = false;
        onRefresh();
    }

    public void onRefresh() {
        mCurrentPage = 0;
        requestData(withTime);
    }

}
