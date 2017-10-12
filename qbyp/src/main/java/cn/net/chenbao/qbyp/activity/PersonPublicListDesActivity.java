package cn.net.chenbao.qbyp.activity;

import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import cn.net.chenbao.qbyp.adapter.listview.CanCustomBackAdapter;
import cn.net.chenbao.qbyp.adapter.listview.CashAdapter;
import cn.net.chenbao.qbyp.adapter.listview.YueDesAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AccountRebateInternal;
import cn.net.chenbao.qbyp.bean.Cash;
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

/***
 * Description:提现明细、余额、分润、积分、分红 Company:
 * wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-29
 */
public class PersonPublicListDesActivity extends FatherActivity implements
        OnClickListener, OnRefreshListener<ListView> {
    private PullToRefreshListView mPullToRefreshListView;
    /**
     * 提现明细
     */
    public static final int TIXIANDES = 0;
    private CashAdapter cashAdapter;
    /**
     * 余额明细
     */
    public static final int YUEDES = 1;
    private YueDesAdapter yueDesAdapter;
    /**
     * 可用积分明细
     */
    public static final int WWBDES = 2;
    /**
     * 待解冻积分明细
     */
    public static final int WAITUNFREEZEDES = 3;
    /**
     * 总积分明细
     */
    public static final int TOTALINTEGRALDES = 4;
    /**
     * 	柒宝币明细
     */
    public static final int CANCUSTOMEDES =5;
    private CanCustomBackAdapter canCustomBackAdapter;
    private int model;
    private LinearLayout ll_tab;
    private TextView custom_rental, tv_0, tv_1, tv_2, tv_3, tv_4;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    private TextView tv_number;
    private TextView tv_total_money;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_person_public_list;
    }

    private SelectTimePop selectTimePop;
    private FrameLayout fl_bottom;

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, TIXIANDES);
        initDefautHead(R.string.tixian_des, true);
        initTextHeadRigth(R.string.query, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            PersonPublicListDesActivity.this,
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
        ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
        custom_rental = (TextView) findViewById(R.id.custom_rental);
        fl_bottom = (FrameLayout) findViewById(R.id.fl_bottom);

        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_3 = (TextView) findViewById(R.id.tv_3);
        tv_4 = (TextView) findViewById(R.id.tv_4);

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
        setView();
    }

    private void setView() {
        switch (model) {
            case TIXIANDES:
                initDefautHead(R.string.tixian_des, true);
                ll_tab.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.date);
                tv_2.setText(R.string.state);
                tv_3.setText(R.string.remark);
                tv_4.setVisibility(View.GONE);
                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f));
                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f));
                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f));
                tv_3.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f));


                url = ApiUser.getTixianDes();
                cashAdapter = new CashAdapter(this);
                mPullToRefreshListView.setAdapter(cashAdapter);
                requestResultCode = "CashDetail";
                break;
            case YUEDES:
                initDefautHead(R.string.remaining_des, true);
                ll_tab.setVisibility(View.VISIBLE);

                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.time);
                tv_2.setText(R.string.remark);
                tv_3.setVisibility(View.GONE);
                tv_4.setVisibility(View.GONE);

                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 3.0f));
                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 3.0f));
                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));

                yueDesAdapter = new YueDesAdapter(this);

                url = ApiUser.getBalanceDetail();
                mPullToRefreshListView.setAdapter(yueDesAdapter);
                requestResultCode = "BalanceDetail";
                break;

            case WWBDES://可用积分明细
                initDefautHead(R.string.can_use_integral_des, true);
                ll_tab.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.time);

                tv_2.setText(R.string.remark);

                tv_3.setVisibility(View.GONE);
                tv_4.setVisibility(View.GONE);
//                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));
//                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));
//                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));


                url = ApiUser.IntegralConsumeGet();
                canCustomBackAdapter = new CanCustomBackAdapter(this);
                canCustomBackAdapter.setMode(WWBDES);
                mPullToRefreshListView.setAdapter(canCustomBackAdapter);
                requestResultCode = "IntegralConsumeGet";
                break;
            case WAITUNFREEZEDES://待解冻积分明细
                initDefautHead(R.string.wait_unfreeze_integral_des, true);
                ll_tab.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.give_integral);

                tv_2.setText(R.string.remark);

                tv_3.setVisibility(View.GONE);
                tv_4.setVisibility(View.GONE);
//                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));
//                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));
//                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));


                url = ApiUser.IntegralDetailGet();
                canCustomBackAdapter = new CanCustomBackAdapter(this);
                canCustomBackAdapter.setMode(WAITUNFREEZEDES);
                mPullToRefreshListView.setAdapter(canCustomBackAdapter);
                requestResultCode = "IntegralDetailGet";
                break;
            case TOTALINTEGRALDES://总（现金）积分明细
                initDefautHead(R.string.total_integral_des, true);
                fl_bottom.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPullToRefreshListView.getLayoutParams();//TODO 设置底部magin值
                params.setMargins(0, 0, 0, DensityUtil.dip2px(this, 50));
                mPullToRefreshListView.setLayoutParams(params);
                ll_tab.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.private_integral);

                tv_2.setText(R.string.remark);

                tv_3.setVisibility(View.GONE);
                tv_4.setVisibility(View.GONE);
