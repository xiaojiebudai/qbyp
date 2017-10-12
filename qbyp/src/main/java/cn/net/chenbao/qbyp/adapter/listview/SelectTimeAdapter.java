package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.GoTime;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

public class SelectTimeAdapter extends FatherAdapter<GoTime> {

	public SelectTimeAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.list_item_select_time, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.setData(getItem(position));
		return convertView;
	}

	public class ViewHolder {
		public TextView time;
		public ImageView iv;

		public ViewHolder(View convertView) {
			time = (TextView) convertView.findViewById(R.id.tv_time);
			iv = (ImageView) convertView.findViewById(R.id.cb);
			convertView.setTag(this);
		}

		public void setData(GoTime bean) {
			time.setText(bean.time);
			if (bean.isSelect) {
//				iv.setSelected(true);//没看到效果   布局把选择器改了
				iv.setBackgroundResource(R.drawable.cricle_small_selector);
			} else {
//				iv.setSelected(false);
				iv.setBackgroundResource(R.drawable.cricle_small_nor);
			}
		}
	}

}
