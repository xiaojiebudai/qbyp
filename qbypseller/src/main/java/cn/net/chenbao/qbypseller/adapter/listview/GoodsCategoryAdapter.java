package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Category;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 商品品类适配器
 * 
 * @author xl
 * @date 2016-7-29 上午2:35:12
 * @description 增加两个静态条目 "全部"+"下架"
 */
public class GoodsCategoryAdapter extends FatherAdapter<Category> {

	public GoodsCategoryAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_category_item,
					parent, false);
		}
		((TextView) convertView).setText(getItem(position).ClassName);
		return convertView;
	}
}
