// Generated code from Butter Knife. Do not modify!
package cn.net.chenbao.qbyp.activity;

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
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelfSupportOrderDetailActivity_ViewBinding<T extends SelfSupportOrderDetailActivity> implements Unbinder {
  protected T target;

  private View view2131689898;

  private View view2131689904;

  private View view2131689905;

  @UiThread
  public SelfSupportOrderDetailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.tvHeadCenter = Utils.findRequiredViewAsType(source, R.id.tv_head_center, "field 'tvHeadCenter'", TextView.class);
    target.tvHeadRight = Utils.findRequiredViewAsType(source, R.id.tv_head_right, "field 'tvHeadRight'", TextView.class);
    target.tvOrderSerialnum = Utils.findRequiredViewAsType(source, R.id.tv_order_serialnum, "field 'tvOrderSerialnum'", TextView.class);
    target.tvOrderState = Utils.findRequiredViewAsType(source, R.id.tv_order_state, "field 'tvOrderState'", TextView.class);
    target.tvReceiverName = Utils.findRequiredViewAsType(source, R.id.tv_receiver_name, "field 'tvReceiverName'", TextView.class);
    target.tvRevceiverPhone = Utils.findRequiredViewAsType(source, R.id.tv_revceiver_phone, "field 'tvRevceiverPhone'", TextView.class);
    target.tvReveiverAddress = Utils.findRequiredViewAsType(source, R.id.tv_reveiver_address, "field 'tvReveiverAddress'", TextView.class);
    target.tvShopName = Utils.findRequiredViewAsType(source, R.id.tv_shop_name, "field 'tvShopName'", TextView.class);
    target.tv0 = Utils.findRequiredViewAsType(source, R.id.tv_0, "field 'tv0'", TextView.class);
    target.llContainer = Utils.findRequiredViewAsType(source, R.id.ll_container, "field 'llContainer'", LinearLayout.class);
    target.ll_jifen_container = Utils.findRequiredViewAsType(source, R.id.ll_jifen_container, "field 'll_jifen_container'", LinearLayout.class);
    target.tvMessage = Utils.findRequiredViewAsType(source, R.id.tv_message, "field 'tvMessage'", TextView.class);
    target.tvGoodsTotalMoney = Utils.findRequiredViewAsType(source, R.id.tv_goods_total_money, "field 'tvGoodsTotalMoney'", TextView.class);
    target.tvFreight = Utils.findRequiredViewAsType(source, R.id.tv_freight, "field 'tvFreight'", TextView.class);
    target.line_pay = Utils.findRequiredViewAsType(source, R.id.line_pay, "field 'line_pay'", TextView.class);
    target.tvRealPay = Utils.findRequiredViewAsType(source, R.id.tv_real_pay, "field 'tvRealPay'", TextView.class);
    target.tvPayWay = Utils.findRequiredViewAsType(source, R.id.tv_pay_way, "field 'tvPayWay'", TextView.class);
    view = Utils.findRequiredView(source, R.id.ll_contact_seller, "field 'llContactSeller' and method 'onClick'");
    target.llContactSeller = Utils.castView(view, R.id.ll_contact_seller, "field 'llContactSeller'", LinearLayout.class);
    view2131689898 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvPlaceAnOrderTime = Utils.findRequiredViewAsType(source, R.id.tv_place_an_order_time, "field 'tvPlaceAnOrderTime'", TextView.class);
    target.tvGoodPayTime = Utils.findRequiredViewAsType(source, R.id.tv_good_pay_time, "field 'tvGoodPayTime'", TextView.class);
    target.tvSendGoodTime = Utils.findRequiredViewAsType(source, R.id.tv_send_good_time, "field 'tvSendGoodTime'", TextView.class);
    view = Utils.findRequiredView(source, R.id.tv_look_good_position, "field 'tvLookGoodPosition' and method 'onClick'");
    target.tvLookGoodPosition = Utils.castView(view, R.id.tv_look_good_position, "field 'tvLookGoodPosition'", TextView.class);
    view2131689904 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_makesure_receive, "field 'tvMakesureReceive' and method 'onClick'");
    target.tvMakesureReceive = Utils.castView(view, R.id.tv_makesure_receive, "field 'tvMakesureReceive'", TextView.class);
    view2131689905 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvNeedPay = Utils.findRequiredViewAsType(source, R.id.tv_need_pay, "field 'tvNeedPay'", TextView.class);
    target.rl_3 = Utils.findRequiredViewAsType(source, R.id.rl_3, "field 'rl_3'", RelativeLayout.class);
    target.tvCreditNo = Utils.findRequiredViewAsType(source, R.id.tv_creditId, "field 'tvCreditNo'", TextView.class);
    target.llId = Utils.findRequiredViewAsType(source, R.id.ll_id, "field 'llId'", LinearLayout.class);
    target.tvHeadLeft = Utils.findRequiredViewAsType(source, R.id.tv_head_left, "field 'tvHeadLeft'", TextView.class);
    target.rlHeadLeft = Utils.findRequiredViewAsType(source, R.id.rl_head_left, "field 'rlHeadLeft'", RelativeLayout.class);
    target.rlHeadRight = Utils.findRequiredViewAsType(source, R.id.rl_head_right, "field 'rlHeadRight'", RelativeLayout.class);
    target.tvReceiveGoodTime = Utils.findRequiredViewAsType(source, R.id.tv_receive_good_time, "field 'tvReceiveGoodTime'", TextView.class);
    target.llReceiveGoodTime = Utils.findRequiredViewAsType(source, R.id.ll_receive_good_time, "field 'llReceiveGoodTime'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvHeadCenter = null;
    target.tvHeadRight = null;
    target.tvOrderSerialnum = null;
    target.tvOrderState = null;
    target.tvReceiverName = null;
    target.tvRevceiverPhone = null;
    target.tvReveiverAddress = null;
    target.tvShopName = null;
    target.tv0 = null;
    target.llContainer = null;
    target.ll_jifen_container = null;
    target.tvMessage = null;
    target.tvGoodsTotalMoney = null;
    target.tvFreight = null;
    target.line_pay = null;
    target.tvRealPay = null;
    target.tvPayWay = null;
    target.llContactSeller = null;
    target.tvPlaceAnOrderTime = null;
    target.tvGoodPayTime = null;
    target.tvSendGoodTime = null;
    target.tvLookGoodPosition = null;
    target.tvMakesureReceive = null;
    target.tvNeedPay = null;
    target.rl_3 = null;
    target.tvCreditNo = null;
    target.llId = null;
    target.tvHeadLeft = null;
    target.rlHeadLeft = null;
    target.rlHeadRight = null;
    target.tvReceiveGoodTime = null;
    target.llReceiveGoodTime = null;

    view2131689898.setOnClickListener(null);
    view2131689898 = null;
    view2131689904.setOnClickListener(null);
    view2131689904 = null;
    view2131689905.setOnClickListener(null);
    view2131689905 = null;

    this.target = null;
  }
}
