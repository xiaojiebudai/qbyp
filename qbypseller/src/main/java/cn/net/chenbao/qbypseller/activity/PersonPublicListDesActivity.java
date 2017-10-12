package cn.net.chenbao.qbypseller.activity;

import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.AccountPendingDetailAdapter;
import cn.net.chenbao.qbypseller.adapter.listview.AcountLisstAdapter;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.AccountPendingDetail;
import cn.net.chenbao.qbypseller.bean.AccountSellerPending;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.view.SelectTimePop;
import cn.net.chenbao.qbypseller.view.SelectTimePop.OnSelectOKLisentner;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:提现明细、余额明细、预结算、销售额Company: wangwanglife Version：1.0
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
    /**
     * 销售额明细
     */
    public static final int XIAOSHOUEDES = 1;
    /**
     * 余额明细
     */
    public static final int YUEDES = 2;
    /**
     * 预结算明细
     */
    public static final int YUJIESUANDES = 3;
    /**
     * 积分明细
     */
    public static final int JIFEN = 4;
    private AcountLisstAdapter acountLisstAdapter;
    private AccountPendingDetailAdapter accountPendingDetailAdapter;

    private int model;
    private LinearLayout ll_time_choose, ll_tab, ll_buttom;
    private TextView tv_time, tv_0, tv_1, tv_2, tv_money_total, tv_total;
    /**
     * 选择时间
     */
    private int year;
    private int month;
    private boolean withTime = false;
    private SelectTimePop selectTimePop;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_person_public_list;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, TIXIANDES);
    }

    @Override
    protected void initView() {
        ll_time_choose = (LinearLayout) findViewById(R.id.ll_time_choose);
        ll_buttom = (LinearLayout) findViewById(R.id.ll_buttom);
        ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
        ll_time_choose.setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.iv_right).setOnClickListener(this);

        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_0 = (TextView) findViewById(R.id.tv_0);
        tv_1 = (TextView) findViewById(R.id.tv_1);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_money_total = (TextView) findViewById(R.id.tv_money_total);
        tv_total = (TextView) findViewById(R.id.tv_total);

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
                ll_time_choose.setVisibility(View.GONE);
                ll_tab.setVisibility(View.VISIBLE);
                ll_buttom.setVisibility(View.GONE);
                tv_0.setText(R.string.date);
                tv_1.setText(R.string.money_sum);
                tv_2.setText(R.string.state);
                inintTitleRight();
                url = ApiSeller.CashDetail();
                acountLisstAdapter = new AcountLisstAdapter(this, TIXIANDES);
                mPullToRefreshListView.setAdapter(acountLisstAdapter);
                requestResultCode = "CashDetail";
                break;
            case XIAOSHOUEDES:
                initDefautHead(R.string.xiaoshoue, true);
                ll_time_choose.setVisibility(View.VISIBLE);
                ll_tab.setVisibility(View.VISIBLE);
                ll_buttom.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.vip);
                tv_1.setText(R.string.order_number);
                tv_2.setText(R.string.money_sum);
                url = ApiSeller.SalesList();
                acountLisstAdapter = new AcountLisstAdapter(this, XIAOSHOUEDES);
                mPullToRefreshListView.setAdapter(acountLisstAdapter);
                requestResultCode = "SalesList";
                break;
            case YUEDES:
                initDefautHead(R.string.remaining_des, true);
                ll_time_choose.setVisibility(View.GONE);
                ll_tab.setVisibility(View.VISIBLE);
                ll_buttom.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.type);
                tv_1.setText(R.string.order_number);
                tv_2.setText(R.string.money_sum);
                inintTitleRight();
                url = ApiSeller.AccountBalanceDetail();
                acountLisstAdapter = new AcountLisstAdapter(this, YUEDES);
                mPullToRefreshListView.setAdapter(acountLisstAdapter);
                requestResultCode = "AccountBalanceDetail";
                break;
            case YUJIESUANDES:
                initDefautHead(R.string.yujiesuan_des, true);
                ll_tab.setVisibility(View.VISIBLE);
                ll_buttom.setVisibility(View.VISIBLE);
                tv_0.setText(R.string.order_number);
                tv_1.setText(R.string.money_sum);
                tv_2.setText(R.string.jiesuan_money_sum);
                inintTitleRight();
                tv_0.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 4.0f));
                tv_1.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));
                tv_2.setLayoutParams(new LinearLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 2.0f));

                accountPendingDetailAdapter = new AccountPendingDetailAdapter(this);
                mPullToRefreshListView.setAdapter(accountPendingDetailAdapter);
                url = ApiSeller.AccountPendingDetail();
                requestResultCode = "AccountPendingDetail";
                break;
            case JIFEN:
                initDefautHead(R.string.jifen_mx, true);
                ll_time_choose.setVisibility(View.GONE);
                ll_tab.setVisibility(View.VISIBLE);
                ll_buttom.setVisibility(View.GONE);
                tv_0.setText(R.string.money_sum);
                tv_1.setText(R.string.date);
                tv_2.setText(R.string.remark);
                inintTitleRight();
                url = ApiSeller.AccountIntegralDetail();
                acountLisstAdapter = new AcountLisstAdapter(this, JIFEN);
                mPullToRefreshListView.setAdapter(acountLisstAdapter);
                requestResultCode = "AccountIntegralDetail";
                break;
        }

    }

    private void inintTitleRight() {
        initTextHeadRigth(R.string.query, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            PersonPublicListDesActivity.this,
                            R.layout.act_person_public_list, new OnSelectOKLisentner() {

                        @Override
                        public void selectOk(int years, int months) {
                            year = years;
                            month = months;
                            setTimeUI(year, month);
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
    protected void doOperate() {
        Time time = new Time("GMT+8");
        time.setToNow();
        year = time.year;
        month = time.month + 1;
        setTimeUI(year, month);
        requestData(withTime);
    }

    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private String requestResultCode;
    private String url;

    // 数据列表
    private ArrayList<AccountSellerPending> listData;

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
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack(requestResultCode) {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                //统一用一个
                mCurrentPage++;
                totalCount = data.getIntValue("PageCount");
                switch (model) {
                    case TIXIANDES:
                        listData = (ArrayList<AccountSellerPending>) JSONArray.parseArray(
                                data.getString("Data"), AccountSellerPending.class);
                        if (mCurrentPage > 1) {
                            acountLisstAdapter.addDatas(listData);
                        } else {
                            acountLisstAdapter.setDatas(listData);
                            if (model != TIXIANDES) {
                                tv_money_total.setText(WWViewUtil.numberFormatPrice(data.getDouble("TotalAmount")));
                                tv_total.setText(String.format(getString(R.string.pen_num),data.getIntValue("TotalCount")));
                            }
                        }
                        break;
                    case XIAOSHOUEDES:
                        listData = (ArrayList<AccountSellerPending>) JSONArray.parseArray(
                                data.getString("Data"), AccountSellerPending.class);
                        if (mCurrentPage > 1) {
                            acountLisstAdapter.addDatas(listData);
                        } else {
                            acountLisstAdapter.setDatas(listData);
                            if (model != TIXIANDES) {
                                tv_money_total.setText(WWViewUtil.numberFormatPrice(data.getDouble("TotalAmount")));
                                tv_total.setText(String.format(getString(R.string.pen_num),data.getIntValue("TotalCount")));
                            }
                        }
                        break;
                    case YUEDES:
                        listData = (ArrayList<AccountSellerPending>) JSONArray.parseArray(
                                data.getString("Data"), AccountSellerPending.class);
                        if (mCurrentPage > 1) {
                            acountLisstAdapter.addDatas(listData);
                        } else {
                            acountLisstAdapter.setDatas(listData);
                            if (model != TIXIANDES) {
                                tv_money_total.setText(WWViewUtil.numberFormatPrice(data.getDouble("TotalAmount")));
                                tv_total.setText(String.format(getString(R.string.pen_num),data.getIntValue("TotalCount")));
                            }
                        }
                        break;
                    case JIFEN:
                        listData = (ArrayList<AccountSellerPending>) JSONArray.parseArray(
                                data.getString("Data"), AccountSellerPending.class);
                        if (mCurrentPage > 1) {
                            acountLisstAdapter.addDatas(listData);
                        } else {
                            acountLisstAdapter.setDatas(listData);
                        }
                        break;
                    case YUJIESUANDES:
                        ArrayList<AccountPendingDetail> listData = (ArrayList<AccountPendingDetail>) JSONArray.parseArray(
                                data.getString("Data"), AccountPendingDetail.class);
                        if (mCurrentPage > 1) {
                            accountPendingDetailAdapter.addDatas(listData);
                        } else {
                            accountPendingDetailAdapter.setDatas(listData);
                            if (model != TIXIANDES) {
                                tv_money_total.setText(WWViewUtil.numberFormatPrice(data.getDouble("TotalAmount")));
                                tv_total.setText(String.format(getString(R.string.pen_num),data.getIntValue("TotalCount")));
                            }
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
        switch (v.getId()) {

            case R.id.ll_time_choose:
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            PersonPublicListDesActivity.this,
                            R.layout.act_person_public_list, new OnSelectOKLisentner() {

                        @Override
                        public void selectOk(int years, int months) {
                            year = years;
                            month = months;
                            setTimeUI(year, month);
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
                break;

            case R.id.iv_left:
                if (!TimeUtil.isFastClick()) {
                    setTime(true);
                    withTime = true;
                    onRefresh();
                } else {

                    WWToast.showShort(R.string.operation_too_fast);
                }
                break;
            case R.id.iv_right:
                if (!TimeUtil.isFastClick()) {
                    setTime(false);
                    withTime = true;
                    onRefresh();
                } else {
                    WWToast.showShort(R.string.operation_too_fast);
                }
                break;

            default:
                break;
        }
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
        setTimeUI(year, month);
    }

    private void setTimeUI(int year2, int month2) {
        tv_time.setText(year2 + "年" + month2 + "月");

    }

}
