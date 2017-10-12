package cn.net.chenbao.qbyp.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.dialog.ImageToLargeDialog;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.CommonDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 * 服务商家详情
 */

public class ServiceProviderShopDetailActivity extends FatherActivity implements PermissionUtil.PermissionCallbacks{
    @BindView(R.id.iv_seller_shop_imgs)
    ImageView ivSellerShopImgs;
    @BindView(R.id.tv_seller_names)
    TextView tvSellerNames;
    @BindView(R.id.tv_industry_type)
    TextView tvIndustryType;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_auth_state)
    TextView tvAuthState;
    @BindView(R.id.tv_service_time)
    TextView tvServiceTime;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_legal_name)
    TextView tvLegalName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    private AgencyInfo agencyInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.act_service_provider_shopdetail;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.agency_shop_detail, true);

    }

    @Override
    protected void initView() {
        agencyInfo = JSON.parseObject(getIntent().getStringExtra(Consts.KEY_DATA), AgencyInfo.class);

    }

    @Override
    protected void doOperate() {
        ImageUtils.setCommonImage(this, agencyInfo.ShopPicture, ivSellerShopImgs);
        tvSellerNames.setText(agencyInfo.SellerName);
        tvIndustryType.setText(agencyInfo.TradeName);
        switch (agencyInfo.ChargeType) {
            case 0:
                tvServiceType.setText(R.string.no_pay2);
                break;
            case 1:
                tvServiceType.setText(R.string.try_use);
                break;
            case 2:
                tvServiceType.setText(R.string.month_fee);
                break;
            case 3:
                tvServiceType.setText(R.string.year_fee);
                break;
            default:
                break;
        }

        switch (agencyInfo.Status) {
            case 0:
                tvState.setText(R.string.entering);
                tvAuthState.setText(R.string.no_auth);
                tvAuthState.setBackgroundResource(R.drawable.no_auth_state_shape);
                break;
            case 1:
                tvState.setText(R.string.normal_busyness);
                tvAuthState.setText(R.string.authed);
                tvAuthState.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            case 2:
                tvState.setText(R.string.close_business);
                tvAuthState.setText(R.string.authed);
                tvAuthState.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            case 3:
                tvState.setText(R.string.freeze);
                tvAuthState.setText(R.string.authed);
                tvAuthState.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            default:
                break;
        }

        tvServiceTime
                .setText(agencyInfo.ExpireTime != 0 ? TimeUtil.getOnlyDateToS2(agencyInfo.ExpireTime * 1000) : "未开通服务");

        tvLegalName.setText(agencyInfo.LegalName);
        tvUserPhone.setText(agencyInfo.SellerTel);
        tvAddress.setText(agencyInfo.Address);
    }

    private final int REQUEST_CODE_CALL_PHONE = 110;
    @OnClick({R.id.ll_phone, R.id.ll_address, R.id.iv_seller_shop_imgs})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_seller_shop_imgs:
                ImageToLargeDialog imageToLargeDialog = new ImageToLargeDialog(
                        this, agencyInfo.ShopPicture);
                imageToLargeDialog.show();
                break;
            case R.id.ll_phone:

                final CommonDialog commonDialog = DialogUtils.getCommonDialog(this, R.string.connect_seller_right_now);
                commonDialog.getButtonLeft(R.string.cancel).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.getButtonRight(R.string.call_phone).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (PermissionUtil.hasPermissions(ServiceProviderShopDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE})) {
                            call();
                        } else {
                            PermissionUtil.requestPermissions(ServiceProviderShopDetailActivity.this, REQUEST_CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE});
                        }

                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;
            case R.id.ll_address:
                Intent intent2 = new Intent();
                intent2.putExtra("Latitude", agencyInfo.Latitude);
                intent2.putExtra("Longitude", agencyInfo.Longitude);
                intent2.setClass(this, AgencySellerMapActivity.class);
                startActivity(intent2);
                break;
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                .parse("tel:" + agencyInfo.UserMobile));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        call();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!PermissionUtil.hasPermissions(ServiceProviderShopDetailActivity.this, list.get(i))) {
                WWToast.showShort(String.format(getString(R.string.permission_deny_),list.get(i)));
            }
        }
    }
}
