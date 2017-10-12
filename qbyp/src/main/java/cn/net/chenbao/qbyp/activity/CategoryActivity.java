package cn.net.chenbao.qbyp.activity;

import java.util.List;

import cn.net.chenbao.qbyp.adapter.CategoryGridViewAdapter;
import cn.net.chenbao.qbyp.adapter.listview.CategoryAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.utils.BaiDuLocationUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.location.BDLocation;

import cn.net.chenbao.qbyp.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * 分类界面
 *
 * @author xl
 * @date:2016-7-27下午4:08:23
 * @description
 */
public class CategoryActivity extends FatherActivity implements
        OnRefreshListener<GridView>, OnItemClickListener {

    /**
     * 类目列表
     */
    private ListView mListView;
    /**
     * 数据网格列表
     */
    private PullToRefreshGridView mPullToRefreshGridView;

    private CategoryAdapter mAdapter;

    private double latitudes;
    private double Longitudes;
    private CategoryGridViewAdapter mGridAdapter;
    private String tradeId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.act_category;
    }

    @Override
    protected void initValues() {
        final BaiDuLocationUtils instance = new BaiDuLocationUtils(this);
        instance.startLocation();
        instance.setLocationListener(new BaiDuLocationUtils.LocationListener() {
            @Override
            public void LocationMessageCallBack(BDLocation bd) {
                latitudes = bd.getLatitude();
                Longitudes = bd.getLongitude();
                instance.stopLocation();
            }
        });// 保证精准再定位一次,不从前面界面带
        initHeadBack();
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_head_center).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.startSearchActivity(CategoryActivity.this, SearchActiivty.LOCATION, 0);
                    }
                });
        mAdapter = new CategoryAdapter(this);
        mListView = (ListView) findViewById(R.id.listview_categories);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mPullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.gridview_datas);
        GridView view = mPullToRefreshGridView.getRefreshableView();
        WWViewUtil.setEmptyView(view);
        mGridAdapter = new CategoryGridViewAdapter(this);
        mPullToRefreshGridView.setAdapter(mGridAdapter);
        mPullToRefreshGridView.setOnRefreshListener(this);
        mPullToRefreshGridView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // PublicWay.startStoreActivity(CategoryActivity.this,
                        // mGridAdapter.getItem(position).SellerId, 0);

                        PublicWay.startSearchResultActivity(
                                CategoryActivity.this,
                                mGridAdapter.getItem(position).TradeId, 0,
                                latitudes, Longitudes,
                                SearchDataActiivty.MORE_MODE);
                    }
                });

    }

    @Override
    protected void doOperate() {
        doRequest();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (!tradeId.equals(mAdapter.getItem(position).TradeId)) {
            tradeId = mAdapter.getItem(position).TradeId;
            getTradeDetail(tradeId);
        }

    }

    private void doRequest() {
        showWaitDialog();
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        x.http().get(params, new WWXCallBack("TradesGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesCategory> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesCategory.class);
                mAdapter.setDatas(list);
            }

            @Override
            public void onAfterFinished() {

                dismissWaitDialog();
                mListView.performItemClick(null, 0, 0);
            }
        });
    }
    private boolean isRequest;
    private void getTradeDetail(String parentId) {
        showWaitDialog();
        isRequest=true;
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        params.addBodyParameter("parentId", parentId);
        x.http().get(params, new WWXCallBack("TradesGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<TradesCategory> list = JSON.parseArray(
                        jsonArray.toJSONString(), TradesCategory.class);
                mGridAdapter.setDatas(list);

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                isRequest=false;
                mPullToRefreshGridView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onRefresh(PullToRefreshBase<GridView> refreshView) {
        if(!isRequest){
            getTradeDetail(tradeId);
        }else{
            mPullToRefreshGridView.onRefreshComplete();
        }


    }
}
