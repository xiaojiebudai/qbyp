package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/***
 * Description:代理区域选择选择列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-8-4
 */
public class AgencySelectAdapter extends FatherAdapter<AgencyInfo> {
	private int selectPostion = 0;

	public AgencySelectAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}
	public void setSelectPostion(int position) {
		this.selectPostion = position;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.list_text_select_agency, null);
			viewHolder = new ViewHolder(convertView); 
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		if (position == selectPostion) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
		}else{
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.white));
		}
		viewHolder.setData(position);
		return convertView;
	}


	public class ViewHolder {
		public TextView tv_name;

		public ViewHolder(View convertView) {
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(this);
		}

		public void setData(int Position) {
			 AgencyInfo item = getItem(Position);
			tv_name.setText(item.AreaName);
		}

	}

}
