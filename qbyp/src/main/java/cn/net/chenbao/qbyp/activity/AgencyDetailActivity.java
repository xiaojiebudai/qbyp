package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.fragment.AgencyInfoFragment;
import cn.net.chenbao.qbyp.fragment.AgencyPersonInfoFragment;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.view.TabScrollView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/***
 * Description:区域明细 Company: jsh Version：1.0
 * 
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyDetailActivity extends FatherActivity {
	/** 区域信息 */
	public static final int MODULE_STORE_QUYU_INFO = 0;
	/** 代理人信息 */
	public static final int MODULE_STORE_DAILI_RES = 1;

	private AgencyInfoFragment agencyInfoFragment;
	private AgencyPersonInfoFragment agencyPersonInfoFragment;

	
	/**
	 * 滑动标签栏
	 */
	private TabScrollView mScrollView;
	/**
	 * 订单ViewPager
	 */
	private ViewPager mPager;

	private ArrayList<Fragment> mFragments;

	/** fragment适配器 */
	private CommonPageAdapter mAdapter;
	
	
	
	
	@Override
	protected int getLayoutId() {
		return R.layout.activity_agency_detail;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.agency_detail, true);
		mFragments = new ArrayList<Fragment>();
		TabScrollView tabScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
		List<FatherFragment> fragments = new ArrayList<FatherFragment>();
		String[] array = getResources().getStringArray(
				R.array.agency_tab_name);
		List<String> tabs = Arrays.asList(array);
		agencyInfoFragment = new AgencyInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(Consts.AGENT_ID, getIntent().getIntExtra(Consts.AGENT_ID, -1));
		agencyInfoFragment.setArguments(bundle);
		agencyPersonInfoFragment = new AgencyPersonInfoFragment();
		agencyPersonInfoFragment.setArguments(bundle);
		fragments.add(agencyInfoFragment);
		fragments.add(agencyPersonInfoFragment);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		CommonPageAdapter adapter = new CommonPageAdapter(
				AgencyDetailActivity.this, tabs, fragments,
				getSupportFragmentManager(), viewPager, 0, false);
		viewPager.setAdapter(adapter);
		tabScrollView.setViewPager(viewPager);
		
		
		
		
		
		
		

	}

	@Override
	protected void initView() {
	}


	@Override
	protected void doOperate() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
	}
}
