package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderActivity;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.utils.WWViewUtil;

import cn.net.chenbao.qbyp.R;

/**
 * 支付结果页面
 *
 * @author licheng
 */
public class PayResultActivity extends FatherActivity implements
        OnClickListener {

    private TextView mTvOrderNum;
    private TextView mTvPrice;
    private TextView tv_check_order;
    private TextView tv_check_order_list;
    public static final int SUCCESS = 1;
    public static final int FAILE = 2;
    public int mode;
    private LinearLayout llFaile;
    private LinearLayout llSuccess;
    private long orderId;
    private String money;
    /**
     * 商品类型
     */
    private int goodType;
    public static final String GOODTYPE = "goodType";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @Override
    protected void initValues() {
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
        goodType = getIntent().getIntExtra(GOODTYPE, -1);
        switch (mode) {
            case SUCCESS:// 成功
                orderId = getIntent().getLongExtra("orderId", -1);
                money = getIntent().getStringExtra("money");
                break;
            case FAILE:// 失敗

                break;
        }
        initDefautHead(R.string.pay_result, false);
    }

    @Override
    protected void initView() {
        mTvOrderNum = (TextView) findViewById(R.id.tv_order_num);
        mTvPrice = (TextView) findViewById(R.id.tv_price);
        tv_check_order = (TextView) findViewById(R.id.tv_check_order);
        tv_check_order_list = (TextView) findViewById(R.id.tv_check_order_list);
        llFaile = (LinearLayout) findViewById(R.id.ll_fail);
        llSuccess = (LinearLayout) findViewById(R.id.ll_success);
        tv_check_order.setOnClickListener(this);
        tv_check_order_list.setOnClickListener(this);


        findViewById(R.id.tv_pay_again).setOnClickListener(this);
        findViewById(R.id.tv_check_order_list_fail).setOnClickListener(this);
        switch (goodType) {
            case PayOrdersActivity.LOCAL:
                break;
            case PayOrdersActivity.DISTRIBUTION:
                tv_check_order.setText(R.string.person_center);
                tv_check_order_list.setText(R.string.back_order_list);
                break;
            case PayOrdersActivity.SELF:
                tv_check_order.setText(R.string.home_page);
                tv_check_order_list.setText(R.string.back_order_list);
                break;
        }


    }

    @Override
    protected void doOperate() {
        switch (mode) {
            case SUCCESS:
                llSuccess.setVisibility(View.VISIBLE);
                mTvOrderNum.setText(orderId + "");
                mTvPrice.setText(WWViewUtil.numberFormatPrice(Double.valueOf(money)));
                break;
            case FAILE:
                llFaile.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_order:// 成功查看订单
                switch (goodType) {
                    case PayOrdersActivity.LOCAL:
                        PublicWay.startOrderDetailActivity(this, orderId);
                        break;
                    case PayOrdersActivity.DISTRIBUTION:
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("personCenterSelect", true);
                        startActivity(intent);
                        break;
                    case PayOrdersActivity.SELF:
                        //返回首页
                        startActivity(new Intent(this, MainActivity.class));
                        break;
                }
                break;
            case R.id.tv_check_order_list:// 成功返回订单列表

                setResult(RESULT_OK);

                switch (goodType) {
                    case PayOrdersActivity.LOCAL:
                        startActivity(new Intent(this, LocalLifeOrderActivity.class));
                        break;
                    case PayOrdersActivity.DISTRIBUTION:
                        //进货
                        PublicWay.startDistributionOrderActivity(this, DistributionOrderActivity.IN);
                        break;
                    case PayOrdersActivity.SELF:
                        //自营订单
                        startActivity(new Intent(this, SelfSupportOrderActivity2.class));
                        break;
                }
                finish();
                break;
            case R.id.tv_pay_again:// 失败重新支付
                finish();
                break;
            case R.id.tv_check_order_list_fail:// 失败查看列表
                setResult(RESULT_OK);

                switch (goodType) {
                    case PayOrdersActivity.LOCAL:
                        startActivity(new Intent(this, LocalLifeOrderActivity.class));
                        break;
                    case PayOrdersActivity.DISTRIBUTION:
                        //进货
                        PublicWay.startDistributionOrderActivity(this, DistributionOrderActivity.IN);
                        break;
                    case PayOrdersActivity.SELF:
                        //返回首页
                        startActivity(new Intent(this, SelfSupportOrderActivity2.class));
                        break;
                }
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        super.onBackPressed();
    }
}
