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

public class SelfSupportGoodsEvaluateListActivity_ViewBinding<T extends SelfSupportGoodsEvaluateListActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SelfSupportGoodsEvaluateListActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.listviewDatas = Utils.findRequiredViewAsType(source, R.id.listview_datas, "field 'listviewDatas'", PullToRefreshListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.listviewDatas = null;

    this.target = null;
  }
}
