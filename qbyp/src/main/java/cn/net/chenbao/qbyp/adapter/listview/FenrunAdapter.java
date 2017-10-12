package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AccountWwbmx;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:分润列表 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class FenrunAdapter extends FatherAdapter<AccountWwbmx> {
	private int tab;

	public FenrunAdapter(Context ctx, int tab) {
		super(ctx);
		this.tab=tab;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_fenrun_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		AccountWwbmx cash = getItem(position);
		viewHolder.setData(cash);
		return convertView;
	}

	public class ViewHolder {
		public TextView tv_time;
		public TextView tv_num;
		public TextView tv_state;

		public ViewHolder(View convertView) {
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			tv_state = (TextView) convertView.findViewById(R.id.tv_state);

			convertView.setTag(this);
		}

		public void setData(AccountWwbmx cash) {
			tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime*1000));
			tv_num.setText(cash.RebateAmt);
			if(cash.ProcessFlag){
				tv_num.setTextColor(mContext.getResources().getColor(R.color.green));
				tv_state.setTextColor(mContext.getResources().getColor(R.color.green));
				tv_state.setText(R.string.already_clear);
			}else{
				tv_num.setTextColor(mContext.getResources().getColor(R.color.red_b));
				tv_state.setTextColor(mContext.getResources().getColor(R.color.red_b));
				tv_state.setText(R.string.pre_clearing);
			}
//			switch (tab) {
//			case FenrunDetailActivity.ONE:
//				tv_mark.setText(cash.SaleName);
//				break;
//			case FenrunDetailActivity.TWO:
//				tv_mark.setText(cash.AgentName);
//				break;
//			case FenrunDetailActivity.THREE:
//				tv_mark.setText(cash.SellerName);
//				break;
//			default:
//				break;
//			}



		}

	}

}
