

package cn.net.chenbao.qbyp.distribution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionLevel;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销等级
 */

public class DistributionLevelAdapter extends FatherAdapter<DistributionLevel> {
    public DistributionLevelAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.list_distribution_level_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_name, tv_upgrade_need, tv_level, tv_1ji, tv_2ji;
        public ImageView iv_good_img, iv_level;


        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_upgrade_need = (TextView) convertView.findViewById(R.id.tv_upgrade_need);
            tv_level = (TextView) convertView.findViewById(R.id.tv_level);
            tv_1ji = (TextView) convertView.findViewById(R.id.tv_1ji);
            tv_2ji = (TextView) convertView.findViewById(R.id.tv_2ji);
            iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
            iv_level = (ImageView) convertView.findViewById(R.id.iv_level);

            convertView.setTag(this);
        }

        public void setData(DistributionLevel item) {
            ImageUtils.setCommonImage(mContext, item.ImageUrl, iv_good_img, DensityUtil.dip2px(mContext, 75), DensityUtil.dip2px(mContext, 75));
            tv_name.setText(item.Product.ProductName);
            tv_1ji.setText("总分销需一次进货" + item.Product.AgentNum1 + "件");
            tv_2ji.setText("二级分销需一次进货" + item.Product.AgentNum2 + "件");

            switch (item.LevelId) {
                case 0:
                    iv_level.setImageResource(R.drawable.user_fenxiaoconsumer_icon);
                    tv_level.setText(R.string.consumer);
                    tv_upgrade_need.setText(item.Explain);
                    break;
                case 1:
                    iv_level.setImageResource(R.drawable.user_fenxiaototal_icon);
                    tv_level.setText(R.string.top_seller);
                    break;
                case 2:
                    iv_level.setImageResource(R.drawable.user_fenxiaosecond_icon);
                    tv_level.setText(R.string.two_big_seller);
                    tv_upgrade_need.setText(item.Explain);
                    break;

            }

        }

    }
}

