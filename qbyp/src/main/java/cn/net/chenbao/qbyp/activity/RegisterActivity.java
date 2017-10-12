package cn.net.chenbao.qbyp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.geetest.android.sdk.Geetest;
import com.geetest.android.sdk.GtDialog;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.api.ApiVariable;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 注册
 *
 * @author licheng
 */

public class RegisterActivity extends FatherActivity implements OnClickListener, PermissionUtil.PermissionCallbacks {
    private EditText mEdPhone;
    private EditText mEdVerCode;
    private EditText mEdPsw;
    private EditText mEdRecommCode;
    private TextView mTvGetVer;
    private TimerTask timerTask;
    private int delayeTime = 60;

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
    private CheckBox mCb;
    private ImageView mHideShow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.register_username, false);
    }

    @Override
    protected void initView() {
        mEdPhone = (EditText) findViewById(R.id.ed_phone_num);
        mEdVerCode = (EditText) findViewById(R.id.ed_ver_code);
        mEdPsw = (EditText) findViewById(R.id.ed_psw);
        mEdRecommCode = (EditText) findViewById(R.id.ed_recomm_code);
        mTvGetVer = (TextView) findViewById(R.id.tv_get_ver);
        mHideShow = (ImageView) findViewById(R.id.iv_hide_show);
        mCb = (CheckBox) findViewById(R.id.cb);
        mCb.setChecked(true);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_server_protocol).setOnClickListener(this);
        findViewById(R.id.tv_have_username).setOnClickListener(this);
        findViewById(R.id.ll_hide_show).setOnClickListener(this);
        mTvGetVer.setOnClickListener(this);
    }

    @Override
    protected void doOperate() {

    }

    private boolean isShow = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_ver:// 获取验证码
                String phone = mEdPhone.getText().toString().trim();
                if (phone.length() > 11 || phone.length() < 11) {
                    WWToast.showShort(R.string.phone_num_error);
                    return;
                }
                if (PermissionUtil.hasPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE})) {
                    startGeeTest();//初始化开启极验验证
                } else {
                    PermissionUtil.requestPermissions(this, 1111, new String[]{Manifest.permission.READ_PHONE_STATE});
                }

                break;
            case R.id.ll_hide_show:// 密码显示隐藏
                if (!TextUtils.isEmpty(mEdPsw.getText().toString().trim())) {
                    if (!isShow) {
                        mEdPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        isShow = true;
                    } else {
                        mEdPsw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        isShow = false;
                    }
                }
                break;

            case R.id.tv_have_username:// 已有账号登录
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.tv_register:// 注册
                doRegister();
                break;

            case R.id.tv_server_protocol:// 注册协议
                getServerInfo();
                break;
        }
    }

    private void startGeeTest() {
        RegisterActivity.GtAppDlgTask gtAppDlgTask = new RegisterActivity.GtAppDlgTask();
        mGtAppDlgTask = gtAppDlgTask;
        mGtAppDlgTask.execute();

        if (!RegisterActivity.this.isFinishing()) {
            progressDialog = ProgressDialog.show(RegisterActivity.this, null, "Loading", true, true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mGtAppDlgTask.getStatus() == AsyncTask.Status.RUNNING) {
                        Log.i("async task", "status running");
                        captcha.cancelReadConnection();
                        mGtAppDlgTask.cancel(true);
                    } else {
                        Log.i("async task", "No thing happen");
                    }
                }
            });
        }
        captcha.setTimeout(5000);
        captcha.setGeetestListener(new Geetest.GeetestListener() {
            @Override
            public void readContentTimeout() {
                mGtAppDlgTask.cancel(true);
                //TODO 获取验证参数超时
                progressDialog.dismiss();
                //Looper.prepare() & Looper.loop(): 在当前线程并没有绑定Looper时返回为null, 可以与toastMsg()一同在正式版本移除
                Looper.prepare();
                WWToast.showShort(R.string.get_params_time_out);
                Looper.loop();
            }

            @Override
            public void submitPostDataTimeout() {
                //TODO 提交二次验证超时
                WWToast.showShort(R.string.commit_verify_time_out);
            }
        });
    }

    private void getServerInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiVariable.UserProtocol());
        x.http().get(params, new WWXCallBack("UserProtocol") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startWebViewActivity(RegisterActivity.this, R.string.register_protocol,
                        data.getString("Data"), WebViewActivity.DATA);
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {

            }

            @Override
            public void onAfterFinished() {
             dismissWaitDialog();
            }
        });
    }

