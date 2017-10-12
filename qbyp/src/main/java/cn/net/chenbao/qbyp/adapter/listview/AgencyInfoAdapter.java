package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.PublicWay;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Description:代理区域adapter
 *
 * @author wuri
 */
public class AgencyInfoAdapter extends FatherAdapter<AgencyInfo> {

    public AgencyInfoAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_agency_item,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder implements  OnClickListener{
        public TextView tv_agency_Address;
        public TextView tv_serial_number;
        public TextView tv_yue_number;
        AgencyInfo agencyInfo;

        public ViewHolder(View convertView) {
            tv_agency_Address = (TextView) convertView
                    .findViewById(R.id.tv_agency_Address);
            tv_serial_number = (TextView) convertView
                    .findViewById(R.id.tv_serial_number);
            tv_yue_number = (TextView) convertView
                    .findViewById(R.id.tv_yue_number);
            convertView.findViewById(R.id.ll_yue).setOnClickListener(this);
            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicWay.stratAgencyDetailActivity((Activity) mContext,
                            agencyInfo.AgentId);
                }
            });
            convertView.setTag(this);
        }

        public void setData(int Position) {
            agencyInfo = getItem(Position);
            tv_agency_Address.setText(agencyInfo.AreaName);
            tv_serial_number.setText("" + agencyInfo.AgentNo);
            tv_yue_number.setText("￥"
                    + WWViewUtil.numberFormatWithTwo(agencyInfo.Account));
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.ll_yue:
                    PublicWay.stratAgencyMoneyDetailActivity((Activity) mContext,
                            agencyInfo.AgentId);
                    break;
                default:
                    break;
            }
        }
    }
}
