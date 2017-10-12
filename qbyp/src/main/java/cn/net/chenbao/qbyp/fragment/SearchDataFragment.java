package cn.net.chenbao.qbyp.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.adapter.listview.BussinessAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
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
 * 搜索数据模块
 * 
 * @author xl
 * @date 2016-7-26 下午10:06:33
 * @description 搜索结果,主界面和搜索结果都可以使用
 */
public class SearchDataFragment extends FatherFragment implements
		OnRefreshListener<ListView>, OnLastItemVisibleListener {

	private PullToRefreshListView mListView;
	/** 首页模式 */
	public static final int HOME_PAGER_MODE = 1;
	/** 热搜模式 */
	public static final int HOT_SEARCH_MODE = 2;
	/** 支持配送 */
	public static final int HOT_SEARCH_PEISONG = 3;
	/** 更多模式 */
	public static final int MORE_SEARCH_MODE = 4;
	/** 更多配送 */
	public static final int MORE_SEARCH_PEISONG = 5;

	private BussinessAdapter mAdapter;
	/** 纬度 */
	private double latitude;
	/** 经度 */
	private double longitude;

	private int currentPager;

	private String tradId;

	// private AutoRankView llContainer;
	private List<View> list = new ArrayList<View>();
	private String keyWords;
	/** 当前模式 */
	private int mode;
	private LinearLayout llContainer;
	private HorizontalScrollView srcollView;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_search_data;
	}

	@SuppressLint("NewApi")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mode = getArguments().getInt(Consts.KEY_MODULE);
		switch (mode) {
		case HOME_PAGER_MODE:
			tradId = getArguments().getString(Consts.KEY_DATA, "");
			latitude = getArguments().getDouble(Consts.LATITUDE, -2);
			longitude = getArguments().getDouble(Consts.LONGITUDE, -2);
			showWaitDialog();
			doRequest();
			if (!tradId.equals("null")) {
				getTabButton(tradId);
			}
			break;
		case HOT_SEARCH_MODE:
			srcollView.setVisibility(View.GONE);
			latitude = getArguments().getDouble(Consts.LATITUDE, -2);
			longitude = getArguments().getDouble(Consts.LONGITUDE, -2);
			keyWords = getArguments().getString(Consts.KEY_DATA);
			showWaitDialog();
			doRequest();
			break;
		case HOT_SEARCH_PEISONG:
			srcollView.setVisibility(View.GONE);
			latitude = getArguments().getDouble(Consts.LATITUDE, -2);
			longitude = getArguments().getDouble(Consts.LONGITUDE, -2);
			keyWords = getArguments().getString(Consts.KEY_DATA);
			showWaitDialog();
			doRequest();
			break;

		case MORE_SEARCH_MODE:
			srcollView.setVisibility(View.GONE);
			latitude = getArguments().getDouble(Consts.LATITUDE, -2);
			longitude = getArguments().getDouble(Consts.LONGITUDE, -2);
			tradId = getArguments().getString(Consts.KEY_DATA, "");
			showWaitDialog();
			doRequest();
			break;
		case MORE_SEARCH_PEISONG:
			srcollView.setVisibility(View.GONE);
			latitude = getArguments().getDouble(Consts.LATITUDE, -2);
			longitude = getArguments().getDouble(Consts.LONGITUDE, -2);
			tradId = getArguments().getString(Consts.KEY_DATA, "");
			showWaitDialog();
			doRequest();
			break;
		}

	}

	@Override
	protected void initView() {
		srcollView = (HorizontalScrollView) mGroup
				.findViewById(R.id.scrollview);
		mGroup.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 关闭硬件加速,为了显示虚线
		llContainer = (LinearLayout) mGroup
				.findViewById(R.id.ll_scroll_container);
		mAdapter = new BussinessAdapter(getActivity());
		mListView = (PullToRefreshListView) mGroup
				.findViewById(R.id.listview_search_data);
		// llContainer = (AutoRankView) mGroup.findViewById(R.id.ll_container);
		ListView listView = mListView.getRefreshableView();
		WWViewUtil.setEmptyView(listView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
				} else {
					PublicWay.startStoreActivity(getActivity(),
							mAdapter.getItem(position - 1).SellerId, 0);
				}
			}
		});
		mListView.setOnRefreshListener(this);
		mListView.setOnLastItemVisibleListener(this);
	}
	DecimalFormat decimalFormat = new DecimalFormat("#.######");//格式化设置
	/**
	 * 请求数据
	 */
	public void doRequest() {
		showWaitDialog();
		RequestParams requestParams = null;

		switch (mode) {
		case HOME_PAGER_MODE:
			requestParams = new RequestParams(ApiSeller.searchTrade());
			requestParams.addBodyParameter("longitude", decimalFormat.format(longitude) + "");
			requestParams.addBodyParameter("latitude", decimalFormat.format(latitude) + "");
			if (!tradId.equals("null")) {
				requestParams.addBodyParameter("tradeId", tradId);
			}
			requestParams.addBodyParameter("pageIndex", currentPager + "");
			if(!TextUtils.isEmpty(keyWords)){
				requestParams.addBodyParameter("key", keyWords);
			}
			break;
		case HOT_SEARCH_MODE:
			requestParams = new RequestParams(ApiSeller.searchTrade());
			requestParams.addBodyParameter("longitude", decimalFormat.format(longitude) + "");
			requestParams.addBodyParameter("latitude", decimalFormat.format(latitude) + "");
			requestParams.addBodyParameter("pageIndex", currentPager + "");
			requestParams.addBodyParameter("key", keyWords);
			break;

		case HOT_SEARCH_PEISONG:
			requestParams = new RequestParams(ApiSeller.searchTrade());
			requestParams.addBodyParameter("longitude", decimalFormat.format(longitude) + "");
			requestParams.addBodyParameter("latitude", decimalFormat.format(latitude) + "");
			requestParams.addBodyParameter("pageIndex", currentPager + "");
			requestParams.addBodyParameter("key", keyWords);
			requestParams.addBodyParameter("deliver", 1 + "");// 支持配送
			break;
		case MORE_SEARCH_MODE:
			requestParams = new RequestParams(ApiSeller.searchTrade());
			requestParams.addBodyParameter("longitude", decimalFormat.format(longitude) + "");
			requestParams.addBodyParameter("latitude", decimalFormat.format(latitude) + "");
			requestParams.addBodyParameter("pageIndex", currentPager + "");
			requestParams.addBodyParameter("tradeId", tradId);
			if(!TextUtils.isEmpty(keyWords)){
				requestParams.addBodyParameter("key", keyWords);
			}
			break;

		case MORE_SEARCH_PEISONG:
			requestParams = new RequestParams(ApiSeller.searchTrade());
			requestParams.addBodyParameter("longitude", decimalFormat.format(longitude)+ "");
			requestParams.addBodyParameter("latitude", decimalFormat.format(latitude) + "");
			requestParams.addBodyParameter("tradeId", tradId);
			requestParams.addBodyParameter("pageIndex", currentPager + "");
			requestParams.addBodyParameter("deliver", 1 + "");
			if(!TextUtils.isEmpty(keyWords)){
				requestParams.addBodyParameter("key", keyWords);
			}
			break;
		}

		x.http().get(requestParams, new WWXCallBack("Search") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
                currentPager++;
				JSONArray jsonArray = data.getJSONArray("Data");
				List<TradesMessage> list = JSON.parseArray(
						jsonArray.toJSONString(), TradesMessage.class);
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
				dismissWaitDialog();

				mListView.onRefreshComplete();
			}
		});
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		currentPager = 0;
		doRequest();
	}

	@Override
	public void onLastItemVisible() {
		doRequest();
	}

	/**
	 * tab选中的刷新数据
	 */
	public void tabSelectRreshList(String tradeId) {
		doRequest();
	}

	public void getTabButton(String tradeId) {
		showWaitDialog();
		RequestParams params = new RequestParams(
				ApiBaseData.getTradesCategory());
		params.addBodyParameter("parentId", tradeId);
		x.http().get(params, new WWXCallBack("TradesGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				ArrayList<TradesCategory> arrayList = new ArrayList<TradesCategory>();
				TradesCategory object = new TradesCategory();
				object.TradeId = tradId;
				object.TradeName = getString(R.string.all);
				arrayList.add(object);
				JSONArray parseArray = data.getJSONArray("Data");
				List<TradesCategory> parseArray2 = JSON.parseArray(
						parseArray.toJSONString(), TradesCategory.class);
				for (TradesCategory tradesCategory : parseArray2) {
					arrayList.add(tradesCategory);
				}
				for (final TradesCategory array : arrayList) {
					final LinearLayout view = (LinearLayout) View.inflate(
							getActivity(), R.layout.home_tab_button, null);
					final TextView inflate = (TextView) view
							.findViewById(R.id.tv);
					if (array.TradeName.equals(getString(R.string.all))) {
						inflate.setSelected(true);
					}
					inflate.setText(array.TradeName);
					inflate.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!inflate.isSelected()) {
								inflate.setSelected(true);
								tradId = array.TradeId;
								currentPager = 0;
								doRequest();
								setTextSelectState(v);
							}
						}
					});
					list.add(inflate);
					llContainer.addView(view);
				}
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}
		});

	}

	/**
	 * 区分业务,不同NEW法
	 * 
	 * @param mode
	 * @param latitude
	 * @param longitude
	 * @param tradId
	 * @param keyWords
	 * @return
	 */
	public static SearchDataFragment newInstance(int mode, double latitude,
			double longitude, String tradId, String keyWords) {
		SearchDataFragment searchDataFragment = null;
		if (mode == HOME_PAGER_MODE) {
			searchDataFragment = new SearchDataFragment();
			Bundle args = new Bundle();
			args.putInt(Consts.KEY_MODULE, mode);
			args.putDouble(Consts.LATITUDE, latitude);
			args.putDouble(Consts.LONGITUDE, longitude);
			args.putString(Consts.KEY_DATA, tradId);
			searchDataFragment.setArguments(args);
		} else if (mode == HOT_SEARCH_MODE) {
			searchDataFragment = new SearchDataFragment();
			Bundle args = new Bundle();
			args.putInt(Consts.KEY_MODULE, mode);
			args.putDouble(Consts.LATITUDE, latitude);
			args.putDouble(Consts.LONGITUDE, longitude);
			args.putString(Consts.KEY_DATA, keyWords);
			searchDataFragment.setArguments(args);
		} else if (mode == HOT_SEARCH_PEISONG) {
			searchDataFragment = new SearchDataFragment();
			Bundle args = new Bundle();
			args.putInt(Consts.KEY_MODULE, mode);
			args.putDouble(Consts.LATITUDE, latitude);
			args.putDouble(Consts.LONGITUDE, longitude);
			args.putString(Consts.KEY_DATA, keyWords);
			searchDataFragment.setArguments(args);
		} else if (mode == MORE_SEARCH_MODE) {
			searchDataFragment = new SearchDataFragment();
			Bundle args = new Bundle();
			args.putInt(Consts.KEY_MODULE, mode);
			args.putDouble(Consts.LATITUDE, latitude);
			args.putDouble(Consts.LONGITUDE, longitude);
			args.putString(Consts.KEY_DATA, tradId);
			searchDataFragment.setArguments(args);
		} else if (mode == MORE_SEARCH_PEISONG) {
			searchDataFragment = new SearchDataFragment();
			Bundle args = new Bundle();
			args.putInt(Consts.KEY_MODULE, mode);
			args.putDouble(Consts.LATITUDE, latitude);
			args.putDouble(Consts.LONGITUDE, longitude);
			args.putString(Consts.KEY_DATA, tradId);
			searchDataFragment.setArguments(args);
		}
		return searchDataFragment;
	}

	public void setTextSelectState(View v) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) != v) {
				list.get(i).setSelected(false);
			}
		}
	}

	/**
	 * 搜索模式公开方法
	 * 
	 * @param key
	 */
	public void setSearchData(String key) {
		//搜索不需要区分模式
//		if (mode == HOT_SEARCH_MODE || mode == HOT_SEARCH_PEISONG|| mode==MORE_SEARCH_MODE) {
		ZLog.showPost("model"+mode);
			currentPager = 0;
			keyWords = key;
			doRequest();
//		}
	}
}
