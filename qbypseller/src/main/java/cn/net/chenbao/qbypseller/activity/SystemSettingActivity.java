package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.api.ApiVariable;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;

/**
 * 系统设置
 *
 * @author xl
 * @date 2016-7-30 下午4:04:22
 * @description
 */
public class SystemSettingActivity extends FatherActivity implements
        OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.act_system_setting;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.system_setting, true);
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_exit).setOnClickListener(this);
        findViewById(R.id.ll_contact_us).setOnClickListener(this);
        findViewById(R.id.ll_about_us).setOnClickListener(this);
        findViewById(R.id.ll_app_versions_des).setOnClickListener(this);

    }

    @Override
    protected void doOperate() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_exit:
                final CommonDialog commonDialog = DialogUtils
                        .getCommonDialogTwiceConfirm(SystemSettingActivity.this,
                                R.string.quit_current_and_quit, true);
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
                PublicWay.startWebViewActivity(SystemSettingActivity.this,
                        "联系我们", data.getString("Data"), WebViewActivity.DATA);
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

        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.Logout());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());

        x.http().get(params, new WWXCallBack("Logout") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                WWApplication.getInstance().clearLoginInfo();
                //保留用户名，清除密码
                SharedPreferenceUtils.getInstance().saveUserPsw("");
                // 直接退出应用
                setResult(RESULT_OK);
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

    private void getInfo() {
        RequestParams params = new RequestParams(ApiVariable.AboutUs());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("AboutUs") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                PublicWay.startWebViewActivity(SystemSettingActivity.this,
                        "关于我们", data.getString("Data"), WebViewActivity.DATA);
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
}
