package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Message;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:消息列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class MessageAdapter extends FatherAdapter<Message> {

	public MessageAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_message_item,
					null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		Message item = getItem(position);
		viewHolder.setData(item);
		return convertView;
	}

	public class ViewHolder {
		public TextView title;
		public TextView time;
		public TextView info;

		public ViewHolder(View convertView) {
			title = (TextView) convertView.findViewById(R.id.title);
			time = (TextView) convertView.findViewById(R.id.time);
			info = (TextView) convertView.findViewById(R.id.info);

			convertView.setTag(this);
		}

		public void setData(Message item) {
			time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
			info.setText(item.Content);
			title.setText(item.SendName);
			if (item.ReadFlag) {
				title.setTextColor(mContext.getResources().getColor(R.color.text_gray_s));
				info.setTextColor(mContext.getResources().getColor(R.color.text_gray_s));
			} else {
				title.setTextColor(mContext.getResources().getColor(R.color.text_f7));
				info.setTextColor(mContext.getResources().getColor(R.color.text_f7));
			}
		}
	}

}
