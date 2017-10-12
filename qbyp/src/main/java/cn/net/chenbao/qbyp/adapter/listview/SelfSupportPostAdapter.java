package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Logs;
import cn.net.chenbao.qbyp.utils.TimeUtil;

/**
 * Created by wuri on 2016/11/4.
 */

public class SelfSupportPostAdapter extends FatherAdapter<Logs> {

    public SelfSupportPostAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.listview_wuliu_item,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView content;
        public TextView tv_time;
        public ImageView image;
        public View view_1;
        public View view_2;

        public ViewHolder(View convertView) {
            content = (TextView) convertView.findViewById(R.id.content);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            image = (ImageView) convertView.findViewById(R.id.image);
            view_1 = convertView.findViewById(R.id.view_1);
            view_2 = convertView.findViewById(R.id.view_2);
            convertView.setTag(this);
        }

        public void setData(int pos) {
            Logs orderLog = getItem(pos);
            if (pos == 0) {
                view_1.setVisibility(View.INVISIBLE);
                view_2.setVisibility(View.VISIBLE);
                image.setBackgroundResource(R.drawable.green_circle_wuliu_shape);
                content.setTextColor(mContext.getResources().getColor(R.color.green));
                tv_time.setTextColor(mContext.getResources().getColor(R.color.green));
            } else if (pos == getDatas().size() - 1) {
                view_2.setVisibility(View.INVISIBLE);
                view_1.setVisibility(View.VISIBLE);
                image.setBackgroundResource(R.drawable.gray_circle_wuliu_shape);
                content.setTextColor(mContext.getResources().getColor(R.color.text_b3));
                tv_time.setTextColor(mContext.getResources().getColor(R.color.text_b3));
            }else{
                view_1.setVisibility(View.VISIBLE);
                view_2.setVisibility(View.VISIBLE);
                image.setBackgroundResource(R.drawable.gray_circle_wuliu_shape);
                content.setTextColor(mContext.getResources().getColor(R.color.text_b3));
                tv_time.setTextColor(mContext.getResources().getColor(R.color.text_b3));
            }
            content.setText(orderLog.Explain);
            tv_time.setText(TimeUtil.getTimeToS(orderLog.CreateTime * 1000));
        }
    }
}
