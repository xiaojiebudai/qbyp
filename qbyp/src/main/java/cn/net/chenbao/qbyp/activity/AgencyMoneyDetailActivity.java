package cn.net.chenbao.qbyp.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.AgencyMoneyAdapter;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyAcount;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.SelectTimePop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:代理余额明细 Company: jsh Version：1.0
 * 
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyMoneyDetailActivity extends FatherActivity implements
		OnClickListener, OnRefreshListener<ListView> {
	private PullToRefreshListView mPullToRefreshListView;
	private AgencyMoneyAdapter agencyMoneyAdapter;
	/**
	 * 选择时间
	 */
	private int year;
	private int month;
	private boolean withTime = false;
	// 分页数据
	private int mCurrentPage = 0;
	private boolean HaveNextPage;
	private int agentId=-1;
	private SelectTimePop selectTimePop;
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_agency_money_detail;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.remaining_des, true);
		agentId=getIntent().getIntExtra(Consts.AGENT_ID, -1);
		initTextHeadRigth(R.string.query, new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectTimePop==null){
					selectTimePop = new SelectTimePop(
							AgencyMoneyDetailActivity.this,R.layout.activity_agency_money_detail, new SelectTimePop.OnSelectOKLisentner() {

						@Override
						public void selectOk(int years, int months) {
							year = years;
							month = months;
							withTime = true;
							onRefresh();
						}

						@Override
						public void selectAll() {
							withTime = false;
							onRefresh();
						}
					});
				}
				selectTimePop.showChooseWindow();
			}
		});
	}

	@Override
	protected void initView() {

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_datas);
		// 设置刷新监听
		WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
		mPullToRefreshListView.setOnRefreshListener(this);
		// 设置 end-of-list监听
		mPullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						if (!HaveNextPage) {
							WWToast.showShort(R.string.nomore_data);
						} else {
							requestData(withTime);
						}
					}
				});
		agencyMoneyAdapter=new AgencyMoneyAdapter(this, AgencyMoneyAdapter.MONTH,agentId);
		mPullToRefreshListView.setAdapter(agencyMoneyAdapter);
	}

	@Override
	protected void doOperate() {
		requestData(withTime);
	}

	@Override
	public void onClick(View v) {

	}
	/***
	 * Description: 请求数据
	 * 
	 * @author ZXJ
	 * @date 2016-10-12
	 * @param withTime
	 ***/
	private void requestData(boolean withTime) {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiAgency.getAccountDayDetail());
		params.addBodyParameter("sessionId", WWApplication.getInstance()
				.getSessionId());
		params.addBodyParameter("pageSize", 20 + "");
		if (withTime) {
			params.addBodyParameter("queryDate", year + ""
					+ (month > 9 ? month : ("0" + month)));
			ZLog.showPost(year + "" + (month > 9 ? month : ("0" + month)));
		}
		if(agentId>0){
			params.addBodyParameter("agentId", agentId + "");
		}
		params.addBodyParameter("pageIndex", mCurrentPage + "");

		x.http().get(params, new WWXCallBack("AccountDayDetail") {

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
				mPullToRefreshListView.onRefreshComplete();
			}

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mCurrentPage++;
				HaveNextPage = data.getBooleanValue("HaveNextPage");
				ArrayList<AgencyAcount> dataList = (ArrayList<AgencyAcount>)JSONArray
						.parseArray(data.getString("Data"),
								AgencyAcount.class);
				if (mCurrentPage > 1) {
					agencyMoneyAdapter.addDatas(dataList);
				} else {
					agencyMoneyAdapter.setDatas(dataList);
				}
			}
		});

	}
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mCurrentPage = 0;
		requestData(withTime);
	}

	public void onRefresh() {
		mCurrentPage = 0;
		requestData(withTime);
	}
}
