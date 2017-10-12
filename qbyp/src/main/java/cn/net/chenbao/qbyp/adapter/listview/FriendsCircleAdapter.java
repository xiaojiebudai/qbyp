package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.utils.TimeUtil;

/***
 * Description:好友列表 Company: Zhubaoyi Version：2.0
 *
 * @author ZXJ
 * @title FriendsCircleAdapter.java
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
        //		public TextView tv_name;
        public TextView tv_mobile;//手机号
        public TextView tv_realname;//等级
        public TextView tv_time;//注册时间
        public ImageView iv_rank;

        public ViewHolder(View convertView) {
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_realname = (TextView) convertView.findViewById(R.id.tv_realname);
            tv_mobile = (TextView) convertView
                    .findViewById(R.id.tv_mobile);
//			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            iv_rank = (ImageView) convertView.findViewById(R.id.iv_rank);
            convertView.setTag(this);
        }

        public void setData(User user) {
//			tv_name.setText(user.UserName);
            tv_mobile.setText(user.Mobile);
            tv_time.setText(TimeUtil.getOnlyDateToS(user.CreateTime * 1000));
            if (user.LevelId == 0) {
                tv_realname.setText(R.string.common_user);
            } else {
                tv_realname.setText(R.string.vip);
            }
            if (user.RelLevel == 1) {
                iv_rank.setImageResource(R.drawable.friend_circle_ico01);
            } else {
                iv_rank.setImageResource(R.drawable.friend_circle_ico02);
            }
        }

    }

}
