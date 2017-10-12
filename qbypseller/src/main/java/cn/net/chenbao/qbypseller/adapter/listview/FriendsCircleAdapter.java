package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.User;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/***
 * Description:好友列表 Company: Zhubaoyi Version：2.0
 * 
 * @title FriendsCircleAdapter.java
 * @author ZXJ
 * @date 2016-8-2
 ***/
public class FriendsCircleAdapter extends FatherAdapter<User> {

	public FriendsCircleAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_friends_circle_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		User user = getItem(position);
		viewHolder.setData(user);
		return convertView;
	}

	public class ViewHolder {
		public TextView tv_name;
		public TextView tv_mobile;
		public TextView tv_realname;
		public TextView tv_time;
		public ImageView iv_rank;

		public ViewHolder(View convertView) {
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_realname = (TextView) convertView.findViewById(R.id.tv_realname);
			tv_mobile = (TextView) convertView
					.findViewById(R.id.tv_mobile);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			iv_rank = (ImageView) convertView.findViewById(R.id.iv_rank);
			convertView.setTag(this);
		}

		public void setData(User user) {
			tv_name.setText(user.UserName);
			tv_mobile.setText(user.Mobile);
			tv_realname.setText(user.RealName);
			tv_time.setText(TimeUtil.getOnlyDate(user.CreateTime*1000));
			if(user.RelLevel==1){
				iv_rank.setImageResource(R.drawable.friend_circle_ico01);
			}else{
				iv_rank.setImageResource(R.drawable.friend_circle_ico02);
			}

		}

	}

}
