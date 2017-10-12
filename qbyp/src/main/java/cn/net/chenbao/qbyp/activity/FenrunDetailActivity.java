package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.FenrunFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.SelectTimePop;
import cn.net.chenbao.qbyp.view.TabScrollView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

public class FenrunDetailActivity extends FatherActivity implements
		OnClickListener {
	/**
	 * 粉丝
	 */
	public final static int ONE = 1;
	/**
	 * 代理
	 */
	public final static int TWO = 2;
	/**
	 * 推荐
	 */
	public final static int THREE = 3;
	private LinearLayout ll_time_choose;
	private TextView tv_time;
	/**
	 * 选择时间
	 */
	private int year;
	private int month;
	private TabScrollView tabScrollView;
	private FenrunFragment fenrunFragmentOne, fenrunFragmentTwo,
			fenrunFragmentThree;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_fenrun_detail;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.fenrun_detail, true);

	}

	@Override
	protected void initView() {
		ll_time_choose = (LinearLayout) findViewById(R.id.ll_time_choose);
		tv_time = (TextView) findViewById(R.id.tv_time);
		ll_time_choose.setOnClickListener(this);
		findViewById(R.id.iv_left).setOnClickListener(this);
		findViewById(R.id.iv_right).setOnClickListener(this);
		tabScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		String[] array = getResources().getStringArray(R.array.fenrun_tab);
		List<String> tabs = Arrays.asList(array);
		List<FatherFragment> fragments = new ArrayList<FatherFragment>();
		fenrunFragmentOne = new FenrunFragment();
		fenrunFragmentOne.setArguments(createBundle(Consts.KEY_MODULE, ONE));
		fragments.add(fenrunFragmentOne);
		fenrunFragmentTwo = new FenrunFragment();
		fenrunFragmentTwo.setArguments(createBundle(Consts.KEY_MODULE, TWO));
		fragments.add(fenrunFragmentTwo);
		fenrunFragmentThree = new FenrunFragment();
		fenrunFragmentThree
				.setArguments(createBundle(Consts.KEY_MODULE, THREE));
		fragments.add(fenrunFragmentThree);
		CommonPageAdapter adapter = new CommonPageAdapter(this, tabs,
				fragments, getSupportFragmentManager(), viewPager, 0, false);
		viewPager.setAdapter(adapter);
		tabScrollView.setViewPager(viewPager);
		tabScrollView.smoothScrollToPosition(getIntent().getIntExtra(
				Consts.KEY_MODULE, 0) - 1);
	}

	public static Bundle createBundle(String tabkey, int tab) {
		Bundle bundle = new Bundle();
		bundle.putInt(tabkey, tab);
		return bundle;
	}

	@Override
	protected void doOperate() {
		Time time = new Time("GMT+8");
		time.setToNow();
		year = time.year;
		month = time.month + 1;
		setTimeUI(year, month);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ll_time_choose:
			SelectTimePop selectTimePop = new SelectTimePop(
					FenrunDetailActivity.this, R.layout.act_person_public_list,
					new SelectTimePop.OnSelectOKLisentner() {

						@Override
						public void selectOk(int years, int months) {
							year = years;
							month = months;
							setTimeUI(year, month);
							onRefresh(false);
						}

						@Override
						public void selectAll() {
							year = 0;
							month = 0;
							onRefresh(true);
						}
					});
			selectTimePop.showChooseWindow();
			break;

		case R.id.iv_left:
			if (!TimeUtil.isFastClick()) {
				setTime(true);
				onRefresh(true);
			} else {

				WWToast.showShort(R.string.operation_too_fast);
			}
			break;
		case R.id.iv_right:
			if (!TimeUtil.isFastClick()) {
				setTime(false);
				onRefresh(false);
			} else {
				WWToast.showShort(R.string.operation_too_fast);
			}
			break;

		default:
			break;
		}

	}

	protected void onRefresh(boolean isAll) {
		// 像个办法刷新fragment
		switch (tabScrollView.getCurrentItem()) {
		case 0:
			fenrunFragmentOne.onRefresh(year, month,isAll);
			break;
		case 1:
			fenrunFragmentTwo.onRefresh(year, month,isAll);
			break;
		case 2:
			fenrunFragmentThree.onRefresh(year, month,isAll);
			break;
		}
	}

	/**
	 * 设置时间
	 *
	 * @param b
	 *            true减
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
