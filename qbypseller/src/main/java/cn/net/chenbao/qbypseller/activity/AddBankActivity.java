package cn.net.chenbao.qbypseller.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.BankListAdapter;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Bank;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.RegexUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:添加银行卡 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-26
 ***/
public class AddBankActivity extends FatherActivity implements OnClickListener {
    private int model;
    /**
     * 添加
     */
    public final static int ADD = 0;
    /**
     * 修改
     */
    public final static int EDIT = 1;
    private Bank bank;

    private TextView tv_bankname, tv_save;
    private EditText et_name, bank_number, et_open_bank;
    private LinearLayout ll_select_bank;
    private RadioGroup bank_type_select;
    private RadioButton chuxuka, xinyongka;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_select_bank:
                seleteCardBank();
                break;
            case R.id.tv_save:
                commit();
                break;

            default:
                break;
        }

    }

    private void commit() {
        // et_name, bank_number, et_open_bank bank
        String username = et_name.getText().toString().trim();
        String bankNumber = bank_number.getText().toString().trim();
        String openBank = et_open_bank.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            WWToast.showShort(R.string.account_name_not_empty);
            return;
        }
        if(RegexUtil.checkHasSpecial(username)){
            WWToast.showShort(R.string.account_name_cannot_contain_special_symbol);
            return;
        }if(RegexUtil.checkHasNum(username)){
            WWToast.showShort(R.string.account_name_cannot_contain_number);
            return;
        }
        if (TextUtils.isEmpty(tv_bankname.getText().toString().trim())) {
            WWToast.showShort(R.string.please_choice_bank);
            return;
        }
        if (TextUtils.isEmpty(bankNumber)) {
            WWToast.showShort(R.string.bank_card_not_null);
            return;
        }
        if (TextUtils.isEmpty(openBank)) {
            WWToast.showShort(R.string.bank_name_not_null);
            return;
        }

        if (!RegexUtil.getBankCardCheckCode(bankNumber)) {
            WWToast.showShort(R.string.bank_card_input_error);
            return;
        }
        if (model == ADD) {
            bank.FlowId = 0;
        } else {
            bank.FlowId = 1;
        }
        bank.AccountName = username;
        bank.AccountNo = bankNumber;
        bank.BankAddress = openBank;
        RadioButton button = (RadioButton) findViewById(bank_type_select
                .getCheckedRadioButtonId());
        if (button.getText().toString().trim().equals("公司账户")) {
            bank.CardType = 0;
        } else {
            bank.CardType = 1;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("data", bank.toJson());
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiSeller.getBankSubmit()),
                new WWXCallBack("BankSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWToast.showShort(R.string.set_succuss);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        if (data.getIntValue("ErrCode") == 104) {
                            final CommonDialog commonDialog = DialogUtils
                                    .getCommonDialog(AddBankActivity.this,
                                            R.string.no_auth_cannot_add_bankcard);
                            commonDialog.getButtonRight(R.string.goto_auth);
                            commonDialog.getButtonLeft(R.string.temp_no_deal);
                            commonDialog.getButtonLeft().setOnClickListener(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.getButtonRight().setOnClickListener(
                                    new OnClickListener() {

                                        @Override
                                        public void onClick(View v) {
                                            PublicWay
                                                    .startCommitInfoActivity(
                                                            AddBankActivity.this,
                                                            WWApplication
                                                                    .getInstance()
                                                                    .getSessionId());
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.show();
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
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_add_bankcard;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, ADD);
        if (model == ADD) {
            initDefautHead(R.string.add_bank, true);
            bank = new Bank();
        } else {
            initDefautHead(R.string.edit_bank, true);
            bank = JSON.parseObject(
                    getIntent().getStringExtra(Consts.KEY_DATA), Bank.class);
        }

    }

    @Override
    protected void initView() {
        ll_select_bank = (LinearLayout) findViewById(R.id.ll_select_bank);
        tv_bankname = (TextView) findViewById(R.id.tv_bankname);
        bank_number = (EditText) findViewById(R.id.bank_number);
        et_name = (EditText) findViewById(R.id.et_name);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_open_bank = (EditText) findViewById(R.id.et_open_bank);
        bank_type_select = (RadioGroup) findViewById(R.id.bank_type_select);
        chuxuka = (RadioButton) findViewById(R.id.chuxuka);
        xinyongka = (RadioButton) findViewById(R.id.xinyongka);

        if (model == EDIT) {
            tv_bankname.setText(bank.BankName);
            et_name.setText(bank.AccountName);
            et_open_bank.setText(bank.BankAddress);
            bank_number.setText(bank.AccountNo);
            if (bank.CardType == 0) {
                chuxuka.setSelected(true);
            } else {
                xinyongka.setSelected(true);
            }
        }

        tv_save.setOnClickListener(this);
        ll_select_bank.setOnClickListener(this);
    }

    @Override
    protected void doOperate() {
        getBankList();
    }

    private void getBankList() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.getBanksGet()),
                new WWXCallBack("BanksGet") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        bankListdata = (ArrayList<Bank>) JSON.parseArray(
                                data.getString("Data"), Bank.class);
                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });

    }

    /**
     * 选择发卡银行
     */
    private ArrayList<Bank> bankListdata;

    private void seleteCardBank() {
        ListView bankList = new ListView(this);
        bankList.setBackgroundResource(R.drawable.bg_yellow_white_shape);
        BankListAdapter adapter = new BankListAdapter(this);
        int width = 80;
        bankList.setAdapter(adapter);
        adapter.setDatas(bankListdata);
        final PopupWindow popupWindow = new PopupWindow(bankList,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WWViewUtil.setPopInSDK7(popupWindow,tv_bankname);
//        popupWindow.showAsDropDown(tv_bankname);
        bankList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                bank.BankName = bankListdata.get(position).BankName;
                bank.BankIco = bankListdata.get(position).BankIco;
                bank.BankNo = bankListdata.get(position).BankNo;
                tv_bankname.setText(bank.BankName);
                popupWindow.dismiss();
            }
        });
    }
}
