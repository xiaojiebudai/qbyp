package cn.net.chenbao.qbyp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.GoodsAdaper;
import cn.net.chenbao.qbyp.adapter.listview.GoodsAdaper.PriceCallBack;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.bean.OrderDetail;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.dialog.TimeSelectDialog;
import cn.net.chenbao.qbyp.dialog.TimeSelectDialog.TimeCallBack;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.ExpandListview;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 积分 u币 对应字段确认
 */
public class MakeSuerOrderActivity extends FatherActivity implements
        OnCheckedChangeListener, OnClickListener {
    /**
     * 到店
     */
    public static final int GO_SHOP = 0;
    /**
     * 配送
     */
    public static final int PEI_SONG = 1;
    /**
     * 购买方式
     */
    public int shoppingWay;

    @BindView(R.id.ll_can_use_integral)
    LinearLayout llCanUseIntegral;
    @BindView(R.id.tv_consume_integral)
    TextView tvConsumeIntegral;
    @BindView(R.id.tv_integral_deduction_money)
    TextView tvIntegralDeductionMoney;
    @BindView(R.id.iv_toggle_integral)
    ImageView ivToggleIntegral;

    @BindView(R.id.ll_can_use_U)
    LinearLayout llCanUseU;
    @BindView(R.id.tv_consume_U)
    TextView tvConsumeU;
    @BindView(R.id.tv_U_deduction_money)
    TextView tvUDeductionMoney;
    @BindView(R.id.iv_toggle_U)
    ImageView ivToggleU;

    @BindView(R.id.ll_yue)
    LinearLayout llYue;
    @BindView(R.id.tv_yue)
    TextView tvYue;
    @BindView(R.id.iv_toggle_yue)
    ImageView ivToggleYue;

    private RadioGroup mRadioGroup;
    /**
     * 地址
     */
    private LinearLayout mllPeisongContainer;
    /**
     * 留言
     */
    private LinearLayout mllMessageContainer;
    /**
     * 配送费等
     */
    private LinearLayout mllJisuanContainer;
    private ExpandListview mListView;
    private long sellerId;
    private OrderDetail orderDetail;
    private GoodsAdaper mAdapter;
    private TextView mData;// 日期
    private TextView mTime;// 时间
    private TextView mName;// 姓名
    private TextView mPhone;// 电话
    private TextView mAddress;// 地址
    private EditText mLeaveMessage;// 买家留言
    private TextView mSumPrice;// 商品合计费
    private TextView mPeiSongPrice;// 配送费
    private TextView mPackeingPrice;// 打包费
    private TextView mTimeWay;
    private TextView mNum;// 总合计
    private TextView mMoney, tv_money_tip;// 总价
    private Address address;
    private String json;// 商家信息json
    private TimeSelectDialog dialog;
    private String sendTime;
    private TradesMessage parseObject;
    private double goShopMoney;
    private boolean isFrist = true;
    private boolean isUseIntegral = false;
    private boolean isUseU = false;
    private boolean isUseBalance = false;
    public static final int INTERNAL_PAY_REQUEST_CODE = 666;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if ((shoppingWay == GO_SHOP ? orderDetail.VipPay1 : orderDetail.VipPay) != 0) {
                llCanUseIntegral.setVisibility(View.VISIBLE);
            }

            if ((shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumePayNoDeliver : orderDetail.FenxiaoConsumePay) != 0) {
                llCanUseU.setVisibility(View.VISIBLE);
            }

            if (orderDetail.BalancePay != 0) {
                llYue.setVisibility(View.VISIBLE);
            }

            setConsumeText();
            tvYue.setText("-" + WWViewUtil.numberFormatPrice(orderDetail.BalancePay));

            mMoney.setText(WWViewUtil.numberFormatWithTwo(shoppingWay == GO_SHOP ? orderDetail.GoodsAmt : orderDetail.TotalAmt));//rule 10%
            mPackeingPrice.setText(WWViewUtil.numberFormatPrice(orderDetail.PackageFee));
            mPeiSongPrice.setText(WWViewUtil.numberFormatPrice(orderDetail.DeliverFee));
            mSumPrice.setText(WWViewUtil.numberFormatPrice(orderDetail.GoodsAmt));
            mNum.setText(orderDetail.Quantity + "");
            calculateSumNew((shoppingWay == GO_SHOP ? orderDetail.GoodsAmt : orderDetail.TotalAmt));//设置总价
            goShopMoney = orderDetail.GoodsAmt;
        }

    };
    private String jinkuaiTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_makesure_order;
    }

    @Override
    protected void initValues() {
        sellerId = getIntent().getLongExtra("sellerId", -1);
        json = getIntent().getStringExtra("json");
        initDefautHead(R.string.make_suer_order, true);
        parseObject = JSON.parseObject(json, TradesMessage.class);
        dialog = new TimeSelectDialog(this, parseObject.BusinessEnd,
                parseObject.BusinessStart);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });
        dialog.setTimeListener(new TimeCallBack() {
            @Override
            public void timeListener(String time) {
                mData.setText(time);
                if (!time.contains("尽快送达")) {
                    sendTime = TimeUtil.getDay() + " " + time;
                } else {
                    sendTime = TimeUtil.getDay() + " " + jinkuaiTime;
                }
            }
        });
    }

    @Override
    protected void initView() {
        mData = (TextView) findViewById(R.id.tv_data);
        mTime = (TextView) findViewById(R.id.tv_time);
        mName = (TextView) findViewById(R.id.tv_name);
        mPhone = (TextView) findViewById(R.id.tv_phone_num);
        mAddress = (TextView) findViewById(R.id.address);
        mLeaveMessage = (EditText) findViewById(R.id.ed_message);
        mSumPrice = (TextView) findViewById(R.id.tv_goods_sum_price);
        mPeiSongPrice = (TextView) findViewById(R.id.tv_peisong_price);
        mPackeingPrice = (TextView) findViewById(R.id.tv_packaging_price);
        mTimeWay = (TextView) findViewById(R.id.tv_time_way);
        mNum = (TextView) findViewById(R.id.tv_num);
        mMoney = (TextView) findViewById(R.id.tv_money);
        tv_money_tip = (TextView) findViewById(R.id.tv_money_tip);

        mllPeisongContainer = (LinearLayout) findViewById(R.id.ll_peisong_container);
        mllMessageContainer = (LinearLayout) findViewById(R.id.ll_message_container);
        mllJisuanContainer = (LinearLayout) findViewById(R.id.ll_jisuan_container);
        mListView = (ExpandListview) findViewById(R.id.lv);
        mAdapter = new GoodsAdaper(this, GoodsAdaper.MODE_ORDER);
        mAdapter.setPriceListener(new PriceCallBack() {

            @Override
            public void subGoodsListener(double price) {// 减
                orderDetail.TotalAmt = Arith.sub(orderDetail.TotalAmt, price);
                orderDetail.GoodsAmt = Arith.sub(orderDetail.GoodsAmt, price);
                orderDetail.Quantity--;
                if (orderDetail.Quantity <= 0) {
                    mNum.setVisibility(View.GONE);
                } else {
                    mNum.setText(orderDetail.Quantity + "");
                }
                mMoney.setText(WWViewUtil.numberFormatPrice(orderDetail.TotalAmt));
                mSumPrice.setText(WWViewUtil.numberFormatPrice(orderDetail.GoodsAmt));
            }

            @Override
            public void addGoodsListener(double price) {// 加
                orderDetail.TotalAmt = Arith.add(orderDetail.TotalAmt, price);
                orderDetail.GoodsAmt = Arith.add(orderDetail.GoodsAmt, price);
                orderDetail.Quantity++;
                if (orderDetail.Quantity <= 99) {
                    if (mNum.getVisibility() == View.GONE) {
                        mNum.setVisibility(View.VISIBLE);
                    }
                    mNum.setText(orderDetail.Quantity + "");
                }
                mMoney.setText(WWViewUtil.numberFormatPrice(orderDetail.TotalAmt));
                mSumPrice.setText(WWViewUtil.numberFormatPrice(orderDetail.GoodsAmt));
            }
        });
        mRadioGroup = (RadioGroup) findViewById(R.id.rg);
        mRadioGroup.setOnCheckedChangeListener(this);
        mllPeisongContainer.setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
        findViewById(R.id.tv_order_commit).setOnClickListener(this);

        mListView.setAdapter(mAdapter);
    }

    @Override
    protected void doOperate() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        int hour = instance.get(Calendar.HOUR_OF_DAY);
        int min = instance.get(Calendar.MINUTE);
        long currentTime = getSelectTimeInMillis(hour + "", min + "");
        long comeTime = currentTime + 2 * 60 * 60 * 1000 + 10 * 60 * 1000;
        jinkuaiTime = TimeUtil.getHourAndMin(comeTime);
        sendTime = TimeUtil.getDay() + " " + TimeUtil.getHourAndMin(comeTime);
        mData.setText(getString(R.string.come_soon) + "("
                + getString(R.string.yuji) + TimeUtil.getHourAndMin(comeTime)
                + getString(R.string.songda) + ")");

        getOrderPreview();
        getDefaultUserAddress();

        if (!parseObject.SupportSince) {
            mRadioGroup.getChildAt(1).setVisibility(View.GONE);

        } else {
            ((RadioButton) mRadioGroup.getChildAt(1)).setChecked(true);
        }

        if (!parseObject.SupportDeliver) {
            mRadioGroup.getChildAt(0).setVisibility(View.GONE);

        } else {
            ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_peisong:// 配送
                mTimeWay.setText(R.string.send_time);
                mllPeisongContainer.setVisibility(View.VISIBLE);
                mllMessageContainer.setVisibility(View.VISIBLE);
                mllJisuanContainer.setVisibility(View.VISIBLE);
                shoppingWay = PEI_SONG;
                setConsumeText();
                if (!isFrist) {
                    calculateSumNew(orderDetail.TotalAmt);
                }
                break;

            case R.id.rb_go_shop:// 到店
                mTimeWay.setText(R.string.order_time);
                mllPeisongContainer.setVisibility(View.GONE);
                mllMessageContainer.setVisibility(View.GONE);
                mllJisuanContainer.setVisibility(View.GONE);
                shoppingWay = GO_SHOP;
                isFrist = false;
                setConsumeText();
                calculateSumNew(goShopMoney);
                break;
        }

    }

    private void setConsumeText() {
        tvConsumeIntegral.setText(WWViewUtil.numberFormatWithTwo((shoppingWay == GO_SHOP ? orderDetail.VipPay1 : orderDetail.VipPay)));
        tvIntegralDeductionMoney.setText(WWViewUtil.numberFormatPrice((shoppingWay == GO_SHOP ? orderDetail.VipAmt1 : orderDetail.VipAmt)));
        tvConsumeU.setText(WWViewUtil.numberFormatWithTwo((shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumePayNoDeliver : orderDetail.FenxiaoConsumePay)));
        tvUDeductionMoney.setText(WWViewUtil.numberFormatPrice((shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumeAmtNoDeliver : orderDetail.FenxiaoConsumeAmt)));

    }

    @OnClick({R.id.iv_toggle_integral, R.id.iv_toggle_U, R.id.iv_toggle_yue})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_commit:
                if (isUseIntegral || isUseU || isUseBalance) {
                    isSetPaypsd();
                } else {
                    orderCommit(null);
                }
                break;
            case R.id.ll_peisong_container:
                // 选择地址
                PublicWay.startAddressListActivity(MakeSuerOrderActivity.this, 888,
                        AddressListActivity.SELECT);
                break;
            case R.id.ll_time:// 时间选择
                dialog.show();
                break;
            case R.id.iv_toggle_integral:

                isUseIntegral = !isUseIntegral;
                if (isUseIntegral) {
                    ivToggleIntegral.setBackgroundResource(R.drawable.activate_button);
                } else {
                    ivToggleIntegral.setBackgroundResource(R.drawable.inactive_button);
                }
                if (isUseIntegral) {
                    isUseU = false;
                    ivToggleU.setBackgroundResource(R.drawable.inactive_button);
                }
                calculateSumNew(shoppingWay == PEI_SONG ? orderDetail.TotalAmt : goShopMoney);
                break;
            case R.id.iv_toggle_U:
                isUseU = !isUseU;
                if (isUseU) {
                    ivToggleU.setBackgroundResource(R.drawable.activate_button);
                } else {
                    ivToggleU.setBackgroundResource(R.drawable.inactive_button);
                }
                if (isUseU) {
                    isUseIntegral = false;
                    ivToggleIntegral.setBackgroundResource(R.drawable.inactive_button);
                }
                calculateSumNew(shoppingWay == PEI_SONG ? orderDetail.TotalAmt : goShopMoney);

                break;
        }
    }

    private void calculateSumNew(double totalAmt) {
        if (isUseIntegral) {
            if (totalAmt - (shoppingWay == GO_SHOP ? orderDetail.VipAmt1 : orderDetail.VipAmt) <= 0) {
                mMoney.setText(WWViewUtil.numberFormatPrice(0));
                tv_money_tip.setText(String.format(getString(R.string.already_sub_money), WWViewUtil.numberFormatPrice(totalAmt)));
            } else {
                mMoney.setText(WWViewUtil.numberFormatPrice(totalAmt - (shoppingWay == GO_SHOP ? orderDetail.VipAmt1 : orderDetail.VipAmt)));
                tv_money_tip.setText(String.format(getString(R.string.already_sub_money), WWViewUtil.numberFormatPrice((shoppingWay == GO_SHOP ? orderDetail.VipAmt1 : orderDetail.VipAmt))));
            }
        } else if (isUseU) {

            if (totalAmt - (shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumeAmtNoDeliver : orderDetail.FenxiaoConsumeAmt) <= 0) {
                mMoney.setText(WWViewUtil.numberFormatPrice(0));
                tv_money_tip.setText(String.format(getString(R.string.already_sub_money), WWViewUtil.numberFormatPrice(totalAmt)));
            } else {
                mMoney.setText(WWViewUtil.numberFormatPrice(totalAmt - (shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumeAmtNoDeliver : orderDetail.FenxiaoConsumeAmt)));
                tv_money_tip.setText(String.format(getString(R.string.already_sub_money), WWViewUtil.numberFormatPrice((shoppingWay == GO_SHOP ? orderDetail.FenxiaoConsumeAmtNoDeliver : orderDetail.FenxiaoConsumeAmt))));
            }
        } else {
            mMoney.setText(WWViewUtil.numberFormatPrice(orderDetail.TotalAmt));
            tv_money_tip.setText(R.string.already_sub_0_money);
        }
    }

    private void isSetPaypsd() {
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("HavePaypsd") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                boolean havePayPsw = data.getBooleanValue("Data");
                if (havePayPsw) {
                    Intent intent = new Intent(MakeSuerOrderActivity.this, VerifyPasswordActivity.class);
                    intent.putExtra(Consts.KEY_MODULE, VerifyPasswordActivity.MODE_NOMAL);
                    startActivityForResult(intent, 666);
                } else {
                    PublicWay.startVerifyPhoneNumberActivity(MakeSuerOrderActivity.this, VerifyPhoneNumberActivity.PAY, "", 10086);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    /**
     * 提交订单
     */
    private void orderCommit(String psw) {
//		if (!dialog.isOpen()) {
//			WWToast.showShort(R.string.shop_close);
//			return;
//		}
        JSONObject jsonObject = null;
        switch (shoppingWay) {
            case PEI_SONG:
                if (TextUtils.isEmpty(sendTime)) {
                    WWToast.showShort(R.string.songhuo_time);
                    return;
                }
                jsonObject = new JSONObject();
                jsonObject.put(Consts.SELLER_ID, sellerId);
                jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                        .getSessionId());
                jsonObject.put("sendMode", PEI_SONG);
                jsonObject.put("sendTime", sendTime);
                if (address != null) {
                    jsonObject.put("addressId", address.AddressId);
                } else {
                    WWToast.showShort(R.string.please_input_address);
                    return;
                }

                if (!TextUtils.isEmpty(mLeaveMessage.getText().toString().trim())) {
                    jsonObject.put("userExplain", mLeaveMessage.getText()
                            .toString().trim());
                }
                break;
            case GO_SHOP:
                jsonObject = new JSONObject();
                jsonObject.put("sendMode", GO_SHOP);
                jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                        .getSessionId());
                if (address != null) {
                    jsonObject.put("addressId", address.AddressId);
                }

                jsonObject.put(Consts.SELLER_ID, sellerId);
                break;
        }

        if (isUseIntegral) {
            jsonObject.put("useVip", true);
        }
        ZLog.showPost("isUseU  " + isUseU);
        if (isUseU) {
            jsonObject.put("useFenxiao", true);
        }

        if (isUseBalance) {
            jsonObject.put("useBalance", true);
        }

        if (isUseIntegral || isUseU || isUseBalance) {
            if (!TextUtils.isEmpty(psw)) {
                jsonObject.put("payPsd", psw);
            }
        }
        showWaitDialog();
        RequestParams postJsonParams = ParamsUtils.getPostJsonParams(
                jsonObject, ApiTrade.orderSubmit());
        x.http().post(postJsonParams, new WWXCallBack("OrderSubmit") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject jsonObject = data.getJSONObject("Data");
                OrderDetail orderDetail = JSON.parseObject(
                        jsonObject.toJSONString(), OrderDetail.class);
                if (orderDetail.PayAmt > 0) {
                    PublicWay.startPayOrderActivity(MakeSuerOrderActivity.this, orderDetail.PayAmt, orderDetail.OrderId, 0, PayOrdersActivity.LOCAL);
                } else {
                    //跳转支付成功界面
                    PublicWay.stratPayResultActivity(MakeSuerOrderActivity.this,
                            PayResultActivity.SUCCESS, orderDetail.OrderId, String.valueOf(orderDetail.PayAmt), 1, PayOrdersActivity.LOCAL);
                }
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
     * 主要获取商品
     */
    private void getOrderPreview() {

        JSONObject jsonObject = JSONObject.parseObject(getIntent().getStringExtra(Consts.KEY_DATA));
        ZLog.showPost(getIntent().getStringExtra(Consts.KEY_DATA));
        orderDetail = JSON.parseObject(jsonObject.getString("Data"),
                OrderDetail.class);
        mAdapter.setDatas(orderDetail.Goods);
        Message msg = mHandler.obtainMessage();
        msg.what = 1;
        mHandler.sendMessage(msg);

//        RequestParams params = new RequestParams(ApiTrade.getOrderDetail());
//        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
//                .getInstance().getSessionId());
//        params.addBodyParameter("sellerId", sellerId + "");
//        x.http().get(params, new WWXCallBack("OrderPreview") {
//
//            @Override
//            public void onAfterSuccessOk(JSONObject data) {
//                JSONObject jsonObject = data.getJSONObject("Data");
//                orderDetail = JSON.parseObject(jsonObject.toJSONString(),
//                        OrderDetail.class);
//                mAdapter.setDatas(orderDetail.Goods);
//                Message msg = mHandler.obtainMessage();
//                msg.what = 1;
//                mHandler.sendMessage(msg);
//            }
//
//            @Override
//            public void onAfterFinished() {
//
//            }
//        });
    }

    /**
     * 获取会员信息
     */
    private void getDefaultUserAddress() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getDefaultAddressGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("DefaultAddressGet", true) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONObject object = data.getJSONObject("Data");
                address = JSON.parseObject(object.toJSONString(), Address.class);
                if (address != null) {
                    mName.setText(address.Consignee);
                    mAddress.setText(address.AddressPre + address.Address);
                    mPhone.setText(address.Mobile);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                if (data.getIntValue("ErrCode") == -1) {
                    mName.setText(R.string.no_address_tips);
                    address = null;
                    mName.setTextColor(getResources().getColor(R.color.text_c1));
                    mAddress.setText("");
                    mPhone.setText("");
                }
                super.onAfterSuccessError(data);
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
            if (arg0 == 888) {
                if (arg2 != null) {
                    address = JSON.parseObject(
                            arg2.getStringExtra(Consts.KEY_DATA), Address.class);
                    mName.setText(address.Consignee);
                    mName.setTextColor(getResources().getColor(R.color.text_f7));
                    mAddress.setText(address.AddressPre + address.Address);
                    mPhone.setText(address.Mobile);
                } else {
                    getDefaultUserAddress();
                }

            }
            if (arg0 == INTERNAL_PAY_REQUEST_CODE) {//使用内部支付时,获取密码跳回来
                String psw = arg2.getStringExtra(Consts.KEY_DATA);
                orderCommit(psw);
            }
        }
    }

    /**
     * 1970.01.01.H.M.0
     *
     * @param first
     * @param second
     * @return
     * @author xl
     * @date:2016-8-24下午5:41:36
     * @description
     */
    private long getSelectTimeInMillis(String first, String second) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        instance.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(first)));
        instance.set(Calendar.MINUTE, (Integer.parseInt(second)));
        instance.set(Calendar.YEAR, 1970);
        instance.set(Calendar.MONTH, Calendar.JANUARY);
        instance.set(Calendar.DATE, 1);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
