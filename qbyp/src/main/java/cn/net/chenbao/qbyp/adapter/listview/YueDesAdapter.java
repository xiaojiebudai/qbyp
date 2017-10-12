package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AccountRebateInternal;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:余额列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-31
 */
public class YueDesAdapter extends FatherAdapter<AccountRebateInternal> {

    public YueDesAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_cancustom_back_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        AccountRebateInternal cash = getItem(position);
        viewHolder.setData(cash);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_time;
        public TextView tv_num;
        public TextView tv_mark;

        public ViewHolder(View convertView) {
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            convertView.setTag(this);
        }


        public void setData(AccountRebateInternal cash) {
            int width = DensityUtil.getScreenWidth(mContext);
            tv_num.setLayoutParams(new LinearLayout.LayoutParams(
                    width / 8 * 2, ViewGroup.LayoutParams.MATCH_PARENT));
            tv_time.setLayoutParams(new LinearLayout.LayoutParams(
                    width / 8 * 3, ViewGroup.LayoutParams.MATCH_PARENT));
            tv_mark.setLayoutParams(new LinearLayout.LayoutParams(
                    width / 8 * 3, ViewGroup.LayoutParams.MATCH_PARENT));
            tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
            if (Double.parseDouble(cash.ChangeAmt) > 0) {
                tv_num.setTextColor(mContext.getResources().getColor(R.color.red_b));
            } else {
                tv_num.setTextColor(mContext.getResources().getColor(R.color.green));
            }
            tv_num.setText(cash.ChangeAmt + "");
            tv_mark.setText(cash.Explain);//"消费订单" + cash.BusId +

        }

    }

}
