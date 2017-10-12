package cn.net.chenbao.qbyp.view;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.bean.ShopProductProAndVal;
import cn.net.chenbao.qbyp.bean.ShopProductProVal;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProductSku;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 木头 on 2016/11/3.
 */

public class SelfSupportGoodsSelectPop implements View.OnClickListener {
    private PopupWindow mPopupWindow;
    private View mParent;
    private Context context;
    private View content;
    private TextView tv_price;
    private TextView tv_skudes;
    private TextView tv_num;
    private ImageView iv_good_img;
    private ShopProductSku sp;
    private int num = 1;

    public SelfSupportGoodsSelectPop(final Context context, int parentId, long productID) {
        // TODO Auto-generated constructor stub
        this.context = context;
        final LayoutInflater inflater = LayoutInflater.from(context);
        mParent = inflater.inflate(parentId, null);
        content = inflater.inflate(R.layout.pop_selfsupport_goodsselect, null);
        content.findViewById(R.id.cancel).setOnClickListener(this);
        content.findViewById(R.id.tv_add_cart).setOnClickListener(this);
        content.findViewById(R.id.tv_buy).setOnClickListener(this);
        content.findViewById(R.id.num_jian).setOnClickListener(this);
        content.findViewById(R.id.num_jia).setOnClickListener(this);
        tv_price = (TextView) content.findViewById(R.id.tv_price);
        tv_skudes = (TextView) content.findViewById(R.id.tv_skudes);
        tv_num = (TextView) content.findViewById(R.id.tv_num);
        iv_good_img = (ImageView) content.findViewById(R.id.iv_good_img);
        final LinearLayout ll_container = (LinearLayout) content.findViewById(R.id.ll_container);
        mPopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (context.getResources()
                        .getDisplayMetrics().heightPixels * 0.6), true);

