package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Comment;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.view.RatingBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

public class ShopCommAdapter extends FatherAdapter<Comment> {

	public ShopCommAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.listview_comm_item,
					null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.setData(getItem(position));
		return convertView;
	}

	public class ViewHolder {
		public ImageView iv_head;
		public TextView tv_name;
		public TextView tv_data;
		public TextView tv_time;
		public RatingBar rb;
		public TextView tv_comm;

		public ViewHolder(View convertView) {
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			rb = (RatingBar) convertView.findViewById(R.id.rb);
			tv_data = (TextView) convertView.findViewById(R.id.tv_data);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_comm = (TextView) convertView.findViewById(R.id.tv_comm);
			convertView.setTag(this);
		}

		public void setData(Comment comm) {
			tv_data.setText(TimeUtil.getTimeToM2(comm.CreateTime * 1000));
			if (comm.IsAnonymous) {
				tv_name.setText(mContext.getResources().getString(
						R.string.no_name));
			} else {
				tv_name.setText(comm.UserName);
			}
			ImageUtils.setCircleHeaderImage(mContext, comm.HeadUrl, iv_head);
			rb.setStar(comm.JudgeLevel);
			if (comm.Content != null) {
				tv_comm.setText(comm.Content);
			} else {
				tv_comm.setText(mContext.getResources().getString(
						R.string.good_goods));
			}
		}

	}

}
