package cn.net.chenbao.qbyp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.ShareUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.utils.Constants;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by wuri on 2016/11/1.
 */

public class ShareActivity extends FatherActivity implements View.OnClickListener, PermissionUtil.PermissionCallbacks {

    private ImageView mHead;
    private TextView mName;
    private TextView mPhone;
    private TextView mInviteCode;
    private ImageView mErweima;
    private ShareAction doShare;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_share;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.share, true);
    }

    @Override
    protected void initView() {
        mHead = (ImageView) findViewById(R.id.iv_head);
        mName = (TextView) findViewById(R.id.tv_name);
        mPhone = (TextView) findViewById(R.id.tv_phone);
        mInviteCode = (TextView) findViewById(R.id.tv_invite_code);
        mErweima = (ImageView) findViewById(R.id.iv_erweima);
        findViewById(R.id.tv_invite).setOnClickListener(this);
    }

    @Override
    protected void doOperate() {
        doRequest();
    }


    private void doRequest() {
        RequestParams params = new RequestParams(ApiUser.getSharErweima());
        params.addBodyParameter("sessionId", SharedPreferenceUtils
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("InviterGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject jsonObject = data.getJSONObject("Data");
                User user = JSON.parseObject(jsonObject.toJSONString(),
                        User.class);
                if (user != null) {
                    ImageUtils.setCircleHeaderImage(ShareActivity.this, user.HeadUrl,
                            mHead);
                    mName.setText(user.UserName);
                    mPhone.setText(user.Mobile);
                    ImageUtils.setCommonImage(ShareActivity.this, user.BarcodeUrl, mErweima);
                    mInviteCode.setText(user.InviterNo);
                    doShare = ShareUtils.doShare(ShareActivity.this,
                            "http://img.chenbao.net.cn/Image/t1/e1dbeba455d24d4aba9053994f0c4d72__.png",//seller :http://img.szhysy.cn/Image/t1/2984c3fb292a4774af2c25b671cca1d0__.png
                            getString(R.string.share_title),
                            getString(R.string.share_content), new CustomShareListener(ShareActivity.this),
                            Constants.SHARE_URL + user.InviterNo);
                    doShare.setShareboardclickCallback(new ShareBoardlistener() {
                        @Override
                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                            if (share_media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
                                doShare.withTitle(getString(R.string.share_content));
                            } else {
                                doShare.withTitle(getString(R.string.share_title));
                            }
                            doShare.setPlatform(share_media);
                            if (PermissionUtil.hasPermissions(ShareActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_EXTERNAL_STORAGE})) {
                                doShare.share();
                            } else {
                                PermissionUtil.requestPermissions(ShareActivity.this, 1111, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.READ_EXTERNAL_STORAGE});
                            }


                        }
                    });
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
        switch (v.getId()) {
            case R.id.tv_invite:// 分享
                ShareBoardConfig config = new ShareBoardConfig();
                config.setTitleVisibility(false); // 隐藏title
                config.setCancelButtonVisibility(false); // 隐藏取消按钮
                if (doShare == null) {
                    WWToast.showShort(R.string.date_no_get);
                    return;
                }
                doShare.open(config);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        doShare.share();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        WWToast.showShort(R.string.permission_deny_cannot_execute_share_operate);
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<ShareActivity> mActivity;

        private CustomShareListener(ShareActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            WWToast.showShort(R.string.share_success);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            WWToast.showShort(R.string.share_fail);

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            WWToast.showShort(R.string.share_cancel);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        doShare.close();
    }
}
