package cn.net.chenbao.qbypseller.activity;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiPay;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Charge;
import cn.net.chenbao.qbypseller.bean.PayWay;
import cn.net.chenbao.qbypseller.bean.WXPay;
import cn.net.chenbao.qbypseller.utils.AlipayPayResult;
import cn.net.chenbao.qbypseller.utils.Constants;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PayUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.view.RegisterTimeLine;
import cn.net.chenbao.qbypseller.wxapi.WXPayEntryActivity;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 开通服务
 *
 * @author licheng
 */
public class OpenServerActivity extends FatherActivity implements
        OnClickListener {
    private final List<View> mList = new ArrayList<View>();
    private String sessionId;
    private int itemId;
    private List<PayWay> list;
    private LinearLayout llPayContainer;
    private IWXAPI wxapi;
    /**
     * 选中的支付项
     */
    private Charge charge_select;
    private int currentCharge = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.act_open_server;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    WWToast.showShort(R.string.pay_success);
                    PublicWay.startCheckActivity(OpenServerActivity.this,
                            CheckActivity.MODE_CHECK, sessionId);
                    finish();
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
        initDefautHead(R.string.business_register, true);
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        sessionId = getIntent().getStringExtra(Consts.KEY_DATA);
        wxapi = WXAPIFactory.createWXAPI(OpenServerActivity.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);
        WWApplication.getInstance().setisHome(false);
    }

    @Override
    protected void initView() {
        RegisterTimeLine rt = (RegisterTimeLine) findViewById(R.id.rt);
        rt.setStep(3);
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        tv1.setTextColor(getResources().getColor(R.color.yellow_ww));
        tv2.setTextColor(getResources().getColor(R.color.yellow_ww));
        tv3.setTextColor(getResources().getColor(R.color.yellow_ww));

        getPayList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 支付列表改为动态生产
     */
    private void getPayList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Consts.KEY_SESSIONID, sessionId);
        params.addBodyParameter("busType", Consts.SELLER_RENEW_PAY_WAY + "");
        llPayContainer = (LinearLayout) findViewById(R.id.ll_payway_container);
        x.http().get(params, new WWXCallBack("PaymentValid") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray("Data");
                list = JSON.parseArray(array.toJSONString(), PayWay.class);
                for (int i = 0; i < list.size(); i++) {
                    final PayWay payWay = list.get(i);
                    final View view = View.inflate(OpenServerActivity.this,
                            R.layout.item_payway, null);
                    final ImageView select = (ImageView) view
                            .findViewById(R.id.iv_selected);
                    view.setTag(i);
                    view.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < list.size(); j++) {
                                list.get(j).isSelect = false;
                            }
                            list.get((Integer) view.getTag()).isSelect = true;
                            for (int j = 0; j < mList.size(); j++) {
                                if (j == (Integer) view.getTag()) {
                                    mList.get(j).setVisibility(View.VISIBLE);
                                } else {
                                    mList.get(j).setVisibility(View.GONE);
                                }
                            }
                            gotoPay(list.get((Integer) view.getTag()));
                        }
                    });

                    ImageView payIco = (ImageView) view
                            .findViewById(R.id.iv_pay_pic);
                    TextView payname = (TextView) view
                            .findViewById(R.id.tv_payname);
                    ImageUtils.setCommonImage(OpenServerActivity.this,
                            payWay.PayIco, payIco);
                    payname.setText(payWay.PayName);
                    mList.add(select);
                    llPayContainer.addView(view);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void gotoPay(PayWay payWay) {
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
                                    OpenServerActivity.this, "网关支付",
                                    data.getString("Data"),
                                    WebViewActivity.Pay,
                                    WebViewActivity.FROMREG,
                                    WebViewActivity.REQUEST_CODE);
                        }else   if (payWayCodeStr.equals(Consts.FuYou)) {
                            PublicWay.startWebViewActivityForResult(
                                    OpenServerActivity.this, getString(R.string.fuyou_pay),
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
                            PayUtils.pay(OpenServerActivity.this, data.getString("Data"), new PayUtils.PayResultLisentner() {
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
                                        PublicWay.startCheckActivity(OpenServerActivity.this,
                                                CheckActivity.MODE_CHECK, sessionId);
                                        finish();
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
    protected void doOperate() {
        RequestParams params = new RequestParams(ApiSeller.chargeItemGet());
        params.addBodyParameter("sessionId", sessionId);
        showWaitDialog();
        x.http().get(params, new WWXCallBack("ChargeItemGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray("Data");
                final List<Charge> charges = JSON.parseArray(array.toJSONString(),
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        // 支付完成
        if (arg1 == RESULT_OK) {
            if (arg0 == WebViewActivity.REQUEST_CODE) {
                PublicWay.startCheckActivity(OpenServerActivity.this,
                        CheckActivity.MODE_CHECK, sessionId);
                finish();
            }
        }
    }

}
