package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.LocalLifeOrderActivity;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.fragment.LocalOrderFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 订单适配器
 *
 * @author xl
 */
public class OrderAdapter extends FatherAdapter<Order> {

    Fragment mFragment;

    public OrderAdapter(Context ctx) {
        super(ctx);
    }

    public OrderAdapter(Context ctx, Fragment fragment) {
        super(ctx);
        this.mFragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_item_order,
                    parent, false);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.tv_check_order.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {// 查看订单详情
                if (mFragment == null) {
                    PublicWay.startOrderDetailActivity((Activity) mContext,
                            getItem(position).OrderId, LocalLifeOrderActivity.REQUEST_CODE);
                } else {
                    PublicWay.startOrderDetailActivity(mFragment,
                            getItem(position).OrderId, LocalOrderFragment.REQUEST_CODE);
                }


            }
        });
        holder.ll_detail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mFragment == null) {
                    PublicWay.startOrderDetailActivity((Activity) mContext,
                            getItem(position).OrderId, LocalLifeOrderActivity.REQUEST_CODE);
                } else {
                    PublicWay.startOrderDetailActivity(mFragment,
                            getItem(position).OrderId, LocalOrderFragment.REQUEST_CODE);
                }

            }
        });
        holder.ll_shop.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                PublicWay.startStoreActivity((Activity) mContext,
                        getItem(position).SellerId, LocalLifeOrderActivity.REQUEST_CODE);
            }
        });
        holder.setData(getItem(position));
        return convertView;
    }

    public class ViewHolder {
        /**
         * 查看订单
         */
        TextView tv_check_order;
        TextView tv_name;
        TextView tv_state;
        TextView tv_order_detail;
        TextView tv_buy_num;
        TextView tv_money;
        ImageView iv_pic;
        ImageView iv_send_type;
        TextView tv_order_operation;
        TextView tv_time;
        TextView tv_yuji;// 预计补贴金额
        LinearLayout ll_shop;// 店铺
        LinearLayout ll_detail;// 订单详情

        public ViewHolder(View v) {
            v.setTag(this);
            ll_shop = (LinearLayout) v.findViewById(R.id.ll_shop);
            ll_detail = (LinearLayout) v.findViewById(R.id.ll_detail);
            tv_check_order = (TextView) v.findViewById(R.id.tv_check_order);
            tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_state = (TextView) v.findViewById(R.id.tv_state);
            iv_pic = (ImageView) v.findViewById(R.id.iv_pic);
            iv_send_type = (ImageView) v.findViewById(R.id.iv_send_type);
            tv_order_detail = (TextView) v.findViewById(R.id.tv_order_detail);
            tv_buy_num = (TextView) v.findViewById(R.id.tv_buy_num);
            tv_money = (TextView) v.findViewById(R.id.tv_money);
            tv_time = (TextView) v.findViewById(R.id.tv_time);
            tv_yuji = (TextView) v.findViewById(R.id.tv_yuji);
            tv_order_operation = (TextView) v
                    .findViewById(R.id.tv_order_operation);
        }

        public void setData(final Order bean) {
            switch (bean.Status) {
                case Order.STATE_COMPLETE:// 已完成
                    tv_order_operation.setVisibility(View.GONE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.finished));
                    break;
                case Order.STATE_REFUND:// 已退款
                    tv_order_operation.setVisibility(View.GONE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.back_money));
                    break;
                case Order.STATE_WAIT_PAY:// 待付款
                    tv_order_operation.setVisibility(View.VISIBLE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.wait_pay));
                    tv_order_operation.setText(mContext.getResources().getString(
                            R.string.pay_soon));
                    tv_order_operation.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (mFragment == null) {
                                PublicWay.startPayOrderActivity((Activity) mContext,
                                        bean.PayAmt, bean.OrderId,
                                        LocalLifeOrderActivity.REQUEST_CODE, PayOrdersActivity.LOCAL);
                            } else {
                                PublicWay.startPayOrderActivity(mFragment,
                                        bean.PayAmt, bean.OrderId,
                                        LocalOrderFragment.REQUEST_CODE, PayOrdersActivity.LOCAL);
                            }

                        }
                    });
                    break;
                case Order.STATE_WAIT_BUYER_CONFIRM:// 确认收货
                    tv_order_operation.setVisibility(View.VISIBLE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.wait_goods));
                    tv_order_operation.setText(mContext.getResources().getString(
                            R.string.make_suer_receiver_goods));
                    tv_order_operation.setOnClickListener(new OnClickListener() {

                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onClick(View v) {
                            final CommonDialog commonDialog = DialogUtils
                                    .getCommonDialog((Activity) mContext,
                                            R.string.make_sure_recriver_goods);
                            commonDialog.setTextColor(0, R.color.text_f7);
                            commonDialog.setTextColor(1, R.color.text_f7);
                            commonDialog.setRightButtonText(R.string.ok);
                            commonDialog.setLeftButtonText(R.string.cancel);
                            commonDialog
                                    .setRightButtonCilck(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            makeSureReceiverGoods(bean.OrderId);
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.setLeftButtonOnClick(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    commonDialog.dismiss();
                                }
                            });
                            commonDialog.show();
                        }
                    });
                    break;
                case Order.STATE_CLOSE:// 已关闭
                    tv_order_operation.setVisibility(View.GONE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.closed));
                    break;
                case Order.STATE_CANCEL:// 已取消
                    tv_order_operation.setVisibility(View.GONE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.canceled));
                    break;
                case Order.STATE_WAIT_SELLER_CONFIRM:// 帶商家確認
                    tv_order_operation.setVisibility(View.GONE);
                    tv_state.setText(mContext.getResources().getString(
                            R.string.wait_shop_sure2));
                    break;
            }
            tv_name.setText(bean.SellerName);
            ImageUtils.setCommonImage(mContext, ImageUtils.getRightImgScreen(bean.GoodsImg, DensityUtil.dip2px(mContext, 66), DensityUtil.dip2px(mContext, 66)), iv_pic);
            if (bean.GoodsName == null) {

                tv_order_detail.setText(mContext.getResources().getString(
                        R.string.dianle) + " ");
            } else {
                tv_order_detail.setText(mContext.getResources().getString(
                        R.string.dianle)
                        + " " + (bean.GoodsName.length() > 10 ? bean.GoodsName.substring(0, 10) + "..." : bean.GoodsName));
            }
            tv_buy_num.setText(bean.Quantity + mContext.getResources().getString(R.string.fen));
            tv_money.setText("¥" + WWViewUtil.numberFormatWithTwo(bean.TotalAmt) + "");
            tv_time.setText(mContext.getResources().getString(
                    R.string.order_create_time)
                    + " " + TimeUtil.getTimeToM2(bean.CreateTime * 1000));
            if (bean.SendMode == Order.GO_SHOP) {
                iv_send_type.setImageResource(R.drawable.totheshop_icon);
            } else {
                iv_send_type.setImageResource(R.drawable.send_icon);
            }
        }
    }

    /**
     * 确认收货
     */
    private void makeSureReceiverGoods(final long orderId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", orderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiTrade.OrderUserConfirm()),
                new WWXCallBack("OrderUserConfirm") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        PublicWay.startShopCommentActivity((Activity) mContext,// 去评价
                                orderId);
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });
    }

}
