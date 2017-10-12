package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.MyViewPagerAdapter;
import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.ShopClass;
import cn.net.chenbao.qbyp.fragment.VipCalssFragment;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import cn.net.chenbao.qbyp.view.TitlePopup;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 木头 on 2016/11/23.
 */

public class VipActivity extends AppCompatActivity implements View.OnClickListener {
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;
    private ViewPager mViewPager;
    private MyViewPagerAdapter viewPagerAdapter;
    private TabLayout mTabLayout;
    private TitlePopup popup;
    private View llCommonMenu;
    private TextView tvShopcartCount;
    private RelativeLayout ll_banner_container;
    private int width;
    private int pxHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.act_vip);
        initHeadBack();
        width = getWindowManager().getDefaultDisplay().getWidth();
        pxHeight = width * 384 / 720;
        tvShopcartCount = (TextView) findViewById(R.id.tv_shopcart_count);
        ll_banner_container = (RelativeLayout) findViewById(R.id.ll_banner_container);
        adImgs = (HorizontalInnerViewPager) findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(this, pxHeight);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        findViewById(R.id.rl_head_right).setOnClickListener(this);
        llCommonMenu = findViewById(R.id.ll_common_menu);
        llCommonMenu.setOnClickListener(this);

        popup = PublicWay.startCommonFunctionPop(this);
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = pxHeight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);
        getCartSum();
        initBanner();
        getIndexClass();
        WWViewUtil.addGuideImage(this, R.drawable.guide, R.id.ll_container);
    }

    protected void initHeadBack() {
        View left = findViewById(R.id.rl_head_left);
        left.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 获取购物车数量
     */
    public void getCartSum() {
        if (WWApplication.getInstance().isLogin()) {
            int Quantity = SharedPreferenceUtils.getInstance().getCartNum();
            if (Quantity > 0) {
                tvShopcartCount.setVisibility(View.VISIBLE);
                if (Quantity > 99) {
                    tvShopcartCount.setText("99+");
                } else {
                    tvShopcartCount.setText(Quantity + "");
                }
            } else {
                tvShopcartCount.setVisibility(View.GONE);
            }
        }
    }

    private void initBanner() {
        RequestParams params = new RequestParams(ApiBaseData.BannersGet());
        params.addBodyParameter("place", Banner.VIP_BANNER + "");
        x.http().get(params,
                new WWXCallBack("BannersGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        List<Banner> list = JSON.parseArray(
                                jsonArray.toJSONString(), Banner.class);
                        if (list != null) {
                            ArrayList<String> mImages = new ArrayList<String>();
                            for (int i = 0; i < list.size(); i++) {
                                mImages.add(getRightImgScreen(list.get(i).PicUrl));
                            }
                            mbAdapter.setData(mImages, list);
                            adImgs.setAdapter(mbAdapter);
                            indicator.setFillColor(getResources().getColor(
                                    R.color.yellow_ww));
                            indicator.setPageColor(0xAAFFFFFF);
                            indicator.setStrokeWidth(0);
                            indicator.setRadius(DensityUtil.dip2px(
                                    VipActivity.this, 3));
                            indicator.setViewPager(adImgs);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                    }
                });

    }

    private String getRightImgScreen(String picUrl) {
        return picUrl.replace("__", "_" + width + "x" + pxHeight);
    }

    /**
     * 获取首页类目
     */
    private void getIndexClass() {
        x.http().get(new RequestParams(ApiShop.Classes()), new WWXCallBack("Classes") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<ShopClass> list = JSON.parseArray(
                        jsonArray.toJSONString(), ShopClass.class);
                if (list.size() > 9) {
                    for (int i = 9; i < list.size(); i++) {
                        list.remove(i);
                    }
                }

                ShopClass shopAll = new ShopClass();
                shopAll.ClassName = getString(R.string.all);
                list.add(0, shopAll);
                for (int i = 0; i < list.size(); i++) {
                    VipCalssFragment vipCalssFragment = VipCalssFragment.newInstance();
                    vipCalssFragment.setClassId(list.get(i).ClassId);
                    viewPagerAdapter.addFragment(vipCalssFragment, list.get(i).ClassName);
                    mTabLayout.addTab(mTabLayout.newTab().setText(list.get(i).ClassName));
                }
                mViewPager.setAdapter(viewPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head_right:
                PublicWay.stratSelfSupportShopCartActivity(this);
                break;
            case R.id.ll_common_menu:
                popup.showWindow(llCommonMenu);
                break;
        }
    }
}
