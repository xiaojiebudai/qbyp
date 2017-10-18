// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WithdrawActivity_ViewBinding<T extends WithdrawActivity> implements Unbinder {
  protected T target;

  @UiThread
  public WithdrawActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tvWithdrawYue = Utils.findRequiredViewAsType(source, R.id.tv_withdraw_yue, "field 'tvWithdrawYue'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvWithdrawYue = null;

    this.target = null;
  }
}
