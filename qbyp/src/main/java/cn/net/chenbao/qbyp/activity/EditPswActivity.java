package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/***
 * Description:修改登陆密码 Company: Zhubaoyi Version：2.0
 *
 * @title EditPswActivity.java
 * @author ZXJ
 * @date 2016-8-2
 ***/
public class EditPswActivity extends FatherActivity implements OnClickListener {
    private EditText ed_old_password, ed_new_psw, ed_new_psw_check;
    private int model;
    /**
     * 修改登录
     */
    public final static int EDITLOGIN = 0;
    /**
     * 修改支付
     */
    public final static int EDITPAY = 1;
    /**
     * 设置支付密码
     */
    public final static int SETPAY = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                submit();
                break;

            default:
                break;
        }

    }

    private void submit() {
        String ed_old_password_str = ed_old_password.getText().toString()
                .trim();
        String ed_new_psw_str = ed_new_psw.getText().toString().trim();
        String ed_new_psw_check_str = ed_new_psw_check.getText().toString()
                .trim();
        String url = "";
        String resultApi = "";
        if (model == EDITLOGIN) {
            // 密码的一些验证
            if (TextUtils.isEmpty(ed_old_password_str)) {
                WWToast.showShort(R.string.please_now_psw);
                return;
            }
            if (TextUtils.isEmpty(ed_new_psw_str)) {
                WWToast.showShort(R.string.input_new_passwrod);
                return;
            }
            if (TextUtils.isEmpty(ed_new_psw_check_str)) {
                WWToast.showShort(R.string.input_new_passwrod_again);
                return;
            }


            if (ed_new_psw_str.length() < 6) {
                WWToast.showShort(R.string.psw_short);
                return;
            } else if (ed_new_psw_str.equals(ed_old_password_str)) {
                WWToast.showShort(R.string.new_old_like);
                return;
            } else if (!ed_new_psw_str.equals(ed_new_psw_check_str)) {
                WWToast.showShort(R.string.psw_check_different);
                return;
            } else if (!RegexUtil.isPsw(ed_new_psw_str)) {
                WWToast.showShort(R.string.reset_pwd_regular_tips);
                return;
            }
            url = ApiUser.getLogpsdChange();
            resultApi = "LogpsdChange";
        } else {
            if (TextUtils.isEmpty(ed_old_password_str)) {
                WWToast.showShort(R.string.current_pwd_not_empty);
                return;
            }
            if (TextUtils.isEmpty(ed_new_psw_str)) {
                WWToast.showShort(R.string.new_pwd_not_empty);
                return;
            }
            if (TextUtils.isEmpty(ed_new_psw_check_str)) {
                WWToast.showShort(R.string.makesure_pwd_not_empty);
                return;
            }
            if (ed_new_psw_str.length() != 6
                    || !RegexUtil.isNumber(ed_new_psw_str)) {
                WWToast.showShort(R.string.psw_must_six);
                return;
            } else if (ed_new_psw_str.equals(ed_old_password_str)) {
                WWToast.showShort(R.string.new_old_like);
                return;
            } else if (!ed_new_psw_str.equals(ed_new_psw_check_str)) {
                WWToast.showShort(R.string.psw_check_different);
                return;
            }

            url = ApiUser.getPaypsdChange();
            resultApi = "PaypsdChange";
        }
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("oldPsd", ed_old_password_str);
        jsonObject.put("newPsd", ed_new_psw_str);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url),
                new WWXCallBack(resultApi) {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWToast.showShort(R.string.set_succuss);
                        finish();
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

    @Override
    protected int getLayoutId() {
        return R.layout.act_edit_psw;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, EDITLOGIN);

    }

    @Override
    protected void initView() {
        ed_old_password = (EditText) findViewById(R.id.ed_old_password);
        ed_new_psw = (EditText) findViewById(R.id.ed_new_psw);
        ed_new_psw_check = (EditText) findViewById(R.id.ed_new_psw_check);
        findViewById(R.id.tv_ok).setOnClickListener(this);
        if (model == EDITLOGIN) {
            initDefautHead(R.string.edit_login_psw, true);
        } else {
            initDefautHead(R.string.edit_pay_psw, true);
            ed_old_password.setInputType(InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            ed_new_psw.setInputType(InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            ed_new_psw_check.setInputType(InputType.TYPE_CLASS_NUMBER
                    | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            ed_new_psw.setHint(R.string.pay_psw_rule_tip);
            ed_new_psw_check.setHint(R.string.input_new_passwrod_again);
        }
    }

    @Override
    protected void doOperate() {
        // TODO Auto-generated method stub

    }

}
