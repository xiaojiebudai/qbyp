// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RechargeActivity_ViewBinding<T extends RechargeActivity> implements Unbinder {
  protected T target;

  private View view2131689862;

  @UiThread
  public RechargeActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.ivPhoto = Utils.findRequiredViewAsType(source, R.id.iv_photo, "field 'ivPhoto'", ImageView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.tvLevel = Utils.findRequiredViewAsType(source, R.id.tv_level, "field 'tvLevel'", TextView.class);
    target.rb0 = Utils.findRequiredViewAsType(source, R.id.rb_0, "field 'rb0'", RadioButton.class);
    target.rb1 = Utils.findRequiredViewAsType(source, R.id.rb_1, "field 'rb1'", RadioButton.class);
    target.rb2 = Utils.findRequiredViewAsType(source, R.id.rb_2, "field 'rb2'", RadioButton.class);
    target.rgPrice = Utils.findRequiredViewAsType(source, R.id.rg_price, "field 'rgPrice'", RadioGroup.class);
    view = Utils.findRequiredView(source, R.id.bt_pay, "field 'btPay' and method 'onClick'");
    target.btPay = Utils.castView(view, R.id.bt_pay, "field 'btPay'", Button.class);
    view2131689862 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.tvRelevel = Utils.findRequiredViewAsType(source, R.id.tv_relevel, "field 'tvRelevel'", TextView.class);
    target.llQuyu = Utils.findRequiredViewAsType(source, R.id.ll_quyu, "field 'llQuyu'", LinearLayout.class);
    target.tvJifen = Utils.findRequiredViewAsType(source, R.id.tv_jifen, "field 'tvJifen'", TextView.class);
    target.ll_banner_container = Utils.findRequiredViewAsType(source, R.id.ll_banner_container, "field 'll_banner_container'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivPhoto = null;
    target.tvName = null;
    target.tvLevel = null;
    target.rb0 = null;
    target.rb1 = null;
    target.rb2 = null;
    target.rgPrice = null;
    target.btPay = null;
    target.tvRelevel = null;
    target.llQuyu = null;
    target.tvJifen = null;
    target.ll_banner_container = null;

    view2131689862.setOnClickListener(null);
    view2131689862 = null;

    this.target = null;
  }
}
