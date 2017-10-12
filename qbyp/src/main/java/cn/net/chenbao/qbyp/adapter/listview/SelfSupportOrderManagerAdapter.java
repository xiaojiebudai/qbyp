package cn.net.chenbao.qbyp.adapter.listview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportFollowDeliverActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportOrderActivity2;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopOrderOutline;
import cn.net.chenbao.qbyp.fragment.SelfSupportOrderItemFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

/**
 * Created by wuri on 2016/11/3.
 * Description:订单(只适用于Activity)中自营商城的adapter
 */

public class SelfSupportOrderManagerAdapter extends FatherAdapter<ShopOrderOutline> {
    Context ctx;

    public SelfSupportOrderManagerAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.lv_self_support_order_manager_item,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        TextView tvSellerName;
        TextView tvState;
        ImageView ivGoodImg;
        TextView tvGoodName;
        TextView tvGoodPrice;
        TextView tv0;
        TextView tvGoodNum;
        TextView tvPlaceAnOrderTime;
        TextView tvShouldPay;
        TextView tv3;
        TextView tvTotalGoodNum;
        TextView tvPayReceiveEvaluate;
        TextView tvCancleLookWuliu;
        LinearLayout llContainer;

        public ViewHolder(View view) {
            view.setTag(this);
            tvSellerName = (TextView) view.findViewById(R.id.tv_seller_name);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            ivGoodImg = (ImageView) view.findViewById(R.id.iv_good_img);
            tvGoodName = (TextView) view.findViewById(R.id.tv_good_name);
            tvGoodPrice = (TextView) view.findViewById(R.id.tv_good_price);
            tv0 = (TextView) view.findViewById(R.id.tv_0);
            tvGoodNum = (TextView) view.findViewById(R.id.tv_good_num);
            tvPlaceAnOrderTime = (TextView) view.findViewById(R.id.tv_place_an_order_time);
            tvShouldPay = (TextView) view.findViewById(R.id.tv_should_pay);
            tv3 = (TextView) view.findViewById(R.id.tv_3);
            tvTotalGoodNum = (TextView) view.findViewById(R.id.tv_total_good_num);
            tvPayReceiveEvaluate = (TextView) view.findViewById(R.id.tv_pay_receive_evaluate);
            tvCancleLookWuliu = (TextView) view.findViewById(R.id.tv_cancle_look_wuliu);
            llContainer = (LinearLayout) view.findViewById(R.id.ll_container);
        }

