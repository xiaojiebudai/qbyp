package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiPay;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.api.ApiUser;
import cn.net.chenbao.qbypseller.bean.JiFen;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.numberKeyboard.InputViewManager;
import cn.net.chenbao.qbypseller.numberKeyboard.PasswordView;
import cn.net.chenbao.qbypseller.numberKeyboard.InputViewManager.OnInputListener;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;

public class VerifyPasswordActivity extends FatherActivity implements
        OnClickListener {

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
    private int model;
    /**
     * 提现
     */
    public final static int WITHDRAW = 0;
    /**
     * 积分支付
     */
    public final static int JIFENZHIFU = 1;
    /**
     * 余额支付
     */
    public final static int YUEZHIFU = 2;
    private JiFen orderDetail;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_verify_password;
    }

    @Override
    protected void initValues() {

        model = getIntent().getIntExtra(Consts.KEY_MODE, WITHDRAW);
        if (model == WITHDRAW) {
            moneyCount = getIntent()
                    .getStringExtra(Consts.KEY_DATA);
            cardId = getIntent().getLongExtra("card_id", 0);
        } else {
            orderDetail = JSON.parseObject(getIntent()
                            .getStringExtra(Consts.KEY_DATA),
                    JiFen.class);
        }

    }

    @Override
    protected void initView() {
        tv_forgetpsw = (TextView) findViewById(R.id.tv_forgetpsw);
        findViewById(R.id.iv_del).setOnClickListener(this);
        findViewById(R.id.ll_commit).setOnClickListener(this);
        tv_forgetpsw.setOnClickListener(this);
        manager = new InputViewManager(findViewById(R.id.ll_keyboard),
                (PasswordView) findViewById(R.id.password_view), 6);
        manager.setOnInputListener(new OnInputListener() {

            @Override
            public void onInputFinish(String s) {
                psw = s;
            }

            @Override
            public void onInputCancel() {
                // TODO Auto-generated method stub

            }
        });

    }

    @Override
    protected void doOperate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {

            case R.id.ll_commit:
                if (psw.length() != 6) {
                    WWToast.showShort(R.string.pwd_input_error);
                } else {
                    if (model == WITHDRAW) {
                        commit(psw);
                    } else {
                        startPay(psw,model);
                    }

                }

                break;
            case R.id.iv_del:
                finish();
                break;
            case R.id.tv_forgetpsw:

                if (!havePayPsw) {// 支付密码没设置过去设置
                    PublicWay.startfindLoginPasswordAcitivity(
                            VerifyPasswordActivity.this,
                            FindlLoginPasswordActivity.MODE_PAY_PSW, "");
                } else if (havePayPsw) {// 设置了去修改
                    PublicWay.startfindLoginPasswordAcitivity(
                            VerifyPasswordActivity.this,
                            FindlLoginPasswordActivity.MODE_PAY_PSW, "");
                }

                break;
            default:
                break;
        }

    }

    /**
     * 积分支付
     *
     * @param psw
     */
    private void startPay(String psw,int model) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.OrderPayGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderDetail.OrderId + "");
        params.addBodyParameter("payPsd", psw);
        params.addBodyParameter("payCode", model==JIFENZHIFU?Consts.INTEGRAL_PAY:Consts.SEL_ACC);
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("cashAmt", moneyCount);
        jsonObject.put("bankId", cardId);
        jsonObject.put("payPsd", psw);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiSeller.getCashSubmit()),
                new WWXCallBack("CashSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWToast.showShort(R.string.set_succuss);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        if (data.getIntValue("ErrCode") == 104) {
                            final CommonDialog commonDialog = DialogUtils
                                    .getCommonDialog(
                                            VerifyPasswordActivity.this,
                                            R.string.have_not_auth);
                            commonDialog.getButtonRight(R.string.goto_auth);
                            commonDialog.getButtonLeft(R.string.temp_no_deal);
                            commonDialog.getButtonLeft().setOnClickListener(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.getButtonRight().setOnClickListener(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            PublicWay
                                                    .startCommitInfoActivity(
                                                            VerifyPasswordActivity.this,
                                                            WWApplication
                                                                    .getInstance()
                                                                    .getSessionId());
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.show();
                        } else if (data.getIntValue("ErrCode") == 105) {
                            // 去设置支付密码
                            PublicWay
                                    .startfindLoginPasswordAcitivity(
                                            VerifyPasswordActivity.this,
                                            FindlLoginPasswordActivity.MODE_PAY_PSW,
                                            "");
                        }
                        finish();
                        super.onAfterSuccessError(data);
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
}
