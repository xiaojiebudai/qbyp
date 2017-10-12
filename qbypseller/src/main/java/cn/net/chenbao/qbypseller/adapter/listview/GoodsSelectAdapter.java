package cn.net.chenbao.qbypseller.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;

/**
 * Created by zxj on 2017/4/14.
 *
 * @description
 */

public class GoodsSelectAdapter extends FatherAdapter<Goods> {
    public GoodsSelectAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_select_goods_item,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = getItem(position);
        viewHolder.setData(goods);
        return convertView;
    }

    private class ViewHolder {
        public ImageView iv_image;
        public TextView tv_name;
        public TextView tv_sales;
        public TextView tv_money;
        public TextView tv_count;
        public LinearLayout ll_operate;
        public View iv_jia;
        public View iv_jian;

        public ViewHolder(View convertView) {
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_sales = (TextView) convertView.findViewById(R.id.tv_sales);
            tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ll_operate = (LinearLayout) convertView.findViewById(R.id.ll_operate);
            iv_jia = convertView.findViewById(R.id.iv_jia);
            iv_jian = convertView.findViewById(R.id.iv_jian);
            convertView.setTag(this);
        }

        public void setData(final Goods goods) {
            ImageUtils.setOwnRadiusImage(mContext, ImageUtils.getRightImgScreen(goods.GoodsImg, DensityUtil.dip2px(mContext, 54), DensityUtil.dip2px(mContext, 54)), iv_image, 3);
            tv_name.setText(goods.GoodsName);

            if (goods.Quantity > 0) {
                iv_jian.setVisibility(View.VISIBLE);
                tv_count.setVisibility(View.VISIBLE);
            } else {
                iv_jian.setVisibility(View.GONE);
                tv_count.setVisibility(View.GONE);
            }
            ll_operate.setVisibility(View.VISIBLE);
            tv_count.setText(goods.Quantity + "");

            tv_sales.setText(mContext.getResources().getString(
                    R.string.seller_num_with_colon)
                    + goods.SaleQty);
            tv_money.setText(WWViewUtil.numberFormatPrice(goods.Price));
            iv_jia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(TimeUtil.isFastClick()){
//                        WWToast.showShort(R.string.operation_too_fast);
//                    }else{
                    if (goods.Quantity >= goods.StockQty) {
                        WWToast.showShort(R.string.buynum_dayu_kucun);
                        return;
                    }
                    goods.Quantity++;
                    priceListener.refreshGoodsListener(goods);
                    notifyDataSetChanged();
                }
            });
            iv_jian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(TimeUtil.isFastClick()){
//                        WWToast.showShort(R.string.operation_too_fast);
//                    }else{
                    if (goods.Quantity == 0) {
                        WWToast.showShort(R.string.buynum_dayu_ling);
                        return;
                    }
                    goods.Quantity--;
                    priceListener.refreshGoodsListener(goods);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private PriceCallBack priceListener;

    public void setPriceListener(PriceCallBack priceListener) {
        this.priceListener = priceListener;
    }

    public interface PriceCallBack {
        void refreshGoodsListener(Goods goods);

    }
}

