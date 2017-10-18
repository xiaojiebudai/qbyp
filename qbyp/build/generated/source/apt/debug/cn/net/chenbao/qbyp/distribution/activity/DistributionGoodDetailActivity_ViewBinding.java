// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
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

public class DistributionGoodDetailActivity_ViewBinding<T extends DistributionGoodDetailActivity> implements Unbinder {
  protected T target;

  private View view2131689672;

  private View view2131689669;

  private View view2131689671;

  @UiThread
  public DistributionGoodDetailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.webDetail = Utils.findRequiredViewAsType(source, R.id.web_detail, "field 'webDetail'", WebView.class);
    target.tvGoodsName = Utils.findRequiredViewAsType(source, R.id.tv_goods_name, "field 'tvGoodsName'", TextView.class);
    target.tvDescription = Utils.findRequiredViewAsType(source, R.id.tv_description, "field 'tvDescription'", TextView.class);
    target.tvPrice = Utils.findRequiredViewAsType(source, R.id.tv_price, "field 'tvPrice'", TextView.class);
    target.tvGoodIntegral = Utils.findRequiredViewAsType(source, R.id.tv_good_integral, "field 'tvGoodIntegral'", TextView.class);
    target.vpGoodsDetailsImgs = Utils.findRequiredViewAsType(source, R.id.vp_goods_details_imgs, "field 'vpGoodsDetailsImgs'", HorizontalInnerViewPager.class);
    target.indicator = Utils.findRequiredViewAsType(source, R.id.indicator, "field 'indicator'", CirclePageIndicator.class);
    target.vNoParams = Utils.findRequiredViewAsType(source, R.id.v_no_params, "field 'vNoParams'", TextView.class);
    target.llParamsContainer = Utils.findRequiredViewAsType(source, R.id.ll_params_container, "field 'llParamsContainer'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_make_sure, "field 'tvMakeSure' and method 'onViewClicked'");
    target.tvMakeSure = Utils.castView(view, R.id.tv_make_sure, "field 'tvMakeSure'", TextView.class);
    view2131689672 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvShopCartCount = Utils.findRequiredViewAsType(source, R.id.tv_shop_cart_count, "field 'tvShopCartCount'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_shop_car, "field 'rlShopCar' and method 'onViewClicked'");
    target.rlShopCar = Utils.castView(view, R.id.rl_shop_car, "field 'rlShopCar'", RelativeLayout.class);
    view2131689669 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_add_cart, "field 'tvAddCart' and method 'onViewClicked'");
    target.tvAddCart = Utils.castView(view, R.id.tv_add_cart, "field 'tvAddCart'", TextView.class);
    view2131689671 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.webDetail = null;
    target.tvGoodsName = null;
    target.tvDescription = null;
    target.tvPrice = null;
    target.tvGoodIntegral = null;
    target.vpGoodsDetailsImgs = null;
    target.indicator = null;
    target.vNoParams = null;
    target.llParamsContainer = null;
    target.tvMakeSure = null;
    target.tvShopCartCount = null;
    target.rlShopCar = null;
    target.tvAddCart = null;

    view2131689672.setOnClickListener(null);
    view2131689672 = null;
    view2131689669.setOnClickListener(null);
    view2131689669 = null;
    view2131689671.setOnClickListener(null);
    view2131689671 = null;

    this.target = null;
  }
}
