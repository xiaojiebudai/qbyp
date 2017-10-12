package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.api.ApiVariable;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;


/***
 * Description:设置 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-27
 ***/
public class SettingActivity extends FatherActivity implements OnClickListener {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                final CommonDialog commonDialog = DialogUtils
                        .getCommonDialogTwiceConfirm(SettingActivity.this,
                                R.string.exit_current_user, true);
                commonDialog.getButtonRight().setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                                exit();

                            }
                        });
                commonDialog.show();
                break;
            case R.id.ll_contact_us:
                getContactInfo();
                break;
            case R.id.ll_about_us:
                getInfo();
                break;
            case R.id.ll_app_versions_des:
                startActivity(new Intent(this, AppVersionExplainActivity.class));
                break;
            case R.id.ll_edit_psw:
                PublicWay.startEditPswActivity(SettingActivity.this,
                        EditPswActivity.EDITLOGIN);
                break;

            default:
                break;
        }
    }

    private void getContactInfo() {
        RequestParams params = new RequestParams(ApiVariable.LinkUs());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("LinkUs") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startWebViewActivity(SettingActivity.this, R.string.contact_us,
                        data.getString("Data"), WebViewActivity.DATA);
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });

    }

    private void exit() {
        RequestParams params = new RequestParams(ApiUser.getLogout());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("Logout") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // 信息置空
                WWApplication.getInstance().updataSessionId("");
                SharedPreferenceUtils.getInstance().clearLoginData();
                SharedPreferenceUtils.getInstance().saveUserPsw("");
                startActivity(new Intent(SettingActivity.this, MainActivity.class).putExtra("isexit", true));
                finish();
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }

    private void getInfo() {
        RequestParams params = new RequestParams(ApiVariable.AboutUs());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("AboutUs") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startWebViewActivity(SettingActivity.this, R.string.about_us,
                        data.getString("Data"), WebViewActivity.DATA);
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
             dismissWaitDialog();
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.setting, true);

    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_exit).setOnClickListener(this);
        findViewById(R.id.ll_contact_us).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.ll_app_versions_des).setOnClickListener(this);
        findViewById(R.id.ll_edit_psw).setOnClickListener(this);
    }

    @Override
    protected void doOperate() {

    }
}
