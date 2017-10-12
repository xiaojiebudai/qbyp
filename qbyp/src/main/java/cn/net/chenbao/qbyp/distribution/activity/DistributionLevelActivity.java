package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.MainActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionLevelAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionLevel;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销等级
 */

public class DistributionLevelActivity extends FatherActivity {
    @BindView(R.id.listview_datas)
    PullToRefreshListView listviewDatas;
    private DistributionLevelAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_level;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.distribution_level, true);
    }

    @Override
    protected void initView() {
        adapter=new DistributionLevelAdapter(this);

        final View emptyView = WWViewUtil.setEmptyView(listviewDatas.getRefreshableView(), R.layout.emptyview_imgbg,R.drawable.empty_grade_icon,"您还没有购买过商品");
        TextView tv_go = ((TextView) emptyView.findViewById(R.id.tv_go));
        tv_go.setVisibility(View.VISIBLE);
        tv_go.setText(R.string.go_everywhere);
        tv_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DistributionLevelActivity.this, MainActivity.class);
                intent.putExtra("fenxiaoSelect", true);
                startActivity(intent);
            }
        });

        listviewDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PublicWay.startDistributionGoodsDetailActivity(DistributionLevelActivity.this,adapter.getDatas().get(position-1).ProductId);
            }
        });
        listviewDatas.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                requestData();
            }
        });
        listviewDatas.setAdapter(adapter);
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiDistribution.MyAgent());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());


        x.http().get(params, new WWXCallBack("MyAgent") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {

                ArrayList<DistributionLevel> list = (ArrayList<DistributionLevel>) JSONArray.parseArray(
                        data.getString("Data"), DistributionLevel.class);

                    adapter.setDatas(list);

            }

            @Override
            public void onAfterFinished() {
                listviewDatas.onRefreshComplete();
                dismissWaitDialog();
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
