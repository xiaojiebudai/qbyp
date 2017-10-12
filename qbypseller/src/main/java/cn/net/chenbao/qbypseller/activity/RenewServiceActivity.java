package cn.net.chenbao.qbypseller.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiPay;
import cn.net.chenbao.qbypseller.bean.SellerInfo;
import cn.net.chenbao.qbypseller.bean.WXPay;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.wxapi.WXPayEntryActivity;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import cn.net.chenbao.qbypseller.R;

import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Charge;
import cn.net.chenbao.qbypseller.bean.PayWay;
import cn.net.chenbao.qbypseller.utils.AlipayPayResult;
import cn.net.chenbao.qbypseller.utils.Constants;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.PayUtils;
import cn.net.chenbao.qbypseller.utils.PayUtils.PayResultLisentner;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * 续费服务
 *
 * @author xl
 * @date 2016-7-31 下午9:00:05
 * @description
 */
public class RenewServiceActivity extends FatherActivity implements
        OnClickListener {
    private IWXAPI wxapi;
    private SellerInfo mSellerInfo;
    private String sessionId;
    private int mode;
    /**
     * 商家正常自己续费
     */
    public static final int NORMAL_RENEW = 1;
    /**
     * 服务过期时登陆提示强制续费
     */
    public static final int LOGIN_FORCE_RENEW = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.act_renew_service;
    }

    /**
     * 选中的支付项
     */
    private Charge charge_select;
    private int currentCharge = 0;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    showPaySuccessTips();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消支付
                    WWToast.showShort(R.string.pay_fail);

                    break;
                case BaseResp.ErrCode.ERR_COMM:// 错误
                    WWToast.showShort(R.string.pay_fail);

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void initValues() {
        initDefautHead(R.string.renew_service, true);
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        wxapi = WXAPIFactory.createWXAPI(RenewServiceActivity.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);
    }

    @Override
    protected void initView() {
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        if (mode == LOGIN_FORCE_RENEW) {
            sessionId = getIntent().getStringExtra(Consts.KEY_DATA);
        } else {
            sessionId = WWApplication
                    .getInstance().getSessionId();
        }

        requestData();

        if (true) {// 也许是其他支付业务:pay_type
            ((TextView) findViewById(R.id.tv_pay_type))
                    .setText(R.string.open_service);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void requestData() {
        RequestParams params = new RequestParams(ApiSeller.infoGet());
        params.addQueryStringParameter(Api.KEY_SESSIONID, sessionId);
        showWaitDialog();
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
        ((TextView) findViewById(R.id.tv_pay_object))
                .setText(mSellerInfo.SellerName);
        if (mode == LOGIN_FORCE_RENEW) {
            findViewById(R.id.ll_renew_force).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.tv_shop_expire_time)).setText(getString(R.string.shop_tiem_end) + ": "
                    + TimeUtil.getOnlyDate(mSellerInfo.ExpireTime * 1000));
        }
    }

    @Override
    protected void doOperate() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.chargeItemGet());
        params.addBodyParameter(Api.KEY_SESSIONID, sessionId);
        x.http().get(params, new WWXCallBack("ChargeItemGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                final List<Charge> charges = JSON.parseArray(data
                                .getJSONArray(Api.KEY_DATA).toJSONString(),
                        Charge.class);
                if (charges != null && charges.size() > 0) {
                    final LinearLayout ll_container = (LinearLayout) findViewById(R.id.ll_pay_items);
                    for (int i = 0; i < charges.size(); i++) {

                        Charge charge = charges.get(i);
                        View item = getLayoutInflater().inflate(
                                R.layout.item_charge, ll_container,
                                false);
                        ((TextView) item
                                .findViewById(R.id.tv_charge_name))
                                .setText(charge.ItemName);
                        ((TextView) item
                                .findViewById(R.id.tv_charge_money))
                                .setText("¥" + charge.Account);
                        ((TextView) item
                                .findViewById(R.id.tv_charge_des))
                                .setText(getString(
                                        R.string.format_charge_store_expire,
                                        TimeUtil.getOnlyDate(charge.ExpireDate * 1000)));

                        if (i == 0) {
                            item.setSelected(true);
                            currentCharge = 0;
                            charge_select = charges.get(0);
                        }
                        item.setTag(i);
                        item.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                int index = ((Integer) v.getTag())
                                        .intValue();
                                charge_select = charges.get(index);
                                if (currentCharge == index) {
                                    return;
                                }
                                ll_container.getChildAt(currentCharge)
                                        .setSelected(false);
                                ll_container.getChildAt(index)
                                        .setSelected(true);
                                currentCharge = index;
                            }
                        });
                        ll_container.addView(item);
                    }

                }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
        requestPayWay();
    }

    @Override
    public void onClick(View v) {
    }

    private void requestPayWay() {
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Api.KEY_SESSIONID, sessionId);
        params.addBodyParameter("busType", Consts.SELLER_RENEW_PAY_WAY + "");
        showWaitDialog();
        x.http().get(params, new WWXCallBack("PaymentValid") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<PayWay> payways = JSON.parseArray(
                        data.getJSONArray(Api.KEY_DATA).toJSONString(),
                        PayWay.class);
                LinearLayout ll_ways = (LinearLayout) findViewById(R.id.ll_pay_ways);
                if (payways != null) {
                    for (int i = 0; i < payways.size(); i++) {
                        final PayWay payWay = payways.get(i);

                        View view = getLayoutInflater().inflate(
                                R.layout.item_pay_way, null);
                        ImageUtils.setCommonImage(RenewServiceActivity.this,
                                payWay.PayIco,
                                (ImageView) view.findViewById(R.id.iv_image));
                        ((TextView) view.findViewById(R.id.tv_content))
                                .setText(payWay.PayName);
                        view.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                startPay(payWay);
                            }

                        });
                        ll_ways.addView(view);

                    }
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    /**
     * 调起不同的支付
     *
     * @param payWay
     */
    public void startPay(PayWay payWay) {
        if (charge_select == null) {
            WWToast.showShort(R.string.please_choice_service_item);
            return;
        }
        final String payWayCodeStr = payWay.PayCode;
        ZLog.showPost(payWay.PayName + "--" + payWay.PayCode);
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, sessionId);
        jsonObject.put("itemId", charge_select.ItemId);
        jsonObject.put("payCode", payWay.PayCode);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiSeller.requestOrder()),
                new WWXCallBack("ChargeSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (payWayCodeStr.equals(Consts.GHTNET_PAY)) {
                            PublicWay.startWebViewActivityForResult(
                                    RenewServiceActivity.this, "网关支付",
                                    data.getString("Data"),
                                    WebViewActivity.Pay, WebViewActivity.FROMRENEW,
                                    WebViewActivity.REQUEST_CODE);
                        }else   if (payWayCodeStr.equals(Consts.FuYou)) {
                            PublicWay.startWebViewActivityForResult(
                                    RenewServiceActivity.this, getString(R.string.fuyou_pay),
                                    data.getString("Data"),
                                    WebViewActivity.Pay,
                                    WebViewActivity.FROMREG,
                                    WebViewActivity.REQUEST_CODE);
                        } else if (payWayCodeStr.equals(Consts.WX_PAY)) {
                            if (wxapi.isWXAppInstalled()) {
                                String string = data.getString("Data");
                                WXPay wxPay = JSONObject.parseObject(string, WXPay.class);
                                PayReq req = new PayReq();
                                req.appId = wxPay.appid;
                                req.partnerId = wxPay.partnerid;
                                req.prepayId = wxPay.prepayid;
                                req.packageValue = "Sign=WXPay";
                                req.nonceStr = wxPay.noncestr;
                                req.timeStamp = wxPay.timestamp;
                                req.sign = wxPay.sign;
                                wxapi.sendReq(req);
                            } else {
                                WWToast.showShort(R.string.please_download_wx_api);
                            }
                        } else if (payWayCodeStr.equals(Consts.ALI_PAY)) {
                            PayUtils.pay(RenewServiceActivity.this, data.getString("Data"), new PayResultLisentner() {
                                @Override
                                public void PayResult(String payResult, int payType) {
                                    // TODO Auto-generated method stub
                                    AlipayPayResult payResult2 = new AlipayPayResult(
                                            payResult);
                                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                                    String resultStatus = payResult2.getResultStatus();
                                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                    if (TextUtils.equals(resultStatus, "9000")) {
                                        // 支付成功
                                        showPaySuccessTips();
                                    } else {
                                        // 判断resultStatus 为非“9000”则代表可能支付失败
                                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                        if (TextUtils.equals(resultStatus, "8000")) {
                                            WWToast.showShort(R.string.pay_result_is_confirming);
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
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        //网关支付完成
        if (arg1 == RESULT_OK) {
            if (arg0 == WebViewActivity.REQUEST_CODE) {
                showPaySuccessTips();
            }
        }
    }

    private void showPaySuccessTips() {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialog(
                        RenewServiceActivity.this,
                        R.string.renew_success_tips);
        commonDialog.getButtonRight(R.string.ok);
        commonDialog.getButtonRight().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                commonDialog.dismiss();
            }
        });
        commonDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mode == LOGIN_FORCE_RENEW) {
                    WWApplication.getInstance().setSessionId(sessionId);
                    setResult(RESULT_OK);
                    startActivity(new Intent(RenewServiceActivity.this, MainActivity.class));
                }
                finish();
            }
        });
        commonDialog.getButtonLeft().setVisibility(View.GONE);
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
    }
}
