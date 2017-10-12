package cn.net.chenbao.qbypseller.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.AccountInfo;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by 木头 on 2016/11/22.
 */

public class WalletActivity extends FatherActivity implements View.OnClickListener {
    private TextView tv_yue, tv_can_pay, tv_yujifanli, tv_daijiedong, tv_jiedongsudu, tv_jien;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.wallet, true);
    }

    @Override
    protected void initView() {
        findViewById(R.id.ll_bank_card).setOnClickListener(this);
        findViewById(R.id.ll_xiaoshoue).setOnClickListener(this);
        findViewById(R.id.ll_can_withdraw).setOnClickListener(this);
        findViewById(R.id.ll_yusuanjine).setOnClickListener(this);
        findViewById(R.id.withdraw).setOnClickListener(this);
        findViewById(R.id.withdraw_des).setOnClickListener(this);
        findViewById(R.id.ll_jifen).setOnClickListener(this);
        tv_yue = (TextView) findViewById(R.id.tv_yue);
        tv_can_pay = (TextView) findViewById(R.id.tv_can_pay);
        tv_yujifanli = (TextView) findViewById(R.id.tv_yujifanli);
        tv_daijiedong = (TextView) findViewById(R.id.tv_daijiedong);
        tv_jiedongsudu = (TextView) findViewById(R.id.tv_jiedongsudu);
        tv_jien = (TextView) findViewById(R.id.tv_jien);
        getMoneyNum();
    }

    @Override
    protected void doOperate() {

    }

    /**
     * 获取用户金额
     */
    private AccountInfo accountInfo;

    private void getMoneyNum() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.getAccountInfo());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("AccountGet") {
            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                accountInfo = JSON.parseObject(data.getString("Data"),
                        AccountInfo.class);
                // 设置数据
                tv_can_pay.setText(WWViewUtil.numberFormatPrice(accountInfo.Balance));
                tv_yujifanli.setText(WWViewUtil.numberFormatPrice(accountInfo.Account));
                tv_daijiedong.setText(String.format(getString(R.string.wait_freeze), WWViewUtil.numberFormatWithTwo(accountInfo.IntegralUndeblock)));
                tv_jiedongsudu.setText(String.format(getString(R.string.freeze_rate), WWViewUtil.numberFormatWithTwo(accountInfo.DeblockRate * 100)));
                tv_jien.setText(WWViewUtil.numberFormatWithTwo(accountInfo.Consume));
            }
        });
        RequestParams params1 = new RequestParams(ApiSeller.OrderCurMonthCount());
        params1.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params1, new WWXCallBack("OrderCurMonthCount") {
            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // 设置数据
                tv_yue.setText("¥" + data.getString("TotalAmount2"));
            }
        });
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 8888 && arg1 == RESULT_OK) {
            getMoneyNum();
        }
    }

    @Override
    public void onClick(View v) {
        // 有关数据的回来需要刷新
        switch (v.getId()) {
            case R.id.ll_bank_card:
                startActivity(new Intent(this, BankListActivity.class));
                break;
            case R.id.ll_xiaoshoue:
                PublicWay.startPersonPublicListDesActivity(this,
                        PersonPublicListDesActivity.XIAOSHOUEDES, 0);
                break;
            case R.id.ll_can_withdraw:
                // 可提现就是余额？
                PublicWay.startPersonPublicListDesActivity(this,
                        PersonPublicListDesActivity.YUEDES, 0);
                break;
            case R.id.ll_yusuanjine:
                PublicWay.startPersonPublicListDesActivity(this,
                        PersonPublicListDesActivity.YUJIESUANDES, 0);
                break;
            case R.id.withdraw:
                Intent intent = new Intent(this,
                        WithdrawActivity.class);
                this.startActivityForResult(intent, 8888);
                break;
            case R.id.withdraw_des:
                PublicWay.startPersonPublicListDesActivity(this,
                        PersonPublicListDesActivity.TIXIANDES, 0);
                break;
            case R.id.ll_jifen:
                PublicWay.startPersonPublicListDesActivity(this,
                        PersonPublicListDesActivity.JIFEN, 0);
                break;

            default:
                break;
        }

    }
}
