package cn.net.chenbao.qbypseller.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.SytemMessage;
import cn.net.chenbao.qbypseller.utils.TimeUtil;

public class SystemMessageAdapter extends FatherAdapter<SytemMessage> {

	@Override
	public int getCount() {
		return super.getCount();
	}

	public SystemMessageAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.listview_item_systemmessage, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		 viewHolder.setData(getItem(position));
		return convertView;
	}

	public class ViewHolder {

		TextView tv_title;
		TextView tv_content;
		TextView tv_data;
		ImageView iv_head;

		public ViewHolder(View convertView) {
			tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
			tv_data = (TextView) convertView.findViewById(R.id.tv_data);
			tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(this);
		}

		public void setData(SytemMessage bean) {
			tv_title.setText(bean.SendName);
			tv_data.setText(TimeUtil.formatPhotoDate(bean.CreateTime*1000));
			tv_content.setText(bean.Content);
			if (bean.ReadFlag) {

				tv_title.setTextColor(mContext.getResources().getColor(R.color.text_gray_s));
				tv_content.setTextColor(mContext.getResources().getColor(R.color.text_gray_s));
			} else {
				tv_title.setTextColor(mContext.getResources().getColor(R.color.text_f7));
				tv_content.setTextColor(mContext.getResources().getColor(R.color.text_f7));
			}
		}

	}

}
