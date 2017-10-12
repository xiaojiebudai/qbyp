package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.SelfSupportGoodsDetailFragment;
import cn.net.chenbao.qbyp.fragment.SelfSupportGoodsDetailWebFragment;
import cn.net.chenbao.qbyp.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城商品详情
 */

public class SelfSupportGoodsDetailActivity extends FatherActivity {
    @BindView(R.id.rl_head_back)
    RelativeLayout rlHeadBack;
    @BindView(R.id.rl_fragment)
    RelativeLayout rlFragment;
    @BindView(R.id.view_shop_tab)
    View viewShopTab;
    @BindView(R.id.view_detail_tab)
    View viewDetailTab;


    private SelfSupportGoodsDetailFragment selfSupportFragment;
    private SelfSupportGoodsDetailWebFragment supportGoodsDetailWebFragment;
    private View[] mTabs;
    private FatherFragment[] mFragments;
    /**
     * 当前的选中的tab
     */
    private int mCurrentTabIndex;
    private long ProductId;


    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_goods_detail;
    }

    @Override
    protected void initValues() {
        ProductId = getIntent().getLongExtra(Consts.KEY_DATA, 0);
    }

    @Override
    protected void initView() {
        selfSupportFragment = new SelfSupportGoodsDetailFragment();
        supportGoodsDetailWebFragment = new SelfSupportGoodsDetailWebFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(Consts.KEY_DATA, ProductId);
        selfSupportFragment.setArguments(bundle);
        supportGoodsDetailWebFragment.setArguments(bundle);

        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.add(R.id.rl_fragment, selfSupportFragment)
                .add(R.id.rl_fragment, supportGoodsDetailWebFragment).hide(supportGoodsDetailWebFragment)
                .show(selfSupportFragment).commit();

        mFragments = new FatherFragment[]{selfSupportFragment, supportGoodsDetailWebFragment};
        mTabs = new View[2];
        mTabs[0] = findViewById(R.id.tv_head_center_left);
        mTabs[1] = findViewById(R.id.tv_head_center_right);
        mTabs[0].setSelected(true);
        viewShopTab.setVisibility(View.VISIBLE);
    }

    @Override
    protected void doOperate() {

    }


    @OnClick({R.id.rl_head_back, R.id.tv_head_center_left, R.id.tv_head_center_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_head_back:
                finish();
                break;
            case R.id.tv_head_center_left:
                viewShopTab.setVisibility(View.VISIBLE);
                viewDetailTab.setVisibility(View.INVISIBLE);
                setSelectTab(0);
                break;
            case R.id.tv_head_center_right:
                viewDetailTab.setVisibility(View.VISIBLE);
                viewShopTab.setVisibility(View.INVISIBLE);
                setSelectTab(1);
                break;
        }
    }


    private void setSelectTab(int select) {
        if (mCurrentTabIndex != select) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[select].isAdded()) {
                trx.add(R.id.rl_fragment, mFragments[select]);
            }
            trx.show(mFragments[select]).commit();
        }
        mTabs[mCurrentTabIndex].setSelected(false);
        mTabs[select].setSelected(true);
        mCurrentTabIndex = select;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
