package cn.net.chenbao.qbypseller.activity;

import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiTrade;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.dialog.PresentIntegralDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;

/**
 * 订单详情
 *
 * @author xl
 * @date 2016-8-3 下午9:47:39
 * @description
 */
public class OrderDetailsActivity extends FatherActivity {

    private String orderId;
//    private PresentIntegralDialog dialog;

    @Override
    protected int getLayoutId() {
        return R.layout.act_order_details;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order_details, true);
        orderId = getIntent().getStringExtra(Consts.KEY_DATA);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void doOperate() {
        requestData();
    }

    private Order mOrder;

    private void requestData() {
        showWaitDialog();
        RequestParams params = ParamsUtils.getSessionParams(ApiTrade
                .orderInfo());
        params.addQueryStringParameter("orderId", orderId);
        x.http().get(params, new WWXCallBack("OrderInfo") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mOrder = JSON.parseObject(data.getJSONObject(Api.KEY_DATA)
                        .toJSONString(), Order.class);
                setDataToUi();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();

            }
        });
    }

    private void setDataToUi() {
        if (mOrder != null) {
            TextView tv_order_account = (TextView) findViewById(R.id.tv_order_account);
            TextView tv_state = (TextView) findViewById(R.id.tv_state);
            TextView tv_order_number = (TextView) findViewById(R.id.tv_order_number);
            TextView tv_pay_way = (TextView) findViewById(R.id.tv_pay_way);
            TextView tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
            TextView tv_receive_people = (TextView) findViewById(R.id.tv_receive_people);
            TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
            TextView tv_address = (TextView) findViewById(R.id.tv_address);
            TextView tv_send_time = (TextView) findViewById(R.id.tv_send_time);
            TextView tv_des = (TextView) findViewById(R.id.tv_des);
            TextView tv_total_money = (TextView) findViewById(R.id.tv_total_money);
            TextView tv_ship_fee = (TextView) findViewById(R.id.tv_ship_fee);
            LinearLayout ll_goods = (LinearLayout) findViewById(R.id.ll_goods_list);// 商品数据
            TextView tv_operation = (TextView) findViewById(R.id.tv_operation);// 商品数据
            TextView tv_peisongtype = (TextView) findViewById(R.id.tv_peisongtype);// 配送方式
            LinearLayout ll_jifen = (LinearLayout) findViewById(R.id.ll_jifen);// 积分 赠送
            TextView tv_znegsongjifen = (TextView) findViewById(R.id.tv_znegsongjifen);// 赠送积分
            TextView tv_rangli = (TextView) findViewById(R.id.tv_rangli);// 让利
            TextView tv_seller_get = (TextView) findViewById(R.id.tv_seller_get);// 商家获赠积分
            TextView tv_create_time = (TextView) findViewById(R.id.tv_create_time);// 下单时间
            TextView tv_seller_take_time = (TextView) findViewById(R.id.tv_seller_take_time);//接单时间
            TextView tv_send_time_title = (TextView) findViewById(R.id.tv_send_time_title);//配送时间
            if (mOrder.Goods != null) {
                for (int i = 0; i < mOrder.Goods.size(); i++) {
                    View view = getLayoutInflater().inflate(
                            R.layout.item_goods, null);
                    Goods goods = mOrder.Goods.get(i);
                    ((TextView) view.findViewById(R.id.tv_name))
                            .setText(goods.GoodsName);
                    ((TextView) view.findViewById(R.id.tv_number)).setText("x"
                            + goods.Quantity);
                    ((TextView) view.findViewById(R.id.tv_money))
                            .setText("¥" + goods.Price + "");
                    ImageUtils.setCommonImage(this, goods.GoodsImg,
                            (ImageView) view.findViewById(R.id.iv_image));
                    ll_goods.addView(view);
                }
            }
            tv_state.setText(Order.getStateString(mOrder.Status));
            tv_order_account.setText(mOrder.UserName);
            tv_order_number.setText(mOrder.OrderId + "");
            tv_create_time.setText(TimeUtil.getTimeToM(mOrder.CreateTime * 1000));
            if (mOrder.Status > Order.STATE_WAIT_PAY) {
                tv_pay_time.setText(TimeUtil.getTimeToM(mOrder.PayTime * 1000));
            }
            if (mOrder.Status > Order.STATE_WAIT_SELLER_CONFIRM) {
                tv_seller_take_time.setText(TimeUtil.getTimeToM(mOrder.SellerConfirmTime * 1000));
            }
            tv_send_time.setText(mOrder.SendTime);
            tv_receive_people.setText(mOrder.ReceiverName);
            tv_phone.setText(mOrder.Mobile);
            tv_address.setText(mOrder.Address);

            tv_pay_way.setText(Order.getPayWayString(mOrder.PayCode));
            tv_des.setText(mOrder.UserExplain);
            tv_total_money.setText("¥" + mOrder.TotalAmt);
            tv_ship_fee.setText(getString(R.string.format_ship_fee,
                    mOrder.DeliverFee + ""));
            if (mOrder.SendMode == Order.SEND_MODE_STORE) {
                tv_peisongtype.setText(R.string.at_store);
                tv_send_time_title.setText(R.string.go_shop_time_colon);
            } else {
                tv_peisongtype.setText(R.string.delivery);
                tv_send_time_title.setText(R.string.peisong_time_colon);
            }
//            //积分相关
//            if (mOrder.PersentIntegral > 0) {
//                ll_jifen.setVisibility(View.VISIBLE);
//                tv_znegsongjifen.setText(WWViewUtil.numberFormatWithTwo(mOrder.PersentIntegral) + " ( " + WWViewUtil.numberFormatWithTwo(mOrder.PersentIntegralRate * 100) + "% )");
//                tv_rangli.setText("￥" + WWViewUtil.numberFormatWithTwo(mOrder.SellerRlAmt));
//                tv_seller_get.setText(WWViewUtil.numberFormatWithTwo(mOrder.SellerIntegral));
//            }
            switch (mOrder.Status) {
                case Order.STATE_WAIT_SELLER_CONFIRM:
                    tv_operation.setVisibility(View.VISIBLE);
                    tv_operation.setText(R.string.confirm_order);// 确认接单
                    tv_operation.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
//                            if (mOrder.PayAmt > 0) {
//                                if (dialog == null) {
//                                    dialog = new PresentIntegralDialog(OrderDetailsActivity.this, R.style.DialogStyle);
//                                    dialog.getTv_ok().setOnClickListener(new View.OnClickListener() {
//
//                                        @Override
//                                        public void onClick(View v) {
//                                            double bili = dialog.getBili();
//                                            requestConfirm(0.1);
//                                        }
//                                    });
//                                }
//                                dialog.setNum(mOrder.PayAmt);
//                                dialog.show();
//                            } else {
                                //不需要赠送积分
                                requestConfirm(0);
//                            }

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    private void requestConfirm(double bili) {
        JSONObject object = new JSONObject();
        object.put("orderId", orderId);
        object.put("integralRate", bili);
        RequestParams params = ParamsUtils.getPostJsonParamsWithSession(object,
                ApiTrade.orderSellerAccept());
        showWaitDialog();
        x.http().post(params, new WWXCallBack("OrderSellerAccept") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getBooleanValue(Api.KEY_DATA)) {
                    WWToast.showCostomSuccess(R.string.get_order_success);
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }
}
