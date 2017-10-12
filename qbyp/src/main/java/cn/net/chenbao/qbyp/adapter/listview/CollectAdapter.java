package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.CollectBean;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.RatingBar;

/***
 * Description:收藏 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-2
 */
public class CollectAdapter extends FatherAdapter<CollectBean> {

    public CollectAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_item_collect,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        CollectBean item = getItem(position);
        viewHolder.setData(item);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_time;
        public TextView tv_pingfen;
        public RatingBar rr_comm;
        public TextView tv_name;
        public ImageView iv_pic;

        public ViewHolder(View convertView) {
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_pingfen = (TextView) convertView.findViewById(R.id.tv_pingfen);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            rr_comm = (RatingBar) convertView.findViewById(R.id.rr_comm);
            iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            convertView.setTag(this);
        }

        public void setData(CollectBean item) {
            ImageUtils.setCommonImage(mContext, item.ShopPicture, iv_pic);
            tv_name.setText(item.SellerName + "");
            double star = Arith.round(item.JudgeLevel, 1);
            rr_comm.setStar((float) star);
            tv_pingfen.setText(String.format(mContext.getString(R.string.five_star), WWViewUtil.numberFormatWithTwo(Arith.round(item.JudgeLevel, 1))));
            tv_time.setText(TimeUtil.getOnlyDateToS(item.CreateTime * 1000) + "");
        }

    }

}
