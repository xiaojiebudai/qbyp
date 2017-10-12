package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.GoodsKind;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/**
 * 商品品类适配器
 * 
 * @author xl
 * @date 2016-7-29 上午2:35:12
 * @description
 */
public class GoodsCategoryAdapter extends FatherAdapter<GoodsKind> {

	public GoodsCategoryAdapter(Context ctx) {
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
		((TextView) convertView).setText(getItem(position).ClassName);
		return convertView;
	}
}
