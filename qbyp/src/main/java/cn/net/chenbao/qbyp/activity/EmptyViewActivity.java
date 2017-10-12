package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.Consts;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zxj on 2017/4/20.
 *
 * @description 自定义空页面
 */

public class EmptyViewActivity extends FatherActivity {
    @BindView(R.id.iv_empty_bg)
    ImageView ivEmptyBg;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_go)
    TextView tvGo;

    private int model;

    /**
     * 分销销货单
     */
    public static final int DISTRIBUTION_ORDER = 0;


    @Override
    protected int getLayoutId() {
        return R.layout.act_empty_view;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
    }

    @Override
    protected void initView() {
        switch (model) {
            case DISTRIBUTION_ORDER:
                initDefautHead(R.string.distribution_order_out,true);
                ivEmptyBg.setImageResource(R.drawable.empty_salesorder_icon);
                tvInfo.setVisibility(View.VISIBLE);
                tvInfo.setText(R.string.no_permission_do_things);
                break;

        }
    }

    @Override
    protected void doOperate() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_go)
    public void onViewClicked() {
    }
}
