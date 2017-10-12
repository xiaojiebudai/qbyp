package cn.net.chenbao.qbyp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.ShopBrand;

/**
     * Created by 木头 on 2016/11/1.
     */
    public class PopSelfBrandGridViewAdapter extends FatherAdapter<ShopBrand> {
        public PopSelfBrandGridViewAdapter(Context ctx) {
            super(ctx);
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.pop_self_brand_select_item,
                        null);
                viewHolder = new ViewHolder(convertView);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.setData(position);
            return convertView;
        }

        public class ViewHolder {

            public TextView name;

            public ViewHolder(View convertView) {
                name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(this);
            }

            public void setData(int pos) {
                ShopBrand shopBrand=getItem(pos);
                name.setText(shopBrand.BrandName);
            }
        }

    }

