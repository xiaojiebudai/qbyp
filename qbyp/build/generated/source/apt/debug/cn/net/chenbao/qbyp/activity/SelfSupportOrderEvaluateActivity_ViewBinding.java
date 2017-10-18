// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.view.RatingBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportOrderEvaluateActivity_ViewBinding<T extends SelfSupportOrderEvaluateActivity> implements Unbinder {
  protected T target;

  private View view2131689694;

  @UiThread
  public SelfSupportOrderEvaluateActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.ivGoodImg = Utils.findRequiredViewAsType(source, R.id.iv_good_img, "field 'ivGoodImg'", ImageView.class);
    target.rb = Utils.findRequiredViewAsType(source, R.id.rb, "field 'rb'", RatingBar.class);
    target.edIntroduce = Utils.findRequiredViewAsType(source, R.id.ed_introduce, "field 'edIntroduce'", EditText.class);
    target.tvIntroduceRemain = Utils.findRequiredViewAsType(source, R.id.tv_introduce_remain, "field 'tvIntroduceRemain'", TextView.class);
    target.cbNoNameComm = Utils.findRequiredViewAsType(source, R.id.cb_no_name_comm, "field 'cbNoNameComm'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.tv_commit, "field 'tvCommit' and method 'onClick'");
    target.tvCommit = Utils.castView(view, R.id.tv_commit, "field 'tvCommit'", TextView.class);
    view2131689694 = view;
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

    target.tvTime = null;
    target.ivGoodImg = null;
    target.rb = null;
    target.edIntroduce = null;
    target.tvIntroduceRemain = null;
    target.cbNoNameComm = null;
    target.tvCommit = null;

    view2131689694.setOnClickListener(null);
    view2131689694 = null;

    this.target = null;
  }
}
