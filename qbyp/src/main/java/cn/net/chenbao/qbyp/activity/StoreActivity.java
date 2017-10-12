package cn.net.chenbao.qbyp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.api.ApiSeller;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.CollectBean;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.fragment.EvaluateFragment;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.GoodsFragment;
import cn.net.chenbao.qbyp.fragment.SellerFragment;
import cn.net.chenbao.qbyp.utils.Arith;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.JsonUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.view.TabScrollView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 店铺界面
 *
 * @author xl
 * @date 2016-7-27 下午9:12:01
 * @description
 */
public class StoreActivity extends FatherActivity implements OnClickListener {

    /**
     * 收藏
     */
    private View collect;
    /**
     * 商家id
     */
    private long sellerId;
    private ImageView mIv;
    private TextView mTvShopName;
    private TextView mTvTips;
    private TextView mTvIntegral;
    private TextView mTvPoint;
    private RatingBar mRb;
    private RelativeLayout mRLCollect;
    private String json;
    private LinearLayout mllBg;
    private float pinfen;
    private RelativeLayout rl_image_bg;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    TabScrollView tabScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
                    List<FatherFragment> fragments = new ArrayList<FatherFragment>();
                    String[] array = getResources().getStringArray(
                            R.array.store_tab);
                    List<String> tabs = Arrays.asList(array);
                    GoodsFragment goodsFragment = GoodsFragment.newInstance(
                            sellerId, json);
                    EvaluateFragment evaluateFragment = EvaluateFragment
                            .newInstance(sellerId, pinfen);
                    SellerFragment sellerFragment = SellerFragment
                            .newInstance(json);
                    fragments.add(goodsFragment);
                    fragments.add(evaluateFragment);
                    fragments.add(sellerFragment);
                    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                    CommonPageAdapter adapter = new CommonPageAdapter(
                            StoreActivity.this, tabs, fragments,
                            getSupportFragmentManager(), viewPager, 0, false);
                    viewPager.setAdapter(adapter);
                    tabScrollView.setViewPager(viewPager);
                    break;

                default:
                    break;
            }

        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.act_store;
    }

    @Override
    protected void initValues() {
        initDefautHead("", true);
        getMyCollectStore();
        sellerId = getIntent().getLongExtra(Consts.KEY_DATA, -1);
        collect = findViewById(R.id.rl_collect);
        collect.setOnClickListener(this);
    }

    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    /**
     * 获得收藏商家列表
     */
    private void getMyCollectStore() {
        RequestParams params = new RequestParams(ApiUser.getFavSellerGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("FavSellerGet", this) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                List<CollectBean> array = JSON.parseArray(
                        jsonArray.toJSONString(), CollectBean.class);
                for (CollectBean collectBean : array) {
                    if (collectBean.SellerId == sellerId) {
                        collect.setSelected(true);
                    }
                }

            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    protected void initView() {
        mIv = (ImageView) findViewById(R.id.iv_image);
        mTvShopName = (TextView) findViewById(R.id.tv_head_center);
        mTvTips = (TextView) findViewById(R.id.tv_tips);
        mTvIntegral = (TextView) findViewById(R.id.tv_integral);
        mTvPoint = (TextView) findViewById(R.id.tv_point);
        mllBg = (LinearLayout) findViewById(R.id.ll_bg);
        mRb = (RatingBar) findViewById(R.id.rb);
        rl_image_bg = (RelativeLayout) findViewById(R.id.rl_image_bg);

        //重新设置高度
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) rl_image_bg.getLayoutParams();
            params1.height = DensityUtil.dip2px(this, 122) + WWViewUtil.getStatusBarHeight(this);
            rl_image_bg.setLayoutParams(params1);
            rl_image_bg.setPadding(0, WWViewUtil.getStatusBarHeight(this), 0, 0);
        }
    }

    @Override
    protected void doOperate() {
        RequestParams params = new RequestParams(ApiSeller.getSeller());
        params.addBodyParameter("sellerId", sellerId + "");
        showWaitDialog();
        x.http().get(params, new WWXCallBack("SellerGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (JsonUtils.parseJsonSuccess(data)) {
                    JSONObject jsonObject2 = data.getJSONObject("Data");
                    TradesMessage parseObject = JSON.parseObject(
                            jsonObject2.toJSONString(), TradesMessage.class);
                    json = jsonObject2.toJSONString();
                    ImageUtils.setOwnRadiusImage(StoreActivity.this,
                            parseObject.ShopPicture, mIv, 3);
                    //店铺顶部背景
//                    Glide.with(StoreActivity.this)
//                            .load(parseObject.ShopPicture).asBitmap()
//                            .into(new SimpleTarget<Bitmap>() {
//                                @Override
//                                public void onResourceReady(Bitmap arg0,
//                                                            GlideAnimation<? super Bitmap> arg1) {
//                                    Bitmap doBlur = BlurImageView.doBlur(
//                                            StoreActivity.this, arg0, 20, 10);
//                                    BitmapDrawable bitmapDrawable = new BitmapDrawable(
//                                            doBlur);
//                                    mllBg.setBackgroundDrawable(bitmapDrawable);
//                                }
//                            });
                    if (parseObject.SupportDeliver) {
                        mTvTips.setText(getString(R.string.support_deliver)
                                + "/" + getString(R.string.full)
                                + parseObject.FullDelivery
                                + getString(R.string.full_free_deliver));
                    } else {
                        mTvTips.setText(getString(R.string.no_support_deliver));
                    }
                    pinfen = parseObject.JudgeLevel;

                    double star = Arith.round(parseObject.JudgeLevel, 1);
                    mTvPoint.setText(String.format(getString(R.string.have_sale_number), parseObject.OrderCount));
                    mTvIntegral.setText(String.format(getString(R.string.percent_rate), (int) (parseObject.ConsumePay * 100)));
                    mRb.setStar((float) star);
                    mTvShopName.setText(parseObject.SellerName);
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_collect:
                requestCollect();
                break;
            default:
                break;
        }
    }

    private void requestCollect() {
        addOrRemoveShop(!collect.isSelected());
    }

    /**
     * 移除或添加商家
     *
     * @param isAdd
     */
    public void addOrRemoveShop(final boolean isAdd) {
        String ulr = "";
        String result = null;
        if (isAdd) {
            ulr = ApiUser.addCollectShop();
            result = "FavSellerAdd";
        } else {
            ulr = ApiUser.removeCollectShop();
            result = "FavSellerRemove";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", SharedPreferenceUtils.getInstance()
                .getSessionId());
        jsonObject.put("sellerId", sellerId);
        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, ulr),
                new WWXCallBack(result) {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWToast.showShort(R.string.collect_success);
                        collect.setSelected(isAdd);
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
        if (arg1 == RESULT_OK) {
            if (arg0 == GoodsFragment.REQUEST_CODE) {
                finish();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
