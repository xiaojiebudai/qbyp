package cn.net.chenbao.qbyp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.utils.ImageUtils;

import cn.net.chenbao.qbyp.R;

/**
 * Created by wuri on 2016/11/5.
 * Description:全部分类中GridView适配器
 */

public class SelfSupportAllCategoryGridViewAdapter extends FatherAdapter<ShopClass> {
    public SelfSupportAllCategoryGridViewAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.gridview_category,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
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

        public void setData(ShopClass shopClass) {
            ImageUtils.setCommonImage(mContext, shopClass.IcoUrl, image);
            name.setText(shopClass.ClassName);
        }
    }
}
