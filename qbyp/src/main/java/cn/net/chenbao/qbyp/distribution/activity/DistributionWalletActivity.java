package cn.net.chenbao.qbyp.distribution.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.IdentityAuthenticationResultActivity;
import cn.net.chenbao.qbyp.activity.WithdrawActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.UserReal;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 我的收益
 */

public class DistributionWalletActivity extends FatherActivity {
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    @BindView(R.id.fake_status_bar1)
    View fake_status_bar1;
    @BindView(R.id.tv_money_all)
    TextView tvMoneyAll;
    @BindView(R.id.tv_ub)
    TextView tvUb;
    @BindView(R.id.ll_ub)
    LinearLayout llUb;
    @BindView(R.id.ll_withdraw)
    LinearLayout llWithdraw;
    @BindView(R.id.ll_withdraw_des)
    LinearLayout llWithdrawDes;
    @BindView(R.id.ll_jifen_des)
    LinearLayout llJifenDes;
    @BindView(R.id.tv_base_rate)
    TextView tvBaseRate;
    @BindView(R.id.tv_money_wait)
    TextView tvMoneyWait;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_yijijifen)
    TextView tvYijijifen;
    //    @BindView(R.id.tv_yijijiasu)
//    TextView tvYijijiasu;
    @BindView(R.id.tv_erjijifen)
    TextView tvErjijifen;
    //    @BindView(R.id.tv_erjijiasu)
//    TextView tvErjijiasu;
    @BindView(R.id.ll_freeze)
    LinearLayout ll_freeze;
    @BindView(R.id.ll_1ji)
    LinearLayout ll_1ji;
    @BindView(R.id.ll_2ji)
    LinearLayout ll_2ji;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_wallet;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.distribution_wallet, true);
    }

    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fakeStatusBar.getLayoutParams();
            params1.height = WWViewUtil.getStatusBarHeight(this);
            fakeStatusBar.setLayoutParams(params1);
            fakeStatusBar.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) fake_status_bar1.getLayoutParams();
            params.height = WWViewUtil.getStatusBarHeight(this);
            fake_status_bar1.setLayoutParams(params);
            fake_status_bar1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private DistributionPublicAccount accountInfo;

    private void getData() {
        realGet();
        showWaitDialog();
        RequestParams params = new RequestParams(ApiDistribution.MyAccount());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("MyAccount") {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                accountInfo = JSON.parseObject(data.getString("Data"),
                        DistributionPublicAccount.class);
                // 设置数据
                tvMoneyAll.setText(WWViewUtil.numberFormatPrice(accountInfo.Account));
                tvUb.setText(WWViewUtil.numberFormatWithTwo(accountInfo.ConsumeBalance));
                tvBaseRate.setText(WWViewUtil.numberFormatWithTwo(accountInfo.FrozenRate * 100) + "%");
                tvMoneyWait.setText(WWViewUtil.numberFormatWithTwo(accountInfo.ConsumeUnfrozen));
                tvMoney.setText(WWViewUtil.numberFormatWithTwo(accountInfo.ConsumeFrozen));
//                tvYijijiasu.setText("加速" + WWViewUtil.numberFormatWithTwo(accountInfo.SpeedRate1*100)+"%");
//                tvErjijiasu.setText("加速" + WWViewUtil.numberFormatWithTwo(accountInfo.SpeedRate2*100)+"%");
            }
        });
    }

    private UserReal userReal;

    private void realGet() {

        RequestParams params = new RequestParams(ApiUser.RealGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("RealGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // TODO Auto-generated method stub
                // 0 待提交，1待审核，2审核通过，3审核不通过
                userReal = JSON.parseObject(data.getString("Data"), UserReal.class);
            }

            @Override
            public void onAfterFinished() {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_money_all, R.id.ll_ub, R.id.ll_withdraw, R.id.ll_withdraw_des, R.id.ll_jifen_des, R.id.ll_freeze, R.id.ll_1ji, R.id.ll_2ji})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_money_all:
                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
                        DistributionPublicListActivity.GOODSPAYMENT);
                break;
            case R.id.ll_ub:
                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
                        DistributionPublicListActivity.UB);
                break;
            case R.id.ll_withdraw:
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        showIdentityDialog("实名认证进行中，查看进度？", "去查看");
                    } else if (userReal.Status == 3) {
                        showIdentityDialog("实名认证失败，查看结果？", "去查看");
                    } else {
                        PublicWay.stratWithdrawActivity(DistributionWalletActivity.this, 0, WithdrawActivity.DISTRIBUTION_WITHDRAW, 8888);
                    }
                } else {
                    showIdentityDialog("还未进行实名认证", "去认证");
                }
                break;
            case R.id.ll_withdraw_des:
                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
                        DistributionPublicListActivity.WITHDRAW);
                break;
            case R.id.ll_jifen_des:
                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
                        DistributionPublicListActivity.INTEGRAL);
                break;
            case R.id.ll_freeze:
                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
                        DistributionPublicListActivity.ConsumePool);
                break;
//            case R.id.ll_1ji:
//                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
//                        DistributionPublicListActivity.PoolLevelOne);
//                break;
//            case R.id.ll_2ji:
//                PublicWay.startDistributionPublicListActivity(DistributionWalletActivity.this,
//                        DistributionPublicListActivity.PoolLevelTwo);
//                break;

        }
    }

    private void showIdentityDialog(String tipsStr, String confirmRight) {
        final CommonDialog commonDialog = DialogUtils.getCommonDialogTwiceConfirm(this, tipsStr, true);
        commonDialog.getButtonRight(confirmRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                DistributionWalletActivity.this,
                                IdentityAuthenticationResultActivity.IDENTITYING,
                                "");
                    } else if (userReal.Status == 3) {
                        PublicWay
                                .startIdentityAuthenticationResultActivity(
                                        DistributionWalletActivity.this,
                                        IdentityAuthenticationResultActivity.IDENTITYDEFAULT,
                                        userReal.Explain);
                    }
                } else {
                    PublicWay.stratIdentityAuthenticationActivity(DistributionWalletActivity.this);
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

}
