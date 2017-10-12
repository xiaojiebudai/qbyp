
package cn.net.chenbao.qbyp.distribution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionUnfreeze;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
     * Created by zxj on 2017/4/16.
     *
     * @description 解冻
     */

    public class DistributionUnfreezeAdapter extends FatherAdapter<DistributionUnfreeze> {
        public DistributionUnfreezeAdapter(Context ctx) {
            super(ctx);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.list_distribution_unfreeze, null);
                viewHolder = new ViewHolder(convertView);
            } else
                viewHolder = (ViewHolder) convertView.getTag();

            viewHolder.setData(getItem(position));
            return convertView;
        }

        public class ViewHolder {
            public TextView tv_jifen,tv_unfree,tv_pre_finish,tv_time;


            public ViewHolder(View convertView) {
                tv_jifen = (TextView) convertView.findViewById(R.id.tv_jifen);
                tv_unfree = (TextView) convertView.findViewById(R.id.tv_unfree);
                tv_pre_finish = (TextView) convertView.findViewById(R.id.tv_pre_finish);
                tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(this);
            }

            public void setData(DistributionUnfreeze item) {
                tv_jifen.setText(WWViewUtil.numberFormatWithTwo(item.Consume-item.ConsumeGive));
                tv_unfree.setText(WWViewUtil.numberFormatWithTwo(item.Consume-item.ConsumeGive-item.ConsumeAlready));
                tv_pre_finish.setText(item.FinishDays+"天");
                tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));


            }

        }
    }
