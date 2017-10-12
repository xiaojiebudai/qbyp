package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportPostAdapter;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.bean.ShopOrderLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;

/**
 * Created by wuri on 2016/11/3.
 * Description:物流订单跟踪界面
 */

public class SelfSupportFollowDeliverActivity extends FatherActivity {
    @BindView(R.id.lv_wuliu)
    ListView lvWuliu;
    @BindView(R.id.tv_post_state)
    TextView tvPostState;
    @BindView(R.id.tv_post_company)
    TextView tvPostCompany;
    @BindView(R.id.tv_post_number)
    TextView tvPostNumber;

    private SelfSupportPostAdapter postAdapter;
    private long orderId;

    private int model;
    /**
     * 自营
     */
    public static final int SELF = 0;
    /**
     * 分销
     */
    public static final int FENXIAO = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_wuliu;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order_logo, true);
        model=getIntent().getIntExtra(Consts.KEY_MODULE,SELF);
        Intent intent = getIntent();
        orderId = intent.getLongExtra(Consts.KEY_DATA, -1);
    }

    @Override
    protected void initView() {
        postAdapter = new SelfSupportPostAdapter(this);
        WWViewUtil.setEmptyViewNew(lvWuliu);
        lvWuliu.setAdapter(postAdapter);
    }

    @Override
    protected void doOperate() {
        requestData();
    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(model==SELF?ApiShop.OrderLog(): ApiDistribution.OrderLogs());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("orderId", orderId + "");
        x.http().get(params, new WWXCallBack(model==SELF?"OrderLog":"OrderLogs") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                ShopOrderLog shopOrderLog = JSONObject.parseObject(
                        data.getString("Data"), ShopOrderLog.class);
                tvPostState.setText(shopOrderLog.Status);
                tvPostCompany.setText(shopOrderLog.DeliverName);
                tvPostNumber.setText(shopOrderLog.LogisticsNo);
                postAdapter.setDatas(shopOrderLog.Logs);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }


}
