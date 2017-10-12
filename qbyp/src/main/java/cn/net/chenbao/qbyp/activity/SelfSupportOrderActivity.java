package cn.net.chenbao.qbyp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.SelfSupportOrderItemFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.view.TabScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ppsher on 2016/12/27.
 * Description: 导航头 +viewpager 分类状态  (全部-待处理-其他)
 */

public class SelfSupportOrderActivity extends FatherActivity {
    private SelfSupportOrderItemFragment selfSupportOrderItemFragment0;
    private SelfSupportOrderItemFragment selfSupportOrderItemFragment1;
    private SelfSupportOrderItemFragment selfSupportOrderItemFragment2;
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
    private View[] mTabs_custom_made;
    private List<String> tabs;
    private TextView tv_count;

    private int model;
    /**
     * 修改tab
     */
    public final static int EDIT = 0;
    /**
     * 刷新页面
     */
    public final static int REFRESH = 1;
    private boolean isFirst = true;
    private ViewPager viewPager;
    private CommonPageAdapter adapter;
    private int status;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(Consts.KEY_MODULE, -1) == EDIT) {
                if (intent.getIntExtra(Consts.KEY_DATA, 0) > 0) {
                    tv_count.setVisibility(View.VISIBLE);
                    tv_count.setText(intent.getIntExtra(Consts.KEY_DATA, 0) + "");
                } else {
                    tv_count.setVisibility(View.GONE);
                }
            } else if (intent.getIntExtra(Consts.KEY_MODULE, -1) == REFRESH) {
                doRequest();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.frag_selfsupport_order;
    }

    @Override
    protected void initValues() {
        TextView center = (TextView) findViewById(R.id.tv_head_center);
        center.setText(R.string.order);
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        View left = findViewById(R.id.rl_head_left);
        left.findViewById(R.id.tv_head_left).setBackgroundResource(
                R.drawable.arrow_back);
        left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initView() {
        status = getIntent().getIntExtra(Consts.KEY_DATA, -1);
        mFragments = new ArrayList<Fragment>();
        mScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
        List<FatherFragment> fragments = new ArrayList<FatherFragment>();
        String[] array = getResources().getStringArray(
                R.array.self_order_name);
        tabs = Arrays.asList(array);
        mTabs_custom_made = new View[3];
        for (int i = 0; i < tabs.size(); i++) {
            if (i == 1) {
                mTabs_custom_made[i] = getTabItemWithRed(tabs.get(i));
            } else {
                mTabs_custom_made[i] = getTabItem(tabs.get(i));
            }
        }
        selfSupportOrderItemFragment0 = new SelfSupportOrderItemFragment();
        selfSupportOrderItemFragment1 = new SelfSupportOrderItemFragment();
        selfSupportOrderItemFragment2 = new SelfSupportOrderItemFragment();
        Bundle bundle0 = new Bundle();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle0.putInt(Consts.KEY_MODULE, SelfSupportOrderItemFragment.MODE_ALL);
        bundle1.putInt(Consts.KEY_MODULE, SelfSupportOrderItemFragment.MODE_PREDEL);
        bundle2.putInt(Consts.KEY_MODULE, SelfSupportOrderItemFragment.MODE_OTHER);
        selfSupportOrderItemFragment0.setArguments(bundle0);
        selfSupportOrderItemFragment1.setArguments(bundle1);
        selfSupportOrderItemFragment2.setArguments(bundle2);
        fragments.add(selfSupportOrderItemFragment0);
        fragments.add(selfSupportOrderItemFragment1);
        fragments.add(selfSupportOrderItemFragment2);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CommonPageAdapter(
                this, tabs, fragments,
                getSupportFragmentManager(), viewPager, 0, true);
        adapter.setCustomMadeTab(mTabs_custom_made);
        viewPager.setOnPageChangeListener(adapter);
        viewPager.setAdapter(adapter);
        mScrollView.setViewPager(viewPager);
        IntentFilter filter = new IntentFilter("order_self");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void doOperate() {

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            selfSupportOrderItemFragment0.onActivityResult(requestCode, resultCode, data);
            selfSupportOrderItemFragment1.onActivityResult(requestCode, resultCode, data);
            selfSupportOrderItemFragment2.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void doRequest() {
        if (selfSupportOrderItemFragment0 != null) {
            selfSupportOrderItemFragment0.setCuttertPager(0);
            selfSupportOrderItemFragment0.doRequest();
        }
        if (selfSupportOrderItemFragment1 != null) {
            selfSupportOrderItemFragment1.setCuttertPager(0);
            selfSupportOrderItemFragment1.doRequest();
        }
        if (selfSupportOrderItemFragment2 != null) {
            selfSupportOrderItemFragment2.setCuttertPager(0);
            selfSupportOrderItemFragment2.doRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
