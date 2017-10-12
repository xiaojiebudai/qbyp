package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.GoodsCollectFragment;
import cn.net.chenbao.qbyp.fragment.ShopCollectFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.view.TabScrollView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:收藏列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-30
 */
public class CollectActivity extends FatherActivity  {
	private ShopCollectFragment shopCollectFragment;
	private GoodsCollectFragment goodsCollectFragment;
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
		// TODO Auto-generated method stub
		return R.layout.act_collect;
	}
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
				if(intent.getBooleanExtra("isgoods",false)){
					((TextView)mTabs_custom_made[1]).setText(tabs.get(1)+"("+intent.getStringExtra(Consts.KEY_DATA)+")");
				}else{
					((TextView)mTabs_custom_made[0]).setText(tabs.get(0)+"("+intent.getStringExtra(Consts.KEY_DATA)+")");
				}

		}
	};
	@Override
	protected void initValues() {
		initDefautHead(R.string.collect, true);
		IntentFilter filter = new IntentFilter("collect_num");
		registerReceiver(receiver, filter);
	}
	private View[] mTabs_custom_made;
	private 	List<String> tabs;
	@Override
	protected void initView() {
		mFragments = new ArrayList<Fragment>();
		mScrollView= (TabScrollView) findViewById(R.id.tabscrollview_tabs);
		List<FatherFragment> fragments = new ArrayList<FatherFragment>();
		String[] array = getResources().getStringArray(
				R.array.collect_tab_name);
		 tabs = Arrays.asList(array);
		mTabs_custom_made=new View[2];
		for (int i = 0; i < tabs.size(); i++) {
			mTabs_custom_made[i]=getTabItem(tabs.get(i));
		}
		shopCollectFragment = new ShopCollectFragment();
		Bundle bundle = new Bundle();
		shopCollectFragment.setArguments(bundle);
		goodsCollectFragment = new GoodsCollectFragment();
		goodsCollectFragment.setArguments(bundle);
		fragments.add(shopCollectFragment);
		fragments.add(goodsCollectFragment);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		CommonPageAdapter adapter = new CommonPageAdapter(
				CollectActivity.this, tabs, fragments,
				getSupportFragmentManager(), viewPager, 0, false);
		adapter.setCustomMadeTab(mTabs_custom_made);
		viewPager.setAdapter(adapter);
		mScrollView.setViewPager(viewPager);
	}
	public TextView getTabItem(String s) {
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
	@Override
	protected void doOperate() {

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
