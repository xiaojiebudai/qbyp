// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShopCartFragment_ViewBinding<T extends ShopCartFragment> implements Unbinder {
  protected T target;

  private View view2131690087;

  private View view2131689650;

  private View view2131689741;

  private View view2131689746;

  @UiThread
  public ShopCartFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onClick'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131690087 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvHeadCenter = Utils.findRequiredViewAsType(source, R.id.tv_head_center, "field 'tvHeadCenter'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_head_right, "field 'tvHeadRight' and method 'onClick'");
    target.tvHeadRight = Utils.castView(view, R.id.tv_head_right, "field 'tvHeadRight'", TextView.class);
    view2131689650 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.CbAllSelect = Utils.findRequiredViewAsType(source, R.id.cb_all_select, "field 'CbAllSelect'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.ll_all_select, "field 'llAllSelect' and method 'onClick'");
    target.llAllSelect = Utils.castView(view, R.id.ll_all_select, "field 'llAllSelect'", LinearLayout.class);
    view2131689741 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvTotalPrice = Utils.findRequiredViewAsType(source, R.id.tv_total_price, "field 'tvTotalPrice'", TextView.class);
    target.llTotalMoney = Utils.findRequiredViewAsType(source, R.id.ll_total_money, "field 'llTotalMoney'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.btn_go_order_detail, "field 'btnGoOrderDetail' and method 'onClick'");
    target.btnGoOrderDetail = Utils.castView(view, R.id.btn_go_order_detail, "field 'btnGoOrderDetail'", TextView.class);
    view2131689746 = view;
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

    target.back = null;
    target.tvHeadCenter = null;
    target.tvHeadRight = null;
    target.CbAllSelect = null;
    target.llAllSelect = null;
    target.tvTotalPrice = null;
    target.llTotalMoney = null;
    target.btnGoOrderDetail = null;

    view2131690087.setOnClickListener(null);
    view2131690087 = null;
    view2131689650.setOnClickListener(null);
    view2131689650 = null;
    view2131689741.setOnClickListener(null);
    view2131689741 = null;
    view2131689746.setOnClickListener(null);
    view2131689746 = null;

    this.target = null;
  }
}
