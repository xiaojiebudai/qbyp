package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportFollowDeliverActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.distribution.been.DistributionOrder;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
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

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 订单详情
 */

public class DistributionOrderDetailActivity extends FatherActivity {
    @BindView(R.id.tv_name_title)
    TextView tvNameTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_orderid)
    TextView tvOrderid;
    @BindView(R.id.state)
    TextView state;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.tv_allprice)
    TextView tvAllprice;
    @BindView(R.id.tv_alljifen)
    TextView tvAlljifen;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    @BindView(R.id.tv_send_type)
    TextView tvSendType;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.tv_address_phone)
    TextView tvAddressPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_createtime)
    TextView tvCreatetime;
    @BindView(R.id.tv_paytime)
    TextView tvPaytime;
    @BindView(R.id.ll_send_time)
    LinearLayout ll_send_time;
    @BindView(R.id.ll_get_time)
    LinearLayout ll_get_time;
    @BindView(R.id.tv_canceltime)
    TextView tvCanceltime;
    @BindView(R.id.ll_create_time)
    LinearLayout ll_create_time;
    @BindView(R.id.ll_pay_time)
    LinearLayout ll_pay_time;
    @BindView(R.id.tv_sendtime)
    TextView tvSendtime;
    @BindView(R.id.tv_gettime)
    TextView tvGettime;
    @BindView(R.id.ll_cancel_time)
    LinearLayout ll_cancel_time;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_order_operation_right)
    TextView tvOrderOperationRight;
    @BindView(R.id.tv_order_operation_left)
    TextView tvOrderOperationLeft;
    @BindView(R.id.ll_operate)
    LinearLayout llOperate;
    @BindView(R.id.tv_postage)
    TextView tv_postage;
    @BindView(R.id.tv_postage_weight)
    TextView tv_postage_weight;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    private long orderId;
    private DistributionOrder orderInfo;
    public static final int REQUEST_CODE = 6661;
    public static final int REQUEST_CODE1 = 6662;


    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_order_detail;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order_detail, true);
        orderId = getIntent().getLongExtra(Consts.KEY_DATA, 0);
    }

    @Override
    protected void initView() {
        getOrderInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE || requestCode == REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                //刷新所有订单
                sendBroadcast(new Intent(DistributionOrderActivity.IS_REFRESH));
                getOrderInfo();
            }
        }
    }

    private void getOrderInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiDistribution.OrderInfo());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        x.http().get(params, new WWXCallBack("OrderInfo") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject jsonObject = data.getJSONObject("Data");
                orderInfo = JSON.parseObject(jsonObject.toJSONString(), DistributionOrder.class);
                setView(orderInfo);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });


    }

    private void setView(final DistributionOrder orderInfo) {
        tvNameTitle.setText(orderInfo.IsUser ? "发货商：" : "会员账号：");
        tvName.setText(orderInfo.IsUser ? (TextUtils.isEmpty(orderInfo.SellerUserName) ? "" : orderInfo.SellerUserName) : orderInfo.BuyUserName + "");
        tvOrderid.setText("订单号" + orderInfo.OrderId);
        state.setText(DistributionOrder.getStateString(orderInfo.Status));
        WWViewUtil.setDistributionGoodsView(this, llContainer, orderInfo.Goods, 2);
        tv_postage_weight.setText("(总重:" + WWViewUtil.numberFormatNoZero(orderInfo.TotalWeight) + "kg)");
        tv_postage.setText("+" + WWViewUtil.numberFormatPrice(orderInfo.PostFee));
        tvTotalMoney.setText(WWViewUtil.numberFormatPrice(orderInfo.GoodsAmt));
        tvAllprice.setText(WWViewUtil.numberFormatPrice(orderInfo.TotalAmt));
        tvAlljifen.setText(WWViewUtil.numberFormatWithTwo(orderInfo.TotalConsume));
        //支付方式显示
        if (!TextUtils.isEmpty(orderInfo.PayCode) && orderInfo.Status != DistributionOrder.STATE_WAIT_PAY) {
            tvPayWay.setText(Order.getPayWayString(orderInfo.PayCode));
        }
        setTime(ll_create_time, tvCreatetime, orderInfo.CreateTime);
        setTime(ll_pay_time, tvPaytime, orderInfo.PayTime);
        setTime(ll_send_time, tvSendtime, orderInfo.SendTime);
        setTime(ll_get_time, tvGettime, orderInfo.ReceiverTime);
        setTime(ll_cancel_time, tvCanceltime, orderInfo.CancelTime);

        if (orderInfo.Status == DistributionOrder.STATE_WAIT_GET || orderInfo.Status == DistributionOrder.STATE_COMPLETE) {
            //与状态相关
            ll_address.setVisibility(View.VISIBLE);
            tvSendType.setText(orderInfo.SendMode == 1 ? "自提" : "物流");

        } else {
            ll_address.setVisibility(View.GONE);
        }
        tvAddressName.setText(orderInfo.Consignee);
        tvAddressPhone.setText(orderInfo.ReceiverMobile);
        tvAddress.setText(orderInfo.Address);
        tvInfo.setText(orderInfo.BuyMemo);

        switch (orderInfo.Status) {
            case DistributionOrder.STATE_WAIT_PAY:

                if (orderInfo.IsUser) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvOrderOperationRight.setText(R.string.cancel_order);
                    tvOrderOperationLeft.setText(R.string.pay_soon);
                    tvOrderOperationRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(DistributionOrderDetailActivity.this, "您确定要取消该订单么？", true);
                            dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                            dialog.setRightButtonCilck(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    orderOperate(orderInfo, ApiDistribution.OrderCancel(), "OrderCancel");
                                }
                            });
                            dialog.show();

                        }
                    });
                    tvOrderOperationLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //支付成功之后再做刷新
                            PublicWay.startPayOrderActivity(DistributionOrderDetailActivity.this, orderInfo.TotalAmt, orderInfo.OrderId, REQUEST_CODE, PayOrdersActivity.DISTRIBUTION);
                        }
                    });
                } else {
                    llOperate.setVisibility(View.GONE);
                }


                break;
            case DistributionOrder.STATE_WAIT_SEND:
                if (orderInfo.IsUser) {
                    llOperate.setVisibility(View.GONE);
                } else {
                    llOperate.setVisibility(View.VISIBLE);
                    tvOrderOperationRight.setText(R.string.no_goods);
                    tvOrderOperationLeft.setText(R.string.send_goods);
                    tvOrderOperationRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(DistributionOrderDetailActivity.this, "释放该订单，您的上级代理将为该订单发货，确定放弃该订单么？", true);
                            dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                            dialog.setRightButtonCilck(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    orderOperate(orderInfo, ApiDistribution.OrderNoGoods(), "OrderNoGoods");
                                }
                            });
                            dialog.show();
                        }
                    });
                    tvOrderOperationLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //发货成功了再刷新，先取用户积分
                            showWaitDialog();
                            x.http().get(ParamsUtils.getSessionParams(ApiDistribution.MyAccount()), new WWXCallBack("MyAccount") {
                                @Override
                                public void onAfterSuccessOk(JSONObject data) {
                                    DistributionPublicAccount distributionPublicAccount = JSONObject.parseObject(data.getString("Data"), DistributionPublicAccount.class);
                                    if (distributionPublicAccount.ConsumeUnfrozen < orderInfo.TotalConsume) {
                                        final CommonDialog dialog = DialogUtils.getSingleBtnDialog(DistributionOrderDetailActivity.this, "操作发货，商品积分将一同赠出，目前您的未解冻积分已经不够赠送，请购买相应商品获得积分后再操作发货", "确定", true);
                                        dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                                        dialog.setRightButtonCilck(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.show();
                                    } else {
                                        PublicWay.startDistributionSendPackageActivity(DistributionOrderDetailActivity.this, orderInfo.OrderId, orderInfo.TotalConsume, REQUEST_CODE1);
                                    }
                                }

                                @Override
                                public void onAfterFinished() {
                                    dismissWaitDialog();
                                }
                            });
                        }
                    });
                }
                break;
            case DistributionOrder.STATE_WAIT_GET:
                if (orderInfo.IsUser) {
                    llOperate.setVisibility(View.VISIBLE);
                    tvOrderOperationRight.setText(R.string.look_post);
                    tvOrderOperationLeft.setText(R.string.make_suer_receiver_goods);
                    tvOrderOperationRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            checkOrderLogs(orderInfo);

                        }
                    });
                    tvOrderOperationLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(DistributionOrderDetailActivity.this, "您确定已经收到该商品了么？", true);
                            dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                            dialog.setRightButtonCilck(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    orderOperate(orderInfo, ApiDistribution.OrderReceive(), "OrderReceive");

                                }
                            });
                            dialog.show();
                        }
                    });
                } else {
                    llOperate.setVisibility(View.VISIBLE);
                    tvOrderOperationRight.setVisibility(View.GONE);
                    tvOrderOperationLeft.setText(R.string.look_post);
                    tvOrderOperationLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkOrderLogs(orderInfo);
                        }
                    });
                }


                break;
            case DistributionOrder.STATE_COMPLETE:
                llOperate.setVisibility(View.VISIBLE);
                tvOrderOperationRight.setVisibility(View.GONE);
                tvOrderOperationLeft.setText(R.string.look_post);
                tvOrderOperationLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkOrderLogs(orderInfo);
                    }
                });
                break;
            case DistributionOrder.STATE_CANCEL:
                llOperate.setVisibility(View.GONE);
                break;
            case DistributionOrder.STATE_CHECK:
                llOperate.setVisibility(View.GONE);
                break;
            default:
                break;
        }


    }

    private void checkOrderLogs(DistributionOrder orderInfo) {
        //无订单操作
        PublicWay.stratSelfSupportFollowDeliverActivity(DistributionOrderDetailActivity.this, orderInfo.OrderId, SelfSupportFollowDeliverActivity.FENXIAO, 1);
    }

    /**
     * 订单操作
     *
     * @param distributionOrder
     * @param apiUrl
     * @param resultCode
     */
    private void orderOperate(final DistributionOrder distributionOrder, String apiUrl, String resultCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("orderId", distributionOrder.OrderId);
        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, apiUrl), new WWXCallBack(resultCode) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getBoolean("Data")) {
                    sendBroadcast(new Intent(DistributionOrderActivity.IS_REFRESH));
                    WWToast.showShort(R.string.set_succuss);
                    getOrderInfo();
                }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void setTime(View view, TextView tvCreatetime, long time) {
        if (time > 0) {
            view.setVisibility(View.VISIBLE);
            tvCreatetime.setText(TimeUtil.getTimeToS(time * 1000));
        } else {
            view.setVisibility(View.GONE);
        }

    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
