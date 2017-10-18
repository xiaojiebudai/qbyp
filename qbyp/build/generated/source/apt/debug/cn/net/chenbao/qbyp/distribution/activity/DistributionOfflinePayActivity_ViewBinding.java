// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionOfflinePayActivity_ViewBinding<T extends DistributionOfflinePayActivity> implements Unbinder {
  protected T target;

  private View view2131689694;

  @UiThread
  public DistributionOfflinePayActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.llAccountContainer = Utils.findRequiredViewAsType(source, R.id.ll_account_container, "field 'llAccountContainer'", LinearLayout.class);
    target.tv_order_id_six = Utils.findRequiredViewAsType(source, R.id.tv_order_id_six, "field 'tv_order_id_six'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_commit, "method 'onClick'");
    view2131689694 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.llAccountContainer = null;
    target.tv_order_id_six = null;

    view2131689694.setOnClickListener(null);
    view2131689694 = null;

    this.target = null;
  }
}
