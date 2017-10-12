package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.fragment.OrderFragment;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.TimeUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 订单适配器
 *
 * @author xl
 * @date 2016-7-31 下午5:24:31
 * @description
 */
public class OrderAdapter extends FatherAdapter<Order> {

    private OrderFragment fragment;

    public OrderAdapter(Context ctx, OrderFragment fragment) {
        super(ctx);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_order_item,
                    parent, false);
            new ViewHolder(convertView);

        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        final Order item = getItem(position);
        if (item != null) {
            ImageUtils.setCommonImage(mContext, item.GoodsImg, holder.iv_image);
            holder.tv_money.setText(mContext
                    .getString(R.string.total_with_colon) + item.TotalAmt);
            holder.tv_time.setText(mContext.getString(
                    R.string.format_order_time,
                    TimeUtil.getTimeToM(item.CreateTime * 1000)));
            if (item.GoodsName.length() > 10) {
                item.GoodsName = item.GoodsName.substring(0, 10)+"...";
            }
            String orderInfoStr = (item.Quantity == 1 ? item.GoodsName
                    : item.GoodsName + "...");
            holder.tv_info
                    .setText(mContext
                            .getString(R.string.format_order_info,
                                    orderInfoStr,
                                    item.Quantity + ""));
            int stateString = R.string.empty;
            switch (item.Status) {
                case Order.STATE_WAIT_SELLER_CONFIRM:// 待接单
                    stateString = R.string.wait_get;
                    break;
                case Order.STATE_WAIT_PAY:// 代付款
                    stateString = R.string.wait_pay;
                    break;
                case Order.STATE_WAIT_BUYER_CONFIRM:// 待收貨
                    stateString = R.string.wait_receive;
                    break;
                case Order.STATE_REFUND:// 退款中
                    stateString = R.string.refunding;
                    break;
                case Order.STATE_CANCEL:// 取消
                    stateString = R.string.cancel;
                    break;
                case Order.STATE_COMPLETE:// 完成
                    stateString = R.string.finish;
                    break;
                case Order.STATE_CLOSE:// 已关闭
                    stateString = R.string.closed;
                    break;
                default:
                    break;
            }
            holder.tv_state.setText(stateString);
        }
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PublicWay.startOrderDetailsActivity(fragment,
                        item.OrderId + "", position,
                        OrderFragment.REQUEST_CODE_DETAILS);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_image;
        TextView tv_info;
        TextView tv_time;
        TextView tv_state;
        TextView tv_money;

        public ViewHolder(View convertView) {
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_info = (TextView) convertView.findViewById(R.id.tv_order_info);
            tv_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            tv_state = (TextView) convertView.findViewById(R.id.tv_order_state);
            tv_money = (TextView) convertView.findViewById(R.id.tv_order_money);
            convertView.setTag(this);
        }

    }
}
