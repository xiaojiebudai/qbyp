package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.bean.Banner;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.adapter.listview.BannerPagerAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;

/**
 * Created by Administrator on 2016/12/27.
 */

public class RechargeActivity extends FatherActivity {
    public static final int REQUEST_CODE = 666;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.rb_0)
    RadioButton rb0;
    @BindView(R.id.rb_1)
    RadioButton rb1;
    @BindView(R.id.rb_2)
    RadioButton rb2;
    @BindView(R.id.rg_price)
    RadioGroup rgPrice;
    @BindView(R.id.bt_pay)
    Button btPay;
    @BindView(R.id.tv_relevel)
    TextView tvRelevel;
    @BindView(R.id.ll_quyu)
    LinearLayout llQuyu;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.ll_banner_container)
    RelativeLayout ll_banner_container;
    private HorizontalInnerViewPager adImgs;
    private CirclePageIndicator indicator;
    private BannerPagerAdapter mbAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.act_recharge;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.recharge_center, true);
        width = getWindowManager().getDefaultDisplay().getWidth();
        pxHeight = width * 165 / 720;
    }

    @Override
    protected void initView() {
        adImgs = (HorizontalInnerViewPager) findViewById(R.id.vp_imgs);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mbAdapter = new BannerPagerAdapter(this, pxHeight);
        rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                        llQuyu.setVisibility(View.VISIBLE);
                        tvRelevel.setText(R.string.san);
                        tvJifen.setText(R.string.integral_20000);
                        break;
                    case R.id.rb_1:
                        llQuyu.setVisibility(View.GONE);
                        tvRelevel.setText(R.string.er);
                        tvJifen.setText(R.string.integral_10000);
                        break;
                    case R.id.rb_2:
                        llQuyu.setVisibility(View.GONE);
                        tvRelevel.setText(R.string.yi);
                        tvJifen.setText(R.string.integral_1000);
                        break;
                }
            }
        });
        ViewGroup.LayoutParams params = ll_banner_container.getLayoutParams();
        params.height = pxHeight;
        params.width = width;
        ll_banner_container.setLayoutParams(params);
    }

    @Override
    protected void doOperate() {
        initBanner();
        getUserInfo();
    }

    private void initBanner() {
        RequestParams params = new RequestParams(ApiBaseData.BannersGet());
        params.addBodyParameter("place", Banner.RECHARGE_CENTER_BANNER + "");
        showWaitDialog();
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
                                    RechargeActivity.this, 3));
                            indicator.setViewPager(adImgs);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    private int width;
    private int pxHeight;

    private String getRightImgScreen(String picUrl) {
        return picUrl.replace("__", "_" + width + "x" + pxHeight);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_pay)
    public void onClick() {
        double savAmt = 20000;
        switch (rgPrice.getCheckedRadioButtonId()) {
            case R.id.rb_0:
                savAmt = 20000;
                break;
            case R.id.rb_1:
                savAmt = 10000;
                break;
            case R.id.rb_2:
                savAmt = 1000;
                break;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("savAmt", savAmt);
        RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiUser.VipSaving());
        final double finalSavAmt = savAmt;
        showWaitDialog();
        x.http().post(sessionParams, new WWXCallBack("VipSaving") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                long orderId = JSONObject.parseObject(data.getString("Data")).getLong("SheetNo");
                Intent intent = new Intent(RechargeActivity.this, PayOrdersActivity.class);
                intent.putExtra("money", finalSavAmt);
                intent.putExtra("orderId", orderId);
                intent.putExtra(Consts.KEY_MODULE, PayOrdersActivity.RECHARGE);
                startActivityForResult(intent, 888);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        RequestParams params = new RequestParams(ApiUser.getUserInfo());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("InfoGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                User user = JSON.parseObject(data.getString("Data"), User.class);
                ImageUtils.setCircleHeaderImage(RechargeActivity.this, user.HeadUrl,
                        ivPhoto);
                tvName.setText(user.UserName);

                switch (user.LevelId) {
                    case 0:
                        tvLevel.setText(R.string.common_vip);
                        break;
                    case 1:
                        tvLevel.setText(R.string.yi);
                        break;
                    case 2:
                        tvLevel.setText(R.string.er);
                        break;
                    case 3:
                        tvLevel.setText(R.string.san);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);

        if (arg0 == 888) {
            if (arg1 == RESULT_OK) {
                PublicWay.stratRechargeResultActivity(this, RechargeResultActivity.SUCCESS, REQUEST_CODE);

            } else {
                PublicWay.stratRechargeResultActivity(this, RechargeResultActivity.FAILE, REQUEST_CODE);
            }
            setResult(RESULT_OK);
            finish();
        }
    }
}
