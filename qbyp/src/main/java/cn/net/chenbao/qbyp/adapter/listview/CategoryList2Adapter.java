package cn.net.chenbao.qbyp.adapter.listview;

import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.TradeExt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CategoryList2Adapter extends BaseAdapter {

    private Context context;
    public List<TradeExt> itemList;

    public CategoryList2Adapter(Context context,
                                List<TradeExt> secondList) {
        this.context = context;
        this.itemList = secondList;
    }

    @Override
    public int getCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList == null ? null : itemList.get(position);
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
        public TextView mNameTextView;

        public ViewHolder(View convertView) {
            mNameTextView = (TextView) convertView.findViewById(R.id.name);
            mCount = (TextView) convertView.findViewById(R.id.count);
            convertView.setTag(this);
        }

        public void setData(int position) {
            mCount.setText(itemList.get(position).RecCount + "");
            mNameTextView.setText(itemList.get(position).TradeName);
        }
    }

}
