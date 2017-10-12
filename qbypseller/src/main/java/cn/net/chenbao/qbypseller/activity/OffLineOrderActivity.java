package cn.net.chenbao.qbypseller.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiOfflineTrade;
import cn.net.chenbao.qbypseller.api.ApiPay;
import cn.net.chenbao.qbypseller.api.ApiUser;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.bean.JiFen;
import cn.net.chenbao.qbypseller.bean.PayWay;
import cn.net.chenbao.qbypseller.bean.WXPay;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.dialog.PayWayDialog;
import cn.net.chenbao.qbypseller.utils.AlipayPayResult;
import cn.net.chenbao.qbypseller.utils.Constants;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PayUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.wxapi.WXPayEntryActivity;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/13.
 * 线下订单
 */

public class OffLineOrderActivity extends FatherActivity implements PermissionUtil.PermissionCallbacks {
    private static final int REQUEST_GOODS_CODE = 888;
    private static final int REQUEST_JIFEN_PAY_CODE = 999;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.ll_select_bank)
    LinearLayout llSelectBank;
    @BindView(R.id.et_num)
    TextView etNum;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.tv_zengsongjinge)
    TextView tvZengsongjinge;
    @BindView(R.id.rb_0)
    RadioButton rb0;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rg_jifen)
    RadioGroup rgJifen;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tv_rangli)
    TextView tvRangli;
    @BindView(R.id.tv_save)
    TextView tvSave;
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 1;
    @BindView(R.id.tv_good)
    TextView tvGood;
    private double bili = 0.1;//0.99  现在改为固定0.1,接口需要参数
    private DecimalFormat df, df1;
    private IWXAPI wxapi;
    private ArrayList<Goods> goods = new ArrayList<Goods>();
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        getHavaPayPsw();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_offlineorder;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int errCode = arg1.getIntExtra(Consts.KEY_DATA, 0);
            switch (errCode) {
                case BaseResp.ErrCode.ERR_OK:// 成功
                    startActivity(new Intent(OffLineOrderActivity.this, PayFeeSuccessActivity.class));
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
        initDefautHead(R.string.offlinepay, true);
        df = new DecimalFormat("######0.00");
        df1 = new DecimalFormat("######0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        df1.setRoundingMode(RoundingMode.HALF_UP);
        IntentFilter intent = new IntentFilter(WXPayEntryActivity.WXPAYRESULT);
        registerReceiver(receiver, intent);
        wxapi = WXAPIFactory.createWXAPI(OffLineOrderActivity.this,
                Constants.WX_APP_ID);
        wxapi.registerApp(Constants.WX_APP_ID);
    }

    @Override
    protected void initView() {
        rgJifen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                        bili = 0.20;
                        setJifenAndRangli(totalPrice, bili);
                        break;
                    case R.id.rb_1:
                        bili = 0.48;
                        setJifenAndRangli(totalPrice, bili);
                        break;
                    case R.id.rb_2:
                        bili = 0.99;
                        setJifenAndRangli(totalPrice, bili);
                        break;
                }
            }
        });
