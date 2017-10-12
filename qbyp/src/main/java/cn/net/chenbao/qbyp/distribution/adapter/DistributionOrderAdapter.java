

package cn.net.chenbao.qbyp.distribution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderActivity;
import cn.net.chenbao.qbyp.distribution.been.DistributionOrder;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销等级
 */

public class DistributionOrderAdapter extends FatherAdapter<DistributionOrder> {
    private int model;

    public DistributionOrderAdapter(Context ctx, int model) {
        super(ctx);
        this.model = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.list_distribution_order_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_orderid, state, tv_time, tv_allprice, tv_order_operation_right, tv_order_operation_left;

        private LinearLayout ll_container, ll_operate;

        public ViewHolder(View convertView) {
            ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
            ll_operate = (LinearLayout) convertView.findViewById(R.id.ll_operate);
            tv_orderid = (TextView) convertView.findViewById(R.id.tv_orderid);
            state = (TextView) convertView.findViewById(R.id.state);

            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_allprice = (TextView) convertView.findViewById(R.id.tv_allprice);
            tv_order_operation_right = (TextView) convertView.findViewById(R.id.tv_order_operation_right);
            tv_order_operation_left = (TextView) convertView.findViewById(R.id.tv_order_operation_left);


            convertView.setTag(this);
        }

        public void setData(final DistributionOrder item) {
            tv_orderid.setText("订单号" + item.OrderId);
            state.setText(DistributionOrder.getStateString(item.Status));
            WWViewUtil.setDistributionGoodsView(mContext, ll_container, item.Goods,1);
            tv_time.setText(TimeUtil.getTimeToS(item.CreateTime*1000));
            tv_allprice.setText(WWViewUtil.numberFormatPrice(item.TotalAmt));

            switch (item.Status) {
                case DistributionOrder.STATE_WAIT_PAY:

                    if (model == DistributionOrderActivity.IN) {
                        ll_operate.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setVisibility(View.VISIBLE);
                        tv_order_operation_left.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setText(R.string.cancel_order);
                        tv_order_operation_left.setText(R.string.pay_soon);
                        tv_order_operation_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.cancelOrder(item);
                                }
                            }
                        });
                        tv_order_operation_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.payOrder(item);
                                }
                            }
                        });
                    } else {
                        ll_operate.setVisibility(View.GONE);

                    }


                    break;
                case DistributionOrder.STATE_WAIT_SEND:
                    if (model == DistributionOrderActivity.IN) {
                        ll_operate.setVisibility(View.GONE);
                    } else {
                        ll_operate.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setVisibility(View.VISIBLE);
                        tv_order_operation_left.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setText(R.string.no_goods);
                        tv_order_operation_left.setText(R.string.send_goods);
                        tv_order_operation_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.noGoodOrder(item);
                                }
                            }
                        });
                        tv_order_operation_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.sendOrder(item);
                                }
                            }
                        });
                    }
                    break;
                case DistributionOrder.STATE_WAIT_GET:
                    if (model == DistributionOrderActivity.IN) {
                        ll_operate.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setVisibility(View.VISIBLE);
                        tv_order_operation_left.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setText(R.string.look_post);
                        tv_order_operation_left.setText(R.string.make_suer_receiver_goods);
                        tv_order_operation_right.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.checkOrderLogs(item);
                                }
                            }
                        });
                        tv_order_operation_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.receivingOrder(item);
                                }
                            }
                        });
                    } else {
                        ll_operate.setVisibility(View.VISIBLE);
                        tv_order_operation_right.setVisibility(View.GONE);
                        tv_order_operation_left.setVisibility(View.VISIBLE);
                        tv_order_operation_left.setText(R.string.look_post);
                        tv_order_operation_left.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lisentner != null) {
                                    lisentner.checkOrderLogs(item);
                                }
                            }
                        });
                    }


                    break;
                case DistributionOrder.STATE_COMPLETE:
                    ll_operate.setVisibility(View.VISIBLE);
                    tv_order_operation_right.setVisibility(View.GONE);
                    tv_order_operation_left.setVisibility(View.VISIBLE);
                    tv_order_operation_left.setText(R.string.look_post);
                    tv_order_operation_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (lisentner != null) {
                                lisentner.checkOrderLogs(item);
                            }
                        }
                    });
                    break;
                case DistributionOrder.STATE_CANCEL:
                    ll_operate.setVisibility(View.GONE);
                    break;
                case DistributionOrder.STATE_CHECK:
                    ll_operate.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 确定按钮的回调
     */
    private OrderOperateCallback lisentner;

    public void setOrderOperateCallback(OrderOperateCallback lisentner) {
        this.lisentner = lisentner;
    }

    public interface OrderOperateCallback {
        /**
         * 取消订单
         *
         * @param distributionOrder
         */
        void cancelOrder(DistributionOrder distributionOrder);

        /**
         * 支付订单
         *
         * @param distributionOrder
         */
        void payOrder(DistributionOrder distributionOrder);

        /**
         * 查看物流
         *
         * @param distributionOrder
         */
        void checkOrderLogs(DistributionOrder distributionOrder);

        /**
         * 确认收货
         *
         * @param distributionOrder
         */
        void receivingOrder(DistributionOrder distributionOrder);

        /**
         * 我没货
         *
         * @param distributionOrder
         */
        void noGoodOrder(DistributionOrder distributionOrder);

        /**
         * 发货
         *
         * @param distributionOrder
         */
        void sendOrder(DistributionOrder distributionOrder);
    }
}

