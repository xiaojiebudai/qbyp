package cn.net.chenbao.qbyp.distribution.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.fragment.DistributionOrderFragment;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.view.TabScrollView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 订单（进货/销货）
 */

public class DistributionOrderActivity extends FatherActivity {
    public static final String IS_REFRESH = "isRefresh";
    /**
     * 滑动标签栏
     */
    private TabScrollView mScrollView;
    /**
     * 订单ViewPager
     */
    private ViewPager mPager;

    private ArrayList<Fragment> mFragments;

    /**
     * fragment适配器
     */
    private CommonPageAdapter mAdapter;

    private int model;
    /**
     * 进货
     */
    public static final int IN = 0;
    /**
     * 销货
     */
    public static final int OUT = 1;
    //进出都有三个不同的tab
    private int flag;
    /**
     * 全部
     */
    public static final int ALL = 0;
    /**
     * 处理中
     */
    public static final int DELLING = 1;
    /**
     * 已处理
     */
    public static final int FINISH = 2;

    private DistributionOrderFragment fragment0, fragment1, fragment2;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //刷新数量，同时刷新列表
            getMyOrdersCountBuy();

            fragment0.refreshData();
            fragment1.refreshData();
            if(fragment2.isAdded()){
                fragment2.refreshData();
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_order;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, IN);
        initDefautHead(model == IN ? R.string.distribution_order_in : R.string.distribution_order_out, true);
        IntentFilter filter = new IntentFilter(IS_REFRESH);
        registerReceiver(receiver, filter);
    }

    private View[] mTabs_custom_made;
    private List<String> tabs;

    @Override
    protected void initView() {
        mFragments = new ArrayList<Fragment>();
        mScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
        List<FatherFragment> fragments = new ArrayList<FatherFragment>();
        String[] array = getResources().getStringArray(
                R.array.distribution_order_state);
        tabs = Arrays.asList(array);
        mTabs_custom_made = new View[3];
        for (int i = 0; i < tabs.size(); i++) {
            mTabs_custom_made[i] = getTabItem(tabs.get(i));
        }
        fragment0 = DistributionOrderFragment.newInstance(model, ALL);
        fragment1 = DistributionOrderFragment.newInstance(model, DELLING);
        fragment2 = DistributionOrderFragment.newInstance(model, FINISH);
        fragments.add(fragment0);
        fragments.add(fragment1);
        fragments.add(fragment2);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        CommonPageAdapter adapter = new CommonPageAdapter(
                DistributionOrderActivity.this, tabs, fragments,
                getSupportFragmentManager(), viewPager, 0, false);
        adapter.setCustomMadeTab(mTabs_custom_made);
        viewPager.setAdapter(adapter);
        mScrollView.setViewPager(viewPager);
        getMyOrdersCountBuy();
    }

    private void getMyOrdersCountBuy() {

        RequestParams params = new RequestParams(model == IN ? ApiDistribution.MyOrdersCountBuy() : ApiDistribution.MyOrdersCountSeller());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("status", "0,1,5,2");

        x.http().get(params, new WWXCallBack(model == IN ? "MyOrdersCountBuy" : "MyOrdersCountSeller") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                int number = data.getIntValue("Data");

                if (mTabs_custom_made[1] != null) {
                    TextView pointView = (TextView) mTabs_custom_made[1].findViewById(R.id.tv_red_point);
                    if (number > 0) {
                        pointView.setVisibility(View.VISIBLE);
                        pointView.setText(number > 99 ? "99" : number + "");
                    } else {
                        pointView.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onAfterFinished() {
            }
        });
    }

    public View getTabItem(String s) {

        View tab = getLayoutInflater().inflate(R.layout.tab_red_point,
                null);
        ((TextView) tab.findViewById(R.id.tv_name))
                .setText(s);
        return tab;
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
