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

public class DistributionWalletActivity_ViewBinding<T extends DistributionWalletActivity> implements Unbinder {
  protected T target;

  private View view2131689693;

  private View view2131689748;

  private View view2131689750;

  private View view2131689751;

  private View view2131689752;

  private View view2131689753;

  private View view2131689755;

  private View view2131689758;

  @UiThread
  public DistributionWalletActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.fakeStatusBar = Utils.findRequiredView(source, R.id.fake_status_bar, "field 'fakeStatusBar'");
    target.fake_status_bar1 = Utils.findRequiredView(source, R.id.fake_status_bar1, "field 'fake_status_bar1'");
    view = Utils.findRequiredView(source, R.id.tv_money_all, "field 'tvMoneyAll' and method 'onViewClicked'");
    target.tvMoneyAll = Utils.castView(view, R.id.tv_money_all, "field 'tvMoneyAll'", TextView.class);
    view2131689693 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvUb = Utils.findRequiredViewAsType(source, R.id.tv_ub, "field 'tvUb'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_ub, "field 'llUb' and method 'onViewClicked'");
    target.llUb = Utils.castView(view, R.id.ll_ub, "field 'llUb'", LinearLayout.class);
    view2131689748 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_withdraw, "field 'llWithdraw' and method 'onViewClicked'");
    target.llWithdraw = Utils.castView(view, R.id.ll_withdraw, "field 'llWithdraw'", LinearLayout.class);
    view2131689750 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_withdraw_des, "field 'llWithdrawDes' and method 'onViewClicked'");
    target.llWithdrawDes = Utils.castView(view, R.id.ll_withdraw_des, "field 'llWithdrawDes'", LinearLayout.class);
    view2131689751 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_jifen_des, "field 'llJifenDes' and method 'onViewClicked'");
    target.llJifenDes = Utils.castView(view, R.id.ll_jifen_des, "field 'llJifenDes'", LinearLayout.class);
    view2131689752 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvBaseRate = Utils.findRequiredViewAsType(source, R.id.tv_base_rate, "field 'tvBaseRate'", TextView.class);
    target.tvMoneyWait = Utils.findRequiredViewAsType(source, R.id.tv_money_wait, "field 'tvMoneyWait'", TextView.class);
    target.tvMoney = Utils.findRequiredViewAsType(source, R.id.tv_money, "field 'tvMoney'", TextView.class);
    target.tvYijijifen = Utils.findRequiredViewAsType(source, R.id.tv_yijijifen, "field 'tvYijijifen'", TextView.class);
    target.tvErjijifen = Utils.findRequiredViewAsType(source, R.id.tv_erjijifen, "field 'tvErjijifen'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_freeze, "field 'll_freeze' and method 'onViewClicked'");
    target.ll_freeze = Utils.castView(view, R.id.ll_freeze, "field 'll_freeze'", LinearLayout.class);
    view2131689753 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_1ji, "field 'll_1ji' and method 'onViewClicked'");
    target.ll_1ji = Utils.castView(view, R.id.ll_1ji, "field 'll_1ji'", LinearLayout.class);
    view2131689755 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_2ji, "field 'll_2ji' and method 'onViewClicked'");
    target.ll_2ji = Utils.castView(view, R.id.ll_2ji, "field 'll_2ji'", LinearLayout.class);
    view2131689758 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.fakeStatusBar = null;
    target.fake_status_bar1 = null;
    target.tvMoneyAll = null;
    target.tvUb = null;
    target.llUb = null;
    target.llWithdraw = null;
    target.llWithdrawDes = null;
    target.llJifenDes = null;
    target.tvBaseRate = null;
    target.tvMoneyWait = null;
    target.tvMoney = null;
    target.tvYijijifen = null;
    target.tvErjijifen = null;
    target.ll_freeze = null;
    target.ll_1ji = null;
    target.ll_2ji = null;

    view2131689693.setOnClickListener(null);
    view2131689693 = null;
    view2131689748.setOnClickListener(null);
    view2131689748 = null;
    view2131689750.setOnClickListener(null);
    view2131689750 = null;
    view2131689751.setOnClickListener(null);
    view2131689751 = null;
    view2131689752.setOnClickListener(null);
    view2131689752 = null;
    view2131689753.setOnClickListener(null);
    view2131689753 = null;
    view2131689755.setOnClickListener(null);
    view2131689755 = null;
    view2131689758.setOnClickListener(null);
    view2131689758 = null;

    this.target = null;
  }
}
