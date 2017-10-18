// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OfflineOrderActivity_ViewBinding<T extends OfflineOrderActivity> implements Unbinder {
  protected T target;

  @UiThread
  public OfflineOrderActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.mListView = Utils.findRequiredViewAsType(source, R.id.listview, "field 'mListView'", PullToRefreshListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.mListView = null;

    this.target = null;
  }
}
