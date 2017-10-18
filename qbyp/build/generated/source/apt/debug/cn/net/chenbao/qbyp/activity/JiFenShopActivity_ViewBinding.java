// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JiFenShopActivity_ViewBinding<T extends JiFenShopActivity> implements Unbinder {
  protected T target;

  @UiThread
  public JiFenShopActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", PullToRefreshListView.class);
    target.tabLayout = Utils.findRequiredViewAsType(source, R.id.tabLayout, "field 'tabLayout'", TabLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.listview = null;
    target.tabLayout = null;

    this.target = null;
  }
}
