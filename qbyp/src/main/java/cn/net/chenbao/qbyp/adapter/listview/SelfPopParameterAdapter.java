package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.bean.ShopProductExtend;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;

public class SelfPopParameterAdapter extends FatherAdapter<ShopProductExtend> {

        public SelfPopParameterAdapter(Context ctx) {
            super(ctx);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_item_goods_parameter, null);
                viewHolder = new ViewHolder(convertView);
            } else
                viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.setData(position);
            return convertView;
        }

        public class ViewHolder {
            public TextView tv0;
            public TextView tv1;

            public ViewHolder(View convertView) {
                tv0 = (TextView) convertView.findViewById(R.id.tv_0);
                tv1 = (TextView) convertView.findViewById(R.id.tv_1);
                convertView.setTag(this);
            }
            public void setData(int position) {
                ShopProductExtend shopProductExtend=getItem(position);
                tv0.setText(shopProductExtend.ProName);
                tv1.setText(shopProductExtend.ValName);
            }
        }

    }

