package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiPay;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.numberKeyboard.InputViewManager;
import cn.net.chenbao.qbyp.numberKeyboard.PasswordView;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

public class VerifyPasswordActivity extends FatherActivity implements
        OnClickListener {
    //TODO 区分模式  MODE_ORDER_PAY
    /**
     * 普通单纯输入模式
     */
    public static final int MODE_NOMAL = 0;
    /**
     * 订单支付模式
     */
    public static final int MODE_ORDER_PAY = 1;
    /**
     * 提现模式
     */
    public static final int MODE_WITH_DRAW = 2;
    /**
     * 代理提现模式
     */
    public static final int MODE_WITH_DRAW_AGENCY = 3;
    /**
     * 分销提现模式
     */
    public static final int MODE_WITH_DRAW_DISTRIBUTION = 4;
    public static final int REQUEST_CODE = 10086;

    /**
     *
     */
    private TextView tv_forgetpsw;
    private ImageView iv_del;
    private LinearLayout ll_commit;

    private long cardId;
    private String moneyCount;
    private String psw = "";
    private InputViewManager manager;

    private int mode;
    private String payWay;
    private long orderId;
    private long agentId;
    private LinearLayout llPay;
    private TextView mPayWay;
    private TextView mMoney;

    @Override
    protected int getLayoutId() {
        return R.layout.act_verify_password;
    }

    @Override
    protected void initValues() {
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        if (mode == MODE_ORDER_PAY) {
            payWay = getIntent().getStringExtra("payway");
            orderId = getIntent().getLongExtra("orderId", -1);
            moneyCount = getIntent().getStringExtra(
                    WithdrawActivity.WITHDRAW_COUNT);
        } else if (mode == MODE_WITH_DRAW || mode == MODE_WITH_DRAW_DISTRIBUTION) {
            moneyCount = getIntent().getStringExtra(
                    WithdrawActivity.WITHDRAW_COUNT);
            cardId = getIntent().getLongExtra("card_id", 0);
        } else if (mode == MODE_WITH_DRAW_AGENCY) {
            moneyCount = getIntent().getStringExtra(
                    WithdrawActivity.WITHDRAW_COUNT);
            cardId = getIntent().getLongExtra("card_id", 0);
            agentId = getIntent().getLongExtra("agentId", 0);
        }
    }

    @Override
    protected void initView() {
        tv_forgetpsw = (TextView) findViewById(R.id.tv_forgetpsw);
        llPay = (LinearLayout) findViewById(R.id.ll_payway);
        mPayWay = (TextView) findViewById(R.id.tv_payway);
        mMoney = (TextView) findViewById(R.id.tv_money);
        if (mode == MODE_ORDER_PAY) {// 订单模式
            mMoney.setText("¥" + moneyCount);
            llPay.setVisibility(View.VISIBLE);
            if (payWay.equals(Consts.BALAN_PAY)) {// 余额支付
                mPayWay.setText(getString(R.string.balan_pay));
            } else if (payWay.equals(Consts.INTER_PAY)) {// 返现支付
                mPayWay.setText(getString(R.string.inter_pay));
            }else if (payWay.equals(Consts.FxAcc)) {//分销货款
                mPayWay.setText(getString(R.string.payway_fenxiao));
            }
        }
        if (mode == MODE_NOMAL) {
            mMoney.setVisibility(View.GONE);
            llPay.setVisibility(View.VISIBLE);
            mPayWay.setText(getString(R.string.self_pay_tips));
        }
        findViewById(R.id.iv_del).setOnClickListener(this);
        findViewById(R.id.ll_commit).setOnClickListener(this);
        tv_forgetpsw.setOnClickListener(this);
        manager = new InputViewManager(findViewById(R.id.ll_keyboard),
                (PasswordView) findViewById(R.id.password_view), 6);
        manager.setOnInputListener(new InputViewManager.OnInputListener() {

            @Override
            public void onInputFinish(String s) {
                psw = s;
            }

            @Override
            public void onInputCancel() {

            }
        });
    }

    @Override
    protected void doOperate() {
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.ll_commit:
                if (psw.length() != 6) {
                    WWToast.showShort(R.string.pwd_error);
                } else {
                    if (mode == MODE_WITH_DRAW || mode == MODE_WITH_DRAW_DISTRIBUTION) {
                        commit(psw);
                    } else if (mode == MODE_ORDER_PAY) {
                        payOrder();
                    } else if (mode == MODE_WITH_DRAW_AGENCY) {
                        commit(psw);
                    } else if (mode == MODE_NOMAL) {
                        Intent intent = new Intent();
                        intent.putExtra(Consts.KEY_DATA, psw);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

                break;
            case R.id.iv_del:
                finish();
                break;
            case R.id.tv_forgetpsw:
                if (havePayPsw) {
                    PublicWay.startVerifyPhoneNumberActivity(this,
                            VerifyPhoneNumberActivity.PAY, SharedPreferenceUtils
                                    .getInstance().getPhoneNum(), 0);
                } else {
                    PublicWay.startVerifyPhoneNumberActivity(this,
                            VerifyPhoneNumberActivity.PAY, SharedPreferenceUtils
                                    .getInstance().getPhoneNum(), 0);
                }

                break;
            default:
                break;
        }

    }

    /**
     * 订单模式,支付订单
     */
    private void payOrder() {
        RequestParams params = new RequestParams(ApiPay.OrderPayGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("payCode", payWay);
        params.addBodyParameter("payPsd", psw);
        ZLog.showPost(params.toString());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("OrderPayGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

        });
    }

    private void commit(String psw) {
        String url = "";
        String dataResult = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("cashAmt", moneyCount);
        jsonObject.put("bankId", cardId);
        jsonObject.put("payPsd", psw);
        if (mode == MODE_WITH_DRAW) {
            url = ApiUser.getCashSubmit();
            dataResult = "CashSubmit";
        } else if (mode == MODE_WITH_DRAW_DISTRIBUTION) {
            url = ApiDistribution.CashSubmit();
            dataResult = "CashSubmit";
        } else {
            url = ApiAgency.CashCommit();
            dataResult = "CashCommit";
            jsonObject.put("agentId", agentId);
        }
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        url),
                new WWXCallBack(dataResult) {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        setResult(RESULT_OK);
                        finish();
                    }
                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    @Override
    protected void onResume() {
        getHavaPayPsw();
        manager.clear();
        psw = "";
        super.onResume();
    }

    private boolean havePayPsw;

    public void getHavaPayPsw() {
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("HavePaypsd") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                havePayPsw = data.getBooleanValue("Data");
                if (havePayPsw) {
                    tv_forgetpsw.setText(R.string.forget_psw_with_qus);
                } else {
                    tv_forgetpsw.setText(R.string.set_pay_psw);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == REQUEST_CODE) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

}
