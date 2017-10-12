package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.bean.InternalAmtPayItem;
import cn.net.chenbao.qbyp.bean.ShopOrderExplain;
import cn.net.chenbao.qbyp.bean.ShopOrderGoods;
import cn.net.chenbao.qbyp.bean.ShopOrderInfo;
import cn.net.chenbao.qbyp.bean.ShopOrderPreview;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 木头 on 2016/11/1.
 */

public class SelfSupportMakesureOrderActivity extends FatherActivity implements View.OnClickListener {
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_receiver_phone)
    TextView tvReceiverPhone;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;
    @BindView(R.id.ll_order_container)
    LinearLayout llOrderContainer;

    @BindView(R.id.tv_freight)
    TextView tvFreight;
    @BindView(R.id.tv_total_good_num)
    TextView tvTotalGoodNum;
    @BindView(R.id.tv_should_pay_total_money)
    TextView tvShouldPayTotalMoney;
    @BindView(R.id.tv_can_deduction_tips)
    TextView tvCanDeductionTips;
    @BindView(R.id.ll_integral_item_container)
    LinearLayout llIntegralItemContainer;


    private ShopOrderPreview shopOrderPreview;
    private List<ShopOrderExplain> orderExplains = new ArrayList<ShopOrderExplain>();
    private HashMap<Long, EditText> orderExplainViews = new HashMap<Long, EditText>();//每个厂家Id对应的一个留言框
    boolean isNeedPsw = false;
    private long[] flowsId;
    public static final int ADDRESS_REQUEST_CODE = 111;
    public static final int INTERNAL_PAY_REQUEST_CODE = 666;
    public static final int NET_PAY_REQUEST_CODE = 888;
    private ArrayList<CheckBox> checkBoxes;
    private double totalMoney;

    @OnClick({R.id.rl_address_manager, R.id.tv_commit_order})

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address_manager:
                PublicWay.startAddressListActivity(this, ADDRESS_REQUEST_CODE, AddressListActivity.SELECT);
                break;
            case R.id.tv_commit_order:
                if (shopOrderPreview.Address == null) {
                    WWToast.showShort(R.string.please_choice_address);
                    return;
                }

                for (int i = 0; i < checkBoxes.size(); i++) {
                    if (checkBoxes.get(i).isChecked()) {
                        isNeedPsw = true;
                        break;
                    }
                    isNeedPsw = false;
                }
                if (isNeedPsw) {
                    isSetPaypsd();
                } else {
                    orderSubmit(null);
                }
                break;
            default:
                break;
        }
    }

    private void isSetPaypsd() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("HavePaypsd") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                boolean havePayPsw = data.getBooleanValue("Data");
                if (havePayPsw) {
                    Intent intent = new Intent(SelfSupportMakesureOrderActivity.this, VerifyPasswordActivity.class);
                    intent.putExtra(Consts.KEY_MODULE, VerifyPasswordActivity.MODE_NOMAL);
                    startActivityForResult(intent, 666);
                } else {
                    PublicWay.startVerifyPhoneNumberActivity(SelfSupportMakesureOrderActivity.this, VerifyPhoneNumberActivity.PAY, "", 10086);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void orderSubmit(String psw) {
        Iterator iterator = orderExplainViews.keySet().iterator();
        while (iterator.hasNext()) {
            long key = (Long) iterator.next();
            EditText editText = orderExplainViews.get(key);
            String content = editText.getText().toString().trim();
            if (!content.isEmpty()) {
                orderExplains.add(new ShopOrderExplain(key, content));
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("previewId", shopOrderPreview.PreviewId);
        jsonObject.put("addressId", shopOrderPreview.Address.AddressId);

        if (orderExplains.size() > 0) {
            jsonObject.put("userExplain", orderExplains);
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < checkBoxes.size(); i++) {
            CheckBox checkBox = checkBoxes.get(i);
            if (checkBox.isChecked()) {
                InternalAmtPayItem internalAmtPayItem = new InternalAmtPayItem();
                internalAmtPayItem.PayCode = shopOrderPreview.InternalPayList[(int) checkBox.getTag()].PayCode;
                internalAmtPayItem.UseFlag = checkBox.isChecked();
                jsonArray.add(internalAmtPayItem.toJson());
            }
        }
        if (jsonArray.size() > 0) {
            jsonObject.put("internalPay", jsonArray);
        }
        if (isNeedPsw && psw != null) {
            jsonObject.put("payPsd", psw);
        }

        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderSubmit()),
                new WWXCallBack("OrderSubmit") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        ShopOrderPreview shopOrderPreview = JSONObject.parseObject(data.getString("Data"), ShopOrderPreview.class);

                        if (shopOrderPreview != null) {
                            List<ShopOrderInfo> orders = shopOrderPreview.Orders;
                            int size = orders.size();
                            long[] orderIds = new long[size];
                            for (int i = 0; i < size; i++) {
                                orderIds[i] = orders.get(i).OrderId;
                            }
                            closeSelfAndCart();
                            if (shopOrderPreview.PayAmt > 0) {
                                PublicWay.startPayOrderActivity(SelfSupportMakesureOrderActivity.this, shopOrderPreview.PayAmt, orderIds, NET_PAY_REQUEST_CODE, PayOrdersActivity.SELF);
                            } else {
                                //跳转支付成功界面
                                PublicWay.stratPayResultActivity(SelfSupportMakesureOrderActivity.this,
                                        PayResultActivity.SUCCESS, shopOrderPreview.PreviewId, String.valueOf(shopOrderPreview.PayAmt), 1, PayOrdersActivity.SELF);
                            }
                        }
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        if (data.getIntValue("ErrCode") == 301)
                            closeSelfAndCart();
                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });

    }

    private void closeSelfAndCart() {
        int cartTotalNum = SharedPreferenceUtils.getInstance().getCartNum();
        if (shopOrderPreview.Quantity > 0) {
            SharedPreferenceUtils.getInstance().saveCartNum(cartTotalNum - shopOrderPreview.Quantity);
        }
        finish();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_makesure_order;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order_preview, true);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void doOperate() {
        Bundle bundle = getIntent().getExtras();
        String previewOrderJson = bundle.getString(Consts.KEY_DATA);
        flowsId = bundle.getLongArray("FlowsId");
        shopOrderPreview = JSONObject.parseObject(previewOrderJson, ShopOrderPreview.class);
        setDataToUi();
    }

    private void setDataToUi() {
        if (shopOrderPreview.Address == null) {
            tvReceiverName.setText(R.string.please_add_receive_address);
        } else {
            tvReceiverName.setText(shopOrderPreview.Address.Consignee);
            tvReceiverPhone.setText(shopOrderPreview.Address.Mobile);
            tvReceiverAddress.setText(shopOrderPreview.Address.AddressPre + shopOrderPreview.Address.Address);
        }

        totalMoney = shopOrderPreview.TotalAmt;
        tvTotalGoodNum.setText(String.format(getString(R.string.total_number_goods), shopOrderPreview.Quantity));
        tvFreight.setText(String.format(getString(R.string.contain_post_fee), WWViewUtil.numberFormatPrice(shopOrderPreview.PostFee)));

        initOrderList();

        if (shopOrderPreview.ConsumeAmt != 0) {
            tvCanDeductionTips.setVisibility(View.VISIBLE);
            tvCanDeductionTips.setText(String.format(getString(R.string.this_order_can_use_integral_deduction),
                    WWViewUtil.numberFormatPrice(shopOrderPreview.ConsumeAmt)));
        }

        initOrderIntegralItem();

        tvShouldPayTotalMoney.setText(WWViewUtil.numberFormatPrice(totalMoney));
    }

    //动态添加积分抵扣项
    private void initOrderIntegralItem() {
        llIntegralItemContainer.removeAllViews();
        checkBoxes = new ArrayList<>();
        for (int i = 0; i < shopOrderPreview.InternalPayList.length; i++) {
            View integralItem = View.inflate(this, R.layout.self_support_consume_integral_item, null);
            CheckBox cb = (CheckBox) integralItem.findViewById(R.id.cb);
            TextView tv_can_use_integral_num = (TextView) integralItem.findViewById(R.id.tv_can_use_integral_num);
            final TextView tv_deduction = (TextView) integralItem.findViewById(R.id.tv_deduction);

            cb.setChecked(shopOrderPreview.InternalPayList[i].UseFlag);
            cb.setTag(i);
            checkBoxes.add(cb);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    for (int j = 0; j < checkBoxes.size(); j++) {
//                        if (isChecked && j != (int) buttonView.getTag()) {
//                            checkBoxes.get(j).setChecked(false);
//                        }
//                    }
                    tv_deduction.setText(String.format(getString(R.string.sub_money),
                            isChecked ? WWViewUtil.numberFormatPrice(shopOrderPreview.InternalPayList[(int) buttonView.getTag()].UseAmt) : "¥0.00"));
                    calculateSum(isChecked, shopOrderPreview.InternalPayList[(int) buttonView.getTag()].UseAmt);
                }
            });
            if (shopOrderPreview.InternalPayList[i].UseNum == 0) {
                checkBoxes.get(i).setEnabled(false);
            }
            tv_can_use_integral_num.setText(String.format(getString(R.string.consume_integral_deduction_money),
                    WWViewUtil.numberFormatWithTwo(shopOrderPreview.InternalPayList[i].UseNum),
                    shopOrderPreview.InternalPayList[i].PayName,
                    WWViewUtil.numberFormatWithTwo(shopOrderPreview.InternalPayList[i].Balance)));
            llIntegralItemContainer.addView(integralItem);
            if (shopOrderPreview.InternalPayList[i].UseFlag) {
                tv_deduction.setText(String.format(getString(R.string.sub_money), WWViewUtil.numberFormatPrice(shopOrderPreview.InternalPayList[i].UseAmt)));
                calculateSum(shopOrderPreview.InternalPayList[i].UseFlag, shopOrderPreview.InternalPayList[i].UseAmt);
            } else {
                tv_deduction.setText(String.format(getString(R.string.sub_money), "¥0.00"));
            }
        }
    }

    //动态添加购买的店铺及旗下商品
    private void initOrderList() {
        List<ShopOrderInfo> orders = shopOrderPreview.Orders;
        llOrderContainer.removeAllViews();
        for (int i = 0; i < orders.size(); i++) {//外层容器数据个数,动态添加数据
            View inflate = View.inflate(this, R.layout.act_selfsupport_makesure_order_item, null);
            LinearLayout smallContainer = (LinearLayout) inflate.findViewById(R.id.ll_goods_container);
            TextView tv_order_shop_name = (TextView) inflate.findViewById(R.id.tv_order_shop_name);
            TextView tv_goods_number = (TextView) inflate.findViewById(R.id.tv_goods_number);
            TextView tv_subtotal = (TextView) inflate.findViewById(R.id.tv_subtotal);
            TextView tv_freight = (TextView) inflate.findViewById(R.id.tv_freight);
            EditText et_message = (EditText) inflate.findViewById(R.id.et_message);

            orderExplainViews.put(orders.get(i).VenderId, et_message);//把每个输入框控件保存起来

            tv_order_shop_name.setText(orders.get(i).VenderName);
            tv_subtotal.setText(WWViewUtil.numberFormatPrice(orders.get(i).TotalAmt));
            tv_goods_number.setText(String.format(getString(R.string.total_number_goods), orders.get(i).Quantity));
            tv_freight.setText(String.format(getString(R.string.contain_post_fee), WWViewUtil.numberFormatPrice(orders.get(i).PostFee)));
            if (i == orders.size() - 1) {
                inflate.findViewById(R.id.v_divide).setVisibility(View.GONE);
            }
            final List<ShopOrderGoods> products = orders.get(i).Products;
            for (int j = 0; j < products.size(); j++) {//该商家下的子数据
                View goodLayout = View.inflate(this, R.layout.selfsupport_makesureorder_gooditem, null);
                ImageView iv_good_img = (ImageView) goodLayout.findViewById(R.id.iv_good_img);
                TextView tv_good_name = (TextView) goodLayout.findViewById(R.id.tv_good_name);
                TextView tv_sku = (TextView) goodLayout.findViewById(R.id.tv_0);
                TextView tv_good_price = (TextView) goodLayout.findViewById(R.id.tv_good_price);
                TextView tv_good_num = (TextView) goodLayout.findViewById(R.id.tv_good_num);//  *1
                final ShopOrderGoods shopOrderGoods = products.get(j);
                goodLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportGoodsDetailActivity(SelfSupportMakesureOrderActivity.this, shopOrderGoods.ProductId);
                    }
                });
                ImageUtils.setCommonImage(this, shopOrderGoods.ImageUrl, iv_good_img);
                WWViewUtil.textInsertDrawable(this, tv_good_name, shopOrderGoods.ProductName, false, shopOrderGoods.IsVipLevel);
                tv_sku.setText(shopOrderGoods.ProName);
                tv_good_price.setText(shopOrderGoods.IsVipLevel?"积分"+shopOrderGoods.SalePrice:WWViewUtil.numberFormatPrice(shopOrderGoods.SalePrice));
                tv_good_num.setText(String.format(getString(R.string.ride_number_of_cases), shopOrderGoods.Quantity));
                smallContainer.addView(goodLayout);//添加商品
            }
            llOrderContainer.addView(inflate);//添加店铺
        }
    }

    private void calculateSum(boolean isChecked, double deductionMoney) {
        if (isChecked) {
            totalMoney -= deductionMoney;
        } else {
            totalMoney += deductionMoney;
        }
        tvShouldPayTotalMoney.setText(WWViewUtil.numberFormatPrice(totalMoney));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_REQUEST_CODE) {
                if (data != null) {
                    shopOrderPreview.Address = JSON.parseObject(
                            data.getStringExtra(Consts.KEY_DATA), Address.class);
                    tvReceiverName.setText(shopOrderPreview.Address.Consignee);
                    tvReceiverAddress.setText(shopOrderPreview.Address.AddressPre + shopOrderPreview.Address.Address);
                    tvReceiverPhone.setText(shopOrderPreview.Address.Mobile);

                } else {
                    if (shopOrderPreview.Address == null) {
                        getDefaultUserAddress();
                    }
                }
            }

            if (requestCode == INTERNAL_PAY_REQUEST_CODE) {
                String psw = data.getStringExtra(Consts.KEY_DATA);
                orderSubmit(psw);
            }

            if (requestCode == NET_PAY_REQUEST_CODE) {
                closeSelfAndCart();
            }
        }
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
                shopOrderPreview.Address = JSON.parseObject(object.toJSONString(), Address.class);
                if (shopOrderPreview.Address != null) {
                    tvReceiverName.setText(shopOrderPreview.Address.Consignee);
                    tvReceiverAddress.setText(shopOrderPreview.Address.AddressPre + shopOrderPreview.Address.Address);
                    tvReceiverPhone.setText(shopOrderPreview.Address.Mobile);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                if (data.getIntValue("ErrCode") == -1) {
                    tvReceiverName.setText(R.string.no_address_tips);
                    shopOrderPreview.Address = null;
                    tvReceiverAddress.setText("");
                    tvReceiverPhone.setText("");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

