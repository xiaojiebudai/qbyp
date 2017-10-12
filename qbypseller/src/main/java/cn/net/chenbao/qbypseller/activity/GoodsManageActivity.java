package cn.net.chenbao.qbypseller.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.GoodsAdaper;
import cn.net.chenbao.qbypseller.adapter.listview.GoodsCategoryAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

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
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;

/**
 * 商品管理
 * 
 * @author xl
 * @date 2016-7-30 上午1:40:57
 * @description
 */
public class GoodsManageActivity extends FatherActivity implements
		OnClickListener {

	private ListView mListView_categories;
	private PullToRefreshListView mPullToRefreshListView_goods;
	private GoodsAdaper mGoodsAdaper;
	private GoodsCategoryAdapter mCategoryAdapter;
	/** 条件类目 */
	private static final int REQUEST_CODE_ADD_CATEGORY = 10;
	/** 添加商品 */
	private static final int REQUEST_CODE_ADD_GOODS = 11;
	/** 编辑商品 */
	private static final int REQUEST_CODE_EDIT_GOODS = 12;

	@Override
	protected int getLayoutId() {
		return R.layout.act_goods_manage;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.goods_manage, true);
	}

	private int currentCategroyPosition = 0;
	private long currentCategroyId = 0;

	@Override
	protected void initView() {
		mListView_categories = (ListView) findViewById(R.id.listview_category);
		mPullToRefreshListView_goods = (PullToRefreshListView) findViewById(R.id.listview_goods);
		mGoodsAdaper = new GoodsAdaper(this);
		mCategoryAdapter = new GoodsCategoryAdapter(this);
		mListView_categories.setAdapter(mCategoryAdapter);
		mPullToRefreshListView_goods.setAdapter(mGoodsAdaper);
		findViewById(R.id.tv_upload_goods).setOnClickListener(this);
		findViewById(R.id.ll_add_category).setOnClickListener(this);
		mListView_categories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPageIndex = 0;
				currentCategroyPosition = position;
				Category category = mCategoryAdapter.getItem(position);
				currentCategroyId = category.ClassId;
				requestGoodsData(category);
			}
		});
		WWViewUtil.setEmptyView(mPullToRefreshListView_goods
				.getRefreshableView());
		mPullToRefreshListView_goods
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Goods goods = mGoodsAdaper.getItem(position - 1);
						Category category;
						if (isLocal(mCategoryAdapter
								.getItem(currentCategroyPosition))) {
							category = mMap_categories.get(goods.ClassId);
						} else {
							category = mMap_categories.get(goods.ClassId);
							// 之前是当前选择的类目,避免发生错位
							// category = mCategoryAdapter
							// .getItem(currentCategroy);
						}
						if (category == null) {// 没拉到类目数据或出现异常
							requestCategoryData();
							return;
						}
						PublicWay.startAddGoodsActivity(
								GoodsManageActivity.this, category, goods,
								position - 1, REQUEST_CODE_EDIT_GOODS);

					}
				});
		mPullToRefreshListView_goods.setMode(Mode.BOTH);
		mPullToRefreshListView_goods
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						mPageIndex = 0;
						Category category = mCategoryAdapter
								.getItem(currentCategroyPosition);
						requestGoodsData(category);
					}

					@Override
					public void onPullUpToRefresh(PullToRefreshBase refreshView) {
						requestGoodsData(mCategoryAdapter
								.getItem(currentCategroyPosition));
					}
				});
	}

	private boolean isLocal(Category category) {
		return Category.CLASS_ALL == category.ClassId
				|| Category.CLASS_OFF == category.ClassId;
	}

	@Override
	protected void doOperate() {
		requestCategoryData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_upload_goods:
			if(mCategoryAdapter!=null&&mCategoryAdapter.getDatas().size()>0){
				Category category = mCategoryAdapter
						.getItem(currentCategroyPosition);

				if (Category.CLASS_ALL == category.ClassId
						|| Category.CLASS_OFF == category.ClassId) {
					PublicWay.startAddGoodsActivity(this, REQUEST_CODE_ADD_GOODS,
							AddGoodsActivity.MODULE_COMMON);
				} else {
					Category item = mCategoryAdapter
							.getItem(currentCategroyPosition);
					PublicWay.startAddGoodsActivity(this, item,
							REQUEST_CODE_ADD_GOODS);
				}
			}else{
				requestCategoryData();
			}

			break;
		case R.id.ll_add_category:
			PublicWay.startAddCategoryActivity(this, REQUEST_CODE_ADD_CATEGORY);
			break;

		default:
			break;
		}
	}

	/** 用于在"全部"和"下架"中选择商品编辑传类目 */
	private Map<Long, Category> mMap_categories;

	/** 请求类目 */
	private void requestCategoryData() {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiSeller.classGet());
		params.addQueryStringParameter(Api.KEY_SELLERID,
				WWApplication.getSellerId());
		x.http().get(params, new WWXCallBack("ClassGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				JSONArray array = data.getJSONArray(Api.KEY_DATA);
				if (array != null) {
					List<Category> list = JSON.parseArray(array.toJSONString(),
							Category.class);
					list = addLocalMode(list);
					if (list != null) {
						mMap_categories = new HashMap<Long, Category>();
						for (Category category : list) {
							mMap_categories.put(category.ClassId, category);
						}
					}
					if (currentCategroyPosition > list.size() - 1) {
						currentCategroyPosition = list.size() - 1;
					}
					mCategoryAdapter.setDatas(list);
					mListView_categories.performItemClick(null,
							currentCategroyPosition, 0);
				}
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}
		});
	}

	/**
	 * 对类目列表处理本地的"全部和下架"业务
	 * 
	 * @param list
	 * @return
	 */
	private List<Category> addLocalMode(List<Category> list) {
		Category all = new Category();
		all.ClassName = getString(R.string.all);// 全部
		all.ClassId = Category.CLASS_ALL;
		Category off = new Category();
		off.ClassName = getString(R.string.of_stock);// 下架
		off.ClassId = Category.CLASS_OFF;
		list.add(0, all);
		list.add(off);
		return list;
	}

	private int mPageIndex;

	/**
	 * 新增的商品
	 * 
	 * @description 处理一个类目上新增了商品后的分页问题
	 * */
	private ArrayList<Long> newAdds;

	/**
	 * 请求商品数据
	 * 
	 * @description 全部和下架的变动大,暂时每次都新拉,未做内存策略
	 * */
	private void requestGoodsData(final Category category) {
		showWaitDialog();
		RequestParams requestParams = ParamsUtils.getGoodsGet(category.ClassId,
				mPageIndex);
		if (Category.CLASS_OFF == category.ClassId) {
			requestParams.addQueryStringParameter("status", Goods.STATE_OFFSALE
					+ "");
		}
		x.http().get(requestParams, new WWXCallBack("GoodsGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {

				List<Goods> list = JSON.parseArray(
						data.getJSONArray(Api.KEY_DATA).toJSONString(),
						Goods.class);

				if (category != null) {
					if (mPageIndex == 0) {// 下拉数据
						mGoodsAdaper.setDatas(list);
						mPageIndex++;
					} else {// 分页加载数据
						if (list == null || list.size() == 0) {// 没有数据
							WWToast.showShort(R.string.no_more_data);
							return;
						}
						mPageIndex++;
						Iterator<Goods> iterator = list.iterator();
						if (newAdds != null && newAdds.size() > 0) {
							while (iterator.hasNext()) {// 检索一下新增的商品
								Goods next = iterator.next();
								if (newAdds.contains(next.GoodsId)) {// 分页中有已添加的商品
									iterator.remove();// 移除
								}
							}
						}
						mGoodsAdaper.addDatas(list);
					}
				}

			}

			@Override
			public void onAfterFinished() {
				mPullToRefreshListView_goods.onRefreshComplete();
				dismissWaitDialog();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_ADD_CATEGORY:
				if (data != null) {
					String s = data.getStringExtra(Consts.KEY_DATA);
					updateCategories(s);
				}
				break;
			case REQUEST_CODE_ADD_GOODS:
				if (data != null) {
					String categories = data
							.getStringExtra(Consts.KEY_DATA_TWO);
					if (categories != null) {// 编辑商品过程中新增过类目
						updateCategories(categories);
					}
					if (mCategoryAdapter.getDatas().size()>0&&!isLocal(mCategoryAdapter
							.getItem(currentCategroyPosition))) {// 不处于全部和下架
						Goods goods = JSON.parseObject(
								data.getStringExtra(Consts.KEY_DATA),
								Goods.class);
						Category category_c = mCategoryAdapter
								.getItem(currentCategroyPosition);
						// TODO:是否处理内存策略
						if (goods.ClassId == category_c.ClassId) {// 添加的当前类目的商品
							mGoodsAdaper.addItem(goods);
						} else {// 添加的其他类目的商品
							Category category_other = mMap_categories
									.get(goods.ClassId);
						}
					}
				}
				break;
			case REQUEST_CODE_EDIT_GOODS://
				if (data != null) {
					String categories = data
							.getStringExtra(Consts.KEY_DATA_TWO);
					if (categories != null) {// 编辑商品过程中新增过类目
						updateCategories(categories);
					}
					//刷新页面数据
					mPageIndex = 0;
					Category category = mCategoryAdapter
							.getItem(currentCategroyPosition);
					requestGoodsData(category);
				}
				break;
			default:
				break;
			}
		}
	}

	/** 根据内存策略刷新类目 */
	private void updateCategories(String s) {
		List<Category> list = JSON.parseArray(s, Category.class);
		addLocalMode(list);
		mCategoryAdapter.clear();
		mCategoryAdapter.setDatas(list);
		int size = list.size();
		// TODO:不必所有的情况都检索,可以优化处理
		boolean isDeleted = true;
		for (int i = 0; i < size; i++) {// 检索类目设置之前的选中状态
			Category category = list.get(i);
			if (category.ClassId == currentCategroyId) {
				isDeleted = false;
				currentCategroyPosition = i;
				mListView_categories.setItemChecked(currentCategroyPosition,
						true);
				mListView_categories.setSelection(currentCategroyPosition);
				return;
			}
		}
		if (isDeleted) {// 当前的类目被删除
			currentCategroyPosition = 0;
			mListView_categories.performItemClick(null,
					currentCategroyPosition, 0);// 选中全部
			mListView_categories.setSelection(0);
		}
	}
}
