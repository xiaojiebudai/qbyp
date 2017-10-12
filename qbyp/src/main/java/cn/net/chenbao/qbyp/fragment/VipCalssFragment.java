package cn.net.chenbao.qbyp.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.view.LoadMoreRecyclerView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.MyRecyclerViewAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProduct;

/**
 * Created by wuri on 2016/12/19.
 */

public class VipCalssFragment extends FatherFragment {
    LinearLayoutManager mLayoutManager;
    private LoadMoreRecyclerView recycler_view;
    private View view;
    private MyRecyclerViewAdapter mAdapter;
    private String classId;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private int type = 0;

    private static VipCalssFragment instance = null;

    public static VipCalssFragment newInstance() {
        return new VipCalssFragment();
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recycleview_data;
    }

    @Override
    protected void initView() {
        view = mGroup.findViewById(R.id.fl_empty);
        recycler_view = (LoadMoreRecyclerView) mGroup.findViewById(R.id.recycler_view);

        recycler_view.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                type = 1;
                getGoodsData();
            }
        });

        recycler_view.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MyRecyclerViewAdapter(getActivity());
        getGoodsData();
    }

    /**
     * 获取商品列表数据
     */
    private void getGoodsData() {
        RequestParams requestParams = new RequestParams(ApiShop.ProductVip());
        requestParams.addBodyParameter("pageIndex", mCurrentPage
                + "");
        requestParams.addBodyParameter("classId", classId
        );
        x.http().get(requestParams, new WWXCallBack("ProductVip") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        mCurrentPage++;
                        ArrayList<ShopProduct> list = (ArrayList<ShopProduct>) JSONArray.parseArray(
                                data.getString("Data"), ShopProduct.class);
                        totalCount = data.getIntValue("PageCount");
                        if (mCurrentPage == 1 && list.size() == 0) {
                            view.setVisibility(View.VISIBLE);
                        } else {
                            view.setVisibility(View.GONE);
                        }
                        if (mCurrentPage > 1) {
                            mAdapter.addDatas(list);
                        } else {
                            mAdapter.setDatas(list);
                        }
                        mAdapter.setHasMore(mCurrentPage <= totalCount);
                        recycler_view.setAdapter(mAdapter);
                        if (type == 0) {
                            mAdapter.notifyDataSetChanged();
                        } else if (type == 1) {
                            recycler_view.notifyMoreFinish(true);
                        }
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                }

        );
    }
}
