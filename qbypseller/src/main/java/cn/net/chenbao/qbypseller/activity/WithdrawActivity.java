package cn.net.chenbao.qbypseller.activity;

import java.util.ArrayList;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.BankListAdapter;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.api.ApiUser;
import cn.net.chenbao.qbypseller.bean.Bank;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
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

/***
 * Description:提现 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-26
 ***/
public class WithdrawActivity extends FatherActivity implements OnClickListener {
    private TextView tv_yue, tv_card, tv_other_card, tv_save, koulv;
    private EditText et_num;
    private LinearLayout ll_select_card;
    private Bank bank;
    private double num;

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
                // 去设置支付密码
                PublicWay
                        .startfindLoginPasswordAcitivity(
                                WithdrawActivity.this,
                                FindlLoginPasswordActivity.MODE_PAY_PSW,
                                "");
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
        final CommonDialog commonDialog = DialogUtils.getCommonDialog(
                WithdrawActivity.this, R.string.withdraw_tips1);
        commonDialog.setTitleText(R.string.commit_ok);
        commonDialog.getContent().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        commonDialog.getButtonRight().setVisibility(View.GONE);
        commonDialog.getButtonLeft(R.string.ok1).setTextColor(
                getResources().getColor(R.color.yellow_ww));
        commonDialog.getButtonLeft().setOnClickListener(new OnClickListener() {

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
    }

    private static final int DECIMAL_DIGITS = 2;//小数的位数

    @Override
    protected void initView() {
        tv_yue = (TextView) findViewById(R.id.tv_yue);
        koulv = (TextView) findViewById(R.id.koulv);
        et_num = (EditText) findViewById(R.id.et_num);
        tv_card = (TextView) findViewById(R.id.tv_card);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_other_card = (TextView) findViewById(R.id.tv_other_card);
        ll_select_card = (LinearLayout) findViewById(R.id.ll_select_card);
        tv_save.setOnClickListener(this);
        ll_select_card.setOnClickListener(this);
        tv_other_card.setOnClickListener(this);
        bankList = new ListView(this);
        bankList.setBackgroundResource(R.drawable.bg_yellow_white_shape);
        adapter = new BankListAdapter(this, BankListAdapter.WITHDRAW);
        bankList.setAdapter(adapter);
        bank = new Bank();
//        et_num.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > DECIMAL_DIGITS) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + DECIMAL_DIGITS + 1);
//                        et_num.setText(s);
//                        et_num.setSelection(s.length());
//                    }
//                }
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    et_num.setText(s);
//                    et_num.setSelection(2);
//                }
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        et_num.setText(s.subSequence(0, 1));
//                        et_num.setSelection(1);
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        WWViewUtil.inputLimit(et_num, 999999999.99, 2);

    }

    @Override
    protected void onResume() {
        getAllowCashAccount();
        getHavaPayPsw();
        super.onResume();
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

    private void getAllowCashAccount() {
        showWaitDialog();
        x.http().get(
                ParamsUtils.getSessionParams(ApiSeller.AllowCashAccount()),
                new WWXCallBack("AllowCashAccount") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        num = data.getDoubleValue("Data");
                        tv_yue.setText(WWViewUtil.numberFormatWithTwo(num));
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    @Override
    protected void doOperate() {
        getBankList();
    }

    private void getBankList() {
        RequestParams params = new RequestParams(ApiSeller.getBanksGet());
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
                    tv_card.setText(bank.BankName
                            + "(尾号"
                            + (bank.AccountNo.length() < 6 ? bank.AccountNo : bank.AccountNo.substring(bank.AccountNo.length() - 5))
                            + ")");
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
                tv_card.setText(bank.BankName + "(尾号"
                        + (bank.AccountNo.length() < 6 ? bank.AccountNo : bank.AccountNo.substring(bank.AccountNo.length() - 5))
                        + ")");
                popupWindow.dismiss();
            }
        });
    }


    private void commit() {
        // et_name, bank_number, et_open_bank bank
        String etnum = et_num.getText().toString().trim();
        String bankname = tv_card.getText().toString().trim();
        if (TextUtils.isEmpty(etnum)) {
            WWToast.showShort(R.string.input_can_not_null);
            return;
        } else if (TextUtils.isEmpty(bankname)) {
            WWToast.showShort(R.string.please_choice_bank_card);
            return;
        } else if (Double.parseDouble(etnum) < 10) {
            WWToast.showShort(R.string.withdraw_than_100);
            return;
        } else if (Double.parseDouble(etnum) > num) {
            WWToast.showShort(R.string.remain_money_not_enough);
            return;
        }

        Intent i = new Intent(this, VerifyPasswordActivity.class);
        i.putExtra(Consts.KEY_DATA, etnum);
        i.putExtra(Consts.KEY_MODE, VerifyPasswordActivity.WITHDRAW);
        i.putExtra("card_id", bank.FlowId);
        startActivityForResult(i, 999);

    }
}
