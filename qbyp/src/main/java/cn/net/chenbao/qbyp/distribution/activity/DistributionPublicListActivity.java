package cn.net.chenbao.qbyp.distribution.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionPublicListAdapter;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionUnfreezeAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.distribution.been.DistributionUnfreeze;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
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
 * Created by zxj on 2017/4/16.
 *
 * @description 钱包明细列表（货款/提现/优币/积分明细）
 */

public class DistributionPublicListActivity extends FatherActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.listview_datas)
    PullToRefreshListView listviewDatas;
    @BindView(R.id.tv_total_money_in)
    TextView tvTotalMoneyIn;
    @BindView(R.id.tv_total_money_out)
    TextView tvTotalMoneyOut;
    @BindView(R.id.fl_bottom)
    LinearLayout flBottom;
    private int model;
    /**
     * 货款
     */
    public static final int GOODSPAYMENT = 0;
    /**
     * 提现
     */
    public static final int WITHDRAW = 1;
    /**
     * 兑换积分
     */
    public static final int UB = 2;

    /**
     * 积分
     */
    public static final int INTEGRAL = 3;
    private DistributionPublicListAdapter adapter;
    /**
     * 解冻
     */
    public static final int ConsumePool = 4;
    private DistributionUnfreezeAdapter freeAdapter;
    /**
     * 一级分销加速列表
     */
    public static final int PoolLevelOne = 5;
    /**
     * 二级分销加速列表
     */
    public static final int PoolLevelTwo = 6;
    /**
     * 分销加速明细列表
     */
    public static final int PoolLevelDes = 7;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    private SelectTimePop selectTimePop;
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_public_list;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, GOODSPAYMENT);
        if (model == PoolLevelDes) {
            userId = getIntent().getStringExtra(Consts.KEY_DATA);
        }
    }

    @Override
    protected void initView() {
        initTextHeadRigth(R.string.query, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            DistributionPublicListActivity.this,
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
        // 设置刷新监听
        WWViewUtil.setEmptyView(listviewDatas.getRefreshableView());
        listviewDatas.setOnRefreshListener(this);
        // 设置 end-of-list监听
        listviewDatas
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (!HaveNextPage) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            requestData(withTime);
                        }
                    }
                });
        setView();
    }

    private void setView() {

        switch (model) {
            case GOODSPAYMENT:
                initDefautHead(R.string.goods_money_des, true);
                llTab.setVisibility(View.VISIBLE);
                tv0.setText(R.string.money_sum);
                tv1.setText(R.string.time);

                tv2.setText(R.string.remark);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                url = ApiDistribution.AccountDetail();
                requestResultCode = "AccountDetail";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            case WITHDRAW:
                initDefautHead(R.string.tixian_des, true);
                llTab.setVisibility(View.VISIBLE);
                tv0.setText(R.string.money_sum);
                tv1.setText(R.string.fee);

                tv2.setText(R.string.state);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                url = ApiDistribution.CashDetail();
                requestResultCode = "CashDetail";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;

            case UB:
                initDefautHead(R.string.ub, true);
                llTab.setVisibility(View.VISIBLE);
                flBottom.setVisibility(View.VISIBLE);
                tv0.setText(R.string.num);
                tv1.setText(R.string.time);

                tv2.setText(R.string.remark);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                url = ApiDistribution.ConsumeBalanceDetail();
                requestResultCode = "ConsumeBalanceDetail";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            case INTEGRAL:
                initDefautHead(R.string.integral_des, true);
                llTab.setVisibility(View.VISIBLE);
                tv0.setText(R.string.jifen_number);
                tv1.setText(R.string.time);

                tv2.setText(R.string.remark);
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                url = ApiDistribution.ConsumeDetail();
                requestResultCode = "ConsumeDetail";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            case ConsumePool:
                initDefautHead(R.string.freeze_detial, true);
                llTab.setVisibility(View.GONE);
                url = ApiDistribution.ConsumePoolDetail();
                requestResultCode = "ConsumePoolDetail";
                freeAdapter = new DistributionUnfreezeAdapter(this);
                listviewDatas.setAdapter(freeAdapter);
                break;
            case PoolLevelOne:
                initDefautHead(R.string.pool_level_one, true);
                llTab.setVisibility(View.VISIBLE);
                findViewById(R.id.rl_head_right).setVisibility(View.GONE);
                tv0.setText(R.string.user_name);
                tv1.setText(R.string.speed_jifen);

                tv2.setText(getString(R.string.speed));
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                params.rightMargin = DensityUtil.dip2px(this, 14);
                tv2.setLayoutParams(params);
                url = ApiDistribution.ConsumePoolSpeeds();
                requestResultCode = "ConsumePoolSpeeds";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            case PoolLevelTwo:
                initDefautHead(R.string.pool_level_two, true);
                llTab.setVisibility(View.VISIBLE);
                findViewById(R.id.rl_head_right).setVisibility(View.GONE);
                tv0.setText(R.string.user_name);
                tv1.setText(R.string.speed_jifen);

                tv2.setText(getString(R.string.speed));
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
                params1.rightMargin = DensityUtil.dip2px(this, 14);
                tv2.setLayoutParams(params1);
                url = ApiDistribution.ConsumePoolSpeeds();
                requestResultCode = "ConsumePoolSpeeds";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            case PoolLevelDes:
                initDefautHead(R.string.pool_level_des, true);
                llTab.setVisibility(View.VISIBLE);
                findViewById(R.id.rl_head_right).setVisibility(View.VISIBLE);
                tv0.setText(R.string.integral);
                tv1.setText(R.string.surplus_speed_days);

                tv2.setText(getString(R.string.time));
                tv3.setVisibility(View.GONE);
                tv4.setVisibility(View.GONE);
                tv0.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv1.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                tv2.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
                //接口需要修改
                url = ApiDistribution.ConsumeSpeeds();
                requestResultCode = "ConsumeSpeeds";
                adapter = new DistributionPublicListAdapter(this, model);
                listviewDatas.setAdapter(adapter);
                break;
            default:
                break;
        }

    }

    public void onRefresh() {
        mCurrentPage = 0;
        requestData(withTime);
    }

    // 分页数据
    private int mCurrentPage = 0;
    private boolean HaveNextPage;
    private String requestResultCode;
    private String url;

    private ArrayList<DistributionPublicAccount> listData;
    private ArrayList<DistributionUnfreeze> listData1;

    private void requestData(boolean withTime) {
        showWaitDialog();
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        if (withTime) {
            params.addBodyParameter("queryDate", year + ""
                    + (month > 9 ? month : ("0" + month)));
            ZLog.showPost(year + "" + (month > 9 ? month : ("0" + month)));
        }
        if (model == PoolLevelOne) {
            params.addBodyParameter("levelId", "1");
        }
        if (model == PoolLevelTwo) {
            params.addBodyParameter("levelId", "2");
        }
        if (model == PoolLevelDes && (!TextUtils.isEmpty(userId))) {
            params.addBodyParameter("userId", userId);
        }
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack(requestResultCode) {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                listviewDatas.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ZLog.showPost(data.toJSONString());
                mCurrentPage++;
                HaveNextPage = data.getBoolean("HaveNextPage");

                if (model == ConsumePool) {
                    listData1 = (ArrayList<DistributionUnfreeze>) JSONArray.parseArray(
                            data.getString("Data"), DistributionUnfreeze.class);
                    if (mCurrentPage > 1) {
                        freeAdapter.addDatas(listData1);
                    } else {
                        freeAdapter.setDatas(listData1);
                    }
                } else {
                    listData = (ArrayList<DistributionPublicAccount>) JSONArray.parseArray(
                            data.getString("Data"), DistributionPublicAccount.class);

                    if (model == UB) {
                        tvTotalMoneyIn.setText(WWViewUtil.numberFormatWithTwo(data.getDoubleValue("TotalAmount")));
                        tvTotalMoneyOut.setText(WWViewUtil.numberFormatWithTwo(data.getDoubleValue("TotalAmount2")));
                    }
                    if (mCurrentPage > 1) {
                        adapter.addDatas(listData);
                    } else {
                        adapter.setDatas(listData);
                    }
                }

            }
        });
    }

    @Override
    protected void doOperate() {
        Time time = new Time("GMT+8");
        time.setToNow();
        year = time.year;
        month = time.month + 1;
        requestData(withTime);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        onRefresh();
    }
}
