package cn.net.chenbao.qbyp.view;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class BannerViewPagerAdapter extends PagerAdapter {

	private List<View> views = null;
	private ScaleType scaleType;
	private boolean adjustViewBounds;
	public BannerViewPagerAdapter(List<View> views) {
		this(views,ScaleType.CENTER_CROP,false);
	}

	public BannerViewPagerAdapter(List<View> views, ScaleType scaleType,boolean adjustViewBounds) {
		super();
        this.views = views;
        if(null == scaleType)
        	this.scaleType = scaleType;
        else
        	this.scaleType = ScaleType.CENTER_CROP;
        this.adjustViewBounds = adjustViewBounds;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
	    // TODO Auto-generated method stub
	    View view = views.get(position);
	    ViewPager viewPager = (ViewPager) container;
	    if (view instanceof ImageView){
	        ((ImageView) view).setScaleType(scaleType);
	        if(adjustViewBounds)
	        	((ImageView) view).setAdjustViewBounds(adjustViewBounds);
	    }
	    viewPager.addView(view, 0);
	    return view;
	}
	
	@Override
	public void destroyItem(View container, int position, Object object) {
	    // TODO Auto-generated method stub
	    ((ViewPager) container).removeView((View) object); 
	}


}
