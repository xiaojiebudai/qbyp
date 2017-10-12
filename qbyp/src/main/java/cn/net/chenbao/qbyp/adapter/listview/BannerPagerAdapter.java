package cn.net.chenbao.qbyp.adapter.listview;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.JiFenShopActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.activity.LoginActivity;

/**
 * 滑动的横幅适配器
 *
 * @author xl
 * @version 创建时间：2015-9-23 下午5:32:50
 */
public class BannerPagerAdapter extends PagerAdapter {
    private final Context mContext;
    private ArrayList<String> mImages = new ArrayList<String>();
    //数据源
    private List<Banner> mList = new ArrayList<Banner>();
    private int height = -1;

    public BannerPagerAdapter(Context context) {
        mContext = context;
    }

    public BannerPagerAdapter(Context context, int height) {
        mContext = context;
        this.height = height;
    }

    public void setData(ArrayList<String> images) {
        if (images != null) {
            mImages = images;
        }
    }

    public void setData(ArrayList<String> images, List<Banner> list) {
        if (images != null) {
            mImages = images;
            mList = list;
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ImageView img = new ImageView(mContext);
        img.setScaleType(ScaleType.CENTER_CROP);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        if (height != -1) {
            img.setLayoutParams(new LayoutParams(width, height));
            ImageUtils.setCommonImage(mContext, mImages.get(position), img, R.drawable.lodingfailh);
        } else {
            img.setLayoutParams(new LayoutParams(width, width));
            ImageUtils.setCommonImage(mContext, mImages.get(position), img);
        }



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList != null && mList.size() > 0) {
                    Banner banner = mList.get(position);
                    switch (banner.OperType) {
                        case 0:
                            //无操作
                            break;
                        case 1:
                            //网页
                            PublicWay.startWebViewActivity((Activity) mContext,banner.BannerName,banner.OperValue, WebViewActivity.URL);
                            break;
                        case 2:
                            //原生界面
                            if (banner.OperValue.equals(Banner.PAGECODE_SELF)) {
                                mContext.startActivity(new Intent(mContext, SelfSupportActivity.class));
                            }else if(banner.OperValue.equals(Banner.CONSUME_AREA)){
                                mContext.startActivity(new Intent(mContext, JiFenShopActivity.class));
                            }
                            break;
                        case 3:
                            //自营商品
                            PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext, Long.parseLong(banner.OperValue));
                            break;
                        case 4:
                            //本地商家店铺
                            if (!WWApplication.getInstance().isLogin()) {// 判断登陆状态
                                Intent intent = new Intent(mContext,
                                        LoginActivity.class);
                                mContext.startActivity(intent);
                            } else {
                                PublicWay.startStoreActivity((Activity) mContext, Long.parseLong(banner.OperValue), 0);
                            }
                            break;
                        case 5:
                            //分销产品详情
                            PublicWay.startDistributionGoodsDetailActivity((Activity)mContext, Long.parseLong(banner.OperValue));
                            break;
                    }

                }
            }
        });
        container.addView(img);
        return img;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
