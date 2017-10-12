package cn.net.chenbao.qbyp.fragment;

/**
 * Created by 木头 on 2016/11/2.
 */


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.GoodsCollectAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
    /**
     * Created by 木头 on 2016/11/2.
     */

    public class GoodsCollectFragment extends FatherFragment implements View.OnClickListener,
            PullToRefreshBase.OnRefreshListener<ListView> {
        private PullToRefreshListView mPullToRefreshListView;
        private GoodsCollectAdapter collectAdapter;
        private boolean isRefresh = true;
        @Override
        protected int getLayoutId() {
            return R.layout.fragment_list_data;
        }

        @Override
        protected void initView() {
            mPullToRefreshListView = (PullToRefreshListView) mGroup.findViewById(R.id.listview_datas);
            mPullToRefreshListView
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            isRefresh = true;
                            PublicWay.stratSelfSupportGoodsDetailActivity(GoodsCollectFragment.this,collectAdapter.getDatas().get(
                                    position - 1).ProductId);
                        }
                    });
            WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
            mPullToRefreshListView.setOnRefreshListener(this);
            // 设置 end-of-list监听
            mPullToRefreshListView
                    .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                        @Override
                        public void onLastItemVisible() {
                            if(mCurrentPage>=totalCount){
                                WWToast.showShort(R.string.nomore_data);
                            }else{
                                getData();
                            }
                        }
                    });
            collectAdapter = new GoodsCollectAdapter(getActivity());
            mPullToRefreshListView.setAdapter(collectAdapter);
        }
        // 分页数据
        private int mCurrentPage = 0;
        private int totalCount = 0;
        protected void getData() {
            RequestParams params = new RequestParams(ApiShop.Favorites());
            params.addBodyParameter("sessionId", WWApplication.getInstance()
                    .getSessionId());
            params.addBodyParameter("pageSize", 10 + "");
            params.addBodyParameter("pageIndex", mCurrentPage + "");

            x.http().get(params, new WWXCallBack("Favorites") {
                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    mCurrentPage++;
                    ArrayList<ShopProduct> list = (ArrayList<ShopProduct>) JSONArray.parseArray(
                            data.getString("Data"), ShopProduct.class);
                    totalCount =data.getIntValue("PageCount");
                        if (mCurrentPage > 1) {
                            collectAdapter.addDatas(list);
                        } else {
                            collectAdapter.setDatas(list);
                           getActivity().sendBroadcast(new Intent("collect_num").putExtra(Consts.KEY_DATA,data.getString("TotalCount")).putExtra("isgoods",true));
                        }

                }

                @Override
                public void onAfterSuccessError(JSONObject data) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAfterFinished() {
                    mPullToRefreshListView.onRefreshComplete();

                }
            });

        }
        @Override
        public void onClick(View v) {

        }

        @Override
        public void onResume() {
            super.onResume();
            if(isRefresh){
                isRefresh=false;
                mCurrentPage = 0;
                getData();
            }
        }

        @Override
        public void onRefresh(PullToRefreshBase<ListView> refreshView) {
            mCurrentPage = 0;
            getData();
        }
    }

