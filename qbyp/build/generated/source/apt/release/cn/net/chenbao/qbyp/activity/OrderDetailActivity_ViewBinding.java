// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrderDetailActivity_ViewBinding<T extends OrderDetailActivity> implements Unbinder {
  protected T target;

  @UiThread
  public OrderDetailActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tvPrivateIntegral = Utils.findRequiredViewAsType(source, R.id.tv_private_integral, "field 'tvPrivateIntegral'", TextView.class);
    target.rl_0 = Utils.findRequiredViewAsType(source, R.id.rl_0, "field 'rl_0'", RelativeLayout.class);
    target.tv_ub = Utils.findRequiredViewAsType(source, R.id.tv_ub, "field 'tv_ub'", TextView.class);
    target.rl_1 = Utils.findRequiredViewAsType(source, R.id.rl_1, "field 'rl_1'", RelativeLayout.class);
    target.tv_can_withdr_yue = Utils.findRequiredViewAsType(source, R.id.tv_can_withdr_yue, "field 'tv_can_withdr_yue'", TextView.class);
    target.rl_2 = Utils.findRequiredViewAsType(source, R.id.rl_2, "field 'rl_2'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvPrivateIntegral = null;
    target.rl_0 = null;
    target.tv_ub = null;
    target.rl_1 = null;
    target.tv_can_withdr_yue = null;
    target.rl_2 = null;

    this.target = null;
  }
}
