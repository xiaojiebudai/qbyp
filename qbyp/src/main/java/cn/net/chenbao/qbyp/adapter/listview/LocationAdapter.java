package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Location;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/**
 * 定位适配器
 * 
 * @author xl
 * @date 2016-7-29 上午1:42:29
 * @description
 */
public class LocationAdapter extends FatherAdapter<Location> {

	private int model;
	public static final int MAP = 0;
	public static final int SEARCH = 1;
	public LocationAdapter(Context ctx,int model) {
		super(ctx);
		this.model=model;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_location_item,
					parent, false);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		if (model==MAP&&position == 0) {
			viewHolder.taget_data.setVisibility(View.GONE);
			viewHolder.current.setVisibility(View.VISIBLE);
		} else {
			viewHolder.taget_data.setVisibility(View.VISIBLE);
			viewHolder.current.setVisibility(View.GONE);
		}
		viewHolder.setData(getItem(position), position);
		return convertView;
	}

	private static class ViewHolder {
		View current;
		TextView taget_data;
		TextView address;

		public ViewHolder(View v) {
			current = v.findViewById(R.id.tv_current_hint);
			taget_data = (TextView) v.findViewById(R.id.tv_target_data);
			address = (TextView) v.findViewById(R.id.tv_address);
			v.setTag(this);
		}

		public void setData(Location bean, int position) {
			taget_data.setText(bean.name);
			address.setText(bean.street);
		}
	}
}
