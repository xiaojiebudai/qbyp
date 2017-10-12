package cn.net.chenbao.qbyp.adapter;

import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.utils.ImageUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

public class CategoryGridViewAdapter extends FatherAdapter<TradesCategory> {

    public CategoryGridViewAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.gridview_category,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {

        public ImageView image;
        public TextView name;

        public ViewHolder(View convertView) {
            image = (ImageView) convertView.findViewById(R.id.iv_image);
            name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(this);
        }

        public void setData(TradesCategory msg) {
            ImageUtils.setCommonImage(mContext, msg.TradeIco, image);
            name.setText(msg.TradeName);
        }
    }

}