//    private void getVerCode(String phone) {
//
//        RequestParams params = new RequestParams(ApiUser.getVerificationCode());
//        params.addBodyParameter("mobile", phone);
//        x.http().get(params, new WWXCallBack("AppRegisterSms") {
//
//            @Override
//            public void onAfterSuccessOk(JSONObject data) {
//                timerTask = new TimerTask() {
//                    @Override
//                    public void run() {
//                        Message message = mHandler.obtainMessage();
//                        message.what = 1;
//                        mHandler.sendMessage(message);
//                    }
//                };
//                Timer timer = new Timer(true);
//                timer.schedule(timerTask, 0, 1000);
//                WWToast.showShort(R.string.send_success);
//            }
//
//            @Override
//            public void onAfterFinished() {
//
//            }
//        });
//    }

    /**
     * 注册
     */
    private void doRegister() {

        if (!mCb.isChecked()) {
            WWToast.showShort(R.string.agree_xieyi);
            return;
        }

        String phone = mEdPhone.getText().toString().trim();
        String verCode = mEdVerCode.getText().toString().trim();
        String psw = mEdPsw.getText().toString().trim();
        String recommCode = mEdRecommCode.getText().toString().trim();
        if (phone.length() == 0) {
            WWToast.showShort(R.string.please_input_phone_number);
            return;
        }
        if (phone.length() > 11 || phone.length() < 11) {
            WWToast.showShort(R.string.phone_num_error);
            return;
        }

        if (!RegexUtil.isPsw(psw)) {
            WWToast.showShort(R.string.psw_input_error);
            return;
        }
        if (TextUtils.isEmpty(verCode)) {
            WWToast.showShort(R.string.auth_code_null);
            return;
        }
        RequestParams params = new RequestParams(ApiUser.registerUser());
        params.addBodyParameter("mobile", phone);
        params.addBodyParameter("code", verCode);
        params.addBodyParameter("logpsd", psw);
        if (!recommCode.equals("")) {
            params.addBodyParameter("inviterNo", recommCode);
        }
        showWaitDialog();
        x.http().get(params, new WWXCallBack("Register") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                String userId = data.getString("Data");// 保存userId
                WWApplication.getInstance().setSessionId(userId);
                WWToast.showShort(R.string.register_success);
                startActivity(new Intent(RegisterActivity.this,
                        MainActivity.class));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }


    //因为可能用户当时所处在低速高延迟网络，所以异步请求可能在后台用时很久才获取到验证的数据。可以自己设计状态指示器, demo仅作演示。
    private ProgressDialog progressDialog;
    private GtAppDlgTask mGtAppDlgTask;
    // 创建验证码网络管理器实例
    private Geetest captcha = new Geetest(

            // 设置获取id，challenge，success的URL，需替换成自己的服务器URL
            ApiUser.getGeeValid(),

            // 设置二次验证的URL，需替换成自己的服务器URL
            ApiUser.getGeeValid()
    );

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startGeeTest();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        WWToast.showShort(R.string.permission_deny_cannot_open_verify);
    }


    class GtAppDlgTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            return captcha.checkServer();
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result) {

                // 根据captcha.getSuccess()的返回值 自动推送正常或者离线验证
                if (captcha.getSuccess()) {
                    openGtTest(RegisterActivity.this, captcha.getGt(), captcha.getChallenge(), captcha.getSuccess());
                } else {
                    // TODO 从API_1获得极验服务宕机或不可用通知, 使用备用验证或静态验证
                    // 静态验证依旧调用上面的openGtTest(_, _, _), 服务器会根据getSuccess()的返回值, 自动切换
                    // openGtTest(context, captcha.getGt(), captcha.getChallenge(), captcha.getSuccess());
                    // WWToast.showShort("Geetest Server is Down.");   //GeeTest服务关闭提示
                    // 执行此处网站主的备用验证码方案
                }

            } else {
                WWToast.showShort(R.string.server_exception);//Can't Get Data from API_1
            }
        }
    }

    public void openGtTest(Context ctx, String id, String challenge, boolean success) {

        GtDialog dialog = new GtDialog(ctx, R.style.DialogStyle, id, challenge, success);

        // 启用debug可以在webview上看到验证过程的一些数据
//        dialog.setDebug(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //TODO 取消验证
//                WWToast.showShort("取消验证");
            }
        });

        dialog.setGtListener(new GtDialog.GtListener() {

            @Override
            public void gtResult(boolean success, String result) {
                if (success) {
                    //TODO 一次验证通过,进行二次验证的操作
                    sendMsgRequest(result);
                } else {
                    //TODO 验证失败
                    WWToast.showShort(R.string.verify_fail);
                }
            }

            @Override
            public void gtCallClose() {
//                toastMsg("close geetest windows");
            }

            @Override
            public void gtCallReady(Boolean status) {

                progressDialog.dismiss();

                if (status) {
                    //TODO 验证加载完成
//                    WWToast.showShort("验证加载完成");
                } else {
                    //TODO 验证加载超时,未准备完成
                    WWToast.showShort(R.string.verify_load_time_out);
                }
            }

            @Override
            public void gtError() {
                progressDialog.dismiss();
                WWToast.showShort(R.string.error_happen);//"Fatal Error Did Occur."
            }

        });

    }

    private void sendMsgRequest(String result) {
        showWaitDialog();
        JSONObject res_json = (JSONObject) JSONObject.parse(result);
        JSONObject jsonObject = new JSONObject();
        String phone = mEdPhone.getText().toString().trim();
        jsonObject.put("mobile", phone);
        jsonObject.put("clientId", captcha.getClientId());
        jsonObject.put("challenge", res_json.getString("geetest_challenge"));
        jsonObject.put("seccode", res_json.getString("geetest_seccode"));
        jsonObject.put("validate", res_json.getString("geetest_validate"));
        RequestParams params = ParamsUtils.getPostJsonParams(jsonObject, ApiUser.RegisterSms());
        x.http().post(params, new WWXCallBack("RegisterSms") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                boolean isSuccess = data.getBooleanValue("Data");
                if (isSuccess) {
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
                    WWToast.showShort(R.string.send_success);
                } else {
                    WWToast.showShort(data.getString("Message"));
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }
}
