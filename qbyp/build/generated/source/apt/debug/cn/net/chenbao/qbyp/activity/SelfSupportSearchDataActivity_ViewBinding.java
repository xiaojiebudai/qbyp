// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SelfSupportSearchDataActivity_ViewBinding<T extends SelfSupportSearchDataActivity> implements Unbinder {
  protected T target;

  private View view2131689868;

  private View view2131689649;

  private View view2131689910;

  private View view2131689911;

  private View view2131689912;

  private View view2131689914;

  private View view2131689915;

  private View view2131689866;

  @UiThread
  public SelfSupportSearchDataActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.head_iv_back, "field 'headIvBack' and method 'onClick'");
    target.headIvBack = Utils.castView(view, R.id.head_iv_back, "field 'headIvBack'", LinearLayout.class);
    view2131689868 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvShopCartCount = Utils.findRequiredViewAsType(source, R.id.tv_shop_cart_count, "field 'tvShopCartCount'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_head_right, "field 'rlHeadRight' and method 'onClick'");
    target.rlHeadRight = Utils.castView(view, R.id.rl_head_right, "field 'rlHeadRight'", RelativeLayout.class);
    view2131689649 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_synthesize, "field 'llSynthesize' and method 'onClick'");
    target.llSynthesize = Utils.castView(view, R.id.ll_synthesize, "field 'llSynthesize'", LinearLayout.class);
    view2131689910 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_new, "field 'llNew' and method 'onClick'");
    target.llNew = Utils.castView(view, R.id.ll_new, "field 'llNew'", LinearLayout.class);
    view2131689911 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvPriceImg = Utils.findRequiredViewAsType(source, R.id.tv_price_img, "field 'tvPriceImg'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_price, "field 'llPrice' and method 'onClick'");
    target.llPrice = Utils.castView(view, R.id.ll_price, "field 'llPrice'", LinearLayout.class);
    view2131689912 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_sales, "field 'llSales' and method 'onClick'");
    target.llSales = Utils.castView(view, R.id.ll_sales, "field 'llSales'", LinearLayout.class);
    view2131689914 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.ll_brand, "field 'llBrand' and method 'onClick'");
    target.llBrand = Utils.castView(view, R.id.ll_brand, "field 'llBrand'", LinearLayout.class);
    view2131689915 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.listview = Utils.findRequiredViewAsType(source, R.id.listview, "field 'listview'", PullToRefreshListView.class);
    target.edtSearch = Utils.findRequiredViewAsType(source, R.id.edt_search, "field 'edtSearch'", EditText.class);
    view = Utils.findRequiredView(source, R.id.iv_search_clear, "field 'ivSearchClear' and method 'onClick'");
    target.ivSearchClear = Utils.castView(view, R.id.iv_search_clear, "field 'ivSearchClear'", ImageView.class);
    view2131689866 = view;
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

    target.headIvBack = null;
    target.tvShopCartCount = null;
    target.rlHeadRight = null;
    target.llSynthesize = null;
    target.llNew = null;
    target.tvPriceImg = null;
    target.llPrice = null;
    target.llSales = null;
    target.llBrand = null;
    target.listview = null;
    target.edtSearch = null;
    target.ivSearchClear = null;

    view2131689868.setOnClickListener(null);
    view2131689868 = null;
    view2131689649.setOnClickListener(null);
    view2131689649 = null;
    view2131689910.setOnClickListener(null);
    view2131689910 = null;
    view2131689911.setOnClickListener(null);
    view2131689911 = null;
    view2131689912.setOnClickListener(null);
    view2131689912 = null;
    view2131689914.setOnClickListener(null);
    view2131689914 = null;
    view2131689915.setOnClickListener(null);
    view2131689915 = null;
    view2131689866.setOnClickListener(null);
    view2131689866 = null;

    this.target = null;
  }
}
