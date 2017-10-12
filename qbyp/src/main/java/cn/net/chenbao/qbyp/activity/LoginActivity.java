package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.JpushUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.bean.CartSum;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.ZLog;

import org.xutils.x;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 登录activity
 *
 * @author licheng
 */
public class LoginActivity extends FatherActivity implements OnClickListener {

    public static final String DATA_PSW = "psw";
    public static final String DATA_PHONE = "phone";
    public static final String ISFRAOMMAIN = "isFromMain";
    private boolean isFromMain = true;

    private EditText mEdUserName;
    private EditText mEdPassWord;
    private String phone;
    private String psw1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.user_login, false);

        phone = SharedPreferenceUtils.getInstance().getPhoneNum();
        psw1 = SharedPreferenceUtils.getInstance().getUserPsw();
        if (!TextUtils.isEmpty(getIntent().getStringExtra(DATA_PHONE))) {
            phone = getIntent().getStringExtra(DATA_PHONE);
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra(DATA_PSW))) {

            psw1 = getIntent().getStringExtra(DATA_PSW);
        }

        isFromMain = getIntent().getBooleanExtra(ISFRAOMMAIN, true);
    }

    @Override
    protected void initView() {
        mEdUserName = (EditText) findViewById(R.id.ed_username);
        mEdPassWord = (EditText) findViewById(R.id.ed_password);
        if (!TextUtils.isEmpty(phone)) {
            mEdUserName.setText(phone);

        }
        if (!TextUtils.isEmpty(psw1)) {
            mEdPassWord.setText(psw1);
        }
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.tv_fatser_register).setOnClickListener(this);
        findViewById(R.id.tv_forget_password).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        phone = SharedPreferenceUtils.getInstance().getPhoneNum();
        psw1 = SharedPreferenceUtils.getInstance().getUserPsw();
        if (!TextUtils.isEmpty(intent.getStringExtra(DATA_PHONE))) {
            phone = intent.getStringExtra(DATA_PHONE);
        }
        if (!TextUtils.isEmpty(intent.getStringExtra(DATA_PSW))) {

            psw1 = intent.getStringExtra(DATA_PSW);
        }

        isFromMain = getIntent().getBooleanExtra(ISFRAOMMAIN, true);
        if (!TextUtils.isEmpty(phone)) {
            mEdUserName.setText(phone);

        }
        if (!TextUtils.isEmpty(psw1)) {
            mEdPassWord.setText(psw1);
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void doOperate() {
        ZLog.showPost("111" + isFromMain);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_login:// 登录
                if (!TimeUtil.isFastClick()) {
                    login();
                } else {
                    WWToast.showShort(R.string.click_too_fast);
                }
                break;
            case R.id.tv_fatser_register:// 快速注册
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_password:// 忘记密码
                PublicWay.startVerifyPhoneNumberActivity(this,
                        VerifyPhoneNumberActivity.LOGIN, null, 0);
                break;

            default:
                break;
        }
    }

    /**
     * 登录操作
     */
    private void login() {
        final String username = mEdUserName.getText().toString();
        final String psw = mEdPassWord.getText().toString();
        if (TextUtils.isEmpty(username)) {
            WWToast.showShort(R.string.please_input_username);
            return;
        }
        if (psw.length() == 0) {
            WWToast.showShort(R.string.please_input_password);
            return;
        }
        if (psw.length() < 6) {
            WWToast.showShort(R.string.psw_input_error);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", username);
        jsonObject.put("logPsd", psw);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject, ApiUser.login()),
                new WWXCallBack("Login") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWApplication.getInstance().setSessionId(
                                data.getString("Data"));
                        SharedPreferenceUtils.getInstance().savePhoneNum(
                                username);
                        SharedPreferenceUtils.getInstance().saveUserPsw(psw);
                        getCartSum();
                        // 调用JPush API设置Alias
                        mHandler.sendMessage(mHandler.obtainMessage(
                                MSG_SET_ALIAS,
                                "User" + data.getString("Message")));
                        if (isFromMain) {
                            startActivity(new Intent(LoginActivity.this,
                                    MainActivity.class).putExtra("LoginSuccess",
                                    true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        finish();

                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        super.onAfterSuccessError(data);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    /**
     * 获取购物车数量
     */
    private void getCartSum() {
        x.http().get(ParamsUtils.getSessionParams(ApiShop.CartSumGet()), new WWXCallBack("CartSumGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                CartSum shopCar = JSONObject.parseObject(data.getString("Data"), CartSum.class);
                SharedPreferenceUtils.getInstance().saveCartNum(shopCar.Quantity);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SET_TAGS, tags),
                                1000 * 60);
                    } else {
                        Log.i("jpush", "No network");
                    }
            }
            ZLog.showPost(logs);
        }

    };
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    if (JpushUtil.isConnected(getApplicationContext())) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(MSG_SET_ALIAS, alias),
                                1000 * 60);
                    } else {
                        Log.i("jpush", "No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
            }

            ZLog.showPost(logs);
        }

    };
    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null,
                            (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
            }
        }
    };

    @Override
    public void onBackPressed() {// 返回键跳主界面
        super.onBackPressed();
        if (isFromMain) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
