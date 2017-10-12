package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单详情
 *
 * @author licheng
 */
public class OrderDetailActivity extends FatherActivity implements OnClickListener {
    public static final int REQUEST_CODE = 10888;
    @BindView(R.id.tv_private_integral)
    TextView tvPrivateIntegral;
    @BindView(R.id.rl_0)
    RelativeLayout rl_0;
    @BindView(R.id.tv_ub)
    TextView tv_ub;
    @BindView(R.id.rl_1)
    RelativeLayout rl_1;
    @BindView(R.id.tv_can_withdr_yue)
    TextView tv_can_withdr_yue;
    @BindView(R.id.rl_2)
    RelativeLayout rl_2;
    private TextView mName;
    private TextView mOrderNum;
    private TextView mGoodsState;
    private TextView mSumPrice;
    private TextView mNeedPay;
    private TextView mPeiSongFei;
    private TextView mTvTextShow;
    private LinearLayout mContainer;
    private TextView mReceiverMan;
    private TextView mPhone;
    private TextView mAddress;
    private TextView mData;
    private TextView mTime;
    private TextView tvGaveIntegral;
    // private long sellerId;
    private long orderId;
    private Order order;
    private TextView mBottomTextView, tv_look_good_position;
    private View payWay;
    private TextView tvPlaceAnOrderTime;
    private TextView tvGoodPayTime;
    /**
     *
     */
    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                tvGaveIntegral.setText(WWViewUtil.numberFormatWithTwo(order.PersentIntegral));
                if (order.VipAmt > 0) {
                    rl_0.setVisibility(View.VISIBLE);
                    tvPrivateIntegral.setText("-" + WWViewUtil.numberFormatPrice(order.VipAmt));
                } else {
                    rl_0.setVisibility(View.GONE);
                }
                if (order.FenxiaoConsumePay > 0) {
                    rl_1.setVisibility(View.VISIBLE);
                    tv_ub.setText("-" + WWViewUtil.numberFormatPrice(order.FenxiaoConsumePay));
                } else {
                    rl_1.setVisibility(View.GONE);
                }
                if (order.BalanceAmt > 0) {
                    rl_2.setVisibility(View.VISIBLE);
                    tv_can_withdr_yue.setText("-" + WWViewUtil.numberFormatPrice(order.BalanceAmt));
                } else {
                    rl_2.setVisibility(View.GONE);
                }
                mName.setText(order.SellerName);
                mOrderNum.setText(order.OrderId + "");
                mSumPrice.setText("¥" + WWViewUtil.numberFormatWithTwo(order.TotalAmt) + "");
                mNeedPay.setText(WWViewUtil.numberFormatPrice(order.PayAmt));
                mPeiSongFei.setText(getString(R.string.deliverFee)
                        + "¥" + WWViewUtil.numberFormatWithTwo(order.DeliverFee)
                        + getString(R.string.deliverFee_yuan));
                mReceiverMan.setText(order.ReceiverName);
                mPhone.setText(order.Mobile);
                mAddress.setText(order.Address);
                mData.setText(order.SendTime);
                if (order.SendMode == Order.GO_SHOP) {// 到店
                    mExplain.setVisibility(View.GONE);
                    mReceiverMessage.setVisibility(View.GONE);
                } else if (order.SendMode == Order.PEISONG) {// 配送
                    if (order.UserExplain != null) {
                        mTvExplain.setText(order.UserExplain);
                    }
                    mExplain.setVisibility(View.VISIBLE);
                    mReceiverMessage.setVisibility(View.VISIBLE);
                }
                //支付方式显示
                if (!TextUtils.isEmpty(order.PayCode)) {
                    tvPayWay.setText(Order.getPayWayString(order.PayCode));
                }
                switch (order.Status) {

                    case Order.STATE_CANCEL:// 取消
                        mGoodsState.setText(getString(R.string.canceled));
                        break;
                    case Order.STATE_CLOSE:// 已关闭
                        mGoodsState.setText(getString(R.string.closed));
                        mTvTextShow.setText(R.string.pay_colon);
                        break;
                    case Order.STATE_COMPLETE:// 已完成
                        mGoodsState.setText(getString(R.string.finished));
                        mTvTextShow.setText(R.string.pay_colon);
                        break;
                    case Order.STATE_REFUND:// 已退款
                        mGoodsState.setText(getString(R.string.back_money));
                        mTvTextShow.setText(R.string.pay_colon);
                        break;
                    case Order.STATE_WAIT_BUYER_CONFIRM:// 待确认
                        mBottomTextView.setVisibility(View.VISIBLE);
                        mGoodsState.setText(getString(R.string.wait_goods));
                        mTvTextShow.setText(R.string.pay_colon);
                        mBottomTextView
                                .setText(getString(R.string.make_suer_receiver_goods));
                        mBottomTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                makeSureReceiverGoods();
                            }
                        });
                        break;
                    case Order.STATE_WAIT_PAY:// 待付款
                        mGoodsState.setText(getString(R.string.wait_pay));
                        mBottomTextView.setVisibility(View.VISIBLE);
                        mBottomTextView.setText(getString(R.string.cancel_order));
                        mBottomTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelOrder();
                            }
                        });
                        tv_look_good_position.setVisibility(View.VISIBLE);
                        tv_look_good_position.setText(getString(R.string.pay_soon));
                        tv_look_good_position.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PublicWay.startPayOrderActivity(OrderDetailActivity.this,
                                        order.PayAmt, order.OrderId,
                                        REQUEST_CODE, PayOrdersActivity.LOCAL);
                            }
                        });
                        break;
                    case Order.STATE_WAIT_SELLER_CONFIRM:// 待商家确认
                        mGoodsState.setText(getString(R.string.wait_shop_sure));
                        mTvTextShow.setText(R.string.pay_colon);
                        mBottomTextView.setVisibility(View.VISIBLE);
                        mBottomTextView.setText(getString(R.string.cancel_order));
                        mBottomTextView.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelOrder();
                            }
                        });
                        break;

                    default:
                        break;
                }
                List<Goods> goods = order.Goods;
                for (int i = 0; i < goods.size(); i++) {
                    Goods goods2 = goods.get(i);
                    View v = View.inflate(OrderDetailActivity.this,
                            R.layout.item_order_detail, null);
                    TextView name = (TextView) v
                            .findViewById(R.id.tv_goods_name);
                    TextView money = (TextView) v.findViewById(R.id.tv_money);
                    TextView num = (TextView) v.findViewById(R.id.tv_num);
                    name.setText(goods2.GoodsName);
                    money.setText("¥" + WWViewUtil.numberFormatWithTwo(Arith.round(
                            Arith.mul(goods2.Quantity, goods2.Price), 2)));
                    num.setText("x" + goods2.Quantity + "");
                    mContainer.addView(v);
                }

                tvPlaceAnOrderTime.setText(TimeUtil.getTimeToS(order.CreateTime * 1000));
                if (order.PayTime > 0) {
                    tvGoodPayTime.setText(TimeUtil.getTimeToS(order.PayTime * 1000));
                } else {
                    tvGoodPayTime.setText(R.string.order_pay_un_finish);
                }

            }
        }
    };
    private TextView tvPayWay;
    private View mReceiverMessage;
    private LinearLayout mExplain;
    private TextView mTvExplain;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orderdetail;
    }

    @Override
    protected void initValues() {
        orderId = getIntent().getLongExtra("orderId", -1);
        initDefautHead(R.string.order_detail, true);

    }

    @Override
    protected void initView() {
        mExplain = (LinearLayout) findViewById(R.id.ll_shuoming);
        mTvExplain = (TextView) findViewById(R.id.tv_peisong_shuoming);
        mReceiverMessage = findViewById(R.id.ll_receiver_message);
        mName = (TextView) findViewById(R.id.tv_name);
        mOrderNum = (TextView) findViewById(R.id.tv_order_num);
        mGoodsState = (TextView) findViewById(R.id.foods_state);
        mSumPrice = (TextView) findViewById(R.id.tv_sum_price);
        mNeedPay = (TextView) findViewById(R.id.tv_need_pay);
        mPeiSongFei = (TextView) findViewById(R.id.tv_peisong_price);
        mTvTextShow = (TextView) findViewById(R.id.tv_text_show);
        mContainer = (LinearLayout) findViewById(R.id.ll_container);
        mReceiverMan = (TextView) findViewById(R.id.tv_receive_people);
        mPhone = (TextView) findViewById(R.id.tv_phone_num);
        mAddress = (TextView) findViewById(R.id.tv_address);
        mData = (TextView) findViewById(R.id.tv_data);
        mTime = (TextView) findViewById(R.id.tv_time);
        mBottomTextView = (TextView) findViewById(R.id.tv_receiver_goods);
        tv_look_good_position = (TextView) findViewById(R.id.tv_look_good_position);
        tvPayWay = (TextView) findViewById(R.id.tv_pay_way);
        tvGaveIntegral = (TextView) findViewById(R.id.tv_gave_integral);
        tvPlaceAnOrderTime = (TextView) findViewById(R.id.tv_place_an_order_time);
        tvGoodPayTime = (TextView) findViewById(R.id.tv_good_pay_time);
        payWay = findViewById(R.id.ll_select_pay_way);
        findViewById(R.id.rl_business_container).setOnClickListener(this);
    }

    @Override
    protected void doOperate() {

        getOrderInfo(orderId);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                getOrderInfo(orderId);
            }
        }
    }

    /**
     * 确认收货
     */
    private void makeSureReceiverGoods() {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", orderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiTrade.OrderUserConfirm()),
                new WWXCallBack("OrderUserConfirm") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        Intent intent = new Intent(OrderDetailActivity.this,
                                ShopCommentActivity.class);
                        intent.putExtra("orderId", orderId);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    /**
     * 取消订单
     */
    private void cancelOrder() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiTrade.OrderCancel());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        x.http().get(params, new WWXCallBack("OrderCancel") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                WWToast.showShort(R.string.cancel_order_success);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

        });

    }

    /**
     * 主要获取收货人信息
     */
    private void getOrderInfo(long orderId) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiTrade.getOrderInfo());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        x.http().get(params, new WWXCallBack("OrderInfo") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject jsonObject = data.getJSONObject("Data");
                order = JSON.parseObject(jsonObject.toJSONString(), Order.class);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_business_container:
                if (order != null) {
                    PublicWay.startStoreActivity(this, order.SellerId, 0);
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
