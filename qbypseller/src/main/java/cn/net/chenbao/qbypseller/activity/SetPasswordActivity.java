package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiUser;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.RegexUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;

/**
 * 商家忘记密码/修改密码/忘记支付密码/修改支付密码
 * 
 * @author licheng
 * 
 */
public class SetPasswordActivity extends FatherActivity implements
		OnClickListener {
	public static final String DATA_CODE = "code";
	public static final String DATA_PHONE = "phone";
	/** 忘记登录密码 */
	public static final int MODE_FOR_PSW = 1;
	/** 忘记支付密码 */
	public static final int MODE_FOR_PAY_PSW = 2;
	/** 修改登录密码 */
	public static final int MODE_UPDATA_PSW = 3;
	/** 修改支付密码 */
	public static final int MODE_UPDATA_PAY_PSW = 4;
	public int mode;

	private EditText mEdNewPassword;
	private EditText mEdMakeSurePassword;
	private String code;
	private String phone;
	private View mOldPswLL;
	private EditText mOldPsw;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_set_passwrod;
	}

	@Override
	protected void initValues() {
		code = getIntent().getStringExtra(DATA_CODE);
		phone = getIntent().getStringExtra(DATA_PHONE);
		mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
	}

	@Override
	protected void initView() {
		mOldPswLL = findViewById(R.id.ll_old);
		mEdNewPassword = (EditText) findViewById(R.id.ed_new_password);
		mEdMakeSurePassword = (EditText) findViewById(R.id.ed_make_sure_password);
		findViewById(R.id.tv_finish).setOnClickListener(this);
		mOldPsw = (EditText) findViewById(R.id.ed_old_password);
		switch (mode) {
		case MODE_UPDATA_PSW:
			initDefautHead(R.string.modify_login_password, true);
			mOldPswLL.setVisibility(View.VISIBLE);
			break;
		case MODE_UPDATA_PAY_PSW:// 支付
			initDefautHead(R.string.modify_pay_password, true);
			mOldPswLL.setVisibility(View.VISIBLE);
			mOldPsw.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdMakeSurePassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdNewPassword.setHint(R.string.pay_psw_rule_tip);
			break;
		case MODE_FOR_PSW:
			initDefautHead(R.string.find_login_password, true);
			break;
		case MODE_FOR_PAY_PSW:// 忘记支付
			initDefautHead(R.string.find_pay_word, true);
			mEdNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			mEdMakeSurePassword.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
			break;
		default:
			break;
		}

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
		String makePsw = mEdMakeSurePassword.getText().toString().trim();
		String oldPsw = mOldPsw.getText().toString().trim();
		RequestParams params = null;
		String modeKey = null;
		if (psw == null || makePsw == null) {
			WWToast.showShort(R.string.psw_not_empty);
			return;
		}
		if (!psw.equals(makePsw)) {
			WWToast.showShort(R.string.psw_input_not_same);
			return;
		}
		switch (mode) {
		case MODE_UPDATA_PSW:// 修改密码
			if (oldPsw == null) {
				WWToast.showShort(R.string.old_psw_not_empty);
				return;
			}
			if (!RegexUtil.isPsw(makePsw)) {
				WWToast.showShort(R.string.psw_length_error);
				return;
			}
			if (!RegexUtil.isPsw(oldPsw)) {
				WWToast.showShort(R.string.psw_length_error);
				return;
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("sessionId", WWApplication.getInstance()
					.getSessionId());
			jsonObject.put("oldPsd", oldPsw);
			jsonObject.put("newPsd", makePsw);
			params = ParamsUtils.getPostJsonParams(jsonObject,
					ApiUser.updataPsw());
			modeKey = "LogpsdChange";
			break;

		case MODE_UPDATA_PAY_PSW:// 修改支付密码
			if (oldPsw == null) {
				WWToast.showShort(R.string.old_psw_not_empty);
				return;
			}
			if (oldPsw.length() != 6 || psw.length() != 6
					|| makePsw.length() != 6) {// 支付密码必须6位
				WWToast.showShort(R.string.pay_word_length_must_six);
				return;
			}
			JSONObject jsonObject2 = new JSONObject();
			jsonObject2.put("sessionId", WWApplication.getInstance()
					.getSessionId());
			jsonObject2.put("oldPsd", oldPsw);
			jsonObject2.put("newPsd", makePsw);
			params = ParamsUtils.getPostJsonParams(jsonObject2,
					ApiUser.updataPayPsw());
			modeKey = "PaypsdChange";
			break;
		case MODE_FOR_PSW:// 忘记登录密码
			if (!RegexUtil.isPsw(makePsw)) {
				WWToast.showShort(R.string.psw_length_error);
				return;
			}
			JSONObject jsonObject3 = new JSONObject();
			jsonObject3.put("mobile", phone);
			jsonObject3.put("code", code);
			jsonObject3.put("newPsd", makePsw);
			params = ParamsUtils.getPostJsonParams(jsonObject3,
					ApiUser.forgetPsw());
			modeKey = "LogPsdForget";
			break;
		case MODE_FOR_PAY_PSW:// 忘记支付密码
			if (psw.length() != 6 || makePsw.length() != 6) {// 支付密码必须6位
				WWToast.showShort(R.string.pay_word_length_must_six);
				return;
			}
			JSONObject jsonObject4 = new JSONObject();
			jsonObject4.put("mobile", phone);
			jsonObject4.put("code", code);
			jsonObject4.put("newPsd", makePsw);
			jsonObject4.put("sessionId", WWApplication.getInstance()
					.getSessionId());
			params = ParamsUtils.getPostJsonParams(jsonObject4,
					ApiUser.forgetPayPsw());
			modeKey = "PaypsdForget";
			break;
		}
		showWaitDialog();
		x.http().post(params, new WWXCallBack(modeKey) {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				WWToast.showShort(R.string.updata_success);
				finish();
			}

			@Override
			public void onAfterFinished() {
            dismissWaitDialog();
			}
		});

	}
}
