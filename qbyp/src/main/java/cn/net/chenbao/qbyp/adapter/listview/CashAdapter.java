package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Cash;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:提现列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class CashAdapter extends FatherAdapter<Cash> {

	public CashAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_cash_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		Cash cash = getItem(position);
		viewHolder.setData(cash);
		return convertView;
	}

	public class ViewHolder {
		public TextView tv_time;
		public TextView tv_num;
		public TextView tv_state;
		public TextView tv_mark;

		public ViewHolder(View convertView) {
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
			convertView.setTag(this);
		}

		public void setData(Cash cash) {
			tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
			tv_num.setText("-¥" + WWViewUtil.numberFormatWithTwo(cash.ApplyAmt));
			tv_num.setTextColor(mContext.getResources().getColor(R.color.green));
			tv_mark.setVisibility(View.VISIBLE);
			if (cash.ProcessFlag == 0) {
				tv_state.setText(R.string.wait_check);
				tv_state.setTextColor(mContext.getResources().getColor(
						R.color.text_f7));
			} else if (cash.ProcessFlag == 1) {
				switch (cash.TransferFlag) {
				case 0:
					tv_state.setText(R.string.wait_transfer_accounts);
					tv_state.setTextColor(mContext.getResources().getColor(
							R.color.text_f7));
					break;
				case 1:
					tv_state.setText(R.string.deal_with_ing);
					tv_state.setTextColor(mContext.getResources().getColor(
							R.color.text_f7));
					break;
				case 2:
					tv_state.setText(R.string.finished);
					tv_state.setTextColor(mContext.getResources().getColor(
							R.color.text_f7));
					break;
				case 3:
					tv_state.setText(R.string.quited);
					tv_state.setTextColor(mContext.getResources().getColor(
							R.color.red_b));
					break;
				}
			} else if (cash.ProcessFlag == 2) {
				tv_state.setText(R.string.refuse);
				tv_state.setTextColor(mContext.getResources().getColor(
						R.color.red_b));
			}
			tv_mark.setText(cash.Explain);

		}

	}

}
