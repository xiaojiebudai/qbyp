package cn.net.chenbao.qbyp.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.listview.AgencyInfoAdapter;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/***
 * Description:区域代理专区 Company: jsh Version：1.0
 * 
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyOnlyFragment extends FatherFragment implements
		OnClickListener, OnRefreshListener<ListView> {
	private PullToRefreshListView mPullToRefreshListView;
	private ImageView iv_header;
	private TextView tv_username;
//	private TextView tv_phone;
	private TextView tv_money;
	private List<AgencyInfo> list;
	private AgencyInfoAdapter agencyInfoAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_agency_only;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		initTitle();

		mPullToRefreshListView = (PullToRefreshListView) mGroup
				.findViewById(R.id.listview_datas);
		View agency_headerview_info = View.inflate(getActivity(),
				R.layout.listview_agency_header_info, null);
		mPullToRefreshListView.getRefreshableView().addHeaderView(
				agency_headerview_info);
		mPullToRefreshListView.setOnRefreshListener(this);
		iv_header = (ImageView) agency_headerview_info
				.findViewById(R.id.img_header);
		tv_username = (TextView) agency_headerview_info
				.findViewById(R.id.tv_username);
//		tv_phone = (TextView) agency_headerview_info
//				.findViewById(R.id.tv_phone);
		tv_money = (TextView) agency_headerview_info
				.findViewById(R.id.tv_money);

		initValue();
	}

	private void initValue() {
		agencyInfoAdapter = new AgencyInfoAdapter(getActivity());
		mPullToRefreshListView.setAdapter(agencyInfoAdapter);
		requestData();
	}

	/***
	 * Description: 请求数据
	 * 
	 * @author ZXJ
	 * @date 2016-10-12
	 ***/
	private void requestData() {
		showWaitDialog();
		x.http().get(ParamsUtils.getSessionParams(ApiAgency.getAgentsInfo()),
				new WWXCallBack("AgentsInfo") {

					@Override
					public void onAfterSuccessOk(JSONObject data) {
						list = JSONArray.parseArray(
								data.getString("Data"), AgencyInfo.class);

						ImageUtils.setCircleHeaderImage(getActivity(),
								data.getString("HeadUrl"), iv_header);
						tv_username.setText(data.getString("UserName"));
//						tv_phone.setText(data.getString("Mobile"));
						tv_money.setText(WWViewUtil.numberFormatPrice(data
										.getDoubleValue("Account")));
						if (list != null) {
							agencyInfoAdapter.setDatas(list);
						}

					}

					@Override
					public void onAfterFinished() {
						mPullToRefreshListView.onRefreshComplete();
						dismissWaitDialog();
					}
				});

	}

	private void initTitle() {
		mGroup.findViewById(R.id.back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						getActivity().finish();
					}
				});
		TextView center = (TextView) mGroup.findViewById(R.id.tv_head_center);
		center.setText(R.string.agency_zone);
		center.setTextColor(getResources().getColor(R.color.white));
		center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		requestData();
	}

}
