package cn.net.chenbao.qbypseller.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.activity.PersonPublicListDesActivity;
import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.AccountSellerPending;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;

/***
 * Description:账户明细 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-31
 */
public class AcountLisstAdapter extends FatherAdapter<AccountSellerPending> {
    private int model;

    public AcountLisstAdapter(Context ctx, int model) {
        super(ctx);
        this.model = model;
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
        AccountSellerPending item = getItem(position);
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

        public void setData(AccountSellerPending cash) {
            int width = DensityUtil.getScreenWidth(mContext);
            switch (model) {

                case PersonPublicListDesActivity.YUJIESUANDES:
                    ll_two.setVisibility(View.VISIBLE);
                    tv_four.setVisibility(View.VISIBLE);
                    tv_two.setVisibility(View.GONE);
                    tv_one.setVisibility(View.GONE);
                    ll_two.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 3, LayoutParams.MATCH_PARENT));
                    tv_three.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    tv_four.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    tv_des.setText(String.format(mContext.getString(R.string.order), cash.OrderId));
                    tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
                    tv_three.setText(WWViewUtil.numberFormatPrice(cash.OrderAmt));
                    tv_three.setTextColor(mContext.getResources().getColor(
                            R.color.yellow_ww));
                    tv_four.setText(WWViewUtil.numberFormatPrice(cash.AccAmt));
                    tv_four.setTextColor(mContext.getResources().getColor(
                            R.color.yellow_ww));
                    break;
                case PersonPublicListDesActivity.TIXIANDES:
                    tv_one.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 6 * 2, LayoutParams.MATCH_PARENT));
                    tv_two.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 6 * 2, LayoutParams.MATCH_PARENT));
                    tv_three.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 6 * 2, LayoutParams.MATCH_PARENT));
                    tv_one.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
                    tv_two.setTextColor(mContext.getResources().getColor(
                            R.color.green));
                    tv_two.setText("-"
                            + WWViewUtil.numberFormatPrice(cash.ApplyAmt));
                    if (cash.ProcessFlag == 0) {
                        tv_three.setText(R.string.wait_check);
                        tv_three.setTextColor(mContext.getResources().getColor(
                                R.color.text_f7));
                    } else if (cash.ProcessFlag == 1) {
                        switch (cash.TransferFlag) {
                            case 0:
                                tv_three.setText(R.string.wait_pay_money);
                                tv_three.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 1:
                                tv_three.setText(R.string.deal_with_ing);
                                tv_three.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 2:
                                tv_three.setText(R.string.finished);
                                tv_three.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 3:
                                tv_three.setText(R.string.reject);
                                tv_three.setTextColor(mContext.getResources().getColor(
                                        R.color.red_b));
                                break;
                        }
                    } else if (cash.ProcessFlag == 2) {
                        tv_three.setText(R.string.refuse);
                        tv_three.setTextColor(mContext.getResources().getColor(
                                R.color.red_b));
                    }

                    break;
                case PersonPublicListDesActivity.XIAOSHOUEDES:
                    ll_two.setVisibility(View.VISIBLE);
                    tv_two.setVisibility(View.GONE);
                    tv_one.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    ll_two.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 3, LayoutParams.MATCH_PARENT));
                    tv_three.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    tv_one.setText(cash.UserName);
                    tv_des.setText(String.format(mContext.getString(R.string.order), cash.OrderId));
                    tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
                    tv_three.setText(WWViewUtil.numberFormatPrice(cash.TotalAmt));
                    tv_three.setTextColor(mContext.getResources().getColor(
                            R.color.yellow_ww));
                    break;
                case PersonPublicListDesActivity.YUEDES:
                    ll_two.setVisibility(View.VISIBLE);
                    tv_two.setVisibility(View.GONE);
                    tv_one.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    ll_two.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 3, LayoutParams.MATCH_PARENT));
                    tv_three.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    tv_des.setText(cash.BusId);
                    if (cash.BusType == 0) {
                        tv_one.setText(R.string.credit);
                    } else if (cash.BusType == 1) {
                        tv_one.setText(R.string.withdraw);
                    } else {
                        tv_one.setText(R.string.pay);
                    }
                    tv_time.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
                    tv_three.setText(WWViewUtil.numberFormatPrice(cash.ChangeAmt));
                    tv_three.setTextColor(mContext.getResources().getColor(
                            R.color.yellow_ww));
                    break;
                case PersonPublicListDesActivity.JIFEN:

                    tv_one.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    tv_two.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 3, LayoutParams.MATCH_PARENT));
                    tv_three.setLayoutParams(new LinearLayout.LayoutParams(
                            width / 7 * 2, LayoutParams.MATCH_PARENT));
                    if (cash.ChangeAmt < 0) {
                        tv_one.setText("" + cash.ChangeAmt);
                        tv_one.setTextColor(mContext.getResources().getColor(
                                R.color.green));
                    } else {
                        tv_one.setText("+" + cash.ChangeAmt);
                        tv_one.setTextColor(mContext.getResources().getColor(
                                R.color.yellow_ww));
                    }
                    tv_two.setText(TimeUtil.getTimeToS(cash.CreateTime * 1000));
                    tv_three.setText(cash.Explain);

                    break;
                default:
                    break;
            }

        }

    }

}
