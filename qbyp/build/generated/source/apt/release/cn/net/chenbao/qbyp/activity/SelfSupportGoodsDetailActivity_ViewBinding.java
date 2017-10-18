// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportGoodsDetailActivity_ViewBinding<T extends SelfSupportGoodsDetailActivity> implements Unbinder {
  protected T target;

  private View view2131689870;

  private View view2131689871;

  private View view2131689873;

  @UiThread
  public SelfSupportGoodsDetailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.rl_head_back, "field 'rlHeadBack' and method 'onClick'");
    target.rlHeadBack = Utils.castView(view, R.id.rl_head_back, "field 'rlHeadBack'", RelativeLayout.class);
    view2131689870 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.rlFragment = Utils.findRequiredViewAsType(source, R.id.rl_fragment, "field 'rlFragment'", RelativeLayout.class);
    target.viewShopTab = Utils.findRequiredView(source, R.id.view_shop_tab, "field 'viewShopTab'");
    target.viewDetailTab = Utils.findRequiredView(source, R.id.view_detail_tab, "field 'viewDetailTab'");
    view = Utils.findRequiredView(source, R.id.tv_head_center_left, "method 'onClick'");
    view2131689871 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_head_center_right, "method 'onClick'");
    view2131689873 = view;
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

    target.rlHeadBack = null;
    target.rlFragment = null;
    target.viewShopTab = null;
    target.viewDetailTab = null;

    view2131689870.setOnClickListener(null);
    view2131689870 = null;
    view2131689871.setOnClickListener(null);
    view2131689871 = null;
    view2131689873.setOnClickListener(null);
    view2131689873 = null;

    this.target = null;
  }
}
