// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class QbbsendActivity_ViewBinding<T extends QbbsendActivity> implements Unbinder {
  protected T target;

  private View view2131689850;

  private View view2131689764;

  @UiThread
  public QbbsendActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvQibaobinum = Utils.findRequiredViewAsType(source, R.id.tv_qibaobinum, "field 'tvQibaobinum'", TextView.class);
    target.edUsername = Utils.findRequiredViewAsType(source, R.id.ed_username, "field 'edUsername'", EditText.class);
    view = Utils.findRequiredView(source, R.id.iv_select, "field 'ivSelect' and method 'onClick'");
    target.ivSelect = Utils.castView(view, R.id.iv_select, "field 'ivSelect'", ImageView.class);
    view2131689850 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.edNum = Utils.findRequiredViewAsType(source, R.id.ed_num, "field 'edNum'", EditText.class);
    view = Utils.findRequiredView(source, R.id.tv_ok, "field 'tvOk' and method 'onClick'");
    target.tvOk = Utils.castView(view, R.id.tv_ok, "field 'tvOk'", TextView.class);
    view2131689764 = view;
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

    target.tvQibaobinum = null;
    target.edUsername = null;
    target.ivSelect = null;
    target.edNum = null;
    target.tvOk = null;

    view2131689850.setOnClickListener(null);
    view2131689850 = null;
    view2131689764.setOnClickListener(null);
    view2131689764 = null;

    this.target = null;
  }
}
