package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Evaluation;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.view.RatingBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 评价适配器
 * 
 * @author xl
 * @date 2016-8-13 下午5:19:08
 * @description
 */
public class EvaluationAdapter extends FatherAdapter<Evaluation> {

	public EvaluationAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listview_evaluation_item,
					parent, false);
			new ViewHolder(convertView);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		Evaluation item = getItem(position);
		if (item.IsAnonymous) {
			holder.account.setText(R.string.anonymity);
		} else {
			holder.account.setText(item.UserName);
		}
		holder.amount.setText(item.OrderAmt);
		holder.order.setText(mContext.getString(R.string.format_order,
				item.OrderId));
		holder.time.setText(TimeUtil.getTimeToM(item.CreateTime * 1000));
		holder.bar.setStar(item.JudgeLevel);
		return convertView;
	}

	private static class ViewHolder {
		TextView account;
		TextView time;
		TextView amount;
		TextView order;
		RatingBar bar;

		public ViewHolder(View convertView) {
			account = (TextView) convertView.findViewById(R.id.tv_account);
			time = (TextView) convertView.findViewById(R.id.tv_time);
			amount = (TextView) convertView.findViewById(R.id.tv_amount);
			order = (TextView) convertView.findViewById(R.id.tv_order_number);
			bar = (RatingBar) convertView.findViewById(R.id.ratingbar);
			convertView.setTag(this);
		}

	}
}
