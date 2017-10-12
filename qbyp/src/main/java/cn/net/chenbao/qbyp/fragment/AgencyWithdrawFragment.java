package cn.net.chenbao.qbyp.fragment;

import java.util.ArrayList;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AddBankActivity;
import cn.net.chenbao.qbyp.activity.AgencyWithdrawActivity;
import cn.net.chenbao.qbyp.activity.IdentityAuthenticationResultActivity;
import cn.net.chenbao.qbyp.activity.VerifyPasswordActivity;
import cn.net.chenbao.qbyp.activity.VerifyPhoneNumberActivity;
import cn.net.chenbao.qbyp.adapter.listview.AgencySelectAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BankListAdapter;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.bean.Bank;
import cn.net.chenbao.qbyp.bean.UserReal;
import cn.net.chenbao.qbyp.dialog.WithdrawTipsDialog;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

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

/***
 * Description:区域代理—体现 Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyWithdrawFragment extends FatherFragment implements
        OnClickListener {
    private LinearLayout ll_select_card, ll_area_select;
    private TextView tv_save, tv_card, tv_other_card, withdraw_tips,
            tv_canwithdraw, tv_area, tv_shouxufei, tv_kou;
    private WithdrawTipsDialog tipsDialog;
    private EditText et_num;
    /**
     * 选择发卡银行
     */
    private ArrayList<Bank> bankListdata;
    private BankListAdapter adapter;
    private ListView bankList;
    private Bank bank;
    /**
     * 选择区域
     */
    private ArrayList<AgencyInfo> agencyInfos;
    private AgencySelectAdapter agencySelectAdapter;
    private ListView agencyListView;
    private AgencyInfo agencyInfo;

    /**
     * @补扣金额
     */
    private double totalAmt;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agency_withdraw;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private static final int DECIMAL_DIGITS = 2;//小数的位数

    @Override
    protected void initView() {
        initTitle();
        tv_card = (TextView) mGroup.findViewById(R.id.tv_card);
        tv_save = (TextView) mGroup.findViewById(R.id.tv_save);
        withdraw_tips = (TextView) mGroup.findViewById(R.id.withdraw_tips);
        tv_other_card = (TextView) mGroup.findViewById(R.id.tv_other_card);
        tv_canwithdraw = (TextView) mGroup.findViewById(R.id.tv_canwithdraw);
        tv_shouxufei = (TextView) mGroup.findViewById(R.id.tv_shouxufei);
        tv_kou = (TextView) mGroup.findViewById(R.id.tv_kou);
        et_num = (EditText) mGroup.findViewById(R.id.et_num);
        tv_area = (TextView) mGroup.findViewById(R.id.tv_area);
        ll_select_card = (LinearLayout) mGroup
                .findViewById(R.id.ll_select_card);
        ll_area_select = (LinearLayout) mGroup
                .findViewById(R.id.ll_area_select);
        tv_save.setOnClickListener(this);
        ll_select_card.setOnClickListener(this);
        ll_area_select.setOnClickListener(this);
        tv_other_card.setOnClickListener(this);
        withdraw_tips.setOnClickListener(this);
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
                    double d = Double.parseDouble(s.toString());
                    tv_shouxufei.setText(WWViewUtil
                            .numberFormatWithTwo(d * 0.05));
                } else {
                    tv_shouxufei.setText("");
                }

            }
        });
        WWViewUtil.inputLimit(et_num, 999999999.99, 2);
        bankList = new ListView(getActivity());
        bankList.setBackgroundResource(R.color.gray_bg);
        bankList.setPadding(DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1));
        adapter = new BankListAdapter(getActivity(), BankListAdapter.WITHDRAW);
        bankList.setAdapter(adapter);

        agencyListView = new ListView(getActivity());
        agencyListView.setBackgroundResource(R.color.gray_bg);
        agencyListView.setPadding(DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1),
                DensityUtil.dip2px(getActivity(), 1));
        agencySelectAdapter = new AgencySelectAdapter(getActivity());
        agencyListView.setAdapter(agencySelectAdapter);

        bank = new Bank();
        agencyInfo = new AgencyInfo();
        getBankList();
        getAgencyList();
    }

    private void initTitle() {
        mGroup.findViewById(R.id.back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        mGroup.findViewById(R.id.tv_head_right).setOnClickListener(this);
        TextView center = (TextView) mGroup.findViewById(R.id.tv_head_center);
        center.setText(R.string.withdraw);
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

        TextView right = (TextView) mGroup.findViewById(R.id.tv_head_right);
        right.setText(R.string.detail);
        right.setTextColor(getResources().getColor(R.color.white));
        right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

    }

    @Override
    public void onResume() {
        getHavaPayPsw();
        realGet();
        super.onResume();
    }

    private UserReal userReal;

    private void realGet() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.RealGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("RealGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // TODO Auto-generated method stub
                // 0 待提交，1待审核，2审核通过，3审核不通过
                userReal = JSON.parseObject(data.getString("Data"),
                        UserReal.class);
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }

    private boolean havePayPsw;

    public void getHavaPayPsw() {
        RequestParams params = new RequestParams(ApiUser.HavePaypsd());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("HavePaypsd") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                havePayPsw = data.getBooleanValue("Data");
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                // 明细
                PublicWay.stratAgencyWithdrawActivity(AgencyWithdrawFragment.this,
                        AgencyWithdrawActivity.WITHDRAW, 0);
                break;
            case R.id.withdraw_tips:
                // 说明
                if (tipsDialog == null) {
                    tipsDialog = new WithdrawTipsDialog(getActivity(),
                            R.style.DialogStyle);
                }
                tipsDialog.show();
                break;
            case R.id.tv_other_card:

                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                getActivity(),
                                IdentityAuthenticationResultActivity.IDENTITYING,
                                "");
                    } else if (userReal.Status == 3) {
                        PublicWay
                                .startIdentityAuthenticationResultActivity(
                                        getActivity(),
                                        IdentityAuthenticationResultActivity.IDENTITYDEFAULT,
                                        userReal.Explain);
                    } else {
                        PublicWay.startAddBankActivity(AgencyWithdrawFragment.this,
                                888, AddBankActivity.ADD, null);
                    }
                } else {
                    PublicWay
                            .stratIdentityAuthenticationActivity(AgencyWithdrawFragment.this);
                }
                break;
            case R.id.ll_select_card:
                seleteCardBank();
                break;
            case R.id.ll_area_select:
                seleteAgency();
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

    /**
     * @param etnum
     * @param flowId
     * @param agentId
     */
    private void showBukouDialog(final double etnum, final long flowId, final int agentId) {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialogTwiceConfirm(getActivity(),
                        String.format(getString(R.string.withdraw_bukou), WWViewUtil.numberFormatWithTwo(getBukou(totalAmt, etnum))), true);
        commonDialog.setRightButtonCilck(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.startVerifyPasswordActivity(AgencyWithdrawFragment.this,
                        VerifyPasswordActivity.MODE_WITH_DRAW_AGENCY, etnum + "",
                        flowId, null, agentId, 999);
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    //计算实际补扣的金额
    private double getBukou(double totalAmt, double etnum) {
        return (totalAmt > etnum * 0.475) ? etnum * 0.475 : totalAmt;
    }

    private void showSetpswDialog() {
        final CommonDialog commonDialog = DialogUtils
                .getCommonDialogTwiceConfirm(getActivity(),
                        getString(R.string.unset_psw_do_it), true);
        commonDialog.setRightButtonCilck(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.startVerifyPhoneNumberActivity(
                        AgencyWithdrawFragment.this,
                        VerifyPhoneNumberActivity.PAY, SharedPreferenceUtils
                                .getInstance().getPhoneNum(), 0);
                commonDialog.dismiss();

            }
        });
        commonDialog.show();
    }

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
        } else if (Double.parseDouble(etnum) < 50) {
            WWToast.showShort(R.string.withdraw_than_50);
            return;
        } else if (Double.parseDouble(etnum) > Double
                .parseDouble(tv_canwithdraw.getText().toString())) {
            WWToast.showShort(R.string.remain_money_shortage);
            return;
        }
        if (totalAmt > 0) {
            showBukouDialog(Double.parseDouble(etnum), bank.FlowId, agencyInfo.AgentId);
        } else {
            PublicWay.startVerifyPasswordActivity(this,
                    VerifyPasswordActivity.MODE_WITH_DRAW_AGENCY, etnum,
                    bank.FlowId, null, agencyInfo.AgentId, 999);
        }

    }

    private void getBankList() {
        RequestParams params = new RequestParams(ApiUser.getBanksGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
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
                adapter.setSelectPostion(0);
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void seleteCardBank() {

        final PopupWindow popupWindow = new PopupWindow(bankList,
                ll_select_card.getWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WWViewUtil.setPopInSDK7(popupWindow, ll_select_card);
//        popupWindow.showAsDropDown(ll_select_card);
        bankList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                bank = bankListdata.get(position);
                tv_card.setText(bank.BankName + "(尾号"
                        + (bank.AccountNo.length() < 6 ? bank.AccountNo : bank.AccountNo.substring(bank.AccountNo.length() - 5))
                        + ")");
                adapter.setSelectPostion(position);
                popupWindow.dismiss();
            }
        });
    }

    private void getAgencyList() {
        RequestParams params = new RequestParams(ApiAgency.getMyAgents());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("MyAgents") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                agencyInfos = (ArrayList<AgencyInfo>) JSON.parseArray(
                        data.getString("Data"), AgencyInfo.class);
                if (agencyInfos != null && agencyInfos.size() > 0) {
                    agencyInfo = agencyInfos.get(0);
                    tv_area.setText(agencyInfo.AreaName);
                }
                agencySelectAdapter.setDatas(agencyInfos);
                agencySelectAdapter.setSelectPostion(0);
                getAllowCash();
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub

            }
        });
    }

    /***
     * Description: 获取可提金额
     *
     * @author ZXJ
     * @date 2016-10-13
     ***/
    protected void getAllowCash() {
        RequestParams params = ParamsUtils.getSessionParams(ApiAgency
                .AllowCashAccount());
        params.addBodyParameter("agentId", agencyInfo.AgentId + "");
        x.http().get(params, new WWXCallBack("AllowCashAccount") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                tv_canwithdraw.setText(data.getString("Data"));
                totalAmt = data.getDouble("TotalAmount");
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void seleteAgency() {
        final PopupWindow popupWindow = new PopupWindow(agencyListView,
                tv_area.getWidth(), LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        WWViewUtil.setPopInSDK7(popupWindow, tv_area);
//        popupWindow.showAsDropDown(tv_area);
        agencyListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                agencyInfo = agencyInfos.get(position);
                tv_area.setText(agencyInfo.AreaName);
                agencySelectAdapter.setSelectPostion(position);
                getAllowCash();
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 888 && arg1 == getActivity().RESULT_OK) {
            getBankList();
        } else if (arg0 == 999 && arg1 == getActivity().RESULT_OK) {
            getAllowCash();
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
                getActivity(), String.format(getResources().getString(R.string.agency_withdraw_tips1), "代理"));
        commonDialog.setTitleText(R.string.commit_ok);
        commonDialog.getContent().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        commonDialog.getButtonRight().setVisibility(View.GONE);
        commonDialog.getButtonLeft(R.string.ok1).setTextColor(
                getResources().getColor(R.color.yellow_ww));
        commonDialog.getButtonLeft().setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
    }
}
