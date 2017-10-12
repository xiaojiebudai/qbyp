package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.OfflineOrder;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by ppsher on 2017/2/13.
 */

public class OfflineOrderAdapter extends FatherAdapter<OfflineOrder> {
    public OfflineOrderAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.offline_order_item,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_order_num;
        private TextView tv_order_time;
        private TextView tv_shop_name;
        private TextView tv_consume_money;
        private TextView tv_give_integral;
        private ImageView iv_seller_shop_img;

        public ViewHolder(View convertView) {
            tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
            tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
            tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            tv_consume_money = (TextView) convertView.findViewById(R.id.tv_consume_money);
            tv_give_integral = (TextView) convertView.findViewById(R.id.tv_give_integral);
            iv_seller_shop_img = (ImageView) convertView.findViewById(R.id.iv_seller_shop_img);
            convertView.setTag(this);

        }

        public void setData(final int position) {
            OfflineOrder offlineOrder = getItem(position);
            tv_order_num.setText("" + offlineOrder.OrderId);
            tv_order_time.setText(TimeUtil.getTimeToM(offlineOrder.CreateTime * 1000));
            tv_shop_name.setText(offlineOrder.SellerName);
            tv_consume_money.setText(WWViewUtil.numberFormatPrice(offlineOrder.OrderAmt));
            tv_give_integral.setText(offlineOrder.PersentIntegral);
            ImageUtils.setCircleImage(mContext, offlineOrder.SellerHeadUrl, iv_seller_shop_img);
        }
    }
}
