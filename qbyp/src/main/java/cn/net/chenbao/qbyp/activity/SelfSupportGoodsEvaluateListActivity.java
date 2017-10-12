package cn.net.chenbao.qbyp.activity;

import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportGoodsEvaluateAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProductJudge;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 木头 on 2016/11/3.
 * 还差商品图片
 */

public class SelfSupportGoodsEvaluateListActivity extends FatherActivity implements PullToRefreshBase.OnRefreshListener<ListView> {
    @BindView(R.id.listview_datas)
    PullToRefreshListView listviewDatas;
    private SelfSupportGoodsEvaluateAdapter supportGoodsEvaluateAdapter;
    private long productId;
    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_evaluate_list;
    }

    @Override
    protected void initValues() {
        productId=getIntent().getLongExtra(Consts.KEY_DATA,0);

        initDefautHead(R.string.goods_evaluate, true);
    }

    @Override
    protected void initView() {
        listviewDatas.setOnRefreshListener(this);
        WWViewUtil.setEmptyView(listviewDatas.getRefreshableView());

        // 设置 end-of-list监听
        listviewDatas
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getData();
                        }

                    }
                });
        supportGoodsEvaluateAdapter = new SelfSupportGoodsEvaluateAdapter(this);
        listviewDatas.setAdapter(supportGoodsEvaluateAdapter);
    }

    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    protected void getData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.ProductJudge());
        params.addBodyParameter("productId", productId + "");
        params.addBodyParameter("pageSize", 10 + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack("ProductJudge") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<ShopProductJudge> list = (ArrayList<ShopProductJudge>) JSONArray.parseArray(
                        data.getString("Data"), ShopProductJudge.class);
                totalCount = data.getIntValue("PageCount");
                if (mCurrentPage > 1) {
                    supportGoodsEvaluateAdapter.addDatas(list);
                } else {
                    supportGoodsEvaluateAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                listviewDatas.onRefreshComplete();

            }
        });
    }

    @Override
    protected void doOperate() {
        getData();

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mCurrentPage = 0;
        getData();

    }
}
