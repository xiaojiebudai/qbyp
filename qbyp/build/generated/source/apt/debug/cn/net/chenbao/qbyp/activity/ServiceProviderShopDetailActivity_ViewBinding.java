// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceProviderShopDetailActivity_ViewBinding<T extends ServiceProviderShopDetailActivity> implements Unbinder {
  protected T target;

  private View view2131689928;

  private View view2131689841;

  private View view2131689705;

  @UiThread
  public ServiceProviderShopDetailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.iv_seller_shop_imgs, "field 'ivSellerShopImgs' and method 'onClick'");
    target.ivSellerShopImgs = Utils.castView(view, R.id.iv_seller_shop_imgs, "field 'ivSellerShopImgs'", ImageView.class);
    view2131689928 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvSellerNames = Utils.findRequiredViewAsType(source, R.id.tv_seller_names, "field 'tvSellerNames'", TextView.class);
    target.tvIndustryType = Utils.findRequiredViewAsType(source, R.id.tv_industry_type, "field 'tvIndustryType'", TextView.class);
    target.tvServiceType = Utils.findRequiredViewAsType(source, R.id.tv_service_type, "field 'tvServiceType'", TextView.class);
    target.tvAuthState = Utils.findRequiredViewAsType(source, R.id.tv_auth_state, "field 'tvAuthState'", TextView.class);
    target.tvServiceTime = Utils.findRequiredViewAsType(source, R.id.tv_service_time, "field 'tvServiceTime'", TextView.class);
    target.tvState = Utils.findRequiredViewAsType(source, R.id.tv_state, "field 'tvState'", TextView.class);
    target.tvLegalName = Utils.findRequiredViewAsType(source, R.id.tv_legal_name, "field 'tvLegalName'", TextView.class);
    target.tvUserPhone = Utils.findRequiredViewAsType(source, R.id.tv_user_phone, "field 'tvUserPhone'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_phone, "field 'llPhone' and method 'onClick'");
    target.llPhone = Utils.castView(view, R.id.ll_phone, "field 'llPhone'", LinearLayout.class);
    view2131689841 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_address, "field 'llAddress' and method 'onClick'");
    target.llAddress = Utils.castView(view, R.id.ll_address, "field 'llAddress'", LinearLayout.class);
    view2131689705 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivSellerShopImgs = null;
    target.tvSellerNames = null;
    target.tvIndustryType = null;
    target.tvServiceType = null;
    target.tvAuthState = null;
    target.tvServiceTime = null;
    target.tvState = null;
    target.tvLegalName = null;
    target.tvUserPhone = null;
    target.llPhone = null;
    target.tvAddress = null;
    target.llAddress = null;

    view2131689928.setOnClickListener(null);
    view2131689928 = null;
    view2131689841.setOnClickListener(null);
    view2131689841 = null;
    view2131689705.setOnClickListener(null);
    view2131689705 = null;

    this.target = null;
  }
}
