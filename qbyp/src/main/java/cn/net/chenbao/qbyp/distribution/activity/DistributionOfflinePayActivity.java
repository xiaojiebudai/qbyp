package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tencent.mm.sdk.modelpay.PayReq;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.PayResultActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiPay;
import cn.net.chenbao.qbyp.bean.WXPay;
import cn.net.chenbao.qbyp.distribution.been.DistributionCompanyAccounts;
import cn.net.chenbao.qbyp.distribution.fragment.ImageSelecWithUploadtFragment;
import cn.net.chenbao.qbyp.fragment.ImageSelectFragment;
import cn.net.chenbao.qbyp.utils.AlipayPayResult;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PayUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ppsher on 2017/4/16.
 * Description: 线下付款
 */

public class DistributionOfflinePayActivity extends FatherActivity {

    @BindView(R.id.ll_account_container)
    LinearLayout llAccountContainer;
    @BindView(R.id.tv_order_id_six)
    TextView tv_order_id_six;
    private ImageSelecWithUploadtFragment imageFragment;
    private long orderId;
    private String payId;


    private int model;
    private long[] orderIds;
    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_offline_pay;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.offline_pay, true);
        model = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
    }

    @Override
    protected void initView() {
        if(model== PayOrdersActivity.SELF){
            orderIds = getIntent().getLongArrayExtra("orderId");
            getInfo(orderIds);
        }else{
            orderId = getIntent().getLongExtra(Consts.KEY_DATA, 0);
            getInfo(orderId);
        }

   
    }

    private void getInfo(long[] orderIds) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("payCode", "Offline");
        jsonObject.put("orderIds", orderIds);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiPay.OrdersPay()), new WWXCallBack("OrdersPay") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                String json = data.getString("Data");
                JSONObject jsonObject = JSONObject.parseObject(json.substring(0, json.length()));
                payId = jsonObject.getString("PayId");
                String busId = jsonObject.getString("BusId");
                if (!TextUtils.isEmpty(busId)) {
                    tv_order_id_six.setText(busId.length() > 6 ? busId.substring(busId.length() - 7,busId.length()-1) : busId);
                }

                imageFragment = new ImageSelecWithUploadtFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ImageSelectFragment.MAX_NUMBER, 5);
                bundle.putInt(ImageSelectFragment.OUTMARGIN, 0);
                bundle.putString(ImageSelecWithUploadtFragment.KEY_EXISTING, jsonObject.getString("ImageUrls"));

                imageFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.image_fragment_container, imageFragment).commit();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private void getInfo(long orderId) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiPay.OrderPayGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("payCode", "Offline");
        x.http().get(params, new WWXCallBack("OrderPayGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                String json = data.getString("Data");
                JSONObject jsonObject = JSONObject.parseObject(json.substring(0, json.length()));
                payId = jsonObject.getString("PayId");
                String busId = jsonObject.getString("BusId");
                if (!TextUtils.isEmpty(busId)) {
                    tv_order_id_six.setText(busId.length() > 6 ? busId.substring(busId.length() - 6) : busId);
                }

                imageFragment = new ImageSelecWithUploadtFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(ImageSelectFragment.MAX_NUMBER, 5);
                bundle.putInt(ImageSelectFragment.OUTMARGIN, 0);
                bundle.putString(ImageSelecWithUploadtFragment.KEY_EXISTING, jsonObject.getString("ImageUrls"));

                imageFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.image_fragment_container, imageFragment).commit();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void doOperate() {
        getCompanyAccount();
    }

    private void getCompanyAccount() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.CompanyAccounts()), new WWXCallBack("CompanyAccounts") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<DistributionCompanyAccounts> accountses = JSONObject.parseArray(data.getString("Data"), DistributionCompanyAccounts.class);
                LayoutInflater inflater = LayoutInflater.from(DistributionOfflinePayActivity.this);
                for (int i = 0; i < accountses.size(); i++) {
                    DistributionCompanyAccounts account = accountses.get(i);
                    View view = inflater.inflate(R.layout.dynamic_account, null);
                    if (i == 0) {
                        view.findViewById(R.id.v_bg).setVisibility(View.GONE);
                    }
                    ((TextView) view.findViewById(R.id.tv_account_name)).setText(account.AccountName);
                    ((TextView) view.findViewById(R.id.tv_account_number)).setText(account.AccountNo);
                    ((TextView) view.findViewById(R.id.tv_account_belong_bank)).setText(account.BankName);
                    llAccountContainer.addView(view);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @OnClick(R.id.tv_commit)
    public void onClick() {
        offlinePaySubmit();
    }

    boolean isLoad;

    private void offlinePaySubmit() {
        if (TimeUtil.isFastClick(1500)) {
            return;
        }
        ArrayList<ImageSelecWithUploadtFragment.UpImgItem> selectImages = imageFragment.getSelectImages();
        ArrayList<String> list = new ArrayList<>();
        if (selectImages == null) {
            WWToast.showShort(R.string.please_choice_certificate_upload);
            return;
        }
        if (TextUtils.isEmpty(payId)) {
            return;
        }
        for (int i = 0; i < selectImages.size(); i++) {
            if (selectImages.get(i).isUp) {
                WWToast.showShort(R.string.certificate_no_upload);
                return;
            }
            list.add(selectImages.get(i).url);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, SharedPreferenceUtils.getInstance().getSessionId());
        jsonObject.put("payId", payId);
        jsonObject.put("imageUrls", JSONArray.toJSON(list));
        if (isLoad) {
            return;
        }
        isLoad = true;
        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ApiPay.OfflinePay()), new WWXCallBack("OfflinePay") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                isLoad = false;
                WWToast.showShort(R.string.commit_ok);
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onAfterFinished() {
                isLoad = false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageFragment.onActivityResult(requestCode, resultCode, data);
    }
}