        // 设置SelectPicPopupWindow弹出窗体可点击
        mPopupWindow.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = ((Activity) context).getWindow()
                        .getAttributes();
                params.alpha = 1.0F;
                ((Activity) context).getWindow().setAttributes(params);
            }
        });

        RequestParams params = new RequestParams(ApiShop.ProductSkus());
        params.addBodyParameter("productId", productID + "");
        x.http().get(params, new WWXCallBack("ProductSkus") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                shopProductSkuList = JSON.parseArray(
                        data.getJSONArray("Data").toJSONString(), ShopProductSku.class);
                shopProductProAndValList = JSON.parseArray(
                        data.getJSONArray("SkuShow").toJSONString(), ShopProductProAndVal.class);
                if (shopProductSkuList != null && shopProductSkuList.size() > 0) {
                    tv_price.setText("");
                    tv_skudes.setText(context.getString(R.string.please_choice_goods_sku));
                    ImageUtils.setCommonImage(context, shopProductSkuList.get(0).ImageUrl, iv_good_img);
                    sp = shopProductSkuList.get(0);
                }
                if (shopProductProAndValList != null && shopProductProAndValList.size() > 0) {

                    for (int i = 0; i < shopProductProAndValList.size(); i++) {
                        View view = inflater.inflate(
                                R.layout.self_support_goods_select_item, null);
                        AutoRankView mAv = (AutoRankView) view.findViewById(R.id.av);
                        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                        tv_title.setText(shopProductProAndValList.get(i).ProName);
                        List<View> list = new ArrayList<View>();
                        for (int j = 0; j < shopProductProAndValList.get(i).Values.size(); j++) {

                            final TextView viewitem = (TextView) View.inflate(context,
                                    R.layout.textview_hot_search, null);
                            ShopProductProVal item = shopProductProAndValList.get(i).Values.get(j);
                            viewitem.setText(item.ValName);
                            viewitem.setTag(i);
                            viewitem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!viewitem.isSelected()) {
                                        viewitem.setSelected(true);
                                        setTextSelectState(viewitem, (Integer) viewitem.getTag());
                                    }
                                }
                            });
                            list.add(viewitem);
                            map.put(i, list);
                            mAv.addView(viewitem);
                        }
                        ll_container.addView(view);
                    }

                }
            }

            @Override
            public void onAfterFinished() {

            }
        });


    }

    HashMap<Integer, List<View>> map = new HashMap<Integer, List<View>>();
    HashMap<Integer, ArrayList<Integer>> mapSelect = new HashMap<Integer, ArrayList<Integer>>();

    private List<ShopProductSku> shopProductSkuList;
    private List<ShopProductProAndVal> shopProductProAndValList;
    int selectPos;

    private boolean isSelectOver;
    private ArrayList<ShopProductProVal> selectValId = new ArrayList<ShopProductProVal>();

    public void setTextSelectState(View v, int pos) {

        List<View> item = map.get(pos);
        for (int i = 0; i < item.size(); i++) {
            if (item.get(i) != v) {
                item.get(i).setSelected(false);
            } else {
                selectPos = i;
            }
        }
        mapSelect.clear();
        if (pos < map.size()) {
            for (int i = pos + 1; i < map.size(); i++) {
                List<View> itemOther = map.get(i);
                ArrayList<Integer> selectId = new ArrayList<Integer>();
                for (int k = 0; k < shopProductSkuList.size(); k++) {
                    if (isOk(shopProductSkuList.get(k).ProId, pos)) {
                        for (int j = 0; j < itemOther.size(); j++) {
                            if (shopProductSkuList.get(k).ProId.contains(shopProductProAndValList.get(i).Values.get(j).ProId + ":" + shopProductProAndValList.get(i).Values.get(j).ValId)) {
                                if (!selectId.contains(j)) {
                                    selectId.add(j);
                                }
                            }
                        }

                    }
                }
                mapSelect.put(i, selectId);
            }
        }

        for (int i = pos + 1; i < map.size(); i++) {
            List<View> itemOther = map.get(i);
            for (int j = 0; j < itemOther.size(); j++) {
                itemOther.get(j).setSelected(false);
                if (mapSelect.size() > 0 && mapSelect.get(i).contains(j)) {
                    itemOther.get(j).setClickable(true);
                    ((TextView) itemOther.get(j)).setTextColor(context.getResources().getColorStateList(
                            R.color.text_home_tab));
                } else {
                    itemOther.get(j).setClickable(false);
                    ((TextView) itemOther.get(j)).setTextColor(context.getResources().getColor(R.color.text_gray_s));
                }
            }


        }

        selectValId.clear();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).isSelected()) {
                    selectValId.add(shopProductProAndValList.get(i).Values.get(j));
                }
            }
        }
        isSelectOver = false;
        if (selectValId.size() == map.size()) {
            //选择完成
            for (int j = 0; j < shopProductSkuList.size(); j++) {
                for (int i = 0; i < selectValId.size(); i++) {
                    if (shopProductSkuList.get(j).ProId.contains(selectValId.get(i).ProId + ":" + selectValId.get(i).ValId)) {
                        if (i == selectValId.size() - 1) {
                            sp = shopProductSkuList.get(j);
                            isSelectOver = true;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        if (isSelectOver) {
            tv_price.setText(WWViewUtil.numberFormatPrice(sp.SalePrice));
            tv_skudes.setText(String.format(context.getString(R.string.have_choice_sku), sp.ProName));
            ImageUtils.setCommonImage(context, sp.ImageUrl, iv_good_img);
        } else {
            tv_price.setText("");
            tv_skudes.setText(context.getString(R.string.please_choice_goods_sku));
            ImageUtils.setCommonImage(context, sp.ImageUrl, iv_good_img);
        }


    }

    private boolean isOk(String proId, int pos) {
        String s = "";
        for (int i = 0; i < pos + 1; i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).isSelected()) {
                    if (i == 0) {
                        s += shopProductProAndValList.get(i).Values.get(j).ProId + ":" + shopProductProAndValList.get(i).Values.get(j).ValId;
                    } else {
                        s += ";" + shopProductProAndValList.get(i).Values.get(j).ProId + ":" + shopProductProAndValList.get(i).Values.get(j).ValId;
                    }

                }
            }
        }
        return proId.contains(s);
    }

    public void showChooseWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
            WindowManager.LayoutParams params = ((Activity) context).getWindow()
                    .getAttributes();
            params.alpha = 0.6F;
            ((Activity) context).getWindow().setAttributes(params);
        }
    }

    public Boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismissWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public void setOperateView(boolean isBuyNow, boolean isAddCart) {
        if (isBuyNow) {
            content.findViewById(R.id.tv_buy).setVisibility(View.VISIBLE);
        } else {
            content.findViewById(R.id.tv_buy).setVisibility(View.GONE);
        }
        if (isAddCart) {
            content.findViewById(R.id.tv_add_cart).setVisibility(View.VISIBLE);
        } else {
            content.findViewById(R.id.tv_add_cart).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismissWindow();
                break;
            case R.id.num_jia:

                if (sp != null && num < sp.StockQty) {
                    num++;
                    tv_num.setText(num + "");
                }

                break;
            case R.id.num_jian:
                if (num > 1) {
                    num--;
                    tv_num.setText(num + "");
                }
                break;
            case R.id.tv_add_cart:
                if (okListener != null) {
                    if (isSelectOver) {
                        okListener.okListener(sp, num);
                        dismissWindow();
                    } else {
                        WWToast.showShort(R.string.good_des_select_tips);
                    }

                }
                break;
            case R.id.tv_buy:
                if (okListener != null) {
                    if (isSelectOver) {
                        okListener.okBuyNowListener(sp, num);
                        dismissWindow();
                    } else {
                        WWToast.showShort(R.string.good_des_select_tips);
                    }

                }
                break;
        }
    }

    private OkCallBack okListener;

    public void setOkListener(OkCallBack listener) {
        this.okListener = listener;
    }

    public interface OkCallBack {
        void okListener(ShopProductSku sp, int num);

        void okBuyNowListener(ShopProductSku sp, int num);
    }
}

