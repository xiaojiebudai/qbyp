package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AccountWwbmx;
import cn.net.chenbao.qbyp.utils.TimeUtil;

/***
 * Description:消费返现 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-31
 */
public class CustomBackAdapter extends FatherAdapter<AccountWwbmx> {

    public CustomBackAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.list_custom_back_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        AccountWwbmx item = getItem(position);
        viewHolder.setData(item);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_time;
        public TextView tv_num;
        public TextView tv_money;
        public TextView tv_state;
        public TextView tv_back;

        public ViewHolder(View convertView) {
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            tv_back = (TextView) convertView.findViewById(R.id.tv_back);
            tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            convertView.setTag(this);
        }

        public void setData(AccountWwbmx item) {
            tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
            tv_num.setText(String.format(mContext.getString(R.string.order_id), item.OrderId));
            tv_back.setText(String.format(mContext.getString(R.string.money), item.RebateAmt));
            tv_money.setText(String.format(mContext.getString(R.string.money), item.OrderAmt));
            tv_money.setTextColor(mContext.getResources().getColor(R.color.red_b));
            // 状态待处理
            if (item.ProcessFlag) {
                tv_state.setText(R.string.already_clear);
            } else {
                tv_state.setText(R.string.pre_clearing);
            }
            // tv_state.setText();
            // tv_mark.setText(item.Explain);

        }

    }

}
