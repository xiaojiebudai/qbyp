package cn.net.chenbao.qbyp.fragment;

import java.util.List;

import cn.net.chenbao.qbyp.adapter.listview.ShopCommAdapter;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.bean.Comment;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;

/**
 * 评价模块
 * 
 * @author xl
 * @date 2016-7-27 下午9:46:06
 * @description
 */
public class EvaluateFragment extends FatherFragment implements
		OnRefreshListener<ListView>, OnLastItemVisibleListener {

	private RatingBar mRatingBar;
	private TextView mTvPoint;
	private PullToRefreshListView mListview;
	private ShopCommAdapter mAdapter;

	private int currentPager;
	private long sellerId;
	private float pinfen;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_evaluate;
	}

	@Override
	protected void initView() {
		sellerId = getArguments().getLong(Consts.KEY_DATA, -1);
		pinfen = getArguments().getFloat("pinfen", -1f);
		doRequest();
		mAdapter = new ShopCommAdapter(getActivity());
		mRatingBar = (RatingBar) mGroup.findViewById(R.id.rb);
		mTvPoint = (TextView) mGroup.findViewById(R.id.tv_point);
		double star = Arith.round(pinfen, 1);
		mTvPoint.setText(star + "");
		mRatingBar.setStar((float) star);
		mListview = (PullToRefreshListView) mGroup
				.findViewById(R.id.listview_datas);
		ListView listView = mListview.getRefreshableView();
		WWViewUtil.setEmptyView(listView);
		mListview.setAdapter(mAdapter);
		mListview.setOnRefreshListener(this);
		mListview.setOnLastItemVisibleListener(this);
	}

	private void doRequest() {
		RequestParams params = new RequestParams(ApiSeller.getShopComment());
		params.addBodyParameter("pageIndex", currentPager + "");
		params.addBodyParameter(Consts.SELLER_ID, sellerId + "");
		x.http().get(params, new WWXCallBack("JudgesGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
                currentPager++;
				JSONArray jsonArray = data.getJSONArray("Data");
				List<Comment> list = JSON.parseArray(jsonArray.toJSONString(),
						Comment.class);
				if (currentPager == 1) {
					mAdapter.setDatas(list);
				} else {
					if (list.size() == 0) {
						WWToast.showShort(R.string.nomore_data);
					} else {
						mAdapter.addDatas(list);
					}
				}
			}

			@Override
			public void onAfterFinished() {

				mListview.onRefreshComplete();
			}
		});

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		currentPager = 0;
		doRequest();
	}

	public static EvaluateFragment newInstance(long sellerId, float fenshu) {
		EvaluateFragment evaluateFragment = new EvaluateFragment();
		Bundle args = new Bundle();
		args.putLong(Consts.KEY_DATA, sellerId);
		args.putFloat("pinfen", fenshu);
		evaluateFragment.setArguments(args);
		return evaluateFragment;
	}

	@Override
	public void onLastItemVisible() {
		doRequest();
	}

}
