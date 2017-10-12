package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AddressListActivity;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.api.ApiVariable;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.dialog.WebviewDialog;
import cn.net.chenbao.qbyp.distribution.been.DistributionOrder;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ppsher on 2017/4/16.
 * Description: 确认订单
 */

public class DistributionMakeSureOrderActivity extends FatherActivity {
    public static final int FROM_DISTRIBUTION_GOOD_DETAIL = 1;
    public static final int FROM_DISTRIBUTION_SHOP_CART = 2;
    public static final int ADDRESS_REQUEST_CODE = 111;
    public static final int NET_PAY_REQUEST_CODE = 888;
    @BindView(R.id.tv_receiver_name)
    TextView tvReceiverName;
    @BindView(R.id.tv_receiver_phone)
    TextView tvReceiverPhone;
    @BindView(R.id.tv_receiver_address)
    TextView tvReceiverAddress;

    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_money_all)
    TextView tv_money_all;
    @BindView(R.id.tv_total_integral)
    TextView tvTotalIntegral;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.tv_input_limit_tip)
    TextView tvInputLimitTip;
    @BindView(R.id.tv_input_num)
    TextView tvInputNum;
    @BindView(R.id.tv_document)
    TextView tv_document;
    @BindView(R.id.tv_input_total)
    TextView tvInputTotal;
    @BindView(R.id.ll_container)
    LinearLayout ll_container;
    @BindView(R.id.ll_doucument)
    LinearLayout ll_doucument;
    @BindView(R.id.tv_no_address)
    TextView tvNoAddress;
    @BindView(R.id.tv_postage)
    TextView tv_postage;
    @BindView(R.id.tv_postage_weight)
    TextView tv_postage_weight;
    @BindView(R.id.iv_checkbox)
    ImageView iv_checkbox;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.ll_tip)
    LinearLayout llTip;
    private DistributionOrder distributionOrder;
    private int keyHeight;
    private boolean isAgreeProtocol;
    private long[] flowsIds;
    private int model;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_makesure_order;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.make_suer_order, true);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String previewJson = intent.getStringExtra(Consts.KEY_DATA);
        model = getIntent().getIntExtra(Consts.KEY_MODULE, FROM_DISTRIBUTION_GOOD_DETAIL);
        if (model == FROM_DISTRIBUTION_SHOP_CART) {
            flowsIds = intent.getLongArrayExtra("FlowsId");
        }


        distributionOrder = JSONObject.parseObject(previewJson, DistributionOrder.class);
        setUi();

        keyHeight = DensityUtil.getScreenHeight(this) / 3;
        sv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right,
                                       int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            sv.scrollTo(0, (int) llTip.getY());
                        }
                    });
                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

                }

            }
        });
    }


    private void setUi() {
        if (distributionOrder == null) {
            return;
        }
        //取地址
        if (distributionOrder.AddressId == 0) {
            tvNoAddress.setVisibility(View.VISIBLE);
        } else {
            tvNoAddress.setVisibility(View.GONE);
            tvReceiverName.setText(distributionOrder.Consignee);
            tvReceiverPhone.setText(distributionOrder.ReceiverMobile);
            tvReceiverAddress.setText(distributionOrder.Address);
        }

        WWViewUtil.setDistributionGoodsView(this, ll_container, distributionOrder.Goods, 0);
        tv_postage_weight.setText("(总重:" + WWViewUtil.numberFormatNoZero(distributionOrder.TotalWeight) + "kg)");
        tv_postage.setText("+" + WWViewUtil.numberFormatPrice(distributionOrder.PostFee));
        tvTotalMoney.setText(WWViewUtil.numberFormatPrice(distributionOrder.GoodsAmt));
        tv_money_all.setText("合计金额: " + WWViewUtil.numberFormatPrice(distributionOrder.TotalAmt));
        tvTotalIntegral.setText(WWViewUtil.numberFormatWithTwo(distributionOrder.TotalConsume));
        if (distributionOrder.UpLevelId > 0) {
            ll_doucument.setVisibility(View.VISIBLE);
            tv_document.setText("《" + distributionOrder.ProtcolName + "》");
            isAgreeProtocol = true;
        }
    }

    @Override
    protected void doOperate() {
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvInputNum.setText(editable.length() + "");
                if (editable.length() == 50) {
                    tvInputLimitTip.setVisibility(View.VISIBLE);
                    tvInputNum.setTextColor(getResources().getColor(R.color.red));
                    tvInputTotal.setTextColor(getResources().getColor(R.color.red));
                } else if (editable.length() < 50 && tvInputLimitTip.getVisibility() == View.VISIBLE) {
                    tvInputLimitTip.setVisibility(View.INVISIBLE);
                    tvInputNum.setTextColor(getResources().getColor(R.color.text_gray));
                    tvInputTotal.setTextColor(getResources().getColor(R.color.text_gray));
                }
            }
        });
    }

    private void OrderSubmit() {
        if (distributionOrder == null) {
            return;
        }
        if (distributionOrder.AddressId == 0) {
            WWToast.showShort(R.string.please_add_receive_address0);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        String message = etMessage.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            jsonObject.put("buyMemo", message);
        }
        jsonObject.put(Consts.KEY_SESSIONID, SharedPreferenceUtils.getInstance().getSessionId());
        jsonObject.put("addressId", distributionOrder.AddressId);//
        jsonObject.put("agreeProtocol", isAgreeProtocol);

        String url = "";
        String modeKey = "";
        if (model == FROM_DISTRIBUTION_GOOD_DETAIL) {
            url = ApiDistribution.OrderSubmit();
            modeKey = "OrderSubmit";
            jsonObject.put("productId", distributionOrder.Goods.get(0).ProductId);
            jsonObject.put("quantity", distributionOrder.Quantity);
        } else if (model == FROM_DISTRIBUTION_SHOP_CART) {
            url = ApiDistribution.OrderSubmitByCart();
            modeKey = "OrderSubmitByCart";
            jsonObject.put("flowIds", flowsIds);
        }

        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url), new WWXCallBack(modeKey) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                DistributionOrder distributionOrder = JSONObject.parseObject(data.getString("Data"), DistributionOrder.class);
                PublicWay.startPayOrderActivity(DistributionMakeSureOrderActivity.this, distributionOrder.TotalAmt, distributionOrder.OrderId, NET_PAY_REQUEST_CODE, PayOrdersActivity.DISTRIBUTION);
                finish();
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
        if (resultCode == RESULT_OK) {
            if (requestCode == ADDRESS_REQUEST_CODE) {//从地址管理跳回来
                if (data != null) {
                    tvNoAddress.setVisibility(View.GONE);
                    Address address = JSON.parseObject(
                            data.getStringExtra(Consts.KEY_DATA), Address.class);
                    distributionOrder.AddressId = address.AddressId;

                    tvReceiverName.setText(address.Consignee);
                    tvReceiverAddress.setText(address.AddressPre + address.Address);
                    tvReceiverPhone.setText(address.Mobile);
                    getPostFee(distributionOrder.AddressId,distributionOrder.TotalWeight);
                } else {
                    getDefaultUserAddress();
                }
            }
        }
    }

    /**
     * 获取会员默认地址信息
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
                Address address = JSON.parseObject(object.toJSONString(), Address.class);
                if (address != null) {
                    tvNoAddress.setVisibility(View.GONE);
                    distributionOrder.AddressId = address.AddressId;
                    tvReceiverName.setText(address.Consignee);
                    tvReceiverAddress.setText(address.AddressPre + address.Address);
                    tvReceiverPhone.setText(address.Mobile);
                    getPostFee(distributionOrder.AddressId,distributionOrder.TotalWeight);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                if (data.getIntValue("ErrCode") == -1) {
                    tvNoAddress.setVisibility(View.VISIBLE);
                    distributionOrder.AddressId = 0;
                    tvReceiverName.setText("");
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

    /**
     * 获取邮费
     *
     * @param addressId
     */
    private void getPostFee(long addressId, double totalWeight) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("addressId", addressId);
        jsonObject.put("totalWeight", totalWeight);
        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiDistribution.ComputePostFee()), new WWXCallBack("ComputePostFee") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                distributionOrder.PostFee=data.getDoubleValue("Data");
                tv_postage.setText("+" + WWViewUtil.numberFormatPrice(distributionOrder.PostFee));
                tv_money_all.setText("合计金额: " + WWViewUtil.numberFormatPrice(distributionOrder.GoodsAmt + distributionOrder.PostFee));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    @OnClick({R.id.rl_address, R.id.tv_commit, R.id.iv_checkbox, R.id.tv_document})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_address:
                //地址选择
                PublicWay.startAddressListActivity(this, ADDRESS_REQUEST_CODE, AddressListActivity.SELECT);
                break;
            case R.id.tv_commit:
                if (!TimeUtil.isFastClick()) {
                    OrderSubmit();
                }
                break;
            case R.id.iv_checkbox:

                setCheck(!isAgreeProtocol);
                break;

            case R.id.tv_document:
                if (!TimeUtil.isFastClick()) {
                    if (dialog != null) {
                        dialog.show();
                    } else {
                        openProtocol();
                    }

                }
                break;
        }
    }

    public void setCheck(boolean ischeck) {
        this.isAgreeProtocol = ischeck;
        iv_checkbox.setImageResource(isAgreeProtocol ? R.drawable.shoppingcat_pitchon_icon : R.drawable.shoppingcat_unchecked_icon);
    }

    private WebviewDialog dialog;

    private void openProtocol() {
        RequestParams requestParams = new RequestParams(ApiVariable.FenxiaoAgentProcotol());
        requestParams.addBodyParameter("levelId", distributionOrder.UpLevelId + "");      //TODO  levelId
        x.http().get(requestParams, new WWXCallBack("FenxiaoAgentProcotol") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                dialog = new WebviewDialog(DistributionMakeSureOrderActivity.this, WebviewDialog.DATA, data.getString("Data"));
                dialog.show();

            }

            @Override
            public void onAfterFinished() {

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
