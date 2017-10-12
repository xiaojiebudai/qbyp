package cn.net.chenbao.qbypseller.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.SystemMessageAdapter;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.SytemMessage;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 系统消息
 * 
 * @author xl
 * @date 2016-7-31 下午9:09:27
 * @description
 */
public class SystemNewsActivity extends FatherActivity {

	private PullToRefreshListView mListView;
	private SystemMessageAdapter mAdapter;

	@Override
	protected int getLayoutId() {
		return R.layout.act_system_news;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.system_news, true);
	}

	@Override
	protected void initView() {
		mAdapter = new SystemMessageAdapter(this);
		mListView = (PullToRefreshListView) findViewById(R.id.plv);
		mListView.setAdapter(mAdapter);
		WWViewUtil.setEmptyView(mListView.getRefreshableView());
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mCurrentPage = 0;
				requestData();
			}
		});
		mListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				if (mCurrentPage >= totalCount) {
					WWToast.showShort(R.string.nomore_data);
				} else {
					requestData();
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!mAdapter.getItem(position - 1).ReadFlag) {
					readMessage(position - 1);
				}
			}
		});
	}

	@Override
	protected void doOperate() {
		requestData();
	}

	// 分页数据
	private int mCurrentPage = 0;
	private int totalCount = 0;

	/***
	 * 请求数据
	 */
	private void requestData() {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiSeller.InterMessage());
		params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		params.addBodyParameter("pageSize", 20 + "");
		params.addBodyParameter("pageIndex", mCurrentPage + "");
		x.http().get(params, new WWXCallBack("InterMessage") {
			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mCurrentPage++;
				totalCount = data.getIntValue("PageCount");
				ArrayList<SytemMessage> listData = (ArrayList<SytemMessage>) JSONArray
						.parseArray(data.getString("Data"), SytemMessage.class);
				if (mCurrentPage > 1) {
					mAdapter.addDatas(listData);
				} else {
					mAdapter.setDatas(listData);
				}
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
				mListView.onRefreshComplete();
			}
		});
	}

	/**
	 * 消息已读
	 * 
	 * @param i
	 */
	private void readMessage(final int i) {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiSeller.InterMessageRead());
		params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		params.addBodyParameter("flowId", mAdapter.getDatas().get(i).FlowId + "");
		x.http().get(params, new WWXCallBack("InterMessageRead") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mAdapter.getDatas().get(i).ReadFlag=true;
				mAdapter.notifyDataSetChanged();
				WWToast.showShort(R.string.message_read);
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}
		});

	}
}
