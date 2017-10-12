package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 商品适配器
 *
 * @author xl
 * @date 2016-7-29 上午2:35:55
 * @description
 */
public class GoodsAdaper extends FatherAdapter<Goods> {
    /**
     * 订单详情
     */
    public static final int MODE_ORDER = 2;
    /**
     * 商品列表
     */
    public static final int MODE_GOODS = 1;
    public int mode;

    public GoodsAdaper(Context ctx, int mode) {
        super(ctx);
        this.mode = mode;
    }

    private PriceCallBack priceListener;

    public PriceCallBack getPriceListener() {
        return priceListener;
    }

    public void setPriceListener(PriceCallBack priceListener) {
        this.priceListener = priceListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_goods_item,
                    parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Goods goods = getItem(position);
        viewHolder.setData(goods);
        viewHolder.iv_jia.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mode == MODE_ORDER) {// 订单模式
                    addOrSubCart(true, goods, viewHolder);
                } else {
                    if (goods.CartNum >= goods.StockQty) {
                        WWToast.showShort(R.string.buynum_dayu_kucun);
                        return;
                    }
                    addOrSubCart(true, goods, viewHolder);
                }
            }
        });

        viewHolder.iv_jian.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mode == MODE_ORDER) {// 订单模式
                    addOrSubCart(false, goods, viewHolder);
                } else {
                    if (goods.CartNum == 0) {
                        WWToast.showShort(R.string.buynum_dayu_ling);
                        return;
                    }
                    addOrSubCart(false, goods, viewHolder);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        public ImageView iv_image;
        public TextView tv_name;
        public TextView tv_sales;
        public TextView tv_money;
        public TextView tv_count;
        public LinearLayout ll_operate;
        public View iv_jia;
        public View iv_jian;

        public ViewHolder(View convertView) {
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_sales = (TextView) convertView.findViewById(R.id.tv_sales);
            tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ll_operate = (LinearLayout) convertView.findViewById(R.id.ll_operate);
            iv_jia = convertView.findViewById(R.id.iv_jia);
            iv_jian = convertView.findViewById(R.id.iv_jian);
            convertView.setTag(this);
        }

        public void setData(Goods goods) {
            ImageUtils.setOwnRadiusImage(mContext, ImageUtils.getRightImgScreen(goods.GoodsImg, DensityUtil.dip2px(mContext, 54), DensityUtil.dip2px(mContext, 54)), iv_image, 3);
            tv_name.setText(goods.GoodsName);
            if (mode == MODE_ORDER) {
                tv_sales.setVisibility(View.GONE);
                iv_jia.setVisibility(View.GONE);
                iv_jian.setVisibility(View.GONE);
                ll_operate.setVisibility(View.VISIBLE);
                tv_count.setText("x " + goods.Quantity + "");
            } else {
                if (goods.CartNum > 0) {
                    iv_jian.setVisibility(View.VISIBLE);
                    tv_count.setVisibility(View.VISIBLE);
                } else {
                    iv_jian.setVisibility(View.GONE);
                    tv_count.setVisibility(View.GONE);
                }
                ll_operate.setVisibility(View.VISIBLE);
                tv_count.setText(goods.CartNum + "");
            }
            tv_sales.setText(mContext.getResources().getString(
                    R.string.seller_num_with_colon)
                    + goods.SaleQty);
            tv_money.setText("¥" + WWViewUtil.numberFormatWithTwo(goods.Price) + "");
        }
    }

    public void addOrSubCart(final boolean isAdd, final Goods goods,
                             final ViewHolder holder) {
        String url = "";
        String result = null;
        if (isAdd) {
            url = ApiTrade.addCart();
            result = "CartAdd";
        } else {
            url = ApiTrade.subCart();
            result = "CartSub";
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("goodsId", goods.GoodsId);
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url),
                new WWXCallBack(result) {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (isAdd) {
                            holder.tv_count.setText(((mode == MODE_ORDER) ? ++goods.Quantity : ++goods.CartNum) + "");
                            WWToast.showShort(R.string.add_cart_success);
                            if (priceListener != null) {
                                priceListener.addGoodsListener(goods.Price);
                            }
                        } else {
                            if (((mode == MODE_ORDER) ? goods.Quantity : goods.CartNum) != 0) {
                                holder.tv_count.setText(((mode == MODE_ORDER) ? --goods.Quantity : --goods.CartNum) + "");
                                WWToast.showShort(R.string.remove_cart_success);
                                if (priceListener != null) {
                                    priceListener.subGoodsListener(goods.Price);
                                }
                            }
                        }
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });

    }

    public interface PriceCallBack {
        void addGoodsListener(double price);

        void subGoodsListener(double price);
    }

}
