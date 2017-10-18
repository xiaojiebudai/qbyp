// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionMakeSureOrderActivity_ViewBinding<T extends DistributionMakeSureOrderActivity> implements Unbinder {
  protected T target;

  private View view2131689692;

  private View view2131689691;

  private View view2131689674;

  private View view2131689694;

  @UiThread
  public DistributionMakeSureOrderActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvReceiverName = Utils.findRequiredViewAsType(source, R.id.tv_receiver_name, "field 'tvReceiverName'", TextView.class);
    target.tvReceiverPhone = Utils.findRequiredViewAsType(source, R.id.tv_receiver_phone, "field 'tvReceiverPhone'", TextView.class);
    target.tvReceiverAddress = Utils.findRequiredViewAsType(source, R.id.tv_receiver_address, "field 'tvReceiverAddress'", TextView.class);
    target.tvTotalMoney = Utils.findRequiredViewAsType(source, R.id.tv_total_money, "field 'tvTotalMoney'", TextView.class);
    target.tv_money_all = Utils.findRequiredViewAsType(source, R.id.tv_money_all, "field 'tv_money_all'", TextView.class);
    target.tvTotalIntegral = Utils.findRequiredViewAsType(source, R.id.tv_total_integral, "field 'tvTotalIntegral'", TextView.class);
    target.etMessage = Utils.findRequiredViewAsType(source, R.id.et_message, "field 'etMessage'", EditText.class);
    target.tvInputLimitTip = Utils.findRequiredViewAsType(source, R.id.tv_input_limit_tip, "field 'tvInputLimitTip'", TextView.class);
    target.tvInputNum = Utils.findRequiredViewAsType(source, R.id.tv_input_num, "field 'tvInputNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_document, "field 'tv_document' and method 'onClick'");
    target.tv_document = Utils.castView(view, R.id.tv_document, "field 'tv_document'", TextView.class);
    view2131689692 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvInputTotal = Utils.findRequiredViewAsType(source, R.id.tv_input_total, "field 'tvInputTotal'", TextView.class);
    target.ll_container = Utils.findRequiredViewAsType(source, R.id.ll_container, "field 'll_container'", LinearLayout.class);
    target.ll_doucument = Utils.findRequiredViewAsType(source, R.id.ll_doucument, "field 'll_doucument'", LinearLayout.class);
    target.tvNoAddress = Utils.findRequiredViewAsType(source, R.id.tv_no_address, "field 'tvNoAddress'", TextView.class);
    target.tv_postage = Utils.findRequiredViewAsType(source, R.id.tv_postage, "field 'tv_postage'", TextView.class);
    target.tv_postage_weight = Utils.findRequiredViewAsType(source, R.id.tv_postage_weight, "field 'tv_postage_weight'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_checkbox, "field 'iv_checkbox' and method 'onClick'");
    target.iv_checkbox = Utils.castView(view, R.id.iv_checkbox, "field 'iv_checkbox'", ImageView.class);
    view2131689691 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.sv = Utils.findRequiredViewAsType(source, R.id.sv, "field 'sv'", ScrollView.class);
    target.llTip = Utils.findRequiredViewAsType(source, R.id.ll_tip, "field 'llTip'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.rl_address, "method 'onClick'");
    view2131689674 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_commit, "method 'onClick'");
    view2131689694 = view;
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
    target.tvTotalMoney = null;
    target.tv_money_all = null;
    target.tvTotalIntegral = null;
    target.etMessage = null;
    target.tvInputLimitTip = null;
    target.tvInputNum = null;
    target.tv_document = null;
    target.tvInputTotal = null;
    target.ll_container = null;
    target.ll_doucument = null;
    target.tvNoAddress = null;
    target.tv_postage = null;
    target.tv_postage_weight = null;
    target.iv_checkbox = null;
    target.sv = null;
    target.llTip = null;

    view2131689692.setOnClickListener(null);
    view2131689692 = null;
    view2131689691.setOnClickListener(null);
    view2131689691 = null;
    view2131689674.setOnClickListener(null);
    view2131689674 = null;
    view2131689694.setOnClickListener(null);
    view2131689694 = null;

    this.target = null;
  }
}
