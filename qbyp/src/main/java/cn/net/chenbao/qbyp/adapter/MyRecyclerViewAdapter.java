package cn.net.chenbao.qbyp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import java.util.List;

import cn.net.chenbao.qbyp.R;

/**
 * Created by  on 2015/7/12 0012.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
    public List<ShopProduct> datas;
    public Context context;
    public boolean isHasMore;


    public MyRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public boolean isHasMore() {
        return isHasMore;
    }

    public void setHasMore(boolean hasMore) {
        isHasMore = hasMore;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vip_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ShopProduct shopProduct = datas.get(position);
        ImageUtils.setCommonImage(context, shopProduct.ImageUrl, holder.iv_good_img);
        holder.tv_good_des.setText(shopProduct.ProductBrief);
        holder.tv_good_name.setText(shopProduct.ProductName);
        holder.tv_good_price.setText(WWViewUtil.numberFormatWithTwo(shopProduct.SalePrice));
        holder.ll_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicWay.stratSelfSupportGoodsDetailActivity((Activity) context, shopProduct.ProductId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void setDatas(List<ShopProduct> list) {
        this.datas = list;
        notifyDataSetChanged();
    }

    public void addDatas(List<ShopProduct> list) {
        if (datas != null) {
            datas.addAll(list);
        }
        notifyDataSetChanged();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_good_des;
    public TextView tv_good_price;
    public TextView tv_good_name;
    public TextView tv_good_tag;
    public ImageView iv_good_img;
    public LinearLayout ll_container;

    public MyViewHolder(View convertView) {
        super(convertView);
        tv_good_des = (TextView) convertView.findViewById(R.id.tv_good_des);
        tv_good_price = (TextView) convertView.findViewById(R.id.tv_good_price);
        tv_good_name = (TextView) convertView.findViewById(R.id.tv_good_name);
        tv_good_tag = (TextView) convertView.findViewById(R.id.tv_good_tag);
        iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
        ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
    }
}
