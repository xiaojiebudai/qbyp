package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.activity.MainActivity;
import cn.net.chenbao.qbyp.utils.PublicWay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ppsher on 2017/4/17.
 * Description:线下支付成功
 */

public class DistributionCertSuccessActivity extends FatherActivity {
    @BindView(R.id.tv_tip)
    TextView tvTip;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_cert_success;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.commit_ok, false);
        View left = findViewById(R.id.rl_head_left);
        if (left != null) {
            left.findViewById(R.id.tv_head_left).setBackgroundResource(
                    R.drawable.arrow_back);
            left.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //跳转进货单list
                    PublicWay.startDistributionOrderActivity(DistributionCertSuccessActivity.this, DistributionOrderActivity.IN);
                    finish();
                }
            });
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {
        String str = getResources().getString(R.string.pay_certificate_tip);
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.yellow_ww)), str.length() - 13, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvTip.setText(style);
    }

    @OnClick({R.id.btn_go_person_center, R.id.btn_go_order_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_go_person_center:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("personCenterSelect", true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_go_order_list:
                //跳转进货单list
                PublicWay.startDistributionOrderActivity(this, DistributionOrderActivity.IN);
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
