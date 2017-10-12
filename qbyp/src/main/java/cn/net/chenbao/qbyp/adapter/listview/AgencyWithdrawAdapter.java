package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.activity.AgencyWithdrawActivity;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AgencyChargeAccount;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/***
 * Description:代理提现明细(补交明细) Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-11
 ***/
public class AgencyWithdrawAdapter extends FatherAdapter<AgencyChargeAccount> {
    private int mode;

    public AgencyWithdrawAdapter(Context ctx, int model) {
        super(ctx);
        this.mode = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_agency_with_month, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_num, tv_finish_state, tv_finish_time, tv_month, tv_time, tv_mark, tv_arrow, tv_detail;
        public LinearLayout ll_finish, ll_detail, ll_detail_one;

        public ViewHolder(View convertView) {
            tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            tv_month = (TextView) convertView.findViewById(R.id.tv_month);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_mark = (TextView) convertView.findViewById(R.id.tv_mark);
            ll_finish = (LinearLayout) convertView.findViewById(R.id.ll_finish);
            ll_detail = (LinearLayout) convertView.findViewById(R.id.ll_detail);
            ll_detail_one = (LinearLayout) convertView.findViewById(R.id.ll_detail_one);
            tv_finish_time = (TextView) convertView.findViewById(R.id.tv_finish_time);
            tv_finish_state = (TextView) convertView.findViewById(R.id.tv_finish_state);
            tv_detail = (TextView) convertView.findViewById(R.id.tv_detail);
            tv_arrow = (TextView) convertView.findViewById(R.id.tv_arrow);
            convertView.setTag(this);
        }

        public void setData(int Position) {
            final AgencyChargeAccount item = getItem(Position);
            switch (mode) {
                case AgencyWithdrawActivity.WITHDRAW:
                    tv_num.setText("-￥" + WWViewUtil.numberFormatWithTwo(item.ApplyAmt));
                    tv_time.setText(TimeUtil.getTimeToM2(item.CreateTime * 1000));
                    tv_detail.setText(String.format(mContext.getString(R.string.withdraw_detail),
                            WWViewUtil.numberFormatPrice(item.Poundage) + "   ",
                            (item.AgentAmt > 0) ? mContext.getString(R.string.bu_jiao_money) + WWViewUtil.numberFormatPrice(item.AgentAmt) + "   " : "   ",
                            WWViewUtil.numberFormatPrice(item.PayAmt)));
                    tv_mark.setVisibility(View.VISIBLE);
                    tv_arrow.setVisibility(View.VISIBLE);
                    ll_finish.setVisibility(View.GONE);
                    ll_detail.setVisibility(item.isOpen ? View.VISIBLE : View.GONE);
                    ll_detail_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.isOpen = !item.isOpen;
                            ll_detail.setVisibility(item.isOpen ? View.VISIBLE : View.GONE);
                            tv_arrow.setBackgroundResource(item.isOpen ? R.drawable.list_pullup_icon : R.drawable.list_pulldown_icon);
                        }
                    });

                    if (item.ProcessFlag == 0) {
                        tv_mark.setText(R.string.wait_check);
                        tv_mark.setTextColor(mContext.getResources().getColor(
                                R.color.text_f7));
                    } else if (item.ProcessFlag == 1) {
                        switch (item.TransferFlag) {
                            case 0:
                                tv_mark.setText(R.string.wait_transfer_accounts);
                                tv_mark.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 1:
                                tv_mark.setText(R.string.deal_with_ing);
                                tv_mark.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 2:
                                ll_finish.setVisibility(View.VISIBLE);
                                tv_mark.setVisibility(View.GONE);
                                tv_finish_state.setText(R.string.finished);
                                tv_finish_state.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                tv_finish_time.setText(TimeUtil.getTimeToM2(item.TransferTime * 1000));
                                break;
                            case 3:
                                tv_mark.setText(R.string.quited);
                                tv_mark.setTextColor(mContext.getResources().getColor(
                                        R.color.red_b));
                                break;
                        }
                    } else if (item.ProcessFlag == 2) {
                        tv_mark.setText(R.string.refuse);
                        tv_mark.setTextColor(mContext.getResources().getColor(
                                R.color.red_b));
                    }
                    break;
                case AgencyWithdrawActivity.REPAYMENT:
                    tv_num.setText(WWViewUtil.numberFormatPrice(item.CashAmt));
                    tv_time.setText(TimeUtil.getTimeToM2(item.CreateTime * 1000));
                    tv_mark.setText(R.string.teamwork_back);
                    break;

                default:
                    break;
            }
        }

    }

}
