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

public class MakeSuerOrderActivity_ViewBinding<T extends MakeSuerOrderActivity> implements Unbinder {
  protected T target;

  private View view2131690014;

  private View view2131690018;

  private View view2131690019;

  @UiThread
  public MakeSuerOrderActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.llCanUseIntegral = Utils.findRequiredViewAsType(source, R.id.ll_can_use_integral, "field 'llCanUseIntegral'", LinearLayout.class);
    target.tvConsumeIntegral = Utils.findRequiredViewAsType(source, R.id.tv_consume_integral, "field 'tvConsumeIntegral'", TextView.class);
    target.tvIntegralDeductionMoney = Utils.findRequiredViewAsType(source, R.id.tv_integral_deduction_money, "field 'tvIntegralDeductionMoney'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_toggle_integral, "field 'ivToggleIntegral' and method 'onClick'");
    target.ivToggleIntegral = Utils.castView(view, R.id.iv_toggle_integral, "field 'ivToggleIntegral'", ImageView.class);
    view2131690014 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llCanUseU = Utils.findRequiredViewAsType(source, R.id.ll_can_use_U, "field 'llCanUseU'", LinearLayout.class);
    target.tvConsumeU = Utils.findRequiredViewAsType(source, R.id.tv_consume_U, "field 'tvConsumeU'", TextView.class);
    target.tvUDeductionMoney = Utils.findRequiredViewAsType(source, R.id.tv_U_deduction_money, "field 'tvUDeductionMoney'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_toggle_U, "field 'ivToggleU' and method 'onClick'");
    target.ivToggleU = Utils.castView(view, R.id.iv_toggle_U, "field 'ivToggleU'", ImageView.class);
    view2131690018 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llYue = Utils.findRequiredViewAsType(source, R.id.ll_yue, "field 'llYue'", LinearLayout.class);
    target.tvYue = Utils.findRequiredViewAsType(source, R.id.tv_yue, "field 'tvYue'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_toggle_yue, "field 'ivToggleYue' and method 'onClick'");
    target.ivToggleYue = Utils.castView(view, R.id.iv_toggle_yue, "field 'ivToggleYue'", ImageView.class);
    view2131690019 = view;
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

    target.llCanUseIntegral = null;
    target.tvConsumeIntegral = null;
    target.tvIntegralDeductionMoney = null;
    target.ivToggleIntegral = null;
    target.llCanUseU = null;
    target.tvConsumeU = null;
    target.tvUDeductionMoney = null;
    target.ivToggleU = null;
    target.llYue = null;
    target.tvYue = null;
    target.ivToggleYue = null;

    view2131690014.setOnClickListener(null);
    view2131690014 = null;
    view2131690018.setOnClickListener(null);
    view2131690018 = null;
    view2131690019.setOnClickListener(null);
    view2131690019 = null;

    this.target = null;
  }
}
