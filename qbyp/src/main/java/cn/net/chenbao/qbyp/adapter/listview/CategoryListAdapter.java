package cn.net.chenbao.qbyp.adapter.listview;

import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.TradeExt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryListAdapter extends BaseAdapter {

    private Context context;
    public List<TradeExt> itemList;

    public CategoryListAdapter(Context context, List<TradeExt> item) {
        this.context = context;
        this.itemList = item;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.category_item, null);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setData(position);
        return convertView;
    }

    private class ViewHolder {
        public TextView mCount;
        public ImageView mImage;
        public TextView mNameTextView;

        public ViewHolder(View convertView) {
            mNameTextView = (TextView) convertView.findViewById(R.id.name);
            mImage = (ImageView) convertView.findViewById(R.id.haschild);
            mCount = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(this);
        }

        public void setData(int position) {
            mCount.setText(itemList.get(position).RecCount + "");
            mNameTextView.setText(itemList.get(position).TradeName);
            mImage
                    .setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        }
    }

}
