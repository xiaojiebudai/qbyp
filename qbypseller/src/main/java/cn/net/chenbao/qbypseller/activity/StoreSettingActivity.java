package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.fragment.FatherFragment;
import cn.net.chenbao.qbypseller.fragment.StoreAptitudeFragment;
import cn.net.chenbao.qbypseller.fragment.StoreInfoFragment;
import cn.net.chenbao.qbypseller.utils.Consts;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.net.chenbao.qbypseller.R;

/**
 * 店铺设置
 * 
 * @author xl
 * @date 2016-7-31 下午8:00:35
 * @description
 */
public class StoreSettingActivity extends FatherActivity {

	/** 店铺信息 */
	public static final int MODULE_STORE_INFO = 0;
	/** 店铺资质 */
	public static final int MODULE_STORE_RES = 1;

	private FatherFragment[] mFragments;
	private StoreInfoFragment mStoreInfoFragment;
	private StoreAptitudeFragment mStoreAptitudeFragment;

	@Override
	protected int getLayoutId() {
		return R.layout.act_store_setting;
	}

	@Override
	protected void initValues() {
		View left = findViewById(R.id.rl_head_left);
		if (left != null) {
			left.findViewById(R.id.tv_head_left).setBackgroundResource(
					R.drawable.arrow_back);
			left.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		mFragments = new FatherFragment[2];
		mStoreAptitudeFragment = new StoreAptitudeFragment();
		mStoreInfoFragment = new StoreInfoFragment();
		mFragments[MODULE_STORE_INFO] = mStoreInfoFragment;
		mFragments[MODULE_STORE_RES] = mStoreAptitudeFragment;

	}

	@Override
	protected void initView() {
		getSupportFragmentManager().beginTransaction()
				.add(R.id.rl_container, mStoreInfoFragment)
				.add(R.id.rl_container, mStoreAptitudeFragment).commit();
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_store_setting);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {// 设置监听
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.rb_store_info:// 店铺信息
							getSupportFragmentManager().beginTransaction()
									.show(mStoreInfoFragment)
									.hide(mStoreAptitudeFragment).commit();
							break;
						case R.id.rb_store_res:// 店铺资质
							getSupportFragmentManager().beginTransaction()
									.show(mStoreAptitudeFragment)
									.hide(mStoreInfoFragment).commit();
							break;
						default:
							break;
						}
					}
				});
		radioGroup.check(getIntent().getIntExtra(Consts.KEY_MODULE,
				MODULE_STORE_INFO) == MODULE_STORE_INFO ? R.id.rb_store_info
				: R.id.rb_store_res);// 初始选择
	}

	@Override
	protected void doOperate() {
		

	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
	}
}
