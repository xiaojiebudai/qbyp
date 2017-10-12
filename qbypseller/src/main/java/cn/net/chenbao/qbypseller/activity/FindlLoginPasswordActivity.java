package cn.net.chenbao.qbypseller.activity;

import java.util.Timer;
import java.util.TimerTask;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiUser;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.JsonUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

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

import cn.net.chenbao.qbypseller.R;

/**
 * 手机验证,找回密码
 * 
 * @author licheng
 * 
 */
public class FindlLoginPasswordActivity extends FatherActivity implements
		OnClickListener {
	/** 支付密码 */
	public static final int MODE_PAY_PSW = 0;
	/** 登录密码 */
	public static final int MODE_LOGIN_PSW = 1;
	public int mode;

	private EditText mEdPhoneNum;
	private EditText mEdVer;
	private TextView mTvGetVer;
	private TimerTask timerTask;
	private int delayeTime = 60;
	private TextView tvPhoneNum;

	private boolean phoneRight;

	private String phoneLogin;

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
	private String phone;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_find_password;
	}

	@Override
	protected void initValues() {
		mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
		if (mode == MODE_PAY_PSW) {
			phone = getIntent().getStringExtra(Consts.KEY_DATA);
			initDefautHead(R.string.find_pay_word, true);
		} else if (mode == MODE_LOGIN_PSW) {
			initDefautHead(R.string.find_login_password, true);

		}
	}

	@Override
	protected void initView() {
		tvPhoneNum = (TextView) findViewById(R.id.tv_num);
		mEdPhoneNum = (EditText) findViewById(R.id.ed_new_password);
		if (mode == MODE_PAY_PSW) {
			tvPhoneNum.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(phone)) {
				phone = SharedPreferenceUtils.getInstance().getPhoneNum();
			}
			tvPhoneNum.setText(phone);
		} else if (mode == MODE_LOGIN_PSW) {
			mEdPhoneNum.setVisibility(View.VISIBLE);
		}
		mEdVer = (EditText) findViewById(R.id.ed_ver);
		mTvGetVer = (TextView) findViewById(R.id.tv_get_ver);
		mTvGetVer.setOnClickListener(this);
		findViewById(R.id.tv_next).setOnClickListener(this);

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
		if(mode==MODE_LOGIN_PSW){
			phoneLogin = mEdPhoneNum.getText().toString().trim();// 修改登录密码
		}else{
			phoneLogin = tvPhoneNum.getText().toString().trim();// 修改支付密码
		}
		String verCode = mEdVer.getText().toString().trim();
		if (phoneLogin.length() != 11) {
			WWToast.showShort(R.string.phone_num_error);
			return;
		}
		if (verCode.equals("") || verCode.length() != 6) {
			WWToast.showShort(R.string.code_input_error);
			return;
		}

		if (mode == MODE_PAY_PSW) {
			PublicWay.startSetPassWordActivity(this,
					SetPasswordActivity.MODE_FOR_PAY_PSW, verCode, phone);
		} else if (mode == MODE_LOGIN_PSW) {
			PublicWay.startSetPassWordActivity(this,
					SetPasswordActivity.MODE_FOR_PSW, verCode, phoneLogin);
		}
		finish();
	}

	/**
	 * 获取验证码
	 */
	private void getVerCode() {
		String PhoneLogin;
		if(mode==MODE_LOGIN_PSW){
			PhoneLogin = mEdPhoneNum.getText().toString().trim();// 修改登录密码
		}else{
			PhoneLogin = tvPhoneNum.getText().toString().trim();// 修改登录密码
		}
		
		if (PhoneLogin.length() != 11) {
			WWToast.showShort(R.string.phone_num_error);
			return;
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

		RequestParams requestParams = null;
		String result = null;
		if (mode == MODE_PAY_PSW) {// 支付
			requestParams = new RequestParams(ApiUser.PaypsdForgetSms());
			requestParams.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
					.getInstance().getSessionId());
			result = "PaypsdForgetSms";
		} else if (mode == MODE_LOGIN_PSW) {// 登录
			requestParams = new RequestParams(ApiUser.LogPsdForgetSms());
			requestParams.addBodyParameter("mobile", PhoneLogin);
			this.phoneLogin = PhoneLogin;
			result = "LogPsdForgetSms";
		}
		showWaitDialog();
		x.http().get(requestParams, new WWXCallBack(result) {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				WWToast.showShort(R.string.send_success);
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}

			@Override
			public void onAfterSuccessError(JSONObject data) {
				WWToast.showShort(JsonUtils.parserJsonMessage(data));
			}
		});

	}
}
