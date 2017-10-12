package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

/**
 * 账户安全
 *
 * @author xl
 * @date 2016-7-30 下午3:59:26
 * @description
 */
public class AccountSafeActivity extends FatherActivity implements
        OnClickListener {
    public boolean havePayPsw;
    private TextView psw;
    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.act_account_safe;
    }

    @Override
    protected void initValues() {
        phone = getIntent().getStringExtra("phone");
        initDefautHead(R.string.acountsafe, true);
    }

    @Override
    protected void initView() {
        psw = (TextView) findViewById(R.id.tv_psw);
        findViewById(R.id.ll_updata_psw).setOnClickListener(this);
        findViewById(R.id.ll_updata_pay_psw).setOnClickListener(this);
        findViewById(R.id.ll_forget_pay_psw).setOnClickListener(this);

    }

    public void getHavaPayPsw() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("HavePaypsd") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                havePayPsw = data.getBooleanValue("Data");
                if (havePayPsw) {
                    psw.setText(getString(R.string.forget_pay_password));
                    findViewById(R.id.ll_updata_pay_psw).setVisibility(View.VISIBLE);
                } else {
                    psw.setText(getString(R.string.set_pay_psw));
                    findViewById(R.id.ll_updata_pay_psw).setVisibility(View.GONE);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        getHavaPayPsw();
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_updata_psw:// 修改登录密码
                PublicWay.startEditPswActivity(AccountSafeActivity.this,
                        EditPswActivity.EDITLOGIN);
                break;
            case R.id.ll_updata_pay_psw:// 修改支付密码
                PublicWay.startEditPswActivity(AccountSafeActivity.this,
                        EditPswActivity.EDITPAY);
                break;
            case R.id.ll_forget_pay_psw:// 忘记支付密码
                PublicWay.startVerifyPhoneNumberActivity(this,
                        VerifyPhoneNumberActivity.PAY, phone,
                        10086);
                break;

        }

    }

}