        public void setData(int pos) {
            final ShopOrderOutline shopOrderInfo = getItem(pos);
            switch (shopOrderInfo.Status) {

                case ShopOrderOutline.STATE_WAIT_PAY:// 待付款
                    tvCancleLookWuliu.setVisibility(View.VISIBLE);
                    tvPayReceiveEvaluate.setVisibility(View.VISIBLE);
                    tvState.setText(mContext.getResources().getString(
                            R.string.wait_pay));
                    tv3.setText(mContext.getResources().getString(
                            R.string.need_pay));
                    tvPayReceiveEvaluate.setText(mContext.getResources().getString(
                            R.string.pay_soon));
                    tvCancleLookWuliu.setText(mContext.getResources().getString(
                            R.string.cancel_order));

                    tvPayReceiveEvaluate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long[] orderIds = new long[1];
                            orderIds[0] = shopOrderInfo.OrderId;
                            PublicWay.startPayOrderActivity((Activity) mContext, shopOrderInfo.PayAmt, orderIds,
                                    SelfSupportOrderActivity2.REQUEST_CODE, PayOrdersActivity.SELF);
                        }
                    });
                    tvCancleLookWuliu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CommonDialog commonDialog = DialogUtils
                                    .getCommonDialog((Activity) mContext,
                                            R.string.check_cancel_order);
                            commonDialog.setTextColor(0, R.color.text_f7);
                            commonDialog.setTextColor(1, R.color.text_f7);
                            commonDialog.setRightButtonText(R.string.ok);
                            commonDialog.setLeftButtonText(R.string.cancel);
                            commonDialog
                                    .setRightButtonCilck(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelOrder(shopOrderInfo.OrderId);
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.setLeftButtonOnClick(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    commonDialog.dismiss();
                                }
                            });
                            commonDialog.show();
                        }
                    });
                    break;
                case ShopOrderOutline.STATE_WAIT_SELLER_CONFIRM:// 待发货
                    tvCancleLookWuliu.setVisibility(View.VISIBLE);
                    tvPayReceiveEvaluate.setVisibility(View.GONE);
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvState.setText(mContext.getResources().getString(
                            R.string.wait_send));
                    tvCancleLookWuliu.setText(mContext.getResources().getString(
                            R.string.cancel_order));
                    tvCancleLookWuliu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final CommonDialog commonDialog = DialogUtils
                                    .getCommonDialog((Activity) mContext,
                                            R.string.check_cancel_order);
                            commonDialog.setTextColor(0, R.color.text_f7);
                            commonDialog.setTextColor(1, R.color.text_f7);
                            commonDialog.setRightButtonText(R.string.ok);
                            commonDialog.setLeftButtonText(R.string.cancel);
                            commonDialog
                                    .setRightButtonCilck(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            cancelOrder(shopOrderInfo.OrderId);
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.setLeftButtonOnClick(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    commonDialog.dismiss();
                                }
                            });
                            commonDialog.show();
                        }
                    });
                    break;
                case ShopOrderOutline.STATE_WAIT_BUYER_CONFIRM:// 确认收货
                    tvCancleLookWuliu.setVisibility(View.VISIBLE);
                    tvPayReceiveEvaluate.setVisibility(View.VISIBLE);
                    tvState.setText(mContext.getResources().getString(
                            R.string.wait_goods));
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvPayReceiveEvaluate.setText(mContext.getResources().getString(
                            R.string.make_suer_receiver_goods));
                    tvCancleLookWuliu.setText(mContext.getResources().getString(
                            R.string.look_post));

                    tvPayReceiveEvaluate.setOnClickListener(new View.OnClickListener() {

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
                                    .setRightButtonCilck(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            makeSureReceiverGoods(shopOrderInfo);
                                            commonDialog.dismiss();
                                        }
                                    });
                            commonDialog.setLeftButtonOnClick(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    commonDialog.dismiss();
                                }
                            });
                            commonDialog.show();
                        }
                    });
                    tvCancleLookWuliu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.stratSelfSupportFollowDeliverActivity((Activity) mContext, shopOrderInfo.OrderId, SelfSupportFollowDeliverActivity.SELF, SelfSupportOrderItemFragment.REQUEST_CODE);
                        }
                    });
                    break;

                case ShopOrderOutline.STATE_COMPLETE:// 已完成
                    if (shopOrderInfo.UserJudge) {
                        tvPayReceiveEvaluate.setVisibility(View.GONE);
                    } else {
                        tvPayReceiveEvaluate.setVisibility(View.VISIBLE);
                    }
                    tvCancleLookWuliu.setVisibility(View.VISIBLE);
                    tvState.setText(mContext.getResources().getString(
                            R.string.finished));
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvPayReceiveEvaluate.setText(mContext.getResources().getString(
                            R.string.go_evaluate));
                    tvCancleLookWuliu.setText(mContext.getResources().getString(
                            R.string.look_post));
                    tvPayReceiveEvaluate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.stratSelfSupportOrderEvaluateActivity((Activity) mContext,// 去评价
                                    shopOrderInfo.OrderId, shopOrderInfo.ImageUrl, shopOrderInfo.ConfirmTime, SelfSupportOrderItemFragment.REQUEST_CODE);
                        }
                    });
                    tvCancleLookWuliu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PublicWay.stratSelfSupportFollowDeliverActivity((Activity) mContext, shopOrderInfo.OrderId, SelfSupportFollowDeliverActivity.SELF, SelfSupportOrderItemFragment.REQUEST_CODE);
                        }
                    });

                    break;


                case ShopOrderOutline.STATE_CANCEL:// 已取消
                    tvCancleLookWuliu.setVisibility(View.GONE);
                    tvPayReceiveEvaluate.setVisibility(View.GONE);
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvState.setText(mContext.getResources().getString(
                            R.string.canceled));
                    break;
                case ShopOrderOutline.STATE_REFUND:// 退款中
                    tvCancleLookWuliu.setVisibility(View.GONE);
                    tvPayReceiveEvaluate.setVisibility(View.GONE);
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvState.setText(mContext.getResources().getString(
                            R.string.back_money));
                    break;
                case ShopOrderOutline.STATE_CLOSE:// 已关闭
                    tvCancleLookWuliu.setVisibility(View.GONE);
                    tvPayReceiveEvaluate.setVisibility(View.GONE);
                    tv3.setText(mContext.getResources().getString(
                            R.string.real_pay_colon));
                    tvState.setText(mContext.getResources().getString(
                            R.string.closed));
                    break;

            }
            tvSellerName.setText(shopOrderInfo.VenderName);

            WWViewUtil.textInsertDrawable(mContext, tvGoodName, shopOrderInfo.ProductName, false, shopOrderInfo.IsVipLevel);


            //需要修改
            tvShouldPay.setText(WWViewUtil.numberFormatPrice(shopOrderInfo.PayAmt));
            tvGoodPrice.setText(WWViewUtil.numberFormatPrice(shopOrderInfo.SalePrice));
            if (shopOrderInfo.Sku != null) {
                tv0.setText(shopOrderInfo.Sku.ProName);
            }
            tvTotalGoodNum.setText(String.format(mContext.getString(R.string.total_number_goods), shopOrderInfo.Quantity));
            tvGoodNum.setText("x" + shopOrderInfo.GoodsQuantity);
            tvPlaceAnOrderTime.setText(TimeUtil.getTimeToM2(shopOrderInfo.CreateTime * 1000));
            ImageUtils.setCommonImage(mContext, ImageUtils.getRightImgScreen(shopOrderInfo.ImageUrl, DensityUtil.dip2px(mContext, 66), DensityUtil.dip2px(mContext, 66)), ivGoodImg);
            llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicWay.stratSelfSupportOrderDetailActivity((Activity) mContext, shopOrderInfo.OrderId, 0);
                }
            });


        }
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    private void cancelOrder(long orderId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", orderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderCancel()),
                new WWXCallBack("OrderCancel") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (data.getBoolean("Data")) {
                            WWToast.showShort(R.string.cancel_order_success);
                            if (okListener != null) {
                                okListener.operateListener();
                            }
                        }
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });
    }

    /**
     * 确认收货
     */
    private void makeSureReceiverGoods(final ShopOrderOutline shopOrderInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", shopOrderInfo.OrderId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderConfirm()),
                new WWXCallBack("OrderConfirm") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (okListener != null) {
                            okListener.operateListener();
                        }
                        final CommonDialog commonDialog = DialogUtils.getCommonDialog((Activity) mContext, mContext.getString(R.string.order_evaluate_tips));
                        commonDialog.getButtonLeft(R.string.temp_no_evaluate).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.getButtonRight(R.string.go_evaluate).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                PublicWay.stratSelfSupportOrderEvaluateActivity((Activity) mContext,// 去评价
                                        shopOrderInfo.OrderId, shopOrderInfo.ImageUrl, System.currentTimeMillis() / 1000, SelfSupportOrderItemFragment.REQUEST_CODE);
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.show();

                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });
    }

    private OperateCallBack okListener;

    public void setOperateListener(OperateCallBack listener) {
        this.okListener = listener;
    }

    public interface OperateCallBack {
        void operateListener();
    }
}
