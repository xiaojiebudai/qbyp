package cn.net.chenbao.qbyp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiPay;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AccountInfo;
import cn.net.chenbao.qbyp.bean.PayWay;
import cn.net.chenbao.qbyp.bean.WXPay;
import cn.net.chenbao.qbyp.distribution.activity.DistributionCertSuccessActivity;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.utils.AlipayPayResult;
import cn.net.chenbao.qbyp.utils.Constants;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PayUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.wxapi.WXPayEntryActivity;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 支付订单
 *
 * @author licheng
 */
public class PayOrdersActivity extends FatherActivity implements
        OnClickListener {
    private int model;
    private int busType;
    /**
     * 本地生活
     */
    public final static int LOCAL = 0;
    /**
     * 自营
     */
    public final static int SELF = 1;
    /**
     * 充值,暂不维护
     */
    public final static int RECHARGE = 2;
    /**
     * 分销
     */
    public final static int DISTRIBUTION = 3;

    private DistributionPublicAccount distributionPublicAccount;
    private final List<View> mList = new ArrayList<View>();
    public static final int REQUEST_CODE = 10086;
    public static final int REQUEST_CODE_SELF = 10087;
    public static final int REQUEST_CODE_LOCAL = 10088;
    public static final int REQUEST_CODE_FENXIAO = 10089;
    public static final int REQUEST_CODE_OFFLINE = 10090;
    public static final int REQUEST_CODE_OFFLINE_SELF = 10091;
    private double money;
    private TextView mPrice;
    private long orderId;
    private long[] orderIds;
    private IWXAPI wxapi;
    private List<PayWay> list = new ArrayList<PayWay>();
    private LinearLayout llPayContainer;
    private final HashMap<String, TextView> map = new HashMap<String, TextView>();
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try{
                    llPayContainer.removeAllViews();
                    for (int i = 0; i < list.size(); i++) {
                        final PayWay payWay = list.get(i);
                        final View view = View.inflate(PayOrdersActivity.this,
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
//                                    mList.get(j).setVisibility(View.VISIBLE);
                                        mList.get(j).setVisibility(View.GONE);
                                    } else {
                                        mList.get(j).setVisibility(View.GONE);
                                    }
                                }
                                gotoPay();
                            }
                        });

                        ImageView payIco = (ImageView) view
                                .findViewById(R.id.iv_pay_pic);
                        TextView payname = (TextView) view
                                .findViewById(R.id.tv_payname);
                        TextView yu_e = (TextView) view
                                .findViewById(R.id.tv_xiaofei_yue);// 余额
                        try{
                            ImageUtils.setCommonImage(PayOrdersActivity.this,
                                    payWay.PayIco, payIco);
                        }catch (Exception e){

                        }

                        payname.setText(payWay.PayName);
                        if (payWay.PayCode.equals(Consts.BALAN_PAY)
                                || payWay.PayCode.equals(Consts.INTER_PAY)) {
                            yu_e.setVisibility(View.VISIBLE);
                            map.put(payWay.PayCode, yu_e);
                        }
                        mList.add(select);
                        llPayContainer.addView(view);
                        if (i == 0) {
                            list.get(0).isSelect = true;
//                        mList.get(0).setVisibility(View.VISIBLE);
                            mList.get(0).setVisibility(View.GONE);
                        }
                    }
                    if (model == LOCAL) {
                        getAccountInfo();
                    }
                }catch (Exception e){

                }
            }
        }

    };
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    switch (model) {
                        case SELF:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.SUCCESS, orderIds[0], money + "", REQUEST_CODE, SELF);
                            break;
                        case LOCAL:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, LOCAL);
                            break;
                        case RECHARGE:
                            PublicWay.stratRechargeResultActivity(PayOrdersActivity.this,
                                    RechargeResultActivity.SUCCESS, REQUEST_CODE);
                            break;
                        case DISTRIBUTION:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, DISTRIBUTION);
                            break;
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消支付
                    WWToast.showShort(R.string.pay_fail);
                    switch (model) {
                        case SELF:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, SELF);
                            break;
                        case LOCAL:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, LOCAL);
                            break;
                        case RECHARGE:
                            PublicWay.stratRechargeResultActivity(PayOrdersActivity.this,
                                    RechargeResultActivity.FAILE, REQUEST_CODE);
                            break;
                        case DISTRIBUTION:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, DISTRIBUTION);
                            break;
                    }
                    break;
                case BaseResp.ErrCode.ERR_COMM:// 错误
                    WWToast.showShort(R.string.pay_fail);
                    switch (model) {
                        case SELF:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, SELF);
                            break;
                        case LOCAL:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, LOCAL);
                            break;
                        case RECHARGE:
                            PublicWay.stratRechargeResultActivity(PayOrdersActivity.this,
                                    RechargeResultActivity.FAILE, REQUEST_CODE);
                            break;
                        case DISTRIBUTION:
                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                    PayResultActivity.FAILE, -1, null, REQUEST_CODE, DISTRIBUTION);
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_orders;
    }


    @Override
    protected void initValues() {
        initTitle();
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        model = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        money = getIntent().getDoubleExtra("money", -1);
        if (model == SELF) {
            orderIds = getIntent().getLongArrayExtra("orderId");
        } else {
            orderId = getIntent().getLongExtra("orderId", -1);
        }

        wxapi = WXAPIFactory.createWXAPI(PayOrdersActivity.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);

        //服务商升级不在这里
        if (model == LOCAL) {
            busType = Consts.ORDER_PAY_WAY;
        } else if (model == SELF) {
            busType = Consts.SELF_ORDER_PAY_WAY;
        } else if (model == RECHARGE) {
            busType = Consts.RECHARGE_PAY_WAY;
        } else if (model == DISTRIBUTION) {
            busType = Consts.FENXIAO_PAY_WAY;
        }


    }

    private void initTitle() {
        initDefautHead(R.string.pay_order, false);
        View left = findViewById(R.id.rl_head_left);
        if (left != null) {
            left.findViewById(R.id.tv_head_left).setBackgroundResource(
                    R.drawable.arrow_back);
            left.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (model != RECHARGE) {
                        dialogTips();
                    } else
                        finish();
                }
            });
        }
    }

    private void dialogTips() {
        final CommonDialog commonDialog = DialogUtils.getCommonDialog(PayOrdersActivity.this, R.string.pay_order_tips);
        commonDialog.setTitleText(R.string.leave_pay);
        commonDialog.getContentView().setGravity(Gravity.LEFT);
        commonDialog.getButtonLeft(R.string.cancel);
        commonDialog.getButtonRight(R.string.ok).setTextColor(getResources().getColor(R.color.yellow_ww));
        commonDialog.setLeftButtonOnClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });
        commonDialog.setRightButtonCilck(new OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
                finish();
            }
        });
        commonDialog.show();
    }

    /**
     * 支付列表改为动态生产
     */
    private void getPayList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        if (busType != 0) {
            params.addBodyParameter("busType", busType + "");
        }
        llPayContainer = (LinearLayout) findViewById(R.id.ll_payway_container);
        x.http().get(params, new WWXCallBack("PaymentValid") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray("Data");
                List<PayWay> listPay = JSON.parseArray(array.toJSONString(), PayWay.class);
                list.clear();
                for (int i = 0; i < listPay.size(); i++) {
                    if (model == DISTRIBUTION &&
                            listPay.get(i).PayCode.equals(Consts.FxAcc) &&
                            (distributionPublicAccount != null && distributionPublicAccount.Account < money)) {
                        continue;//不添加
                    }
                    list.add(listPay.get(i));
                }
                Message msg = mHandler.obtainMessage();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void initView() {
        mPrice = (TextView) findViewById(R.id.tv_price);
//        findViewById(R.id.tv_go_pay).setOnClickListener(this);
        mPrice.setText(WWViewUtil.numberFormatPrice(money));
    }

    @Override
    protected void doOperate() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        dialogTips();
    }

    /**
     * 获取账户信息
     */
    private void getAccountInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getAccountInfo());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("AccountGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                AccountInfo account = JSON.parseObject(data.getString("Data"),
                        AccountInfo.class);
                map.get(Consts.BALAN_PAY).setText(
                        getString(R.string.yue_with_colon) + WWViewUtil.numberFormatPrice(account.Balance));
                map.get(Consts.INTER_PAY).setText(
                        getString(R.string.yue_with_colon) + "¥"
                                + account.InternalBalance);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    private void gotoPay() {
        boolean isSelect = false;
        PayWay payWay = null;
        for (int i = 0; i < list.size(); i++) {
            PayWay payWay2 = list.get(i);
            if (payWay2.isSelect) {
                payWay = payWay2;
                isSelect = true;
            }
        }
        if (!isSelect) {
            WWToast.showShort(R.string.choice_pay_way);
            return;
        }
        final String payWayCodeStr = payWay.PayCode;
        ZLog.showPost(payWay.PayName + "--" + payWay.PayCode);
        if (payWay.PayCode.equals(Consts.INTER_PAY)
                || payWay.PayCode.equals(Consts.BALAN_PAY) || payWay.PayCode.equals(Consts.FxAcc)) {// 如果是内部支付
            if (havePayPsw) {
                PublicWay.startVerifyPasswordActivity(this,
                        VerifyPasswordActivity.MODE_ORDER_PAY, money + "", 0,
                        payWay.PayCode, orderId, getRequestCode());
            } else {
                showSetpswDialog();
            }

            return;
        }
        if (Consts.Offline.equals(payWay.PayCode)) {
            if(model == SELF){
                PublicWay.startDistributionOfflinePayActivity(PayOrdersActivity.this,model, orderIds,REQUEST_CODE_OFFLINE_SELF);
            }else{
                PublicWay.startDistributionOfflinePayActivity(PayOrdersActivity.this, orderId,REQUEST_CODE_OFFLINE);
            }

            return;
        }

        final String payCode = payWay.PayCode;
        showWaitDialog();
        //区分支付了
        if (model == SELF) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                    .getInstance().getSessionId());
            jsonObject.put("payCode", payCode);
            jsonObject.put("orderIds", orderIds);
            x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiPay.OrdersPay()), new WWXCallBack("OrdersPay") {

                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    if (payWayCodeStr.equals(Consts.GHTNET_PAY)) {
                        PublicWay.startWebViewActivityForResult(
                                PayOrdersActivity.this, getString(R.string.net_pay),
                                data.getString("Data"), WebViewActivity.Pay, REQUEST_CODE_SELF);

                    } else if(payWayCodeStr.equals(Consts.FuYou)){
                        PublicWay.startWebViewActivityForResult(
                                PayOrdersActivity.this,getString(R.string.fuyou_pay),
                                data.getString("Data"), WebViewActivity.Pay, REQUEST_CODE_SELF);
                    }


                    else if (payWayCodeStr.equals(Consts.WX_PAY)) {
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
                            WWToast.showShort(R.string.please_download_wx_app);
                        }
                    } else if (payWayCodeStr.equals(Consts.ALI_PAY)) {
                        PayUtils.pay(PayOrdersActivity.this,
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
                                            PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                    PayResultActivity.SUCCESS, orderIds[0], money + "", REQUEST_CODE, SELF);
                                        } else {
                                            // 判断resultStatus 为非“9000”则代表可能支付失败
                                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                            if (TextUtils.equals(resultStatus, "8000")) {
                                                WWToast.showShort(R.string.pay_result_is_confirming);
                                                finish();
                                            } else {
                                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                                WWToast.showShort(R.string.pay_fail);
                                                PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                        PayResultActivity.FAILE, -1, null, REQUEST_CODE, SELF);
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
        } else {
            RequestParams params = new RequestParams(ApiPay.OrderPayGet());
            params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                    .getInstance().getSessionId());
            params.addBodyParameter("orderId", orderId + "");
            params.addBodyParameter("payCode", payCode);
            x.http().get(params, new WWXCallBack("OrderPayGet") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {

                    if (payWayCodeStr.equals(Consts.GHTNET_PAY)) {
                        PublicWay.startWebViewActivityForResult(
                                PayOrdersActivity.this, "网关支付",
                                data.getString("Data"), WebViewActivity.Pay, getRequestCode());
                    }else if(payWayCodeStr.equals(Consts.FuYou)){
                        PublicWay.startWebViewActivityForResult(
                                PayOrdersActivity.this,getString(R.string.fuyou_pay),
                                data.getString("Data"), WebViewActivity.Pay, getRequestCode());
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
                        PayUtils.pay(PayOrdersActivity.this,
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
                                            if (model == LOCAL) {
                                                PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                        PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, LOCAL);
                                            } else if (model == DISTRIBUTION) {
                                                //分销支付
                                                PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                        PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, DISTRIBUTION);
                                            } else {
                                                PublicWay.stratRechargeResultActivity(PayOrdersActivity.this, RechargeResultActivity.SUCCESS, REQUEST_CODE);
                                            }

                                        } else {
                                            // 判断resultStatus 为非“9000”则代表可能支付失败
                                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                            if (TextUtils.equals(resultStatus, "8000")) {
                                                WWToast.showShort(R.string.pay_result_is_confirming);
                                                finish();
                                            } else {
                                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                                WWToast.showShort(R.string.pay_fail);
                                                if (model == LOCAL) {
                                                    PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                            PayResultActivity.FAILE, -1, null, REQUEST_CODE, LOCAL);
                                                } else if (model == DISTRIBUTION) {
                                                    //分销支付
                                                    PublicWay.stratPayResultActivity(PayOrdersActivity.this,
                                                            PayResultActivity.FAILE, -1, null, REQUEST_CODE, DISTRIBUTION);
                                                } else {

                                                    PublicWay.stratRechargeResultActivity(PayOrdersActivity.this, RechargeResultActivity.FAILE, REQUEST_CODE);


                                                }

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


    }

    private int getRequestCode() {

        int requestCode = REQUEST_CODE;
        switch (model) {
            case LOCAL:
                requestCode = REQUEST_CODE_LOCAL;
                break;
            case SELF:
                requestCode = REQUEST_CODE_LOCAL;
                break;
            case RECHARGE:
                requestCode = REQUEST_CODE;
                break;
            case DISTRIBUTION:
                requestCode = REQUEST_CODE_FENXIAO;
                break;
        }
        return requestCode;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == REQUEST_CODE) {
            if (arg1 == RESULT_OK) {
//				getAccountInfo();
                setResult(RESULT_OK);
                finish();
            }
        } else if (arg0 == REQUEST_CODE_SELF) {
            //自营支付结果
            if (arg1 == RESULT_OK) {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.SUCCESS, orderIds[0], money + "", REQUEST_CODE, model);
            } else {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.FAILE, -1, null, REQUEST_CODE, model);
            }
        } else if (arg0 == REQUEST_CODE_LOCAL) {
            //本地支付结果
            if (arg1 == RESULT_OK) {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, model);
            } else {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.FAILE, -1, null, REQUEST_CODE, model);
            }
        } else if (arg0 == REQUEST_CODE_FENXIAO) {
            //分销支付结果
            if (arg1 == RESULT_OK) {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.SUCCESS, orderId, money + "", REQUEST_CODE, model);
            } else {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.FAILE, -1, null, REQUEST_CODE, model);
            }
        } else if (arg0 == REQUEST_CODE_OFFLINE) {
            //线下支付结果
            if (arg1 == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
                startActivity(new Intent(PayOrdersActivity.this, DistributionCertSuccessActivity.class));

            } else {

            }
        }else if(arg0==REQUEST_CODE_OFFLINE_SELF){
            //自营支付结果
            if (arg1 == RESULT_OK) {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.SUCCESS, orderIds[0], money + "", REQUEST_CODE, model);
            } else {
                PublicWay.stratPayResultActivity(this,
                        PayResultActivity.FAILE, -1, null, REQUEST_CODE, model);
            }
        }
    }

    @Override
    protected void onResume() {
        if (model == DISTRIBUTION) {
            getMyAccount();
        } else {
            getPayList();
        }

        getHavaPayPsw();
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_go_pay:
                gotoPay();
                break;

            default:
                break;
        }
    }

    private boolean havePayPsw;

    public void getHavaPayPsw() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("HavePaypsd") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                havePayPsw = data.getBooleanValue("Data");
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void showSetpswDialog() {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialogTwiceConfirm(this,
                        getString(R.string.unset_psw_do_it), true);
        commonDialog.setRightButtonCilck(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PublicWay.startVerifyPhoneNumberActivity(
                        PayOrdersActivity.this, VerifyPhoneNumberActivity.PAY,
                        SharedPreferenceUtils.getInstance().getPhoneNum(), 999);
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    /**
     * 分销货款余额
     */
    private void getMyAccount() {
        x.http().get(ParamsUtils.getSessionParams(ApiDistribution.MyAccount()), new WWXCallBack("MyAccount") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                distributionPublicAccount = JSONObject.parseObject(data.getString("Data"), DistributionPublicAccount.class);
                getPayList();
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                getPayList();
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }
}
