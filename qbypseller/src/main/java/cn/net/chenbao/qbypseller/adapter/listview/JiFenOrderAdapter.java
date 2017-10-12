
package cn.net.chenbao.qbypseller.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;

import cn.net.chenbao.qbypseller.R;

import cn.net.chenbao.qbypseller.bean.JiFen;
import cn.net.chenbao.qbypseller.bean.Order;
import cn.net.chenbao.qbypseller.utils.TimeUtil;

/***
 * Description:线下订单 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-4
 */
public class JiFenOrderAdapter extends FatherAdapter<JiFen> {

    public JiFenOrderAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_jifenorder_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_orderid;
        public TextView tv_ordertime;
        public TextView tv_xiaofei;
        public TextView tv_zengsongjifen;
        public TextView tv_rangli;
        public TextView tv_pay_way;
        public ImageView iv_head;
        public TextView tv_name;
        JiFen item;
        int positionNow;

        public ViewHolder(View convertView) {
            tv_orderid = (TextView) convertView
                    .findViewById(R.id.tv_orderid);
            tv_ordertime = (TextView) convertView
                    .findViewById(R.id.tv_ordertime);
            tv_xiaofei = (TextView) convertView.findViewById(R.id.tv_xiaofei);
            tv_zengsongjifen = (TextView) convertView.findViewById(R.id.tv_zengsongjifen);
            tv_rangli = (TextView) convertView.findViewById(R.id.tv_rangli);
            tv_pay_way = (TextView) convertView.findViewById(R.id.tv_pay_way);
            iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(this);
        }

        public void setData(int Position) {
            positionNow = Position;
            item = getItem(Position);
            tv_orderid.setText(String.format(mContext.getString(R.string.order_d), item.OrderId));
            tv_ordertime.setText(TimeUtil.getTimeToM(item.CreateTime * 1000));
            tv_xiaofei.setText(String.format(mContext.getString(R.string.consume), WWViewUtil.numberFormatWithTwo(item.OrderAmt)));
            tv_zengsongjifen.setText(String.format(mContext.getString(R.string.give_integral_s), item.PersentIntegral));
            tv_rangli.setText(String.format(mContext.getString(R.string.give), WWViewUtil.numberFormatWithTwo(item.SellerRlAmt)));
            tv_pay_way.setText(Order.getPayWayString(item.PayCode));
            tv_name.setText(item.UserName);
            ImageUtils.setCircleHeaderImage(mContext, item.UserHeadUrl, iv_head);
        }


    }

}

