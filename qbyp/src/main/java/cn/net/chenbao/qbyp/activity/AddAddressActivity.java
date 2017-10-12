package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.bean.Location;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

/***
 * Description:新加地址 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-26
 ***/
public class AddAddressActivity extends FatherActivity implements
        OnClickListener {
    public static final int REQUEST_CODE = 888;
    private EditText et_address_des, et_phone, et_name;
    private TextView tv_address, tv_save;
    private ImageView iv_man, iv_woman;
    private LinearLayout ll_man, ll_woman, ll_locate;
    private boolean isMan = true;
    private int model;
    /**
     * 添加
     */
    public final static int ADD = 0;
    /**
     * 修改
     */
    public final static int EDIT = 1;
    private Address addressInfo;
    // 定位信息
    private Location location;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                submit();
                break;
            case R.id.ll_locate:
                PublicWay.startRegionPickActivity(AddAddressActivity.this, REQUEST_CODE, RegionPickActivity.CONTAIN_ONLY_ONE);
                break;
            case R.id.ll_man:
                if (!isMan) {
                    isMan = !isMan;
                    iv_man.setImageResource(R.drawable.shangpinzhuangtai_select);
                    iv_woman.setImageResource(R.drawable.shangpinzhuangtai_nor);
                }
                break;
            case R.id.ll_woman:
                if (isMan) {
                    isMan = !isMan;
                    iv_man.setImageResource(R.drawable.shangpinzhuangtai_nor);
                    iv_woman.setImageResource(R.drawable.shangpinzhuangtai_select);
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == REQUEST_CODE) {
            if (arg2 != null) {

                addressInfo.Province = arg2.getStringExtra("province_id");
                addressInfo.City = arg2.getStringExtra("city_id");
                addressInfo.County =(TextUtils.isEmpty(arg2.getStringExtra("county_id"))?"0":arg2.getStringExtra("county_id"));
                StringBuffer sbf = new StringBuffer();
                sbf.append(arg2.getStringExtra("province_name") + " ");
                sbf.append(arg2.getStringExtra("city_name") + " ");
                sbf.append(arg2.getStringExtra("county_name"));
                tv_address.setText(sbf.toString());

            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_add_address;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, ADD);
        if (model == ADD) {
            initDefautHead(R.string.add_address, true);
            addressInfo = new Address();
        } else {
            initDefautHead(R.string.edit_address, true);
            addressInfo = JSON.parseObject(
                    getIntent().getStringExtra(Consts.KEY_DATA), Address.class);
        }

    }

    @Override
    protected void initView() {
        et_address_des = (EditText) findViewById(R.id.et_address_des);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_save = (TextView) findViewById(R.id.tv_save);
        iv_woman = (ImageView) findViewById(R.id.iv_woman);
        iv_man = (ImageView) findViewById(R.id.iv_man);
        ll_man = (LinearLayout) findViewById(R.id.ll_man);
        ll_woman = (LinearLayout) findViewById(R.id.ll_woman);
        ll_locate = (LinearLayout) findViewById(R.id.ll_locate);

        if (model == EDIT) {
            et_name.setText(addressInfo.Consignee);
            et_address_des.setText(addressInfo.Address);
            et_phone.setText(addressInfo.Mobile);
            tv_address.setText(addressInfo.AddressPre);
            if (addressInfo.Sex.equals("男")) {
                addressInfo.Sex = "男";
            } else {
                addressInfo.Sex = "女";
            }

        }

        tv_save.setOnClickListener(this);
        ll_woman.setOnClickListener(this);
        ll_man.setOnClickListener(this);
        ll_locate.setOnClickListener(this);
    }

    @Override
    protected void doOperate() {

    }

    private void submit() {
        String Consignee = et_name.getText().toString().trim();
        String Mobile = et_phone.getText().toString().trim();
        String address_des = et_address_des.getText().toString().trim();
        String address = tv_address.getText().toString().trim();
        // 地址的一些验证
        if (TextUtils.isEmpty(Consignee)) {
            WWToast.showShort(R.string.please_edit_consignee_name);
            return;
        } else if (TextUtils.isEmpty(Mobile)) {
            WWToast.showShort(R.string.please_edit_consignee_phone);
            return;
        } else if (TextUtils.isEmpty(address)) {
            WWToast.showShort(R.string.please_choice_address);
            return;
        } else if (TextUtils.isEmpty(address_des)) {
            WWToast.showShort(R.string.please_input_detail_address);
            return;
        }
        if(RegexUtil.checkHasSpecial(Consignee)){
            WWToast.showShort(R.string.receiver_name_cannot_contain_special_symbol);
            return;
        }
      if(RegexUtil.checkHasNum(Consignee)){
            WWToast.showShort(R.string.receiver_name_cannot_contain_number);
            return;
        }
        if (Mobile.length() != 11) {
            WWToast.showShort(R.string.phone_input_error);
            return;
        }

        if (model == ADD) {
            if (TextUtils.isEmpty(address)) {
                WWToast.showShort(R.string.please_choice_address);
                return;
            }
            addressInfo.FlowId = 0;
        } else {

            addressInfo.FlowId = 1;
        }

        addressInfo.Consignee = Consignee;
        addressInfo.Mobile = Mobile;
        addressInfo.Address = address_des;
        addressInfo.AddressPre = address;
        if (isMan)

        {
            addressInfo.Sex = "0";
        } else

        {
            addressInfo.Sex = "1";
        }

        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().

                getSessionId());
        // jsonObject.put("data", JSON.toJSONString(addressInfo));
        jsonObject.put("data", addressInfo.toJson());
        ZLog.showPost(jsonObject.toJSONString());
        x.http().

                post(
                        ParamsUtils.getPostJsonParams(jsonObject,
                                ApiUser.getAddressSubmit()),
                        new

                                WWXCallBack("AddressSubmit") {

                                    @Override
                                    public void onAfterSuccessOk(JSONObject data) {
                                        WWToast.showShort(R.string.set_succuss);
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public void onAfterSuccessError(JSONObject data) {

                                    }

                                    @Override
                                    public void onAfterFinished() {
                                        // TODO Auto-generated method stub
                                        dismissWaitDialog();

                                    }
                                });
    }

}
