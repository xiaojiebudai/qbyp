package cn.net.chenbao.qbyp.distribution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/**
 * Created by ppsher on 2017/4/18.
 */

public class AdAdapter extends FatherAdapter<Banner> {

    private int mAdHeight;

    public AdAdapter(Context ctx, int adHeight) {
        super(ctx);
        mAdHeight = adHeight;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(mContext,
                    R.layout.list_distribution_ad, null);
            viewHolder = new ViewHolder(view);
        } else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.setData(i);
        return view;
    }

    public class ViewHolder {
        public ImageView iv_ad;

        public ViewHolder(View convertView) {
            iv_ad = (ImageView) convertView.findViewById(R.id.iv_ad);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mAdHeight);
            params.setMargins(0, DensityUtil.dip2px(mContext, 5), 0, 0);
            iv_ad.setLayoutParams(params);
            convertView.setTag(this);
        }

        public void setData(int position) {
            String url = ImageUtils.getRightImgScreen(getItem(position).PicUrl, DensityUtil.getScreenWidth(mContext), mAdHeight);
            ImageUtils.setCommonImage(mContext, url, iv_ad, R.drawable.lodingfailh);
        }

    }
}
