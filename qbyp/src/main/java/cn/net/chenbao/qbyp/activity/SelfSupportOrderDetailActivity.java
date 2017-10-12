package cn.net.chenbao.qbyp.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.InternalAmtPayItem;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.bean.ShopOrderGoods;
import cn.net.chenbao.qbyp.bean.ShopOrderOutline;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.bean.ShopOrderInfo;

import java.util.List;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城订单详情
 */

public class SelfSupportOrderDetailActivity extends FatherActivity implements View.OnClickListener, PermissionUtil.PermissionCallbacks {
    @BindView(R.id.tv_head_center)
    TextView tvHeadCenter;
    @BindView(R.id.tv_head_right)
    TextView tvHeadRight;
    @BindView(R.id.tv_order_serialnum)
    TextView tvOrderSerialnum;
    @BindView(R.id.tv_order_state)
    TextView tvOrderState;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_revceiver_phone)
    TextView tvRevceiverPhone;
    @BindView(R.id.tv_reveiver_address)
    TextView tvReveiverAddress;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.ll_jifen_container)
    LinearLayout ll_jifen_container;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_goods_total_money)
    TextView tvGoodsTotalMoney;
    @BindView(R.id.tv_freight)
    TextView tvFreight;

    @BindView(R.id.line_pay)
    TextView line_pay;
    @BindView(R.id.tv_real_pay)
    TextView tvRealPay;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.ll_contact_seller)
    LinearLayout llContactSeller;
    @BindView(R.id.tv_place_an_order_time)
    TextView tvPlaceAnOrderTime;
    @BindView(R.id.tv_good_pay_time)
    TextView tvGoodPayTime;
    @BindView(R.id.tv_send_good_time)
    TextView tvSendGoodTime;
    @BindView(R.id.tv_look_good_position)
    TextView tvLookGoodPosition;
    @BindView(R.id.tv_makesure_receive)
    TextView tvMakesureReceive;
    @BindView(R.id.tv_need_pay)
    TextView tvNeedPay;
    @BindView(R.id.rl_3)
    RelativeLayout rl_3;
    @BindView(R.id.tv_creditId)
    TextView tvCreditNo;
    @BindView(R.id.ll_id)
    LinearLayout llId;
    @BindView(R.id.tv_head_left)
    TextView tvHeadLeft;
    @BindView(R.id.rl_head_left)
    RelativeLayout rlHeadLeft;
    @BindView(R.id.rl_head_right)
    RelativeLayout rlHeadRight;
    @BindView(R.id.tv_receive_good_time)
    TextView tvReceiveGoodTime;
    @BindView(R.id.ll_receive_good_time)
    LinearLayout llReceiveGoodTime;

    private ShopOrderInfo shopOrderInfo;
    private long orderId;
    public static final int REQUEST_CODE = 10089;

    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_order_detail;
    }

    @Override
    protected void initValues() {
        orderId = getIntent().getLongExtra(Consts.KEY_DATA, -1);
    }

    @Override
    protected void initView() {
        initDefautHead(R.string.order_detail, true);
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestData();
    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.OrderInfo());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        x.http().get(params, new WWXCallBack("OrderInfo") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                shopOrderInfo = JSONObject.parseObject(
                        data.getString("Data"), ShopOrderInfo.class);

                //动态添加商品条目
                llContainer.removeAllViews();
                for (int i = 0; i < shopOrderInfo.Products.size(); i++) {
                    final View goodItem = View.inflate(SelfSupportOrderDetailActivity.this, R.layout.selfsupport_makesureorder_gooditem, null);
                    final ShopOrderGoods shopOrderGoods = shopOrderInfo.Products.get(i);
                    goodItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.stratSelfSupportGoodsDetailActivity(SelfSupportOrderDetailActivity.this, shopOrderGoods.ProductId);
                        }
                    });
                    String imageUrl = shopOrderGoods.ImageUrl;
                    ImageUtils.setCommonImage(SelfSupportOrderDetailActivity.this, ImageUtils.getRightImgScreen(imageUrl, DensityUtil.dip2px(SelfSupportOrderDetailActivity.this, 80), DensityUtil.dip2px(SelfSupportOrderDetailActivity.this, 80)), (ImageView) goodItem.findViewById(R.id.iv_good_img));
                    WWViewUtil.textInsertDrawable(SelfSupportOrderDetailActivity.this, ((TextView) goodItem.findViewById(R.id.tv_good_name)), shopOrderGoods.ProductName, false, shopOrderGoods.IsVipLevel);
                    ((TextView) goodItem.findViewById(R.id.tv_good_price)).setText(WWViewUtil.numberFormatPrice(shopOrderGoods.SalePrice));
                    ((TextView) goodItem.findViewById(R.id.tv_good_num)).setText("x" + shopOrderGoods.Quantity);
                    ((TextView) goodItem.findViewById(R.id.tv_0)).setText(shopOrderGoods.ProName);
                    llContainer.addView(goodItem);
                }
                setDataToUi();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void setDataToUi() {
        setOperateView();

        if (!TextUtils.isEmpty(shopOrderInfo.CreditNo)) {
            tvCreditNo.setText(shopOrderInfo.CreditNo);
            llId.setVisibility(View.VISIBLE);
        } else {
            llId.setVisibility(View.GONE);
        }
        tvOrderSerialnum.setText(shopOrderInfo.OrderId + "");
        tvReceiverName.setText(shopOrderInfo.Consignee);
        tvRevceiverPhone.setText(shopOrderInfo.ReceiverMobile);
        tvReveiverAddress.setText(shopOrderInfo.Address);
        tvShopName.setText(shopOrderInfo.VenderName);
        tvMessage.setText(shopOrderInfo.UserExplain);
        tvGoodsTotalMoney.setText(WWViewUtil.numberFormatPrice(shopOrderInfo.GoodsAmt));

        tvFreight.setText(WWViewUtil.numberFormatPrice(shopOrderInfo.PostFee));
        if (shopOrderInfo.InternalPayList != null && shopOrderInfo.InternalPayList.size() > 0) {
            ll_jifen_container.removeAllViews();
            for (int i = 0; i < shopOrderInfo.InternalPayList.size(); i++) {
                View jifenItem = View.inflate(SelfSupportOrderDetailActivity.this, R.layout.self_order_jifen_item, null);
                InternalAmtPayItem item = shopOrderInfo.InternalPayList.get(i);
                ((TextView) jifenItem.findViewById(R.id.tv_name)).setText(item.PayName);
                ((TextView) jifenItem.findViewById(R.id.tv_integral_num)).setText("-" + WWViewUtil.numberFormatPrice(item.UseAmt));
                ll_jifen_container.addView(jifenItem);
            }
        }


        tvRealPay.setText(WWViewUtil.numberFormatPrice(shopOrderInfo.PayAmt));
        if (!TextUtils.isEmpty(shopOrderInfo.PayCode)) {
            rl_3.setVisibility(View.VISIBLE);

            if (Consts.INTEG_PAY.equals(shopOrderInfo.PayCode)) {
                rl_3.setVisibility(View.GONE);
            } else {
                tvPayWay.setText(Order.getPayWayString(shopOrderInfo.PayCode));
            }

        } else {
            rl_3.setVisibility(View.GONE);

        }
        tvPlaceAnOrderTime.setText(TimeUtil.getTimeToS(shopOrderInfo.CreateTime * 1000));
        if (shopOrderInfo.PayTime > 0) {
            tvGoodPayTime.setText(TimeUtil.getTimeToS(shopOrderInfo.PayTime * 1000));
        } else {
            tvGoodPayTime.setText(R.string.order_pay_un_finish);
        }
        if (shopOrderInfo.SendTime > 0) {
            tvSendGoodTime.setText(TimeUtil.getTimeToS(shopOrderInfo.SendTime * 1000));
        } else {
            tvSendGoodTime.setText(R.string.order_pay_un_send);
        }
        if (shopOrderInfo.ConfirmTime > 0) {
            tvReceiveGoodTime.setText(TimeUtil.getTimeToS(shopOrderInfo.ConfirmTime * 1000));
        } else {
            if (shopOrderInfo.SendTime > 0) {
                tvReceiveGoodTime.setText(R.string.order_not_makesure_receive);
            } else
                tvReceiveGoodTime.setText(R.string.order_pay_un_send);
        }
    }

    private void setOperateView() {
        switch (shopOrderInfo.Status) {
            case ShopOrderOutline.STATE_WAIT_PAY:// 待付款
                tvLookGoodPosition.setVisibility(View.VISIBLE);
                tvMakesureReceive.setVisibility(View.VISIBLE);
                tvNeedPay.setText(getString(
                        R.string.need_pay));
                tvOrderState.setText(
                        R.string.wait_pay);
                tvMakesureReceive.setText(
                        R.string.pay_soon);
                tvLookGoodPosition.setText(
                        R.string.cancel_order);

                break;
            case ShopOrderOutline.STATE_WAIT_SELLER_CONFIRM:// 待发货
                tvLookGoodPosition.setVisibility(View.VISIBLE);
                tvMakesureReceive.setVisibility(View.GONE);
                tvOrderState.setText(
                        R.string.wait_send);
                tvLookGoodPosition.setText(
                        R.string.cancel_order);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                break;
            case ShopOrderOutline.STATE_WAIT_BUYER_CONFIRM:// 确认收货
                tvLookGoodPosition.setVisibility(View.VISIBLE);
                tvMakesureReceive.setVisibility(View.VISIBLE);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                tvOrderState.setText(
                        R.string.wait_goods);
                tvMakesureReceive.setText(
                        R.string.make_suer_receiver_goods);
                tvLookGoodPosition.setText(
                        R.string.look_post);
                break;

            case ShopOrderOutline.STATE_COMPLETE:// 已完成
                if (shopOrderInfo.UserJudge) {
                    tvMakesureReceive.setVisibility(View.GONE);
                } else {
                    tvMakesureReceive.setVisibility(View.VISIBLE);
                }
                tvLookGoodPosition.setVisibility(View.VISIBLE);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                tvOrderState.setText(
                        R.string.finished);
                tvMakesureReceive.setText(
                        R.string.go_evaluate);
                tvLookGoodPosition.setText(
                        R.string.look_post);
                break;

            case ShopOrderOutline.STATE_CANCEL:// 已取消
                tvLookGoodPosition.setVisibility(View.GONE);
                tvMakesureReceive.setVisibility(View.GONE);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                tvOrderState.setText(
                        R.string.canceled);
                findViewById(R.id.ll_operate).setVisibility(View.GONE);
                break;
            case ShopOrderOutline.STATE_REFUND:// 退款中
                tvLookGoodPosition.setVisibility(View.GONE);
                tvMakesureReceive.setVisibility(View.GONE);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                tvOrderState.setText(
                        R.string.back_money);
                findViewById(R.id.ll_operate).setVisibility(View.GONE);
                break;
            case ShopOrderOutline.STATE_CLOSE:// 已关闭
                tvLookGoodPosition.setVisibility(View.GONE);
                tvMakesureReceive.setVisibility(View.GONE);
                tvNeedPay.setText(getString(
                        R.string.real_pay_colon));
                tvOrderState.setText(
                        R.string.closed);
                findViewById(R.id.ll_operate).setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.ll_contact_seller, R.id.tv_look_good_position, R.id.tv_makesure_receive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_contact_seller:
                showDialogToCall();
                break;
            case R.id.tv_look_good_position:
                switch (shopOrderInfo.Status) {
                    case ShopOrderOutline.STATE_WAIT_PAY:// 待付款

                        showCancelDialog(shopOrderInfo.OrderId);

                        break;
                    case ShopOrderOutline.STATE_WAIT_SELLER_CONFIRM:// 待发货
                        showCancelDialog(shopOrderInfo.OrderId);
                        break;
                    case ShopOrderOutline.STATE_WAIT_BUYER_CONFIRM:// 确认收货
                        PublicWay.stratSelfSupportFollowDeliverActivity(SelfSupportOrderDetailActivity.this, shopOrderInfo.OrderId, SelfSupportFollowDeliverActivity.SELF, 0);
                        break;
                    case ShopOrderOutline.STATE_COMPLETE:// 已完成
                        PublicWay.stratSelfSupportFollowDeliverActivity(SelfSupportOrderDetailActivity.this, shopOrderInfo.OrderId, SelfSupportFollowDeliverActivity.SELF, 0);
                        break;
                }
                break;
            case R.id.tv_makesure_receive:
                switch (shopOrderInfo.Status) {
                    case ShopOrderOutline.STATE_WAIT_PAY:// 待付款
                        long[] orderIds = new long[1];
                        orderIds[0] = shopOrderInfo.OrderId;
                        PublicWay.startPayOrderActivity(SelfSupportOrderDetailActivity.this,
                                shopOrderInfo.PayAmt, orderIds,
                                REQUEST_CODE, PayOrdersActivity.SELF);
                        break;
                    case ShopOrderOutline.STATE_WAIT_SELLER_CONFIRM:// 待发货
                        break;
                    case ShopOrderOutline.STATE_WAIT_BUYER_CONFIRM:// 确认收货
                        final CommonDialog commonDialog = DialogUtils
                                .getCommonDialog(SelfSupportOrderDetailActivity.this,
                                        R.string.make_sure_recriver_goods);
                        commonDialog.setTextColor(0, R.color.text_f7);
                        commonDialog.setTextColor(1, R.color.text_f7);
                        commonDialog.setRightButtonText(R.string.ok);
                        commonDialog.setLeftButtonText(R.string.cancel);
                        commonDialog
                                .setRightButtonCilck(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        makeSureReceiverGoods(shopOrderInfo);
                                        commonDialog.dismiss();
                                    }
                                });
                        commonDialog.setLeftButtonOnClick(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.show();
                        break;
                    case ShopOrderOutline.STATE_COMPLETE:// 已完成
                        PublicWay.stratSelfSupportOrderEvaluateActivity(SelfSupportOrderDetailActivity.this,// 去评价
                                shopOrderInfo.OrderId, shopOrderInfo.Products.get(0).ImageUrl, shopOrderInfo.ConfirmTime, REQUEST_CODE);
                        break;
                }


                break;
        }
    }

    private void showCancelDialog(final long orderIds) {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialog(this,
                        R.string.check_cancel_order);
        commonDialog.setTextColor(0, R.color.text_f7);
        commonDialog.setTextColor(1, R.color.text_f7);
        commonDialog.setRightButtonText(R.string.ok);
        commonDialog.setLeftButtonText(R.string.cancel);
        commonDialog
                .setRightButtonCilck(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrder(orderIds);
                        commonDialog.dismiss();
                    }
                });
        commonDialog.setLeftButtonOnClick(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private final int REQUEST_CODE_CALL_PHONE = 110;

    private void showDialogToCall() {
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
                if (PermissionUtil.hasPermissions(SelfSupportOrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE})) {
                    call();
                } else {
                    PermissionUtil.requestPermissions(SelfSupportOrderDetailActivity.this, REQUEST_CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE});
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void call() {
        if (shopOrderInfo != null) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri
                    .parse("tel:" + shopOrderInfo.VenderTel));
            startActivity(intent);
        }
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
            if (!PermissionUtil.hasPermissions(SelfSupportOrderDetailActivity.this, list.get(i))) {
                WWToast.showShort(String.format(getString(R.string.permission_deny_), list.get(i)));
            }
        }
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    private void cancelOrder(long orderId) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", orderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderCancel()),
                new WWXCallBack("OrderCancel") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (data.getBoolean("Data")) {
                            WWToast.showShort(R.string.cancel_order_success);
//                            sendBroadcast(new Intent("order_self").putExtra(Consts.KEY_MODULE, SelfSupportOrderFragment.REFRESH));
                            requestData();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    /**
     * 确认收货
     */
    private void makeSureReceiverGoods(final ShopOrderInfo shopOrderInfo) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", shopOrderInfo.OrderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderConfirm()),
                new WWXCallBack("OrderConfirm") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        requestData();
//                        sendBroadcast(new Intent("order_self").putExtra(Consts.KEY_MODULE, SelfSupportOrderFragment.REFRESH));
                        final CommonDialog commonDialog = DialogUtils.getCommonDialog(SelfSupportOrderDetailActivity.this, getString(R.string.order_evaluate_tips));
                        commonDialog.getButtonLeft(R.string.temp_no_evaluate).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.getButtonRight(R.string.go_evaluate).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                PublicWay.stratSelfSupportOrderEvaluateActivity(SelfSupportOrderDetailActivity.this,// 去评价
                                        shopOrderInfo.OrderId, shopOrderInfo.Products.get(0).ImageUrl, shopOrderInfo.ConfirmTime, REQUEST_CODE);
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.show();

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
        if (arg1 == RESULT_OK) {
            if (arg0 == REQUEST_CODE) {
                //评价或者支付成功需要刷新订单列表
//                sendBroadcast(new Intent("order_self").putExtra(Consts.KEY_MODULE, SelfSupportOrderFragment.REFRESH));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
