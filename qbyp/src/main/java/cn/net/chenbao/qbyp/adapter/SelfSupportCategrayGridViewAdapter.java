package cn.net.chenbao.qbyp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/**
 * Created by 木头 on 2016/11/1.
 */
public class SelfSupportCategrayGridViewAdapter extends FatherAdapter<ShopClass> {
    public SelfSupportCategrayGridViewAdapter(Context ctx) {
        super(ctx);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.self_gridview_category,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.setData(position);
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

        public void setData(int pos) {
            ShopClass msg=getItem(pos);
            if(pos==(getCount()-1)){
                image.setImageResource(R.drawable.mall_index_allclassify_icon);
            }else{
                ImageUtils.setCommonImage(mContext, msg.ImageUrl, image);
            }
            name.setText(msg.ClassName);
        }
    }

}
