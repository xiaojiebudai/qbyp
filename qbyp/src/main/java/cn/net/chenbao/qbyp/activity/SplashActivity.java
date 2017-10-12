package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;


/**
 * Created by zxj on 2017/3/9.
 *
 * @description 引导页
 */

public class SplashActivity extends FatherActivity implements
        GestureDetector.OnGestureListener, View.OnTouchListener {
    private ViewPager imagePager;
    private TextView textView1;


    private int pager_num;
    int total_page;
    int backgoundWidth;
    PagerAdapter mPagerAdapter;
    private int[] draws = new int[]{R.drawable.splash0,
            R.drawable.splash1};

    @Override
    protected int getLayoutId() {
        return R.layout.act_splash;
    }

    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    @Override
    protected void initValues() {
        PermissionUtil.requestAllPermissions(this);
    }

    @Override
    protected void initView() {
        imagePager = (ViewPager) findViewById(R.id.image_pager);
        initAdapter();
        imagePager.setAdapter(mPagerAdapter);
        imagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pager_num = position + 1;
                if (position == (draws.length - 1)) {
                    textView1.setVisibility(View.VISIBLE);
                } else {
                    textView1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        imagePager.setOnTouchListener(this);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                SharedPreferenceUtils.getInstance().saveSplash(true);
                SharedPreferenceUtils.getInstance().saveAppVersion(PublicWay.getVersionCode(SplashActivity.this));
                finish();
            }
        });
    }

    @Override
    protected void doOperate() {

    }


    GestureDetector mygesture = new GestureDetector(this);

    private void initAdapter() {
        mPagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(SplashActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(draws[position]);
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                ImageView imageView = new ImageView(SplashActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(draws[position]);
                container.removeView(imageView);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == (View) arg1;
            }

            @Override
            public int getCount() {
                return draws == null ? 0 : draws.length;// 因为会有一个左右尽头的数据
            }
        };
    }

    public boolean onTouch(View v, MotionEvent event) {
        return mygesture.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        if (e1.getX() - e2.getX() > 120) {
            if (pager_num == draws.length) {
                textView1.setVisibility(View.VISIBLE);
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                SharedPreferenceUtils.getInstance().saveSplash(true);
                SharedPreferenceUtils.getInstance().saveAppVersion(PublicWay.getVersionCode(this));
                finish();
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
