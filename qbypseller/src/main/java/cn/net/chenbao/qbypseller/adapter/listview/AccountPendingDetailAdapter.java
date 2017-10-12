package cn.net.chenbao.qbypseller.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.AccountPendingDetail;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;

/***
 * Description:预结算明细 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-31
 */
public class AccountPendingDetailAdapter extends
        FatherAdapter<AccountPendingDetail> {
	private int model;

	public AccountPendingDetailAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.list_acount_list_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		AccountPendingDetail item = getItem(position);
		viewHolder.setData(item);
		return convertView;
	}

	public class ViewHolder {
		public TextView tv_one;
		public TextView tv_two;
		public TextView tv_three;
		public TextView tv_des;
		public TextView tv_time;
		public TextView tv_four;
		public LinearLayout ll_two;

		public ViewHolder(View convertView) {
			tv_one = (TextView) convertView.findViewById(R.id.tv_one);
			tv_two = (TextView) convertView.findViewById(R.id.tv_two);
			tv_three = (TextView) convertView.findViewById(R.id.tv_three);
			tv_four = (TextView) convertView.findViewById(R.id.tv_four);
			tv_des = (TextView) convertView.findViewById(R.id.tv_des);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			ll_two = (LinearLayout) convertView.findViewById(R.id.ll_two);
			convertView.setTag(this);
		}

		public void setData(AccountPendingDetail cash) {
			int width = DensityUtil.getScreenWidth(mContext);
			ll_two.setVisibility(View.VISIBLE);
			tv_four.setVisibility(View.VISIBLE);
			tv_two.setVisibility(View.GONE);
			tv_one.setVisibility(View.GONE);
			ll_two.setLayoutParams(new LinearLayout.LayoutParams(width / 7 * 3,
					LayoutParams.MATCH_PARENT));
			tv_three.setLayoutParams(new LinearLayout.LayoutParams(
					width / 7 * 2, LayoutParams.MATCH_PARENT));
			tv_four.setLayoutParams(new LinearLayout.LayoutParams(
					width / 7 * 2, LayoutParams.MATCH_PARENT));
			tv_des.setText(String.format(mContext.getString(R.string.order),cash.OrderId));
			tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
			tv_three.setText(WWViewUtil.numberFormatPrice(cash.OrderAmt));
			tv_three.setTextColor(mContext.getResources().getColor(
					R.color.yellow_ww));
			tv_four.setText(WWViewUtil.numberFormatPrice(cash.AccAmt));
			tv_four.setTextColor(mContext.getResources().getColor(
					R.color.yellow_ww));
		}
	}

}
