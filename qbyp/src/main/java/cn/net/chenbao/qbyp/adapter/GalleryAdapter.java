package cn.net.chenbao.qbyp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/**
 * Created by ppsher on 2017/1/3.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private List<ShopClass> mDatas;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, Object data);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public GalleryAdapter(Context context) {
        this.mContext = context;
    }

    public void setDatas(List<ShopClass> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.gridview_category_happy_area,
                null);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.iv_image);
        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.tv_name);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageUtils.setCommonImage(mContext, mDatas.get(position).ImageUrl, holder.mImg);
        holder.mTxt.setText(mDatas.get(position).ClassName);
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImg;
        TextView mTxt;

        public ViewHolder(View arg0) {
            super(arg0);
        }
    }
}
