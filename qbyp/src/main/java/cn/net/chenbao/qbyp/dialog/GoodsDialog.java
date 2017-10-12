package cn.net.chenbao.qbyp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.common.util.DensityUtil;
import org.xutils.x;

/**
 * 商品弹窗
 *
 * @author xl
 * @date 2016-7-27 下午11:19:10
 * @description
 */
public class GoodsDialog extends Dialog implements
        android.view.View.OnClickListener {

    private ImageView gooodsImage;
    private TextView name;
    private TextView state;
    private TextView mSellerNum;
    private TextView mMoney;
    private final Goods goods;
    private final Context context;
    private int buyNum = 1;
    private TextView count;
    private int kucun;
    private TextView mDescription;
    private NumAndPriceCallBack numAndPriceListener;

    public NumAndPriceCallBack getNumAndPriceListener() {
        return numAndPriceListener;
    }

    public void setNumAndPriceListener(NumAndPriceCallBack numAndPriceListener) {
        this.numAndPriceListener = numAndPriceListener;
    }

    public GoodsDialog(Context context, Goods goods) {
        super(context, R.style.DialogStyle);
        this.goods = goods;
        this.context = context;
        setContentView(R.layout.dialog_goods);
        getWindow().getAttributes().width = (int) (DensityUtil.getScreenWidth() * 0.9F);
        initView();
    }

    private void initView() {
        gooodsImage = (ImageView) findViewById(R.id.iv_image);
        LayoutParams layoutParams = gooodsImage.getLayoutParams();
        layoutParams.width = (int) (DensityUtil.getScreenWidth() * 0.9F);
        layoutParams.height = (int) (DensityUtil.getScreenWidth() * 0.9F);
        gooodsImage.setLayoutParams(layoutParams);
        name = (TextView) findViewById(R.id.tv_name);
        state = (TextView) findViewById(R.id.tv_state);
        count = (TextView) findViewById(R.id.tv_count);
        mDescription = (TextView) findViewById(R.id.tv_description);
        mSellerNum = (TextView) findViewById(R.id.tv_seller_num);
        mMoney = (TextView) findViewById(R.id.tv_money);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ll_jian).setOnClickListener(this);
        findViewById(R.id.ll_jia).setOnClickListener(this);
        setDataToUi();
    }

    private void setDataToUi() {
        kucun = goods.StockQty;
        ImageUtils.setCommonImage(context, goods.GoodsImg, gooodsImage);
        name.setText(goods.GoodsName);
        mDescription.setText(goods.Describe);
        state.setText(goods.StockQty > 0 ? R.string.have_good : R.string.no_good);
        mSellerNum.setText(context.getResources().getString(
                R.string.seller_num_with_colon)
                + goods.SaleQty);
        mMoney.setText(WWViewUtil.numberFormatPrice(goods.Price));
        buyNum = goods.CartNum;
        count.setText(buyNum + "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.ll_jian:
                if (buyNum == 0) {
                    WWToast.showShort(R.string.buynum_dayu_ling);
                    return;
                }
                addOrSubCart(false);
                break;
            case R.id.ll_jia:
                if (buyNum >= kucun) {
                    WWToast.showShort(R.string.buynum_dayu_kucun);
                    return;
                }
                addOrSubCart(true);
                break;

            default:
                break;
        }
    }

    /**
     * 商品加减请求
     *
     * @param isAdd
     */
    public void addOrSubCart(final boolean isAdd) {
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
        // x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url),
        // new WWHttpCallBack() {
        //
        // @Override
        // public void onSuccessOk(String result) {
        // if (isAdd) {
        // JSONObject jsonObject2 = JSON.parseObject(result)
        // .getJSONObject("CartAddResult");
        // numAndPriceListener.addListener(goods.Price);
        // if (JsonUtils.parseJsonSuccess(jsonObject2)) {
        // buyNum++;
        // WWToast.showShort(R.string.add_cart_success);
        // count.setText(buyNum + "");
        // } else {
        // WWToast.showShort(JsonUtils
        // .parserJsonMessage(jsonObject2));
        // }
        // } else {
        // JSONObject jsonObject2 = JSON.parseObject(result)
        // .getJSONObject("CartSubResult");
        // numAndPriceListener.subListener(goods.Price);
        // if (JsonUtils.parseJsonSuccess(jsonObject2)) {
        // if (buyNum != 0) {
        // buyNum--;
        // }
        // WWToast.showShort(R.string.remove_cart_success);
        // count.setText(buyNum + "");
        // } else {
        // WWToast.showShort(JsonUtils
        // .parserJsonMessage(jsonObject2));
        // }
        // }
        // }
        //
        // @Override
        // public void onAfterFinished() {
        //
        // }
        // });

        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url),
                new WWXCallBack(result) {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (isAdd) {
                            numAndPriceListener.addListener(goods.Price);
                            buyNum++;
                            WWToast.showShort(R.string.add_cart_success);
                            count.setText(buyNum + "");
                        } else {
                            numAndPriceListener.subListener(goods.Price);
                            if (buyNum != 0) {
                                buyNum--;
                            }
                            WWToast.showShort(R.string.remove_cart_success);
                            count.setText(buyNum + "");
                        }
                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });

    }

    public interface NumAndPriceCallBack {

        void addListener(double price);

        void subListener(double price);

    }

}
