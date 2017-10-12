package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.BankAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Bank;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:银行卡列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-29
 */
public class BankListActivity extends FatherActivity implements
		OnClickListener, OnRefreshListener<ListView> {
	private PullToRefreshListView mPullToRefreshListView;
	private BankAdapter adapter;
	// 分页数据
	private int mCurrentPage = 0;
	// 提现
	private ArrayList<Bank> list;
	private int model;
	/** 展示 */
	public final static int DISPLAY = 0;
	/** 选择 */
	public final static int SELECT = 1;

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_bank_list;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.bank_manage, true);
		model = getIntent().getIntExtra(Consts.KEY_MODULE, DISPLAY);
	}

	@Override
	protected void initView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_bank);
		mPullToRefreshListView.setOnRefreshListener(this);
		mPullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

					@Override
					public void onLastItemVisible() {
						 if(mCurrentPage>=totalCount){
							 WWToast.showShort(R.string.nomore_data);
							 }else{
									requestData();
							 }
					}
				});
		WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
		adapter = new BankAdapter(this);
		mPullToRefreshListView.setAdapter(adapter);
		mPullToRefreshListView
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (model == SELECT) {
							Intent intent = new Intent();
							intent.putExtra(
									Consts.KEY_DATA,
									JSON.toJSONString(adapter.getDatas().get(
											position-1)));
							setResult(RESULT_OK, intent);
							finish();
						} 
//						else {
//							PublicWay.startAddBankActivity(
//									BankListActivity.this, 888,
//									AddBankActivity.EDIT, JSONObject
//											.toJSONString(adapter.getDatas()
//													.get(position)));
//						}
					}
				});
		findViewById(R.id.ll_add).setOnClickListener(this);
	}

	@Override
	protected void doOperate() {
		requestData();
	}
	private int totalCount = 0;
	private void requestData() {
		showWaitDialog();
		mCurrentPage = 0;
		RequestParams params = new RequestParams(ApiUser.getBanksGet());
		params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		params.addBodyParameter("pageSize", 20 + "");
		params.addBodyParameter("pageIndex", mCurrentPage + "");
		x.http().get(params, new WWXCallBack("BanksGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mCurrentPage++;
				list = (ArrayList<Bank>) JSONArray.parseArray(
						data.getString("Data"), Bank.class);
				totalCount =data.getIntValue("PageCount");
				if (list != null && list.size() > 0) {
					if (mCurrentPage > 1) {
						adapter.addDatas(list);
					} else {
						adapter.setDatas(list);
					}
				}

			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
				mPullToRefreshListView.onRefreshComplete();
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mCurrentPage = 0;
		requestData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_add:
			PublicWay.startAddBankActivity(BankListActivity.this, 888,
					AddBankActivity.ADD, null);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 888 && arg1 == RESULT_OK) {
			mCurrentPage = 0;
			requestData();
		}
	}
}
