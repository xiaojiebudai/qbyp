package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.SelfSupportMakesureOrderActivity;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.bean.ShopProductSku;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.view.SelfSupportGoodsSelectPop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;


/***
 * Description:商品收藏 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-2
 */
public class GoodsCollectAdapter extends FatherAdapter<ShopProduct> {

    public GoodsCollectAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_goods_collect_item,
                    null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_time;
        public TextView tv_name;
        public TextView tv_price;
        public ImageView iv_goods_img;
        public TextView bt_add_cart;

        public ViewHolder(View convertView) {
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            bt_add_cart = (TextView) convertView.findViewById(R.id.bt_add_cart);
            iv_goods_img = (ImageView) convertView.findViewById(R.id.iv_goods_img);
            convertView.setTag(this);
        }

        public void setData(int position) {
            final ShopProduct item = getItem(position);
            ImageUtils.setCommonImage(mContext, item.ImageUrl, iv_goods_img);
            tv_price.setText(WWViewUtil.numberFormatPrice(item.SalePrice));
                WWViewUtil.textInsertDrawable(mContext, tv_name, item.ProductName,false,item.IsVipLevel);
            tv_time.setText(TimeUtil.getTimeToM2(item.CreateTime * 1000) + "");
            bt_add_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.HaveSku) {
                        SelfSupportGoodsSelectPop selfSupportGoodsSelectPop = new SelfSupportGoodsSelectPop(mContext, R.layout.act_collect, item.ProductId);
                        selfSupportGoodsSelectPop.setOkListener(new SelfSupportGoodsSelectPop.OkCallBack() {
                            @Override
                            public void okBuyNowListener(ShopProductSku sp, int num) {
                             buyNow(item, sp, num);
                            }

                            @Override
                            public void okListener(ShopProductSku sp, int num) {
                                addToCart(item, sp, num);
                            }
                        });
                        selfSupportGoodsSelectPop.setOperateView(false,true);
                        selfSupportGoodsSelectPop.showChooseWindow();
                    } else {
                        addToCart(item, null, 1);
                    }
                }
            });
        }
    }

    private void buyNow(ShopProduct item, ShopProductSku selectSku,final int selectNum) {
        if (selectSku != null && selectNum > selectSku.StockQty) {// || selectNum > item.StockQty数据没有该字段为0,无Sku无法判断库存
            WWToast.showShort(R.string.stockQty_less_tips);
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("productId", item.ProductId + "");
        jsonObject.put("quantity", selectNum + "");
        if (selectSku != null) {
            jsonObject.put("skuId", selectSku.SkuId + "");
        }
        RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiShop.CartAtOnce());

        x.http().post(sessionParams, new WWXCallBack("CartAtOnce") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                long[] selectGoodsFlowId= new long[1];
                selectGoodsFlowId[0]=data.getLong("Data");
                excuteOrderPreview(selectGoodsFlowId);
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }
    private void excuteOrderPreview(long[] selectGoodsFlowId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", selectGoodsFlowId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderPreview()),
                new WWXCallBack("OrderPreview") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        String string = data.getString("Data");
                        Intent intent = new Intent(mContext, SelfSupportMakesureOrderActivity.class);
                        intent.putExtra(Consts.KEY_DATA, string);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                    }
                });
    }
    private void addToCart(ShopProduct item, ShopProductSku selectSku, final int selectNum) {
        if (selectSku != null && selectNum > selectSku.StockQty) {// || selectNum > item.StockQty数据没有该字段为0,无Sku无法判断库存
            WWToast.showShort(R.string.stockQty_less_tips);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("productId", item.ProductId + "");
        jsonObject.put("quantity", selectNum + "");
        if (selectSku != null) {
            jsonObject.put("skuId", selectSku.SkuId + "");
        }

        RequestParams sessionParams = ParamsUtils.getPostJsonParams(jsonObject, ApiShop.CartNum());

        x.http().post(sessionParams, new WWXCallBack("CartNum") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                int cartNum = SharedPreferenceUtils.getInstance().getCartNum() + selectNum;
                SharedPreferenceUtils.getInstance().saveCartNum(cartNum);
                //新加成功需要修改本地数据，同时弹窗
                final CommonDialog commonDialog = DialogUtils.getCommonDialog((Activity) mContext, R.string.addto_shoppingcart_success);
                commonDialog.getButtonLeft(R.string.Stay).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.getButtonRight(R.string.to_shopcart).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PublicWay.stratSelfSupportShopCartActivity((Activity) mContext);
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

}