//        etNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!TextUtils.isEmpty(s)) {
//                    try {
//                        num = Double.valueOf(s + "");
//                        setJifenAndRangli(num, bili);
//                    } catch (Exception e) {
//                        num = 0;
//                    }
//
//                }
//            }
//        });
    }

    /**
     * 设置积分和让利
     *
     * @param num
     * @param bili
     */
    private void setJifenAndRangli(double num, double bili) {
//        if (bili == 0.2) {
//        tvZengsongjinge.setText(df.format((num * bili)) + "");
//            tvRangli.setText(df1.format((num * 0.05)) + "");
//        } else if (bili == 0.48) {
//        tvZengsongjinge.setText(df.format((num * bili)) + "");
//            tvRangli.setText(df1.format((num * 0.10)) + "");
//        } else {
//        tvZengsongjinge.setText(df.format((num * bili)) + "");
//            tvRangli.setText(df1.format((num * 0.20)) + "");
//        }
        //totalMoney * 0.1=让利金额 ,  让利金额 * 1.5 =(可用积分 和 现金积分),这里显示两种积分总和
        tvZengsongjinge.setText(df.format((num * 0.1 * bili *1)) + "");
        tvRangli.setText(df1.format((num * 0.1)) + "");
    }

    @Override
    protected void doOperate() {

    }

    final public static int REQUEST_CODE_ASK_CAMERA = 123;

    @OnClick({R.id.iv_qrcode, R.id.tv_save, R.id.ll_choice_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_qrcode:
                //判断权限
                if (PermissionUtil.hasPermissions(this, new String[]{Manifest.permission.CAMERA})) {
                    PublicWay.startCaptureActivityForResult(this, REQUEST_CODE_SCAN);
                } else {
                    PermissionUtil.requestPermissions(this, REQUEST_CODE_ASK_CAMERA, new String[]{Manifest.permission.CAMERA});
                }

                break;
            case R.id.tv_save:
                commitInfo();
                break;
            case R.id.ll_choice_goods:
                PublicWay.startSelectGoodsActivity(this, JSONObject.toJSONString(goods), REQUEST_GOODS_CODE);
                break;
        }
    }

    private void commitInfo() {
        String userMobile = etName.getText().toString().trim();
        if (TextUtils.isEmpty(userMobile)) {
            WWToast.showShort(R.string.please_input_vip_account);
            return;
        }
        ZLog.showPost(JSONObject.toJSONString(goods));
        if (goods == null && goods.size() > 0) {
            WWToast.showShort(R.string.please_select_goods);
            return;
        }


        if (totalPrice == 0) {
            WWToast.showShort(R.string.pay_money_than_0);
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < goods.size(); i++) {
            jsonArray.add(goods.get(i).toJson());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goods", jsonArray);
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderAmt", Double.valueOf(tvRangli.getText().toString().trim()));
        jsonObject.put("userMobile", userMobile);
        jsonObject.put("integralRate", bili);
        ZLog.showPost(jsonObject.toJSONString());
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiOfflineTrade.OfflineOrderSubmit()),
                new WWXCallBack("OfflineOrderSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        final JiFen orderDetail = JSON.parseObject(data.getString("Data"),
                                JiFen.class);
                        PayWayDialog payWayDialog = new PayWayDialog(OffLineOrderActivity.this, R.style.DialogStyle,
                                orderDetail.SellerAccount.Consume >= orderDetail.SellerRlAmt,
                                orderDetail.SellerAccount.Balance >= orderDetail.SellerRlAmt,
                                new PayWayDialog.OnSelectOKLisentner() {
                                    @Override
                                    public void selectPayWay(PayWay payWay) {
                                        gotoPay(payWay, orderDetail);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCAN:
                    if (data != null) {
                        String code = data.getStringExtra(Consts.KEY_DATA).trim();
                        if (code.contains(Api.getShareUrl())) {
                            etName.setText(code.replace(Api.getShareUrl(), ""));
                        } else {
                            WWToast.showShort(R.string.erwei_info_error);
                        }

                    }
                    break;
                case WebViewActivity.REQUEST_CODE:
                    startActivity(new Intent(OffLineOrderActivity.this, PayFeeSuccessActivity.class));
                    finish();
                    break;
                case REQUEST_JIFEN_PAY_CODE:
                    startActivity(new Intent(OffLineOrderActivity.this, PayFeeSuccessActivity.class));
                    finish();
                    break;
                case REQUEST_GOODS_CODE:
                    goods = (ArrayList<Goods>) JSON.parseArray(data.getStringExtra(Consts.KEY_DATA),
                            Goods.class);
                    int quantity = 0;
                    totalPrice = 0;
                    if (goods.size() > 0) {
                        for (int i = 0; i < goods.size(); i++) {
                            quantity += goods.get(i).Quantity;
                            totalPrice += goods.get(i).Price * goods.get(i).Quantity;
                        }
                        setJifenAndRangli(totalPrice, 1.5);//TODO bili 让利支付金额的1.5倍赠送(可用积分,现金积分)积分
                        tvGood.setText(String.format(getString(R.string.choice_num), quantity));
                        etNum.setText(WWViewUtil.numberFormatWithTwo(totalPrice));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void gotoPay(PayWay payWay, final JiFen orderDetail) {

        final String payWayCodeStr = payWay.PayCode;
        if (payWay.PayCode.equals(Consts.INTEGRAL_PAY) || payWay.PayCode.equals(Consts.SEL_ACC)) {// 如果是内部支付
            if (havePayPsw) {
                Intent i = new Intent(this, VerifyPasswordActivity.class);
                i.putExtra(Consts.KEY_DATA, JSONObject.toJSONString(orderDetail));
                i.putExtra(Consts.KEY_MODE, payWay.PayCode.equals(Consts.INTEGRAL_PAY) ? VerifyPasswordActivity.JIFENZHIFU : VerifyPasswordActivity.YUEZHIFU);
                startActivityForResult(i, REQUEST_JIFEN_PAY_CODE);
            } else {
                showSetpswDialog();
            }

            return;
        }
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.OrderPayGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderDetail.OrderId + "");
        params.addBodyParameter("payCode", payWay.PayCode);
        x.http().get(params, new WWXCallBack("OrderPayGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (payWayCodeStr.equals(Consts.GHTNET_PAY)) {
                    PublicWay.startWebViewActivityForResult(
                            OffLineOrderActivity.this, "网关支付",
                            data.getString("Data"),
                            WebViewActivity.Pay, WebViewActivity.FROMOFFLINE,
                            WebViewActivity.REQUEST_CODE);
                } else if (payWayCodeStr.equals(Consts.FuYou)) {
                    PublicWay.startWebViewActivityForResult(
                            OffLineOrderActivity.this, getString(R.string.fuyou_pay),
                            data.getString("Data"),
                            WebViewActivity.Pay, WebViewActivity.FROMOFFLINE,
                            WebViewActivity.REQUEST_CODE);
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
                        WWToast.showShort(R.string.please_download_wx_api);
                    }
                } else if (payWayCodeStr.equals(Consts.ALI_PAY)) {
                    PayUtils.pay(OffLineOrderActivity.this,
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
                                        startActivity(new Intent(OffLineOrderActivity.this, PayFeeSuccessActivity.class));
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
        commonDialog.setRightButtonCilck(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PublicWay.startfindLoginPasswordAcitivity(OffLineOrderActivity.this,
                        FindlLoginPasswordActivity.MODE_PAY_PSW, SharedPreferenceUtils.getInstance().getPhoneNum());
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        PublicWay.startCaptureActivityForResult(this, REQUEST_CODE_SCAN);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        for (int i = 0; i < perms.size(); i++) {
            if (!PermissionUtil.hasPermissions(this, perms.get(i))) {
                WWToast.showShort(perms.get(i) + "被拒绝");
            }
        }
    }

}
