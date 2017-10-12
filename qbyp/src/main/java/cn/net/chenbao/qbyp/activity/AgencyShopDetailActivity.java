package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.dialog.ImageToLargeDialog;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.CommonDialog;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

/***
 * Description:区域代理-商家详情 Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyShopDetailActivity extends FatherActivity implements
        OnClickListener, PermissionUtil.PermissionCallbacks {

    private ImageView iv_seller_shop_imgs;
    private TextView tv_seller_names;
    private TextView tv_industry_type;
    private TextView tv_service_type;
    private TextView tv_auth_state;
    private TextView tv_service_time;
    private TextView tv_state;
    private TextView tv_legal_name;
    private TextView tv_user_phone;
    private TextView tv_shop_address;
    private TextView tv_area_name;
    private AgencyInfo agencyInfo;
    private final int REQUEST_CODE_CALL_PHONE = 110;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_agency_shop_detail;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.agency_shop_detail, true);
    }

    @Override
    protected void initView() {
        agencyInfo = JSON.parseObject(getIntent().getStringExtra(Consts.KEY_DATA), AgencyInfo.class);
        iv_seller_shop_imgs = (ImageView) findViewById(R.id.iv_seller_shop_imgs);
        tv_seller_names = (TextView) findViewById(R.id.tv_seller_names);
        tv_industry_type = (TextView) findViewById(R.id.tv_industry_type);
        tv_service_type = (TextView) findViewById(R.id.tv_service_type);
        tv_auth_state = (TextView) findViewById(R.id.tv_auth_state);
        tv_service_time = (TextView) findViewById(R.id.tv_service_time);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_legal_name = (TextView) findViewById(R.id.tv_legal_name);
        tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
        tv_shop_address = (TextView) findViewById(R.id.tv_shop_address);
        tv_area_name = (TextView) findViewById(R.id.tv_area_name);
        findViewById(R.id.rl_call_phone).setOnClickListener(this);
        findViewById(R.id.rl_address).setOnClickListener(this);
        iv_seller_shop_imgs.setOnClickListener(this);
    }

    @Override
    protected void doOperate() {
        // TODO Auto-generated method stub

        ImageUtils.setCommonImage(this, agencyInfo.ShopPicture, iv_seller_shop_imgs);
        tv_seller_names.setText(agencyInfo.SellerName);
        tv_industry_type.setText(agencyInfo.TradeName);
        switch (agencyInfo.ChargeType) {
            case 0:
                tv_service_type.setText(R.string.no_pay2);
                break;
            case 1:
                tv_service_type.setText(R.string.try_use);
                break;
            case 2:
                tv_service_type.setText(R.string.month_fee);
                break;
            case 3:
                tv_service_type.setText(R.string.year_fee);
                break;
            default:
                break;
        }

        switch (agencyInfo.Status) {
            case 0:
                tv_state.setText(R.string.entering);
                tv_auth_state.setText(R.string.no_auth);
                tv_auth_state.setBackgroundResource(R.drawable.no_auth_state_shape);
                break;
            case 1:
                tv_state.setText(R.string.normal_busyness);
                tv_auth_state.setText(R.string.authed);
                tv_auth_state.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            case 2:
                tv_state.setText(R.string.close_business);
                tv_auth_state.setText(R.string.authed);
                tv_auth_state.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            case 3:
                tv_state.setText(R.string.freeze);
                tv_auth_state.setText(R.string.authed);
                tv_auth_state.setBackgroundResource(R.drawable.authed_state_shape);
                break;
            default:
                break;
        }

        tv_service_time
                .setText(agencyInfo.ExpireTime != 0 ? TimeUtil.getOnlyDateToS2(agencyInfo.ExpireTime * 1000) : "未开通服务");

        tv_legal_name.setText(agencyInfo.LegalName);
        tv_user_phone.setText(agencyInfo.SellerTel);
        tv_shop_address.setText(agencyInfo.Address);
        tv_area_name.setText(agencyInfo.AreaName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_seller_shop_imgs:
                ImageToLargeDialog imageToLargeDialog = new ImageToLargeDialog(
                        this, agencyInfo.ShopPicture);
                imageToLargeDialog.show();
                break;
            case R.id.rl_call_phone:


                final CommonDialog commonDialog = DialogUtils.getCommonDialog(this, R.string.connect_seller_right_now);
                commonDialog.getButtonLeft(R.string.cancel).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.getButtonRight(R.string.call_phone).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (PermissionUtil.hasPermissions(AgencyShopDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE})) {
                            call();
                        } else {
                            PermissionUtil.requestPermissions(AgencyShopDetailActivity.this, REQUEST_CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE});
                        }
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();

                break;
            case R.id.rl_address:
                Intent intent2 = new Intent();
                intent2.putExtra("Latitude", agencyInfo.Latitude);
                intent2.putExtra("Longitude", agencyInfo.Longitude);
                intent2.setClass(this, AgencySellerMapActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }

    }

    public void call() {
        String phoneNumber = tv_user_phone.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                .parse("tel:" + phoneNumber));
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
            if (!PermissionUtil.hasPermissions(AgencyShopDetailActivity.this, list.get(i))) {
                WWToast.showShort(String.format(getString(R.string.permission_deny_), list.get(i)));
            }
        }
    }
}
