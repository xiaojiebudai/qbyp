package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.view.RatingBar;

/**
 * 商家信息适配器
 *
 * @author xl
 * @date 2016-7-26 下午10:14:13
 * @description
 */
public class BussinessAdapter extends FatherAdapter<TradesMessage> {

    public BussinessAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.listview_bussiness_item, null);
            holder = new ViewHolder(convertView);
        } else
            holder = (ViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {

        public TextView distance;
        public TextView tv_name;
        public TextView tv_fen;
        public ImageView iv_pic;
        public RatingBar rr_comm;
        public TextView tv_sales;//已售多少单

        public ViewHolder(View convertView) {
            distance = (TextView) convertView.findViewById(R.id.tv_distance);
            iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_fen = (TextView) convertView.findViewById(R.id.tv_fen);
            tv_sales = (TextView) convertView.findViewById(R.id.tv_sales);
            rr_comm = (RatingBar) convertView.findViewById(R.id.rb);
            convertView.setTag(this);
        }

        public void setData(TradesMessage message) {
            tv_name.setText(message.SellerName);
            distance.setText(message.Distance < 1000 ? message.Distance + "m"
                    : message.Distance / 1000 + "km");
            ImageUtils.setCommonRadiusImage(mContext, message.ShopPicture, iv_pic);
            double star = Arith.round(message.JudgeLevel, 1);
            tv_sales.setText(String.format(mContext.getString(R.string.have_sale_number), message.OrderCount));
            rr_comm.setStar((float) star);
            tv_fen.setText(Arith.round(message.JudgeLevel, 1) + "");
        }

    }
}
