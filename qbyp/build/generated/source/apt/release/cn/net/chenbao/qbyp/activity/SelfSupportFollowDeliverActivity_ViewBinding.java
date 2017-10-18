// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportFollowDeliverActivity_ViewBinding<T extends SelfSupportFollowDeliverActivity> implements Unbinder {
  protected T target;

  @UiThread
  public SelfSupportFollowDeliverActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.lvWuliu = Utils.findRequiredViewAsType(source, R.id.lv_wuliu, "field 'lvWuliu'", ListView.class);
    target.tvPostState = Utils.findRequiredViewAsType(source, R.id.tv_post_state, "field 'tvPostState'", TextView.class);
    target.tvPostCompany = Utils.findRequiredViewAsType(source, R.id.tv_post_company, "field 'tvPostCompany'", TextView.class);
    target.tvPostNumber = Utils.findRequiredViewAsType(source, R.id.tv_post_number, "field 'tvPostNumber'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.lvWuliu = null;
    target.tvPostState = null;
    target.tvPostCompany = null;
    target.tvPostNumber = null;

    this.target = null;
  }
}
