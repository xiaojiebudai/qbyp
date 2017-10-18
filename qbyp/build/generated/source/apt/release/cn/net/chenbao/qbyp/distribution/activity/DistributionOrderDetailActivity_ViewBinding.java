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
import java.lang.IllegalStateException;
import java.lang.Override;

public class DistributionOrderDetailActivity_ViewBinding<T extends DistributionOrderDetailActivity> implements Unbinder {
  protected T target;

  @UiThread
  public DistributionOrderDetailActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.tvNameTitle = Utils.findRequiredViewAsType(source, R.id.tv_name_title, "field 'tvNameTitle'", TextView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.tvOrderid = Utils.findRequiredViewAsType(source, R.id.tv_orderid, "field 'tvOrderid'", TextView.class);
    target.state = Utils.findRequiredViewAsType(source, R.id.state, "field 'state'", TextView.class);
    target.llContainer = Utils.findRequiredViewAsType(source, R.id.ll_container, "field 'llContainer'", LinearLayout.class);
    target.tvAllprice = Utils.findRequiredViewAsType(source, R.id.tv_allprice, "field 'tvAllprice'", TextView.class);
    target.tvAlljifen = Utils.findRequiredViewAsType(source, R.id.tv_alljifen, "field 'tvAlljifen'", TextView.class);
    target.tvPayWay = Utils.findRequiredViewAsType(source, R.id.tv_pay_way, "field 'tvPayWay'", TextView.class);
    target.tvSendType = Utils.findRequiredViewAsType(source, R.id.tv_send_type, "field 'tvSendType'", TextView.class);
    target.tvAddressName = Utils.findRequiredViewAsType(source, R.id.tv_address_name, "field 'tvAddressName'", TextView.class);
    target.ll_address = Utils.findRequiredViewAsType(source, R.id.ll_address, "field 'll_address'", LinearLayout.class);
    target.tvAddressPhone = Utils.findRequiredViewAsType(source, R.id.tv_address_phone, "field 'tvAddressPhone'", TextView.class);
    target.tvAddress = Utils.findRequiredViewAsType(source, R.id.tv_address, "field 'tvAddress'", TextView.class);
    target.tvCreatetime = Utils.findRequiredViewAsType(source, R.id.tv_createtime, "field 'tvCreatetime'", TextView.class);
    target.tvPaytime = Utils.findRequiredViewAsType(source, R.id.tv_paytime, "field 'tvPaytime'", TextView.class);
    target.ll_send_time = Utils.findRequiredViewAsType(source, R.id.ll_send_time, "field 'll_send_time'", LinearLayout.class);
    target.ll_get_time = Utils.findRequiredViewAsType(source, R.id.ll_get_time, "field 'll_get_time'", LinearLayout.class);
    target.tvCanceltime = Utils.findRequiredViewAsType(source, R.id.tv_canceltime, "field 'tvCanceltime'", TextView.class);
    target.ll_create_time = Utils.findRequiredViewAsType(source, R.id.ll_create_time, "field 'll_create_time'", LinearLayout.class);
    target.ll_pay_time = Utils.findRequiredViewAsType(source, R.id.ll_pay_time, "field 'll_pay_time'", LinearLayout.class);
    target.tvSendtime = Utils.findRequiredViewAsType(source, R.id.tv_sendtime, "field 'tvSendtime'", TextView.class);
    target.tvGettime = Utils.findRequiredViewAsType(source, R.id.tv_gettime, "field 'tvGettime'", TextView.class);
    target.ll_cancel_time = Utils.findRequiredViewAsType(source, R.id.ll_cancel_time, "field 'll_cancel_time'", LinearLayout.class);
    target.tvInfo = Utils.findRequiredViewAsType(source, R.id.tv_info, "field 'tvInfo'", TextView.class);
    target.tvOrderOperationRight = Utils.findRequiredViewAsType(source, R.id.tv_order_operation_right, "field 'tvOrderOperationRight'", TextView.class);
    target.tvOrderOperationLeft = Utils.findRequiredViewAsType(source, R.id.tv_order_operation_left, "field 'tvOrderOperationLeft'", TextView.class);
    target.llOperate = Utils.findRequiredViewAsType(source, R.id.ll_operate, "field 'llOperate'", LinearLayout.class);
    target.tv_postage = Utils.findRequiredViewAsType(source, R.id.tv_postage, "field 'tv_postage'", TextView.class);
    target.tv_postage_weight = Utils.findRequiredViewAsType(source, R.id.tv_postage_weight, "field 'tv_postage_weight'", TextView.class);
    target.tvTotalMoney = Utils.findRequiredViewAsType(source, R.id.tv_total_money, "field 'tvTotalMoney'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.tvNameTitle = null;
    target.tvName = null;
    target.tvOrderid = null;
    target.state = null;
    target.llContainer = null;
    target.tvAllprice = null;
    target.tvAlljifen = null;
    target.tvPayWay = null;
    target.tvSendType = null;
    target.tvAddressName = null;
    target.ll_address = null;
    target.tvAddressPhone = null;
    target.tvAddress = null;
    target.tvCreatetime = null;
    target.tvPaytime = null;
    target.ll_send_time = null;
    target.ll_get_time = null;
    target.tvCanceltime = null;
    target.ll_create_time = null;
    target.ll_pay_time = null;
    target.tvSendtime = null;
    target.tvGettime = null;
    target.ll_cancel_time = null;
    target.tvInfo = null;
    target.tvOrderOperationRight = null;
    target.tvOrderOperationLeft = null;
    target.llOperate = null;
    target.tv_postage = null;
    target.tv_postage_weight = null;
    target.tvTotalMoney = null;

    this.target = null;
  }
}
