package cn.net.chenbao.qbyp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiPay;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.api.ApiVariable;
import cn.net.chenbao.qbyp.bean.ApplyServiceProvider;
import cn.net.chenbao.qbyp.bean.PayWay;
import cn.net.chenbao.qbyp.bean.WXPay;
import cn.net.chenbao.qbyp.dialog.PayWayDialog;
import cn.net.chenbao.qbyp.utils.AlipayPayResult;
import cn.net.chenbao.qbyp.utils.Constants;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PayUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.wxapi.WXPayEntryActivity;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by ppsher on 2017/2/13.
 * Description:申请成为服务商
 */

public class ServiceProviderApplyActivity extends FatherActivity implements View.OnClickListener {

    private TextView tv_service_fee;//后台可以动态改变服务费
    private IWXAPI wxapi;
    private ApplyServiceProvider applyServiceProvider;
    private TextView tv_area;
    private double allFee;
    private double placeFee;
    private String provinceId = "";
    private String cityId = "";
    private String countyId = "";
    private String serviceType = "0";

    @Override
    protected int getLayoutId() {
        return R.layout.act_fuwushang_pay;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.service_fee_pay, true);
    }

    @Override
    protected void initView() {
        tv_service_fee = (TextView) findViewById(R.id.tv_service_fee);
        tv_area = (TextView) findViewById(R.id.tv_area);
        findViewById(R.id.ll_choice_area).setOnClickListener(this);
        findViewById(R.id.btn_confirm_apply).setOnClickListener(this);
        wxapi = WXAPIFactory.createWXAPI(ServiceProviderApplyActivity.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);
    }

    @Override
    protected void doOperate() {
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        tv_area.setText(R.string.all_position);//default  fee
        getServiceCost(0);//全国
        getServiceCost(111111);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void getServiceCost(final int areaId) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiVariable.ServiceCost());
        params.addBodyParameter("county", areaId + "");
        x.http().get(params, new WWXCallBack("ServiceCost") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                double serviceShopFee = data.getDoubleValue("Data");
                if (areaId == 0) {
                    allFee = serviceShopFee;
                    tv_service_fee.setText(WWViewUtil.numberFormatPrice(allFee));
                } else {
                    placeFee = serviceShopFee;
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void clickSubmit() {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("serviceType", serviceType);
        if ("1".equals(serviceType)) {
            jsonObject.put("province", provinceId);
            jsonObject.put("city", cityId);
            jsonObject.put("county", countyId);
        }
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject,
                ApiUser.ServiceShopSubmit()), new WWXCallBack("ServiceShopSubmit") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                applyServiceProvider = JSON.parseObject(data.getString("Data"), ApplyServiceProvider.class);
                PayWayDialog payWayDialog = new PayWayDialog(ServiceProviderApplyActivity.this, PayWayDialog.SERVICE_APPLY, R.style.DialogStyle, new PayWayDialog.OnSelectOKLisentner() {
                    @Override
                    public void selectPayWay(PayWay payWay) {
                        gotoPay(payWay);
                    }
                });
                payWayDialog.show();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void gotoPay(PayWay payWay) {
        final String payWayCodeStr = payWay.PayCode;
        RequestParams params = new RequestParams(ApiPay.OrderPayGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", applyServiceProvider.ServiceId + "");
        params.addBodyParameter("payCode", payWay.PayCode);
        showWaitDialog();
        x.http().get(params, new WWXCallBack("OrderPayGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (payWayCodeStr.equals(Consts.GHTNET_PAY)) {
                    PublicWay.startWebViewActivityForResult(
                            ServiceProviderApplyActivity.this, "网关支付",
                            data.getString("Data"),
                            WebViewActivity.Pay,
                            WebViewActivity.REQUEST_CODE);
                }else if(payWayCodeStr.equals(Consts.FuYou)){
                    PublicWay.startWebViewActivityForResult(
                            ServiceProviderApplyActivity.this, getString(R.string.fuyou_pay),
                            data.getString("Data"), WebViewActivity.Pay,  WebViewActivity.REQUEST_CODE);
                } else if (payWayCodeStr.equals(Consts.WX_PAY)) {
                    if (wxapi.isWXAppInstalled()) {
                        String string = data.getString("Data");
                        WXPay wxPay = JSON.parseObject(string, WXPay.class);
                        PayReq req = new PayReq();
                        req.appId = wxPay.appid;
                        req.partnerId = wxPay.partnerid;
                        req.prepayId = wxPay.prepayid;
                        req.nonceStr = wxPay.noncestr;
                        req.packageValue = "Sign=WXPay";
                        req.timeStamp = wxPay.timestamp;
                        req.sign = wxPay.sign;
                        wxapi.sendReq(req);
                    } else {
                        WWToast.showShort(R.string.please_download_wx_app);
                    }
                } else if (payWayCodeStr.equals(Consts.ALI_PAY)) {
                    PayUtils.pay(ServiceProviderApplyActivity.this,
                            data.getString("Data"), new PayUtils.PayResultLisentner() {
                                @Override
                                public void PayResult(String payResult,
                                                      int payType) {
                                    // TODO Auto-generated method stub
                                    AlipayPayResult payResult2 = new AlipayPayResult(
                                            payResult);
                                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                                    String resultStatus = payResult2.getResultStatus();
                                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                    if (TextUtils.equals(resultStatus, "9000")) {
                                        // 支付成功
                                        PublicWay.stratPayFeeActivity(ServiceProviderApplyActivity.this,
                                                PayFeeSuccessActivity.SUCCESS, 0, 0);
                                        finish();
                                    } else {
                                        // 判断resultStatus 为非“9000”则代表可能支付失败
                                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                        if (TextUtils.equals(resultStatus, "8000")) {
                                            WWToast.showShort(R.string.pay_result_is_confirming);
                                            finish();
                                        } else {
                                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                            WWToast.showShort(R.string.pay_fail);
                                        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 888 && resultCode == RESULT_OK) {//区域地址
            if (data != null) {
                if (getString(R.string.all_position).equals(data.getStringExtra("province_name"))) {
                    serviceType = "0";
                    tv_area.setText(R.string.all_position);
                    tv_service_fee.setText(WWViewUtil.numberFormatPrice(allFee));
                    return;
                }
                serviceType = "1";
                provinceId = data.getStringExtra("province_id");
                cityId = data.getStringExtra("city_id");
                countyId = data.getStringExtra("county_id");

                StringBuffer sbf = new StringBuffer();
                sbf.append(data.getStringExtra("province_name") + " ");
                sbf.append(data.getStringExtra("city_name") + " ");
                sbf.append(data.getStringExtra("county_name"));
                tv_area.setText(sbf.toString());
                tv_service_fee.setText(WWViewUtil.numberFormatPrice(placeFee));
            }
        } else if (requestCode == WebViewActivity.REQUEST_CODE) {//支付
            if (resultCode == RESULT_OK) {
                PublicWay.stratPayFeeActivity(ServiceProviderApplyActivity.this,
                        PayFeeSuccessActivity.SUCCESS, 0, 0);
                finish();
            } else {
                WWToast.showShort(R.string.pay_fail);
            }

        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    WWToast.showShort(R.string.apply_for_success);
                    finish();
                    PublicWay.stratPayFeeActivity(ServiceProviderApplyActivity.this, PayFeeSuccessActivity.SUCCESS, 0, 0);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消支付
                case BaseResp.ErrCode.ERR_COMM:// 错误
                    WWToast.showShort(R.string.pay_fail);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_choice_area:
                PublicWay.startRegionPickActivity(ServiceProviderApplyActivity.this, 888, RegionPickActivity.CONTAIN_ALL);
                break;
            case R.id.btn_confirm_apply:
                clickSubmit();
                break;
        }
    }

}
