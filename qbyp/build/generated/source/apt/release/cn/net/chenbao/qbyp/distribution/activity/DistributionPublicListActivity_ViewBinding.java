// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.distribution.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionPublicListActivity_ViewBinding<T extends DistributionPublicListActivity> implements Unbinder {
  protected T target;

  @UiThread
  public DistributionPublicListActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tv0 = Utils.findRequiredViewAsType(source, R.id.tv_0, "field 'tv0'", TextView.class);
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv_1, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv_2, "field 'tv2'", TextView.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.tv_3, "field 'tv3'", TextView.class);
    target.tv4 = Utils.findRequiredViewAsType(source, R.id.tv_4, "field 'tv4'", TextView.class);
    target.llTab = Utils.findRequiredViewAsType(source, R.id.ll_tab, "field 'llTab'", LinearLayout.class);
    target.listviewDatas = Utils.findRequiredViewAsType(source, R.id.listview_datas, "field 'listviewDatas'", PullToRefreshListView.class);
    target.tvTotalMoneyIn = Utils.findRequiredViewAsType(source, R.id.tv_total_money_in, "field 'tvTotalMoneyIn'", TextView.class);
    target.tvTotalMoneyOut = Utils.findRequiredViewAsType(source, R.id.tv_total_money_out, "field 'tvTotalMoneyOut'", TextView.class);
    target.flBottom = Utils.findRequiredViewAsType(source, R.id.fl_bottom, "field 'flBottom'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tv0 = null;
    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;
    target.tv4 = null;
    target.llTab = null;
    target.listviewDatas = null;
    target.tvTotalMoneyIn = null;
    target.tvTotalMoneyOut = null;
    target.flBottom = null;

    this.target = null;
  }
}
