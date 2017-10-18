// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionCertSuccessActivity_ViewBinding<T extends DistributionCertSuccessActivity> implements Unbinder {
  protected T target;

  private View view2131689655;

  private View view2131689656;

  @UiThread
  public DistributionCertSuccessActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvTip = Utils.findRequiredViewAsType(source, R.id.tv_tip, "field 'tvTip'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_go_person_center, "method 'onClick'");
    view2131689655 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_go_order_list, "method 'onClick'");
    view2131689656 = view;
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

    target.tvTip = null;

    view2131689655.setOnClickListener(null);
    view2131689655 = null;
    view2131689656.setOnClickListener(null);
    view2131689656 = null;

    this.target = null;
  }
}
