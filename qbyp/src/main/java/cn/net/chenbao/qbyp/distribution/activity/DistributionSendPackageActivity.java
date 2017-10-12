package cn.net.chenbao.qbyp.distribution.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionDeliversAdapter;
import cn.net.chenbao.qbyp.distribution.been.ShopDeliver;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 发货
 */

public class DistributionSendPackageActivity extends FatherActivity {
    @BindView(R.id.tv_logistics_company)
    TextView tvLogisticsCompany;
    @BindView(R.id.ll_logistics_company)
    LinearLayout llLogisticsCompany;
    @BindView(R.id.ll_logistics_num)
    EditText llLogisticsNum;
    @BindView(R.id.cb_no_name_comm)
    CheckBox cbNoNameComm;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.tv_zengsongjifen)
    TextView tv_zengsongjifen;
    private long orderId;
    private double jifen;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_sendpackage;
    }

    @Override
    protected void initValues() {
        orderId = getIntent().getLongExtra(Consts.KEY_DATA, 0);
        jifen = getIntent().getDoubleExtra(Consts.KEY_MONEYCOUNT, 0);
        initDefautHead(R.string.send, true);
    }

    @Override
    protected void initView() {
        cbNoNameComm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                llLogisticsCompany.setClickable(!isChecked);
                llLogisticsNum.setEnabled(!isChecked);
                llLogisticsCompany.setBackgroundResource(isChecked ? R.color.edit_bg_gray : R.color.white);
                llLogisticsNum.setBackgroundResource(isChecked ? R.color.edit_bg_gray : R.color.white);
            }
        });
        tv_zengsongjifen.setText("发货成功后，将赠送" + WWViewUtil.numberFormatWithTwo(jifen) + "未解冻积分给买家");
    }

    @Override
    protected void doOperate() {
        getDeliver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_logistics_company, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_logistics_company:
                if (popupWindow == null) {
                    getDeliver();
                } else {
                    popupWindow.setWidth(view.getWidth());
                    if (!popupWindow.isShowing()) {
                        WWViewUtil.setPopInSDK7(popupWindow, view);
                    }
                }


                break;
            case R.id.tv_send:
                JSONObject jsonObject = new JSONObject();

                jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                        .getInstance().getSessionId());
                jsonObject.put("orderId", orderId);

                //，0物流，1买家自提
                if (cbNoNameComm.isChecked()) {
                    jsonObject.put("sendMode", "1");
                } else {
                    jsonObject.put("sendMode", "0");
                    String logisticsNo = llLogisticsNum.getText().toString().trim();
                    if (shopDeliverSelect == null) {
                        WWToast.showShort(R.string.please_choice_wuliu_company);
                        return;
                    } else {
                        jsonObject.put("deliverNo", shopDeliverSelect.DeliverNo);
                        if (TextUtils.isEmpty(logisticsNo)) {
                            WWToast.showShort(R.string.wuliu_No_not_empty);
                            return;
                        }
                        if (RegexUtil.checkNameChese(logisticsNo)) {
                            WWToast.showShort(R.string.wuliu_No_not_input_Ch);
                            return;
                        }
                        jsonObject.put("logisticsNo", logisticsNo);
                    }

                }


                showWaitDialog();
                x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiDistribution.OrderSend()), new WWXCallBack("OrderSend") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (data.getBoolean("Data")) {
                            WWToast.showShort(R.string.set_succuss);
                            setResult(RESULT_OK);
                            finish();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });


                break;
        }
    }

    /**
     * 获得行业列表
     */
    List<ShopDeliver> parseArray;
    private DistributionDeliversAdapter tradesCategoryAdapter;
    private ShopDeliver shopDeliverSelect = null;

    private void getDeliver() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.Delivers()),
                new WWXCallBack("Delivers") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        parseArray = JSON.parseArray(jsonArray.toJSONString(),
                                ShopDeliver.class);
                        ListView inflate = new ListView(DistributionSendPackageActivity.this);
                        inflate.setBackgroundResource(R.color.white);
                        tradesCategoryAdapter = new DistributionDeliversAdapter(
                                DistributionSendPackageActivity.this);
                        tradesCategoryAdapter.setDatas(parseArray);
                        inflate.setAdapter(tradesCategoryAdapter);
                        inflate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {
                                shopDeliverSelect = parseArray.get(position);
                                tvLogisticsCompany.setText(shopDeliverSelect.DeliverName);
                                popupWindow.dismiss();
                            }
                        });
                        popupWindow = new PopupWindow(inflate);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        if (parseArray.size() > 0) {
                            shopDeliverSelect = parseArray.get(0);
                            tvLogisticsCompany.setText(shopDeliverSelect.DeliverName);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }

                });

    }
}
