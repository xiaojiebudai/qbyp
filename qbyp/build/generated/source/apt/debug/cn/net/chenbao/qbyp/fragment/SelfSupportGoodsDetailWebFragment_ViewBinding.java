// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.fragment;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.webkit.WebView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportGoodsDetailWebFragment_ViewBinding<T extends SelfSupportGoodsDetailWebFragment> implements Unbinder {
  protected T target;

  @UiThread
  public SelfSupportGoodsDetailWebFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.webview = Utils.findRequiredViewAsType(source, R.id.webview, "field 'webview'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.webview = null;

    this.target = null;
  }
}
