package cn.net.chenbao.qbyp.fragment;

import java.util.ArrayList;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FenrunDetailActivity;
import cn.net.chenbao.qbyp.adapter.listview.FenrunAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AccountWwbmx;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/***
 * Description:分润fragment Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-29
 */
public class FenrunFragment extends FatherFragment implements OnClickListener,
		OnRefreshListener<ListView> {
	private PullToRefreshListView mPullToRefreshListView;
	private FenrunAdapter fenrunAdapter;
	private int tab;
	private TextView tv_jiesun, tv_unjiesun,tv_2;
	private boolean withTime = false;
	private int year;
	private int month;

	@Override
	public void onClick(View v) {

	}

	@Override
	protected int getLayoutId() {
		return R.layout.frag_fenrun;
	}

	@Override
	protected void initView() {
		tab = getArguments().getInt(Consts.KEY_MODULE);
		tv_2 = (TextView) mGroup.findViewById(R.id.tv_2);
		switch (tab) {
		case FenrunDetailActivity.ONE:
			requestResultCode = "RebateParentDetail";
			url = ApiUser.getRebateParentDetail();
			tv_2.setText(R.string.fans);
			break;
		case FenrunDetailActivity.TWO:
			requestResultCode = "RebateRegionDetail";
			url = ApiUser.getRebateRegionDetail();
			tv_2.setText(R.string.area);
			break;
		case FenrunDetailActivity.THREE:
			requestResultCode = "RebateRecSellerDetail";
			url = ApiUser.getRebateRecSellerDetail();
			tv_2.setText(R.string.shop);
			break;
		default:
			break;
		}
		fenrunAdapter = new FenrunAdapter(getActivity(),tab);
		mPullToRefreshListView = (PullToRefreshListView) mGroup
				.findViewById(R.id.listview_datas);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
					}
				});
		// 设置刷新监听
		WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
		mPullToRefreshListView.setOnRefreshListener(this);
		// 设置 end-of-list监听
		mPullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						if(mCurrentPage>=totalCount){
							WWToast.showShort(R.string.nomore_data);
						}else{
							requestData(withTime);
						}
					}
				});
		tv_jiesun = (TextView) mGroup.findViewById(R.id.tv_jiesun);
		tv_unjiesun = (TextView) mGroup.findViewById(R.id.tv_unjiesun);
	
		mPullToRefreshListView.setAdapter(fenrunAdapter);
		requestData(withTime);
	}

	// 分页数据
	private int mCurrentPage = 0;
	private int totalCount = 0;
	private String requestResultCode;
	private String url;
	private ArrayList<AccountWwbmx> list;

	private void requestData(boolean withTime) {
		showWaitDialog();
		RequestParams params = new RequestParams(url);
		params.addBodyParameter("sessionId", WWApplication.getInstance()
				.getSessionId());
		if (withTime) {
			params.addBodyParameter("queryDate", year + ""
					+ (month > 9 ? month : ("0" + month)));
		}
		params.addBodyParameter("pageSize", 20 + "");
		params.addBodyParameter("pageIndex", mCurrentPage + "");
		
		x.http().get(params, new WWXCallBack(requestResultCode) {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mCurrentPage++;
				totalCount=data.getIntValue("PageCount");
				list = (ArrayList<AccountWwbmx>) JSONArray.parseArray(
						data.getString("Data"), AccountWwbmx.class);
				tv_jiesun.setText(data.getString("TotalAmount"));
				tv_unjiesun.setText(data.getString("TotalAmount2"));
					if (mCurrentPage > 1) {
						fenrunAdapter.addDatas(list);
					} else {
						fenrunAdapter.setDatas(list);
					}
			}

			@Override
			public void onAfterFinished() {
				mPullToRefreshListView.onRefreshComplete();
				dismissWaitDialog();
			}

		});

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mCurrentPage = 0;
		requestData(withTime);
	}

	/**
	 * 外部刷新
	 * @param years
	 * @param months
	 */
	public void onRefresh(int years, int months,boolean isAll) {
		if(isAll){
			mCurrentPage = 0;
			withTime=false;
			requestData(withTime);
		}else{
			year = years;
			month = months;
			mCurrentPage = 0;
			withTime=true;
			requestData(withTime);
		}

	}
}
