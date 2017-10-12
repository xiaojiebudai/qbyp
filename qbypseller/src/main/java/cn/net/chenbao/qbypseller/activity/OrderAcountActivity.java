package cn.net.chenbao.qbypseller.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.CommonPageAdapter;
import cn.net.chenbao.qbypseller.api.ApiTrade;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.eventbus.OrderEvent;
import cn.net.chenbao.qbypseller.fragment.OrderFragment;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.view.SelectTimePop;
import cn.net.chenbao.qbypseller.view.TabScrollView;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * 订单列表(统计)
 *
 * @author xl
 * @date 2016-7-30 下午5:00:50
 * @description
 */
public class OrderAcountActivity extends FatherActivity implements
        OnClickListener {

    private TextView mTv_date;
    private String date;

    private int year;
    private int mouth;
    private View[] mTabs_custom_made;
    private TextView tv_count;

    @Override
    protected int getLayoutId() {
        return R.layout.act_order_acount;
    }

    List<OrderFragment> fragments;
    CommonPageAdapter adapter;

    private TextView mTv_count;

    @Override
    protected void initValues() {

        int initState = getIntent().getIntExtra(Consts.KEY_STATE,
                Order.STATE_WAIT_SELLER_CONFIRM);// 默认index=0的状态
        int index = 0;
        switch (initState) {
            case Order.STATE_WAIT_SELLER_CONFIRM:
                index = 0;
                break;
            case Order.STATE_WAIT_PAY:
                index = 1;
                break;
            case Order.STATE_WAIT_BUYER_CONFIRM:
                index = 2;
                break;
            case Order.STATE_REFUND:
                index = 3;
                break;
            case Order.STATE_COMPLETE:
                index = 5;
                break;
            case Order.STATE_CANCEL:
                index = 4;
                break;

            default:
                break;
        }
        date = TimeUtil.timeFormat(System.currentTimeMillis(), "yyyyMM");
        initDefautHead(R.string.order_list, true);
        String[] array = getResources().getStringArray(
                R.array.order_acount_tabs);
        List<String> tabs = Arrays.asList(array);
        mTabs_custom_made = new View[6];
        for (int i = 0; i < tabs.size(); i++) {
            if (i == 0) {
                mTabs_custom_made[i] = getTabItemWithRed(tabs.get(i));
            } else {
                mTabs_custom_made[i] = getTabItem(tabs.get(i));
            }
        }
        TabScrollView tabScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<OrderFragment>();

        date = TimeUtil.timeFormat(System.currentTimeMillis(), "yyyyMM");
        OrderFragment wait_receive = OrderFragment.getInstance(
                Order.STATE_WAIT_SELLER_CONFIRM, date, initState);
        OrderFragment wait_pay = OrderFragment.getInstance(
                Order.STATE_WAIT_PAY, date, initState);
        OrderFragment wait_confirm = OrderFragment.getInstance(
                Order.STATE_WAIT_BUYER_CONFIRM, date, initState);
        OrderFragment refund = OrderFragment.getInstance(Order.STATE_REFUND,
                date, initState);
        OrderFragment complete = OrderFragment.getInstance(
                Order.STATE_COMPLETE, date, initState);
        OrderFragment cancel = OrderFragment.getInstance(Order.STATE_CANCEL,
                date, initState);
        fragments.add(wait_receive);// 待接单
        fragments.add(wait_pay);// 待付款
        fragments.add(wait_confirm);// 待收货
        fragments.add(refund);// 退款中
        fragments.add(cancel);// 取消
        fragments.add(complete);// 完成
        adapter = new CommonPageAdapter(this, tabs, fragments,
                getSupportFragmentManager(), viewPager, 0, true);
        adapter.setCustomMadeTab(mTabs_custom_made);
        viewPager.setAdapter(adapter);
        tabScrollView.setViewPager(viewPager);
        tabScrollView.setOnPageChangeListener(adapter);
        tabScrollView.outerClickForMove(index);
        EventBus.getDefault().register(this);
    }

    public View getTabItem(String s) {
        TextView tab = new TextView(this);
        tab.setPadding(0, 12, 0, 12);
        tab.setText(s);
        tab.setBackgroundColor(Color.TRANSPARENT);
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(this.getResources().getDimension(
                R.dimen.tab_text_size));
        tab.setTextColor(this.getResources().getColorStateList(
                R.color.tab_text_selector));
        return tab;
    }

    public View getTabItemWithRed(String s) {
        View view = View.inflate(this,
                R.layout.tab_text_red, null);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_name.setText(s);
        return view;
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_date_container).setOnClickListener(this);
        mTv_date = (TextView) findViewById(R.id.tv_date);
        Time time = new Time();
        time.setToNow();
        year = time.year;
        mouth = time.month + 1;
        mTv_date.setText(TimeUtil.timeFormat(System.currentTimeMillis(),
                "yyyy年MM月"));
        findViewById(R.id.rl_date_down).setOnClickListener(this);
        findViewById(R.id.rl_date_up).setOnClickListener(this);
        mTv_count = ((TextView) findViewById(R.id.tv_order_count));
        initTextHeadRigth(R.string.query, new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectTimePop == null) {
                    selectTimePop = new SelectTimePop(
                            OrderAcountActivity.this,R.layout.act_order_acount,new SelectTimePop.OnSelectOKLisentner() {

                        @Override
                        public void selectOk(int years, int months) {
                            year = years;
                            mouth = months;
                            changeDate(true);
                        }

                        @Override
                        public void selectAll() {
                            changeDate(false);
                        }
                    });
                }
                selectTimePop.showChooseWindow();

            }
        });

    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        getOrderCount();
        super.onResume();
    }

    private void getOrderCount() {
        RequestParams params = new RequestParams(ApiTrade.orderStatusCount());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("status", Order.STATE_WAIT_SELLER_CONFIRM + "");
        x.http().get(params, new WWXCallBack("OrderStatusCount") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<Integer> list = JSONArray.parseArray(data.getString("Data"), Integer.class);
                if (list.get(0) != 0) {
                    tv_count.setText((list.get(0) < 100) ? list.get(0) + "" : "99");
                    tv_count.setVisibility(View.VISIBLE);
                } else
                    tv_count.setVisibility(View.GONE);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private SelectTimePop selectTimePop;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date_container:

                break;
            case R.id.rl_date_down:
                if (mouth == 1) {
                    year--;
                    mouth = 12;
                } else {
                    mouth--;
                }
                changeDate(true);
                break;
            case R.id.rl_date_up:
                if (mouth == 12) {
                    year++;
                    mouth = 1;
                } else {
                    mouth++;
                }
                changeDate(true);
                break;
            default:
                break;
        }
    }

    private void changeDate(boolean withTime) {
        date = year + (mouth > 9 ? "" + mouth : "0" + mouth);
        mTv_date.setText(year + "年" + mouth + "月");
        ZLog.showPost(date + "---" + withTime);
        for (OrderFragment fragment : fragments) {
            fragment.changeDate(date, withTime);
        }
        adapter.getCurrentFragment().onRefresh();// 手动刷新
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void OrderEvent(OrderEvent event) {
        if (mTv_count != null) {
            mTv_count.setText(getString(R.string.format_total_count, event.data));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
