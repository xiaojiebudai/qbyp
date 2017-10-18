// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportFragment_ViewBinding<T extends SelfSupportFragment> implements Unbinder {
  protected T target;

  private View view2131689774;

  private View view2131689649;

  @UiThread
  public SelfSupportFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.rlHeadLeft = Utils.findRequiredViewAsType(source, R.id.rl_head_left, "field 'rlHeadLeft'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.ll_search, "field 'llSearch' and method 'onClick'");
    target.llSearch = Utils.castView(view, R.id.ll_search, "field 'llSearch'", LinearLayout.class);
    view2131689774 = view;
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
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", PullToRefreshListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rlHeadLeft = null;
    target.llSearch = null;
    target.tvShopcartCount = null;
    target.rlHeadRight = null;
    target.listview = null;

    view2131689774.setOnClickListener(null);
    view2131689774 = null;
    view2131689649.setOnClickListener(null);
    view2131689649 = null;

    this.target = null;
  }
}
