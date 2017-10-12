package cn.net.chenbao.qbypseller.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.SellerInfo;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 配送设置
 *
 * @author xl
 * @date 2016-7-30 下午12:45:09
 * @description
 */
public class ShipSettingActivity extends FatherActivity implements
        OnClickListener {

    private EditText mEdt_ship_fee;
    private EditText mEdt_ship_free;
    private EditText mEdt_packet_fee;
    private EditText mEdt_packet_free;

    private SellerInfo mSellerInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.act_ship_setting;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.ship_setting, true);
        mSellerInfo = SharedPreferenceUtils.getInstance().getSellerInfo();

    }

    /**
     * 配送
     */
    View ll_deliver;
    /**
     * 到店
     */
    View ll_at_store;

    @Override
    protected void initView() {
        mEdt_ship_fee = (EditText) findViewById(R.id.edt_ship_fee);
        mEdt_ship_free = (EditText) findViewById(R.id.edt_ship_free);
        mEdt_packet_fee = (EditText) findViewById(R.id.edt_packaging_fee);
        mEdt_packet_free = (EditText) findViewById(R.id.edt_packaging_free);
        ll_deliver = findViewById(R.id.ll_delivery_select);
        ll_at_store = findViewById(R.id.ll_at_store_select);
        findViewById(R.id.tv_save).setOnClickListener(this);

        ll_deliver.setOnClickListener(this);
        ll_at_store.setOnClickListener(this);
        setDataToUi();

        inputLimit(mEdt_ship_fee);
        inputLimit(mEdt_ship_free);
        inputLimit(mEdt_packet_fee);
        inputLimit(mEdt_packet_free);
    }

    private void inputLimit(final EditText view) {
        view.addTextChangedListener(new TextWatcher() {
            boolean isFirst = true;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFirst) {
                    isFirst = false;
                    view.setText("");
                }
            }
        });
        view.setFilters(new InputFilter[]{new InputFilter() {
            Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

            //输入的最大金额
            private static final double MAX_VALUE = 999.99;
            //小数点后的位数
            private static final int POINTER_LENGTH = 2;

            private static final String POINTER = ".";

            private static final String ZERO = "0";

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String sourceText = source.toString();
                String destText = dest.toString();
                //验证删除等按键
                if (TextUtils.isEmpty(sourceText)) {
                    return "";
                }

                Matcher matcher = mPattern.matcher(source);
                //已经输入小数点的情况下，只能输入数字
                if (destText.contains(POINTER)) {
                    if (!matcher.matches()) {
                        return "";
                    } else {
                        if (POINTER.equals(source)) {  //只能输入一个小数点
                            return "";
                        }
                    }
                    //验证小数点精度，保证小数点后只能输入两位
                    int index = destText.indexOf(POINTER);
                    int length = destText.length() - index;
                    //如果长度大于2，并且新插入字符在小数点之后
                    if (length > POINTER_LENGTH && index < dstart) {
                        //超出2位返回null
                        return "";
                    }
                } else {
                    //没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点
                    if (!matcher.matches()) {
                        return "";
                    } else {
                        if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
                            return "";
                        }
                    }
                    if (view.getText().toString().startsWith(ZERO) && !sourceText.equals(POINTER)) {
                        return "";
                    }
                }
                //验证输入金额的大小
                double sumText = Double.parseDouble(destText + sourceText);
                if (sumText > MAX_VALUE) {
                    return dest.subSequence(dstart, dend);
                }
                return dest.subSequence(dstart, dend) + sourceText;
            }
        }});
    }

    @Override
    protected void doOperate() {

    }

    private void requestUpdate() {

        final String ship_fee = mEdt_ship_fee.getText().toString().trim();
        final String ship_free = mEdt_ship_free.getText().toString().trim();
        final String packet_fee = mEdt_packet_fee.getText().toString().trim();
        final String packet_free = mEdt_packet_free.getText().toString().trim();

        final boolean deliver = ll_deliver.isSelected();
        final boolean at_store = ll_at_store.isSelected();
        JSONObject object = new JSONObject();
        object.put("supSince", at_store);// 到店
        object.put("supDelivery", deliver);// 配送
        object.put("deliverFee", TextUtils.isEmpty(ship_fee) ? 0 : ship_fee);
        object.put("fullDelivery", TextUtils.isEmpty(ship_free) ? 0 : ship_free);
        object.put("packageFee", TextUtils.isEmpty(packet_fee) ? 0 : packet_fee);
        object.put("fullPackage", TextUtils.isEmpty(packet_free) ? 0
                : packet_free);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParamsWithSession(object,
                        ApiSeller.infoSendUpdate()),
                new WWXCallBack("InfoSendUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        mSellerInfo.DeliverFee = TextUtils.isEmpty(ship_fee) ? "0"
                                : ship_fee;
                        mSellerInfo.FullDelivery = TextUtils.isEmpty(ship_free) ? "0"
                                : ship_free;
                        mSellerInfo.PackageFee = TextUtils.isEmpty(packet_fee) ? "0"
                                : packet_fee;
                        mSellerInfo.FullPackage = TextUtils
                                .isEmpty(packet_free) ? "0" : packet_free;
                        mSellerInfo.SupportDeliver = deliver;
                        mSellerInfo.SupportSince = at_store;
                        setDataToUi();
                        SharedPreferenceUtils.getInstance().saveSellerInfo(
                                mSellerInfo);
                        WWToast.showCostomSuccess(R.string.set_success);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    private void setDataToUi() {
        if (mSellerInfo != null) {
            mEdt_ship_fee.setText(mSellerInfo.DeliverFee + "");
            mEdt_ship_free.setText(mSellerInfo.FullDelivery + "");
            mEdt_packet_fee.setText(mSellerInfo.PackageFee + "");
            mEdt_packet_free.setText(mSellerInfo.FullPackage + "");
            ll_deliver.setSelected(mSellerInfo.SupportDeliver);
            ll_at_store.setSelected(mSellerInfo.SupportSince);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                requestUpdate();
                break;
            case R.id.ll_delivery_select:
                if (!ll_at_store.isSelected() && ll_deliver.isSelected()) {

                } else {
                    v.setSelected(!v.isSelected());
                }

                break;
            case R.id.ll_at_store_select:

                if (!ll_deliver.isSelected() && ll_at_store.isSelected()) {

                } else {
                    v.setSelected(!v.isSelected());
                }

                break;

            default:
                break;
        }

    }
}
