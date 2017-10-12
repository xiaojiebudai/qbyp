package cn.net.chenbao.qbypseller.activity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.RegexUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zxj on 2017/4/11.
 *
 * @description
 */

public class EditShopPhoneActivity extends FatherActivity {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_save)
    TextView tvSave;

    @Override
    protected int getLayoutId() {
        return R.layout.act_edit_shop_phone;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.edit_shop_phone, true);
    }

    @Override
    protected void initView() {
        etPhone.setText(getIntent().getStringExtra(Consts.KEY_DATA));
        etPhone.setSelection((!TextUtils.isEmpty(getIntent().getStringExtra(Consts.KEY_DATA)) ? etPhone.getText().length() : 0));
    }

    @Override
    protected void doOperate() {

    }


    @OnClick(R.id.tv_save)
    public void onViewClicked() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            WWToast.showShort(R.string.please_input_shop_phone);
            return;
        }
        if (!(RegexUtil.isMobile(phone) || RegexUtil.isGuHua(phone) || RegexUtil.isGUHUA400_800(phone))) {
            WWToast.showShort(R.string.shop_phone_error);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Api.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("tel", phone);
        RequestParams params = ParamsUtils.getPostJsonParams(jsonObject, ApiSeller.InfoTelUpdate());

        showWaitDialog();
        x.http().post(params, new WWXCallBack("InfoTelUpdate") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getBoolean("Data")) {
                    WWToast.showShort(R.string.updata_success);
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }
}
