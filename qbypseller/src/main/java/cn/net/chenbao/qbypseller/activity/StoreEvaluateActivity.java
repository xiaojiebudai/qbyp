package cn.net.chenbao.qbypseller.activity;

import java.util.List;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.EvaluationAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Evaluation;
import cn.net.chenbao.qbypseller.bean.SellerInfo;
import cn.net.chenbao.qbypseller.utils.Arith;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.view.RatingBar;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.net.chenbao.qbypseller.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 店铺评价
 * 
 * @author xl
 * @date 2016-8-13 下午4:30:29
 * @description
 */
public class StoreEvaluateActivity extends FatherActivity {

	@Override
	protected int getLayoutId() {
		return R.layout.act_store_evaluate;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.store_evaluate, true);
	}

	private EvaluationAdapter mAdapter;

	@Override
	protected void initView() {
		mAdapter = new EvaluationAdapter(this);
		PullToRefreshListView listView = (PullToRefreshListView) findViewById(R.id.listview_datas);
		listView.setAdapter(mAdapter);
		SellerInfo sellerInfo = SharedPreferenceUtils.getInstance()
				.getSellerInfo();
		if (sellerInfo != null) {
			((RatingBar) findViewById(R.id.ratingbar)).setStar((float) Arith.round(
							Double.parseDouble(sellerInfo.JudgeLevel), 1));
			((TextView) findViewById(R.id.tv_point)).setText(Arith.round(
					Double.parseDouble(sellerInfo.JudgeLevel), 1)
					+ "");
		}

	}

	@Override
	protected void doOperate() {
		requestData();
	}

	private int mCurrentPage = 0;

	private void requestData() {
		RequestParams requestParams = ParamsUtils.getPageGetParams(
				ApiSeller.judgesGet(), mCurrentPage);
		requestParams.addQueryStringParameter("sellerId",
				WWApplication.getSellerId());
		showWaitDialog();
		x.http().get(requestParams, new WWXCallBack("JudgesGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				JSONArray array = data.getJSONArray(Api.KEY_DATA);
				if (array != null) {
					List<Evaluation> list = JSON.parseArray(
							array.toJSONString(), Evaluation.class);
					if (mCurrentPage == 0) {
						mAdapter.setDatas(list);
					} else {
						mAdapter.addDatas(list);
					}
				}
			}

			@Override
			public void onAfterFinished() {
               dismissWaitDialog();
			}
		});

	}
}
