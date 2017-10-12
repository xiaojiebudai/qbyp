package cn.net.chenbao.qbyp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.fragment.SelfSupportOrderFragment;
import cn.net.chenbao.qbyp.fragment.ShopCartFragment;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.SelfSupportFragment;
import cn.net.chenbao.qbyp.utils.Consts;

/**
 * Created by 木头 on 2016/11/23.(幸福专区)
 */

public class SelfSupportMainActivity extends FatherActivity {

    private View[] mTabs;
    private FatherFragment[] mFragments;

    private SelfSupportFragment selfSupportFragment;
    private SelfSupportOrderFragment mOrderFragment;
    private ShopCartFragment shopCartFragment;

    public final static int TAB_HOMEPAGE = 0;
    public final static int TAB_SELF = 1;
    public final static int TAB_ORDER = 2;
    public final static int TAB_PERSONCENTER = 3;

    /***
     * 刷新订单列表
     */
    public static final String ORDER_LIST_REFRESH = "self_order_list_refresh";

    /**
     * 欲选中tab
     */
    private int mIndex;
    /**
     * 当前的选中的tab
     */
    private int mCurrentTabIndex;
    private boolean doRefresh;
    private int goosType;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            doRefresh = true;
            goosType = arg1.getIntExtra(Consts.KEY_MODULE, 0);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_self_main;
    }

    @Override
    protected void initValues() {
        IntentFilter intent = new IntentFilter(ORDER_LIST_REFRESH);
        registerReceiver(receiver, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (doRefresh) {
            mOrderFragment.doRequest();
            mIndex = TAB_ORDER;
            setSelectTab();
            doRefresh = false;
        }
//		selfSupportFragment.getCartSum();
    }

    @Override
    protected void initView() {
        // mHomePageFragment = new HomePageFragment();
        selfSupportFragment = new SelfSupportFragment();
        mOrderFragment = new SelfSupportOrderFragment();
        shopCartFragment = new ShopCartFragment();
        mFragments = new FatherFragment[]{selfSupportFragment, shopCartFragment, mOrderFragment};

        mTabs = new View[4];
        mTabs[TAB_HOMEPAGE] = findViewById(R.id.rl_tab_homepage);
        mTabs[TAB_SELF] = findViewById(R.id.rl_tab_selfsupport);
        mTabs[TAB_ORDER] = findViewById(R.id.rl_tab_order);
        mTabs[TAB_PERSONCENTER] = findViewById(R.id.rl_tab_person_center);
        mIndex = getIntent().getIntExtra(Consts.KEY_DATA, 0);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_container, selfSupportFragment)
                .add(R.id.rl_container, shopCartFragment).hide(shopCartFragment)
                .show(selfSupportFragment).commit();
        mTabs[mIndex].setSelected(true);
        switch (mIndex) {
            case TAB_HOMEPAGE:
                onTabClick(findViewById(R.id.rl_tab_homepage));
                break;
            case TAB_SELF:
                onTabClick(findViewById(R.id.rl_tab_selfsupport));
                break;
            case TAB_ORDER:
                onTabClick(findViewById(R.id.rl_tab_order));
                break;
        }
    }

    @Override
    protected void doOperate() {
    }

    /**
     * 底部tab点击事件
     *
     * @param view
     * @author xl
     * @date:2016-7-25下午1:00:06
     * @description
     */
    public void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.rl_tab_homepage:
                mIndex = TAB_HOMEPAGE;
                break;
            case R.id.rl_tab_order:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                mOrderFragment.doRequest();
                mIndex = TAB_ORDER;
                break;
            case R.id.rl_tab_person_center:
                Intent intent = new Intent(this,
                        MainActivity.class);
                intent.putExtra("personCenterSelect", true);
                startActivity(intent);
                finish();
                break;
            case R.id.rl_tab_selfsupport:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                shopCartFragment.doRequest();
                mIndex = TAB_SELF;
                break;
            default:
                break;
        }
        setSelectTab();

    }

    private void setSelectTab() {
        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.rl_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }
        mTabs[mCurrentTabIndex].setSelected(false);
        mTabs[mIndex].setSelected(true);
        mCurrentTabIndex = mIndex;
    }

}

