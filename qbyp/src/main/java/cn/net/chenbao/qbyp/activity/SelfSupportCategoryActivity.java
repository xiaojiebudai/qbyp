package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportCategoryAdapter;
import cn.net.chenbao.qbyp.bean.ShopClasses;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城类目
 */

public class SelfSupportCategoryActivity extends FatherActivity implements View.OnClickListener {

    @BindView(R.id.lv_category)
    ListView lvCategory;
    @BindView(R.id.head_iv_back)
    RelativeLayout headIvBack;
    @BindView(R.id.tv_shopcart_count)
    TextView tvShopcartCount;
    @BindView(R.id.rl_head_right)
    RelativeLayout rlHeadRight;
    private SelfSupportCategoryAdapter selfSupportCategoryAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_category;
    }

    @Override
    protected void initValues() {
    }

    @Override
    protected void initView() {
        selfSupportCategoryAdapter = new SelfSupportCategoryAdapter(this);
        WWViewUtil.setEmptyViewNew(lvCategory);
        lvCategory.setAdapter(selfSupportCategoryAdapter);
    }

    @Override
    protected void doOperate() {
        requestData();
    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiShop.ClassTree());
        x.http().get(params, new WWXCallBack("ClassTree") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<ShopClasses> shopClassList = JSONObject.parseArray(
                        data.getString("Data"), ShopClasses.class);
                selfSupportCategoryAdapter.setDatas(shopClassList);
                selfSupportCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @OnClick({R.id.head_iv_back, R.id.rl_head_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_iv_back:
                finish();//到商城首页
                break;
            case R.id.rl_head_right:
                //跳转到购物车
                PublicWay.stratSelfSupportShopCartActivity(this);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WWApplication.getInstance().isLogin()) {
            int Quantity = SharedPreferenceUtils.getInstance().getCartNum();
            if (Quantity > 0) {
                tvShopcartCount.setVisibility(View.VISIBLE);
                if (Quantity > 99) {
                    tvShopcartCount.setText("99+");
                } else {
                    tvShopcartCount.setText(Quantity + "");
                }
            } else {
                tvShopcartCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
