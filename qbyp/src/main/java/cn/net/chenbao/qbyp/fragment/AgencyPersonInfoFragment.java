package cn.net.chenbao.qbyp.fragment;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

/***
 * Description:区域代理人信息Company: jsh Version：1.0
 * 
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyPersonInfoFragment extends FatherFragment implements
		OnClickListener {

	private TextView tv_legal_person_name;
	private TextView tv_agency_phone;
	private TextView tv_legal_certificate_no;
	private TextView tv_business_license_name;
	private TextView tv_business_license_no;
	private TextView tv_state;
	private int agentId;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_agency_detail_2;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		agentId = getArguments().getInt(Consts.AGENT_ID);
		tv_legal_person_name = (TextView) mGroup
				.findViewById(R.id.tv_legal_person_name);
		tv_agency_phone = (TextView) mGroup.findViewById(R.id.tv_agency_phone);
		tv_legal_certificate_no = (TextView) mGroup
				.findViewById(R.id.tv_legal_certificate_no);
		tv_business_license_name = (TextView) mGroup
				.findViewById(R.id.tv_business_license_name);
		tv_business_license_no = (TextView) mGroup
				.findViewById(R.id.tv_business_license_no);
		tv_state = (TextView) mGroup.findViewById(R.id.tv_state);
		initValue();
	}

	private void initValue() {
		showWaitDialog();
		RequestParams params = ParamsUtils.getSessionParams(ApiAgency
				.getAgentRealinfo());
		params.addBodyParameter("agentId", agentId + "");
		x.http().get(params, new WWXCallBack("AgentRealInfo") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				AgencyInfo agencyInfo = JSONObject.parseObject(
						data.getString("Data"), AgencyInfo.class);
				tv_legal_person_name.setText(agencyInfo.LegalName);
				tv_agency_phone.setText(agencyInfo.LegalMobile);
				tv_legal_certificate_no.setText(agencyInfo.LegalNo);
				tv_business_license_name.setText(agencyInfo.CropName);
				tv_business_license_no.setText(agencyInfo.LicenceNo);
				if (agencyInfo.Status == 1) {
					tv_state.setText(R.string.outhed);
					tv_state.setBackgroundResource(R.drawable.authed_state_shape);
				} else {
					tv_state.setText(R.string.unouth);
					tv_state.setBackgroundResource(R.drawable.no_auth_state_shape);
				}
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}
		});
	}

	@Override
	public void onClick(View v) {
	}

}
