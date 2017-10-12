package cn.net.chenbao.qbypseller.activity;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.CategoryUpdateAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.dialog.EditCategoryDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbypseller.R;

/**
 * 添加分类
 *
 * @author xl
 * @date 2016-7-30 下午12:34:33
 * @description
 */
public class AddCategoryActivity extends FatherActivity implements
        OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.act_add_category;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.add_category, true);

    }

    private CategoryUpdateAdapter mAdapter;

    @Override
    protected void initView() {
        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_categories);
        mAdapter = new CategoryUpdateAdapter(this);
        pullToRefreshListView.setAdapter(mAdapter);
        findViewById(R.id.tv_add_category).setOnClickListener(this);
    }

    @Override
    protected void doOperate() {

        requestData();
    }

    private void requestData() {
        RequestParams params = new RequestParams(ApiSeller.classGet());
        params.addQueryStringParameter(Api.KEY_SELLERID,
                WWApplication.getSellerId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("ClassGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ZLog.showPost(data.toJSONString());
                JSONArray array = data.getJSONArray(Api.KEY_DATA);
                if (array != null) {
                    List<Category> list = JSON.parseArray(array.toJSONString(),
                            Category.class);
                    mAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    private EditCategoryDialog mDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_category:
                if (mDialog == null) {
                    mDialog = new EditCategoryDialog(this);
                    ((TextView) mDialog.findViewById(R.id.tv_title))
                            .setText(R.string.add_category);
                    final EditCategoryDialog.DialogHolder holder = mDialog.getTag();
                    holder.content.setHint(R.string.please_input_class_name);
                    holder.right.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            requestAdd(holder);
                        }
                    });
                }
                mDialog.show();
                break;

            default:
                break;
        }
    }

    /**
     * 新增
     */
    private ArrayList<Category> addList;

    /**
     * 添加
     */
    private void requestAdd(final EditCategoryDialog.DialogHolder holder) {
        String s = holder.content.getText().toString().trim();
        if (TextUtils.isEmpty(s)) {
            return;
        }
        showWaitDialog();
        JSONObject object = new JSONObject();
        Category category = new Category();
        category.ClassName = s;
        object.put(Api.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        object.put("data", category);
        RequestParams params = ParamsUtils.getPostJsonParams(object,
                ApiSeller.classUpdate());
        x.http().post(params,
                new WWXCallBack("ClassUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {

                        Category new_c = JSON
                                .parseObject(data.getJSONObject(Api.KEY_DATA)
                                        .toJSONString(), Category.class);
                        if (addList == null) {
                            addList = new ArrayList<Category>();
                        }
                        addList.add(new_c);
                        mAdapter.addItem(new_c);
                        holder.content.setText("");
                        mDialog.dismiss();
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(Consts.KEY_DATA,
                JSONArray.toJSONString(mAdapter.getDatas()));// 之前是把新增的返回,可能有删除,直接返回所有类目
        setResult(RESULT_OK, intent);
        super.finish();

    }

}
