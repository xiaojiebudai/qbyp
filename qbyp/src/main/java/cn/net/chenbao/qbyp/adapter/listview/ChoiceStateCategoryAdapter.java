package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.AgencyInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoiceStateCategoryAdapter extends FatherAdapter<AgencyInfo> {
    private int selectPos = -1;
    private int mode;
    public static final int LPOP = 1;
    public static final int RPOP = 2;

    public ChoiceStateCategoryAdapter(Context ctx, int model) {
        super(ctx);
        mode = model;
        // TODO Auto-generated constructor stub
    }

    public void setSelectPos(int pos) {
        selectPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.list_min_text_item, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        private TextView tv_info;
        private ImageView iv_gou;

        public ViewHolder(View convertView) {
            tv_info = (TextView) convertView
                    .findViewById(R.id.tv_info);
            iv_gou = (ImageView) convertView
                    .findViewById(R.id.iv_gou);
            convertView.setTag(this);
        }

        public void setData(int Position) {
            if (selectPos == Position) {
                tv_info.setTextColor(mContext.getResources().getColor(R.color.yellow_ww));
                iv_gou.setVisibility(View.VISIBLE);
            } else {
                tv_info.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                iv_gou.setVisibility(View.GONE);
            }
            if (mode == LPOP) {
                tv_info.setText(getItem(Position).AreaName);
            } else if (mode == RPOP) {
                tv_info.setText(getItem(Position).StateName);
            }
        }

    }
}
