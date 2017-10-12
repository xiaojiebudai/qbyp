package cn.net.chenbao.qbyp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.VersionInfo;

import cn.net.chenbao.qbyp.distribution.fragment.DistributionAdvantageFragmentNew;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.HomePageFragment2;
import cn.net.chenbao.qbyp.fragment.LocalOrderAllFragment;
import cn.net.chenbao.qbyp.fragment.PersonCenterFragment;
import cn.net.chenbao.qbyp.fragment.SelfSupportFragment;
import cn.net.chenbao.qbyp.service.UpdateVersionService;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 开工
 *
 * @author xl
 * @date:2016-7-24下午3:14:45
 * @description
 */
public class MainActivity extends FatherActivity implements PermissionUtil.PermissionCallbacks {

    private View[] mTabs;
    private FatherFragment[] mFragments;

    private HomePageFragment2 mHomePageFragment;
    private DistributionAdvantageFragmentNew mDistributionAdvantageFragment;
    private SelfSupportFragment selfSupportFragment;
    private LocalOrderAllFragment mShopCartFragment;
    private PersonCenterFragment mPersonCenterFragment;
    //TODO

    //    public final static int TAB_DISTRIBUTION = 0;
    public final static int TAB_SELF = 0;
    public final static int TAB_HOMEPAGE = 1;
    public final static int TAB_SHOPCAR = 2;
    public final static int TAB_PERSONCENTER = 3;

