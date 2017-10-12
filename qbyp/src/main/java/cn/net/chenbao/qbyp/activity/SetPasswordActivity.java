package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

public class SetPasswordActivity extends FatherActivity implements
		OnClickListener {
	public static final String DATA_CODE = "code";
	public static final String DATA_PHONE = "phone";
	public static final int PAY = 1;
	public static final int LOGIN = 2;

	private EditText mEdNewPassword;
	private EditText mEdMakeSurePassword;
	private String code;
	private String phone;
	private int mode;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_find_passwrod;
	}

	@Override
	protected void initValues() {
		mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
		if (mode == PAY) {
			initDefautHead(R.string.set_pay_psw, false);
		} else if (mode == LOGIN) {
			initDefautHead(R.string.find_login_password, false);
			phone = getIntent().getStringExtra(DATA_PHONE);
		}
		code = getIntent().getStringExtra(DATA_CODE);
	}

	@Override
	protected void initView() {
		mEdNewPassword = (EditText) findViewById(R.id.ed_new_password);
		mEdMakeSurePassword = (EditText) findViewById(R.id.ed_make_sure_password);
		if (mode == PAY) {
			mEdNewPassword.setHint(R.string.pay_psw_limit);
			mEdMakeSurePassword.setHint(R.string.pay_psw_limit);
			mEdNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdMakeSurePassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdNewPassword.setHint(R.string.pay_psw_rule_tip);
			mEdMakeSurePassword.setHint(R.string.pay_psw_rule_tip);
		}
		findViewById(R.id.tv_finish).setOnClickListener(this);
	}

	@Override
	protected void doOperate() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_finish:// 完成
			doRequest();
			break;
		default:
			break;
		}

	}

	/**
	 * 修改密码
	 */
	private void doRequest() {
		String psw = mEdNewPassword.getText().toString().trim();
		final String makePsw = mEdMakeSurePassword.getText().toString().trim();
		if (!psw.equals(makePsw)) {
			WWToast.showShort(R.string.psw_input_not_same);
			return;
		}
		if (mode == PAY) {
			if (psw.length() != 6 || makePsw.length() != 6) {
				WWToast.showShort(R.string.pay_psw_limit);
				return;
			}
		} else if (mode == LOGIN) {
			if (!RegexUtil.isPsw(makePsw)) {
				WWToast.showShort(R.string.input_password_limit2);
				return;
			}

		}
		RequestParams postJsonParams;
		String result = null;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		jsonObject.put("newPsd", makePsw);
		if (mode == PAY) {
			jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
					.getSessionId());
			postJsonParams = ParamsUtils.getPostJsonParams(jsonObject,
					ApiUser.PaypsdForget());
			result = "PaypsdForget";
		} else {
			jsonObject.put("mobile", phone);
			postJsonParams = ParamsUtils.getPostJsonParams(jsonObject,
					ApiUser.setNewPassword());
			result = "LogPsdForget";
		}
		showWaitDialog();
		x.http().post(postJsonParams, new WWXCallBack(result) {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				if (mode == PAY) {
					WWToast.showShort(R.string.set_success);
					sendBroadcast(new Intent("set_success"));
					finish();
				} else if (mode == LOGIN) {
					PublicWay.startLoginActivity(SetPasswordActivity.this,
							phone, makePsw);
					finish();
				}
			}

			@Override
			public void onAfterFinished() {
             dismissWaitDialog();
			}
		});
	}
}
