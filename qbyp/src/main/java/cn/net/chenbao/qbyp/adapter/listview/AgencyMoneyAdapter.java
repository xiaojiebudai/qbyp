package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AgencyAcount;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/***
 * Description:代理余额明细(日明细) Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-11
 ***/
public class AgencyMoneyAdapter extends FatherAdapter<AgencyAcount> {
    private int mode;
    private int agentId;
    /**
     * 月
     */
    public final static int MONTH = 0;
    /**
     * 天
     */
    public final static int DAY = 1;

    public AgencyMoneyAdapter(Context ctx, int model, int agentId) {
        super(ctx);
        this.mode = model;
        this.agentId = agentId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_agency_money,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(convertView, position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_num, tv_time, tv_mark;

        public ViewHolder(View convertView) {
            tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            convertView.setTag(this);
        }

        public void setData(View convertView, int Position) {
            final AgencyAcount item = getItem(Position);
            if (mode == MONTH) {
                tv_num.setText( WWViewUtil.numberFormatPrice(item.Account));
                tv_time.setText(item.CreateDate);
                tv_mark.setText(String.format(mContext.getString(R.string.pen_number), item.RecCount));
                tv_num.setTextColor(mContext.getResources().getColor(
                        R.color.red_b));
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PublicWay.stratAgencyMoneyDayDetailActivity(
                                (Activity) mContext, agentId, item.CreateDate);

                    }
                });
            } else {
                tv_num.setText(WWViewUtil.numberFormatPrice(item.ChangeAmt));
                tv_time.setText(TimeUtil.getOnlyDateToS(item.CreateTime * 1000));
                tv_mark.setText(String.format(mContext.getString(R.string.order_id), item.BusId));
                tv_num.setTextColor(mContext.getResources().getColor(
                        R.color.red_b));
            }
        }

    }

}