    /**
     * 欲选中tab
     */
    private int mIndex;
    /**
     * 当前的选中的tab
     */
    private int mCurrentTabIndex;
    private VersionInfo version;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_new;
    }


    @Override
    protected void initValues() {

    }

    private void getRedPackegeInfo() {
        x.http().get(ParamsUtils.getSessionParams(ApiUser.BonusCount()),
                new WWXCallBack("BonusCount") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        int bonusCount = data.getIntValue("Data");
                        if (bonusCount > 0) {
                            Intent intent = new Intent(MainActivity.this,
                                    RedPacketActivity.class);
                            intent.putExtra(Consts.KEY_DATA, bonusCount);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub

                    }
                });
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
                            if (version.InterNo != PublicWay.getVersionCode(MainActivity.this)) {
                                final CommonDialog commonDialog = DialogUtils
                                        .getCommonDialog(MainActivity.this,
                                                version.Explain);
                                commonDialog.getButtonLeft().setText(R.string.cancel);
                                commonDialog.getButtonLeft()
                                        .setOnClickListener(
                                                new OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            View arg0) {
                                                        if (version.IsForce) {
                                                            finish();
                                                        }
                                                        commonDialog.dismiss();
                                                    }
                                                });
                                commonDialog.getButtonRight().setText(R.string.download_right_now);
                                commonDialog.getButtonRight()
                                        .setOnClickListener(
                                                new OnClickListener() {
                                                    @Override
                                                    public void onClick(
                                                            View arg0) {
                                                        if (PermissionUtil.hasPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                                                            update(version);
                                                        } else {
                                                            PermissionUtil.requestPermissions(MainActivity.this, RESULT_CODE_WRITE_PERMISSION, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
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

    private static final int RESULT_CODE_WRITE_PERMISSION = 1122;

    private void update(VersionInfo version) {
        if (version.IsForce) {
            apk_path = version.DownloadUrl;
            showDownloadDialog();
        } else {
            Intent updateIntent = new Intent(MainActivity.this,
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
    private static final String FILE_NAME = FILE_PATH + "qbyp.apk";
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
        final downloadAsyncTask asyncTask = new MainActivity.downloadAsyncTask();
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
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == RESULT_CODE_WRITE_PERMISSION) {
            update(version);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == RESULT_CODE_WRITE_PERMISSION) {
            WWToast.showShort(R.string.write_permission_deny_cannot_update);
            if (version != null && version.IsForce) {
                finish();
            }
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
                    .getCommonDialog(MainActivity.this,
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
                        "cn.net.chenbao.qbyp.fileprovider", appFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            startActivity(intent);
        } catch (Exception e) {

        }
    }


    @Override
    protected void initView() {

        //TODO new verssion use
        mHomePageFragment = new HomePageFragment2();
//        mDistributionAdvantageFragment = new DistributionAdvantageFragmentNew();
        mShopCartFragment = new LocalOrderAllFragment();
        selfSupportFragment = new SelfSupportFragment();
        mPersonCenterFragment = new PersonCenterFragment();
        mFragments = new FatherFragment[]{selfSupportFragment, mHomePageFragment, mShopCartFragment,
                mPersonCenterFragment};//mDistributionAdvantageFragment,
        getSupportFragmentManager().beginTransaction()
                .add(R.id.rl_container, selfSupportFragment)
                .add(R.id.rl_container, mHomePageFragment).hide(mHomePageFragment)
                .show(selfSupportFragment).commit();
        mTabs = new View[4];

//        mTabs[TAB_DISTRIBUTION] = findViewById(R.id.rl_tab_distribution);
        mTabs[TAB_SELF] = findViewById(R.id.rl_tab_selfsupport);
        mTabs[TAB_HOMEPAGE] = findViewById(R.id.rl_tab_homepage);
        mTabs[TAB_SHOPCAR] = findViewById(R.id.rl_tab_shop_car);
        mTabs[TAB_PERSONCENTER] = findViewById(R.id.rl_tab_person_center);
        mTabs[TAB_SELF].setSelected(true);
    }

    @Override
    protected void doOperate() {
        appVerGet();
        showToastPermisionDialog();
        // 登陆状态去取红包
        if (WWApplication.getInstance().isLogin()) {
            getRedPackegeInfo();
        }
    }

    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    /**
     * 底部tab点击事件
     *
     * @param view
     * @author xl
     * @date:2016-7-25下午1:00:06
     * @description
     */
    public void onTabClick(View view) {
        switch (view.getId()) {
            case R.id.rl_tab_homepage:
                mIndex = TAB_HOMEPAGE;
                break;
            case R.id.rl_tab_selfsupport:
                mIndex = TAB_SELF;
                break;
//            case R.id.rl_tab_distribution:
//                mIndex = TAB_DISTRIBUTION;
//                break;
            case R.id.rl_tab_shop_car:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, false));
                    return;
                }
//			mOrderFragment.doRequest();

                mIndex = TAB_SHOPCAR;

                break;
            case R.id.rl_tab_person_center:
                if (!WWApplication.getInstance().isLogin()) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class).putExtra(LoginActivity.ISFRAOMMAIN, false));
                    break;
                }

                mIndex = TAB_PERSONCENTER;
                break;
            default:
                break;
        }
        setSelectTab();

    }

    private void setSelectTab() {
        if (mCurrentTabIndex != mIndex) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mFragments[mCurrentTabIndex]);
            if (!mFragments[mIndex].isAdded()) {
                trx.add(R.id.rl_container, mFragments[mIndex]);
            }
            trx.show(mFragments[mIndex]).commit();
        }
        mTabs[mCurrentTabIndex].setSelected(false);
        mTabs[mIndex].setSelected(true);
        mCurrentTabIndex = mIndex;
    }

    // 不去区分业务模式了，之后再改
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean sessionError = intent.getBooleanExtra(Consts.KEY_SESSION_ERROR,
                false);

        //纯拼音，没毛病
        boolean loginSuccess = intent.getBooleanExtra("LoginSuccess",
                false);
        boolean isexit = intent.getBooleanExtra("isexit", false);
        boolean indexSelect = intent.getBooleanExtra("indexSelect", false);
        boolean fenxiaoSelect = intent.getBooleanExtra("fenxiaoSelect", false);
        boolean personCenterSelect = intent.getBooleanExtra(
                "personCenterSelect", false);
        if (personCenterSelect) {
            // 个人中心
            if (!WWApplication.getInstance().isLogin()) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            } else {
                mIndex = TAB_PERSONCENTER;
                setSelectTab();
            }
        } else if (fenxiaoSelect) {
            // 分销中心
//            if (!WWApplication.getInstance().isLogin()) {
//                startActivity(new Intent(MainActivity.this, LoginActivity.class));
//
//            } else {
//                mIndex = TAB_DISTRIBUTION;
//                setSelectTab();
//            }
        } else if (isexit || indexSelect) {
            // 退出登录切换到首页
            mIndex = TAB_HOMEPAGE;
            setSelectTab();
        } else if (sessionError) {// session相关错误
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        if (loginSuccess) {
            // 登陆状态去取红包
            if (WWApplication.getInstance().isLogin()) {
                getRedPackegeInfo();
            }
        }
    }

    public void setTabs(int i) {
        mIndex = i;
        setSelectTab();
    }

    private void showToastPermisionDialog() {
        if (!WWToast.isFloatWindowOpAllowed(this)) {
            final CommonDialog commonDialogTwiceConfirm = DialogUtils.getCommonDialogTwiceConfirm(this, R.string.floating_window_closed, false);
            commonDialogTwiceConfirm.setRightButtonText(R.string.goto_setting);
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
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
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
