package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.BankListAdapter;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Bank;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

/***
 * Description:提现 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-26
 ***/
public class WithdrawActivity extends FatherActivity implements OnClickListener {
    @BindView(R.id.tv_withdraw_yue)
    TextView tvWithdrawYue;
    private TextView tv_yue, tv_card, tv_other_card, tv_save, tv_shouxufei, tv_yue_title;
    private EditText et_num;
    private LinearLayout ll_select_card;
    private Bank bank;
    private double num;
    private int model;
    //个人提现
    public static final int PERSON_WITHDRAW = 0;
    //分销提现
    public static final int DISTRIBUTION_WITHDRAW = 1;
    private double withdraw_limit;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_other_card:
                PublicWay.startAddBankActivity(WithdrawActivity.this, 888,
                        AddBankActivity.ADD, null);
                break;
            case R.id.ll_select_card:
                seleteCardBank();
                break;
            case R.id.tv_save:
                String money = et_num.getText().toString();
                if (TextUtils.isEmpty(money)) {
                    WWToast.showShort(this, R.string.please_input_withdraw_money);
                    return;
                }
                double edu = Double.parseDouble(tvWithdrawYue.getText().toString().trim());
                if (edu < Double.parseDouble(money)) {
                    WWToast.showShort(this, R.string.rather_than_withdraw_edu);
                    return;
                }
                if (havePayPsw) {
                    commit();
                } else {
                    showSetpswDialog();
                }

                break;

