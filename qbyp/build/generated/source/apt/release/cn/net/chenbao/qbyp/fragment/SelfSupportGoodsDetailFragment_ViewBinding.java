// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.view.CirclePageIndicator;
import cn.net.chenbao.qbyp.view.HorizontalInnerViewPager;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportGoodsDetailFragment_ViewBinding<T extends SelfSupportGoodsDetailFragment> implements Unbinder {
  protected T target;

  private View view2131690242;

  private View view2131689665;

  private View view2131690246;

  private View view2131690222;

  private View view2131689669;

  private View view2131689671;

  private View view2131690250;

  @UiThread
  public SelfSupportGoodsDetailFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.vpGoodsDetailsImgs = Utils.findRequiredViewAsType(source, R.id.vp_goods_details_imgs, "field 'vpGoodsDetailsImgs'", HorizontalInnerViewPager.class);
    target.indicator = Utils.findRequiredViewAsType(source, R.id.indicator, "field 'indicator'", CirclePageIndicator.class);
    target.llGoodsDetailsBannerContainer = Utils.findRequiredViewAsType(source, R.id.ll_goods_details_banner_container, "field 'llGoodsDetailsBannerContainer'", RelativeLayout.class);
    target.tvGoodsName = Utils.findRequiredViewAsType(source, R.id.tv_goods_name, "field 'tvGoodsName'", TextView.class);
    target.tvDescription = Utils.findRequiredViewAsType(source, R.id.tv_description, "field 'tvDescription'", TextView.class);
    target.tvPriceNow = Utils.findRequiredViewAsType(source, R.id.tv_price_now, "field 'tvPriceNow'", TextView.class);
    target.tvPriceGone = Utils.findRequiredViewAsType(source, R.id.tv_price_gone, "field 'tvPriceGone'", TextView.class);
    target.tvSales = Utils.findRequiredViewAsType(source, R.id.tv_sales, "field 'tvSales'", TextView.class);
    target.tvStandard = Utils.findRequiredViewAsType(source, R.id.tv_standard, "field 'tvStandard'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_choose_standard, "field 'llChooseStandard' and method 'onClick'");
    target.llChooseStandard = Utils.castView(view, R.id.ll_choose_standard, "field 'llChooseStandard'", LinearLayout.class);
    view2131690242 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_parameter, "field 'llParameter' and method 'onClick'");
    target.llParameter = Utils.castView(view, R.id.ll_parameter, "field 'llParameter'", LinearLayout.class);
    view2131689665 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvShopsName = Utils.findRequiredViewAsType(source, R.id.tv_shops_name, "field 'tvShopsName'", TextView.class);
    target.tvShopsAddress = Utils.findRequiredViewAsType(source, R.id.tv_shops_address, "field 'tvShopsAddress'", TextView.class);
    target.tvEvaluateNum = Utils.findRequiredViewAsType(source, R.id.tv_evaluate_num, "field 'tvEvaluateNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_evaluate_more, "field 'llEvaluateMore' and method 'onClick'");
    target.llEvaluateMore = Utils.castView(view, R.id.ll_evaluate_more, "field 'llEvaluateMore'", LinearLayout.class);
    view2131690246 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llEvaluate = Utils.findRequiredViewAsType(source, R.id.ll_evaluate, "field 'llEvaluate'", LinearLayout.class);
    target.ivCollect = Utils.findRequiredViewAsType(source, R.id.iv_collect, "field 'ivCollect'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.ll_collect, "field 'llCollect' and method 'onClick'");
    target.llCollect = Utils.castView(view, R.id.ll_collect, "field 'llCollect'", LinearLayout.class);
    view2131690222 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvShopCartCount = Utils.findRequiredViewAsType(source, R.id.tv_shop_cart_count, "field 'tvShopCartCount'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_shop_car, "field 'rlShopCar' and method 'onClick'");
    target.rlShopCar = Utils.castView(view, R.id.rl_shop_car, "field 'rlShopCar'", RelativeLayout.class);
    view2131689669 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_add_cart, "field 'tvAddCart' and method 'onClick'");
    target.tvAddCart = Utils.castView(view, R.id.tv_add_cart, "field 'tvAddCart'", TextView.class);
    view2131689671 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_go_pay, "field 'tvGoPay' and method 'onClick'");
    target.tvGoPay = Utils.castView(view, R.id.tv_go_pay, "field 'tvGoPay'", TextView.class);
    view2131690250 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tv_jifen_can_use = Utils.findRequiredViewAsType(source, R.id.tv_jifen_can_use, "field 'tv_jifen_can_use'", TextView.class);
    target.tv_jifen_used = Utils.findRequiredViewAsType(source, R.id.tv_jifen_used, "field 'tv_jifen_used'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.vpGoodsDetailsImgs = null;
    target.indicator = null;
    target.llGoodsDetailsBannerContainer = null;
    target.tvGoodsName = null;
    target.tvDescription = null;
    target.tvPriceNow = null;
    target.tvPriceGone = null;
    target.tvSales = null;
    target.tvStandard = null;
    target.llChooseStandard = null;
    target.llParameter = null;
    target.tvShopsName = null;
    target.tvShopsAddress = null;
    target.tvEvaluateNum = null;
    target.llEvaluateMore = null;
    target.llEvaluate = null;
    target.ivCollect = null;
    target.llCollect = null;
    target.tvShopCartCount = null;
    target.rlShopCar = null;
    target.tvAddCart = null;
    target.tvGoPay = null;
    target.tv_jifen_can_use = null;
    target.tv_jifen_used = null;

    view2131690242.setOnClickListener(null);
    view2131690242 = null;
    view2131689665.setOnClickListener(null);
    view2131689665 = null;
    view2131690246.setOnClickListener(null);
    view2131690246 = null;
    view2131690222.setOnClickListener(null);
    view2131690222 = null;
    view2131689669.setOnClickListener(null);
    view2131689669 = null;
    view2131689671.setOnClickListener(null);
    view2131689671 = null;
    view2131690250.setOnClickListener(null);
    view2131690250 = null;

    this.target = null;
  }
}
