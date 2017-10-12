package cn.net.chenbao.qbyp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/**
 * Created by ppsher on 2017/3/14.
 */

public class LocalCategoryGridViewAdapter extends FatherAdapter<TradesCategory> {

    public LocalCategoryGridViewAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.local_gridview_category,
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
            if ("-1".equals(msg.TradeIco)) {
                image.setBackgroundResource(R.drawable.allsale_icon);
            } else {
                ImageUtils.setCommonImage(mContext, msg.TradeIco, image);
            }
            name.setText(msg.TradeName);
        }
    }
}
