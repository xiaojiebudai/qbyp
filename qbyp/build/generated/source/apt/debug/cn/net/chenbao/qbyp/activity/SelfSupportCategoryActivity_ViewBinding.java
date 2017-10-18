// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportCategoryActivity_ViewBinding<T extends SelfSupportCategoryActivity> implements Unbinder {
  protected T target;

  private View view2131689868;

  private View view2131689649;

  @UiThread
  public SelfSupportCategoryActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.lvCategory = Utils.findRequiredViewAsType(source, R.id.lv_category, "field 'lvCategory'", ListView.class);
    view = Utils.findRequiredView(source, R.id.head_iv_back, "field 'headIvBack' and method 'onClick'");
    target.headIvBack = Utils.castView(view, R.id.head_iv_back, "field 'headIvBack'", RelativeLayout.class);
    view2131689868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvShopcartCount = Utils.findRequiredViewAsType(source, R.id.tv_shopcart_count, "field 'tvShopcartCount'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_head_right, "field 'rlHeadRight' and method 'onClick'");
    target.rlHeadRight = Utils.castView(view, R.id.rl_head_right, "field 'rlHeadRight'", RelativeLayout.class);
    view2131689649 = view;
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

    target.lvCategory = null;
    target.headIvBack = null;
    target.tvShopcartCount = null;
    target.rlHeadRight = null;

    view2131689868.setOnClickListener(null);
    view2131689868 = null;
    view2131689649.setOnClickListener(null);
    view2131689649 = null;

    this.target = null;
  }
}
