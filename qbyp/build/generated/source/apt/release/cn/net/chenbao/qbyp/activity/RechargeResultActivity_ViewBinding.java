// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

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

public class RechargeResultActivity_ViewBinding<T extends RechargeResultActivity> implements Unbinder {
  protected T target;

  private View view2131689863;

  private View view2131689864;

  private View view2131689833;

  @UiThread
  public RechargeResultActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.tv_shopping, "field 'tvShopping' and method 'onClick'");
    target.tvShopping = Utils.castView(view, R.id.tv_shopping, "field 'tvShopping'", TextView.class);
    view2131689863 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_wallet, "field 'tvWallet' and method 'onClick'");
    target.tvWallet = Utils.castView(view, R.id.tv_wallet, "field 'tvWallet'", TextView.class);
    view2131689864 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llSuccess = Utils.findRequiredViewAsType(source, R.id.ll_success, "field 'llSuccess'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_pay_again, "field 'tvPayAgain' and method 'onClick'");
    target.tvPayAgain = Utils.castView(view, R.id.tv_pay_again, "field 'tvPayAgain'", TextView.class);
    view2131689833 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llFail = Utils.findRequiredViewAsType(source, R.id.ll_fail, "field 'llFail'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvShopping = null;
    target.tvWallet = null;
    target.llSuccess = null;
    target.tvPayAgain = null;
    target.llFail = null;

    view2131689863.setOnClickListener(null);
    view2131689863 = null;
    view2131689864.setOnClickListener(null);
    view2131689864 = null;
    view2131689833.setOnClickListener(null);
    view2131689833 = null;

    this.target = null;
  }
}
