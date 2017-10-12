package cn.net.chenbao.qbyp.activity;

import java.util.Timer;
import java.util.TimerTask;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 手机验证,找回密码,找回支付密码
 *
 * @author licheng
 */
public class VerifyPhoneNumberActivity extends FatherActivity implements
        OnClickListener {

    private EditText mEdPhoneNum;
    private EditText mEdVer;
    private TextView mTvGetVer;
    private TimerTask timerTask;
    private int delayeTime = 60;

    private boolean phoneRight;

    private int mode;
    /**
     * 支付密码验证
     */
    public final static int PAY = 0;
    /**
     * 登录 密码验证
     */
    public final static int LOGIN = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                delayeTime--;
                if (delayeTime == 0) {
                    timerTask.cancel();
                    mTvGetVer.setClickable(true);
                    mTvGetVer.setText(R.string.get_verification_code);
                    delayeTime = 60;
                } else {
                    mTvGetVer.setText(getString(R.string.send_ready)
                            + delayeTime + getString(R.string.sec));
                    mTvGetVer.setClickable(false);
                }
            }
        }
    };
    private TextView mTvPhoneNum;
    private String phone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_password_verify;
    }

    @Override
    protected void initValues() {
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        if (mode == PAY) {
            phone = getIntent().getStringExtra(Consts.KEY_DATA);
            if (phone.equals("")) {
                phone = SharedPreferenceUtils.getInstance().getPhoneNum();
            }
            initDefautHead(R.string.set_pay_psw, true);
        } else if (mode == LOGIN) {
            initDefautHead(R.string.find_login_password, true);
        }
    }

    @Override
    protected void initView() {
        mEdPhoneNum = (EditText) findViewById(R.id.ed_new_password);
        mTvPhoneNum = (TextView) findViewById(R.id.tv_num);
        mEdVer = (EditText) findViewById(R.id.ed_ver);
        mTvGetVer = (TextView) findViewById(R.id.tv_get_ver);
        mTvGetVer.setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
        if (mode == PAY) {
            mTvPhoneNum.setVisibility(View.VISIBLE);
            mTvPhoneNum.setText(phone);
        } else if (mode == LOGIN) {
            mEdPhoneNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void doOperate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_ver:
                getVerCode();
                break;
            case R.id.tv_next:// 下一步
                verifyCode();
                break;
        }

    }

    /**
     * 验证验证码
     */
    private void verifyCode() {

        switch (mode) {
            case PAY:
                String verCode = mEdVer.getText().toString().trim();
                if (TextUtils.isEmpty(verCode)) {
                    WWToast.showShort(R.string.ver_code_not_empty);
                    return;
                }
                if (verCode.length() != 6) {
                    WWToast.showShort(R.string.code_input_error);
                    return;
                }
                PublicWay.startSetPasswordActivity(this, verCode, null,
                        SetPasswordActivity.PAY);
                finish();
                break;

            case LOGIN:
                String phone = mEdPhoneNum.getText().toString().trim();
                String verCode2 = mEdVer.getText().toString().trim();
                if (phone.length() == 0) {
                    WWToast.showShort(R.string.please_input_phone_number);
                    return;
                }
                if (phone.length() != 11) {
                    WWToast.showShort(R.string.phone_num_error);
                    return;
                }
                if (verCode2.equals("")) {
                    WWToast.showShort(R.string.please_input_verification_code);
                    return;
                }
                if (verCode2.length() != 6) {
                    WWToast.showShort(R.string.code_input_error);
                    return;
                }
                if (phoneRight) {
                    PublicWay.startSetPasswordActivity(this, verCode2, phone,
                            SetPasswordActivity.LOGIN);
                    finish();
                } else {
                    WWToast.showShort(R.string.code_input_error);
                }
                break;
        }
    }

    /**
     * 获取验证码
     */
    private void getVerCode() {
        String phones = mEdPhoneNum.getText().toString().trim();// 登录手机号
        if (mode == LOGIN) {
            if (phones.length() != 11) {
                WWToast.showShort(R.string.phone_num_error);
                return;
            }
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mHandler.obtainMessage();
                message.what = 1;
                mHandler.sendMessage(message);
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 0, 1000);
        RequestParams params = null;
        String result = null;
        switch (mode) {
            case PAY:
                params = new RequestParams(ApiUser.PaypsdForgetSms());
                params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                        .getInstance().getSessionId());
                result = "PaypsdForgetSms";
                break;
            case LOGIN:
                params = new RequestParams(ApiUser.findPswVerify());
                params.addBodyParameter("mobile", phones);
                result = "LogPsdForgetSms";
                break;
        }
        showWaitDialog();
        x.http().get(params, new WWXCallBack(result) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                WWToast.showShort(R.string.send_success);
                phoneRight = true;
            }


            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }
}
