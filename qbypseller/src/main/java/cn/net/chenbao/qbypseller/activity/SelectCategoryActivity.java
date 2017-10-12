package cn.net.chenbao.qbypseller.activity;

import java.util.List;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.CategoryUpdateAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;

/**
 * 选择分类
 *
 * @author xl
 * @date:2016-8-5上午11:22:43
 * @description 避免以后改动, 不和添加类目用一个界面控制
 */
public class SelectCategoryActivity extends FatherActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.act_select_category;
    }

    private CategoryUpdateAdapter mAdapter;

    @Override
    protected void initValues() {
        initDefautHead(R.string.select_category, true);
    }

    @Override
    protected void initView() {
        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_categories);
        mAdapter = new CategoryUpdateAdapter(this,
                CategoryUpdateAdapter.MODE_SELECT);
        pullToRefreshListView.setAdapter(mAdapter);
        pullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position != (mAdapter.getCount())) {
                    Intent data = new Intent();
                    data.putExtra(Consts.KEY_DATA,
                            JSON.toJSONString(mAdapter.getItem(position - 1)));
                    if (beforeCount < mAdapter.getCount()) {// 有新增
                        // TODO:可以选择存SP拿SP
                        data.putExtra(Consts.KEY_DATA_TWO,
                                JSONArray.toJSONString(mAdapter.getDatas()));
                    }
                    setResult(RESULT_OK, data);
                    finish();
                }

            }
        });
    }

    @Override
    protected void doOperate() {
        requestData();
    }

    /**
     * 之前的类目数
     */
    private int beforeCount;

    private void requestData() {
        RequestParams params = new RequestParams(ApiSeller.classGet());
        params.addQueryStringParameter(Api.KEY_SELLERID,
                WWApplication.getSellerId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("ClassGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray array = data.getJSONArray(Api.KEY_DATA);
                if (array != null) {
                    List<Category> list = JSON.parseArray(array.toJSONString(),
                            Category.class);
                    if (list != null) {
                        beforeCount = list.size();
                    }
                    mAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }
}
