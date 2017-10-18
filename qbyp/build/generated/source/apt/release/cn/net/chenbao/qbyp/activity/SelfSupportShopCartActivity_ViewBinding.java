// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportShopCartActivity_ViewBinding<T extends SelfSupportShopCartActivity> implements Unbinder {
  protected T target;

  private View view2131689746;

  private View view2131689741;

  @UiThread
  public SelfSupportShopCartActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.evShopcart = Utils.findRequiredViewAsType(source, R.id.ev_shopcart, "field 'evShopcart'", PullToRefreshExpandableListView.class);
    target.CbAllSelect = Utils.findRequiredViewAsType(source, R.id.cb_all_select, "field 'CbAllSelect'", CheckBox.class);
    target.tvTotalPrice = Utils.findRequiredViewAsType(source, R.id.tv_total_price, "field 'tvTotalPrice'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_go_order_detail, "field 'btnGoOrderDetail' and method 'onClick'");
    target.btnGoOrderDetail = Utils.castView(view, R.id.btn_go_order_detail, "field 'btnGoOrderDetail'", TextView.class);
    view2131689746 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_all_select, "field 'llAllSelect' and method 'onClick'");
    target.llAllSelect = Utils.castView(view, R.id.ll_all_select, "field 'llAllSelect'", LinearLayout.class);
    view2131689741 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.llTotalMoney = Utils.findRequiredViewAsType(source, R.id.ll_total_money, "field 'llTotalMoney'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.evShopcart = null;
    target.CbAllSelect = null;
    target.tvTotalPrice = null;
    target.btnGoOrderDetail = null;
    target.llAllSelect = null;
    target.llTotalMoney = null;

    view2131689746.setOnClickListener(null);
    view2131689746 = null;
    view2131689741.setOnClickListener(null);
    view2131689741 = null;

    this.target = null;
  }
}
