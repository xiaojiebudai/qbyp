package cn.net.chenbao.qbyp.distribution.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.activity.DistributionPublicListActivity;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 钱包明细列表（货款/提现/优币/积分明细）
 */

public class DistributionPublicListAdapter extends FatherAdapter<DistributionPublicAccount> {
    private int model;

    public DistributionPublicListAdapter(Context ctx, int model) {
        super(ctx);
        this.model = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.list_distribution_public_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_0, tv_11, tv_12, tv_21, tv_22, tv_right;
        public LinearLayout ll_1, ll_2, ll_container;


        public ViewHolder(View convertView) {
            tv_0 = (TextView) convertView.findViewById(R.id.tv_0);
            tv_11 = (TextView) convertView.findViewById(R.id.tv_11);
            tv_12 = (TextView) convertView.findViewById(R.id.tv_12);
            tv_21 = (TextView) convertView.findViewById(R.id.tv_21);
            tv_22 = (TextView) convertView.findViewById(R.id.tv_22);
            tv_right = (TextView) convertView.findViewById(R.id.tv_right);
            ll_1 = (LinearLayout) convertView.findViewById(R.id.ll_1);
            ll_2 = (LinearLayout) convertView.findViewById(R.id.ll_2);
            ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);

            convertView.setTag(this);
        }

        public void setData(final DistributionPublicAccount item) {
            switch (model) {
                case DistributionPublicListActivity.GOODSPAYMENT:
                    if (item.ChangeAmt < 0) {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.green_text));
                        tv_0.setText(WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    } else {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.red_b));
                        tv_0.setText("+" + WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    }
                    tv_11.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                    tv_21.setText(item.BusType==0?item.BusId+"":item.Explain);
                    break;
                case DistributionPublicListActivity.WITHDRAW:
                    tv_22.setVisibility(View.VISIBLE);
                    tv_0.setText("-" + WWViewUtil.numberFormatPrice(item.ApplyAmt));
                    tv_0.setTextColor(mContext.getResources().getColor(R.color.green));
                    tv_11.setText(WWViewUtil.numberFormatPrice(item.Poundage));
                    if (item.ProcessFlag == 0) {
                        tv_21.setText(R.string.wait_check);
                        tv_21.setTextColor(mContext.getResources().getColor(
                                R.color.text_f7));
                    } else if (item.ProcessFlag == 1) {
                        switch (item.TransferFlag) {
                            case 0:
                                tv_21.setText(R.string.wait_transfer_accounts);
                                tv_21.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 1:
                                tv_21.setText(R.string.deal_with_ing);
                                tv_21.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 2:
                                tv_21.setText(R.string.finished);
                                tv_21.setTextColor(mContext.getResources().getColor(
                                        R.color.text_f7));
                                break;
                            case 3:
                                tv_21.setText(R.string.quited);
                                tv_21.setTextColor(mContext.getResources().getColor(
                                        R.color.red_b));
                                break;
                        }
                    } else if (item.ProcessFlag == 2) {
                        tv_21.setText(R.string.refuse);
                        tv_21.setTextColor(mContext.getResources().getColor(
                                R.color.red_b));
                    }
                    tv_22.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                    break;

                case DistributionPublicListActivity.UB:
                    if (item.ChangeAmt < 0) {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.green_text));
                        tv_0.setText(WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    } else {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.red_b));
                        tv_0.setText("+" + WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    }
                    tv_11.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                    if(item.BusType==2||item.BusType==3){
                        tv_22.setVisibility(View.VISIBLE);
                        tv_22.setText(item.BusId+"");
                    }else{
                        tv_22.setVisibility(View.GONE);
                    }
                    tv_21.setText(item.Explain);
                    break;
                case DistributionPublicListActivity.INTEGRAL:

                    if (item.ChangeAmt < 0) {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.green_text));
                        tv_0.setText(WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    } else {
                        tv_0.setTextColor(mContext.getResources().getColor(R.color.red_b));
                        tv_0.setText("+" + WWViewUtil.numberFormatWithTwo(item.ChangeAmt));
                    }
                    tv_11.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));

                    tv_21.setText(item.Explain);
                    break;
                case DistributionPublicListActivity.PoolLevelOne:
                    tv_right.setVisibility(View.VISIBLE);
                    tv_0.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                    tv_0.setText(item.UserName);
                    tv_11.setText(WWViewUtil.numberFormatWithTwo(item.CurrentConsume));
                    tv_21.setText(WWViewUtil.numberFormatWithTwo(item.SpeedRate * 100) + "%");
                    ll_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.startDistributionPublicListActivity((Activity) mContext, DistributionPublicListActivity.PoolLevelDes, item.UserId + "");
                        }
                    });
                    break;
                case DistributionPublicListActivity.PoolLevelTwo:
                    tv_right.setVisibility(View.VISIBLE);
                    tv_0.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                    tv_0.setText(item.UserName);
                    tv_11.setText(WWViewUtil.numberFormatWithTwo(item.CurrentConsume));
                    tv_21.setText(WWViewUtil.numberFormatWithTwo(item.SpeedRate * 100) + "%");
                    ll_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.startDistributionPublicListActivity((Activity) mContext, DistributionPublicListActivity.PoolLevelDes, item.UserId + "");
                        }
                    });
                    break;
                case DistributionPublicListActivity.PoolLevelDes:
                    //数据需要调整
                    tv_0.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                    tv_0.setText(WWViewUtil.numberFormatWithTwo(item.Consume));
                    if(item.DaysLast>0){
                        tv_11.setTextColor(mContext.getResources().getColor(R.color.red_fenxiao));
                    }else{
                        tv_11.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                    }
                    tv_11.setText(item.DaysLast+"");
                    tv_21.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
                    break;


            }
        }

    }
}
