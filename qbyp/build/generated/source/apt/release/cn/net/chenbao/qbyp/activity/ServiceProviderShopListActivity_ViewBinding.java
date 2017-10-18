// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceProviderShopListActivity_ViewBinding<T extends ServiceProviderShopListActivity> implements Unbinder {
  protected T target;

  private View view2131689937;

  @UiThread
  public ServiceProviderShopListActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvIndustry = Utils.findRequiredViewAsType(source, R.id.tv_industry, "field 'tvIndustry'", TextView.class);
    target.ivIndustry = Utils.findRequiredViewAsType(source, R.id.iv_industry, "field 'ivIndustry'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.ll_select, "field 'llSelect' and method 'onClick'");
    target.llSelect = Utils.castView(view, R.id.ll_select, "field 'llSelect'", LinearLayout.class);
    view2131689937 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.listviewDatas = Utils.findRequiredViewAsType(source, R.id.listview_datas, "field 'listviewDatas'", PullToRefreshListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvIndustry = null;
    target.ivIndustry = null;
    target.llSelect = null;
    target.listviewDatas = null;

    view2131689937.setOnClickListener(null);
    view2131689937 = null;

    this.target = null;
  }
}
