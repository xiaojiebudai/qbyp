// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EmptyViewActivity_ViewBinding<T extends EmptyViewActivity> implements Unbinder {
  protected T target;

  private View view2131690156;

  @UiThread
  public EmptyViewActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.ivEmptyBg = Utils.findRequiredViewAsType(source, R.id.iv_empty_bg, "field 'ivEmptyBg'", ImageView.class);
    target.tvInfo = Utils.findRequiredViewAsType(source, R.id.tv_info, "field 'tvInfo'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_go, "field 'tvGo' and method 'onViewClicked'");
    target.tvGo = Utils.castView(view, R.id.tv_go, "field 'tvGo'", TextView.class);
    view2131690156 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivEmptyBg = null;
    target.tvInfo = null;
    target.tvGo = null;

    view2131690156.setOnClickListener(null);
    view2131690156 = null;

    this.target = null;
  }
}