            default:
                break;
        }
    }

    private void showSetpswDialog() {
        final CommonDialog commonDialog = DialogUtils.getCommonDialogTwiceConfirm(
                this, getString(R.string.unset_psw_do_it), true);
        commonDialog.setRightButtonCilck(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PublicWay.startVerifyPhoneNumberActivity(WithdrawActivity.this,
                        VerifyPhoneNumberActivity.PAY, SharedPreferenceUtils
                                .getInstance().getPhoneNum(), 0);
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 888 && arg1 == RESULT_OK) {
            getBankList();
        } else if (arg0 == 999 && arg1 == RESULT_OK) {
            showWithdrawSuccessDialog();
        }
    }

    /***
     * Description: 提现成功弹窗
     *
     * @author ZXJ
     * @date 2016-10-11
     ***/
    protected void showWithdrawSuccessDialog() {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialog(
                        WithdrawActivity.this,
                        String.format(getResources().getString(R.string.agency_withdraw_tips1),
                                model == DISTRIBUTION_WITHDRAW ? getString(R.string.fenxiao) : getString(R.string.account)));
        commonDialog.setTitleText(R.string.commit_ok);
        commonDialog.getContent().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        commonDialog.getButtonRight().setVisibility(View.GONE);
        commonDialog.getButtonLeft(R.string.ok1).setTextColor(getResources().getColor(R.color.yellow_ww));
        commonDialog.getButtonLeft().setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK);
                        finish();
                        commonDialog.dismiss();
                    }
                });
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
    }

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_withdraw;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.withdraw, true);
        Intent intent = getIntent();
        model = intent.getIntExtra(Consts.KEY_MODULE, PERSON_WITHDRAW);
        withdraw_limit = intent.getDoubleExtra(Consts.KEY_DATA, 0);
    }

    private static final int DECIMAL_DIGITS = 2;//小数的位数

    @Override
    protected void initView() {
        tv_yue = (TextView) findViewById(R.id.tv_yue);
        tv_yue_title = (TextView) findViewById(R.id.tv_yue_title);
        et_num = (EditText) findViewById(R.id.et_num);
        tv_card = (TextView) findViewById(R.id.tv_card);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_shouxufei = (TextView) findViewById(R.id.tv_shouxufei);
        tv_other_card = (TextView) findViewById(R.id.tv_other_card);
        ll_select_card = (LinearLayout) findViewById(R.id.ll_select_card);

        tv_save.setOnClickListener(this);
        ll_select_card.setOnClickListener(this);
        tv_other_card.setOnClickListener(this);
        WWViewUtil.viewUnderLine(tv_other_card);

        bankList = new ListView(this);
        bankList.setBackgroundResource(R.drawable.bg_yellow_white_shape);
        adapter = new BankListAdapter(this, BankListAdapter.WITHDRAW);
        bankList.setAdapter(adapter);
        bank = new Bank();
        et_num.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                WWViewUtil.limitEditMoneyRule(s, et_num, DECIMAL_DIGITS);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && (!s.toString().equals("."))) {
                    double d = Double.parseDouble(et_num.getText().toString());
                    tv_shouxufei.setText(WWViewUtil
                            .numberFormatWithTwo(d * (model == PERSON_WITHDRAW ? 0.1 : 0.01)));
                } else {
                    tv_shouxufei.setText("");
                }

            }
        });
        WWViewUtil.inputLimit(et_num, 999999999.99, 2);
        tvWithdrawYue.setText(WWViewUtil.numberFormatWithTwo(withdraw_limit));
        tv_yue_title.setText(model == PERSON_WITHDRAW ? R.string.agency_yue_money : R.string.goods_money_colon);
        tv_shouxufei.setHint(model == PERSON_WITHDRAW ? R.string.service_fee_10 : R.string.service_fee_1);
    }


    @Override
    protected void doOperate() {
        getBankList();
    }

    @Override
    protected void onResume() {
        getHavaPayPsw();
        getAllowCashAccount();
        super.onResume();
    }

    private void getAllowCashAccount() {
        showWaitDialog();
        x.http().get(ParamsUtils.getSessionParams(model == PERSON_WITHDRAW ? ApiUser.AllowCashAccount() : ApiDistribution.AllowCashAccount()),
                new WWXCallBack("AllowCashAccount") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        num = data.getDoubleValue("Data");
                        tv_yue.setText(WWViewUtil.numberFormatPrice(num));
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    private boolean havePayPsw;

    public void getHavaPayPsw() {
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("HavePaypsd") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                havePayPsw = data.getBooleanValue("Data");
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }


    private void getBankList() {
        RequestParams params = new RequestParams(ApiUser.getBanksGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("BanksGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                bankListdata = (ArrayList<Bank>) JSON.parseArray(
                        data.getString("Data"), Bank.class);
                if (bankListdata != null && bankListdata.size() > 0) {
                    bank = bankListdata.get(0);
                    tv_card.setText(String.format(getString(R.string.bank_last_six),
                            bank.BankName,
                            bank.AccountNo.length() < 6 ? bank.AccountNo : bank.AccountNo.substring(bank.AccountNo.length() - 5)));
                }
                adapter.setDatas(bankListdata);
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
    private BankListAdapter adapter;
    private ListView bankList;

    private void seleteCardBank() {

        final PopupWindow popupWindow = new PopupWindow(bankList,
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WWViewUtil.setPopInSDK7(popupWindow, tv_card);
//        popupWindow.showAsDropDown(tv_card);
        bankList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                bank = bankListdata.get(position);
                tv_card.setText(String.format(getString(R.string.bank_last_six),
                        bank.BankName,
                        bank.AccountNo.length() < 6 ? bank.AccountNo : bank.AccountNo.substring(bank.AccountNo.length() - 5)));
                popupWindow.dismiss();
            }
        });
    }

    public static final String WITHDRAW_COUNT = "withdraw_count";

    private void commit() {
        // et_name, bank_number, et_open_bank bank
        String etnum = et_num.getText().toString().trim();
        String bankname = tv_card.getText().toString().trim();
        if (TextUtils.isEmpty(etnum)) {
            WWToast.showShort(R.string.input_can_not_null);
            return;
        } else if (TextUtils.isEmpty(bankname)) {
            WWToast.showShort(R.string.choice_bank_card);
            return;
        } else if (Double.parseDouble(etnum) < 10) {
            WWToast.showShort(R.string.withdraw_than_10);
            return;
        } else if (Double.parseDouble(etnum) > num) {
            WWToast.showShort(R.string.remain_money_shortage);
            return;
        }
        PublicWay.startVerifyPasswordActivity(this, model == PERSON_WITHDRAW ?
                        VerifyPasswordActivity.MODE_WITH_DRAW : VerifyPasswordActivity.MODE_WITH_DRAW_DISTRIBUTION, etnum, bank.FlowId,
                null, 0, 999);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
