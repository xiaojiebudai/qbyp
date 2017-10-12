package cn.net.chenbao.qbypseller.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.VersionInfo;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.service.UpdateVersionService;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.JpushUtil;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 商家登录页面
 *
 * @author licheng
 */
public class BusinessLoginActivity extends FatherActivity implements
        OnClickListener, PermissionUtil.PermissionCallbacks {

    private EditText mUsername;
    private EditText mPassWord;

    /**
     * 未认证商家进入填写资料页
     */
    public static final int STATE_UNCERTIFICATE = 0;
    /**
     * 已提交资料未付款进入付款页
     */
    public static final int STATE_UNPAY = 1;
    /**
     * 已提交资料已付款进入等待审核页
     */
    public static final int STATE_UNDERWAY_CHECK = 2;
    /**
     * 审核驳回进入被拒绝提示页
     */
    public static final int STATE_REFUSE = 3;
    /**
     * 审核成功进入成功提示页
     */
    public static final int STATE_PASS = 4;
    /**
     * 已认证商家进入正常商家首页
     */
    public static final int STATE_COMMON = 5;
    /**
     * 商家续费到期进入续费页面
     */
    public static final int SERVER_OUT_DATE = 6;
    private VersionInfo version;
    private String phone;
    private String psw1;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1103;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_business_login;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.business_login, false);
    }

    @Override
    protected void initView() {
        mUsername = (EditText) findViewById(R.id.ed_username);
        mPassWord = (EditText) findViewById(R.id.ed_password);
        findViewById(R.id.rl_forget).setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);
        findViewById(R.id.tv_business_register).setOnClickListener(this);
        phone = SharedPreferenceUtils.getInstance().getPhoneNum();
        psw1 = SharedPreferenceUtils.getInstance().getUserPsw();
        if (!TextUtils.isEmpty(phone)) {
            mUsername.setText(phone);

        }
        if (!TextUtils.isEmpty(psw1)) {
            mPassWord.setText(psw1);
        }
    }

    @Override
    protected void doOperate() {
        appVerGet();
        showToastPermisionDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_forget:// 忘记密码
                PublicWay.startfindLoginPasswordAcitivity(this,
                        FindlLoginPasswordActivity.MODE_LOGIN_PSW, null);
                break;
            case R.id.tv_login:// 登录
                String username = mUsername.getText().toString();
                String psw = mPassWord.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    WWToast.showShort(R.string.username_not_empty);
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    WWToast.showShort(R.string.psw_not_empty);
                    return;
                }
                doLogin(username, psw);
                break;
            case R.id.tv_business_register:// 注册
                startActivity(new Intent(this, BusinessRegisterActivity.class));
                break;
        }
    }

    private void appVerGet() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.AppVerGet()),
                new WWXCallBack("AppVerGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        version = JSON.parseObject(
                                data.getString("Data"), VersionInfo.class);
                        try {
                            if (version.InterNo != PublicWay.getVersionCode(BusinessLoginActivity.this)) {
                                final CommonDialog commonDialog = DialogUtils
                                        .getCommonDialog(BusinessLoginActivity.this,
                                                version.Explain);
                                commonDialog.getButtonLeft().setText(R.string.cancel);
                                commonDialog.getButtonLeft().setOnClickListener(
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                if (version.IsForce) {
                                                    finish();
                                                }
                                                commonDialog.dismiss();
                                            }
                                        });
                                commonDialog.getButtonRight().setText(R.string.download_right_now);
                                commonDialog.getButtonRight().setOnClickListener(
                                        new OnClickListener() {
                                            @Override
                                            public void onClick(View arg0) {
                                                if (PermissionUtil.hasPermissions(BusinessLoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                                                    update(version);
                                                } else {
                                                    PermissionUtil.requestPermissions(BusinessLoginActivity.this, REQUEST_CODE_WRITE_EXTERNAL_STORAGE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                                                }

                                                commonDialog.dismiss();
                                            }
                                        });
                                if (version.IsForce) {
                                    commonDialog.setCancelable(false);
                                } else {
                                    commonDialog.setCancelable(true);
                                }
                                commonDialog.show();
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });

    }

    private void update(VersionInfo version) {
        if (version.IsForce) {
            apk_path = version.DownloadUrl;
            showDownloadDialog();
        } else {
            Intent updateIntent = new Intent(BusinessLoginActivity.this,
                    UpdateVersionService.class);
            updateIntent
                    .putExtra("titleId", R.string.app_name);
            updateIntent.putExtra("downloadUrl", version.DownloadUrl);
            updateIntent.putExtra("app_desc", version.Explain);
            startService(updateIntent);

        }
    }

    ProgressDialog progressDialog;
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment.getExternalStorageDirectory() + "/" + "app/download" + "/";
    // 下载应用存放全路径
    private static final String FILE_NAME = FILE_PATH + "qbypseller.apk";
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 1;
    private String apk_path = "";

    /**
     * 显示下载进度对话框
     */
    public void showDownloadDialog() {

        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setTitle(R.string.downloading);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        final downloadAsyncTask asyncTask = new downloadAsyncTask();
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (asyncTask != null) {
                    asyncTask.cancel(true);
                }
            }
        });
        asyncTask.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            update(version);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE) {
            if (version != null && version.IsForce) {
                finish();
            }
            WWToast.showShort(R.string.write_permission_deny_cannot_update);
        }
    }

    /**
     * 下载新版本应用
     */
    private class downloadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {

            URL url;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            try {
                url = new URL(apk_path);
                connection = (HttpURLConnection) url.openConnection();

                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                File file_path = new File(FILE_PATH);
                if (!file_path.exists()) {
                    file_path.mkdir();
                }

                out = new FileOutputStream(new File(FILE_NAME));//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;
                    int curProgress = (int) (((float) readLength / fileLength) * 100);
                    publishProgress(curProgress);
                    if (readLength >= fileLength) {
                        break;
                    }
                }

                out.flush();
                return INSTALL_TOKEN;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {

            progressDialog.dismiss();//关闭进度条
            final CommonDialog commonDialog = DialogUtils
                    .getCommonDialog(BusinessLoginActivity.this,
                            R.string.download_success_please_install);
            commonDialog.getButtonLeft().setVisibility(View.GONE);
            commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    finish();
                }
            });
            commonDialog.setCanceledOnTouchOutside(false);
            commonDialog.getButtonRight().setText(R.string.install_now);
            commonDialog.getButtonRight().setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            installApp();
                        }
                    });
            commonDialog.show();
            //安装应用
            installApp();
        }
    }

    /**
     * 安装新版本应用
     */
    private void installApp() {
        try {
            File appFile = new File(FILE_NAME);
            if (!appFile.exists()) {
                return;
            }
            // 跳转到新版本应用安装页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//兼容7.0
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                        "cn.net.chenbao.qbypseller.fileprovider", appFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }


    /***
     * 登录操作
     *
     * @param psw
     * @param username
     */
    private void doLogin(final String username, final String psw) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", username);
        jsonObject.put("logPsd", psw);
        RequestParams params = ParamsUtils.getPostJsonParams(jsonObject,
                ApiSeller.login());
        x.http().post(params, new WWXCallBack("Login") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                String sessionId = data.getString("Data");
                WWApplication.getInstance().setSessionPast();
                if (!TextUtils.isEmpty(data.getString("Message"))
                        && (!data.getString("Message").equals("0"))) {
                    // 调用JPush API设置Alias
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS,
                            "Seller" + data.getString("Message")));
                }
                SharedPreferenceUtils.getInstance().savePhoneNum(
                        username);
                SharedPreferenceUtils.getInstance().saveUserPsw(psw);
                switch (data.getIntValue("TotalCount")) {
                    case STATE_UNCERTIFICATE:// 到填写资本资料页面
                        PublicWay.startBaseInfoActivity(BusinessLoginActivity.this,
                                sessionId);
                        break;
                    case STATE_UNPAY:// 到支付页面，关闭入口
                        PublicWay.startOpenServerActivity(
                                BusinessLoginActivity.this, sessionId);
                        break;
                    case STATE_UNDERWAY_CHECK:// 等待审核
                        PublicWay.startCheckActivity(BusinessLoginActivity.this,
                                CheckActivity.MODE_CHECK, null);
                        break;
                    case STATE_REFUSE:// 未通过
                        PublicWay.startCheckActivity(BusinessLoginActivity.this,
                                CheckActivity.MODE_UNPASS, sessionId);
                        break;
                    case STATE_PASS:
                        PublicWay.startCheckActivity(BusinessLoginActivity.this,
                                CheckActivity.MODE_PASS, 888, sessionId);// 要传ID
                        break;
                    case STATE_COMMON:
                        WWApplication.getInstance().setSessionId(sessionId);
                        startActivity(new Intent(BusinessLoginActivity.this,
                                MainActivity.class));
                        finish();
                        break;
                    case SERVER_OUT_DATE://服务过期提示续费
                        showServerOutDateTips(sessionId);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void showServerOutDateTips(final String sessionId) {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialog(
                        BusinessLoginActivity.this, R.string.server_out_date_tips);
        commonDialog.getButtonRight(R.string.renew);
        commonDialog.getButtonRight().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessLoginActivity.this, RenewServiceActivity.class);
                intent.putExtra(Consts.KEY_DATA, sessionId);
                intent.putExtra(Consts.KEY_MODULE, RenewServiceActivity.LOGIN_FORCE_RENEW);
                startActivityForResult(intent, 666);
                commonDialog.dismiss();
            }
        });
        commonDialog.getButtonLeft(R.string.cancel);
        commonDialog.getButtonLeft().setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
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
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // 审核通过了
        if (arg1 == RESULT_OK) {
            if (arg0 == 888 || arg0 == 666) {
                finish();
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    private void showToastPermisionDialog() {
        if (!WWToast.isFloatWindowOpAllowed(this)) {
            final CommonDialog commonDialogTwiceConfirm = DialogUtils.getCommonDialogTwiceConfirm(this, R.string.floating_window_closed, false);
            commonDialogTwiceConfirm.setRightButtonText(R.string.go_setting);
            commonDialogTwiceConfirm.setRightButtonCilck(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    commonDialogTwiceConfirm.dismiss();
                    Uri packageURI = Uri.parse("package:" + getPackageName());
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                    startActivity(intent);
                }
            });
            commonDialogTwiceConfirm.show();
        }

    }

    @Override
    public void onBackPressed() {
        if (TimeUtil.isFastClick(3000)) {
            finish();
        } else {
            WWToast.showShort(R.string.double_clike_exit);
        }
    }
}
