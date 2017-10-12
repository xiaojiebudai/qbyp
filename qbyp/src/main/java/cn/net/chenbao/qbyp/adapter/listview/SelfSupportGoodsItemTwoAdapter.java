package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;

/**
 * Created by 木头 on 2016/11/1.
 */

public class SelfSupportGoodsItemTwoAdapter extends FatherAdapter<ShopProduct> {

    public SelfSupportGoodsItemTwoAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public int getCount() {

        if (getDatas().size() % 2 > 0) {
            return getDatas().size() / 2 + 1;
        } else {
            return getDatas().size() / 2;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_selfsupport_goods_item_two,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_good_img, iv_good_img1;
        public LinearLayout ll_goods, ll_goods1;
        public TextView tv_good_name, tv_good_price, tv_good_name1, tv_good_price1;
        int widthImg= (DensityUtil.getScreenWidth(mContext)-DensityUtil.dip2px(mContext,15))/2;

        public ViewHolder(View convertView) {
            ll_goods = (LinearLayout) convertView.findViewById(R.id.ll_goods);
            ll_goods1 = (LinearLayout) convertView.findViewById(R.id.ll_goods1);
            iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
            iv_good_img1 = (ImageView) convertView.findViewById(R.id.iv_good_img1);
            tv_good_name = (TextView) convertView.findViewById(R.id.tv_good_name);
            tv_good_price = (TextView) convertView.findViewById(R.id.tv_good_price);
            tv_good_name1 = (TextView) convertView.findViewById(R.id.tv_good_name1);
            tv_good_price1 = (TextView) convertView.findViewById(R.id.tv_good_price1);

            ViewGroup.LayoutParams layoutParams=iv_good_img.getLayoutParams();
            layoutParams.height=widthImg;
            layoutParams.width=widthImg;
            iv_good_img.setLayoutParams(layoutParams); ViewGroup.LayoutParams layoutParams1=iv_good_img1.getLayoutParams();
            layoutParams1.height=widthImg;
            layoutParams1.width=widthImg;
            iv_good_img1.setLayoutParams(layoutParams1);


            convertView.setTag(this);
        }

        public void setData(int position) {
            if (position * 2 + 1 < getDatas().size()) {
                //双
                final ShopProduct shopProduct = getDatas().get(position * 2);
                final ShopProduct shopProduct1 = getDatas().get(position * 2 + 1);
                ll_goods1.setVisibility(View.VISIBLE);
                ImageUtils.setCommonImage(mContext, DensityUtil.getRightImgScreen(shopProduct.ImageUrl,widthImg,widthImg), iv_good_img);

                WWViewUtil.textInsertDrawable(mContext, tv_good_name, shopProduct.ProductName, false, shopProduct.IsVipLevel);


                tv_good_price.setText("￥" + WWViewUtil.numberFormatWithTwo(shopProduct.SalePrice));
                ImageUtils.setCommonImage(mContext, DensityUtil.getRightImgScreen(shopProduct1.ImageUrl,widthImg,widthImg), iv_good_img1);
                WWViewUtil.textInsertDrawable(mContext, tv_good_name1, shopProduct1.ProductName, false, shopProduct1.IsVipLevel);
                tv_good_price1.setText("￥" + WWViewUtil.numberFormatWithTwo(shopProduct1.SalePrice));
                ll_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext, shopProduct.ProductId);
                    }
                });
                ll_goods1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext, shopProduct1.ProductId);
                    }
                });
            } else {
                //单
                ll_goods1.setVisibility(View.GONE);
                final ShopProduct shopProduct = getDatas().get(position * 2);
                ImageUtils.setCommonImage(mContext,  DensityUtil.getRightImgScreen(shopProduct.ImageUrl,widthImg,widthImg), iv_good_img);
                WWViewUtil.textInsertDrawable(mContext, tv_good_name, shopProduct.ProductName, false, shopProduct.IsVipLevel);
                tv_good_price.setText("￥" + WWViewUtil.numberFormatWithTwo(shopProduct.SalePrice));
                ll_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext, shopProduct.ProductId);
                    }
                });
            }
        }

    }
}
