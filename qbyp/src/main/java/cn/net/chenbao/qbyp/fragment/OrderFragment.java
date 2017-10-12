package cn.net.chenbao.qbyp.fragment;

import cn.net.chenbao.qbyp.activity.PayOrdersActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * 订单
 * 
 * @author licheng
 * 
 */
public class OrderFragment extends FatherFragment implements
		OnCheckedChangeListener{

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_order;
	}

	private RadioGroup mRb;
	private SelfSupportOrderFragment supportOrderFragment;
	private  LocalOrderFragment localOrderFragment;
	private FatherFragment[] mFragments;
	/** 当前的选中的tab */
	private int mCurrentTabIndex;
	private View fake_status_bar;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	protected void initView() {
		mRb = (RadioGroup) mGroup.findViewById(R.id.rb);
		mRb.setOnCheckedChangeListener(this);
		RadioButton childAt = (RadioButton) mRb.getChildAt(0);
		childAt.setChecked(true);
		localOrderFragment = new LocalOrderFragment();
		supportOrderFragment = new SelfSupportOrderFragment();
		FragmentTransaction	trx=getChildFragmentManager().beginTransaction();

		trx .add(R.id.rl_fragment, localOrderFragment)
				.add(R.id.rl_fragment,supportOrderFragment).hide(supportOrderFragment)
				.show(localOrderFragment).commit();
		mFragments = new FatherFragment[] { localOrderFragment, supportOrderFragment};
		fake_status_bar = mGroup.findViewById(R.id.fake_status_bar);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

			ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fake_status_bar.getLayoutParams();
			params1.height = WWViewUtil.getStatusBarHeight(getActivity());
			fake_status_bar.setLayoutParams(params1);
			fake_status_bar.setVisibility(View.VISIBLE);

		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_left:// 本地
			setSelectTab(0);
			break;
		case R.id.rbtn_right:// 自营
			setSelectTab(1);
			break;
		default:
			break;
		}
	}
	private void setSelectTab(int select) {
		if (mCurrentTabIndex != select) {
			FragmentTransaction trx = getChildFragmentManager()
					.beginTransaction();
			trx.hide(mFragments[mCurrentTabIndex]);
			if (!mFragments[select].isAdded()) {
				trx.add(R.id.rl_fragment, mFragments[select]);
			}
			trx.show(mFragments[select]).commit();
		}
		mCurrentTabIndex = select;
	}
	public void doRequest() {
		if(localOrderFragment!=null){
			localOrderFragment.setCuttertPager(0);
			localOrderFragment.doRequest();

		}
		if(supportOrderFragment!=null){
			supportOrderFragment.doRequest();
		}
	}
	private int model=-1;
	public void doRequest(int model) {
		this.model=model;
		if(model== PayOrdersActivity.LOCAL){
			if(localOrderFragment!=null){
				localOrderFragment.setCuttertPager(0);
				localOrderFragment.doRequest();
			}
		}else{
			if(supportOrderFragment!=null){
				supportOrderFragment.doRequest();
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if(model>0)
		{
			if(model==PayOrdersActivity.LOCAL){
				if(!((RadioButton) mRb.getChildAt(0)).isChecked())
					((RadioButton) mRb.getChildAt(0)).setChecked(true);
			}else{
				if(!((RadioButton) mRb.getChildAt(1)).isChecked())
					((RadioButton) mRb.getChildAt(1)).setChecked(true);
			}
			model=-1;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		localOrderFragment.onActivityResult(requestCode, resultCode, data);
		supportOrderFragment.onActivityResult(requestCode, resultCode, data);
	}
}
