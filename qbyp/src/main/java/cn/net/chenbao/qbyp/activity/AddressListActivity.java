package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.AddressAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/***
 * Description:收货地址列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-30
 */
public class AddressListActivity extends FatherActivity implements
        OnClickListener, OnRefreshListener<ListView> {
    private PullToRefreshListView mPullToRefreshListView;
    private AddressAdapter mAdapter;
    private int model;
    /**
     * 展示
     */
    public final static int DISPLAY = 0;
    /**
     * 选择
     */
    public final static int SELECT = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.act_address_list;
    }

    @Override
    protected void initValues() {
        mAdapter = new AddressAdapter(this);
        model = getIntent().getIntExtra(Consts.KEY_MODULE, DISPLAY);
        TextView center = (TextView) findViewById(R.id.tv_head_center);
        if (center != null) {
            center.setText(R.string.my_address);
            center.setTextColor(getResources().getColor(R.color.white));
            center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        View left = findViewById(R.id.rl_head_left);
        if (left != null) {
            left.findViewById(R.id.tv_head_left).setBackgroundResource(
                    R.drawable.arrow_back);
            left.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (model == SELECT) {
                        setResult(RESULT_OK);
                    }
                    back();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (model == SELECT) {
            setResult(RESULT_OK);
        }
        super.onBackPressed();
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_save).setOnClickListener(this);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_datas);
        mPullToRefreshListView
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        if (model == SELECT) {
                            Intent intent = new Intent();
                            intent.putExtra(
                                    Consts.KEY_DATA,
                                    JSON.toJSONString(mAdapter.getDatas().get(
                                            position - 1)));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
        mPullToRefreshListView.setOnRefreshListener(this);
        WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
        // 设置 end-of-list监听
        mPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getData();
                        }

                    }
                });
        mAdapter = new AddressAdapter(AddressListActivity.this);
        mPullToRefreshListView.setAdapter(mAdapter);
    }

    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;

    protected void getData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getAddressGet());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 10 + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack("AddressGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<Address> list = (ArrayList<Address>) JSONArray.parseArray(
                        data.getString("Data"), Address.class);
                totalCount = data.getIntValue("PageCount");
                if (mCurrentPage > 1) {
                    mAdapter.addDatas(list);
                } else {
                    mAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                mPullToRefreshListView.onRefreshComplete();
                dismissWaitDialog();
            }
        });

    }

    @Override
    protected void doOperate() {
        getData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                PublicWay.startAddAddressActivity(AddressListActivity.this, 888,
                        AddAddressActivity.ADD, null);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 888 && arg1 == RESULT_OK) {
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
