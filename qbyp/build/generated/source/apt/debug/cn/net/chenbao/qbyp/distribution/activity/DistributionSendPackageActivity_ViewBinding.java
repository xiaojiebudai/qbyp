// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionSendPackageActivity_ViewBinding<T extends DistributionSendPackageActivity> implements Unbinder {
  protected T target;

  private View view2131689732;

  private View view2131689737;

  @UiThread
  public DistributionSendPackageActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvLogisticsCompany = Utils.findRequiredViewAsType(source, R.id.tv_logistics_company, "field 'tvLogisticsCompany'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_logistics_company, "field 'llLogisticsCompany' and method 'onViewClicked'");
    target.llLogisticsCompany = Utils.castView(view, R.id.ll_logistics_company, "field 'llLogisticsCompany'", LinearLayout.class);
    view2131689732 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.llLogisticsNum = Utils.findRequiredViewAsType(source, R.id.ll_logistics_num, "field 'llLogisticsNum'", EditText.class);
    target.cbNoNameComm = Utils.findRequiredViewAsType(source, R.id.cb_no_name_comm, "field 'cbNoNameComm'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.tv_send, "field 'tvSend' and method 'onViewClicked'");
    target.tvSend = Utils.castView(view, R.id.tv_send, "field 'tvSend'", TextView.class);
    view2131689737 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tv_zengsongjifen = Utils.findRequiredViewAsType(source, R.id.tv_zengsongjifen, "field 'tv_zengsongjifen'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvLogisticsCompany = null;
    target.llLogisticsCompany = null;
    target.llLogisticsNum = null;
    target.cbNoNameComm = null;
    target.tvSend = null;
    target.tv_zengsongjifen = null;

    view2131689732.setOnClickListener(null);
    view2131689732 = null;
    view2131689737.setOnClickListener(null);
    view2131689737 = null;

    this.target = null;
  }
}
