package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.ImageUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;

/**
 * 商品适配器
 *
 * @author xl
 * @date 2016-7-29 上午2:35:55
 * @description
 */
public class GoodsAdaper extends FatherAdapter<Goods> {

    public GoodsAdaper(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_goods_item,
                    parent, false);
            new ViewHolder(convertView);
        }
        Goods item = getItem(position);

        ViewHolder holder = (ViewHolder) convertView.getTag();
        ImageUtils.setCommonImage(mContext, ImageUtils.getRightImgScreen(item.GoodsImg, DensityUtil.dip2px(mContext,48),DensityUtil.dip2px(mContext,48)), holder.image);
        holder.name.setText(item.GoodsName);
        holder.money.setText(WWViewUtil.numberFormatWithTwo(item.Price));
        holder.sales.setText(mContext.getString(R.string.sales_volume) + item.SaleQty
        );
        return convertView;
    }

    private static class ViewHolder {

        TextView sales;
        TextView name;
        TextView money;
        ImageView image;

        public ViewHolder(View convertView) {
            sales = (TextView) convertView.findViewById(R.id.tv_sales);
            name = (TextView) convertView.findViewById(R.id.tv_name);
            money = (TextView) convertView.findViewById(R.id.tv_money);
            image = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(this);

        }

    }
}
