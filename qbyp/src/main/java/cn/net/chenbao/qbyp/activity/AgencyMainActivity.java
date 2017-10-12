package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.fragment.AgencyOnlyFragment;
import cn.net.chenbao.qbyp.fragment.AgencyShopFragment;
import cn.net.chenbao.qbyp.fragment.AgencyWithdrawFragment;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.view.CustomToast;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

/**
 * 开工
 * 
 * @author xl
 * @date:2016-7-24下午3:14:45
 * @description
 */
public class AgencyMainActivity extends FatherActivity {

	private View[] mTabs;
	private FatherFragment[] mFragments;

	private AgencyOnlyFragment agencyOnlyFragment;
	private AgencyWithdrawFragment agencyWithdrawFragment;
	private AgencyShopFragment agencyShopFragment;

	private final static int TAB_ONLY = 0;
	private final static int TAB_WITHDRAW = 1;
	private final static int TAB_SHOP = 2;

	/** 欲选中tab */
	private int mIndex;
	/** 当前的选中的tab */
	private int mCurrentTabIndex;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_agency_main;
	}

	@Override
	protected void initValues() {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void initView() {
		// mHomePageFragment = new HomePageFragment();
		agencyOnlyFragment = new AgencyOnlyFragment();
		agencyWithdrawFragment = new AgencyWithdrawFragment();
		agencyShopFragment = new AgencyShopFragment();
		mFragments = new FatherFragment[] { agencyOnlyFragment,
				agencyWithdrawFragment, agencyShopFragment };
		getSupportFragmentManager().beginTransaction()
				.add(R.id.rl_container, agencyOnlyFragment)
				.add(R.id.rl_container, agencyWithdrawFragment)
				.hide(agencyWithdrawFragment).show(agencyOnlyFragment).commit();
		mTabs = new View[4];
		mTabs[TAB_ONLY] = findViewById(R.id.rl_tab_only);
		mTabs[TAB_WITHDRAW] = findViewById(R.id.rl_tab_withdraw);
		mTabs[TAB_SHOP] = findViewById(R.id.rl_tab_shop);
		mTabs[TAB_ONLY].setSelected(true);
	}

	@Override
	protected void doOperate() {
	}

	/**
	 * 底部tab点击事件
	 * 
	 * @author xl
	 * @date:2016-7-25下午1:00:06
	 * @description
	 * @param view
	 */
	public void onTabClick(View view) {
		switch (view.getId()) {
		case R.id.rl_tab_only:
			mIndex = TAB_ONLY;
			break;
		case R.id.rl_tab_withdraw:
			mIndex = TAB_WITHDRAW;
			break;
		case R.id.rl_tab_shop:
			mIndex = TAB_SHOP;
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

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		CustomToast.removeWindow();
		finish();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
	}
}
