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

public class SelfSupportMakesureOrderActivity_ViewBinding<T extends SelfSupportMakesureOrderActivity> implements Unbinder {
  protected T target;

  private View view2131689876;

  private View view2131689883;

  @UiThread
  public SelfSupportMakesureOrderActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvReceiverName = Utils.findRequiredViewAsType(source, R.id.tv_receiver_name, "field 'tvReceiverName'", TextView.class);
    target.tvReceiverPhone = Utils.findRequiredViewAsType(source, R.id.tv_receiver_phone, "field 'tvReceiverPhone'", TextView.class);
    target.tvReceiverAddress = Utils.findRequiredViewAsType(source, R.id.tv_receiver_address, "field 'tvReceiverAddress'", TextView.class);
    target.llOrderContainer = Utils.findRequiredViewAsType(source, R.id.ll_order_container, "field 'llOrderContainer'", LinearLayout.class);
    target.tvFreight = Utils.findRequiredViewAsType(source, R.id.tv_freight, "field 'tvFreight'", TextView.class);
    target.tvTotalGoodNum = Utils.findRequiredViewAsType(source, R.id.tv_total_good_num, "field 'tvTotalGoodNum'", TextView.class);
    target.tvShouldPayTotalMoney = Utils.findRequiredViewAsType(source, R.id.tv_should_pay_total_money, "field 'tvShouldPayTotalMoney'", TextView.class);
    target.tvCanDeductionTips = Utils.findRequiredViewAsType(source, R.id.tv_can_deduction_tips, "field 'tvCanDeductionTips'", TextView.class);
    target.llIntegralItemContainer = Utils.findRequiredViewAsType(source, R.id.ll_integral_item_container, "field 'llIntegralItemContainer'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.rl_address_manager, "method 'onClick'");
    view2131689876 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_commit_order, "method 'onClick'");
    view2131689883 = view;
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

    target.tvReceiverName = null;
    target.tvReceiverPhone = null;
    target.tvReceiverAddress = null;
    target.llOrderContainer = null;
    target.tvFreight = null;
    target.tvTotalGoodNum = null;
    target.tvShouldPayTotalMoney = null;
    target.tvCanDeductionTips = null;
    target.llIntegralItemContainer = null;

    view2131689876.setOnClickListener(null);
    view2131689876 = null;
    view2131689883.setOnClickListener(null);
    view2131689883 = null;

    this.target = null;
  }
}
