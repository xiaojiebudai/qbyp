package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.chenbao.qbyp.R;

/**
 * Created by Administrator on 2017/1/3.
 */

public class RechargeResultActivity extends FatherActivity {
    @BindView(R.id.tv_shopping)
    TextView tvShopping;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.ll_success)
    LinearLayout llSuccess;
    @BindView(R.id.tv_pay_again)
    TextView tvPayAgain;
    @BindView(R.id.ll_fail)
    LinearLayout llFail;
    public static final int SUCCESS = 1;
    public static final int FAILE = 2;
    public int mode;
    @Override
    protected int getLayoutId() {
        return R.layout.act_recharge_result;
    }

    @Override
    protected void initValues() {
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        switch (mode) {
            case SUCCESS:// 成功
                llSuccess.setVisibility(View.VISIBLE);
                break;
            case FAILE:// 失敗
                llFail.setVisibility(View.VISIBLE);
                break;
        }
        initDefautHead(R.string.pay_result, false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_shopping, R.id.tv_wallet, R.id.tv_pay_again})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_shopping:
                setResult(RESULT_OK);
                startActivity(new Intent(this, SelfSupportActivity.class));
                finish();
                break;
            case R.id.tv_wallet:
                setResult(RESULT_OK);
                startActivity(new Intent(this, MyWalletActivity.class));
                finish();
                break;
            case R.id.tv_pay_again:
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }
}
