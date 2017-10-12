package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.TradesCategory;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/**
 * 类目适配器
 * 
 * @author xl
 * @date:2016-7-27下午4:25:36
 * @description
 */
public class CategoryAdapter extends FatherAdapter<TradesCategory> {

	public CategoryAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_category_item,
					parent, false);
			convertView
					.setBackgroundResource(R.drawable.selector_goods_category_check);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		tvName.setText(getItem(position).TradeName);
		return convertView;
	}
}
