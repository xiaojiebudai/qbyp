package cn.net.chenbao.qbypseller.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.bean.VersionInfo;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.service.UpdateVersionService;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by wuri on 2016/11/17.
 */

public class AppVersionExplainActivity extends FatherActivity implements View.OnClickListener ,PermissionUtil.PermissionCallbacks{
    private TextView tvOldOrNewest;
    private TextView tvCurrentVersionCode;
    private TextView tvCheckNewVersion;
    private String currentVersionName;
    private int currentVersionCode;
    private VersionInfo version;

    @Override
    protected int getLayoutId() {
        return R.layout.act_app_version_explain;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.version_expalin, true);
        tvOldOrNewest = (TextView) findViewById(R.id.tv_old_or_newest);
        tvCurrentVersionCode = (TextView) findViewById(R.id.tv_current_verssion_code);
        tvCheckNewVersion = (TextView) findViewById(R.id.tv_check_new_verssion);
        tvCheckNewVersion.setOnClickListener(this);
        currentVersionName = getCurrentVerssionName();
        if (!TextUtils.isEmpty(currentVersionName)) {
            tvCurrentVersionCode.setText("V" + currentVersionName);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {
        getLatestVerssionCode();
    }

    private String getCurrentVerssionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            currentVersionCode = info.versionCode;
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getLatestVerssionCode() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.AppVerGet()),
                new WWXCallBack("AppVerGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        version = JSON.parseObject(
                                data.getString("Data"), VersionInfo.class);
                        if (currentVersionCode != 0) {
                            if (version.InterNo == currentVersionCode) {
                                tvOldOrNewest.setText(R.string.current_latest_version_colon);
                                tvCheckNewVersion.setVisibility(View.INVISIBLE);
                            } else {
                                tvOldOrNewest.setText(R.string.current_version_colon);
                                tvCheckNewVersion.setVisibility(View.VISIBLE);
                            }
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
        final CommonDialog commonDialog = DialogUtils.getCommonDialog(this, version.Explain);
        commonDialog.setTitleText(R.string.have_new_version);
        commonDialog.getButtonLeft(R.string.ignore).setTextColor(getResources().getColor(R.color.yellow_ww));
        commonDialog.getButtonRight(R.string.updata).setTextColor(getResources().getColor(R.color.yellow_ww));
        commonDialog.setLeftButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });
        commonDialog.setRightButtonCilck(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.hasPermissions(AppVersionExplainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
                    update();
                } else {
                    PermissionUtil.requestPermissions(AppVersionExplainActivity.this, 11111, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private void update() {
        Intent updateIntent = new Intent(AppVersionExplainActivity.this,
                UpdateVersionService.class);
        updateIntent
                .putExtra("titleId", R.string.app_name);
        updateIntent.putExtra("downloadUrl", version.DownloadUrl);
        updateIntent.putExtra("app_desc", version.Explain);
        startService(updateIntent);
        WWToast.showShort(R.string.downloading);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        update();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        WWToast.showShort(R.string.write_permission_deny_cannot_update);
    }
}