//                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));
//                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));
//                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
//                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));

                findViewById(R.id.fl_bottom).setVisibility(View.VISIBLE);
                tv_number = (TextView) findViewById(R.id.tv_number);
                tv_total_money = (TextView) findViewById(R.id.tv_total_money);
                url = ApiUser.IntegralDetailGet();
                canCustomBackAdapter = new CanCustomBackAdapter(this);
                canCustomBackAdapter.setMode(TOTALINTEGRALDES);
                mPullToRefreshListView.setAdapter(canCustomBackAdapter);
                requestResultCode = "IntegralDetailGet";
                break;
            case CANCUSTOMEDES:
                initDefautHead("柒宝币明细", true);
                ll_tab.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.time);
                tv_2.setText(R.string.remark);
                tv_3.setVisibility(View.GONE);
                tv_4.setVisibility(View.GONE);

                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 3.0f));
                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 3.0f));
                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));

                url = ApiUser.getInternalDetail();
                canCustomBackAdapter = new CanCustomBackAdapter(this);
                mPullToRefreshListView.setAdapter(canCustomBackAdapter);
                requestResultCode = "InternalDetail";
                break;
            default:
                break;
        }

    }

    @Override
    protected void doOperate() {
        Time time = new Time("GMT+8");
        time.setToNow();
        year = time.year;
        month = time.month + 1;
        requestData(withTime);
    }

    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private String requestResultCode;
    private String url;

    // 提现
    private ArrayList<Cash> Cashlist;
    // 可消费补贴--余额
    private ArrayList<AccountRebateInternal> accountRebateInternallist;

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
        if (model == WAITUNFREEZEDES) {
            params.addBodyParameter("status", 0 + "");
        }
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack(requestResultCode) {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ZLog.showPost(data.toJSONString());
                mCurrentPage++;
                totalCount = data.getIntValue("PageCount");
                switch (model) {
                    case TIXIANDES:
                        Cashlist = (ArrayList<Cash>) JSONArray.parseArray(
                                data.getString("Data"), Cash.class);
                        if (mCurrentPage > 1) {
                            cashAdapter.addDatas(Cashlist);
                        } else {
                            cashAdapter.setDatas(Cashlist);
                        }
                        break;
                    case YUEDES:
                        accountRebateInternallist = (ArrayList<AccountRebateInternal>) JSONArray
                                .parseArray(data.getString("Data"),
                                        AccountRebateInternal.class);
                        if (mCurrentPage > 1) {
                            yueDesAdapter.addDatas(accountRebateInternallist);
                        } else {
                            yueDesAdapter.setDatas(accountRebateInternallist);
                        }
                        break;
                    case WAITUNFREEZEDES:
                    case WWBDES://可用积分
                        accountRebateInternallist = (ArrayList<AccountRebateInternal>) JSONArray
                                .parseArray(data.getString("Data"),
                                        AccountRebateInternal.class);
                        custom_rental.setText(data.getString("TotalAmount"));
                        if (mCurrentPage > 1) {
                            canCustomBackAdapter
                                    .addDatas(accountRebateInternallist);
                        } else {
                            canCustomBackAdapter
                                    .setDatas(accountRebateInternallist);
                        }
                        break;
                    case TOTALINTEGRALDES:
                        tv_number.setText(String.format(getString(R.string.pen_number_), data.getIntValue("TotalCount")));
                        tv_total_money.setText(String.format(getString(R.string.money), data.getString("TotalAmount")));
                        accountRebateInternallist = (ArrayList<AccountRebateInternal>) JSONArray
                                .parseArray(data.getString("Data"),
                                        AccountRebateInternal.class);
                        custom_rental.setText(String.format(getString(R.string.money), data.getString("TotalAmount")));
                        if (mCurrentPage > 1) {
                            canCustomBackAdapter
                                    .addDatas(accountRebateInternallist);
                        } else {
                            canCustomBackAdapter
                                    .setDatas(accountRebateInternallist);
                        }
                        break;
                    case CANCUSTOMEDES:
                        accountRebateInternallist = (ArrayList<AccountRebateInternal>) JSONArray
                                .parseArray(data.getString("Data"),
                                        AccountRebateInternal.class);
                        if (mCurrentPage > 1) {
                            canCustomBackAdapter
                                    .addDatas(accountRebateInternallist);
                        } else {
                            canCustomBackAdapter
                                    .setDatas(accountRebateInternallist);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
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
        if (b) {
            if (month > 1) {
                month--;
            } else {
                year--;
                month = 12;
            }
        } else {
            if (month < 12) {
                month++;
            } else {
                year++;
                month = 1;
            }
        }
    }


}
