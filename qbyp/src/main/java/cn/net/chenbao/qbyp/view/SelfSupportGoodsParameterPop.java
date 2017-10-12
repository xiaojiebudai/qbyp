package cn.net.chenbao.qbyp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.adapter.listview.SelfPopParameterAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopProductExtend;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * Created by 木头 on 2016/11/3.
 */

public class SelfSupportGoodsParameterPop{
    private PopupWindow mPopupWindow;
    private View mParent;
    private Context context;
    private View content;
    public SelfSupportGoodsParameterPop(final Context context, int parentId,long productID) {
        // TODO Auto-generated constructor stub
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mParent = inflater.inflate(parentId, null);
        content = inflater.inflate(R.layout.pop_selfsupport_goodsparameter, null);
         content.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 dismissWindow();
             }
         });
        ListView lv_data=(ListView) content.findViewById(R.id.lv_data);
        final SelfPopParameterAdapter selfPopParameterAdapter=new SelfPopParameterAdapter(context);
        lv_data.setAdapter(selfPopParameterAdapter);
        mPopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                (int) (context.getResources()
                .getDisplayMetrics().heightPixels * 0.4), true);

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
        RequestParams params = new RequestParams(ApiShop.ProductExtends());
        params.addBodyParameter("productId",productID+"");
        x.http().get(params,new WWXCallBack("ProductExtends"){
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<ShopProductExtend> list = JSON.parseArray(
                        data.getJSONArray("Data").toJSONString(), ShopProductExtend.class);
                selfPopParameterAdapter.setDatas(list);
            }

            @Override
            public void onAfterFinished() {
            }
        });

    }

    public void showWindow() {
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
}
