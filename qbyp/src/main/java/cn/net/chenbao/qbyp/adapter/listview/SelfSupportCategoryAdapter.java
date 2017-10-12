package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.adapter.SelfSupportAllCategoryGridViewAdapter;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.bean.ShopClasses;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.view.NoScrollGridView;

import java.util.List;

/**
 * Created by wuri on 2016/11/1.
 * Description:自营商城全部分类listview Adapter
 */

public class SelfSupportCategoryAdapter extends FatherAdapter<ShopClasses> {

    public SelfSupportCategoryAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.lv_selfsupport_yiji_category_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(getDatas().get(position));
        return convertView;
    }

    public class ViewHolder implements AdapterView.OnItemClickListener {
        TextView tvHeadCategory;
        NoScrollGridView gvCategray;
        List<ShopClass> shopClassList;

        public ViewHolder(View convertView) {
            tvHeadCategory = (TextView) convertView.findViewById(R.id.tv_yiji_category);
            gvCategray = (NoScrollGridView) convertView.findViewById(R.id.gv_categray);
            convertView.setTag(this);
        }

        public void setData(ShopClasses shopClasses) {
            tvHeadCategory.setText(shopClasses.ClassName);
            shopClassList = shopClasses.Children;

            SelfSupportAllCategoryGridViewAdapter categoryAllGridViewAdapter = new SelfSupportAllCategoryGridViewAdapter(mContext);
            categoryAllGridViewAdapter.setDatas(shopClassList);
            gvCategray.setAdapter(categoryAllGridViewAdapter);
            gvCategray.setOnItemClickListener(ViewHolder.this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PublicWay.stratSelfSupportSearchDataActivity((Activity) mContext, null, shopClassList.get(position).ClassId);
        }
    }
}
