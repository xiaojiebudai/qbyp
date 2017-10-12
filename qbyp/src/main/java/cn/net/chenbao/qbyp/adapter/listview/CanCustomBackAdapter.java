package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.activity.PersonPublicListDesActivity;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AccountRebateInternal;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/***
 * Description:可消费返现 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-31
 */
public class CanCustomBackAdapter extends FatherAdapter<AccountRebateInternal> {
    int mode;

    public CanCustomBackAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.list_cancustom_back_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        AccountRebateInternal item = getItem(position);
        viewHolder.setData(item);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_num;
        public TextView tv_time;
        public TextView tv_mark;

        public ViewHolder(View convertView) {
            tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            convertView.setTag(this);
        }

        public void setData(AccountRebateInternal item) {
            int width = DensityUtil.getScreenWidth(mContext);
//            tv_num.setLayoutParams(new LinearLayout.LayoutParams(
//                    width / 8 * 2, ViewGroup.LayoutParams.MATCH_PARENT));
//            tv_time.setLayoutParams(new LinearLayout.LayoutParams(
//                    width / 8 * 3, ViewGroup.LayoutParams.MATCH_PARENT));
//            tv_mark.setLayoutParams(new LinearLayout.LayoutParams(
//                    width / 8 * 3, ViewGroup.LayoutParams.MATCH_PARENT));
            if (mode == 0) {
                tv_num.setText(item.ChangeAmt + "");
                tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                tv_mark.setText(item.Explain);
            } else if (mode == PersonPublicListDesActivity.WAITUNFREEZEDES) {
                tv_num.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                tv_num.setText(WWViewUtil.numberFormatPrice(item.BusAmt));
                tv_time.setTextColor(mContext.getResources().getColor(R.color.green_text));
                tv_time.setText(String.format(mContext.getString(R.string.add_integral), WWViewUtil.numberFormatWithTwo(item.DeblockingIntegral)));
                tv_mark.setText(String.format(mContext.getString(R.string.order_id_time), item.BusId, TimeUtil.getOnlyDateToS(item.CreateTime * 1000)));
            } else if (mode == PersonPublicListDesActivity.TOTALINTEGRALDES) {
                tv_num.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                tv_num.setText(WWViewUtil.numberFormatPrice(item.BusAmt));
                if (item.DeblockingIntegral > 0) {
                    tv_time.setTextColor(mContext.getResources().getColor(R.color.green_text));
                    tv_time.setText(WWViewUtil.numberFormatPrice(item.DeblockingIntegral));
                } else {
                    tv_time.setTextColor(mContext.getResources().getColor(R.color.yellow_ww));
                    tv_time.setText(String.format(mContext.getString(R.string.add_integral), WWViewUtil.numberFormatWithTwo(item.DeblockingIntegral)));
                }
                tv_mark.setText(String.format(mContext.getString(R.string.order_id_time), item.BusId, TimeUtil.getOnlyDateToS(item.CreateTime * 1000)));
            } else if (mode == PersonPublicListDesActivity.WWBDES) {//可用积分
                if (item.ChangeAmt.startsWith("-")) {
                    tv_num.setTextColor(mContext.getResources().getColor(R.color.green_text));
                    tv_num.setText(item.ChangeAmt);
                } else {
                    tv_num.setTextColor(mContext.getResources().getColor(R.color.yellow_ww));
                    tv_num.setText(String.format(mContext.getString(R.string.add_string), item.ChangeAmt));
                }
                tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                tv_mark.setText(item.Explain);
            }
            // 状态待处理
            // tv_state.setText();
            // tv_mark.setText(item.Explain);

        }

    }

}
