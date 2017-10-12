package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import cn.net.chenbao.qbyp.R;


public class VipGoodsAdapter extends FatherAdapter<ShopProduct> {

        public VipGoodsAdapter(Context ctx) {
            super(ctx);
            // TODO Auto-generated constructor stub
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.vip_item, null);
                viewHolder = new ViewHolder(convertView);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.setData(position);
            return convertView;
        }

        public class ViewHolder {
            public TextView tv_good_des;
            public TextView tv_good_price;
            public TextView tv_good_name;
            public TextView tv_good_tag;
            public ImageView iv_good_img;
            public LinearLayout ll_container;

            public ViewHolder(View convertView) {
                tv_good_des = (TextView) convertView.findViewById(R.id.tv_good_des);
                tv_good_price = (TextView) convertView.findViewById(R.id.tv_good_price);
                tv_good_name = (TextView) convertView.findViewById(R.id.tv_good_name);
                tv_good_tag = (TextView) convertView.findViewById(R.id.tv_good_tag);
                iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
                ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
                convertView.setTag(this);
            }

            public void setData(int Position) {
                final ShopProduct shopProduct =getDatas().get(Position);
                ImageUtils.setCommonImage(mContext,shopProduct.ImageUrl,iv_good_img);
                tv_good_des.setText(shopProduct.ProductBrief);
                tv_good_name.setText(shopProduct.ProductName);
                tv_good_price.setText(WWViewUtil.numberFormatWithTwo(shopProduct.SalePrice));
                ll_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext,shopProduct.ProductId);
                    }
                });


            }

        }

    }
