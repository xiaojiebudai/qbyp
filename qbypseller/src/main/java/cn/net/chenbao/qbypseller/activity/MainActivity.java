package cn.net.chenbao.qbypseller.activity;

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
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.api.ApiTrade;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.bean.SellerInfo;
import cn.net.chenbao.qbypseller.bean.VersionInfo;
import cn.net.chenbao.qbypseller.dialog.ChoosePhotoDialog;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.service.UpdateVersionService;
import cn.net.chenbao.qbypseller.utils.Arith;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.view.RatingBar;
import cn.net.chenbao.qbypseller.view.ScrollBanner;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 旺旺商家
 *
 * @author xl
 * @description
 */
public class MainActivity extends FatherActivity implements OnClickListener, PermissionUtil.PermissionCallbacks {
    private static final int REQUEST_CODE_SET_HEAD = 10;
    private static final int REQUEST_CODE_READ_PHONE_STATE = 1102;
    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1101;
    private static final int EXIT = 1000;
    private ScrollBanner tv_message;
    private TextView tv_news_count;
    private RelativeLayout rl_image_bg, rl_title;
    private SellerInfo mSellerInfo;
    private VersionInfo version;
    private TextView tv_wait_confirm_tips_num;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    @Override
    protected void initView() {
        findViewById(R.id.rl_head_right).setOnClickListener(this);
        findViewById(R.id.ll_my_goods).setOnClickListener(this);
        findViewById(R.id.ll_system_setting).setOnClickListener(this);
        findViewById(R.id.ll_account_safe).setOnClickListener(this);
        findViewById(R.id.ll_ship_setting).setOnClickListener(this);
        findViewById(R.id.rl_wait_receive_order)
                .setOnClickListener(this);
        findViewById(R.id.ll_all_order).setOnClickListener(this);
        findViewById(R.id.rl_wait_pay).setOnClickListener(this);
        findViewById(R.id.rl_wait_receive_goods)
                .setOnClickListener(this);
        findViewById(R.id.rl_has_cancel).setOnClickListener(this);

        findViewById(R.id.ll_wallet).setOnClickListener(this);
        findViewById(R.id.ll_goods_publish).setOnClickListener(this);
        findViewById(R.id.iv_image).setOnClickListener(this);
        findViewById(R.id.pohto_set).setOnClickListener(this);
        findViewById(R.id.rl_image_bg).setOnClickListener(this);
        findViewById(R.id.ll_offline_order).setOnClickListener(this);
        findViewById(R.id.ll_offline).setOnClickListener(this);
        tv_news_count = (TextView) findViewById(R.id.tv_news_count);
        tv_wait_confirm_tips_num = (TextView) findViewById(R.id.tv_wait_confirm_tips_num);
        tv_message = (ScrollBanner) findViewById(R.id.tv_message);
        tv_message.setOnClickListener(this);
        rl_image_bg = (RelativeLayout) findViewById(R.id.rl_image_bg);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        //重新设置高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl_title.getLayoutParams();
            params.topMargin = getStatusBarHeight(this);
            rl_title.setLayoutParams(params);
            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) rl_image_bg.getLayoutParams();
            params1.height = DensityUtil.dip2px(this, 135) + getStatusBarHeight(this);
            rl_image_bg.setLayoutParams(params1);
            rl_image_bg.setPadding(DensityUtil.dip2px(this, 15), DensityUtil.dip2px(this, 54) + getStatusBarHeight(this), DensityUtil.dip2px(this, 15), 0);
        }

        //处理友盟统计关键权限
        ArrayList<String> list = new ArrayList<String>();
        if (PermissionUtil.hasPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE})) {
            MobclickAgent.setScenarioType(MainActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        } else {
            list.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (list.size() > 0) {
            PermissionUtil.requestPermissions(this, REQUEST_CODE_READ_PHONE_STATE, (String[]) list.toArray(new String[list.size()]));
        }

        appVerGet();
        showToastPermisionDialog();
    }

    @Override
    protected void doOperate() {
        // startActivity(new Intent(this, BusinessRegisterActivity.class));
    }

    /**
     * 获取消息数据
     */
    private void getMessageInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.InterMessageCount());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());

        x.http().get(params, new WWXCallBack("InterMessageCount") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getString("Data").equals("0")) {
                    tv_news_count.setVisibility(View.GONE);
                } else {
                    tv_news_count.setVisibility(View.VISIBLE);
                    tv_news_count.setText((data.getIntValue("Data") < 100) ? data.getString("Data") : "99");
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (mSellerInfo == null) {
            requestData();
            return;
        }
        switch (v.getId()) {
            case R.id.rl_head_right:
                startActivityForResult(new Intent(this,
                        SystemNewsActivity.class), 0);
                break;
            case R.id.ll_wallet:
                startActivityForResult(new Intent(this,
                        WalletActivity.class), 0);
                break;
            case R.id.tv_message:
                startActivityForResult(new Intent(this,
                        SystemNewsActivity.class), 0);
                break;
            case R.id.ll_my_goods:// 我的商品
                PublicWay.startGoodsManageActivity(this, 0);
                break;
            case R.id.ll_goods_publish:// 商品发布
                PublicWay.startAddGoodsActivity(this, 0,
                        AddGoodsActivity.MODULE_DIRECT);
                break;
            case R.id.ll_system_setting:
                startActivityForResult(new Intent(
                        this, SystemSettingActivity.class), EXIT);
                break;
            case R.id.ll_account_safe:
                Intent intent = new Intent(this, AccountSafeActivity.class);
                intent.putExtra("phone", mSellerInfo.UserMobile);
                startActivity(intent);
                break;
            case R.id.ll_ship_setting:
                startActivity(new Intent(this, ShipSettingActivity.class));
                break;
            case R.id.ll_all_order:// 所有订单
                PublicWay.startOrderAcountActivity(this,
                        Order.STATE_WAIT_SELLER_CONFIRM, 0);
                break;
            case R.id.rl_wait_receive_order:// 待接单
                PublicWay.startOrderAcountActivity(this,
                        Order.STATE_WAIT_SELLER_CONFIRM, 0);
                break;
            case R.id.rl_wait_pay:// 待付款
                PublicWay.startOrderAcountActivity(this, Order.STATE_WAIT_PAY, 0);
                break;
            case R.id.rl_wait_receive_goods:// 待收货
                PublicWay.startOrderAcountActivity(this,
                        Order.STATE_WAIT_BUYER_CONFIRM, 0);
                break;
            case R.id.rl_has_cancel:// 已取消
                PublicWay.startOrderAcountActivity(this, Order.STATE_CANCEL, 0);
                break;

//		case R.id.ll_store_zizhi:// 商铺资质
//			PublicWay.startStoreSettingActivity(getActivity(),
//					StoreSettingActivity.MODULE_STORE_RES, 0);
//			break;
            case R.id.iv_image:
                ChoosePhotoDialog dialog = new ChoosePhotoDialog(this, true, true,
                        REQUEST_CODE_SET_HEAD);
                dialog.show();
                break;
            case R.id.pohto_set:
                ChoosePhotoDialog dialog1 = new ChoosePhotoDialog(this, true, true,
                        REQUEST_CODE_SET_HEAD);
                dialog1.show();
                break;
            case R.id.rl_image_bg:
                PublicWay.startStoreSettingActivity(this,
                        StoreSettingActivity.MODULE_STORE_INFO, 0);

                break;
            case R.id.ll_offline:
                //线下收款
                startActivity(new Intent(this, OffLineOrderActivity.class));
                break;
            case R.id.ll_offline_order:
                //线下订单
                startActivity(new Intent(this, OffLineOrderListActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        if (!TextUtils.isEmpty(SharedPreferenceUtils.getInstance().getSessionId())) {
            getMessageInfo();
            requestData();
            getOrderCount();
        }
        super.onResume();
    }

    private void getOrderCount() {
        RequestParams params = new RequestParams(ApiTrade.orderStatusCount());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("status", Order.STATE_WAIT_SELLER_CONFIRM + "");
        x.http().get(params, new WWXCallBack("OrderStatusCount") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<Integer> list = JSONArray.parseArray(data.getString("Data"), Integer.class);
                if (list.get(0) != 0) {
                    tv_wait_confirm_tips_num.setText((list.get(0) < 100) ? list.get(0) + "" : "99");
                    tv_wait_confirm_tips_num.setVisibility(View.VISIBLE);
                } else
                    tv_wait_confirm_tips_num.setVisibility(View.GONE);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private void requestData() {
        showWaitDialog();
//        getCurrentMonthOrderCount();
//		requestMessageData();
        RequestParams params = new RequestParams(ApiSeller.infoGet());
        params.addQueryStringParameter(Api.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params,
                new WWXCallBack("InfoGet") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        ZLog.showPost(data.toJSONString());
                        JSONObject object = data.getJSONObject(Api.KEY_DATA);
                        if (object != null) {
                            String info_json = object.toJSONString();
                            SharedPreferenceUtils instance = SharedPreferenceUtils
                                    .getInstance();
                            instance.saveSellerInfo(info_json);
                            mSellerInfo = JSON.parseObject(info_json,
                                    SellerInfo.class);
                            instance.savePhoneNum(mSellerInfo.UserMobile);
                            setSellerInfoDataToUi();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    private void setSellerInfoDataToUi() {
        ImageUtils.setOwnRadiusImage(this, mSellerInfo.ShopPicture,
                (ImageView) findViewById(R.id.iv_image), 3);
        ((TextView) findViewById(R.id.tv_business_address))
                .setText(mSellerInfo.Address);
        ((TextView) findViewById(R.id.tv_hot)).setText(getString(
                R.string.format_store_hot, mSellerInfo.FavNum));
        ((TextView) findViewById(R.id.tv_title))
                .setText(mSellerInfo.SellerName);
        ((RatingBar) findViewById(R.id.rr)).setStar(Float
                .parseFloat(mSellerInfo.JudgeLevel));
        ((TextView) findViewById(R.id.tv_store_starts))
                .setText(getString(R.string.format_star_store, Arith.round(
                        Double.parseDouble(mSellerInfo.JudgeLevel), 1) + ""));
//		((TextView) mGroup.findViewById(R.id.tv_peibi))
//				.setText("配比"+(int)(mSellerInfo.consumePay*100)+"%");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FragmentActivity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SET_HEAD:
                    if (data != null) {
                        showWaitDialog();
                        String url = data.getStringExtra(Consts.KEY_DATA);
                        x.http().post(ParamsUtils.getPostImageParams(url),
                                new WWXCallBack("UpImage") {

                                    @Override
                                    public void onAfterSuccessOk(JSONObject data) {
                                        final String imageUrl = data
                                                .getString(Api.KEY_DATA);
                                        JSONObject object = new JSONObject();
                                        object.put("shopPicture", imageUrl);
                                        RequestParams params = ParamsUtils
                                                .getPostJsonParamsWithSession(
                                                        object,
                                                        ApiSeller.infoHeadUpdate());

                                        x.http()
                                                .post(params,
                                                        new WWXCallBack(
                                                                "InfoHeadUpdate") {

                                                            @Override
                                                            public void onAfterSuccessOk(
                                                                    JSONObject data) {
                                                                mSellerInfo.ShopPicture = imageUrl;
                                                                ImageUtils
                                                                        .setOwnRadiusImage(
                                                                                MainActivity.this,
                                                                                mSellerInfo.ShopPicture,
                                                                                (ImageView) findViewById(R.id.iv_image), 3);
                                                            }

                                                            @Override
                                                            public void onAfterFinished() {
                                                                dismissWaitDialog();
                                                            }
                                                        });
                                    }

                                    @Override
                                    public void onError(Throwable arg0, boolean arg1) {
                                        super.onError(arg0, arg1);
                                        dismissWaitDialog();
                                    }

                                    @Override
                                    public void onAfterFinished() {
                                        dismissWaitDialog();
                                    }
                                });
                    }
                    break;
                case EXIT:
                    startActivity(new Intent(this,
                            BusinessLoginActivity.class));
                    finish();
                    break;
                default:
                    break;
            }
        }
    }

    private void getCurrentMonthOrderCount() {
        showWaitDialog();
        RequestParams params = ParamsUtils.getSessionParams(ApiSeller
                .orderCurMonthCount());
        x.http().get(params, new WWXCallBack("OrderCurMonthCount") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ((TextView) findViewById(R.id.tv_order_count))
                        .setText(getString(
                                R.string.format_order_month_count,
                                data.getIntValue(Api.KEY_DATA)));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
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
                                                if (PermissionUtil.hasPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                                                    update(version);
                                                } else {
                                                    PermissionUtil.requestPermissions(MainActivity.this, REQUEST_CODE_WRITE_EXTERNAL_STORAGE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
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
        progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
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
        } else if (requestCode == REQUEST_CODE_READ_PHONE_STATE) {
            MobclickAgent.setScenarioType(MainActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean sessionError = intent.getBooleanExtra(Consts.KEY_SESSION_ERROR,
                false);
        if (sessionError) {// session相关错误
            startActivity(new Intent(this, BusinessLoginActivity.class));
            finish();
        }
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
