package cn.net.chenbao.qbyp.fragment;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.activity.AgencyWithdrawActivity;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

/***
 * Description:区域代理信息Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyInfoFragment extends FatherFragment {

    private TextView tv_join_time;
    private TextView tv_total_money;
    private TextView tv_pre_pay;
    private TextView tv_already_add_fee;
    private TextView tv_serial_num;
    private TextView tv_area_name;
    private int agentId;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agency_detail_1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        agentId = getArguments().getInt(Consts.AGENT_ID);
        tv_join_time = (TextView) mGroup.findViewById(R.id.tv_join_time);
        tv_total_money = (TextView) mGroup.findViewById(R.id.tv_total_money);
        tv_pre_pay = (TextView) mGroup.findViewById(R.id.tv_pre_pay);
        tv_already_add_fee = (TextView) mGroup.findViewById(R.id.tv_already_add_fee);
        tv_serial_num = (TextView) mGroup.findViewById(R.id.tv_serial_num);
        tv_area_name = (TextView) mGroup.findViewById(R.id.tv_area_name);
        mGroup.findViewById(R.id.ll_already_add_fee_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublicWay.stratAgencyWithdrawActivity(AgencyInfoFragment.this, AgencyWithdrawActivity.REPAYMENT, agentId);
            }
        });
        initValue();
    }

    private void initValue() {
        showWaitDialog();
        RequestParams params = ParamsUtils.getSessionParams(ApiAgency.getAgentinfo());
        params.addBodyParameter("agentId", agentId + "");
        x.http().get(params,
                new WWXCallBack("AgentInfo") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        AgencyInfo agencyInfo = JSONObject.parseObject(
                                data.getString("Data"), AgencyInfo.class);
                        tv_join_time.setText(TimeUtil
                                .getOnlyDateToS(agencyInfo.CreateTime * 1000));
                        tv_total_money.setText(WWViewUtil.numberFormatWithTwo(agencyInfo.AgentAmt));
                        tv_pre_pay.setText(WWViewUtil.numberFormatWithTwo(agencyInfo.AlreadyAmt));
                        tv_already_add_fee.setText(WWViewUtil.numberFormatWithTwo(agencyInfo.CashAmt));
                        tv_serial_num.setText(agencyInfo.AgentNo + "");
                        tv_area_name.setText(agencyInfo.AreaName);

                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }
}
