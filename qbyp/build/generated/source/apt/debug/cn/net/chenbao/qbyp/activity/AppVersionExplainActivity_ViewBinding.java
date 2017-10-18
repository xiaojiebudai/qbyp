// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

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

public class AppVersionExplainActivity_ViewBinding<T extends AppVersionExplainActivity> implements Unbinder {
  protected T target;

  private View view2131689641;

  @UiThread
  public AppVersionExplainActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvOldOrNewest = Utils.findRequiredViewAsType(source, R.id.tv_old_or_newest, "field 'tvOldOrNewest'", TextView.class);
    target.tvCurrentVersionCode = Utils.findRequiredViewAsType(source, R.id.tv_current_verssion_code, "field 'tvCurrentVersionCode'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_check_new_verssion, "field 'tvCheckNewVersion' and method 'onClick'");
    target.tvCheckNewVersion = Utils.castView(view, R.id.tv_check_new_verssion, "field 'tvCheckNewVersion'", TextView.class);
    view2131689641 = view;
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

    target.tvOldOrNewest = null;
    target.tvCurrentVersionCode = null;
    target.tvCheckNewVersion = null;

    view2131689641.setOnClickListener(null);
    view2131689641 = null;

    this.target = null;
  }
}
