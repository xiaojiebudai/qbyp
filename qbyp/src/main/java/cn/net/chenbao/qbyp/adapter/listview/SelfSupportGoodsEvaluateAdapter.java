package cn.net.chenbao.qbyp.adapter.listview;

/**
 * Created by 木头 on 2016/11/3.
 */


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.ShopProductJudge;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.view.RatingBar;

import java.util.ArrayList;
import java.util.List;

/***
 * Description:商品评价列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date2016年11月3日16:55:07
 */
public class SelfSupportGoodsEvaluateAdapter extends FatherAdapter<ShopProductJudge> {
    public SelfSupportGoodsEvaluateAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_self_support_goods_evalute_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_name;
        public TextView tv_time;
        public TextView tv_info;
        public RatingBar rb;
        public ImageView iv_img;
        public LinearLayout ll_container;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            tv_info = (TextView) convertView.findViewById(R.id.tv_info);
            rb = (RatingBar) convertView.findViewById(R.id.rb);
            iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            ll_container = (LinearLayout) convertView.findViewById(R.id.ll_container);
            convertView.setTag(this);
        }

        public void setData(int position) {
            ShopProductJudge item = getItem(position);

            ImageUtils.setCircleHeaderImage(mContext, item.HeadUrl, iv_img);
            if (item.IsAnonymous) {
                tv_name.setText(R.string.no_name);
            } else {
                tv_name.setText(item.UserName);
            }
            tv_time.setText(TimeUtil.getTimeToS(item.CreateTime * 1000));
            tv_info.setText(item.Content);
            rb.setStar(item.JudgeLevel);
            ll_container.removeAllViews();
            if (!TextUtils.isEmpty(item.ImageUrl1)) {
                ll_container.addView(creatImgView(item.ImageUrl1, 0, item));
            }
            if (!TextUtils.isEmpty(item.ImageUrl2)) {
                ll_container.addView(creatImgView(item.ImageUrl2, 1, item));
            }
            if (!TextUtils.isEmpty(item.ImageUrl3)) {
                ll_container.addView(creatImgView(item.ImageUrl3, 2, item));
            }
            if (!TextUtils.isEmpty(item.ImageUrl4)) {
                ll_container.addView(creatImgView(item.ImageUrl4, 3, item));
            }
            if (!TextUtils.isEmpty(item.ImageUrl5)) {
                ll_container.addView(creatImgView(item.ImageUrl5, 4, item));
            }

        }

        /**
         * 图片组装
         */
        private View creatImgView(String url, final int pos, ShopProductJudge item) {
            final List<String> urls = new ArrayList<String>();
            if (!TextUtils.isEmpty(item.ImageUrl1)) {
                urls.add(item.ImageUrl1);
            }
            if (!TextUtils.isEmpty(item.ImageUrl2)) {
                urls.add(item.ImageUrl2);
            }
            if (!TextUtils.isEmpty(item.ImageUrl3)) {
                urls.add(item.ImageUrl3);
            }
            if (!TextUtils.isEmpty(item.ImageUrl4)) {
                urls.add(item.ImageUrl4);
            }
            if (!TextUtils.isEmpty(item.ImageUrl5)) {
                urls.add(item.ImageUrl5);
            }
            int width = (mContext.getResources().getDisplayMetrics().widthPixels
                    - DensityUtil.dip2px(mContext, 20)) / 5;
            final ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    width, width);
            imageView.setPadding(DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 5));
            imageView.setLayoutParams(layoutParams);
            ImageUtils.setCommonImage(mContext,ImageUtils.getRightImgScreen(url,width,width), imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicWay.startBigImageActivity((Activity) mContext, urls, pos);
                }
            });
            return imageView;
        }
    }
}

